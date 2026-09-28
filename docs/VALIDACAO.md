# Validação do Geo Mania

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
