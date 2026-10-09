# GeoExpedição e Camo — entrega de 09/10/2026

GeoExpedição é o terceiro jogo do Geo Mania. Funciona offline em um mapa esquemático fictício do Vale das Conexões, com oito locais: cidade, rio, plantação, nascente, floresta, estrada, mina e fábrica. Os números do mapa correspondem aos botões de destino abaixo dele. Os caminhos mostram os trajetos; as setas laranja representam a conexão investigada, que pode ser diferente do caminho percorrido.

## Como jogar

Escolha uma missão. Toque num destino e aguarde Camo chegar. Leia a pista no caderno, explore outros locais e ative **Marcar conexão no mapa**. Toque nos locais visitados na sequência pedida pela missão. **Conferir conexão** verifica as evidências e a ordem. Tocar numa marca desfaz essa marca e as seguintes; Limpar permite tentar outra conexão. As dicas ajudam sem encerrar a investigação.

Há três missões e 24 pistas:

- O mistério do rio: investigar o transporte de resíduos da plantação pelo rio até a cidade. O registro da fábrica permite distinguir proximidade de evidência de origem.
- A floresta fragmentada: comparar registros antigos e atuais para identificar a relação entre a estrada e a fragmentação.
- A cidade e seus recursos: acompanhar o lote de metal da extração à transformação e ao consumo.

Os registros, amostras, lotes e acontecimentos são fictícios. As fontes fundamentam os conceitos ambientais e geográficos; não documentam este território inventado. A sequência é uma resposta ao caso apresentado, não uma regra universal sobre sociedades.

O ViewModel mantém a exploração durante rotação. As missões concluídas ficam marcadas durante a sessão desta tela. Voltar às missões reinicia a tentativa após confirmação; sair do modo ou encerrar o processo descarta a sessão. O recorde do GeoMatch permanece no DataStore.

## Mascote

**Camo** é um camaleão explorador verde e turquesa, com mochila, lupa e cauda enrolada. Aparece na Home, na seleção das missões, percorre o mapa e celebra cada conclusão. A ilustração PNG tem fundo transparente e está em `app/src/main/res/drawable-nodpi/camo_mascot.png`.

Método: geração original pela ferramenta integrada `image_gen.imagegen`, sem imagem de referência; a saída foi copiada para o recurso Android sem edição. A arte é um arquivo local incorporado ao APK.

Prompt usado:

> Use case: illustration-story. Asset type: transparent mascot sprite for an educational Android geography game. ONE original friendly green and turquoise chameleon explorer, full body, large curious eyes, clearly curled spiral tail, small warm yellow accents, tiny explorer backpack and magnifying glass. Polished charming 2D game illustration with clean bold contours and restrained shading, readable at small sizes. Three-quarter view facing slightly right, centered square frame, complete tail and feet visible. Transparent background, no ground, no scene, no text, no logo, no watermark, no other characters.

## Conferência no celular

Instale `entrega/GeoMania-0.6.0-debug.apk` como atualização. Entre pela Home, visite locais, leia as pistas, use a dica, erre uma conexão e corrija; conclua as três missões. Confira rotação durante viagem, retorno com confirmação, fonte grande, modo avião e o recorde anterior do GeoMatch. A verificação automatizada está em `VALIDACAO.md`; a aparência final no aparelho depende deste teste manual.
