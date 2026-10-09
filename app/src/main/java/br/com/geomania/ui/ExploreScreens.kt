package br.com.geomania.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.geomania.data.*

private val Forest = Color(0xFF173F35)
private val Lime = Color(0xFFDCECA6)
private val Clay = Color(0xFF8B482E)

@Composable
fun HomeScreen(data: Conteudo, stats: Estatisticas, error: String?, navigate: (String) -> Unit) {
    val fontScale = LocalDensity.current.fontScale
    val heroHeight = (190f * fontScale).coerceIn(190f, 320f).dp
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item {
            Surface(modifier = Modifier.entranceMotion(), shape = RoundedCornerShape(30.dp), color = Forest, contentColor = Color.White) {
                Column {
                    Box(Modifier.fillMaxWidth().height(heroHeight)) {
                        Landscape(Modifier.fillMaxSize())
                        AnimatedCamo(Modifier.align(Alignment.BottomEnd).width(148.dp).height(180.dp), interactive = true)
                        Column(Modifier.align(Alignment.BottomStart).fillMaxWidth(.60f).padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("SEU GUIA DE EXPLORAÇÃO", style = MaterialTheme.typography.labelSmall, color = Lime)
                            Text("Oi, explorador!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            Text("Toque no Camo para dar um oi!", style = MaterialTheme.typography.bodyMedium, color = Color.White)
                        }
                    }
                    Column(Modifier.padding(start = 24.dp, end = 24.dp, bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text("EXPLORE. CONECTE. TRANSFORME.", style = MaterialTheme.typography.labelMedium, color = Lime)
                        Text("A natureza faz parte\nda nossa história.", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                        Text(data.learning.tema, style = MaterialTheme.typography.titleMedium, color = Lime)
                        Text(data.learning.pergunta, style = MaterialTheme.typography.bodyLarge)
                        Button(onClick = { navigate("timeline") }, modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Lime, contentColor = Forest),
                            contentPadding = PaddingValues(16.dp)) { Text("Explorar a linha do tempo  →", fontWeight = FontWeight.Bold) }
                    }
                }
            }
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Metric("${data.periodos.size}", "contextos", Modifier.weight(1f))
                Metric("${data.recursos.size}", "recursos", Modifier.weight(1f))
                Metric("3", "jogos", Modifier.weight(1f))
            }
        }
        item { SectionTitle("Aprenda fazendo", "Três maneiras de conectar suas descobertas.") }
        item {
            Card(onClick = { navigate("expedition") }, modifier = Modifier.fillMaxWidth().entranceMotion(2), shape = RoundedCornerShape(26.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD5EEF0), contentColor = Forest)) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AnimatedCamo(Modifier.fillMaxWidth().height(150.dp))
                    Text("NOVO • GEOEXPEDIÇÃO", style = MaterialTheme.typography.labelLarge)
                    Text("Um mapa. Muitas conexões.", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Explore com Camo, seu camaleão guia. Investigue o rio, a floresta e o caminho dos recursos em três missões.")
                    Text("Começar expedição →", fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            FeatureCard("01", "ESCOLHAS DA TERRA", "Que futuro suas decisões constroem?",
                "Quatro sociedades, 16 decisões e consequências para discutir.", "Tomar decisões", Forest, Lime) { navigate("choices") }
        }
        item {
            FeatureCard("02", "GEOMATCH", "Cada recurso tem uma história.",
                "Encontre os pares entre natureza, técnicas e usos históricos.", "Encontrar conexões", Clay, Color(0xFFFFE8CF)) { navigate("game") }
        }
        item {
            OutlinedCard(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SEU PERCURSO NO GEOMATCH", style = MaterialTheme.typography.labelMedium)
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Column(Modifier.weight(1f)) {
                            Text("${stats.recorde}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text("melhor pontuação", style = MaterialTheme.typography.bodyMedium)
                        }
                        Column(Modifier.weight(1f)) {
                            Text("${stats.partidas}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                            Text("partidas concluídas", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
        if (error != null) item { Text(error, color = MaterialTheme.colorScheme.error) }
        item { SectionTitle("Olhe de perto", "O que usamos, como usamos e quem tem acesso.") }
        item { NavigationCard("Biblioteca de recursos", "Água, solo, florestas, minerais, biomassa e combustíveis fósseis.") { navigate("resources") } }
        item {
            Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("PARA PENSAR", style = MaterialTheme.typography.labelLarge)
                    Text(data.learning.destaque, style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { navigate("about") }) { Text("Conhecer as fontes  ↗") }
                }
            }
        }
        item { Text(data.learning.conceito, style = MaterialTheme.typography.bodyLarge) }
        item { Text("CONTEÚDO E JOGOS OFFLINE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary) }
    }
}

@Composable
private fun Metric(value: String, label: String, modifier: Modifier) {
    Surface(modifier, color = MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun SectionTitle(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, modifier = Modifier.semantics { heading() })
        Text(subtitle, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun FeatureCard(number: String, tag: String, title: String, description: String,
    action: String, ink: Color, paper: Color, click: () -> Unit) {
    Card(onClick = click, modifier = Modifier.fillMaxWidth().entranceMotion(2), shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = paper, contentColor = ink)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(color = ink, contentColor = paper, shape = CircleShape) { Text(number, Modifier.padding(12.dp), fontWeight = FontWeight.Bold) }
                Text(tag, style = MaterialTheme.typography.labelLarge, modifier = Modifier.weight(1f))
            }
            Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text(description, style = MaterialTheme.typography.bodyLarge)
            Text("$action  →", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Landscape(modifier: Modifier) {
    val phase by rememberAmbientPhase(16000)
    Canvas(modifier) {
        drawRect(Brush.verticalGradient(listOf(Color(0xFF2C6654), Forest)))
        val sun = Offset(size.width * (.77f + .015f * kotlin.math.sin(phase * 6.283f)), size.height * .38f)
        drawCircle(Lime, size.height * .2f, sun)
        drawCircle(Lime.copy(alpha = .2f), size.height * .29f, sun, style = Stroke(1.dp.toPx()))
        repeat(9) { i ->
            val orbit = phase*6.283f + i*.71f
            val x = size.width*(.06f+i*.1f) + kotlin.math.sin(orbit)*8.dp.toPx()
            val y = size.height*(.22f+(i%4)*.12f) + kotlin.math.cos(orbit)*5.dp.toPx()
            drawCircle(Lime.copy(alpha = .2f+.15f*(1f+kotlin.math.sin(orbit))), 1.5.dp.toPx(), Offset(x,y))
        }
        val far = Path().apply {
            moveTo(0f, size.height * .78f)
            cubicTo(size.width * .3f, size.height * .2f, size.width * .5f, size.height, size.width, size.height * .5f)
            lineTo(size.width, size.height); lineTo(0f, size.height); close()
        }
        drawPath(far, Color(0xFF61916C))
        val near = Path().apply {
            moveTo(0f, size.height * .95f)
            cubicTo(size.width * .38f, size.height * .5f, size.width * .64f, size.height * .5f, size.width, size.height)
            lineTo(0f, size.height); close()
        }
        drawPath(near, Forest)
        repeat(4) { i ->
            val x = size.width * (.12f + i * .085f) + kotlin.math.sin(phase*6.283f+i)*2.dp.toPx()
            val y = size.height * (.59f + (i % 2) * .08f)
            drawLine(Forest, Offset(x, y), Offset(x, size.height * .88f), 3.dp.toPx())
            drawCircle(Forest, size.height * .065f, Offset(x, y))
        }
    }
}

@Composable
fun TimelineScreen(data: Conteudo, open: (String) -> Unit, play: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(0.dp)) {
        item { Column(Modifier.padding(bottom = 24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Heading("Uma natureza.\nMuitas histórias.", "LINHA DO TEMPO")
            Text("Explore ${data.periodos.size} contextos e descubra como técnicas, trabalho e acesso aos recursos transformam territórios.", style = MaterialTheme.typography.bodyLarge)
            Note(data.learning.aviso)
        } }
        itemsIndexed(data.periodos, key = { _, p -> p.id }) { index, period ->
            Row(Modifier.height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                Column(Modifier.width(38.dp).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(shape = CircleShape, color = Forest, contentColor = Lime) {
                        Box(Modifier.size(38.dp), contentAlignment = Alignment.Center) { Text("${index + 1}", fontWeight = FontWeight.Bold) }
                    }
                    Box(Modifier.width(2.dp).weight(1f).background(MaterialTheme.colorScheme.outlineVariant))
                }
                OutlinedCard(onClick = { open(period.id) }, modifier = Modifier.weight(1f).padding(bottom = 18.dp), shape = RoundedCornerShape(22.dp)) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(period.periodo, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                        Text(period.titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(period.resumo, style = MaterialTheme.typography.bodyLarge)
                        Text("Explorar contexto  →", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        item { FeatureCard("→", "DA HISTÓRIA À DECISÃO", "Agora é sua vez.", "Experimente as consequências do uso dos recursos em Escolhas da Terra.", "Entrar no simulador", Forest, Lime, play) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PeriodScreen(period: Periodo, index: Int, total: Int, sources: () -> Unit,
    previous: (() -> Unit)?, nextLabel: String, next: () -> Unit) {
    val list = rememberLazyListState()
    LaunchedEffect(period.id) { list.scrollToItem(0) }
    LazyColumn(state = list, contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        item { Text("CONTEXTO ${index + 1} DE $total", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary) }
        item { LinearProgressIndicator(progress = { (index + 1f) / total }, modifier = Modifier.fillMaxWidth()) }
        item { Heading(period.titulo, period.periodo) }
        item { Text(period.resumo, style = MaterialTheme.typography.titleMedium) }
        item { FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            period.recursos.forEach { resource ->
                Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)) {
                    Text(resource, Modifier.padding(horizontal = 12.dp, vertical = 8.dp), style = MaterialTheme.typography.labelLarge)
                }
            }
        } }
        item { DetailBlock("01", "Como a sociedade produz", period.formaProducao) }
        item { DetailBlock("02", "Quem acessa e como utiliza", period.apropriacao) }
        item { DetailBlock("03", "Técnicas e conhecimentos", period.tecnica) }
        item { DetailBlock("04", "Usos no cotidiano", period.usos.joinToString("\n\n")) }
        item { DetailBlock("05", "O que se transforma", period.transformacoes.joinToString("\n\n")) }
        item { Note("PARA REFLETIR\n\n${period.pergunta}") }
        item { TextButton(onClick = sources) { Text("Consultar fontes ${period.fontes.joinToString { "[$it]" }}") } }
        item { Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = next, modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(16.dp)) { Text("$nextLabel  →") }
            if (previous != null) OutlinedButton(onClick = previous, modifier = Modifier.fillMaxWidth()) { Text("←  Contexto anterior") }
        } }
    }
}

@Composable
private fun DetailBlock(number: String, title: String, body: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("$number  /  $title", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.semantics { heading() })
        Text(body, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    }
}
