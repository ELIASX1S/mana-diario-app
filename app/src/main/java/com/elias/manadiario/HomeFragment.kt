package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import java.util.*

class HomeFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var listenButton: ImageButton

    private var fraseAtual: String = ""
    private lateinit var versiculoPrincipalTextView: TextView
    private lateinit var versiculoComplementarTextView: TextView
    private lateinit var reflexaoTextView: TextView
    private lateinit var favoriteButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        val shareButton: ImageButton = view.findViewById(R.id.shareButton)
        favoriteButton = view.findViewById(R.id.favoriteButton)

        listenButton = view.findViewById(R.id.listenButton)
        listenButton.isEnabled = false

        carregarDevocionalDoDia()

        atualizarIconeFavorito()

        listenButton.setOnClickListener {
            tts.stop()
            falarTexto()
        }

        favoriteButton.setOnClickListener {
            val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val favoritos = preferencias.getStringSet("frases_favoritas", mutableSetOf())?.toMutableSet()

            if (favoritos != null) {
                if (favoritos.contains(fraseAtual)) {
                    favoritos.remove(fraseAtual)
                } else {
                    favoritos.add(fraseAtual)
                }
                preferencias.edit().putStringSet("frases_favoritas", favoritos).apply()
                atualizarIconeFavorito()
            }
        }

        shareButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, fraseAtual)
            }
            startActivity(Intent.createChooser(intent, "Compartilhar Devocional Via"))
        }
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

        // Evita repetir se os dois vierem iguais
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

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("pt", "BR"))
            listenButton.isEnabled =
                result != TextToSpeech.LANG_MISSING_DATA &&
                        result != TextToSpeech.LANG_NOT_SUPPORTED
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
                versiculoComplementarTextView.textSize = 18f
                reflexaoTextView.textSize = 16f
            }
            "grande" -> {
                versiculoPrincipalTextView.textSize = 26f
                versiculoComplementarTextView.textSize = 22f
                reflexaoTextView.textSize = 20f
            }
        }
    }

    private fun atualizarIconeFavorito() {
        val preferencias = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val favoritos = preferencias.getStringSet("frases_favoritas", setOf())

        if (favoritos != null && favoritos.contains(fraseAtual)) {
            favoriteButton.setImageResource(R.drawable.ic_star_filled)
        } else {
            favoriteButton.setImageResource(R.drawable.ic_star_border)
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