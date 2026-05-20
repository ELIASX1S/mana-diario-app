package com.elias.manadiario

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.preference.PreferenceManager
import java.util.Calendar
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        private const val CHANNEL_ID = "lembrete_diario_id"
        private const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        mostrarNotificacao(context)
    }

    private fun mostrarNotificacao(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        criarCanalSeNecessario(notificationManager)

        val pendingIntent = criarIntentDeAbertura(context)
        val conteudo = obterConteudoDaNotificacao(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(conteudo.titulo)
            .setContentText(conteudo.mensagemCurta)
            .setStyle(NotificationCompat.BigTextStyle().bigText(conteudo.mensagemCompleta))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    private fun criarCanalSeNecessario(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Lembrete Diário",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notificação diária para ler a Palavra e continuar sua jornada com Deus."
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun criarIntentDeAbertura(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun obterConteudoDaNotificacao(context: Context): ConteudoNotificacao {
        if (isJornadaConcluida(context)) {
            return ConteudoNotificacao(
                titulo = "Sua jornada foi concluída 🙏",
                mensagemCurta = "Que tal começar uma nova caminhada com Deus hoje?",
                mensagemCompleta = "Você concluiu sua jornada de 7 dias. Abra o Maná Diário e continue alimentando sua fé com a Palavra de Deus."
            )
        }

        val diaPlanoAtual = obterDiaAtualPlano(context)
        val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        val mensagens = when (hora) {
            in 5..11 -> listOf(
                ConteudoNotificacao(
                    "Bom dia com Deus 🙏",
                    "Comece o dia com uma palavra de fé.",
                    "Antes da correria começar, reserve alguns minutos para ouvir o que Deus quer falar com você hoje."
                ),
                ConteudoNotificacao(
                    "Sua palavra da manhã chegou ✨",
                    "Dia $diaPlanoAtual da sua jornada está esperando.",
                    "Abra o Maná Diário e fortaleça sua fé antes de começar mais um dia. Deus caminha com você."
                ),
                ConteudoNotificacao(
                    "Entregue este dia a Deus 🌅",
                    "Ore, respire e siga com fé.",
                    "Não comece este dia sozinho. Abra sua palavra diária e entregue seus planos nas mãos de Deus."
                )
            )

            in 12..17 -> listOf(
                ConteudoNotificacao(
                    "Uma pausa para Deus 🙏",
                    "Sua fé também precisa respirar.",
                    "Pare por alguns minutos, leia a Palavra e renove sua força para continuar o dia com paz."
                ),
                ConteudoNotificacao(
                    "Deus ainda está cuidando de você ✨",
                    "Abra sua mensagem de fé de hoje.",
                    "Mesmo no meio do dia, Deus pode renovar seu coração com uma palavra simples e poderosa."
                ),
                ConteudoNotificacao(
                    "Continue sua jornada 📖",
                    "O dia $diaPlanoAtual está pronto para você.",
                    "Sua caminhada com Deus continua hoje. Abra o Maná Diário e fortaleça sua confiança no Senhor."
                )
            )

            else -> listOf(
                ConteudoNotificacao(
                    "Termine o dia em paz 🌙",
                    "Entregue suas preocupações a Deus.",
                    "Antes de dormir, leia uma palavra, ore com calma e descanse sabendo que Deus cuida de você."
                ),
                ConteudoNotificacao(
                    "Deus guardou você até aqui 🙏",
                    "Receba uma palavra antes de descansar.",
                    "Feche o dia com gratidão. Abra o Maná Diário e entregue sua noite nas mãos de Deus."
                ),
                ConteudoNotificacao(
                    "Noite de oração ✨",
                    "Sua jornada de fé continua hoje.",
                    "Ainda dá tempo de buscar a presença de Deus hoje. Abra o app e fortaleça sua fé antes de dormir."
                )
            )
        }

        return mensagens[Random.nextInt(mensagens.size)]
    }

    private fun obterDiaAtualPlano(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt("home_faith_plan_day", 1).coerceIn(1, 7)
    }

    private fun isJornadaConcluida(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean("home_faith_plan_completed", false)
    }

    private data class ConteudoNotificacao(
        val titulo: String,
        val mensagemCurta: String,
        val mensagemCompleta: String
    )
}