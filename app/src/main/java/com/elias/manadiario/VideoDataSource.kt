// VideoDataSource.kt
package com.elias.manadiario

object VideoDataSource { // Usamos 'object' para criar um Singleton

    val videoList = listOf(
        Video(" Louvores de Intimidade com Deus", "Nh78MMtzCis"),
        Video("Louvores de Libertação e Avivamento", "wc9Tq-19Lt8"),
        Video("A Luz Que Guia", "8yRy4_2h-og"),
        Video("Melhores Músicas de Louvor e Adoração de 2025", "MQ3UlZVCMQM"),
        Video("Santo Espírito És Meu Guia ", "8Fqh2YyUQZc"),
        Video("Louvores Ungidos 2025", "ksn_tuLlvXY"),
        Video("Presença Divina", "JbBXpG2xSf4"),
        Video("Top 20 Louvores Gospel Que Tocam o Coração", "uKO-9s8zw08"),
        Video("Top 7 Louvores Sobre Intimidade com o Espírito Santo", "wcCZkCFgZqQ"),
        Video("Top 6 Louvores de Avivamento Profético", "gNNT9UvoiPs"),
        Video("Top 6 Louvores Que Tocam a Alma", "0DDj64E2VU0"),
        Video("Deus Escolheu Você", "A8alyBKHccI"),
        Video("Ele Quebra as Correntes", "s86fs5f5-PE"),
        Video("Me Batiza Outra Vez!", "96ssAsBtfBA"),
        Video("TOP 7: Louvores Que Ministram Cura e Restauração ", "D1ZbZXSOaFk"),
        Video("Louvores de Arrependimento", "UkrE8qL7g2w"),
        Video("Fundo Musical Para Oração", "56f8M2qvtmE"),
        Video("Toca em Mim, Jesus", "B7O_q6h69Ro"),
        Video("Volta, Filho Meu ", "tXcJvbCSQtk"),
        Video("Aqui Tem Fogo, Glória e Poder!", "EKIfHTqRhyY")


    )

    // Se no futuro você precisar de uma função para obter vídeos específicos,
    // ou filtrar, ela pode vir aqui.
    // Exemplo:
    // fun getVideoById(id: String): Video? {
    //     return videoList.find { it.videoId == id }
    // }
}