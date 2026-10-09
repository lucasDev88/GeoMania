"""Initial fictional map cases. Edit expedition.json directly after review."""
import json
from pathlib import Path

places = [
    ('nascente', 'Nascente', 'agua', .16, .12),
    ('floresta', 'Floresta', 'floresta', .63, .12),
    ('estrada', 'Estrada', 'estrada', .65, .30),
    ('mina', 'Mina', 'mina', .87, .39),
    ('plantacao', 'Plantação', 'plantacao', .17, .43),
    ('fabrica', 'Fábrica', 'fabrica', .68, .55),
    ('rio', 'Rio', 'agua', .31, .70),
    ('cidade', 'Cidade', 'cidade', .74, .88),
]
roads = [('cidade','rio'), ('cidade','fabrica'), ('rio','plantacao'), ('plantacao','nascente'),
         ('plantacao','estrada'), ('estrada','floresta'), ('estrada','mina'), ('estrada','fabrica')]
def mission(id, title, objective, instruction, clues, essential, routes, hint, conclusion, sources):
    return dict(id=id, titulo=title, objetivo=objective, instrucao=instruction,
        pistas=[dict(local=k, titulo=v[0], texto=v[1]) for k,v in clues.items()],
        essenciais=essential, rotas=routes, dica=hint, conclusao=conclusion, fontes=sources)

missions = [
mission('agua', 'O mistério do rio',
    'Depois de uma chuva forte, a água que chega à cidade mudou. Descubra a origem do material e marque o caminho até os moradores.',
    'Marque três locais: origem do material → trecho do rio que o recebe → cidade afetada. A correnteza segue da nascente em direção à cidade.', {
    'cidade': ('Relato dos moradores', 'Neste caso fictício, a água ficou turva depois da chuva. O registro da coleta mostra material chegando pelo rio, antes da captação.'),
    'rio': ('O que a correnteza trouxe', 'As amostras deste cenário mostram solo e resíduos agrícolas entrando no trecho abaixo da plantação e seguindo para a cidade.'),
    'plantacao': ('O canal de escoamento', 'O terreno tem pouca cobertura perto do canal. Na chuva do caso, água carregou solo e resíduos da plantação para o rio.'),
    'fabrica': ('Uma hipótese descartada', 'Os registros fictícios mostram saída de água tratada, sem o material observado na amostra do rio. A proximidade da fábrica, sozinha, não comprova a origem.'),
    'nascente': ('A montante', 'A amostra da nascente, coletada antes do trecho da plantação, não contém o material investigado. A posição no rio ajuda a comparar as pistas.'),
    'mina': ('Outro material', 'A equipe do caso encontrou rochas no depósito da mina. Elas não correspondem ao material das amostras usadas nesta missão.'),
    'floresta': ('Cobertura do terreno', 'Neste trecho do cenário, a vegetação cobre o solo. A equipe não encontrou ali o canal que levou o material investigado ao rio.'),
    'estrada': ('O registro da chuva', 'O diário local confirma a chuva forte antes da mudança da água. A estrada serve para deslocamento, mas não explica sozinha a origem do material.'),
    }, ['plantacao','rio','cidade','fabrica'], [['plantacao','rio','cidade']],
    'Compare a amostra do rio com o canal da plantação e verifique os registros da fábrica. Depois siga a direção da água.',
    'Caso resolvido: no cenário, o material saiu da plantação, entrou no rio e chegou à cidade. Uso do solo, chuva e direção da água conectam territórios. A fábrica próxima foi investigada, mas suas pistas não correspondiam ao caso. A conclusão vale para esta história fictícia, não para qualquer rio real.', [6,1]),
mission('floresta', 'A floresta fragmentada',
    'O mapa antigo mostrava uma faixa contínua de vegetação. Agora há dois trechos separados. Investigue qual mudança criou essa divisão.',
    'Marque dois locais: intervenção que cortou a vegetação → floresta afetada. A linha marcada representa causa e efeito, não o caminho de Camo.', {
    'cidade': ('Mapas de duas épocas', 'O mapa antigo do caso mostra vegetação contínua. O novo mostra uma faixa aberta cortando essa área, depois da construção de uma via.'),
    'floresta': ('Dois trechos separados', 'No local, Camo encontra vegetação nos dois lados da faixa aberta. O relatório fictício registra a interrupção da ligação entre esses trechos.'),
    'estrada': ('A faixa recém-aberta', 'O diário de obras do caso registra retirada de vegetação para abrir esta estrada justamente entre os trechos antes conectados.'),
    'mina': ('Uma atividade mais antiga', 'Neste cenário, a área da mina não mudou entre os dois mapas. A existência de mineração não prova que ela criou a divisão observada.'),
    'plantacao': ('Os limites do cultivo', 'Os mapas usados na missão mostram a plantação com os mesmos limites antes e depois da separação da floresta.'),
    'fabrica': ('A localização da obra', 'O registro desta fábrica indica uma área distante da faixa investigada. A pista relevante deve combinar localização e momento da mudança.'),
    'rio': ('Uma conexão diferente', 'O curso do rio não mudou nos mapas do caso. A nova faixa aberta tem a forma da estrada, não do canal do rio.'),
    'nascente': ('A vegetação ao redor', 'A nascente continua cercada de vegetação nos registros. Essa pista ajuda a distinguir a mudança recente de elementos que já existiam.'),
    }, ['cidade','floresta','estrada'], [['estrada','floresta']],
    'Compare o mapa antigo da cidade com o diário de obras da estrada. Procure a intervenção feita entre os dois registros.',
    'Caso resolvido: a abertura da estrada separou os trechos de floresta neste território fictício. Infraestrutura muda o uso do espaço e pode interromper conexões da vegetação. Comparar localização, data e evidências permite explicar a mudança sem atribuí-la automaticamente à atividade mais próxima.', [1,3,5]),
mission('recursos', 'A cidade e seus recursos',
    'A escola recebeu mesas metálicas. Descubra a cadeia que conecta os materiais dessas mesas à cidade e marque sua trajetória produtiva.',
    'Marque três locais: onde a matéria-prima é extraída → onde ela é transformada → onde o produto é usado.', {
    'cidade': ('As mesas da escola', 'O comprovante fictício de entrega informa que as mesas foram feitas com o lote metálico M7, vindo da fábrica deste mapa.'),
    'fabrica': ('Do minério ao produto', 'O registro do cenário conecta o lote M7 às mesas. Esta fábrica transforma o material recebido da mina em componentes metálicos para a escola.'),
    'mina': ('A origem do lote', 'O registro de saída do caso identifica o minério do lote M7, enviado à fábrica para transformação. É a origem documentada desta cadeia.'),
    'estrada': ('O transporte', 'Os veículos do cenário usam a estrada para transportar materiais. Transporte liga as etapas, mas esta missão pede extração, transformação e uso.'),
    'plantacao': ('Outra cadeia', 'O registro da plantação trata de alimentos para a cidade. Ele não corresponde ao lote de material metálico investigado.'),
    'floresta': ('Materiais diferentes', 'Há madeira usada em outros produtos do cenário. O comprovante das mesas investigadas, porém, identifica componentes metálicos do lote M7.'),
    'rio': ('Água na produção', 'A água tem usos produtivos e de abastecimento. Essa relação importa, mas não identifica a origem do material metálico pedido nesta missão.'),
    'nascente': ('O abastecimento', 'A nascente participa do sistema de água do território. A missão atual acompanha a matéria-prima das mesas, não a rede de abastecimento.'),
    }, ['cidade','fabrica','mina'], [['mina','fabrica','cidade']],
    'Siga o código M7: registro de extração na mina, transformação na fábrica e comprovante de entrega na escola.',
    'Caso resolvido: mina → fábrica → cidade. Um objeto cotidiano conecta extração, transformação, transporte e consumo. Os benefícios e os impactos dessa produção acontecem em diferentes locais, mesmo quando o consumidor só vê o produto final.', [1,5]),
]
content = dict(introducao='Explore o Vale das Conexões com Camo, o camaleão investigador. Visite os locais, leia as pistas e trace no mapa a conexão que resolve cada caso. O território, os registros e as amostras são fictícios.',
    lugares=[dict(id=id,nome=name,tipo=type,x=x,y=y) for id,name,type,x,y in places],
    caminhos=[dict(de=a,para=b) for a,b in roads], missoes=missions)
path = Path(__file__).resolve().parents[1] / 'app/src/main/assets/expedition.json'
path.write_text(json.dumps(content, ensure_ascii=False, indent=2)+'\n', encoding='utf-8')
print('GeoExpedição: 8 locais, 8 caminhos, 3 missões e 24 pistas.')
