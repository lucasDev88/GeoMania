"""One-time import of the supplied architecture. JSON files are edited directly afterward."""
from pathlib import Path
from docx import Document
import json
import re

root = Path(__file__).resolve().parents[1]
doc = Document(root / 'Geo_Mania_Arquitetura_Android_Kotlin.docx')
assets = root / 'app/src/main/assets'
assets.mkdir(parents=True, exist_ok=True)
def write(name, value):
    (assets / name).write_text(json.dumps(value, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')

ids = ['povos-tradicionais', 'sociedades-agrarias', 'europa-medieval', 'colonizacao', 'industrializacao', 'globalizacao', 'transicao']
dates = ['Passado e presente', 'Antiguidade, em diferentes regiões', 'Europa, aproximadamente séculos V a XV', 'A partir do século XV', 'Séculos XVIII e XIX, com ritmos regionais', 'Séculos XX e XXI', 'Debates e práticas do presente']
techniques = ['Conhecimentos do território, ciclos de plantio, pesca e manejo; práticas variam entre povos e lugares.', 'Canais de irrigação, ferramentas agrícolas e obras hidráulicas.', 'Arados, moinhos de água e vento e técnicas artesanais.', 'Navegação oceânica, engenhos e técnicas de mineração.', 'Máquinas a vapor, mecanização fabril e siderurgia.', 'Motores, redes elétricas, mecanização e tecnologias digitais.', 'Reuso, reciclagem, energia solar e eólica e restauração de ecossistemas.']
sources = [[3], [1,6], [1], [1], [4], [5], [5]]
uses = [
    ['Alimentação por pesca, coleta e agricultura conforme cada território', 'Materiais, moradia e práticas culturais'],
    ['Irrigação e produção de alimentos', 'Construção de cidades e obras coletivas'],
    ['Cultivo e criação de animais', 'Moinhos, artesanato e abastecimento local'],
    ['Produção agrícola para exportação', 'Construção naval, mineração e comércio de longa distância'],
    ['Máquinas a vapor e produção fabril', 'Siderurgia, ferrovias e infraestrutura urbana'],
    ['Transporte, petroquímica e energia', 'Alimentos, equipamentos eletrônicos e bens de consumo'],
    ['Geração de energia renovável', 'Reaproveitamento de materiais e redução do desperdício']
]
periods = []
for i, row in enumerate(doc.tables[1].rows[1:]):
    c = [cell.text for cell in row.cells]
    periods.append(dict(id=ids[i], titulo=c[0], periodo=dates[i], formaProducao=c[1], recursos=c[2].split(', '), apropriacao=c[3], tecnica=techniques[i], usos=uses[i], transformacoes=[c[4]], fontes=sources[i]))
write('periods.json', periods)

resources = [
    dict(id='agua', titulo='Água', descricao='Sustenta a vida, a agricultura e muitas atividades produtivas. Sua disponibilidade depende dos ciclos naturais e das formas de distribuição e uso.', usos=['Abastecimento e irrigação', 'Indústria e geração de energia'], cuidado='Acesso à água, qualidade e disputa entre usos precisam ser considerados juntos.', fontes=[6]),
    dict(id='solo', titulo='Solo', descricao='É a base de grande parte da produção de alimentos. Forma-se lentamente e reúne minerais, matéria orgânica, água e organismos.', usos=['Agricultura e pastagens', 'Suporte à vegetação'], cuidado='Erosão, contaminação e manejo inadequado podem comprometer sua fertilidade.', fontes=[6]),
    dict(id='florestas', titulo='Florestas e madeira', descricao='Florestas oferecem materiais e alimentos e participam da regulação da água e do clima. Também são territórios de diferentes povos.', usos=['Madeira para construção e energia', 'Alimentos, fibras e manejo comunitário'], cuidado='Conservar exige considerar biodiversidade, ritmo de extração e direitos territoriais.', fontes=[3,5]),
    dict(id='minerais', titulo='Minerais', descricao='São usados em construções, máquinas e equipamentos. A extração liga territórios produtores a redes de transformação e consumo.', usos=['Metais em ferramentas e infraestrutura', 'Componentes de equipamentos eletrônicos'], cuidado='Mineração demanda gestão de rejeitos, água, impactos territoriais e condições de trabalho.', fontes=[5]),
    dict(id='biomassa', titulo='Biomassa', descricao='Matéria de origem biológica, como madeira e resíduos agrícolas, que pode ser usada como material ou fonte de energia.', usos=['Lenha e carvão vegetal', 'Biocombustíveis e aproveitamento de resíduos'], cuidado='Ser renovável não elimina impactos: manejo, uso da terra e emissões devem ser avaliados.', fontes=[4,5]),
    dict(id='fosseis', titulo='Combustíveis fósseis', descricao='Carvão mineral, petróleo e gás natural formaram-se em escalas geológicas e não se renovam no ritmo do consumo humano.', usos=['Energia para indústria e transporte', 'Matérias-primas petroquímicas'], cuidado='Sua queima emite gases de efeito estufa. Reduzir a dependência envolve mudanças técnicas e sociais.', fontes=[4,5])
]
write('resources.json', resources)
pairs_table = next(t for t in doc.tables if t.rows[0].cells[0].text == 'Carta A')
pair_periods = ['sociedades-agrarias', 'colonizacao', 'colonizacao', 'industrializacao', 'globalizacao', 'sociedades-agrarias']
pair_resources = ['agua', 'florestas', 'minerais', 'fosseis', 'fosseis', 'solo']
pairs = []
for i, row in enumerate(pairs_table.rows[1:]):
    a,b,c = [cell.text for cell in row.cells]
    pairs.append(dict(pairId=f'par-{i+1}', recursoOuContexto=a, usoOuApropriacao=b, explicacao=c, periodoId=pair_periods[i], recursoId=pair_resources[i]))
write('game_pairs.json', pairs)
refs = []
for p in doc.paragraphs:
    m = re.match(r'^\[(\d+)\] (.+?) — (https://\S+)', p.text)
    if m and int(m[1]) in [1,2,3,4,5,6,9]:
        refs.append(dict(id=int(m[1]), titulo=m[2], url=m[3]))
write('sources.json', refs)
write('learning.json', dict(
    tema='Recursos naturais: apropriação e usos',
    pergunta='Quem utiliza a natureza, de que maneira e com quais consequências?',
    conceito='Apropriar-se da natureza envolve acesso, controle, extração, transformação e consumo. Essas relações mudam com as técnicas e com a organização de cada sociedade.',
    destaque='A extração global de recursos naturais triplicou nas cinco décadas analisadas pelo relatório do PNUMA de 2024. [5]',
    aviso='As formas de apropriação coexistem. Esta organização didática não representa uma sequência obrigatória de evolução das sociedades.',
    objetivo='Relacionar recursos naturais, técnicas, formas de produção e transformações socioambientais por meio da exploração histórica e do GeoMatch.',
    metodo='Síntese didática a partir do documento de arquitetura e das referências abaixo. As fontes oferecem contextos temáticos; os recortes históricos devem ser discutidos com o professor. Todo o conteúdo e o jogo funcionam offline. Abrir os links externos exige internet.'
))
print(f'Conteúdo importado: {len(periods)} contextos, {len(resources)} recursos, {len(pairs)} pares e {len(refs)} fontes.')
