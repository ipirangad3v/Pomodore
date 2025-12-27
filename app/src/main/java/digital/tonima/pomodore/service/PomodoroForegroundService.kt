package digital.tonima.pomodore.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import digital.tonima.pomodore.MainActivity
import digital.tonima.pomodore.R
import digital.tonima.pomodore.data.model.TimerMode
import digital.tonima.pomodore.data.model.TimerState
import digital.tonima.pomodore.util.Constants
import digital.tonima.pomodore.util.formatTime

class PomodoroForegroundService : Service() {

    private val notificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_START, Constants.ACTION_RESUME -> {
                val timerState = intent.getSerializableExtra(Constants.EXTRA_TIMER_STATE) as? TimerState
                timerState?.let { updateNotification(it) }
            }
            Constants.ACTION_PAUSE -> {
                // Handled by MainActivity/ViewModel
            }
            Constants.ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                getString(R.string.notification_channel_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = getString(R.string.notification_channel_description)
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun updateNotification(timerState: TimerState) {
        val notification = createNotification(timerState)
        notificationManager.notify(Constants.NOTIFICATION_ID, notification)
        startForeground(Constants.NOTIFICATION_ID, notification)
    }

    private fun createNotification(timerState: TimerState): Notification {
        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, contentText, mode) = when (timerState) {
            is TimerState.Running -> {
                val titleRes = when (timerState.mode) {
                    TimerMode.WORK -> R.string.notification_title_work
                    TimerMode.SHORT_BREAK -> R.string.notification_title_short_break
                    TimerMode.LONG_BREAK -> R.string.notification_title_long_break
                }
                Triple(
                    getString(titleRes),
                    getString(R.string.notification_time_remaining, formatTime(timerState.timeRemainingMillis)),
                    timerState.mode
                )
            }
            is TimerState.Paused -> {
                val titleRes = when (timerState.mode) {
                    TimerMode.WORK -> R.string.notification_title_work
                    TimerMode.SHORT_BREAK -> R.string.notification_title_short_break
                    TimerMode.LONG_BREAK -> R.string.notification_title_long_break
                }
                Triple(
                    getString(titleRes),
                    getString(R.string.notification_paused),
                    timerState.mode
                )
            }
            else -> Triple(getString(R.string.app_name), "", TimerMode.WORK)
        }

        val builder = NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(contentIntent)
            .setOngoing(true)
            .setSilent(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)

        // Add action buttons
        when (timerState) {
            is TimerState.Running -> {
                builder.addAction(
                    0,
                    getString(R.string.notification_action_pause),
                    createActionIntent(Constants.ACTION_PAUSE)
                )
            }
            is TimerState.Paused -> {
                builder.addAction(
                    0,
                    getString(R.string.notification_action_resume),
                    createActionIntent(Constants.ACTION_RESUME)
                )
            }
            else -> {}
        }

        builder.addAction(
            0,
            getString(R.string.notification_action_skip),
            createActionIntent(Constants.ACTION_SKIP)
        )

        return builder.build()
    }

    private fun createActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MainActivity::class.java).apply {
            this.action = action
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        return PendingIntent.getActivity(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        fun startService(context: Context, timerState: TimerState) {
            val intent = Intent(context, PomodoroForegroundService::class.java).apply {
                action = Constants.ACTION_START
                putExtra(Constants.EXTRA_TIMER_STATE, timerState)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun updateService(context: Context, timerState: TimerState) {
            val intent = Intent(context, PomodoroForegroundService::class.java).apply {
                action = Constants.ACTION_RESUME
                putExtra(Constants.EXTRA_TIMER_STATE, timerState)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, PomodoroForegroundService::class.java).apply {
                action = Constants.ACTION_STOP
            }
            context.startService(intent)
        }
    }
}

