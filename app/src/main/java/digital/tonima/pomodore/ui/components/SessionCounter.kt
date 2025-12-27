package digital.tonima.pomodore.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import digital.tonima.pomodore.R

@Composable
fun SessionCounter(
    currentSession: Int,
    completedSessions: Int,
    totalCycles: Int = 4,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.session_count, currentSession, totalCycles),
            style = MaterialTheme.typography.titleMedium,
            color = color
        )
        Text(
            text = stringResource(R.string.completed_sessions, completedSessions),
            style = MaterialTheme.typography.bodyMedium,
            color = color.copy(alpha = 0.7f)
        )
    }
}

