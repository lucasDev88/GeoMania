package br.com.geomania.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.geomania.R
import br.com.geomania.expedition.*

@Composable
fun CamoMascot(modifier: Modifier = Modifier) {
    Image(painterResource(R.drawable.camo_mascot), "Camo, o camaleão explorador do Geo Mania", modifier)
}

@Composable
fun ExpeditionScreen(vm: ExpeditionViewModel, sources: () -> Unit) {
    val s by vm.state.collectAsStateWithLifecycle()
    val data = vm.content
    val motion = LocalMotionEnabled.current
    val mission = s.mission?.let { data.missoes[it] }
    var leave by remember { mutableStateOf(false) }
    var hint by rememberSaveable(s.mission) { mutableStateOf(false) }
    BackHandler(enabled = mission != null) { if (s.solved) vm.overview() else leave = true }
    if (leave) AlertDialog(onDismissRequest = { leave = false }, title = { Text("Voltar às missões?") },
        text = { Text("As pistas desta tentativa serão reiniciadas. As missões concluídas nesta sessão continuam marcadas.") },
        confirmButton = { TextButton(onClick = { leave = false; vm.overview() }) { Text("Ver missões") } },
        dismissButton = { TextButton(onClick = { leave = false }) { Text("Continuar explorando") } })
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        if (mission == null) {
            item { Heading("GeoExpedição", "EXPLORE COM CAMO") }
            item { AnimatedCamo(Modifier.fillMaxWidth().height(190.dp)) }
            item { Note(data.introducao) }
            item { Text("1. Toque nos locais para viajar e coletar pistas.\n2. Leia o caderno e ative Marcar conexão.\n3. Toque nos locais na ordem que explica o caso e confira sua conclusão.") }
            item { Text("${s.completed.size} de ${data.missoes.size} missões concluídas nesta sessão") }
            items(data.missoes.indices.toList()) { i -> NavigationCard(
                "${if (i in s.completed) "✓ " else ""}${data.missoes[i].titulo}", data.missoes[i].objetivo) { vm.start(i) } }
        } else {
            item { Heading(mission.titulo, "GEOEXPEDIÇÃO • ${(s.mission ?: 0) + 1}/3") }
            item { Text(mission.objetivo, style = MaterialTheme.typography.titleMedium) }
            item { ExpeditionMap(data, s, vm::touch) }
            item { Text("N ↑ • Verde: floresta • Azul: água • Bege: caminhos\nLaranja: sua conexão. Mapa esquemático fictício, sem escala.", style = MaterialTheme.typography.bodySmall) }
            item { Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(20.dp)) {
                Text(if (s.moving.isNotEmpty()) "Camo está viajando… Aguarde chegar para coletar a pista." else
                    if (s.marking) "Toque nos locais já visitados para marcar a sequência. Tocar numa marca remove ela e as seguintes." else "Camo está em ${data.lugares.first { it.id == s.position }.nome}. Escolha um destino no mapa ou abaixo.",
                    Modifier.fillMaxWidth().padding(18.dp).semantics { liveRegion = LiveRegionMode.Polite }, style = MaterialTheme.typography.bodyLarge)
            } }
            items(data.lugares.chunked(2)) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    row.forEach { place -> OutlinedButton(onClick = { vm.touch(place.id) }, modifier = Modifier.weight(1f),
                        enabled = !s.solved && s.moving.isEmpty() && (!s.marking || place.id in s.visited)) {
                        Text("${data.lugares.indexOf(place) + 1}. ${place.nome}${if (place.id in s.visited) " ✓" else ""}")
                    } }
                }
            }
            item { Heading("Caderno de pistas", "${s.visited.size}/${data.lugares.size} LOCAIS VISITADOS") }
            items(mission.pistas.filter { it.local in s.visited }, key = { it.local }) { clue ->
                OutlinedCard(Modifier.fillMaxWidth().entranceMotion(1)) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("${data.lugares.first { it.id == clue.local }.nome} • ${clue.titulo}", style = MaterialTheme.typography.titleMedium)
                    Text(clue.texto)
                } }
            }
            item { Note(mission.instrucao) }
            if (!s.solved) {
                item { Button(onClick = vm::toggleMarking, enabled = s.moving.isEmpty(), modifier = Modifier.fillMaxWidth()) {
                    Text(if (s.marking) "Voltar a explorar" else "Marcar conexão no mapa") } }
                item { Text("Sua conexão: " + if (s.route.isEmpty()) "nenhum local marcado" else s.route.joinToString(" → ") { id -> data.lugares.first { it.id == id }.nome }) }
                item { Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = vm::clear, enabled = s.route.isNotEmpty()) { Text("Limpar") }
                    Button(onClick = vm::check, enabled = s.route.isNotEmpty() && s.moving.isEmpty()) { Text("Conferir conexão") }
                } }
                item { TextButton(onClick = { hint = !hint }) { Text(if (hint) "Fechar dica" else "Pedir uma dica a Camo") }
                    AnimatedVisibility(hint,
                        enter = if (motion) expandVertically(tween(350)) + fadeIn(tween(250)) else EnterTransition.None,
                        exit = if (motion) shrinkVertically(tween(280)) + fadeOut(tween(200)) else ExitTransition.None) { Note(mission.dica) }
                }
            }
            if (s.feedback.isNotEmpty()) item(key = "feedback-${mission.id}") { Surface(color = if (s.solved) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(20.dp)) { Column(Modifier.padding(20.dp).semantics { liveRegion = LiveRegionMode.Polite }, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (s.solved) { Celebration(); Text("Missão concluída!", style = MaterialTheme.typography.titleLarge) }
                    Text(s.feedback)
                } } }
            if (s.solved && s.mission!! < data.missoes.lastIndex) item { Button(onClick = { vm.start(s.mission!! + 1) }) { Text("Próxima missão") } }
            item { TextButton(onClick = { if (s.solved) vm.overview() else leave = true }) { Text("Ver todas as missões") } }
            item { TextButton(onClick = sources) { Text("Referências: ${mission.fontes.joinToString { "[$it]" }}") } }
        }
    }
}

@Composable
private fun ExpeditionMap(data: ExpeditionContent, s: ExpeditionState, touch: (String) -> Unit) {
    val position = data.lugares.first { it.id == s.position }
    val motion = LocalMotionEnabled.current
    val waterPhase by rememberAmbientPhase(2400)
    val x by animateFloatAsState(position.x, if (motion) tween(450) else snap(), label = "Camo X")
    val y by animateFloatAsState(position.y, if (motion) tween(450) else snap(), label = "Camo Y")
    val anchor by animateFloatAsState(if (position.y < .25f) 8f else -62f, if (motion) tween(450) else snap(), label = "Camo posição")
    Surface(shape = RoundedCornerShape(24.dp), color = Color(0xFFE6EECF)) {
        BoxWithConstraints(Modifier.fillMaxWidth().aspectRatio(1f)) {
            Canvas(Modifier.fillMaxSize()) {
                fun point(id: String): Offset { val p = data.lugares.first { it.id == id }; return Offset(p.x * size.width, p.y * size.height) }
                // The forest is visibly split by the north road.
                listOf(.49f to .13f, .76f to .16f, .53f to .24f, .80f to .26f).forEach { (a,b) ->
                    drawCircle(Color(0xFF82AD65), size.width * .08f, Offset(size.width*a,size.height*b)) }
                val river = Path().apply { moveTo(size.width*.16f,0f); cubicTo(size.width*.06f,size.height*.38f,size.width*.49f,size.height*.48f,size.width*.31f,size.height*.70f); quadraticTo(size.width*.33f,size.height*.91f,size.width*.78f,size.height) }
                drawPath(river, Color(0xFF65BACA), style = Stroke(12.dp.toPx(), cap = StrokeCap.Round))
                drawPath(river, Color(0xFFB8EDF0), style = Stroke(2.dp.toPx(), cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8.dp.toPx(),14.dp.toPx()), -waterPhase*22.dp.toPx())))
                data.caminhos.forEach { drawLine(Color(0xFFC6AF83), point(it.de), point(it.para), 7.dp.toPx(), StrokeCap.Round) }
                drawLine(Color(0xFF6B7B51), Offset(size.width*.65f,size.height*.30f), Offset(size.width*.65f,0f), 7.dp.toPx())
                fun arrow(a: Offset,b: Offset,color: Color) {
                    drawLine(color,a,b,3.dp.toPx(),StrokeCap.Round)
                    val delta = b-a; val length = delta.getDistance(); if (length > 0) {
                        val unit = delta/length; val end = a+delta*.58f; val side=Offset(-unit.y,unit.x)
                        drawLine(color,end,end-unit*10.dp.toPx()+side*5.dp.toPx(),3.dp.toPx())
                        drawLine(color,end,end-unit*10.dp.toPx()-side*5.dp.toPx(),3.dp.toPx())
                    }
                }
                arrow(Offset(size.width*.13f,size.height*.22f),Offset(size.width*.18f,size.height*.32f),Color(0xFF166876))
                s.route.zipWithNext().forEach { (a,b) -> arrow(point(a),point(b),Color(0xFFB45125)) }
            }
            Text("N ↑", Modifier.align(Alignment.TopEnd).padding(12.dp), color = Color(0xFF173F35))
            data.lugares.forEachIndexed { index, p ->
                val order = s.route.indexOf(p.id)
                val pinScale by animateFloatAsState(if (order >= 0) 1.1f else 1f,
                    if (motion) spring(dampingRatio = .5f, stiffness = 320f) else snap(), label = "Marca no mapa")
                FilledTonalButton(onClick = { touch(p.id) }, modifier = Modifier.offset(maxWidth*p.x-24.dp,maxHeight*p.y-24.dp).size(48.dp).graphicsLayer {
                    scaleX = pinScale; scaleY = pinScale
                }.semantics {
                    contentDescription = "${p.nome}, ${if (p.id in s.visited) "visitado" else "não visitado"}${if (order >= 0) ", conexão ${order+1}" else ""}"
                },
                    contentPadding = PaddingValues(0.dp), enabled = !s.solved && s.moving.isEmpty() && (!s.marking || p.id in s.visited),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = if (order >= 0) Color(0xFFF7C897) else if (p.id in s.visited) Color(0xFFBDDEA8) else Color.White)) {
                    Text(if (order >= 0) "${order+1}→" else "${index+1}")
                }
            }
            AnimatedCamo(Modifier.offset {
                val camoX = (maxWidth.toPx()*x-30.dp.toPx()).coerceIn(2.dp.toPx(), maxWidth.toPx()-62.dp.toPx())
                val camoY = (maxHeight.toPx()*y + anchor.dp.toPx())
                    .coerceIn(2.dp.toPx(), maxHeight.toPx()-62.dp.toPx())
                IntOffset(camoX.roundToInt(), camoY.roundToInt())
            }.size(60.dp).semantics { contentDescription = "Camo está em ${position.nome}" }, travelling = s.moving.isNotEmpty())
        }
    }
}
