# Recomendações de próximos passos (priorizadas)

## 1) Estabilidade e confiança (prioridade máxima)
- Migrar chamadas de anúncios para modo **safe render** em todas as telas com fallback visual consistente.
- Adicionar telemetria de crashes e eventos (Firebase Crashlytics + Analytics).
- Criar testes instrumentados para os fluxos críticos: abrir Bíblia, favoritos, jornada e notificações.

## 2) Bíblia com experiência de app grande
- Salvar histórico de leitura (último livro/capítulo) e mostrar bloco "Continue lendo".
- Adicionar busca por **conteúdo de versículo** (não só nome do livro).
- Criar abas "Recentes", "Favoritos" e "Plano de leitura" na área Bíblia.

## 3) Premium (monetização real)
- Integrar **Google Play Billing** com assinatura mensal/anual.
- Colocar paywall com benefícios reais desbloqueáveis (sem anúncios, áudio premium, estudos guiados).
- Medir funil: visualização da aba premium -> clique -> início de checkout -> conversão.

## 4) UX e design system
- Consolidar estilos de botões, cards, títulos e espaçamentos em um arquivo de estilos comum.
- Definir componentes padrão (Card principal, botão primário/secundário, estados vazios).
- Revisar contraste e acessibilidade (TalkBack, tamanho mínimo de toque, tipografia dinâmica).

## 5) Performance
- Adotar DiffUtil/ListAdapter nas listas com atualização frequente.
- Fazer cache local dos dados bíblicos e pré-processamento para busca rápida.
- Medir tempo de abertura da Home e da Bíblia e definir meta de startup.

## 6) Operação e qualidade contínua
- Adicionar lint estático (ktlint/detekt) no CI.
- Definir checklist de release com smoke tests obrigatórios.
- Criar changelog por versão e política de rollback.

---

## Plano sugerido em 2 semanas
### Semana 1
1. Billing + paywall inicial.
2. Continue lendo na Bíblia.
3. Crashlytics + eventos de funil premium.

### Semana 2
1. Busca por conteúdo de versículos.
2. Refino de design system (styles e componentes).
3. Lint/quality gate no CI.
