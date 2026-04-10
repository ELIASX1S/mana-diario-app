# Sugestões de melhoria (priorizadas)

## 1) Segurança (alta prioridade)
- **Nunca versionar arquivos de assinatura/certificado** (`.jks`, `.keystore`, `.pem`, `.p12`).
- Manter credenciais apenas via variáveis de ambiente/CI secrets.
- Revisar histórico do Git para remover segredos já publicados (com rotação das chaves).

## 2) Qualidade técnica
- Padronizar nomes de arquivos e classes (ex.: revisar arquivos com nome duplicado/suspeito como `*.k.kt`).
- Separar camadas de UI, dados e domínio para reduzir acoplamento.
- Adotar regras de lint/format automáticas no CI (ktlint + detekt).

## 3) Confiabilidade
- Cobrir casos críticos com testes instrumentados e unitários:
  - leitura e busca bíblica,
  - favoritos,
  - notificações diárias,
  - fluxo de plano devocional.
- Definir meta mínima de cobertura para módulos críticos.

## 4) Produto e UX
- Internacionalização: mover textos hardcoded para `strings.xml`.
- Melhorar acessibilidade (contentDescription, contraste e tamanhos de toque).
- Incluir estados de erro/vazio/carregamento em todas as listas/telas de conteúdo.

## 5) Performance
- Evitar parsing repetitivo de JSON pesado em tempo de UI: usar cache local.
- Revisar uso de RecyclerView/Adapters com DiffUtil e payload updates.
- Medir tempo de inicialização e otimizar carga inicial.

## 6) Operação e manutenção
- Criar pipeline de CI com etapas mínimas: build + lint + testes.
- Versionar changelog e critérios de release.
- Criar documentação curta de arquitetura e convenções do projeto.
