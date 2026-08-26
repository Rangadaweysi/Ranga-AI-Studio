package com.example.ui.screens

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Handler
import android.os.Looper
import kotlin.math.sin

/**
 * Real on-device audio synthesizer for Sound & Music playback preview.
 * Generates harmonic chords for Music, sweeps/clicks for SFX, gentle drones for Ambient,
 * and cinematic strings for Soundtracks without needing massive external MP3 assets.
 */
class SoundAudioEngine {

    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private val mainHandler = Handler(Looper.getMainLooper())
    private var playbackThread: Thread? = null

    fun playSound(
        soundType: String,
        category: String,
        durationSeconds: Int,
        volume: Float = 0.85f,
        fadeIn: Boolean = true,
        fadeOut: Boolean = true,
        onProgress: (currentSec: Int, progress: Float) -> Unit,
        onCompletion: () -> Unit
    ) {
        stop()

        isPlaying = true
        val sampleRate = 44100
        val effectiveDuration = durationSeconds.coerceIn(3, 120)
        val numSamples = sampleRate * effectiveDuration

        val baseFrequencies = when {
            soundType.contains("Música", ignoreCase = true) || category.contains("Música", ignoreCase = true) -> {
                // Harmonic triad chord (e.g. C major / A minor)
                listOf(261.63, 329.63, 392.00, 523.25)
            }
            soundType.contains("SFX", ignoreCase = true) || category.contains("Efeito", ignoreCase = true) -> {
                // Punchy blips and sweeps
                listOf(440.0, 880.0, 220.0)
            }
            soundType.contains("Ambiente", ignoreCase = true) || category.contains("Ambiente", ignoreCase = true) -> {
                // Low atmospheric harmonic drone
                listOf(110.0, 164.81, 220.0, 329.63)
            }
            else -> {
                // Cinematic brass / strings
                listOf(196.0, 246.94, 293.66, 392.0)
            }
        }

        playbackThread = Thread {
            try {
                val bufferSize = AudioTrack.getMinBufferSize(
                    sampleRate,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                ).coerceAtLeast(4096)

                val track = AudioTrack.Builder()
                    .setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    .setAudioFormat(
                        AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(sampleRate)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build()
                    )
                    .setBufferSizeInBytes(bufferSize)
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .build()

                audioTrack = track
                track.play()

                val shortBuffer = ShortArray(bufferSize / 2)
                var sampleIndex = 0
                val totalSamples = numSamples

                while (isPlaying && sampleIndex < totalSamples) {
                    val chunkSize = minOf(shortBuffer.size, totalSamples - sampleIndex)

                    for (i in 0 until chunkSize) {
                        val currentGlobalIndex = sampleIndex + i
                        val time = currentGlobalIndex.toDouble() / sampleRate
                        val progress = currentGlobalIndex.toDouble() / totalSamples

                        // Amplitude Envelope
                        var envelope = volume.toDouble()
                        if (fadeIn && progress < 0.15) {
                            envelope *= (progress / 0.15)
                        } else if (fadeOut && progress > 0.85) {
                            envelope *= ((1.0 - progress) / 0.15)
                        }

                        // Synthesis algorithm per type
                        var sampleVal = 0.0
                        if (soundType.contains("SFX", ignoreCase = true)) {
                            // Frequency sweep
                            val sweepFreq = 800.0 * (1.0 - (progress * 4.0 % 1.0)) + 150.0
                            sampleVal = sin(2.0 * Math.PI * sweepFreq * time) * envelope
                        } else if (soundType.contains("Ambiente", ignoreCase = true)) {
                            // Warm filtered drone with subtle tremolo
                            val lfo = 1.0 + 0.15 * sin(2.0 * Math.PI * 0.4 * time)
                            baseFrequencies.forEach { freq ->
                                sampleVal += sin(2.0 * Math.PI * freq * time) * (envelope * 0.35) * lfo
                            }
                        } else {
                            // Music chord with subtle rhythmic arpeggiator pulsation
                            val step = ((time * 3.0).toInt()) % baseFrequencies.size
                            val currentLeadFreq = baseFrequencies[step]
                            sampleVal += sin(2.0 * Math.PI * currentLeadFreq * time) * (envelope * 0.5)
                            baseFrequencies.forEach { freq ->
                                sampleVal += sin(2.0 * Math.PI * (freq / 2.0) * time) * (envelope * 0.2)
                            }
                        }

                        val clamped = (sampleVal * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                        shortBuffer[i] = clamped.toShort()
                    }

                    track.write(shortBuffer, 0, chunkSize)
                    sampleIndex += chunkSize

                    val currentSec = (sampleIndex / sampleRate)
                    val currentProgress = (sampleIndex.toFloat() / totalSamples.toFloat()).coerceIn(0f, 1f)

                    mainHandler.post {
                        if (isPlaying) {
                            onProgress(currentSec, currentProgress)
                        }
                    }
                }

                track.stop()
                track.release()
                audioTrack = null

                mainHandler.post {
                    if (isPlaying) {
                        isPlaying = false
                        onCompletion()
                    }
                }
            } catch (e: Exception) {
                mainHandler.post {
                    isPlaying = false
                    onCompletion()
                }
            }
        }.apply { start() }
    }

    fun stop() {
        isPlaying = false
        try {
            playbackThread?.interrupt()
            playbackThread = null
            audioTrack?.let {
                if (it.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    it.stop()
                }
                it.release()
            }
            audioTrack = null
        } catch (_: Exception) {}
    }

    fun isCurrentlyPlaying(): Boolean = isPlaying
}
