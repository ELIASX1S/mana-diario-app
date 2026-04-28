# Maná Diário

Aplicativo Android cristão desenvolvido em Kotlin com foco em devocional diário, leitura bíblica, jornada espiritual, favoritos, notas, louvores e notificações.

O objetivo do projeto é oferecer uma experiência simples, bonita e constante para ajudar o usuário a se aproximar de Deus todos os dias.

## ✨ Funcionalidades

- Versículo do dia
- Reflexão diária
- Sequência diária de acesso
- Bíblia com livros, capítulos e versículos
- Busca bíblica
- Versículos favoritos
- Notas bíblicas
- Jornada devocional
- Louvores
- Mensagens de fé
- Compartilhamento de versículos e devocionais
- Copiar textos para a área de transferência
- Leitura em voz alta com Text-to-Speech
- Tema claro e escuro
- Configuração de tamanho da fonte
- Notificações diárias
- Monetização com anúncios AdMob
- Área Premium em preparação

## 🛠️ Tecnologias utilizadas

- Kotlin
- Android SDK
- AndroidX
- Material Design Components
- SharedPreferences
- Text-to-Speech
- AdMob / Google Mobile Ads
- Glide
- YouTube Player
- Shimmer
- Gradle Kotlin DSL

## 📱 Estrutura principal

O app é organizado em múltiplas telas e recursos, incluindo:

- `MainActivity` — tela principal e navegação inferior
- `HomeFragment` — devocional do dia, reflexão, sequência e ações rápidas
- `BibleBooksActivity` — listagem dos livros da Bíblia
- `BibleChaptersActivity` — capítulos bíblicos
- `BibleVersesActivity` — versículos bíblicos
- `BibleSearchActivity` — busca na Bíblia
- `BibleFavoritesActivity` — favoritos bíblicos
- `BibleNotesActivity` — notas bíblicas
- `JourneyActivity` — jornada devocional
- `ChoosePlanActivity` — escolha de plano devocional
- `PremiumActivity` — área Premium em preparação
- `SettingsActivity` — configurações do usuário
- `NotificationReceiver` — lembretes diários

## 💰 Monetização

O projeto já possui integração com anúncios via Google Mobile Ads / AdMob.

A área Premium está em preparação e deve ser integrada futuramente ao Google Play Billing antes de ser usada para assinaturas reais.

## 🚧 Status do projeto

Projeto em desenvolvimento ativo.

Próximas melhorias recomendadas:

- Integrar Google Play Billing para o Premium
- Revisar fluxo de anúncios para melhorar experiência do usuário
- Criar onboarding para novos usuários
- Melhorar screenshots e materiais para Play Store
- Revisar política de privacidade e dados coletados
- Adicionar testes básicos
- Criar changelog de versões

## 🚀 Como abrir o projeto

1. Clone o repositório:

```bash
git clone https://github.com/ELIASX1S/mana-diario-app.git
```

2. Abra o projeto no Android Studio.

3. Aguarde o Gradle sincronizar as dependências.

4. Execute o app em um emulador ou dispositivo Android.

## 📦 Build

O projeto utiliza Gradle Kotlin DSL.

Para gerar uma build de debug:

```bash
./gradlew assembleDebug
```

Para gerar uma build de release:

```bash
./gradlew assembleRelease
```

> Antes de publicar na Play Store, configure corretamente a assinatura do app, revise permissões, política de privacidade e dados coletados.

## 📌 Observações importantes

Este é um projeto de aprendizado e evolução prática em desenvolvimento Android.

Como a área Premium ainda não possui integração real com Google Play Billing, ela não deve ser usada como assinatura paga em produção até que a integração oficial seja implementada.

## 👨‍💻 Autor

Desenvolvido por Elias Moreira.
