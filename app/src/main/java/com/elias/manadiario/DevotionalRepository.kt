package com.elias.manadiario

object DevotionalRepository {

    private val plano7Dias = DevotionalPlan(
        id = "plano_7_dias",
        nome = "Jornada de 7 Dias",
        descricao = "Uma jornada devocional curta para fortalecer sua fé durante a semana.",
        dias = listOf(
            DevotionalDay(
                dia = 1,
                titulo = "Confiança em Deus",
                versiculoPrincipal = "Confie no Senhor de todo o seu coração e não se apoie em seu próprio entendimento. (Provérbios 3:5)",
                versiculoComplementar = "Entregue o seu caminho ao Senhor; confie nele, e ele agirá. (Salmos 37:5)",
                reflexao = "Confiar em Deus é descansar mesmo quando não entendemos tudo. A fé cresce quando entregamos nossos caminhos ao Senhor.",
                oracao = "Senhor, ensina-me a confiar em Ti acima dos meus medos e dúvidas. Fortalece o meu coração hoje.",
                aplicacao = "Hoje, entregue em oração uma preocupação específica ao Senhor."
            ),
            DevotionalDay(
                dia = 2,
                titulo = "Força para continuar",
                versiculoPrincipal = "Tudo posso naquele que me fortalece. (Filipenses 4:13)",
                versiculoComplementar = "Mas os que esperam no Senhor renovarão as suas forças. (Isaías 40:31)",
                reflexao = "Nossa força não vem apenas de nós mesmos. Deus renova quem decide permanecer nele com esperança.",
                oracao = "Pai, renova minhas forças e ajuda-me a continuar, mesmo quando eu estiver cansado.",
                aplicacao = "Separe alguns minutos hoje para respirar, orar e lembrar que sua força vem de Deus."
            ),
            DevotionalDay(
                dia = 3,
                titulo = "Paz em meio às lutas",
                versiculoPrincipal = "Deixo-lhes a paz; a minha paz lhes dou. (João 14:27)",
                versiculoComplementar = "E a paz de Deus, que excede todo o entendimento, guardará os seus corações. (Filipenses 4:7)",
                reflexao = "A paz de Cristo não depende das circunstâncias. Ela guarda nosso coração mesmo quando o mundo ao redor está agitado.",
                oracao = "Jesus, enche meu coração com a tua paz e acalma toda ansiedade dentro de mim.",
                aplicacao = "Hoje, ao sentir ansiedade, repita em oração: 'A tua paz guarda o meu coração'."
            ),
            DevotionalDay(
                dia = 4,
                titulo = "Deus cuida de você",
                versiculoPrincipal = "Lancem sobre ele toda a sua ansiedade, porque ele tem cuidado de vocês. (1 Pedro 5:7)",
                versiculoComplementar = "O Senhor é o meu pastor; de nada terei falta. (Salmos 23:1)",
                reflexao = "Você não está sozinho. Deus cuida dos detalhes da sua vida e continua presente em cada necessidade.",
                oracao = "Senhor, entrego a Ti minhas preocupações. Ajuda-me a descansar no teu cuidado fiel.",
                aplicacao = "Escreva hoje uma preocupação e entregue conscientemente isso a Deus em oração."
            ),
            DevotionalDay(
                dia = 5,
                titulo = "Amor que transforma",
                versiculoPrincipal = "O amor é paciente, o amor é bondoso. (1 Coríntios 13:4)",
                versiculoComplementar = "Amados, amemos uns aos outros, pois o amor procede de Deus. (1 João 4:7)",
                reflexao = "Quando o amor de Deus enche nosso coração, nossa forma de tratar as pessoas também muda.",
                oracao = "Pai, derrama teu amor em mim e faz de mim alguém mais paciente, bondoso e semelhante a Cristo.",
                aplicacao = "Hoje, pratique um gesto intencional de amor com alguém próximo."
            ),
            DevotionalDay(
                dia = 6,
                titulo = "Coragem para enfrentar o dia",
                versiculoPrincipal = "Sejam fortes e corajosos. Não tenham medo. (Deuteronômio 31:6)",
                versiculoComplementar = "Não fui eu que lhe ordenei? Seja forte e corajoso! (Josué 1:9)",
                reflexao = "A coragem não é ausência de medo, mas a decisão de seguir adiante sabendo que Deus está com você.",
                oracao = "Senhor, dá-me coragem para enfrentar este dia com fé, firmeza e confiança na tua presença.",
                aplicacao = "Dê hoje um passo que você estava adiando por medo."
            ),
            DevotionalDay(
                dia = 7,
                titulo = "Esperança no futuro",
                versiculoPrincipal = "Porque sou eu que conheço os planos que tenho para vocês, diz o Senhor. (Jeremias 29:11)",
                versiculoComplementar = "Que o Deus da esperança os encha de toda alegria e paz. (Romanos 15:13)",
                reflexao = "Mesmo quando o futuro parece incerto, Deus continua escrevendo uma história de esperança sobre sua vida.",
                oracao = "Pai, eu entrego meu futuro a Ti. Renova minha esperança e ajuda-me a confiar nos teus planos.",
                aplicacao = "Hoje, declare em voz alta que seu futuro está nas mãos de Deus."
            )
        )
    )

    private val plano21Dias = DevotionalPlan(
        id = "plano_21_dias",
        nome = "Jornada de 21 Dias",
        descricao = "Um plano de três semanas para fortalecer fé, paz, coragem e constância espiritual.",
        dias = listOf(
            DevotionalDay(
                dia = 1,
                titulo = "Começando com fé",
                versiculoPrincipal = "Ora, a fé é a certeza daquilo que esperamos e a prova das coisas que não vemos. (Hebreus 11:1)",
                versiculoComplementar = "O justo viverá pela fé. (Romanos 1:17)",
                reflexao = "Toda jornada com Deus começa pela fé. Mesmo quando você não enxerga tudo, pode continuar confiando.",
                oracao = "Senhor, fortalece minha fé e ajuda-me a permanecer firme em Ti desde o começo desta jornada.",
                aplicacao = "Dê hoje um passo de fé em algo que você vinha adiando."
            ),
            DevotionalDay(
                dia = 2,
                titulo = "Descansando em Deus",
                versiculoPrincipal = "Venham a mim, todos os que estão cansados e sobrecarregados, e eu lhes darei descanso. (Mateus 11:28)",
                versiculoComplementar = "Em paz me deito e logo adormeço, pois só tu, Senhor, me fazes viver em segurança. (Salmos 4:8)",
                reflexao = "Deus não quer apenas sua força; Ele também quer ser seu descanso.",
                oracao = "Pai, tira de mim o peso excessivo e ensina-me a repousar no teu cuidado.",
                aplicacao = "Separe um momento hoje para silenciar o coração diante de Deus."
            ),
            DevotionalDay(
                dia = 3,
                titulo = "Deus é refúgio",
                versiculoPrincipal = "Deus é o nosso refúgio e a nossa fortaleza, auxílio sempre presente na adversidade. (Salmos 46:1)",
                versiculoComplementar = "O Senhor é bom, um refúgio em tempos de angústia. (Naum 1:7)",
                reflexao = "Nos dias difíceis, Deus continua sendo abrigo seguro para quem corre até Ele.",
                oracao = "Senhor, sê meu refúgio hoje. Guarda meu coração em meio às pressões.",
                aplicacao = "Quando sentir medo, repita: 'O Senhor é meu refúgio'."
            ),
            DevotionalDay(
                dia = 4,
                titulo = "Palavra que guia",
                versiculoPrincipal = "Lâmpada para os meus pés é a tua palavra e, luz para os meus caminhos. (Salmos 119:105)",
                versiculoComplementar = "A tua palavra é a verdade. (João 17:17)",
                reflexao = "A Palavra de Deus ilumina o próximo passo, mesmo quando você não vê todo o caminho.",
                oracao = "Pai, fala comigo por meio da tua Palavra e guia minhas decisões.",
                aplicacao = "Leia novamente o versículo principal antes de dormir."
            ),
            DevotionalDay(
                dia = 5,
                titulo = "Coragem em Deus",
                versiculoPrincipal = "Não fui eu que lhe ordenei? Seja forte e corajoso. (Josué 1:9)",
                versiculoComplementar = "O Senhor é a minha luz e a minha salvação; de quem terei temor? (Salmos 27:1)",
                reflexao = "A coragem cristã nasce da certeza de que Deus caminha conosco.",
                oracao = "Senhor, tira meu medo e coloca em mim firmeza para obedecer.",
                aplicacao = "Enfrente hoje com coragem uma situação que você vinha evitando."
            ),
            DevotionalDay(
                dia = 6,
                titulo = "Ansiedade aos pés do Senhor",
                versiculoPrincipal = "Lancem sobre ele toda a sua ansiedade, porque ele tem cuidado de vocês. (1 Pedro 5:7)",
                versiculoComplementar = "Não andem ansiosos por coisa alguma. (Filipenses 4:6)",
                reflexao = "Ansiedade diminui quando entregamos o controle a Deus.",
                oracao = "Pai, eu entrego minhas preocupações e descanso na tua fidelidade.",
                aplicacao = "Escreva uma preocupação e entregue isso em oração."
            ),
            DevotionalDay(
                dia = 7,
                titulo = "Primeira semana concluída",
                versiculoPrincipal = "Este é o dia que o Senhor fez; regozijemo-nos e alegremo-nos nele. (Salmos 118:24)",
                versiculoComplementar = "Deem graças em todas as circunstâncias. (1 Tessalonicenses 5:18)",
                reflexao = "Gratidão fortalece o coração e nos lembra que Deus esteve presente durante toda a semana.",
                oracao = "Obrigado, Senhor, por me sustentares até aqui. Continua guiando meus passos.",
                aplicacao = "Liste 3 motivos de gratidão desta semana."
            ),
            DevotionalDay(
                dia = 8,
                titulo = "Deus vê o coração",
                versiculoPrincipal = "O Senhor não vê como o homem: o homem vê a aparência, mas o Senhor vê o coração. (1 Samuel 16:7)",
                versiculoComplementar = "Acima de tudo, guarde o seu coração. (Provérbios 4:23)",
                reflexao = "Deus olha além da aparência. Ele se importa com quem você é por dentro.",
                oracao = "Pai, purifica meu coração e alinha minhas intenções à tua vontade.",
                aplicacao = "Peça a Deus hoje sinceridade e pureza interior."
            ),
            DevotionalDay(
                dia = 9,
                titulo = "Paz que excede",
                versiculoPrincipal = "E a paz de Deus, que excede todo o entendimento, guardará os seus corações. (Filipenses 4:7)",
                versiculoComplementar = "Tu guardarás em perfeita paz aquele cujo propósito está firme. (Isaías 26:3)",
                reflexao = "A paz de Deus não depende de lógica humana. Ela é presente do alto.",
                oracao = "Senhor, guarda minha mente e meu coração com a tua paz.",
                aplicacao = "Desligue por alguns minutos as distrações e ore em silêncio."
            ),
            DevotionalDay(
                dia = 10,
                titulo = "Força renovada",
                versiculoPrincipal = "Mas os que esperam no Senhor renovarão as suas forças. (Isaías 40:31)",
                versiculoComplementar = "O Senhor é a força da minha vida. (Salmos 27:1)",
                reflexao = "Quando você espera em Deus, não fica vazio; você é renovado.",
                oracao = "Pai, renova minhas forças físicas, emocionais e espirituais.",
                aplicacao = "Hoje, escolha depender de Deus em vez de apenas da sua própria força."
            ),
            DevotionalDay(
                dia = 11,
                titulo = "Amor em prática",
                versiculoPrincipal = "Amados, amemos uns aos outros, pois o amor procede de Deus. (1 João 4:7)",
                versiculoComplementar = "O amor é paciente, o amor é bondoso. (1 Coríntios 13:4)",
                reflexao = "O amor verdadeiro se revela nas atitudes simples do dia a dia.",
                oracao = "Senhor, faz-me amar de forma prática, verdadeira e humilde.",
                aplicacao = "Faça hoje algo bondoso por alguém sem esperar retorno."
            ),
            DevotionalDay(
                dia = 12,
                titulo = "Perdão liberta",
                versiculoPrincipal = "Perdoem como o Senhor lhes perdoou. (Colossenses 3:13)",
                versiculoComplementar = "Sejam bondosos e compassivos uns para com os outros. (Efésios 4:32)",
                reflexao = "Perdão não muda o passado, mas liberta o coração para seguir adiante.",
                oracao = "Pai, ajuda-me a liberar perdão e a não carregar pesos desnecessários.",
                aplicacao = "Ore hoje por alguém que feriu você."
            ),
            DevotionalDay(
                dia = 13,
                titulo = "Sabedoria do alto",
                versiculoPrincipal = "Se algum de vocês tem falta de sabedoria, peça-a a Deus. (Tiago 1:5)",
                versiculoComplementar = "O temor do Senhor é o princípio da sabedoria. (Provérbios 9:10)",
                reflexao = "Sabedoria não é apenas saber muito, mas viver guiado por Deus.",
                oracao = "Senhor, dá-me discernimento para escolher o que é certo.",
                aplicacao = "Antes de uma decisão hoje, ore primeiro."
            ),
            DevotionalDay(
                dia = 14,
                titulo = "Segunda semana concluída",
                versiculoPrincipal = "Alegrem-se na esperança, sejam pacientes na tribulação, perseverem na oração. (Romanos 12:12)",
                versiculoComplementar = "Orai sem cessar. (1 Tessalonicenses 5:17)",
                reflexao = "Perseverança é continuar com Deus mesmo quando tudo ainda não mudou.",
                oracao = "Pai, fortalece minha perseverança e mantém minha esperança viva.",
                aplicacao = "Reserve um tempo maior de oração hoje."
            ),
            DevotionalDay(
                dia = 15,
                titulo = "Chamado à santidade",
                versiculoPrincipal = "Sejam santos porque eu sou santo. (1 Pedro 1:16)",
                versiculoComplementar = "Mas, assim como é santo aquele que os chamou, sejam santos vocês também. (1 Pedro 1:15)",
                reflexao = "Santidade não é perfeição humana, mas separação sincera para Deus.",
                oracao = "Senhor, ajuda-me a viver de modo que te honre em cada área.",
                aplicacao = "Afaste-se hoje de algo que esfria sua comunhão com Deus."
            ),
            DevotionalDay(
                dia = 16,
                titulo = "Alegria no Senhor",
                versiculoPrincipal = "A alegria do Senhor é a vossa força. (Neemias 8:10)",
                versiculoComplementar = "Regozijai-vos sempre. (1 Tessalonicenses 5:16)",
                reflexao = "A alegria do Senhor sustenta até em tempos difíceis.",
                oracao = "Pai, enche meu coração com alegria que vem de Ti.",
                aplicacao = "Louve a Deus hoje mesmo sem vontade."
            ),
            DevotionalDay(
                dia = 17,
                titulo = "Deus provê",
                versiculoPrincipal = "O meu Deus suprirá todas as necessidades de vocês. (Filipenses 4:19)",
                versiculoComplementar = "O Senhor é o meu pastor; de nada terei falta. (Salmos 23:1)",
                reflexao = "A provisão de Deus alcança mais do que o material; alcança também a alma.",
                oracao = "Senhor, eu confio que Tu cuidarás das minhas necessidades.",
                aplicacao = "Troque hoje a murmuração por gratidão."
            ),
            DevotionalDay(
                dia = 18,
                titulo = "Nada é impossível",
                versiculoPrincipal = "Pois nada é impossível para Deus. (Lucas 1:37)",
                versiculoComplementar = "Tudo é possível àquele que crê. (Marcos 9:23)",
                reflexao = "Aquilo que parece impossível para você ainda está dentro do alcance de Deus.",
                oracao = "Pai, aumenta minha fé diante do impossível.",
                aplicacao = "Apresente hoje a Deus uma área que parece impossível."
            ),
            DevotionalDay(
                dia = 19,
                titulo = "Coração ensinável",
                versiculoPrincipal = "Ensina-nos a contar os nossos dias, para que alcancemos coração sábio. (Salmos 90:12)",
                versiculoComplementar = "Ouça conselhos e aceite a disciplina. (Provérbios 19:20)",
                reflexao = "Quem aprende com Deus amadurece mais rápido e caminha com mais equilíbrio.",
                oracao = "Senhor, dá-me um coração humilde e ensinável.",
                aplicacao = "Receba hoje uma correção sem orgulho."
            ),
            DevotionalDay(
                dia = 20,
                titulo = "Vencendo o mal com o bem",
                versiculoPrincipal = "Não se deixem vencer pelo mal, mas vençam o mal com o bem. (Romanos 12:21)",
                versiculoComplementar = "Amem os seus inimigos e orem por aqueles que os perseguem. (Mateus 5:44)",
                reflexao = "A resposta do Reino não é vingança, é transformação por meio do bem.",
                oracao = "Pai, ajuda-me a responder com graça onde antes eu reagiria com dureza.",
                aplicacao = "Escolha hoje uma resposta pacífica em uma situação difícil."
            ),
            DevotionalDay(
                dia = 21,
                titulo = "Fechando a jornada com esperança",
                versiculoPrincipal = "Que o Deus da esperança os encha de toda alegria e paz. (Romanos 15:13)",
                versiculoComplementar = "Sabemos que Deus age em todas as coisas para o bem daqueles que o amam. (Romanos 8:28)",
                reflexao = "Ao terminar esta jornada, você não termina seu crescimento. Deus continua trabalhando em sua vida.",
                oracao = "Senhor, obrigado por cada dia desta jornada. Continua aperfeiçoando a tua obra em mim.",
                aplicacao = "Olhe para trás e reconheça o que Deus já transformou em você."
            )
        )
    )

    private val plano30Dias = DevotionalPlan(
        id = "plano_30_dias",
        nome = "Jornada de 30 Dias",
        descricao = "Um plano mais profundo para criar constância espiritual, fortalecer a fé e desenvolver intimidade com Deus.",
        dias = listOf(
            DevotionalDay(
                dia = 1,
                titulo = "Novo começo",
                versiculoPrincipal = "As misericórdias do Senhor se renovam a cada manhã. (Lamentações 3:22-23)",
                versiculoComplementar = "Este é o dia que o Senhor fez. (Salmos 118:24)",
                reflexao = "Todo começo com Deus carrega esperança. Sua misericórdia é nova hoje.",
                oracao = "Pai, obrigado por este novo começo. Caminha comigo desde o primeiro dia.",
                aplicacao = "Comece este plano com uma oração sincera de entrega."
            ),
            DevotionalDay(
                dia = 2,
                titulo = "Confiança em Deus",
                versiculoPrincipal = "Confie no Senhor de todo o seu coração. (Provérbios 3:5)",
                versiculoComplementar = "Entregue o seu caminho ao Senhor. (Salmos 37:5)",
                reflexao = "A confiança cresce quando você escolhe depender de Deus mais do que de si mesmo.",
                oracao = "Senhor, ensina-me a confiar sem reservas.",
                aplicacao = "Entregue hoje uma decisão importante ao Senhor."
            ),
            DevotionalDay(
                dia = 3,
                titulo = "Esperança viva",
                versiculoPrincipal = "Temos essa esperança como âncora da alma. (Hebreus 6:19)",
                versiculoComplementar = "Cristo em vocês, a esperança da glória. (Colossenses 1:27)",
                reflexao = "A esperança em Cristo sustenta a alma quando tudo parece incerto.",
                oracao = "Pai, ancora minha alma na esperança que vem de Ti.",
                aplicacao = "Declare hoje em voz alta uma promessa bíblica sobre sua vida."
            ),
            DevotionalDay(
                dia = 4,
                titulo = "Paz do alto",
                versiculoPrincipal = "A minha paz lhes dou. (João 14:27)",
                versiculoComplementar = "A paz de Deus guardará os seus corações. (Filipenses 4:7)",
                reflexao = "A paz de Cristo é maior que o caos externo.",
                oracao = "Jesus, enche meu coração com a tua paz.",
                aplicacao = "Respire fundo e ore por alguns minutos sempre que a ansiedade vier."
            ),
            DevotionalDay(
                dia = 5,
                titulo = "Descanso da alma",
                versiculoPrincipal = "Eu lhes darei descanso. (Mateus 11:28)",
                versiculoComplementar = "Vocês encontrarão descanso para as suas almas. (Mateus 11:29)",
                reflexao = "Existe um descanso que o mundo não oferece: o descanso em Cristo.",
                oracao = "Senhor, alivia meu cansaço e meu peso interior.",
                aplicacao = "Separe um momento do dia para ficar em silêncio diante de Deus."
            ),
            DevotionalDay(
                dia = 6,
                titulo = "Palavra que sustenta",
                versiculoPrincipal = "Nem só de pão viverá o homem. (Mateus 4:4)",
                versiculoComplementar = "A palavra de nosso Deus permanece para sempre. (Isaías 40:8)",
                reflexao = "Sua alma precisa da Palavra tanto quanto seu corpo precisa de alimento.",
                oracao = "Pai, alimenta meu espírito com a tua verdade.",
                aplicacao = "Leia mais um trecho bíblico hoje além do devocional."
            ),
            DevotionalDay(
                dia = 7,
                titulo = "Primeira semana",
                versiculoPrincipal = "Até aqui nos ajudou o Senhor. (1 Samuel 7:12)",
                versiculoComplementar = "Deem graças em todas as circunstâncias. (1 Tessalonicenses 5:18)",
                reflexao = "Ao fim da primeira semana, reconheça a mão de Deus sustentando você.",
                oracao = "Obrigado, Senhor, por me manter de pé até aqui.",
                aplicacao = "Escreva 3 motivos de gratidão desta semana."
            ),
            DevotionalDay(
                dia = 8,
                titulo = "Coração guardado",
                versiculoPrincipal = "Guarde o seu coração. (Provérbios 4:23)",
                versiculoComplementar = "Bem-aventurados os puros de coração. (Mateus 5:8)",
                reflexao = "Cuidar do coração é proteger a fonte da sua vida espiritual.",
                oracao = "Senhor, guarda meu coração de tudo o que me afasta de Ti.",
                aplicacao = "Afaste-se hoje de algo que tem contaminado seus pensamentos."
            ),
            DevotionalDay(
                dia = 9,
                titulo = "Força em Deus",
                versiculoPrincipal = "Tudo posso naquele que me fortalece. (Filipenses 4:13)",
                versiculoComplementar = "O Senhor é a força da minha vida. (Salmos 27:1)",
                reflexao = "A força espiritual vem da dependência e não da autossuficiência.",
                oracao = "Pai, fortalece-me para viver este dia com fé.",
                aplicacao = "Enfrente hoje uma tarefa difícil confiando em Deus."
            ),
            DevotionalDay(
                dia = 10,
                titulo = "Deus cuida",
                versiculoPrincipal = "Ele tem cuidado de vocês. (1 Pedro 5:7)",
                versiculoComplementar = "O Senhor é o meu pastor. (Salmos 23:1)",
                reflexao = "O cuidado de Deus alcança as pequenas e grandes áreas da sua vida.",
                oracao = "Senhor, ajuda-me a descansar no teu cuidado fiel.",
                aplicacao = "Entregue conscientemente a Deus uma preocupação de hoje."
            ),
            DevotionalDay(
                dia = 11,
                titulo = "Alegria verdadeira",
                versiculoPrincipal = "A alegria do Senhor é a vossa força. (Neemias 8:10)",
                versiculoComplementar = "Regozijai-vos sempre. (1 Tessalonicenses 5:16)",
                reflexao = "A alegria do Senhor sustenta quando as circunstâncias não colaboram.",
                oracao = "Pai, faz brotar em mim uma alegria que vem da tua presença.",
                aplicacao = "Louve a Deus hoje por algo simples."
            ),
            DevotionalDay(
                dia = 12,
                titulo = "Sabedoria para viver",
                versiculoPrincipal = "Se algum de vocês tem falta de sabedoria, peça-a a Deus. (Tiago 1:5)",
                versiculoComplementar = "O temor do Senhor é o princípio da sabedoria. (Provérbios 9:10)",
                reflexao = "Deus não apenas responde; Ele orienta com sabedoria quem o busca.",
                oracao = "Senhor, dirige minhas escolhas com a tua sabedoria.",
                aplicacao = "Antes de decidir algo hoje, ore primeiro."
            ),
            DevotionalDay(
                dia = 13,
                titulo = "Amor em ação",
                versiculoPrincipal = "Amemo-nos uns aos outros. (1 João 4:7)",
                versiculoComplementar = "O amor é paciente, o amor é bondoso. (1 Coríntios 13:4)",
                reflexao = "Amor não é só sentimento; é atitude inspirada por Deus.",
                oracao = "Pai, ensina-me a amar com verdade e constância.",
                aplicacao = "Faça algo bondoso por alguém hoje."
            ),
            DevotionalDay(
                dia = 14,
                titulo = "Perdoar e seguir",
                versiculoPrincipal = "Perdoem como o Senhor lhes perdoou. (Colossenses 3:13)",
                versiculoComplementar = "O amor cobre multidão de pecados. (1 Pedro 4:8)",
                reflexao = "Perdão cura o coração e abre espaço para Deus agir.",
                oracao = "Senhor, ajuda-me a liberar perdão e a não guardar amargura.",
                aplicacao = "Ore hoje por alguém com quem você precisa se reconciliar."
            ),
            DevotionalDay(
                dia = 15,
                titulo = "Metade da jornada",
                versiculoPrincipal = "Perseverem na oração. (Romanos 12:12)",
                versiculoComplementar = "Orai sem cessar. (1 Tessalonicenses 5:17)",
                reflexao = "Na metade do caminho, a perseverança se torna ainda mais importante.",
                oracao = "Pai, ajuda-me a continuar firme e constante nesta jornada.",
                aplicacao = "Faça hoje uma oração mais longa e intencional."
            ),
            DevotionalDay(
                dia = 16,
                titulo = "Coragem diária",
                versiculoPrincipal = "Seja forte e corajoso. (Josué 1:9)",
                versiculoComplementar = "O Senhor está comigo, não temerei. (Salmos 118:6)",
                reflexao = "Coragem espiritual é obedecer a Deus mesmo quando há medo.",
                oracao = "Senhor, fortalece meu coração diante dos desafios.",
                aplicacao = "Dê hoje um passo que o medo vinha impedindo."
            ),
            DevotionalDay(
                dia = 17,
                titulo = "Santidade prática",
                versiculoPrincipal = "Sejam santos em tudo o que fizerem. (1 Pedro 1:15)",
                versiculoComplementar = "Sede santos, porque eu sou santo. (1 Pedro 1:16)",
                reflexao = "Santidade também aparece nas escolhas comuns do dia a dia.",
                oracao = "Pai, faz-me viver de forma que te glorifique.",
                aplicacao = "Escolha hoje uma atitude que reflita mais claramente seu compromisso com Deus."
            ),
            DevotionalDay(
                dia = 18,
                titulo = "Nada é impossível",
                versiculoPrincipal = "Nada é impossível para Deus. (Lucas 1:37)",
                versiculoComplementar = "Tudo é possível àquele que crê. (Marcos 9:23)",
                reflexao = "Deus continua sendo maior do que seus limites e impossibilidades.",
                oracao = "Senhor, aumenta minha fé no teu poder.",
                aplicacao = "Apresente hoje a Deus uma área que parece impossível."
            ),
            DevotionalDay(
                dia = 19,
                titulo = "Deus vê além",
                versiculoPrincipal = "O Senhor vê o coração. (1 Samuel 16:7)",
                versiculoComplementar = "Guarde o seu coração. (Provérbios 4:23)",
                reflexao = "Mais do que aparência, Deus se importa com a verdade interior.",
                oracao = "Pai, sonda meu coração e purifica minhas intenções.",
                aplicacao = "Peça hoje a Deus sinceridade diante Dele."
            ),
            DevotionalDay(
                dia = 20,
                titulo = "Paciência no processo",
                versiculoPrincipal = "O choro pode durar uma noite, mas a alegria vem pela manhã. (Salmos 30:5)",
                versiculoComplementar = "Sejam pacientes na tribulação. (Romanos 12:12)",
                reflexao = "Nem todo processo é rápido, mas Deus continua agindo.",
                oracao = "Senhor, dá-me paciência enquanto espero teu tempo.",
                aplicacao = "Escolha confiar hoje sem exigir respostas imediatas."
            ),
            DevotionalDay(
                dia = 21,
                titulo = "Três semanas com Deus",
                versiculoPrincipal = "Aquele que começou boa obra em vocês vai completá-la. (Filipenses 1:6)",
                versiculoComplementar = "Sabemos que Deus age em todas as coisas. (Romanos 8:28)",
                reflexao = "Deus não abandona a obra que Ele mesmo começou em você.",
                oracao = "Pai, continua moldando minha vida segundo a tua vontade.",
                aplicacao = "Olhe para trás e reconheça mudanças que já aconteceram."
            ),
            DevotionalDay(
                dia = 22,
                titulo = "Obediência que produz fruto",
                versiculoPrincipal = "Se guardarem os meus mandamentos, permanecerão no meu amor. (João 15:10)",
                versiculoComplementar = "O meu mandamento é este: amem-se uns aos outros. (João 15:12)",
                reflexao = "Obediência não é peso; é caminho de permanência no amor de Cristo.",
                oracao = "Senhor, ajuda-me a obedecer com alegria e sinceridade.",
                aplicacao = "Pratique hoje algo que Deus já mostrou que você deve fazer."
            ),
            DevotionalDay(
                dia = 23,
                titulo = "Firme contra o mal",
                versiculoPrincipal = "Resistam ao diabo, e ele fugirá de vocês. (Tiago 4:7)",
                versiculoComplementar = "Vistam toda a armadura de Deus. (Efésios 6:11)",
                reflexao = "A vida espiritual também exige firmeza e vigilância.",
                oracao = "Pai, fortalece-me para resistir ao mal e permanecer em Ti.",
                aplicacao = "Afaste-se hoje de uma tentação recorrente."
            ),
            DevotionalDay(
                dia = 24,
                titulo = "Verdade que liberta",
                versiculoPrincipal = "Conhecerão a verdade, e a verdade os libertará. (João 8:32)",
                versiculoComplementar = "Se o Filho os libertar, vocês de fato serão livres. (João 8:36)",
                reflexao = "Liberdade verdadeira nasce do encontro com Cristo e sua verdade.",
                oracao = "Jesus, liberta-me de tudo o que me prende longe de Ti.",
                aplicacao = "Reconheça hoje uma área em que você precisa da liberdade de Cristo."
            ),
            DevotionalDay(
                dia = 25,
                titulo = "Servir é grandeza",
                versiculoPrincipal = "Aquele que é o maior entre vocês deverá ser servo. (Mateus 23:11)",
                versiculoComplementar = "Pois quem se humilha será exaltado. (Mateus 23:12)",
                reflexao = "No Reino de Deus, grandeza se mede pelo serviço humilde.",
                oracao = "Pai, tira de mim o orgulho e ensina-me a servir.",
                aplicacao = "Sirva hoje alguém de forma simples e sincera."
            ),
            DevotionalDay(
                dia = 26,
                titulo = "Vida abundante",
                versiculoPrincipal = "Eu vim para que tenham vida, e a tenham plenamente. (João 10:10)",
                versiculoComplementar = "O Senhor é bom. (Naum 1:7)",
                reflexao = "Cristo não oferece apenas sobrevivência espiritual, mas vida abundante.",
                oracao = "Senhor, ensina-me a viver a plenitude que tens para mim.",
                aplicacao = "Faça hoje algo que fortaleça sua vida espiritual com intencionalidade."
            ),
            DevotionalDay(
                dia = 27,
                titulo = "Humildade e honra",
                versiculoPrincipal = "O orgulho precede a destruição. (Provérbios 16:18)",
                versiculoComplementar = "Com os humildes está a sabedoria. (Provérbios 11:2)",
                reflexao = "Humildade protege o coração e mantém você ensinável diante de Deus.",
                oracao = "Pai, livra-me do orgulho e dá-me um coração humilde.",
                aplicacao = "Reconheça hoje um erro sem justificar-se."
            ),
            DevotionalDay(
                dia = 28,
                titulo = "Deus sustenta até o fim",
                versiculoPrincipal = "Eu o segurarei com a minha mão direita vitoriosa. (Isaías 41:10)",
                versiculoComplementar = "Não tema; eu o ajudarei. (Isaías 41:13)",
                reflexao = "Quando sua força falha, a mão de Deus continua sustentando.",
                oracao = "Senhor, segura minha mão e ajuda-me a continuar.",
                aplicacao = "Lembre hoje de uma vez em que Deus já sustentou você."
            ),
            DevotionalDay(
                dia = 29,
                titulo = "Perseverança final",
                versiculoPrincipal = "Combati o bom combate, terminei a corrida, guardei a fé. (2 Timóteo 4:7)",
                versiculoComplementar = "Aquele que perseverar até o fim será salvo. (Mateus 24:13)",
                reflexao = "Perseverar é continuar fiel até o fim, um dia de cada vez.",
                oracao = "Pai, ajuda-me a permanecer fiel até o fim da minha jornada.",
                aplicacao = "Renove hoje seu compromisso com Deus em oração."
            ),
            DevotionalDay(
                dia = 30,
                titulo = "Jornada concluída",
                versiculoPrincipal = "Até aqui nos ajudou o Senhor. (1 Samuel 7:12)",
                versiculoComplementar = "Aquele que começou boa obra em vocês vai completá-la. (Filipenses 1:6)",
                reflexao = "Você concluiu esta jornada, mas a caminhada com Deus continua. Ele permanece aperfeiçoando sua vida dia após dia.",
                oracao = "Obrigado, Senhor, por cada dia desta jornada. Continua conduzindo minha vida e aprofundando minha comunhão contigo.",
                aplicacao = "Celebre esta conclusão e decida continuar cultivando sua rotina espiritual."
            )
        )
    )

    private val planos = listOf(
        plano7Dias,
        plano21Dias,
        plano30Dias
    )

    fun getAllPlans(): List<DevotionalPlan> {
        return planos
    }

    fun getPlanById(id: String): DevotionalPlan? {
        return planos.find { it.id == id }
    }
}