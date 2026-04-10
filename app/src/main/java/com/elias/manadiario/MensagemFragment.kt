package com.elias.manadiario

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class MensagemFragment : Fragment() {

    private lateinit var mensagemTextView: TextView
    private lateinit var mensagens: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mensagem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. IDs ATUALIZADOS para bater com o novo Layout Bonito
        mensagemTextView = view.findViewById(R.id.textViewMensagem)
        val botaoNova = view.findViewById<Button>(R.id.buttonNovaMensagem)
        val botaoCompartilhar = view.findViewById<Button>(R.id.buttonCompartilhar)

        // 2. Carrega as mensagens
        mensagens = carregarMensagensDoArquivo()

        // Mostra a primeira
        if (mensagens.isNotEmpty()) {
            mostrarMensagemAleatoria()
        } else {
            mensagemTextView.text = getString(R.string.message_load_error)
        }

        // 3. Ações dos Botões
        botaoNova.setOnClickListener {
            if (mensagens.isNotEmpty()) mostrarMensagemAleatoria()
        }

        botaoCompartilhar.setOnClickListener {
            compartilharMensagem()
        }

        mensagemTextView.setOnLongClickListener {
            copiarMensagem()
            true
        }
    }

    private fun mostrarMensagemAleatoria() {
        val mensagem = mensagens.random()
        mensagemTextView.text = mensagem
    }

    private fun compartilharMensagem() {
        val mensagem = mensagemTextView.text.toString()
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message_of_faith, mensagem))
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_via)))
    }

    private fun copiarMensagem() {
        val mensagem = mensagemTextView.text.toString()
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getString(R.string.clip_label_message), mensagem)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.message_copied), Toast.LENGTH_SHORT).show()
    }

    private fun carregarMensagensDoArquivo(): List<String> {
        return try {
            val inputStream = resources.openRawResource(R.raw.mensagens)
            inputStream.bufferedReader().useLines { it.toList() }.filter { it.isNotBlank() }
        } catch (e: Exception) {
            resources.getStringArray(R.array.mensagens_fallback).toList()
        }
    }
}
