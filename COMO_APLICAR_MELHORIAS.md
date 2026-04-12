# Como aplicar as melhorias (passo a passo)

Este guia coloca em prática as prioridades do `MELHORIAS.md` com ações objetivas.

## 1) Segurança de segredos
1. Garanta que arquivos sensíveis não sejam versionados (`.jks`, `.keystore`, `.pem`, `.p12`).
2. Salve chaves somente em segredo do CI/CD (GitHub Secrets, por exemplo).
3. Se algum segredo já foi exposto, faça rotação imediata.

## 2) CI básico (já configurado neste repositório)
O workflow em `.github/workflows/android-ci.yml` já executa:
- build debug (`:app:assembleDebug`)
- testes unitários (`testDebugUnitTest`)

Para validar localmente:
```bash
chmod +x gradlew
./gradlew :app:assembleDebug
./gradlew testDebugUnitTest
```

## 3) Próximo incremento recomendado
1. Adicionar ktlint/detekt ao Gradle.
2. Incluir etapa de lint no workflow.
3. Corrigir avisos por prioridade (primeiro os críticos).

## 4) Métrica mínima para qualidade
- Build verde em toda PR.
- Testes unitários passando.
- Sem regressão de funcionalidades centrais (busca bíblica, favoritos, notificações).
