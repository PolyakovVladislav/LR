package com.lucky.rush.ui.extensions

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lucky.rush.ui.utils.Data
import java.io.IOException

internal inline fun Fragment.addOnBackPressedCallback(
    crossinline onBackPressed: () -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        },
    )
}

fun Fragment.playWinSound() {
    try {
        val assetManager = requireActivity().assets
        val mediaPlayer = MediaPlayer()
        val volume = Data.getInstance(requireActivity()).musicVolume / 100f
        mediaPlayer.setVolume(volume, volume)

        val afd = assetManager.openFd("win_sound.wav")
        mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
        mediaPlayer.isLooping = false
        mediaPlayer.prepare()
        mediaPlayer.start()
    } catch (ex: IOException) {
        Log.e("MediaPlayer", ex.localizedMessage, ex)
    }
}

@Suppress("DEPRECATION")
fun Fragment.vibr(duration: Long = 150L) {
    try {
        val v = ContextCompat.getSystemService(requireContext(), Vibrator::class.java)
        if (v?.hasVibrator() == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(
                    VibrationEffect.createOneShot(
                        duration,
                        (255 * Data.getInstance(requireActivity()).vibratingVolume / 100f).toInt(),
                    ),
                )
            } else {
                v.vibrate((duration * Data.getInstance(requireActivity()).vibratingVolume / 100f).toLong())
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@SuppressLint("SourceLockedOrientationActivity")
fun Fragment.setOrientationPortrait() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

@SuppressLint("SourceLockedOrientationActivity")
fun Fragment.setOrientationFull() {
    requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
}
