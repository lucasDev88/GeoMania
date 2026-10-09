package br.com.geomania.ui

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import br.com.geomania.expedition.ExpeditionViewModel

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
    val motion = LocalMotionEnabled.current
    Scaffold(topBar = {
        TopAppBar(title = { Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                CamoMascot(Modifier.size(38.dp).padding(3.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                Text("Geo Mania", fontWeight = FontWeight.Bold)
                Text("EXPLORE • APRENDA • JOGUE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } }, navigationIcon = {
            if (route != "home") TextButton(onClick = {
                if (route == "choices" || route == "expedition") backDispatcher?.onBackPressed() else nav.popBackStack()
            }) { Text("Voltar") }
        }, actions = { if (route != "about") TextButton(onClick = { nav.navigate("about") { launchSingleTop = true } }) { Text("Fontes") } })
    }, bottomBar = {
        val tabs = listOf(
            Triple("home", "⌂", "Início"), Triple("timeline", "↟", "História"),
            Triple("resources", "◈", "Recursos"), Triple("expedition", "⌖", "Mapa")
        )
        if (route in setOf("home", "timeline", "resources", "expedition")) {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                tabs.forEach { (destination, glyph, label) ->
                    NavigationBarItem(
                        selected = route == destination,
                        onClick = { if (route != destination) nav.navigate(destination) {
                            popUpTo("home") { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        } },
                        icon = { Text(glyph, style = MaterialTheme.typography.titleLarge) },
                        label = { Text(label) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
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
                NavHost(nav, startDestination = "home", modifier = Modifier.widthIn(max = 840.dp).fillMaxSize(),
                    enterTransition = { if (motion) fadeIn(tween(320)) + slideInHorizontally(tween(420)) { it/9 } + scaleIn(tween(420), initialScale = .97f) else EnterTransition.None },
                    exitTransition = { if (motion) fadeOut(tween(180)) + slideOutHorizontally(tween(300)) { -it/12 } else ExitTransition.None },
                    popEnterTransition = { if (motion) fadeIn(tween(320)) + slideInHorizontally(tween(420)) { -it/9 } else EnterTransition.None },
                    popExitTransition = { if (motion) fadeOut(tween(180)) + slideOutHorizontally(tween(300)) { it/12 } else ExitTransition.None }) {
                    composable("home") { HomeScreen(data, stats, error) { nav.navigate(it) } }
                    composable("timeline") {
                        TimelineScreen(data, open = { nav.navigate("period/$it") }, play = { nav.navigate("choices") })
                    }
                    composable("period/{id}") { back ->
                        val period = data.periodos.find { it.id == back.arguments?.getString("id") }
                        if (period == null) Text("Contexto não encontrado.") else PeriodScreen(period,
                            index = data.periodos.indexOf(period), total = data.periodos.size,
                            previous = if (data.periodos.indexOf(period) == 0) null else ({
                                val index = data.periodos.indexOf(period)
                                nav.navigate("period/${data.periodos[index - 1].id}") {
                                    popUpTo("period/{id}") { inclusive = true }
                                }
                            }),
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
                    composable("expedition") {
                        val expedition: ExpeditionViewModel = viewModel(factory = viewModelFactory { initializer { ExpeditionViewModel(data.expedicao) } })
                        ExpeditionScreen(expedition) { nav.navigate("about") }
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
    OutlinedCard(onClick = onClick, modifier = Modifier.fillMaxWidth().entranceMotion(1), shape = RoundedCornerShape(22.dp)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyLarge)
            Text("Explorar →", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelLarge)
        }
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
        item { Text("Versão 0.6.0 • Sem cadastro e sem coleta de dados.\nRecorde e partidas ficam apenas neste aparelho.") }
    }
}
