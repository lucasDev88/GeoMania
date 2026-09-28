"""Generate initial fictional decision scenarios. Edit the JSON after editorial review."""
import json
from pathlib import Path

def indicators(n, p, s, r):
    return dict(natureza=n, producao=p, sociedade=s, recursos=r)
def option(title, delta, explanation):
    return dict(titulo=title, efeitos=indicators(*delta), explicacao=explanation)
def decision(id, situation, *options):
    return dict(id=id, situacao=situation, opcoes=list(options))
def phase(id, title, context, initial, sources, reflection, decisions):
    return dict(id=id, titulo=title, contexto=context, iniciais=indicators(*initial), fontes=sources, reflexao=reflection, decisoes=decisions)

phases = [
phase('coleta', 'Sociedades caçadoras e coletoras',
    'Uma comunidade fictícia combina caça, pesca e coleta. Conhecimento sazonal, mobilidade e acordos orientam o uso do território. Esse cenário não representa todos os povos nem uma etapa que todos deveriam superar.',
    (90,30,60,80), [3],
    'Observe como mobilidade, partilha e conhecimento do território mudaram a disponibilidade de alimentos. Não confunda baixa extração com ausência de técnicas ou organização social.', [
    decision('coleta-alimentos', 'Os frutos estão amadurecendo. Como organizar a coleta?',
        option('Coletar o máximo para formar reservas', (-15,20,8,-18), 'Neste cenário, o estoque cresce, mas restam menos frutos para outros seres vivos e para renovação.'),
        option('Alternar áreas de coleta conforme a estação', (8,8,-5,6), 'A alternância alivia a pressão local, mas exige deslocamentos e mais esforço do grupo.'),
        option('Coletar pouco e compartilhar o que já existe', (5,-8,12,8), 'A partilha amplia o acesso imediato, mas reduz a reserva para os próximos dias.')),
    decision('coleta-caca', 'A presença de animais perto do acampamento diminuiu.',
        option('Ampliar as caçadas na mesma área', (-18,18,5,-20), 'Mais esforço traz alimentos agora, mas aumenta a pressão sobre os animais próximos.'),
        option('Mudar temporariamente o acampamento', (10,-6,-8,12), 'O deslocamento permite descanso da área, ao custo de reorganizar abrigo e abastecimento.'),
        option('Combinar pesca e coleta em outros locais', (-5,10,8,-8), 'Diversificar reduz a dependência da caça, mas transfere parte da pressão para outros recursos.')),
    decision('coleta-abrigo', 'O grupo precisa reparar os abrigos antes das chuvas.',
        option('Retirar madeira nova em grande quantidade', (-15,18,10,-18), 'A construção acelera e melhora o abrigo, consumindo mais madeira disponível.'),
        option('Reaproveitar materiais e fazer reparos graduais', (7,-6,8,8), 'O reaproveitamento reduz a retirada, mas o trabalho fica mais lento.'),
        option('Priorizar abrigos comuns e adiar os demais', (4,5,-8,6), 'Concentrar materiais economiza recursos, mas deixa necessidades individuais sem atendimento.')),
    decision('coleta-acesso', 'Uma comunidade vizinha também precisa acessar os locais de pesca.',
        option('Reservar os melhores locais para seu grupo', (-8,15,-15,-10), 'Seu abastecimento imediato cresce, mas restringir o acesso aumenta tensões entre comunidades.'),
        option('Combinar períodos de uso e partilhar conhecimentos', (8,-5,15,5), 'Os acordos favorecem cooperação e descanso dos locais, mas limitam a retirada imediata.'),
        option('Buscar áreas mais distantes', (-5,8,-8,5), 'A pressão próxima diminui; viagens mais longas exigem trabalho e afetam outros lugares.')),
]),
phase('agricultura', 'Sociedades agrícolas',
    'Uma aldeia agrícola fictícia depende do solo e da água. Trabalho coletivo, armazenamento e distribuição da terra influenciam a produção. Agricultura e outras formas de sustento podem coexistir.',
    (85,40,55,75), [1,6],
    'Produzir alimentos envolve controlar terra, água e trabalho. Reflita sobre quem recebeu os benefícios e quem assumiu os custos das decisões.', [
    decision('agricultura-agua', 'Uma estação seca ameaça as plantações.',
        option('Desviar muita água do rio para irrigação', (-18,25,5,-20), 'A colheita aumenta no cenário, enquanto sobra menos água para outros usos e ambientes.'),
        option('Construir canais menores e dividir a água', (-5,12,12,-8), 'O acordo amplia o acesso, mas construir canais ainda exige materiais e trabalho.'),
        option('Reduzir o plantio e guardar água para consumo', (8,-15,8,12), 'O abastecimento humano recebe prioridade, com menor quantidade de alimentos produzidos.')),
    decision('agricultura-terra', 'A aldeia precisa de mais alimentos.',
        option('Abrir novas áreas de cultivo', (-20,25,8,-15), 'A área produtiva cresce, substituindo vegetação e ampliando a demanda sobre o território.'),
        option('Alternar cultivos e reservar áreas de descanso', (12,-8,5,8), 'Parte da terra descansa; a oferta imediata diminui para favorecer a manutenção do solo.'),
        option('Trocar excedentes artesanais por alimentos', (-5,-6,15,5), 'As trocas complementam a alimentação, mas dependem da produção e dos acordos de outros lugares.')),
    decision('agricultura-estoque', 'Uma boa colheita permite guardar excedentes.',
        option('Concentrar os estoques com os grandes proprietários', (-5,20,-15,-8), 'O estoque organizado favorece a produção, mas concentra o controle sobre os alimentos.'),
        option('Construir um celeiro coletivo', (-6,10,18,-10), 'O armazenamento compartilhado amplia a proteção do grupo e consome materiais de construção.'),
        option('Distribuir a maior parte da colheita agora', (3,-12,18,-5), 'Mais pessoas recebem alimentos imediatamente, mas sobra menos reserva para períodos difíceis.')),
    decision('agricultura-floresta', 'A produção de ferramentas e construções demanda madeira.',
        option('Intensificar o corte nas áreas próximas', (-18,20,6,-20), 'O fornecimento aumenta rapidamente, pressionando a vegetação perto da aldeia.'),
        option('Reparar ferramentas e limitar a retirada', (8,-8,8,10), 'A manutenção economiza madeira, mas reduz o ritmo das novas construções.'),
        option('Obter madeira por trocas com outras aldeias', (-6,12,5,-8), 'As trocas sustentam as obras, mas a extração e seus impactos continuam em outro território.')),
]),
phase('industria', 'Revolução Industrial',
    'Uma cidade industrial fictícia expande fábricas e transportes. Carvão, trabalho assalariado e controle dos meios de produção estruturam as decisões. Crescimento produtivo não garante distribuição igual dos benefícios.',
    (100,30,50,80), [4,1],
    'O carvão ganhou importância na industrialização. Compare o crescimento produtivo com o consumo material, as condições de vida e a distribuição dos benefícios.', [
    decision('industria-carvao', 'Uma grande reserva de carvão foi encontrada. O que fazer?',
        option('Explorar intensamente para aumentar a produção', (-20,30,10,-25), 'O carvão alimenta máquinas a vapor, fábricas e transportes. Neste cenário, a expansão também aumenta impactos ambientais.'),
        option('Explorar moderadamente e escalonar a expansão', (-10,15,5,-12), 'A extração continua, mas em menor ritmo; a oferta de energia cresce mais devagar.'),
        option('Preservar grande parte da reserva', (8,-10,-5,12), 'Menor extração conserva disponibilidade futura, mas limita o crescimento e os empregos previstos neste cenário.')),
    decision('industria-alimentos', 'A cidade precisa de mais alimentos.',
        option('Expandir as áreas agrícolas próximas', (-18,20,12,-15), 'A oferta local cresce com a conversão de terras e maior consumo de água e solo.'),
        option('Melhorar ferramentas e manejo nas terras existentes', (-6,12,6,-10), 'Ganhos de produtividade reduzem a necessidade de expansão, mas requerem materiais e trabalho.'),
        option('Importar alimentos de outras regiões', (-8,5,15,-12), 'O transporte amplia o abastecimento e a dependência de redes externas; os impactos não desaparecem.')),
    decision('industria-trabalho', 'As fábricas querem ampliar sua produção.',
        option('Aumentar jornadas e manter os equipamentos', (-10,25,-20,-15), 'A produção cresce à custa de mais desgaste dos trabalhadores e consumo de recursos.'),
        option('Reduzir jornadas e melhorar a segurança', (5,-10,20,-5), 'As condições de trabalho melhoram, mas a produção imediata e os materiais disponíveis diminuem.'),
        option('Investir em máquinas mais produtivas', (-10,20,-5,-18), 'Novos equipamentos ampliam a capacidade, consomem materiais e podem deslocar trabalhadores.')),
    decision('industria-cidade', 'O crescimento urbano pressiona rios e bairros operários.',
        option('Priorizar novas fábricas', (-20,25,-10,-18), 'A capacidade industrial cresce enquanto as condições ambientais e urbanas recebem menos atenção.'),
        option('Investir em abastecimento e saneamento', (10,-8,20,-12), 'Obras coletivas melhoram a vida urbana, mas exigem materiais e recursos antes destinados à expansão.'),
        option('Levar parte das fábricas para a periferia', (-12,12,5,-10), 'A pressão perto do centro diminui, mas se desloca para outros moradores e ambientes.')),
]),
phase('atual', 'Sociedade contemporânea',
    'Uma sociedade fictícia está ligada a cadeias globais de produção e consumo. Infraestrutura, desigualdade e mudanças ambientais influenciam as opções; nenhuma tecnologia elimina todos os impactos.',
    (60,65,55,55), [5,6],
    'Cadeias globais conectam extração, consumo e descarte. Renováveis, eficiência e reciclagem também precisam de materiais, investimentos e decisões sobre acesso.', [
    decision('atual-energia', 'A demanda por eletricidade aumentou.',
        option('Ampliar rapidamente a geração com combustíveis fósseis', (-20,25,8,-22), 'O fornecimento cresce no curto prazo, aumentando a extração e as emissões do sistema energético.'),
        option('Investir em energia solar, eólica e redes', (10,10,5,-18), 'A matriz muda, mas equipamentos e redes exigem minerais e infraestrutura.'),
        option('Reduzir desperdícios e limitar usos nos horários de pico', (12,-8,-5,15), 'A demanda por recursos cai, mas a limitação imediata afeta atividades e rotinas.')),
    decision('atual-minerais', 'Uma região possui minerais usados em eletrônicos e baterias.',
        option('Acelerar a mineração para atender às exportações', (-22,25,8,-25), 'A produção se integra ao mercado global, consumindo reservas e pressionando o território.'),
        option('Limitar a extração e negociar proteção e benefícios locais', (-8,10,15,-10), 'A negociação distribui melhor os benefícios do cenário, mas a extração continua causando impactos.'),
        option('Priorizar reparo e reciclagem de equipamentos', (12,-8,8,15), 'Materiais permanecem em uso por mais tempo; a coleta e o reaproveitamento limitam a oferta imediata.')),
    decision('atual-agua', 'Agricultura, indústria e bairros disputam água em uma seca.',
        option('Priorizar os setores que mais produzem', (-12,20,-18,-15), 'O indicador produtivo melhora enquanto parte da população perde acesso à água.'),
        option('Garantir consumo básico e negociar cotas de uso', (8,-10,18,10), 'O acesso básico recebe prioridade, com restrição temporária a atividades produtivas.'),
        option('Investir em reuso e reduzir perdas das redes', (10,5,10,-15), 'O sistema aproveita melhor a água, mas as obras consomem materiais e exigem manutenção.')),
    decision('atual-consumo', 'O descarte de produtos cresce a cada ano.',
        option('Expandir a produção de itens baratos e de curta duração', (-20,25,5,-22), 'Mais bens chegam ao mercado, com maior demanda material e descarte no cenário.'),
        option('Produzir bens duráveis e ampliar serviços de reparo', (15,-8,10,15), 'A vida útil aumenta e reduz novas retiradas, enquanto cai a produção imediata de itens novos.'),
        option('Ampliar coleta e reciclagem mantendo o consumo', (5,10,8,-8), 'Parte dos materiais retorna ao uso, mas a reciclagem não elimina a demanda por recursos adicionais.')),
])

]
content = dict(
    introducao='Decida como quatro sociedades fictícias utilizam a natureza. Cada fase tem quatro situações e condições próprias: não há uma alternativa sempre correta nem uma sequência obrigatória de evolução.',
    indicadores='Índices didáticos de 0 a 100, sem unidade real: Natureza representa qualidade ambiental; Produção, capacidade de produzir; Sociedade, bem-estar e acesso; Recursos, disponibilidade para usos futuros. Os efeitos são hipóteses do jogo, não medições históricas. Cada fase começa de novo: preservar recursos não cria carvão nem torna uma reserva renovável.',
    fases=phases)
path = Path(__file__).resolve().parents[1] / 'app/src/main/assets/earth_choices.json'
path.write_text(json.dumps(content, ensure_ascii=False, indent=2)+'\n', encoding='utf-8')
print('Criadas 4 fases, 16 situações e 48 opções com consequências.')
