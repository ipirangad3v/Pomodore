package digital.tonima.pomodore.util

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log

class SoundAndVibrationManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null

    /**
     * Plays a notification sound using the system default notification sound
     */
    fun playCompletionSound() {
        try {
            // Release any existing media player
            mediaPlayer?.release()

            // Use system default notification sound
            val notification = android.provider.Settings.System.DEFAULT_NOTIFICATION_URI
            mediaPlayer = MediaPlayer.create(context, notification)

            mediaPlayer?.setOnCompletionListener { mp ->
                mp.release()
                mediaPlayer = null
            }

            mediaPlayer?.start()
        } catch (e: Exception) {
            Log.e("SoundAndVibration", "Error playing completion sound", e)
        }
    }

    /**
     * Triggers a short vibration
     */
    fun vibrateShort() {
        try {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Short vibration pattern: vibrate for 300ms
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        300,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(300)
            }
        } catch (e: Exception) {
            Log.e("SoundAndVibration", "Error vibrating", e)
        }
    }

    /**
     * Plays sound and vibrates for timer completion
     */
    fun notifyTimerComplete() {
        playCompletionSound()
        vibrateShort()
    }

    /**
     * Releases resources
     */
    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

