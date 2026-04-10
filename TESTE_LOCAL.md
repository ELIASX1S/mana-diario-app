# Como testar o app (passo a passo)

## 1) Pré-requisitos
- JDK 17 instalado (`java -version`)
- Android SDK configurado (via Android Studio)
- Variável `ANDROID_HOME`/`ANDROID_SDK_ROOT` apontando para o SDK

## 2) Teste rápido (automatizado)
Na raiz do projeto:

```bash
./scripts/test_local.sh
```

Esse script executa:
1. `chmod +x gradlew`
2. `./gradlew :app:assembleDebug`
3. `./gradlew testDebugUnitTest`

Se você já tinha o app instalado e **não viu mudanças**, rode também:

```bash
./gradlew clean
```

Depois reinstale o APK debug gerado.

## 3) Teste manual da funcionalidade Premium
1. Abra o app.
2. Toque em **Mais**.
3. Toque em **Maná Premium**.
4. Verifique se a tela abre com:
   - título e benefícios,
   - botão mensal,
   - botão anual,
   - botão de contato.
5. Clique em **Assinar mensal** e **Assinar anual** e valide se aparece o toast de placeholder de checkout.
6. Clique em **Falar com suporte comercial** e confirme abertura do app de e-mail (quando disponível).

## 4) Testes de regressão essenciais
- **Favoritos**: adicionar/remover e compartilhar.
- **Bíblia**: abrir livros/capítulos e copiar versículos.
- **Mensagem de Fé**: gerar nova mensagem, copiar e compartilhar.
- **Jornada**: abrir plano ativo ou tela de escolha sem crash.

## 5) Se der erro no download do Gradle (proxy/rede)
Se aparecer erro como `HTTP 403` ao baixar o Gradle:
- teste em outra rede (sem proxy restritivo),
- configure proxy no `~/.gradle/gradle.properties`,
- ou rode no CI do GitHub Actions (quando disponível).
