package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import kotlin.random.Random

class CaixinhaFragment : Fragment() {

    private lateinit var adViewCaixinha: AdView

    // Lista de Promessas (Texto e Referência)
    private val promessas = listOf(
        Pair("O Senhor é o meu pastor, nada me faltará.", "Salmos 23:1"),
        Pair("Tudo posso naquele que me fortalece.", "Filipenses 4:13"),
        Pair("Mil cairão ao teu lado, e dez mil à tua direita, mas não chegará a ti.", "Salmos 91:7"),
        Pair("Porque sou eu que conheço os planos que tenho para vocês, diz o Senhor, planos de fazê-los prosperar e não de causar dano, planos de dar a vocês esperança e um futuro.", "Jeremias 29:11"),
        Pair("Entregue o seu caminho ao Senhor; confie nele, e ele agirá.", "Salmos 37:5"),
        Pair("Não fui eu que ordenei a você? Seja forte e corajoso! Não se apavore nem desanime, pois o Senhor, o seu Deus, estará com você por onde você andar.", "Josué 1:9"),
        Pair("Venham a mim, todos os que estão cansados e sobrecarregados, e eu darei descanso a vocês.", "Mateus 11:28"),
        Pair("Mas os que esperam no Senhor renovarão as forças, subirão com asas como águias; correrão, e não se cansarão; caminharão, e não se fatigarão.", "Isaías 40:31"),
        Pair("Deem graças ao Senhor, porque ele é bom. O seu amor dura para sempre!", "Salmos 136:1"),
        Pair("Busquem, pois, em primeiro lugar o Reino de Deus e a sua justiça, e todas essas coisas lhes serão acrescentadas.", "Mateus 6:33")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_caixinha, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imgCaixa = view.findViewById<ImageView>(R.id.imgCaixa)
        val cardPromessa = view.findViewById<CardView>(R.id.cardPromessa)
        val tvTexto = view.findViewById<TextView>(R.id.tvVersiculoTexto)
        val tvRef = view.findViewById<TextView>(R.id.tvVersiculoRef)
        val btnPartilhar = view.findViewById<Button>(R.id.btnPartilharPromessa)
        adViewCaixinha = view.findViewById(R.id.adViewCaixinha)
        configurarBanner()

        // Ao clicar na caixa
        imgCaixa.setOnClickListener {
            // 1. Efeito visual simples (opcional, treme a caixa)
            imgCaixa.animate().rotation(15f).setDuration(100).withEndAction {
                imgCaixa.animate().rotation(-15f).setDuration(100).withEndAction {
                    imgCaixa.animate().rotation(0f).setDuration(100).start()
                }
            }.start()

            // 2. Sorteia uma promessa
            val sorteio = promessas[Random.nextInt(promessas.size)]

            // 3. Preenche o texto
            tvTexto.text = "\"${sorteio.first}\""
            tvRef.text = sorteio.second

            // 4. Mostra o cartão (se estiver escondido)
            if (cardPromessa.visibility == View.GONE) {
                cardPromessa.alpha = 0f
                cardPromessa.visibility = View.VISIBLE
                cardPromessa.animate().alpha(1f).setDuration(500).start()
            }
        }

        // Botão de Partilhar Texto
        btnPartilhar.setOnClickListener {
            if (tvTexto.text.isNullOrBlank() || tvRef.text.isNullOrBlank()) {
                Toast.makeText(requireContext(), getString(R.string.promise_box_pick_first), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val textoParaEnviar = getString(R.string.promise_box_share_text, tvTexto.text.toString(), tvRef.text.toString())

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, textoParaEnviar)
            startActivity(Intent.createChooser(intent, getString(R.string.promise_box_share_chooser)))
        }
    }

    private fun configurarBanner() {
        val adRequest = AdRequest.Builder().build()
        adViewCaixinha.loadAd(adRequest)
    }
}