package com.elias.manadiario

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Quando o alarme toca, esta função é chamada
        mostrarNotificacao(context)
    }

    private fun mostrarNotificacao(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "lembrete_diario_id"

        // 1. Criar o Canal de Notificação (Obrigatório no Android 8+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Lembrete Diário",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificação diária para ler a palavra"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // 2. O que acontece quando clica na notificação (Abre o App)
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Montar a Notificação
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Usa o ícone do teu app
            .setContentTitle("Maná Diário 🙏")
            .setContentText(obterMensagemDiaria())
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // 4. Mostrar!
        notificationManager.notify(1, builder.build())
    }

    private fun obterMensagemDiaria(): String {
        val mensagens = listOf(
            "Deus tem uma palavra para você hoje.",
            "Antes de começar o dia, fortaleça sua fé.",
            "Não enfrente esse dia sozinho. Ore e siga com Deus.",
            "Reserve alguns minutos para ouvir o que Deus quer falar com você.",
            "Sua jornada de fé continua hoje. Abra o Maná Diário 🙏"
        )
        return mensagens[Random.nextInt(mensagens.size)]
    }
}
