# Próximas melhorias para competir com Top 1 da Google Play

Objetivo: elevar o Maná Diário para padrão de app líder, focando em retenção, avaliação média e crescimento orgânico.

## Norte (KPIs principais)
- Nota média: **4.8+**
- Crash-free users: **99.7%+**
- ANR rate: **< 0.3%**
- Retenção D7: **35%+**
- Retenção D30: **20%+**
- Conversão premium: **3%+**

## Roadmap priorizado (impacto x esforço)

## P0 — Obrigatório (0-30 dias)
1. **Confiabilidade primeiro**
   - Integrar Crashlytics + alertas por regressão.
   - Fechar os 10 principais crashes/ANRs por volume.
2. **Velocidade percebida**
   - Reduzir cold start e travamentos em telas com listas/imagens.
   - Pré-carregar conteúdo essencial da Home.
3. **Onboarding orientado a hábito**
   - Primeiro uso com objetivo claro: “definir horário devocional + plano”.
   - Ativar notificação diária com preview de benefício.
4. **Solicitação inteligente de avaliação**
   - Pedir review só após “momento de valor” (ex.: 7 dias de sequência ou plano concluído).

## P1 — Diferenciação forte (30-60 dias)
1. **Personalização real**
   - Feed devocional por tema de interesse (ansiedade, família, propósito, fé).
   - Recomendação por histórico de leitura e favoritos.
2. **Experiência premium de conteúdo**
   - Séries guiadas de 7/21/40 dias com progresso visual.
   - Áudio narrado offline para devocionais e reflexões.
3. **Social leve (sem fricção)**
   - Compartilhamento com artes automáticas para stories.
   - Cartões de progresso semanal compartilháveis.

## P2 — Escala e crescimento (60-90 dias)
1. **ASO de alto nível**
   - A/B de ícone, título curto, screenshots e vídeo da loja.
   - Otimização por keywords cristãs de alta intenção.
2. **Monetização saudável**
   - Assinatura com teste grátis e benefícios claros.
   - Paywall contextual (sem interromper oração/leitura).
3. **Motor de experimentação**
   - Feature flags + experimentos A/B com métricas de sucesso por hipótese.

## Checklist técnico para “nível top app”
- Arquitetura: `ui/domain/data`, fluxo unidirecional, estados previsíveis.
- Qualidade: lint, testes unitários críticos, testes instrumentados de fluxo principal.
- Observabilidade: logs estruturados, tracing de tela, funil de onboarding.
- Segurança: política de dados transparente, consentimento e criptografia local básica.
- Release: trilhas `internal`, `closed`, `production` com rollback rápido.

## Backlog de features (Top 10)
1. Plano devocional adaptativo (IA simples por regras).
2. Widgets com versículo diário.
3. Modo offline robusto.
4. Busca bíblica semântica.
5. Highlights/underline em versículos.
6. Histórico de oração e gratidão.
7. Rotina matinal/noturna guiada.
8. Conteúdo em áudio com velocidade ajustável.
9. Metas semanais com streak recovery.
10. Comunidade moderada por temas.

## Ritmo de execução recomendado
- **Semanal:** release pequena + revisão de métricas.
- **Quinzenal:** 1 experimento de crescimento + 1 melhoria de retenção.
- **Mensal:** foco em um KPI principal (ex.: D7, rating, conversão premium).

## Regra de priorização
Implementar primeiro o que melhora simultaneamente:
1) retenção, 2) estabilidade, 3) avaliação média.
