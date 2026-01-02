package digital.tonima.pomodore.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressTimer(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    val finalModifier = if (modifier == Modifier) {
        Modifier.size(280.dp)
    } else {
        modifier
    }

    CircularProgressIndicator(
        progress = { progress },
        modifier = finalModifier,
        strokeWidth = 12.dp,
        color = color,
        trackColor = trackColor,
    )
}

