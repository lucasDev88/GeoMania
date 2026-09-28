package br.com.geomania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import br.com.geomania.ui.GeoManiaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Color(0xFF245D4B), onPrimary = Color.White,
                primaryContainer = Color(0xFFDDEDBB), onPrimaryContainer = Color(0xFF193E31),
                secondary = Color(0xFF735B34), secondaryContainer = Color(0xFFF4E5C9),
                background = Color(0xFFF8F8F1), surface = Color(0xFFF8F8F1),
                onSurface = Color(0xFF20382F), surfaceVariant = Color(0xFFE8EBDF)
            )) { GeoManiaApp() }
        }
    }
}
