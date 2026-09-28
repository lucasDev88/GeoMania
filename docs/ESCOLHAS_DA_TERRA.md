# Escolhas da Terra

Mini simulador offline acrescentado ao Geo Mania 0.2.0. Quatro fases com quatro situações cada, três alternativas por situação e resultados interpretativos. O acesso principal acompanha o término da linha do tempo; há atalho no início.

## Como funciona

Cada fase começa com seus próprios indicadores de 0 a 100. Escolher aplica os efeitos uma única vez e exibe uma explicação; o jogador precisa continuar antes da próxima decisão. As mudanças realmente aplicadas aparecem nos indicadores, respeitando os limites. Não há cronômetro, derrota automática, alternativa universalmente correta ou ranking.

Após quatro decisões, o resultado compara os valores finais e iniciais, resume as mudanças e permite rever as escolhas. Os resultados das fases ficam na sessão. Repetir uma fase substitui seu resultado anterior; as outras são preservadas. A rotação mantém o estado; sair do modo ou encerrar o processo descarta a sessão.

## Conteúdo e interpretação

As situações são ficcionais e inspiradas em relações entre território, técnica, produção e acesso. Não representam decisões literais de um povo específico. Os efeitos numéricos são escolhas de modelagem didática, não valores retirados das fontes nem previsões. Sociedade resume bem-estar e acesso, sem descrever todas as desigualdades. Recursos representa disponibilidade para usos futuros, sem transformar combustíveis fósseis em recursos renováveis.

As fases não constituem uma escala de superioridade nem uma sequência histórica obrigatória. Os indicadores iniciais não devem ser usados para comparar sociedades reais. As opções explicitam benefícios e custos, incluindo deslocamento de impactos para outros territórios.

Referências temáticas consultadas em 28/09/2026:

- [UNESCO: conhecimentos indígenas e locais](https://www.unesco.org/en/links/ipbes) — diversidade e relação com territórios; não é fonte para os números ou uma descrição universal de caçadores e coletores.
- [FAO: terra, solo e água](https://www.fao.org/land-water/overview/Land-soil-water-and-the-sustainable-development-goals/) — produção de alimentos, manejo e competição por água.
- [Our World in Data: história das fontes de energia](https://ourworldindata.org/global-energy-200-years) — contexto da importância do carvão na industrialização.
- [PNUMA: Global Resources Outlook 2024](https://www.unep.org/resources/Global-Resource-Outlook-2024) — extração, consumo e demanda material.

## Manutenção

O arquivo `app/src/main/assets/earth_choices.json` contém as quatro fases e todos os textos específicos. `choices/ChoicesEngine.kt` contém as regras; `ChoicesViewModel.kt` mantém o estado; `ui/ChoicesScreen.kt` renderiza o jogo. Nenhuma nova dependência foi adicionada.

O teste percorre as 81 combinações de cada fase, 324 no total, verificando limites, conclusão e histórico. Há testes adicionais de cliques duplicados, reinício, referências, exemplo do carvão e cálculo da conclusão. A revisão humana dos textos e a validação visual no celular completam a avaliação.
