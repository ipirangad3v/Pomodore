package digital.tonima.pomodore.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import digital.tonima.pomodore.data.model.PomodoroSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pomodoro_settings")

class PomodoroRepository(private val context: Context) {

    private object PreferencesKeys {
        val WORK_DURATION = intPreferencesKey("work_duration")
        val SHORT_BREAK_DURATION = intPreferencesKey("short_break_duration")
        val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration")
        val SESSIONS_UNTIL_LONG_BREAK = intPreferencesKey("sessions_until_long_break")
    }

    val settings: Flow<PomodoroSettings> = context.dataStore.data.map { preferences: Preferences ->
        PomodoroSettings(
            workDurationMinutes = preferences[PreferencesKeys.WORK_DURATION] ?: 25,
            shortBreakDurationMinutes = preferences[PreferencesKeys.SHORT_BREAK_DURATION] ?: 5,
            longBreakDurationMinutes = preferences[PreferencesKeys.LONG_BREAK_DURATION] ?: 15,
            sessionsUntilLongBreak = preferences[PreferencesKeys.SESSIONS_UNTIL_LONG_BREAK] ?: 4
        )
    }

    suspend fun updateSettings(settings: PomodoroSettings) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.WORK_DURATION] = settings.workDurationMinutes
            preferences[PreferencesKeys.SHORT_BREAK_DURATION] = settings.shortBreakDurationMinutes
            preferences[PreferencesKeys.LONG_BREAK_DURATION] = settings.longBreakDurationMinutes
            preferences[PreferencesKeys.SESSIONS_UNTIL_LONG_BREAK] = settings.sessionsUntilLongBreak
        }
    }
}

