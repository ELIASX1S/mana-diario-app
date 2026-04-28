package com.elias.manadiario

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class EbookOracaoActivity : AppCompatActivity() {

    private lateinit var billingManager: EbookPrayerBillingManager
    private lateinit var statusTextView: TextView
    private lateinit var contentTextView: TextView
    private lateinit var buyButton: MaterialButton
    private lateinit var restoreButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ebook_oracao)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "E-book de Oração"

        statusTextView = findViewById(R.id.textEbookStatus)
        contentTextView = findViewById(R.id.textEbookContent)
        buyButton = findViewById(R.id.buttonBuyEbook)
        restoreButton = findViewById(R.id.buttonRestoreEbook)

        billingManager = EbookPrayerBillingManager(
            context = this,
            listener = object : EbookPrayerBillingManager.Listener {
                override fun onBillingReady(productDetails: com.android.billingclient.api.ProductDetails?) {
                    val price = productDetails?.oneTimePurchaseOfferDetails?.formattedPrice
                    buyButton.text = if (price.isNullOrBlank()) {
                        "Liberar e-book"
                    } else {
                        "Liberar e-book por $price"
                    }
                }

                override fun onPurchaseUnlocked() {
                    renderUnlockedState()
                }

                override fun onBillingMessage(message: String) {
                    statusTextView.text = message
                }
            }
        )

        buyButton.setOnClickListener {
            billingManager.launchPurchase(this)
        }

        restoreButton.setOnClickListener {
            billingManager.restorePurchases()
        }

        if (billingManager.isEbookUnlocked()) {
            renderUnlockedState()
        } else {
            renderLockedState()
            billingManager.startConnection()
        }
    }

    private fun renderLockedState() {
        statusTextView.text = "Conteúdo premium bloqueado. Libere o e-book pela Google Play para acessar."
        contentTextView.text = "7 Passos para Melhorar sua Vida de Oração\n\nUm guia prático para fortalecer sua oração diária com fundamentos bíblicos, aplicação prática e um cronograma de 30 dias."
        buyButton.visibility = View.VISIBLE
        restoreButton.visibility = View.VISIBLE
    }

    private fun renderUnlockedState() {
        statusTextView.text = "E-book liberado ✅"
        contentTextView.text = buildEbookContent()
        buyButton.visibility = View.GONE
        restoreButton.visibility = View.GONE
    }

    private fun buildEbookContent(): String = buildString {
        appendLine("SONS DO ESPÍRITO")
        appendLine("7 PASSOS PARA MELHORAR SUA VIDA DE ORAÇÃO")
        appendLine("Um guia prático para fortalecer sua oração diária")
        appendLine()
        appendLine("INTRODUÇÃO — A ORAÇÃO COMO FUNDAÇÃO")
        appendLine()
        appendLine("Você já ajoelhou, fechou os olhos… e nada saiu? Ou pior: as palavras vieram, mas pareceram ecoar no vazio — como se Deus estivesse longe, ocupado, ou simplesmente em silêncio?")
        appendLine()
        appendLine("Se você sentiu isso, não está sozinho. Muitos cristãos sinceros passam anos orando de forma mecânica, sem profundidade, sem constância — e carregam uma culpa silenciosa por isso.")
        appendLine()
        appendLine("A oração não é uma performance religiosa. É um relacionamento — e relacionamentos se constroem passo a passo.")
        appendLine()
        appendLine("Este e-book foi criado para te ajudar a transformar sua vida de oração com sete passos práticos, fundamentados na Bíblia. Ao final, você encontrará um cronograma de 30 dias para aplicar cada passo de forma progressiva.")
        appendLine()
        appendLine("PASSO 1 — A IMPORTÂNCIA DA ORAÇÃO NA VIDA CRISTÃ")
        appendLine()
        appendLine("A oração é o alicerce de uma vida cristã forte e conectada com Deus. Ela não é apenas um momento de falar com Deus, mas também de ouvi-Lo, entender Sua vontade e buscar Sua direção para nossas vidas.")
        appendLine()
        appendLine("Oração: a comunicação direta com Deus")
        appendLine("A oração é uma linha direta para o Criador do universo. Através dela, podemos expressar nossas preocupações, agradecimentos, arrependimentos e nossa dependência dEle.")
        appendLine()
        appendLine("Filipenses 4:6-7 — Não andem ansiosos por coisa alguma, mas em tudo, pela oração e súplicas, e com ação de graças, apresentem seus pedidos a Deus.")
        appendLine()
        appendLine("Jesus: nosso exemplo de oração")
        appendLine("Mesmo sendo o Filho de Deus, Jesus demonstrou a necessidade de estar em constante comunicação com o Pai. Ele orava antes de tomar decisões importantes, orava por força nos momentos difíceis e ensinava seus discípulos a orar com eficácia.")
        appendLine()
        appendLine("Se até Jesus orava constantemente, quanto mais nós precisamos da oração.")
        appendLine()
        appendLine("Passo prático 1: crie seu horário de oração")
        appendLine("Escolha um horário fixo hoje — manhã, almoço ou noite. Comece com apenas 10 minutos. Consistência vale mais que duração.")
        appendLine()
        appendLine("PASSO 2 — TENHA UM PROPÓSITO CLARO AO ORAR")
        appendLine()
        appendLine("Muitas vezes caímos na armadilha de fazer orações genéricas e repetitivas, sem uma direção específica. Isso pode nos levar a sentir que nossas orações não têm impacto, e acabamos nos desmotivando.")
        appendLine()
        appendLine("Mateus 7:7 — Peçam, e lhes será dado; busquem, e encontrarão; batam, e a porta será aberta.")
        appendLine()
        appendLine("Orar com propósito significa saber exatamente o que estamos pedindo a Deus e por quê. Isso organiza nossos pensamentos e nos ensina a alinhar nossas orações com a vontade de Deus.")
        appendLine()
        appendLine("O modelo do Pai Nosso nos ensina a reconhecer a soberania de Deus, buscar Sua vontade, pedir por necessidades reais, pedir perdão, perdoar e buscar proteção.")
        appendLine()
        appendLine("Passo prático 2: escreva suas petições antes de orar")
        appendLine("Faça uma lista rápida de 3 a 5 coisas pelas quais quer orar hoje. Isso mantém o foco e te ajuda a perceber quando Deus responde.")
        appendLine()
        appendLine("Passo prático 2: alinhe seus pedidos à Palavra")
        appendLine("Para cada pedido, pergunte: isso está alinhado com o que a Bíblia ensina? Use um versículo como base.")
        appendLine()
        appendLine("PASSO 3 — ORANDO COM FÉ E CONFIANÇA")
        appendLine()
        appendLine("Uma oração sem fé é como uma semente que nunca brota. A fé é a força motriz por trás de uma oração eficaz. Quando oramos com fé, declaramos nossa confiança em Deus — mesmo que não vejamos a resposta imediatamente.")
        appendLine()
        appendLine("Tiago 1:6 — Peça-a com fé, sem duvidar, pois aquele que duvida é semelhante à onda do mar, levada e agitada pelo vento.")
        appendLine()
        appendLine("A fé da mulher com fluxo de sangue")
        appendLine("Ela acreditava que, se apenas tocasse na orla do manto de Jesus, seria curada. Sua fé não era baseada em palavras ou rituais — era confiança inabalável.")
        appendLine()
        appendLine("Como fortalecer sua fé na oração:")
        appendLine("• Medite nas promessas de Deus.")
        appendLine("• Agradeça antes de receber.")
        appendLine("• Declare em voz alta sua confiança.")
        appendLine("• Lembre-se das respostas passadas.")
        appendLine()
        appendLine("Passo prático 3: confesse a dúvida em vez de escondê-la")
        appendLine("Se você está duvidando, diga isso a Deus: Senhor, ajuda-me a crer. Deus não se afasta da honestidade — Ele a honra.")
        appendLine()
        appendLine("PASSO 4 — MANTENHA A PERSISTÊNCIA NA ORAÇÃO")
        appendLine()
        appendLine("A oração nem sempre traz resultados imediatos. Vivemos em um mundo de respostas rápidas, e quando não vemos uma resposta clara de Deus, podemos nos sentir desmotivados. Mas a Bíblia nos ensina que a persistência é essencial.")
        appendLine()
        appendLine("Lucas 18:1 — Jesus contou essa parábola para mostrar que seus discípulos deviam orar sempre e nunca desanimar.")
        appendLine()
        appendLine("Elias e a oração pela chuva")
        appendLine("Elias orou sete vezes antes de ver a primeira nuvem no horizonte. Ele poderia ter desistido após a primeira, segunda ou sexta tentativa. Mas persistiu — e a chuva abundante restaurou a terra.")
        appendLine()
        appendLine("Por que Deus às vezes demora?")
        appendLine("• Para amadurecimento.")
        appendLine("• Para um propósito maior.")
        appendLine("• Para provar nossa fé.")
        appendLine()
        appendLine("Passo prático 4: mantenha um diário de oração")
        appendLine("Anote cada pedido com a data. Deixe espaço para registrar a resposta quando vier. Em 30 dias, você terá evidências concretas da fidelidade de Deus.")
        appendLine()
        appendLine("Passo prático 4: ore com outros cristãos")
        appendLine("Quando a motivação cai, a oração em grupo sustenta. Combine com alguém para orar junto uma vez por semana.")
        appendLine()
        appendLine("PASSO 5 — ORAÇÃO COM AÇÃO DE GRAÇAS E ADORAÇÃO")
        appendLine()
        appendLine("A oração não é apenas o momento de pedir algo a Deus. Ela também é um momento de agradecer pelo que Ele já fez e adorá-Lo por quem Ele é.")
        appendLine()
        appendLine("1 Tessalonicenses 5:18 — Em tudo dai graças, pois esta é a vontade de Deus para vocês em Cristo Jesus.")
        appendLine()
        appendLine("Paulo e Silas na prisão")
        appendLine("Presos injustamente, eles escolheram orar e cantar hinos. O resultado foi um terremoto que abriu as portas e tocou a vida do carcereiro. Adorar a Deus na adversidade é um ato de fé que muda circunstâncias.")
        appendLine()
        appendLine("Três formas de gratidão na oração:")
        appendLine("• Agradeça pelo passado.")
        appendLine("• Agradeça pelo presente.")
        appendLine("• Agradeça pelo futuro.")
        appendLine()
        appendLine("Passo prático 5: comece toda oração com 3 gratidões")
        appendLine("Antes de fazer qualquer pedido, liste três coisas pelas quais você é grato hoje. Isso muda completamente o tom e a profundidade da sua oração.")
        appendLine()
        appendLine("PASSO 6 — PEÇA A DEUS SABEDORIA E DISCERNIMENTO")
        appendLine()
        appendLine("Muitas decisões exigem sabedoria que não temos sozinhos. Felizmente, Deus promete concedê-la a quem pede com fé.")
        appendLine()
        appendLine("Tiago 1:5 — Se algum de vocês tem falta de sabedoria, peça-a a Deus, que a todos dá livremente, de boa vontade.")
        appendLine()
        appendLine("A oração de Salomão")
        appendLine("Quando Deus disse: peça o que quiser, Salomão escolheu sabedoria para governar com justiça. Deus ficou tão satisfeito que acrescentou riquezas e honra além do que foi pedido.")
        appendLine()
        appendLine("Sabedoria vs. discernimento")
        appendLine("• Sabedoria: capacidade de aplicar o conhecimento da forma correta.")
        appendLine("• Discernimento: capacidade de distinguir o que é de Deus do que não é.")
        appendLine()
        appendLine("Passo prático 6: ore por áreas específicas")
        appendLine("Escolha uma área: relacionamentos, finanças ou carreira. Ore diariamente pedindo sabedoria específica para essa área e aguarde clareza.")
        appendLine()
        appendLine("PASSO 7 — A ORAÇÃO COM O ESPÍRITO SANTO")
        appendLine()
        appendLine("Quando oramos, muitas vezes nos sentimos limitados pelas palavras. É nesses momentos que o Espírito Santo nos ajuda — levando nossas orações além do que conseguimos expressar.")
        appendLine()
        appendLine("Romanos 8:26 — O Espírito nos ajuda em nossa fraqueza, pois não sabemos como orar, mas o próprio Espírito intercede por nós com gemidos inexprimíveis.")
        appendLine()
        appendLine("Orar no Espírito é permitir que Ele guie nossas orações de acordo com a vontade de Deus. Isso pode se manifestar como uma oração guiada por impressões, intercessão por pessoas que surgem no coração ou momentos de silêncio onde deixamos o Espírito agir.")
        appendLine()
        appendLine("Como convidar o Espírito Santo:")
        appendLine("• Comece pedindo: Espírito Santo, toma o controle desta oração.")
        appendLine("• Preste atenção a pensamentos ou nomes que surgem durante a oração.")
        appendLine("• Ore com o coração, sem fórmulas.")
        appendLine("• Use as Escrituras.")
        appendLine()
        appendLine("Passo prático 7: reserve 5 minutos de silêncio")
        appendLine("Ao final de cada oração, fique em silêncio por 5 minutos. Não peça nada. Apenas ouça. O Espírito fala na quietude.")
        appendLine()
        appendLine("CRONOGRAMA DE 30 DIAS — ORAÇÃO TRANSFORMADORA")
        appendLine()
        appendLine("SEMANA 1 — DIAS 1 A 7")
        appendLine("Foco: fundamento e propósito.")
        appendLine("Dia 1: Leia Filipenses 4:6-7. Ore por 5 minutos agradecendo antes de pedir qualquer coisa.")
        appendLine("Dia 2: Escreva 3 áreas da sua vida que precisam da intervenção de Deus.")
        appendLine("Dia 3: Use o Pai Nosso como roteiro. Siga cada parte lentamente.")
        appendLine("Dia 4: Ore em voz alta. Diga exatamente o que sente, sem filtro.")
        appendLine("Dia 5: Escolha 1 versículo e ore com base nele.")
        appendLine("Dia 6: Ore por outra pessoa antes de orar por si mesmo.")
        appendLine("Dia 7: Revise o que escreveu nos dias anteriores. Anote qualquer resposta de Deus.")
        appendLine()
        appendLine("SEMANA 2 — DIAS 8 A 14")
        appendLine("Foco: fé e persistência.")
        appendLine("Dia 8: Leia Marcos 11:24. Ore pedindo algo específico e declare que acredita.")
        appendLine("Dia 9: Identifique 1 dúvida que te paralisa. Confesse-a a Deus honestamente.")
        appendLine("Dia 10: Relembre 3 orações que Deus já respondeu na sua vida. Agradeça.")
        appendLine("Dia 11: Ore pela mesma coisa 3 vezes ao longo do dia.")
        appendLine("Dia 12: Leia 1 Reis 18:42-45. Ore com persistência.")
        appendLine("Dia 13: Escreva uma carta a Deus sobre algo que você quase desistiu de pedir.")
        appendLine("Dia 14: Ore e fique em silêncio por 5 minutos. Apenas ouça.")
        appendLine()
        appendLine("SEMANA 3 — DIAS 15 A 21")
        appendLine("Foco: gratidão e sabedoria.")
        appendLine("Dia 15: Liste 10 coisas pelas quais você é grato — sem pedir nada.")
        appendLine("Dia 16: Ore cantando ou com música de louvor ao fundo.")
        appendLine("Dia 17: Leia o Salmo 100 e use como roteiro de adoração.")
        appendLine("Dia 18: Peça sabedoria para uma decisão que você está adiando.")
        appendLine("Dia 19: Ore por discernimento: Senhor, mostra o que é Teu e o que não é.")
        appendLine("Dia 20: Ore por uma área financeira, uma relacional e uma espiritual.")
        appendLine("Dia 21: Faça um balanço da semana. O que mudou? O que você está vendo diferente?")
        appendLine()
        appendLine("SEMANA 4 — DIAS 22 A 30")
        appendLine("Foco: oração com o Espírito Santo e integração.")
        appendLine("Dia 22: Antes de orar, diga: Espírito Santo, toma o controle desta oração.")
        appendLine("Dia 23: Ore em silêncio por 10 minutos.")
        appendLine("Dia 24: Interceda por 3 pessoas usando Romanos 8:26 como base.")
        appendLine("Dia 25: Ore os 7 passos em sequência.")
        appendLine("Dia 26: Leia sua carta do Dia 13 e ore novamente.")
        appendLine("Dia 27: Convide alguém para orar junto.")
        appendLine("Dia 28: Ore pelo mesmo pedido do Dia 1 e registre o que mudou.")
        appendLine("Dia 29: Escreva uma carta para você mesmo sobre o que aprendeu.")
        appendLine("Dia 30: Oração livre. Sem roteiro. Apenas você e Deus.")
        appendLine()
        appendLine("CONCLUSÃO — APLICANDO OS 7 PASSOS DIARIAMENTE")
        appendLine()
        appendLine("Você chegou ao fim deste guia — mas ao começo de algo maior. A oração não é uma habilidade que se domina, é um relacionamento que se aprofunda dia a dia.")
        appendLine()
        appendLine("Recapitulando os 7 passos:")
        appendLine("1. Reconheça o valor da oração na sua vida cristã.")
        appendLine("2. Ore com propósito e intenção claros.")
        appendLine("3. Ore com fé, sem deixar a dúvida assumir o controle.")
        appendLine("4. Persista — a resposta de Deus vem no tempo certo.")
        appendLine("5. Inclua gratidão e adoração em cada oração.")
        appendLine("6. Peça sabedoria e discernimento para suas decisões.")
        appendLine("7. Convide o Espírito Santo para guiar suas orações.")
        appendLine()
        appendLine("A oração é uma jornada, não um destino. Quanto mais você ora, mais você cresce.")
        appendLine()
        appendLine("Deus abençoe sua caminhada.")
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun onDestroy() {
        billingManager.endConnection()
        super.onDestroy()
    }
}
