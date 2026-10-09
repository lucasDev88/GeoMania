# Animações — Geo Mania 0.6.0

A versão acrescenta movimento à Home e aos três jogos, usando os recursos nativos de animação do Compose e a arte original do Camo.

- Home: entrada dos cartões com deslocamento, escala e rotação amortecida; cenário com sol, copas e pontos de luz em movimento. Camo tem movimento ambiente e salta e gira suavemente ao receber um toque.
- Navegação: telas entram com deslocamento lateral, escala e transparência; o retorno inverte o sentido.
- GeoMatch: cartas giram em perspectiva com mudança de face no meio do giro, mantendo o texto na orientação correta. O par encontrado tem um pequeno salto de escala. A explicação aguarda a abertura da segunda carta (540 ms), durante o bloqueio já previsto nas regras.
- Escolhas da Terra: números e barras percorrem gradualmente os novos valores. O leitor de tela recebe os valores finais, evitando anunciar cada quadro intermediário. O resultado inclui Camo e uma explosão breve de partículas.
- GeoExpedição: correnteza animada, saltos nas marcas da conexão, balanço de caminhada de Camo e expansão gradual da dica. A âncora do mascote também se desloca para evitar saltos perto do topo do mapa. Missões concluídas celebram com partículas e Camo.

As animações mantêm botões e alvos de toque disponíveis. Pontuações, decisões, pistas e validações continuam nos motores existentes. As partículas são decorativas e não entram na leitura por voz.

`MotionSettings` observa o estado das animações do Android. Quando o sistema as desativa, as mudanças passam diretamente ao estado final, a dica abre imediatamente e a explicação não espera o giro. Movimentos ambientes param quando o aplicativo sai do primeiro plano e quando os componentes saem da composição. Não são usados vídeos, downloads ou bibliotecas novas.

## Conferência no aparelho

1. Abrir a Home, tocar em Camo e navegar pelas quatro abas.
2. Jogar GeoMatch: virar cartas, errar um par, encontrar outro, abrir a explicação e concluir a partida.
3. Em Escolhas da Terra, observar indicadores e resultado após quatro decisões.
4. Na GeoExpedição, viajar à nascente, marcar uma conexão, abrir/fechar a dica e concluir uma missão.
5. Desativar as animações nas configurações do Android e repetir os percursos; ações e leitura por voz devem continuar disponíveis.
6. Alternar para outro aplicativo e voltar; conferir rotação, fonte grande e fluidez no celular.

O build e os testes JVM verificam compilação e regras. A fluidez e a aparência das animações exigem esta conferência física.
