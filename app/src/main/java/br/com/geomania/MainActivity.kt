package br.com.geomania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import br.com.geomania.ui.GeoManiaApp
import br.com.geomania.ui.MotionSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MotionSettings {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Color(0xFF245D4B), onPrimary = Color.White,
                primaryContainer = Color(0xFFDDEDBB), onPrimaryContainer = Color(0xFF193E31),
                secondary = Color(0xFF735B34), onSecondary = Color.White,
                secondaryContainer = Color(0xFFF4E5C9), onSecondaryContainer = Color(0xFF30220D),
                tertiary = Color(0xFF286B70), onTertiary = Color.White,
                tertiaryContainer = Color(0xFFD9F0EE), onTertiaryContainer = Color(0xFF123B3E),
                background = Color(0xFFF6F7F1), onBackground = Color(0xFF20382F),
                surface = Color(0xFFFCFDF9), onSurface = Color(0xFF20382F),
                surfaceVariant = Color(0xFFE8EBDF), onSurfaceVariant = Color(0xFF414A3F),
                outline = Color(0xFF59665A), outlineVariant = Color(0xFFC5CCC0)
            ), shapes = Shapes(
                extraSmall = RoundedCornerShape(10.dp), small = RoundedCornerShape(14.dp),
                medium = RoundedCornerShape(20.dp), large = RoundedCornerShape(26.dp),
                extraLarge = RoundedCornerShape(32.dp)
            )) { GeoManiaApp() } }
        }
    }
}
