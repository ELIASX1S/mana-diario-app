# Plano Sênior — Maná Diário (90 dias)

Este plano transforma o app em produto com padrão profissional, sem reescrever tudo de uma vez.

## 1) Base técnica (Semanas 1-2)
- Padronizar qualidade contínua no CI: `assembleDebug`, `testDebugUnitTest`, `lintDebug`.
- Definir política de versionamento (`semver`) e checklist de release.
- Congelar baseline de dívida técnica (bugs conhecidos + priorização).

## 2) Arquitetura e manutenção (Semanas 3-4)
- Evoluir para camadas explícitas: `ui`, `domain`, `data`.
- Introduzir `ViewModel` + `StateFlow` nas telas críticas (Home, Bíblia, Favoritos).
- Padronizar tratamento de erro com `Result`/sealed classes por fluxo.

## 3) Qualidade de produto (Semanas 5-8)
- Criar suíte mínima de testes:
  - unitários para repositórios e regras de negócio;
  - instrumentados para fluxos críticos.
- Adicionar monitoramento de crashes e eventos (ex.: Firebase Crashlytics + Analytics).
- Revisar UX de onboarding e paywall para reduzir abandono.

## 4) Pronto para escala (Semanas 9-12)
- Implementar feature flags para rollout seguro.
- Otimizar performance (startup time, jank, ANR).
- Criar pipeline de release com trilhas `internal`, `beta`, `prod`.

## Métricas que eu acompanharia semanalmente
- Crash-free users (%).
- ANR rate.
- Tempo de inicialização a frio (P50/P95).
- Retenção D1/D7.
- Conversão do conteúdo premium.

## Quick wins imediatos (essa semana)
1. Garantir Java 21 no CI para aderência ao toolchain do projeto.
2. Rodar `lintDebug` no pipeline para elevar padrão de qualidade.
3. Definir 5 testes unitários de alto valor (busca bíblica, favoritos, jornada, parsing JSON, preferências).
