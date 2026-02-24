package com.sillytavern.android.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6B8DD6),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3D5A9E),
    onPrimaryContainer = Color(0xFFDCE3FF),
    
    secondary = Color(0xFFB8C4E8),
    onSecondary = Color(0xFF222D4C),
    secondaryContainer = Color(0xFF384464),
    onSecondaryContainer = Color(0xFFDCE3FF),
    
    tertiary = Color(0xFFD6B8E8),
    onTertiary = Color(0xFF3D2C4C),
    tertiaryContainer = Color(0xFF544265),
    onTertiaryContainer = Color(0xFFF2DAFF),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE3E1EC),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE3E1EC),
    surfaceVariant = Color(0xFF45464F),
    onSurfaceVariant = Color(0xFFC6C5D4),
    
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    outline = Color(0xFF8F909A),
    outlineVariant = Color(0xFF45464F),
    
    inverseSurface = Color(0xFFE3E1EC),
    inverseOnSurface = Color(0xFF1E1E1E),
    inversePrimary = Color(0xFF4A5D9E)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF4A5D9E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE3FF),
    onPrimaryContainer = Color(0xFF00164E),
    
    secondary = Color(0xFF575E71),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDCE3FF),
    onSecondaryContainer = Color(0xFF131C2C),
    
    tertiary = Color(0xFF6B5778),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF2DAFF),
    onTertiaryContainer = Color(0xFF251431),
    
    background = Color(0xFFFFFBFF),
    onBackground = Color(0xFF1E1E1E),
    surface = Color(0xFFFFFBFF),
    onSurface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFFE3E1EC),
    onSurfaceVariant = Color(0xFF45464F),
    
    error = Color(0xFFBA1A1A),
    onError = Color.White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    
    outline = Color(0xFF767680),
    outlineVariant = Color(0xFFC6C5D4),
    
    inverseSurface = Color(0xFF303034),
    inverseOnSurface = Color(0xFFF3F0F4),
    inversePrimary = Color(0xFF6B8DD6)
)

@Composable
fun SillyTavernTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
