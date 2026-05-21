package com.elias.manadiario

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MaisOpcoesDrawerDialog(
    private val opcoes: List<MaisOpcao>,
    private val onItemClick: (MaisOpcao) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_mais_drawer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewMaisDrawer)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MaisOpcoesAdapter(opcoes) { opcaoSelecionada ->
            onItemClick(opcaoSelecionada)
            dismiss()
        }

        view.findViewById<View>(R.id.btnCloseDrawer).setOnClickListener { dismiss() }
        view.setOnClickListener { dismiss() }
        view.findViewById<View>(R.id.drawerPanel).setOnClickListener { /* evita fechar ao tocar dentro */ }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.let { window ->
            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            window.setGravity(Gravity.START)
            window.setDimAmount(0f)
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }
}
