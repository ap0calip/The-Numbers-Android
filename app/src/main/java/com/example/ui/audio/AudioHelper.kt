package com.example.ui.audio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import com.example.R
import java.util.Locale

class AudioHelper(private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    var soundEnabled: Boolean = true

    private var audioManager: AudioManager? = null
    private val appContext: Context = context.applicationContext

    init {
        val attrContext = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            appContext.createAttributionContext("tts")
        } else {
            appContext
        }

        try {
            audioManager = attrContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        } catch (_: Exception) {
            audioManager = null
        }
        try {
            tts = TextToSpeech(attrContext, this)
        } catch (_: Exception) {
            tts = null
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            tts?.setSpeechRate(0.6f)
            isTtsReady = true
        }
    }

    private fun playRawSound(resId: Int, volume: Float = 0.3f) {
        if (!soundEnabled) return
        try {
            val mediaPlayer = MediaPlayer.create(appContext, resId)
            mediaPlayer?.setVolume(volume, volume)
            mediaPlayer?.setOnCompletionListener { mp ->
                try {
                    mp.release()
                } catch (_: Exception) {}
            }
            mediaPlayer?.start()
        } catch (_: Exception) {}
    }

    fun playPopSound() {
        if (!soundEnabled) return
        try {
            audioManager?.playSoundEffect(AudioManager.FX_KEY_CLICK)
        } catch (_: Exception) {}
    }

    fun playPositiveSound() {
        playRawSound(R.raw.sound_positive, 0.3f)
    }

    fun playSuccessSound() {
        playPositiveSound()
    }

    fun playNegativeSound() {
        playRawSound(R.raw.sound_negative, 0.3f)
    }

    fun playStickerSound() {
        playRawSound(R.raw.sound_sticker, 0.3f)
    }

    fun speakNumber(num: Int, includePopSound: Boolean = true) {
        if (!soundEnabled) return
        if (includePopSound) playPopSound()
        if (isTtsReady && tts != null) {
            try {
                tts?.setSpeechRate(0.6f)
                tts?.speak(num.toString(), TextToSpeech.QUEUE_FLUSH, null, "count_$num")
            } catch (_: Exception) {}
        }
    }

    fun speakText(text: String) {
        if (!soundEnabled) return
        if (isTtsReady && tts != null) {
            try {
                tts?.setSpeechRate(0.6f)
                tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "text_speak")
            } catch (_: Exception) {}
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Exception) {}
    }
}
