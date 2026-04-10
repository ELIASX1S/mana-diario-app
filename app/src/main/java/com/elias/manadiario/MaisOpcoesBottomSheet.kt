package com.elias.manadiario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.RecyclerView

class MaisOpcoesBottomSheet(
    private val opcoes: List<MaisOpcao>,
    private val onItemClick: (MaisOpcao) -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_mais_opcoes, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMais)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MaisOpcoesAdapter(opcoes) { opcaoSelecionada ->
            onItemClick(opcaoSelecionada)
            dismiss()
        }

        return view
    }
}
