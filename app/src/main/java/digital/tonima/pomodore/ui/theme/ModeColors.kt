package digital.tonima.pomodore.ui.theme

import androidx.compose.ui.graphics.Color
import digital.tonima.pomodore.data.model.TimerMode

data class ModeColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val accent: Color
)

fun getModeColors(mode: TimerMode): ModeColors {
    return when (mode) {
        TimerMode.WORK -> ModeColors(
            primary = WorkPrimary,
            secondary = WorkSecondary,
            background = WorkBackground,
            accent = WorkAccent
        )
        TimerMode.SHORT_BREAK -> ModeColors(
            primary = ShortBreakPrimary,
            secondary = ShortBreakSecondary,
            background = ShortBreakBackground,
            accent = ShortBreakAccent
        )
        TimerMode.LONG_BREAK -> ModeColors(
            primary = LongBreakPrimary,
            secondary = LongBreakSecondary,
            background = LongBreakBackground,
            accent = LongBreakAccent
        )
    }
}

