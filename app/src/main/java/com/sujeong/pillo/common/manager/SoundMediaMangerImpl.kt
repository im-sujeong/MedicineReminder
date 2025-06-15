package com.sujeong.pillo.common.manager

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.sujeong.pillo.R

class SoundMediaMangerImpl(
    private val context: Context
): SoundMediaManger {
    private var mediaPlayer: MediaPlayer? = null

    override fun play() {
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
            mediaPlayer?.apply {
                isLooping = true  // 알람이 끝날 때까지 반복

                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )

                start()
            }
        }
    }

    override fun stop() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}