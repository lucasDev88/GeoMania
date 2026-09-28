package br.com.geomania.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.geomania.data.Estatisticas
import br.com.geomania.game.GameViewModel

@Composable
fun GameScreen(vm: GameViewModel, stats: Estatisticas) {
    val state by vm.state.collectAsStateWithLifecycle()
    val saveStatus by vm.saveStatus.collectAsStateWithLifecycle()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle, vm) {
        vm.setActive(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) vm.setActive(true)
            if (event == Lifecycle.Event.ON_PAUSE) vm.setActive(false)
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer); vm.setActive(false) }
    }
    var confirmRestart by remember { mutableStateOf(false) }
    val columns = if (LocalConfiguration.current.screenWidthDp >= 600 && LocalDensity.current.fontScale < 1.5f) 3 else 2
    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Heading("Desafio da apropriação", "GEOMATCH") }
        item { Text("Vire duas cartas e associe um recurso/contexto ao seu uso. O tempo começa no primeiro toque e pausa durante as explicações.") }
        item { Note("${state.matchedPairs.size}/6 pares • ${state.attempts} tentativas • ${state.elapsedSeconds}s") }
        if (state.isFinished && state.explanation == null) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                    Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Você conectou todas as histórias!", style = MaterialTheme.typography.headlineSmall)
                        Text("${state.score} pontos", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                        Text("Recorde salvo: ${stats.recorde} pontos")
                        Text(saveStatus)
                        if (saveStatus.startsWith("Não")) TextButton(onClick = vm::save) { Text("Tentar salvar novamente") }
                        Button(onClick = vm::restart, enabled = saveStatus != "Salvando resultado…") { Text("Jogar novamente") }
                    }
                }
            }
        }
        items(state.cards.chunked(columns)) { row ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { card ->
                    val matched = card.pairId in state.matchedPairs
                    val visible = matched || card.id in state.selected
                    val position = state.cards.indexOf(card) + 1
                    val description = when {
                        matched -> "Par encontrado. ${card.text}"
                        visible -> card.text
                        else -> "Carta $position, fechada"
                    }
                    OutlinedCard(
                        onClick = { vm.select(card.id) },
                        enabled = !state.blocked && !matched && card.id !in state.selected,
                        modifier = Modifier.weight(1f).heightIn(min = 128.dp).semantics { contentDescription = description },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.outlinedCardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = if (visible) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary,
                            disabledContentColor = if (visible) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(if (matched) "PAR ENCONTRADO" else "CARTA $position", style = MaterialTheme.typography.labelSmall)
                            Text(if (visible) card.text else "?", style = if (visible) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.headlineLarge)
                        }
                    }
                }
            }
        }
        item {
            OutlinedButton(onClick = { confirmRestart = true }, enabled = saveStatus != "Salvando resultado…") { Text("Recomeçar partida") }
        }
        item { Text("Pontuação: 1.000 − 25 por tentativa além de 6 − 2 por segundo. Mínimo de 100 pontos.", style = MaterialTheme.typography.bodySmall) }
    }
    state.explanation?.let { explanation ->
        AlertDialog(onDismissRequest = vm::dismissExplanation,
            title = { Text("Conexão encontrada!") }, text = { Text(explanation, Modifier.verticalScroll(rememberScrollState())) },
            confirmButton = { TextButton(onClick = vm::dismissExplanation) { Text(if (state.isFinished) "Ver resultado" else "Continuar") } })
    }
    if (confirmRestart) AlertDialog(onDismissRequest = { confirmRestart = false },
        title = { Text("Recomeçar?") }, text = { Text("O progresso desta partida será descartado. Seu recorde permanece salvo.") },
        confirmButton = { TextButton(onClick = { vm.restart(); confirmRestart = false }) { Text("Recomeçar") } },
        dismissButton = { TextButton(onClick = { confirmRestart = false }) { Text("Continuar jogando") } })
}
