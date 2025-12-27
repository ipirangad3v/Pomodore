package digital.tonima.pomodore.util

object Constants {
    const val NOTIFICATION_CHANNEL_ID = "pomodoro_timer_channel"
    const val NOTIFICATION_ID = 1001
    const val ACTION_START = "ACTION_START"
    const val ACTION_PAUSE = "ACTION_PAUSE"
    const val ACTION_RESUME = "ACTION_RESUME"
    const val ACTION_STOP = "ACTION_STOP"
    const val ACTION_SKIP = "ACTION_SKIP"
    const val EXTRA_TIMER_STATE = "EXTRA_TIMER_STATE"
}

fun formatTime(millis: Long): String {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}

