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
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.play.core.review.ReviewManagerFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment(), TextToSpeech.OnInitListener {

    companion object {
        private const val KEY_LAST_ACCESS = "home_last_access"
        private const val KEY_STREAK_DAYS = "home_streak_days"
        private const val KEY_FAITH_PLAN_DAY = "home_faith_plan_day"
        private const val KEY_FAITH_PLAN_LAST_COMPLETED_DATE = "home_faith_plan_last_completed_date"
        private const val KEY_FAITH_PLAN_COMPLETED = "home_faith_plan_completed"
        private const val FAITH_PLAN_TOTAL_DAYS = 7
        private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L
        private const val KEY_REVIEW_REQUESTED = "home_review_requested"
        private const val REVIEW_STREAK_THRESHOLD = 7
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
    private lateinit var streakMotivationTextView: TextView
    private lateinit var faithPlanDayTextView: TextView
    private lateinit var faithPlanVerseTextView: TextView
    private lateinit var faithPlanReflectionTextView: TextView
    private lateinit var faithPlanPrayerTextView: TextView
    private lateinit var faithPlanProgressTextView: TextView
    private lateinit var faithPlanProgressIndicator: LinearProgressIndicator
    private lateinit var completeFaithPlanDayButton: MaterialButton
    private var isReflectionExpanded: Boolean = false
    private var prayerAtualPlano: String = ""

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
        streakMotivationTextView = view.findViewById(R.id.streakMotivationTextView)
        faithPlanDayTextView = view.findViewById(R.id.faithPlanDayTextView)
        faithPlanVerseTextView = view.findViewById(R.id.faithPlanVerseTextView)
        faithPlanReflectionTextView = view.findViewById(R.id.faithPlanReflectionTextView)
        faithPlanPrayerTextView = view.findViewById(R.id.faithPlanPrayerTextView)
        faithPlanProgressTextView = view.findViewById(R.id.faithPlanProgressTextView)
        faithPlanProgressIndicator = view.findViewById(R.id.faithPlanProgressIndicator)

        val favoriteAction: View = view.findViewById(R.id.favoriteAction)
        val shareAction: View = view.findViewById(R.id.shareAction)
        val copyAction: View = view.findViewById(R.id.copyAction)
        listenAction = view.findViewById(R.id.listenAction)
        val readReflectionButton: MaterialButton = view.findViewById(R.id.readReflectionButton)
        completeFaithPlanDayButton = view.findViewById(R.id.completeFaithPlanDayButton)
        val shareFaithPlanPrayerButton: MaterialButton = view.findViewById(R.id.shareFaithPlanPrayerButton)

        carregarDevocionalDoDia()
        atualizarHomeEmocional()
        atualizarIconeFavorito()
        configurarBotaoReflexao(readReflectionButton)
        atualizarPlano7Dias()

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
        completeFaithPlanDayButton.setOnClickListener {
            val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val jornadaConcluida = preferencias.getBoolean(KEY_FAITH_PLAN_COMPLETED, false)
            if (jornadaConcluida) {
                recomecarJornada7Dias()
                Toast.makeText(requireContext(), getString(R.string.home_faith_plan_restarted), Toast.LENGTH_SHORT).show()
            } else {
                concluirDiaPlano7Dias()
            }
        }
        shareFaithPlanPrayerButton.setOnClickListener {
            compartilharOracaoDoPlano()
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
            getMensagemConquistaStreak(streakDays)
        } else {
            getString(R.string.home_daily_sequence_fallback)
        }

        streakMotivationTextView.text = when {
            streakDays <= 1 -> getString(R.string.home_daily_motivation_start)
            streakDays <= 3 -> getString(R.string.home_daily_motivation_short)
            streakDays <= 7 -> getString(R.string.home_daily_motivation_medium)
            else -> getString(R.string.home_daily_motivation_long, streakDays)
        }

        val dataUltimoAcesso = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(now))
        lastAccessTextView.text = getString(R.string.home_daily_sequence_last_access, dataUltimoAcesso)

        maybeRequestInAppReview(streakDays)
    }

    private fun getMensagemConquistaStreak(streakDays: Int): String {
        return when {
            streakDays >= 30 -> getString(R.string.home_streak_badge_30, streakDays)
            streakDays >= 7 -> getString(R.string.home_streak_badge_7, streakDays)
            else -> getString(R.string.home_streak_badge_3, streakDays)
        }
    }

    private fun atualizarPlano7Dias() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val diaAtual = preferencias.getInt(KEY_FAITH_PLAN_DAY, 1).coerceIn(1, FAITH_PLAN_TOTAL_DAYS)
        val jornadaConcluida = preferencias.getBoolean(KEY_FAITH_PLAN_COMPLETED, false)
        val indice = diaAtual - 1

        val temas = resources.getStringArray(R.array.home_faith_plan_themes)
        val versiculos = resources.getStringArray(R.array.home_faith_plan_verses)
        val reflexoes = resources.getStringArray(R.array.home_faith_plan_reflections)
        val oracoes = resources.getStringArray(R.array.home_faith_plan_prayers)

        faithPlanDayTextView.text = if (jornadaConcluida) {
            getString(R.string.home_faith_plan_completed_status)
        } else {
            getString(R.string.home_faith_plan_day_format, diaAtual, FAITH_PLAN_TOTAL_DAYS, temas[indice])
        }
        if (jornadaConcluida) {
            faithPlanVerseTextView.text = getString(R.string.home_faith_plan_completed_final_verse)
            faithPlanReflectionTextView.text = getString(R.string.home_faith_plan_completed_final_message)
            prayerAtualPlano = getString(R.string.home_faith_plan_completed_final_prayer)
            faithPlanPrayerTextView.text = getString(R.string.home_prayer_of_day_prefix, prayerAtualPlano)
            completeFaithPlanDayButton.text = getString(R.string.home_faith_plan_start_again)
        } else {
            faithPlanVerseTextView.text = versiculos[indice]
            faithPlanReflectionTextView.text = reflexoes[indice]
            prayerAtualPlano = oracoes[indice]
            faithPlanPrayerTextView.text = getString(R.string.home_prayer_of_day_prefix, prayerAtualPlano)
            completeFaithPlanDayButton.text = getString(R.string.home_faith_plan_complete_day)
        }
        val completedDays = if (jornadaConcluida) FAITH_PLAN_TOTAL_DAYS else (diaAtual - 1)
        val progressPercent = ((completedDays.toFloat() / FAITH_PLAN_TOTAL_DAYS) * 100).toInt().coerceIn(0, 100)
        faithPlanProgressIndicator.setProgressCompat(progressPercent, true)

        faithPlanProgressTextView.text = if (jornadaConcluida) {
            getString(R.string.home_faith_plan_progress_completed)
        } else {
            getString(R.string.home_faith_plan_progress_format, completedDays, FAITH_PLAN_TOTAL_DAYS)
        }
    }

    private fun concluirDiaPlano7Dias() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val diaAtual = preferencias.getInt(KEY_FAITH_PLAN_DAY, 1).coerceIn(1, FAITH_PLAN_TOTAL_DAYS)
        val hoje = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val ultimaConclusao = preferencias.getString(KEY_FAITH_PLAN_LAST_COMPLETED_DATE, null)

        if (ultimaConclusao == hoje) {
            Toast.makeText(requireContext(), getString(R.string.home_faith_plan_already_completed_today), Toast.LENGTH_SHORT).show()
            return
        }

        if (diaAtual >= FAITH_PLAN_TOTAL_DAYS) {
            preferencias.edit()
                .putString(KEY_FAITH_PLAN_LAST_COMPLETED_DATE, hoje)
                .putBoolean(KEY_FAITH_PLAN_COMPLETED, true)
                .apply()
            mostrarDialogoConclusaoJornada()
            atualizarPlano7Dias()
            maybeRequestInAppReview(getIntSafe(preferencias, KEY_STREAK_DAYS))
            return
        }

        val proximoDia = if (diaAtual >= FAITH_PLAN_TOTAL_DAYS) 1 else diaAtual + 1

        preferencias.edit()
            .putString(KEY_FAITH_PLAN_LAST_COMPLETED_DATE, hoje)
            .putInt(KEY_FAITH_PLAN_DAY, proximoDia)
            .putBoolean(KEY_FAITH_PLAN_COMPLETED, false)
            .apply()
        atualizarPlano7Dias()
        Toast.makeText(requireContext(), getString(R.string.home_faith_plan_day_completed), Toast.LENGTH_SHORT).show()
    }

    private fun mostrarDialogoConclusaoJornada() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.home_faith_plan_completed_title))
            .setMessage(getString(R.string.home_faith_plan_completed_message))
            .setPositiveButton(getString(R.string.home_faith_plan_start_again)) { dialog, _ ->
                recomecarJornada7Dias()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.home_faith_plan_share_achievement)) { dialog, _ ->
                compartilharConquistaJornada()
                dialog.dismiss()
            }
            .show()
    }

    private fun recomecarJornada7Dias() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        preferencias.edit()
            .putInt(KEY_FAITH_PLAN_DAY, 1)
            .putBoolean(KEY_FAITH_PLAN_COMPLETED, false)
            .apply()
        atualizarPlano7Dias()
    }

    private fun compartilharConquistaJornada() {
        val mensagem = getString(R.string.home_faith_plan_achievement_share_text)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mensagem)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_devotional_chooser)))
    }

    private fun compartilharOracaoDoPlano() {
        val mensagem = getString(R.string.home_faith_plan_prayer_share_text, prayerAtualPlano)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, mensagem)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_devotional_chooser)))
    }

    private fun configurarBotaoReflexao(button: MaterialButton) {
        isReflectionExpanded = false
        reflexaoTextView.maxLines = 3
        button.text = getString(R.string.home_read_reflection)

        button.setOnClickListener {
            isReflectionExpanded = !isReflectionExpanded
            reflexaoTextView.maxLines = if (isReflectionExpanded) Int.MAX_VALUE else 3
            button.text = if (isReflectionExpanded) {
                getString(R.string.home_hide_reflection)
            } else {
                getString(R.string.home_read_reflection)
            }
        }
    }

    private fun obterSaudacao(): String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 5..11 -> "Bom dia"
            in 12..17 -> "Boa tarde"
            else -> "Que Deus fale com você hoje"
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
        startActivity(Intent.createChooser(intent, getString(R.string.share_devotional_chooser)))
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


    private fun maybeRequestInAppReview(streakDays: Int) {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val alreadyRequested = preferencias.getBoolean(KEY_REVIEW_REQUESTED, false)

        if (alreadyRequested || streakDays < REVIEW_STREAK_THRESHOLD) return

        val reviewManager = ReviewManagerFactory.create(requireContext())
        reviewManager.requestReviewFlow().addOnCompleteListener { requestTask ->
            if (!isAdded || !requestTask.isSuccessful) return@addOnCompleteListener

            val reviewInfo = requestTask.result
            reviewManager.launchReviewFlow(requireActivity(), reviewInfo).addOnCompleteListener {
                if (!isAdded) return@addOnCompleteListener
                preferencias.edit().putBoolean(KEY_REVIEW_REQUESTED, true).apply()
            }
        }
    }

    private fun getIntSafe(preferencias: android.content.SharedPreferences, key: String): Int {
        return preferencias.getInt(key, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
    }
}
