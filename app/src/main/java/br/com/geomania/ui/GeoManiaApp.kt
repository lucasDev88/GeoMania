package br.com.geomania.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.*
import br.com.geomania.data.*
import br.com.geomania.game.GameViewModel
import br.com.geomania.choices.ChoicesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeoManiaApp(vm: AppViewModel = viewModel()) {
    val content by vm.content.collectAsStateWithLifecycle()
    val error by vm.error.collectAsStateWithLifecycle()
    val stats by vm.stats.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val entry by nav.currentBackStackEntryAsState()
    val route = entry?.destination?.route ?: "home"
    Scaffold(topBar = {
        TopAppBar(title = { Text("Geo Mania", fontWeight = FontWeight.Bold) }, navigationIcon = {
            if (route != "home") TextButton(onClick = {
                if (route == "choices") backDispatcher?.onBackPressed() else nav.popBackStack()
            }) { Text("Voltar") }
        }, actions = { if (route != "about") TextButton(onClick = { nav.navigate("about") { launchSingleTop = true } }) { Text("Fontes") } })
    }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.TopCenter) {
            val data = content
            if (data == null) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (error == null) CircularProgressIndicator() else {
                        Text(error.orEmpty())
                        Button(onClick = vm::load) { Text("Tentar novamente") }
                    }
                }
            } else {
                NavHost(nav, startDestination = "home", modifier = Modifier.widthIn(max = 840.dp).fillMaxSize()) {
                    composable("home") { HomeScreen(data, stats, error) { nav.navigate(it) } }
                    composable("timeline") {
                        LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            item { Heading("Uma natureza, muitas histórias", "EXPLORAR") }
                            item { Note(data.learning.aviso) }
                            items(data.periodos, key = { it.id }) { period ->
                                NavigationCard(period.titulo, period.periodo) { nav.navigate("period/${period.id}") }
                            }
                            item { NavigationCard("Agora é sua vez: Escolhas da Terra", "Depois de explorar a história, decida como utilizar os recursos em quatro sociedades.") { nav.navigate("choices") } }
                        }
                    }
                    composable("period/{id}") { back ->
                        val period = data.periodos.find { it.id == back.arguments?.getString("id") }
                        if (period == null) Text("Contexto não encontrado.") else PeriodScreen(period,
                            sources = { nav.navigate("about") },
                            nextLabel = if (period == data.periodos.last()) "Jogar Escolhas da Terra" else "Próximo contexto",
                            next = {
                                val index = data.periodos.indexOf(period)
                                if (index == data.periodos.lastIndex) nav.navigate("choices")
                                else nav.navigate("period/${data.periodos[index + 1].id}") {
                                    popUpTo("period/{id}") { inclusive = true }
                                }
                            })
                    }
                    composable("resources") {
                        LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            item { Heading("Recursos que movem sociedades", "DESCOBRIR") }
                            items(data.recursos, key = { it.id }) { resource ->
                                NavigationCard(resource.titulo, resource.descricao) { nav.navigate("resource/${resource.id}") }
                            }
                        }
                    }
                    composable("resource/{id}") { back ->
                        val resource = data.recursos.find { it.id == back.arguments?.getString("id") }
                        if (resource == null) Text("Recurso não encontrado.") else ResourceScreen(resource) { nav.navigate("about") }
                    }
                    composable("game") {
                        val game: GameViewModel = viewModel(factory = viewModelFactory { initializer { GameViewModel(data.pares, vm.preferences) } })
                        GameScreen(game, stats)
                    }
                    composable("choices") {
                        val choices: ChoicesViewModel = viewModel(factory = viewModelFactory { initializer { ChoicesViewModel(data.escolhas) } })
                        ChoicesScreen(choices) { nav.navigate("about") }
                    }
                    composable("about") { AboutScreen(data) }
                }
            }
        }
    }
}

@Composable
fun Heading(title: String, eyebrow: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(eyebrow, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold,
            modifier = Modifier.semantics { heading() })
    }
}

@Composable
fun Note(text: String) {
    Surface(color = MaterialTheme.colorScheme.secondaryContainer, shape = RoundedCornerShape(20.dp)) {
        Text(text, Modifier.fillMaxWidth().padding(18.dp), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun NavigationCard(title: String, subtitle: String, onClick: () -> Unit) {
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyLarge)
            Text("Explorar →", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun HomeScreen(data: Conteudo, stats: Estatisticas, error: String?, navigate: (String) -> Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(18.dp)) {
        item {
            Surface(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(28.dp)) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("GEOGRAFIA • HISTÓRIA • NATUREZA", style = MaterialTheme.typography.labelMedium)
                    Globe()
                    Text(data.learning.tema, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text(data.learning.pergunta, style = MaterialTheme.typography.bodyLarge)
                    FilledTonalButton(onClick = { navigate("timeline") }) { Text("Começar a explorar") }
                }
            }
        }
        item { Text(data.learning.conceito, style = MaterialTheme.typography.bodyLarge) }
        item { NavigationCard("Escolhas da Terra", "Quatro fases, 16 decisões. Como sua sociedade vai utilizar a natureza?") { navigate("choices") } }
        item { NavigationCard("GeoMatch", "Encontre 6 pares e relacione cada recurso ao seu uso histórico.") { navigate("game") } }
        item { Note("Seu recorde: ${stats.recorde} pontos • ${stats.partidas} partidas concluídas") }
        if (error != null) item { Text(error, color = MaterialTheme.colorScheme.error) }
        item { NavigationCard("Recursos naturais", "Água, solo, florestas, minerais, biomassa e combustíveis fósseis.") { navigate("resources") } }
        item { Note(data.learning.destaque) }
        item { Text("Aprenda e jogue sem internet.", style = MaterialTheme.typography.labelLarge) }
    }
}

@Composable
private fun Globe() {
    Canvas(Modifier.size(104.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val r = size.minDimension * .46f
        drawCircle(Color(0xFFDDEDBB), r, center)
        drawCircle(Color(0xFF245D4B), r, center, style = Stroke(3.dp.toPx()))
        drawOval(Color(0xFF245D4B), Offset(center.x - r * .48f, center.y - r),
            androidx.compose.ui.geometry.Size(r * .96f, r * 2), style = Stroke(2.dp.toPx()))
        drawLine(Color(0xFF245D4B), Offset(center.x - r, center.y), Offset(center.x + r, center.y), 2.dp.toPx())
        drawLine(Color(0xFF245D4B), Offset(center.x - r * .86f, center.y - r * .5f), Offset(center.x + r * .86f, center.y - r * .5f), 2.dp.toPx())
        drawLine(Color(0xFF245D4B), Offset(center.x - r * .86f, center.y + r * .5f), Offset(center.x + r * .86f, center.y + r * .5f), 2.dp.toPx())
    }
}

@Composable
private fun Section(title: String, text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.semantics { heading() })
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun PeriodScreen(period: Periodo, sources: () -> Unit, nextLabel: String, next: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item { Heading(period.titulo, period.periodo) }
        item { Section("Organização da produção", period.formaProducao) }
        item { Note("Recursos em destaque\n${period.recursos.joinToString(", ")}") }
        item { Section("Acesso e apropriação", period.apropriacao) }
        item { Section("Técnicas e conhecimentos", period.tecnica) }
        item { Section("Usos", period.usos.joinToString("\n")) }
        item { Section("Transformações e conflitos", period.transformacoes.joinToString("\n")) }
        item { TextButton(onClick = sources) { Text("Referências temáticas: ${period.fontes.joinToString { "[$it]" }}") } }
        item { Button(onClick = next, modifier = Modifier.fillMaxWidth()) { Text(nextLabel) } }
    }
}

@Composable
private fun ResourceScreen(resource: Recurso, sources: () -> Unit) {
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        item { Heading(resource.titulo, "RECURSO NATURAL") }
        item { Text(resource.descricao, style = MaterialTheme.typography.bodyLarge) }
        item { Section("Onde aparece", resource.usos.joinToString("\n\n")) }
        item { Note(resource.cuidado) }
        item { TextButton(onClick = sources) { Text("Referências: ${resource.fontes.joinToString { "[$it]" }}") } }
    }
}

@Composable
private fun AboutScreen(data: Conteudo) {
    val uri = LocalUriHandler.current
    var linkError by remember { mutableStateOf(false) }
    LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
        item { Heading("Sobre e fontes", "GEO MANIA") }
        item { Section("Objetivo", data.learning.objetivo) }
        item { Section("Como o conteúdo foi preparado", data.learning.metodo) }
        item { Note(data.learning.aviso) }
        if (linkError) item { Text("Nenhum aplicativo disponível para abrir este link.", color = MaterialTheme.colorScheme.error) }
        items(data.fontes, key = { it.id }) { source ->
            OutlinedCard(onClick = {
                try { uri.openUri(source.url); linkError = false } catch (_: Exception) { linkError = true }
            }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("[${source.id}] ${source.titulo}", fontWeight = FontWeight.SemiBold)
                    Text("Abrir referência ↗", color = MaterialTheme.colorScheme.primary)
                }
            }
        }
        item { Text("Versão 0.2.0 • Sem cadastro e sem coleta de dados.\nRecorde e partidas ficam apenas neste aparelho.") }
    }
}
