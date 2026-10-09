package br.com.geomania.ui

import android.animation.ValueAnimator
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import kotlin.math.*

val LocalMotionEnabled = staticCompositionLocalOf { true }
private val LocalMotionActive = staticCompositionLocalOf { true }

/** Stops decorative loops off-screen and follows the Android animator setting live. */
@Composable
fun MotionSettings(content: @Composable () -> Unit) {
    val resolver = LocalContext.current.contentResolver
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var enabled by remember { mutableStateOf(ValueAnimator.areAnimatorsEnabled()) }
    var active by remember { mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) }
    DisposableEffect(resolver, lifecycle) {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) { enabled = ValueAnimator.areAnimatorsEnabled() }
        }
        resolver.registerContentObserver(Settings.Global.getUriFor(Settings.Global.ANIMATOR_DURATION_SCALE), false, observer)
        val listener = LifecycleEventObserver { _, _ ->
            active = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
            enabled = ValueAnimator.areAnimatorsEnabled()
        }
        lifecycle.addObserver(listener)
        onDispose { resolver.unregisterContentObserver(observer); lifecycle.removeObserver(listener) }
    }
    CompositionLocalProvider(LocalMotionEnabled provides enabled, LocalMotionActive provides (enabled && active), content = content)
}

@Composable
fun Modifier.entranceMotion(order: Int = 0): Modifier {
    val enabled = LocalMotionEnabled.current
    var arrived by rememberSaveable { mutableStateOf(false) }
    val phase = remember { Animatable(if (arrived) 1f else 0f) }
    LaunchedEffect(enabled) {
        if (!enabled) phase.snapTo(1f) else if (!arrived) {
            delay((order.coerceIn(0, 4) * 65).toLong())
            phase.animateTo(1f, spring(dampingRatio = .78f, stiffness = 190f))
        }
        arrived = true
    }
    return this.graphicsLayer {
        val p = phase.value
        alpha = p.coerceIn(0f, 1f)
        translationY = (1f-p) * 32.dp.toPx()
        scaleX = .94f + .06f*p; scaleY = scaleX
        rotationZ = (1f-p)*-1.5f
    }
}

@Composable
fun rememberAmbientPhase(period: Int = 5200): State<Float> {
    val enabled = LocalMotionActive.current
    val phase = remember { Animatable(0f) }
    LaunchedEffect(enabled, period) {
        if (!enabled) phase.snapTo(0f) else while (ValueAnimator.areAnimatorsEnabled()) {
            phase.snapTo(0f)
            phase.animateTo(1f, tween(period, easing = LinearEasing))
            delay(16)
        }
    }
    return phase.asState()
}

@Composable
fun AnimatedCamo(modifier: Modifier = Modifier, travelling: Boolean = false, celebrating: Boolean = false, interactive: Boolean = false) {
    val phase by rememberAmbientPhase(if (travelling) 650 else 5200)
    val enabled = LocalMotionEnabled.current
    val reaction = remember { Animatable(0f) }
    var greetings by remember { mutableIntStateOf(0) }
    LaunchedEffect(celebrating, enabled, greetings) {
        reaction.snapTo(0f)
        if ((celebrating || greetings > 0) && enabled) reaction.animateTo(0f, keyframes {
            durationMillis = 1000
            0f at 0; 1f at 220 using FastOutSlowInEasing
            0f at 450; .6f at 650; 0f at 1000
        })
    }
    val greeting = if (interactive) Modifier.clickable(onClickLabel = "Cumprimentar Camo", role = Role.Button) { greetings++ } else Modifier
    CamoMascot(modifier.then(greeting).graphicsLayer {
        val wave = sin(phase * 2f * PI.toFloat())
        translationY = -abs(wave) * (if (travelling) 5 else 3).dp.toPx() - reaction.value*12.dp.toPx()
        rotationZ = wave*(if (travelling) 4f else 1.5f) + reaction.value*-7f
        scaleX = 1f + reaction.value*.07f
        scaleY = 1f + reaction.value*.07f
    })
}

@Composable
fun Celebration() {
    val enabled = LocalMotionEnabled.current
    var played by rememberSaveable { mutableStateOf(false) }
    val phase = remember { Animatable(if (played) 1f else 0f) }
    LaunchedEffect(enabled) {
        if (!enabled) phase.snapTo(1f) else if (!played) phase.animateTo(1f, tween(1800, easing = LinearEasing))
        played = true
    }
    Box(Modifier.fillMaxWidth().height(170.dp), contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize().clearAndSetSemantics { }) {
            val t = phase.value
            if (t > 0f && t < 1f) repeat(32) { i ->
                val angle = i * 2f*PI.toFloat()/32
                val velocity = (.3f + (i%5)*.055f)*size.width
                val x = size.width/2 + cos(angle)*velocity*t
                val y = size.height*.55f + sin(angle)*velocity*t + size.height*1.1f*t*t
                val center = Offset(x,y)
                val ink = listOf(Color(0xFF397552),Color(0xFFC18A28),Color(0xFF388B91))[i%3].copy(alpha = (1f-t).coerceIn(0f,1f))
                rotate(i*29f+t*280f, center) {
                    drawOval(ink, center-Offset(3.dp.toPx(),1.5.dp.toPx()), androidx.compose.ui.geometry.Size(6.dp.toPx(),3.dp.toPx()))
                }
            }
        }
        AnimatedCamo(Modifier.size(130.dp), celebrating = true)
    }
}
