package br.com.geomania.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import kotlin.math.roundToInt
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.geomania.choices.*

@Composable
fun ChoicesScreen(vm: ChoicesViewModel, sources: () -> Unit) {
    val state by vm.state.collectAsStateWithLifecycle()
    val phase = state.phase?.let { vm.content.fases[it] }
    var restart by rememberSaveable { mutableStateOf(false) }
    var leave by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = phase != null) {
        if (state.finished) vm.overview() else leave = true
    }
    val list = rememberLazyListState()
    LaunchedEffect(state.phase, state.decision) { list.scrollToItem(0) }
    LazyColumn(state = list, contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item { Heading("Escolhas da Terra", "MINI SIMULADOR") }
        if (phase == null) {
            item { Text(vm.content.introducao, style = MaterialTheme.typography.bodyLarge) }
            item { Note(vm.content.indicadores) }
            item { Text("${state.results.size}/4 fases concluídas nesta sessão", style = MaterialTheme.typography.titleMedium) }
            itemsIndexed(vm.content.fases) { index, item ->
                NavigationCard(item.titulo, if (index in state.results) "Concluída • Jogar novamente com outras escolhas" else "4 decisões • ${item.contexto}") { vm.start(index) }
            }
            if (state.results.isNotEmpty()) {
                item { Heading("Suas sociedades", "RESULTADOS DA SESSÃO") }
                itemsIndexed(vm.content.fases) { index, item ->
                    state.results[index]?.let { result ->
                        Text(item.titulo, style = MaterialTheme.typography.titleLarge)
                        IndicatorPanel(result.indicators, item.iniciais)
                    }
                }
                item { Note("Compare escolhas e consequências dentro de cada fase. Os cenários começam em condições diferentes e não formam um ranking entre sociedades.") }
            }
            item { Text("A rotação mantém seu progresso. Sair deste modo ou encerrar o aplicativo inicia uma nova sessão.", style = MaterialTheme.typography.bodySmall) }
        } else {
            item { Text(phase.titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold) }
            item { Text(phase.contexto) }
            item { IndicatorPanel(state.indicators, if (state.selected != null) state.history.last().before else phase.iniciais,
                if (state.selected != null) "Efeito real desta escolha" else "Mudança desde o início") }
            if (state.finished) {
                item { Celebration() }
                item { Heading("Sua sociedade", "FASE CONCLUÍDA • 4/4 DECISÕES") }
                item { Note(conclusion(state.indicators, phase.iniciais)) }
                item { Text(phase.reflexao, style = MaterialTheme.typography.bodyLarge) }
                item { Text("Reveja suas decisões", style = MaterialTheme.typography.titleLarge) }
                itemsIndexed(state.history) { _, record ->
                    val decision = phase.decisoes[record.decision]
                    val choice = decision.opcoes[record.option]
                    OutlinedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(decision.situacao, fontWeight = FontWeight.SemiBold)
                            Text(choice.titulo)
                            Text(choice.explicacao)
                        }
                    }
                }
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (state.phase!! < vm.content.fases.lastIndex) {
                            Button(onClick = { vm.start(state.phase!! + 1) }, modifier = Modifier.fillMaxWidth()) { Text("Próxima fase") }
                        }
                        OutlinedButton(onClick = { vm.start(state.phase!!) }, modifier = Modifier.fillMaxWidth()) { Text("Experimentar outras escolhas") }
                        TextButton(onClick = vm::overview) { Text("Ver fases e resultados") }
                    }
                }
            } else {
                val decision = phase.decisoes[state.decision]
                item { Text("Decisão ${state.decision + 1} de 4", style = MaterialTheme.typography.labelLarge) }
                item {
                    val progress = (state.decision + 1) / 4f
                    LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth().semantics {
                        progressBarRangeInfo = ProgressBarRangeInfo(progress, 0f..1f)
                    })
                }
                item { Text(decision.situacao, style = MaterialTheme.typography.headlineSmall) }
                if (state.selected == null) {
                    itemsIndexed(decision.opcoes) { index, option ->
                        OutlinedCard(onClick = { vm.choose(index) }, modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("${'A' + index} • ${option.titulo}", style = MaterialTheme.typography.titleMedium)
                                Text(effectText(option.efeitos), style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                    item { Text("Considere benefícios e custos. Os efeitos são limitados ao intervalo de 0 a 100.", style = MaterialTheme.typography.bodySmall) }
                } else {
                    val choice = decision.opcoes[state.selected!!]
                    item {
                        Column(Modifier.semantics { liveRegion = LiveRegionMode.Polite }, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Você escolheu: ${choice.titulo}", fontWeight = FontWeight.Bold)
                            Note(choice.explicacao)
                            Button(onClick = vm::advance, modifier = Modifier.fillMaxWidth()) { Text(if (state.decision == 3) "Ver resultado" else "Continuar") }
                        }
                    }
                }
                item {
                    Row {
                        TextButton(onClick = { restart = true }) { Text("Recomeçar fase") }
                        TextButton(onClick = { leave = true }) { Text("Ver fases") }
                    }
                }
            }
            item { TextButton(onClick = sources) { Text("Referências temáticas: ${phase.fontes.joinToString { "[$it]" }}") } }
            item { Text("Simulação didática: os números não são medições históricas.", style = MaterialTheme.typography.bodySmall) }
        }
    }
    if (restart || leave) AlertDialog(
        onDismissRequest = { restart = false; leave = false },
        title = { Text(if (restart) "Recomeçar esta fase?" else "Voltar às fases?") },
        text = { Text("O progresso da fase atual será descartado. Os resultados das outras fases desta sessão serão mantidos.") },
        confirmButton = { TextButton(onClick = {
            if (restart) state.phase?.let(vm::start) else vm.overview()
            restart = false; leave = false
        }) { Text("Confirmar") } },
        dismissButton = { TextButton(onClick = { restart = false; leave = false }) { Text("Continuar jogando") } })
}

private val indicatorNames = listOf("Natureza", "Produção", "Sociedade", "Recursos")
private fun signed(value: Int) = if (value > 0) "+$value" else "$value"
private fun effectText(delta: Indicators) = indicatorNames.zip(delta.values()).joinToString(" • ") { (label, value) -> "$label ${signed(value)}" }

@Composable
private fun IndicatorPanel(current: Indicators, baseline: Indicators, label: String = "Mudança desde o início") {
    val motion = LocalMotionEnabled.current
    OutlinedCard(Modifier.fillMaxWidth().entranceMotion()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            val delta = (current - baseline).values()
            indicatorNames.zip(current.values()).forEachIndexed { index, (name, value) ->
                val animated by animateFloatAsState(value.toFloat(), if (motion) spring(dampingRatio = .85f, stiffness = 85f) else snap(), label = name)
                Text("$name: ${animated.roundToInt().coerceIn(0,100)}/100 (${signed(delta[index])})", fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clearAndSetSemantics { contentDescription = "$name: $value de 100, mudança ${signed(delta[index])}" })
                LinearProgressIndicator(progress = { (animated / 100f).coerceIn(0f,1f) }, modifier = Modifier.fillMaxWidth().semantics {
                    progressBarRangeInfo = ProgressBarRangeInfo(value/100f, 0f..1f)
                })
            }
        }
    }
}

internal fun conclusion(current: Indicators, initial: Indicators): String {
    val change = current - initial
    val headline = when {
        change.producao > 0 && change.recursos < 0 -> "Você ampliou a capacidade produtiva consumindo parte dos recursos disponíveis."
        change.natureza > 0 && change.producao < 0 -> "Você favoreceu a qualidade ambiental com redução da produção imediata."
        change.sociedade > 0 && change.producao < 0 -> "Você ampliou o bem-estar e o acesso, abrindo mão de produção imediata."
        else -> "Suas decisões combinaram prioridades e custos diferentes."
    }
    return "$headline\n\n" + indicatorNames.zip(change.values()).joinToString("\n") { (name, delta) ->
        when {
            delta > 0 -> "$name aumentou $delta pontos."
            delta < 0 -> "$name diminuiu ${-delta} pontos."
            else -> "$name terminou no valor inicial."
        }
    } + "\n\nQuem se beneficiou e quem arcou com os custos? Os indicadores resumem a sociedade e não revelam todas as desigualdades."
}
