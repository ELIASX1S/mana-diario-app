# Plano da nova grande atualização (V2)

## Recomendação principal
## **Maná Estudo 2.0**
Uma atualização focada em aumentar retenção e monetização com base no que já existe na aba Bíblia.

## Meta de produto
- Tornar o app o lugar principal para leitura e estudo diário.
- Converter usuários ativos em assinantes premium com valor claro.

## Escopo da V2

### 1) Hub de estudo diário
- trilha "Hoje" com leitura guiada,
- resumo do capítulo,
- perguntas de reflexão,
- botão "continuar" inteligente.

### 2) Anotações e destaques
- marcar versículos com cores,
- notas por versículo,
- filtro "somente anotados".

### 3) Busca poderosa
- busca por texto completo,
- histórico e buscas recentes,
- ranking de relevância.

### 4) Premium com valor real
- estudo guiado premium,
- plano avançado de 30/90 dias,
- áudio premium e download offline.

### 5) Base técnica
- Room para persistência local de notas/histórico,
- WorkManager para lembretes inteligentes,
- eventos de funil (analytics) por tela.

## Roadmap sugerido (6 semanas)

### Semanas 1-2
- modelagem local (Room) para notas, destaques e histórico.
- UI inicial de anotações em versículos.

### Semanas 3-4
- busca full-text e tela de resultados refinada.
- trilha "Hoje" com continuidade automática.

### Semanas 5-6
- paywall premium com benefícios V2.
- eventos e dashboard de métricas.

## Métricas-alvo
- retenção D7: +20%
- tempo médio por sessão: +25%
- conversão premium (trial/assinatura): +3~5%

## Critérios de pronto
- sem regressão nos fluxos atuais,
- cobertura mínima de testes nas telas novas,
- build e unit tests verdes no CI.
