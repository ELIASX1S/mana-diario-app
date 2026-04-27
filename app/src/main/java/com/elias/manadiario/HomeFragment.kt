package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(), TextToSpeech.OnInitListener {

    companion object {
        private const val KEY_LAST_ACCESS = "home_last_access"
        private const val KEY_STREAK_DAYS = "home_streak_days"
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
    }

    private lateinit var tts: TextToSpeech
    private lateinit var listenAction: View

    private var fraseAtual: String = ""
    private lateinit var versiculoPrincipalTextView: TextView
    private lateinit var versiculoComplementarTextView: TextView
    private lateinit var reflexaoTextView: TextView
    private lateinit var favoriteIcon: ImageView
    private lateinit var greetingTextView: TextView
    private lateinit var sequenceTextView: TextView
    private lateinit var lastAccessTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tts = TextToSpeech(requireContext(), this)

        val toolbar: Toolbar = view.findViewById(R.id.home_toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        versiculoPrincipalTextView = view.findViewById(R.id.versiculoPrincipalTextView)
        versiculoComplementarTextView = view.findViewById(R.id.versiculoComplementarTextView)
        reflexaoTextView = view.findViewById(R.id.reflexaoTextView)
        favoriteIcon = view.findViewById(R.id.favoriteIcon)
        greetingTextView = view.findViewById(R.id.greetingTextView)
        sequenceTextView = view.findViewById(R.id.sequenceTextView)
        lastAccessTextView = view.findViewById(R.id.lastAccessTextView)

        val favoriteAction: View = view.findViewById(R.id.favoriteAction)
        val shareAction: View = view.findViewById(R.id.shareAction)
        val copyAction: View = view.findViewById(R.id.copyAction)
        listenAction = view.findViewById(R.id.listenAction)
        val readReflectionButton: MaterialButton = view.findViewById(R.id.readReflectionButton)

        carregarDevocionalDoDia()
        atualizarHomeEmocional()
        atualizarIconeFavorito()

        listenAction.isEnabled = false
        listenAction.alpha = 0.5f

        listenAction.setOnClickListener {
            tts.stop()
            falarTexto()
        }

        favoriteAction.setOnClickListener {
            toggleFavorito()
        }

        shareAction.setOnClickListener {
            compartilharDevocional()
        }

        copyAction.setOnClickListener {
            copiarDevocional()
        }

        readReflectionButton.setOnClickListener {
            Toast.makeText(requireContext(), getString(R.string.home_open_reflection_toast), Toast.LENGTH_SHORT).show()
            reflexaoTextView.requestFocus()
        }

        view.findViewById<View>(R.id.journeyShortcut).setOnClickListener { abrirJornadaDevocional() }
        view.findViewById<View>(R.id.bibleShortcut).setOnClickListener { startActivity(Intent(requireContext(), BibleBooksActivity::class.java)) }
        view.findViewById<View>(R.id.worshipShortcut).setOnClickListener { abrirLouvor() }
        view.findViewById<View>(R.id.notesShortcut).setOnClickListener { startActivity(Intent(requireContext(), BibleNotesActivity::class.java)) }
        view.findViewById<View>(R.id.favoritesShortcut).setOnClickListener { startActivity(Intent(requireContext(), FavoritesActivity::class.java)) }
    }

    private fun carregarDevocionalDoDia() {
        val versiculosPrincipais = resources.getStringArray(R.array.versiculos_principais)
        val versiculosComplementares = resources.getStringArray(R.array.versiculos_complementares)
        val reflexoes = resources.getStringArray(R.array.reflexoes)

        val calendario = Calendar.getInstance()
        val diaDoAno = calendario.get(Calendar.DAY_OF_YEAR)
        val indice = diaDoAno % versiculosPrincipais.size

        val vPrincipal = versiculosPrincipais[indice].trim()
        val vComplementar = versiculosComplementares[indice].trim()
        val reflexao = reflexoes[indice].trim()

        versiculoPrincipalTextView.text = vPrincipal

        if (vComplementar.equals(vPrincipal, ignoreCase = true)) {
            versiculoComplementarTextView.text = ""
        } else {
            versiculoComplementarTextView.text = vComplementar
        }

        reflexaoTextView.text = reflexao

        fraseAtual = if (vComplementar.equals(vPrincipal, ignoreCase = true)) {
            "$vPrincipal\n\n$reflexao"
        } else {
            "$vPrincipal\n\n$vComplementar\n\n$reflexao"
        }
    }

    private fun atualizarHomeEmocional() {
        greetingTextView.text = obterSaudacao()

        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val now = System.currentTimeMillis()
        val todayStart = startOfDay(now)
        val lastAccess = preferencias.getLong(KEY_LAST_ACCESS, 0L)
        val lastAccessStart = startOfDay(lastAccess)
        var streakDays = preferencias.getInt(KEY_STREAK_DAYS, 0)

        streakDays = when {
            lastAccess == 0L -> 1
            todayStart == lastAccessStart -> streakDays.coerceAtLeast(1)
            todayStart - lastAccessStart == MILLIS_IN_DAY -> streakDays + 1
            else -> 1
        }

        preferencias.edit()
            .putLong(KEY_LAST_ACCESS, now)
            .putInt(KEY_STREAK_DAYS, streakDays)
            .apply()

        sequenceTextView.text = if (streakDays > 0) {
            getString(R.string.home_daily_sequence_message, streakDays)
        } else {
            getString(R.string.home_daily_sequence_fallback)
        }

        val dataUltimoAcesso = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(now))
        lastAccessTextView.text = getString(R.string.home_daily_sequence_last_access, dataUltimoAcesso)
    }

    private fun obterSaudacao(): String {
        val nome = getString(R.string.home_username_default)
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> getString(R.string.home_greeting_morning, nome)
            in 12..17 -> getString(R.string.home_greeting_afternoon, nome)
            else -> getString(R.string.home_greeting_evening)
        }
    }

    private fun startOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    private fun toggleFavorito() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val favoritos = preferencias.getStringSet("frases_favoritas", mutableSetOf())?.toMutableSet() ?: mutableSetOf()

        val foiRemovido = if (favoritos.contains(fraseAtual)) {
            favoritos.remove(fraseAtual)
            true
        } else {
            favoritos.add(fraseAtual)
            false
        }

        preferencias.edit().putStringSet("frases_favoritas", favoritos).apply()
        atualizarIconeFavorito()

        val mensagem = if (foiRemovido) R.string.home_favorite_removed else R.string.home_favorite_added
        Toast.makeText(requireContext(), getString(mensagem), Toast.LENGTH_SHORT).show()
    }

    private fun compartilharDevocional() {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, fraseAtual)
        }
        startActivity(Intent.createChooser(intent, "Compartilhar Devocional Via"))
    }

    private fun copiarDevocional() {
        val clipboardManager = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.clip_label_verse), fraseAtual)
        clipboardManager.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.toast_verse_copied), Toast.LENGTH_SHORT).show()
    }

    private fun abrirJornadaDevocional() {
        val journeyManager = JourneyManager(requireContext())
        val intent = if (journeyManager.hasActivePlan()) {
            Intent(requireContext(), JourneyActivity::class.java)
        } else {
            Intent(requireContext(), ChoosePlanActivity::class.java)
        }
        startActivity(intent)
    }

    private fun abrirLouvor() {
        (activity as? AppCompatActivity)?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container, LouvorFragment())
            ?.commit()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("pt", "BR"))
            val enabled = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            listenAction.isEnabled = enabled
            listenAction.alpha = if (enabled) 1f else 0.5f
        }
    }

    private fun falarTexto() {
        var textoParaFalar = fraseAtual

        textoParaFalar = textoParaFalar.replace(":", " versículo ")
        textoParaFalar = textoParaFalar.replace("1 Samuel", "Primeiro Samuel")
        textoParaFalar = textoParaFalar.replace("2 Samuel", "Segundo Samuel")
        textoParaFalar = textoParaFalar.replace("1 Reis", "Primeiro Reis")
        textoParaFalar = textoParaFalar.replace("2 Reis", "Segundo Reis")
        textoParaFalar = textoParaFalar.replace("1 Crônicas", "Primeiro Crônicas")
        textoParaFalar = textoParaFalar.replace("2 Crônicas", "Segundo Crônicas")
        textoParaFalar = textoParaFalar.replace("1 Coríntios", "Primeira Coríntios")
        textoParaFalar = textoParaFalar.replace("2 Coríntios", "Segunda Coríntios")
        textoParaFalar = textoParaFalar.replace("1 Tessalonicenses", "Primeira Tessalonicenses")
        textoParaFalar = textoParaFalar.replace("2 Tessalonicenses", "Segunda Tessalonicenses")
        textoParaFalar = textoParaFalar.replace("1 Timóteo", "Primeira Timóteo")
        textoParaFalar = textoParaFalar.replace("2 Timóteo", "Segunda Timóteo")
        textoParaFalar = textoParaFalar.replace("1 Pedro", "Primeira Pedro")
        textoParaFalar = textoParaFalar.replace("2 Pedro", "Segunda Pedro")
        textoParaFalar = textoParaFalar.replace("1 João", "Primeira João")
        textoParaFalar = textoParaFalar.replace("2 João", "Segunda João")
        textoParaFalar = textoParaFalar.replace("3 João", "Terceira João")

        tts.speak(textoParaFalar, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        aplicarTamanhoDaFonte()
        atualizarIconeFavorito()
    }

    private fun aplicarTamanhoDaFonte() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val tamanhoSalvo = preferencias.getString("tamanho_fonte", "medio")

        when (tamanhoSalvo) {
            "pequeno" -> {
                versiculoPrincipalTextView.textSize = 18f
                versiculoComplementarTextView.textSize = 14f
                reflexaoTextView.textSize = 12f
            }
            "medio" -> {
                versiculoPrincipalTextView.textSize = 22f
                versiculoComplementarTextView.textSize = 16f
                reflexaoTextView.textSize = 14f
            }
            "grande" -> {
                versiculoPrincipalTextView.textSize = 26f
                versiculoComplementarTextView.textSize = 20f
                reflexaoTextView.textSize = 18f
            }
        }
    }

    private fun atualizarIconeFavorito() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val favoritos = preferencias.getStringSet("frases_favoritas", setOf())

        if (favoritos != null && favoritos.contains(fraseAtual)) {
            favoriteIcon.setImageResource(R.drawable.ic_star_filled)
        } else {
            favoriteIcon.setImageResource(R.drawable.ic_star_border)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}
