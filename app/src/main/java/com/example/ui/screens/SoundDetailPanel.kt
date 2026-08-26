package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Loop
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.SoundMusicEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PurpleCreative

@Composable
fun SoundAudioPlayerCard(
    sound: SoundMusicEntity,
    isPlaying: Boolean,
    currentPlaybackSeconds: Int,
    playbackProgress: Float,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onOpenEditor: () -> Unit
) {
    var volume by remember { mutableFloatStateOf(sound.bgVolume) }
    var isLooping by remember { mutableStateOf<Boolean>(sound.isLooping) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val animatedWaveScale by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(450, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wave_scale"
    )

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = NavyDeep),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top Bar with Icon & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when {
                                sound.type.contains("SFX", ignoreCase = true) || sound.type.contains("Efeito", ignoreCase = true) -> Icons.Default.GraphicEq
                                sound.type.contains("Ambiente", ignoreCase = true) -> Icons.Default.Audiotrack
                                else -> Icons.Default.MusicNote
                            },
                            contentDescription = null,
                            tint = CyanAccent,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Column {
                        Text(
                            text = sound.title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            maxLines = 1
                        )
                        Text(
                            text = "${sound.type} • ${sound.category} • ${sound.mood}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isPlaying) CyanAccent.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = if (isPlaying) "Reproduzindo" else "Pronto",
                        color = if (isPlaying) CyanAccent else Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Animated Visualizer Waveform Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF070F1E))
                    .padding(horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(32) { idx ->
                        val baseHeight = ((Math.sin(idx.toDouble() * 0.5) * 0.5 + 0.5) * 44.0 + 8.0).dp
                        val animatedHeight = if (isPlaying) {
                            val factor = if (idx % 2 == 0) animatedWaveScale else (1.4f - animatedWaveScale)
                            (baseHeight.value * factor).coerceIn(6f, 54f).dp
                        } else {
                            baseHeight
                        }

                        val isPassed = (idx.toFloat() / 32f) <= playbackProgress

                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(animatedHeight)
                                .clip(RoundedCornerShape(2.dp))
                                .background(
                                    if (isPassed) CyanAccent
                                    else if (isPlaying) Color(0xFF38BDF8).copy(alpha = 0.5f)
                                    else Color.White.copy(alpha = 0.2f)
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrubber Slider & Timing
            Slider(
                value = playbackProgress,
                onValueChange = {},
                colors = SliderDefaults.colors(
                    thumbColor = CyanAccent,
                    activeTrackColor = CyanAccent,
                    inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val curMin = currentPlaybackSeconds / 60
                val curSec = currentPlaybackSeconds % 60
                Text(
                    text = String.format("%02d:%02d", curMin, curSec),
                    color = CyanAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = sound.duration,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Player Control Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isLooping = !isLooping },
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        Icons.Default.Loop,
                        contentDescription = "Repetição",
                        tint = if (isLooping) CyanAccent else Color.White.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPlay,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            Icons.Default.Replay,
                            contentDescription = "Reiniciar",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Large Play / Pause button
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Brush.linearGradient(listOf(CyanAccent, Color(0xFF0284C7))))
                            .clickable { if (isPlaying) onStop() else onPlay() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                            tint = NavyDark,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    IconButton(
                        onClick = onStop,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            Icons.Default.Stop,
                            contentDescription = "Parar",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onOpenEditor,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        Icons.Default.ContentCut,
                        contentDescription = "Editar Áudio",
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Volume Control Slider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.VolumeUp,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(18.dp)
                )
                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    valueRange = 0f..1f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = Color.White,
                        inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${(volume * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SoundQuickEditorCard(
    sound: SoundMusicEntity,
    onOpenFullEditor: () -> Unit,
    onEditMetadata: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Tune, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    }
                    Text("Ajustes & Parâmetros", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                }

                TextButton(onClick = onEditMetadata) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar Metadados", fontSize = 12.sp, color = NavyPrimary)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Andamento", fontSize = 10.sp, color = Color.Gray)
                        Text("${sound.tempoBpm} BPM", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Tonalidade", fontSize = 10.sp, color = Color.Gray)
                        Text(sound.musicalKey, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Duração Total", fontSize = 10.sp, color = Color.Gray)
                        Text(sound.duration, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    }
                }
            }

            Button(
                onClick = onOpenFullEditor,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Abrir Editor de Cortes & Fades", fontSize = 13.sp)
            }
        }
    }
}

@Composable
fun SoundSceneEpisodeLinkCard(
    sound: SoundMusicEntity,
    onAttachToScene: () -> Unit,
    onAttachToEpisode: () -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(EmeraldSuccess.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                }
                Column {
                    Text("Integração com Roteiro", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Vincule faixas e efeitos diretamente às cenas e episódios", fontSize = 11.sp, color = Color.Gray)
                }
            }

            // Scene Attachment Status
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Vínculo com Cena", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = sound.sceneName ?: "Nenhuma cena associada",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (sound.sceneName != null) NavyPrimary else Color.DarkGray
                            )
                        }
                        OutlinedButton(
                            onClick = onAttachToScene,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (sound.sceneName != null) "Alterar Cena" else "Usar na Cena", fontSize = 12.sp)
                        }
                    }
                }
            }

            // Episode Attachment Status
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Trilha de Fundo do Episódio", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = sound.episodeName ?: "Não definida como fundo",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (sound.episodeName != null) NavyPrimary else Color.DarkGray
                            )
                        }
                        OutlinedButton(
                            onClick = onAttachToEpisode,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(if (sound.episodeName != null) "Alterar Ep." else "Usar no Ep.", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoundTechnicalSpecsCard(sound: SoundMusicEntity) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E7FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                }
                Text("Especificações Técnicas", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDark)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                TechSpecRow("Formato & Resolução", sound.format)
                TechSpecRow("Taxa de Amostragem", "48.000 Hz / 24-bit")
                TechSpecRow("Canais de Áudio", "Estéreo (L / R)")
                TechSpecRow("Bitrate", "320 kbps High Quality")
                TechSpecRow("Origem da Faixa", if (sound.isAiGenerated) "Gerado por IA (RANGA Audio)" else "Biblioteca do Estúdio")
                TechSpecRow("Usos em Cenas", "${sound.usageScenesCount} cenas")
                TechSpecRow("Usos em Episódios", "${sound.usageEpisodesCount} episódios")
            }
        }
    }
}

@Composable
private fun TechSpecRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = NavyDark)
    }
}

@Composable
fun SoundAiAssistantCard(
    selectedSound: SoundMusicEntity?,
    onOpenAiWithPrompt: (String) -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(PurpleCreative, CyanAccent))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
                Column {
                    Text("Diretor de Áudio com IA", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Sugestões de sonoplastia e mixagem para o roteiro", fontSize = 11.sp, color = Color.Gray)
                }
            }

            val suggestions = listOf(
                "Sugerir mapa de efeitos sonoros para as cenas de ação do episódio",
                "Indicar trilha ideal para o clímax dramático da temporada",
                "Planejar transições e cortes sonoros suaves (fades) entre cenas",
                "Criar proposta de tema musical exclusivo para o protagonista"
            )

            suggestions.forEach { prompt ->
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PurpleCreative.copy(alpha = 0.05f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PurpleCreative.copy(alpha = 0.15f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenAiWithPrompt(prompt) }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleCreative, modifier = Modifier.size(16.dp))
                        Text(
                            text = prompt,
                            fontSize = 12.sp,
                            color = NavyDark,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(Icons.Default.Send, contentDescription = null, tint = PurpleCreative, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}
