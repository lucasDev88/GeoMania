# Validação do Geo Mania

## Versão 0.6.0 — animações (09/10/2026)

Entradas com mola, deslocamento e escala; navegação com profundidade; cenário e Camo em movimento, saudação ao toque, giro em perspectiva no GeoMatch, indicadores graduais, correnteza no mapa, marcas com salto e comemorações de partículas. Detalhes em `ANIMACOES.md`.

- [x] Compilação final: BUILD SUCCESSFUL em 09/10/2026, 24 testes JVM sem falhas e lint com zero erros e 13 avisos preexistentes de SDK, dependências e qualificador de ícone. Log: `.tools/motion-release-build.log`.
- [x] APK 0.6.0, versionCode 6, assinatura válida e igual à 0.5.0, sete JSONs idênticos às fontes e sprite Camo preservado. Cópia final: `entrega/GeoMania-0.6.0-debug.apk`.
- [ ] Conferir no celular: giro e fechamento de cartas, explicação após abertura, toque no Camo, viagem e chegada à nascente, barras e partículas nas três modalidades.
- [ ] Desativar animações do Android e verificar estados imediatos e ausência de movimento ambiente; voltar ao app após pausa.

Animações não alteram regras e valores; o leitor de tela recebe estados finais. O observador das configurações e o ciclo de vida controlam efeitos decorativos. A fluidez física e a aparência final permanecem dependentes da conferência no aparelho.


## Versão 0.5.0 — identidade visual e acessibilidade (09/10/2026)

A identidade do mascote agora aparece também no ícone adaptável e redondo de Android, com variante de ícone tradicional. A barra de navegação inferior oferece atalhos consistentes para Home, linha do tempo, recursos e mapa; o Camo aparece na barra superior e dá boas-vindas na Home. A paleta, as formas e os cartões ganham mais contraste e consistência. A GeoExpedição anuncia viagens e a localização atual ao leitor de tela e mantém o retrato dos oito destinos; Escolhas da Terra informa progresso como barra acessível.

- [x] Implementado sem alterar conteúdo, decisões, regras ou referências das três missões.
- [x] APK 0.5.0 gerado: `entrega/GeoMania-0.5.0-debug.apk`, versão de pacote 0.5.0 e code 5; ícones adaptável e redondo referenciados pelo manifesto e tema monocromático incluído.
- [ ] Instalar como atualização; conferir o ícone adaptável e redondo no aparelho, sem desinstalar a versão anterior.
- [ ] Conferir quatro abas, leitura pelo TalkBack, fonte ampliada, avanço das decisões e anúncio de chegada do Camo no mapa.
- [ ] Revisar em tela pequena a arte e o texto de abertura da Home.

`assembleDebug lintDebug`: BUILD SUCCESSFUL em 09/10/2026, zero erros e 13 avisos de atualizações SDK/dependências e configuração de ícone API 26. O APK foi assinado com a mesma chave da versão 0.4.0. Testes JVM não foram executados nesta atualização. O roteiro de GeoExpedição permanece em `GEOEXPEDICAO.md`.


## Versão 0.4.0 — GeoExpedição e Camo (09/10/2026)

Mapa esquemático fictício com oito locais, caminhos conectados, viagem animada de Camo, coleta de pistas ao chegar ao destino, caderno, dicas, marcação ordenada das conexões e conclusões. Três missões e 24 pistas; acesso pela Home junto aos outros dois jogos. Arte original do camaleão com transparência, incorporada localmente ao APK.

- [x] Validação independente dos sete JSONs, lugares e referências.
- [x] 24 testes JVM sem falhas: oito novos testes cobrem os 64 pares de destinos, coleta apenas no destino, comandos durante viagem, marcas em locais visitados, evidências obrigatórias, correção da ordem, três conclusões e progresso da sessão.
- [x] Build final e lint: BUILD SUCCESSFUL em 09/10/2026, zero erros e 12 avisos de versões de dependências/SDK e configuração Android já existente. Log: `.tools/expedition-final-build.log`.
- [x] Compilação do APK 0.4.0 com versionCode 4 e mesmo identificador. Os sete JSONs do APK são idênticos às fontes e a arte Camo está presente, RGBA 1254 × 1254, com alpha de 0 a 255.
- [x] Assinatura debug válida e igual à versão 0.3.0; atualização compatível com preservação do DataStore.
- [ ] Teste manual no aparelho da versão 0.4.0: instalação sobre a anterior, Home, mapa, toques, leitura, animação, dica, erro e correção da conexão, três conclusões.
- [ ] Rotação durante viagem e marcação; botão Voltar e confirmação; saída e entrada no modo.
- [ ] Fonte a 200%, tela pequena e modo avião; recorde anterior do GeoMatch.

As conclusões ambientais pertencem a casos fictícios e não são medições ou provas sobre locais reais. As três missões têm respostas determinadas por suas pistas; Escolhas da Terra continua permitindo diferentes prioridades sem alternativa universalmente correta. A verificação automatizada não substitui a conferência visual no celular. Nenhum push foi realizado.


## Versão 0.3.0 — Home e linha do tempo

Home redesenhada em Compose, ilustração vetorial local sem download de imagens, cartões distintos dos dois jogos e painel do GeoMatch. Linha do tempo com sete etapas visuais, resumos, perguntas de reflexão, posição do contexto e navegação anterior/próxima. Textos específicos dos contextos permanecem em `periods.json`.

- [x] JSONs: resumos e perguntas presentes nos sete contextos; referências e conteúdo dos jogos válidos.
- [x] Build, testes JVM e lint da versão 0.3.0: compilação bem-sucedida, 16 testes sem falhas e lint sem erros em 28/09/2026. Conteúdo do APK conferido e assinatura igual à versão 0.2.0.
- [ ] Verificação visual no celular: Home, etapas e detalhes em tela pequena e com fonte a 200%.
- [ ] Abrir os dois jogos e biblioteca pela Home; conferir recorde após atualização.
- [ ] Abrir todos os contextos; avançar, voltar e entrar no simulador pelo último contexto.

O repositório `lucasDev88/GeoMania` foi acessado com sucesso em 28/09/2026 e corresponde ao remoto `origin`. As alterações desta etapa são locais; nenhum push foi realizado.

## Versão 0.2.0 e prazo de três dias

Em 28/09/2026 o usuário confirmou que a versão anterior funcionou no seu celular. Modelo e versão Android não foram informados, nem a execução de todos os cenários abaixo. A confirmação se refere à versão 0.1.0; Escolhas da Terra exige novo teste no aparelho.

Novo conteúdo: quatro fases, 16 decisões, 48 alternativas; acesso pelo início, final da linha do tempo e último detalhe histórico. O plano vigente está em `PLANO_3_DIAS.md`.

- [x] Validação dos JSONs de Escolhas da Terra, referências e efeitos com benefícios e custos.
- [x] Compilação, testes e lint da versão 0.2.0: `BUILD SUCCESSFUL` em 28/09/2026. Dezesseis testes, zero falhas; seis testes novos cobrem também os 324 percursos possíveis. Lint sem erros.
- [x] Os seis JSONs finais foram conferidos no APK; assinatura verificada e igual à versão anterior. Identificador preservado, versão 0.2.0 e versionCode 2.
- [ ] Instalar como atualização e conferir o recorde anterior do GeoMatch.
- [ ] Percorrer os detalhes históricos até o botão do novo jogo.
- [ ] Concluir as quatro fases e conferir explicações, indicadores e resultados da sessão.
- [ ] Tocar rapidamente em opções e em Continuar; cada decisão deve alterar os indicadores uma única vez.
- [ ] Repetir uma fase com escolhas diferentes e conferir os novos resultados.
- [ ] Girar o aparelho no simulador, inclusive durante a explicação e no resultado.
- [ ] Conferir rolagem com fonte grande, leitura por TalkBack e funcionamento offline.
- [ ] Conferir Voltar, cancelamento da saída e reinício da fase.

Entrega atual: `entrega/GeoMania-0.2.0-debug.apk`, também disponível como `entrega/GeoMania-debug.apk`. A versão anterior foi preservada como `entrega/GeoMania-0.1.0-debug.apk`. Não é necessário desinstalar a versão antiga para atualizar.

## Verificações automatizadas da versão 0.1.0

- [x] JSONs: contagens, identificadores únicos, seis pares, referências e vínculos para períodos e recursos.
- [x] Build Android `assembleDebug`: APK debug gerado.
- [x] Testes JVM `testDebugUnitTest`: 10 testes, zero falhas e zero erros. Seis de regras, três de ViewModel e um de leitura dos JSONs reais.
- [x] Análise estática `lintDebug`: zero erros e 12 avisos. Onze indicam versões mais recentes de SDK/Gradle/bibliotecas; um recomenda declarar também `fullBackupContent` para Android anterior ao 12. O manifesto já desativa backup com `allowBackup=false` e contém regras para Android 12+. Os avisos não foram suprimidos.
- [x] Assinatura do APK validada por `apksigner verify`.

Os cinco JSONs empacotados no APK foram comparados byte a byte com os arquivos finais de conteúdo, sem divergências. Código compilado e testes automatizados não equivalem a funcionamento confirmado no aparelho.

Execução final em 27/09/2026: `testDebugUnitTest assembleDebug lintDebug`, resultado `BUILD SUCCESSFUL`. Entrega em `entrega/GeoMania-debug.apk`, com checksum em `entrega/SHA256.txt`. O APK requer Android 8 ou superior e não declara permissão de internet.

## Testes de aceitação no Android

Registrar modelo do aparelho, versão Android, data e problemas encontrados. Os itens abaixo permanecem pendentes até execução real.

- [x] Instalar o APK 0.1.0 e abrir: funcionamento confirmado pelo usuário em 28/09/2026.
- [ ] Abrir início, sete contextos e seus detalhes, seis recursos e seus detalhes, fontes e jogo.
- [ ] Repetir a navegação em modo avião; textos e jogo devem funcionar.
- [ ] Conferir textos e recortes históricos com o grupo/professor.
- [ ] Jogar uma partida com erros e acertos; somente pares corretos permanecem abertos.
- [ ] Tocar rapidamente em várias cartas; uma tentativa não pode aceitar uma terceira carta.
- [ ] Encontrar o sexto par, ler a explicação e ver o resultado.
- [ ] Reabrir o app; recorde e contagem de partidas devem permanecer.
- [ ] Reiniciar uma partida com cartas abertas; tentativas e tempo voltam a zero.
- [ ] Girar o celular durante a partida; cartas e tentativas devem permanecer.
- [ ] Colocar o app em segundo plano por dez segundos; tempo de jogo deve pausar.
- [ ] Aumentar a fonte para 200%; verificar rolagem e legibilidade das cartas.
- [ ] Usar TalkBack; cartas fechadas devem anunciar posição, sem revelar o conteúdo.
- [ ] Abrir uma fonte com rede; retornar ao app.
- [ ] Demonstrar em 3–5 minutos, conforme `DEMONSTRACAO.md`.

## Limites conhecidos

- A partida em andamento é mantida durante rotação, mas não é restaurada depois de encerrar o processo ou sair da tela do jogo.
- Links para sites exigem internet; os conteúdos didáticos e referências bibliográficas estão no APK.
- O APK debug destina-se a avaliação e apresentação. Assinatura de distribuição e loja não fazem parte desta etapa.
- O usuário confirmou funcionamento da primeira versão no celular. A revisão visual detalhada e os novos fluxos da versão 0.2.0 permanecem pendentes.
- Os resultados de Escolhas da Terra são mantidos na sessão, inclusive após rotação. Encerrar o processo ou sair do modo descarta esses resultados; o recorde persistente do GeoMatch não é alterado.
