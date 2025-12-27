package digital.tonima.pomodore.data.model

import java.io.Serializable

sealed class TimerState : Serializable {
    data object Idle : TimerState()
    data class Running(
        val mode: TimerMode,
        val timeRemainingMillis: Long,
        val totalTimeMillis: Long
    ) : TimerState()
    data class Paused(
        val mode: TimerMode,
        val timeRemainingMillis: Long,
        val totalTimeMillis: Long
    ) : TimerState()
    data class Completed(val mode: TimerMode) : TimerState()
}

