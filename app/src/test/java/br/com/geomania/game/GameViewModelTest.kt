package br.com.geomania.game

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelProvider
import br.com.geomania.data.GamePair
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val owner = ViewModelStore()
    private val results = mutableMapOf<String, Int>()
    private var failSave = false
    private lateinit var vm: GameViewModel
    @Before fun setup() {
        Dispatchers.setMain(dispatcher)
        val pairs = (1..6).map { GamePair("$it", "A $it", "B $it", "Por quê $it", "p", "r") }
        val store = object : ResultStore {
            override suspend fun saveResult(session: String, score: Int) {
                if (failSave) throw java.io.IOException("Falha de teste")
                results[session] = score
            }
        }
        vm = ViewModelProvider(owner, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T = GameViewModel(pairs, store) as T
        })[GameViewModel::class.java]
    }
    @After fun teardown() { owner.clear(); Dispatchers.resetMain() }

    @Test fun timerOnlyRunsWhilePlayingAndVisible() = runTest(dispatcher) {
        vm.setActive(true)
        advanceTimeBy(3000); runCurrent()
        assertEquals(0, vm.state.value.elapsedSeconds)
        vm.select(0)
        advanceTimeBy(3000); runCurrent()
        assertEquals(3, vm.state.value.elapsedSeconds)
        vm.setActive(false)
        advanceTimeBy(3000); runCurrent()
        assertEquals(3, vm.state.value.elapsedSeconds)
        vm.setActive(true)
        vm.select(1)
        advanceTimeBy(3000); runCurrent()
        assertEquals(3, vm.state.value.elapsedSeconds)
        owner.clear()
    }
    @Test fun mismatchResetsAfterDelayAndRestartCancelsPendingReset() = runTest(dispatcher) {
        vm.select(0); vm.select(2)
        advanceTimeBy(1300); runCurrent()
        assertTrue(vm.state.value.selected.isEmpty())
        vm.select(0); vm.select(2)
        vm.restart(); vm.select(4)
        advanceTimeBy(2000); runCurrent()
        assertEquals(listOf(4), vm.state.value.selected)
        assertEquals(0, vm.state.value.attempts)
        owner.clear()
    }
    @Test fun finishedGameSavesAndRetryUsesSameSession() = runTest(dispatcher) {
        failSave = true
        repeat(6) { vm.select(it * 2); vm.select(it * 2 + 1); vm.dismissExplanation() }
        runCurrent()
        assertTrue(vm.state.value.isFinished)
        assertTrue(vm.saveStatus.value.startsWith("Não"))
        failSave = false
        vm.save(); runCurrent()
        vm.save(); runCurrent()
        assertEquals(1, results.size)
        assertEquals(1000, results.values.single())
        vm.restart()
        repeat(6) { vm.select(it * 2); vm.select(it * 2 + 1); vm.dismissExplanation() }
        runCurrent()
        assertEquals(2, results.size)
        owner.clear()
    }
}
