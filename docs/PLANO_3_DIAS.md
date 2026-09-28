# Geo Mania em três dias

Plano vigente atualizado em 28/09/2026. Prazo comprimido para três dias totais, preservando o início em 27/09 e antecipando a entrega para **29/09/2026**. Substitui o plano de cinco dias.

O usuário confirmou em 28/09 que a primeira versão funcionou no celular. Isso valida o teste inicial informado; a nova versão com Escolhas da Terra precisa de nova conferência.

| Dia | Trabalho | Critério de conclusão |
| --- | --- | --- |
| 1 — 27/09 | Base Android, conteúdo, navegação, GeoMatch e recorde | Primeiro APK compilado e testado pelo usuário no celular (confirmação em 28/09) |
| 2 — 28/09 | Encadear linha do tempo e adicionar Escolhas da Terra | Quatro fases, 16 decisões, 48 opções, efeitos, explicações, resultados e novo APK com testes aprovados |
| 3 — 29/09 | Revisão final, correções e apresentação | Usuário testa os dois jogos, rotação, fonte grande e modo avião; grupo revisa conteúdo; APK final e ensaio de 3–5 minutos |

## Escopo da entrega

Conteúdo histórico, seis recursos, fontes, GeoMatch e Escolhas da Terra. Aplicativo Kotlin/Compose offline, sem novas bibliotecas, backend, login, multiplayer ou publicação em loja.

Escolhas da Terra contém quatro contextos independentes: sociedades caçadoras e coletoras, sociedades agrícolas, Revolução Industrial e sociedade contemporânea. Cada fase possui quatro decisões com três alternativas e indicadores de natureza, produção, sociedade e recursos. Nenhum indicador gera ranking ou resposta única correta. Os números são parâmetros didáticos fictícios, não dados históricos.

O jogador pode entrar ao final da linha do tempo, após o último detalhe histórico ou pelo início. Cada fase reinicia seus indicadores; os resultados ficam disponíveis durante a sessão, e a rotação mantém o estado. O GeoMatch continua disponível com recorde persistente.

## Prioridades até a entrega

1. Compilar e testar a versão com os dois jogos.
2. Instalar como atualização sobre a versão anterior, sem desinstalar, para conferir a manutenção do recorde.
3. Conferir as 16 situações e identificar eventuais dificuldades de leitura no celular.
4. Corrigir problemas do percurso principal e ensaiar a demonstração. Novos modos de jogo ficam fora deste prazo.

O plano é um roteiro de execução nesta conversa, não uma automação. Os testes pendentes estão em `VALIDACAO.md`.
