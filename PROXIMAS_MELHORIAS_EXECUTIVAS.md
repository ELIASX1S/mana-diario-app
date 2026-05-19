# Próximas melhorias (plano executivo v2)

## Objetivo
Aumentar retenção, avaliação da loja e conversão premium com entregas quinzenais pequenas, rastreáveis e com dono definido.

## Prioridades (próximos 45 dias)

### P0 — Retenção e valor percebido (semana 1-2)
1. **Histórico recente da Bíblia (últimos 5 capítulos)**
   - Mostrar card de “Recentes” na entrada da Bíblia.
   - Abrir direto no capítulo/versículo salvo.
2. **Busca bíblica por texto (local)**
   - Campo único para buscar por palavra/frase em versículos.
   - Resultado com livro/capítulo/versículo + highlight.
3. **Atalhos rápidos na Bíblia**
   - Botões “Favoritos” e “Notas” no topo da tela de livros.

### P1 — Engajamento e hábito (semana 3-4)
1. **Lembretes com qualidade**
   - Ajustar janela de envio (evitar horário de silêncio do usuário).
   - Mensagem dinâmica por contexto (manhã/noite).
2. **Pedido de avaliação contextual**
   - Solicitar review somente após momento de valor (7 dias streak, plano finalizado).
3. **Metas semanais visuais**
   - Mini painel com progresso semanal (dias lidos, meta e recuperação de streak).

### P2 — Base para crescimento (semana 5-6)
1. **Crashlytics + eventos essenciais**
   - Eventos: abertura home, início leitura, conclusão plano, clique premium.
2. **Hardening de performance**
   - Melhorar tempo de abertura das telas da Bíblia.
   - Reduzir jank em listas e rolagem de versículos.
3. **Premium com proposta clara**
   - Comparativo mensal/anual e benefícios concretos.

## Entregáveis por feature (Definition of Done)
Cada melhoria só é “concluída” quando atender:
1. **Código + UX** entregues.
2. **Métrica instrumentada** (evento/contador).
3. **Teste mínimo** (unitário ou instrumentado no fluxo principal).
4. **Validação manual** documentada no PR.

## KPIs e metas de curto prazo
- Retenção D7: **+10%** em 45 dias.
- Avaliação média: **+0.2** pontos.
- Sessões > 5 min: **+15%**.
- Crash-free users: **99.5%+**.

## Backlog imediato (ordem sugerida)
1. Histórico recente da Bíblia.
2. Busca por texto local.
3. Atalhos Favoritos/Notas.
4. Painel de metas semanais.
5. Instrumentação de eventos críticos.
6. Paywall premium revisado.

## Risco principal e mitigação
- **Risco:** excesso de escopo em uma única release.
- **Mitigação:** manter PRs pequenos (1 feature por PR) e release semanal incremental.
