package com.elias.manadiario

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTemas)

        // 1. A TUA LISTA DE BOTÕES
        val listaTemas = listOf(
            Tema(1, "Fé", android.R.drawable.ic_menu_view),
            Tema(2, "Esperança", android.R.drawable.ic_menu_compass),
            Tema(3, "Amor", android.R.drawable.btn_star_big_on),
            Tema(4, "Paz", android.R.drawable.ic_menu_gallery),
            Tema(5, "Salvação", android.R.drawable.ic_menu_upload),
            Tema(6, "Sabedoria", android.R.drawable.ic_menu_info_details),
            Tema(7, "Coragem", android.R.drawable.ic_media_play),
            Tema(8, "Perdão", android.R.drawable.ic_menu_revert)
        )

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // 2. AQUI ESTÁ A LIGAÇÃO COM OS TEUS NOMES REAIS DO XML
        recyclerView.adapter = TemaAdapter(listaTemas) { tema ->

            val idDoArray = when (tema.id) {
                1 -> R.array.dados_tema_fe                //
                2 -> R.array.dados_tema_esperanca         //
                3 -> R.array.dados_tema_amor              //
                4 -> R.array.dados_tema_paz_completo      //
                5 -> R.array.dados_tema_salvacao_completo //
                6 -> R.array.dados_tema_sabedoria_completo //
                7 -> R.array.dados_tema_coragem_completo  //
                8 -> R.array.dados_tema_perdao_completo   //
                else -> 0
            }

            if (idDoArray != 0) {
                val intent = Intent(requireContext(), VersesActivity::class.java)
                intent.putExtra("THEME_NAME", tema.nome)
                intent.putExtra("VERSES_ARRAY_ID", idDoArray)
                startActivity(intent)
            } else {
                Toast.makeText(context, "Em breve!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}