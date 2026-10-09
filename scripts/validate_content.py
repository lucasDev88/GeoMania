"""Validate packaged educational data independently of the Android SDK."""
import json
from pathlib import Path

assets = Path(__file__).resolve().parents[1] / 'app/src/main/assets'
def read(name):
    return json.loads((assets / name).read_text(encoding='utf-8'))

periods, resources, pairs, sources = [read(n + '.json') for n in ['periods', 'resources', 'game_pairs', 'sources']]
assert len(periods) >= 6 and len(resources) >= 6 and len(pairs) == 6
assert all(p['resumo'].strip() and p['pergunta'].strip() for p in periods)
for entries, key in [(periods, 'id'), (resources, 'id'), (pairs, 'pairId'), (sources, 'id')]:
    assert len({e[key] for e in entries}) == len(entries), f'Duplicate {key}'
    assert all(all(value != '' for value in entry.values()) for entry in entries)
source_ids = {s['id'] for s in sources}
for entry in periods + resources:
    assert entry['fontes'] and set(entry['fontes']) <= source_ids
for p in pairs:
    assert p['periodoId'] in {entry['id'] for entry in periods}
    assert p['recursoId'] in {entry['id'] for entry in resources}
assert len({p['recursoOuContexto'] for p in pairs}) == 6
assert len({p['usoOuApropriacao'] for p in pairs}) == 6
assert all(s['url'].startswith('https://') for s in sources)
learning = read('learning.json')
assert 'coexistem' in learning['aviso']
print(f'OK: {len(periods)} contextos, {len(resources)} recursos, 6 pares, fontes e vínculos válidos.')

earth = read('earth_choices.json')
assert len(earth['fases']) == 4
decisions = [d for phase in earth['fases'] for d in phase['decisoes']]
assert len(decisions) == 16 and len({d['id'] for d in decisions}) == 16
for phase in earth['fases']:
    assert len(phase['decisoes']) == 4 and set(phase['fontes']) <= source_ids
    assert all(0 <= v <= 100 for v in phase['iniciais'].values())
for decision in decisions:
    assert len(decision['opcoes']) == 3
    for option in decision['opcoes']:
        assert option['explicacao'].strip()
        assert any(v < 0 for v in option['efeitos'].values())
        assert any(v > 0 for v in option['efeitos'].values())
print('OK: Escolhas da Terra com 4 fases, 16 decisões e 48 alternativas com benefícios e custos.')

expedition = read('expedition.json')
locations = {p['id'] for p in expedition['lugares']}
assert len(locations) == 8 and len(expedition['missoes']) == 3
assert all(0.08 <= p['x'] <= 0.92 and 0.08 <= p['y'] <= 0.92 for p in expedition['lugares'])
assert all(e['de'] in locations and e['para'] in locations for e in expedition['caminhos'])
for mission in expedition['missoes']:
    assert {p['local'] for p in mission['pistas']} == locations and len(mission['pistas']) == 8
    assert set(mission['fontes']) <= source_ids
    assert all(set(route) <= set(mission['essenciais']) for route in mission['rotas'])
    assert mission['dica'] and mission['conclusao'] and mission['instrucao']
print('OK: GeoExpedição com 8 locais, 3 missões e 24 pistas; referências válidas.')
