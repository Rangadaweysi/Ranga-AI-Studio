package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PurpleCreative

@Composable
fun NewSoundDialog(
    sound: SoundMusicEntity? = null,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onSave: (SoundMusicEntity) -> Unit
) {
    var title by remember { mutableStateOf(sound?.title ?: "") }
    var soundType by remember { mutableStateOf(sound?.type ?: "Música") }
    var category by remember { mutableStateOf(sound?.category ?: "Trilha Principal") }
    var mood by remember { mutableStateOf(sound?.mood ?: "Épico / Heroico") }
    var duration by remember { mutableStateOf(sound?.duration ?: "02:15") }
    var durationSeconds by remember { mutableIntStateOf(sound?.durationSeconds ?: 135) }
    var tempoBpm by remember { mutableIntStateOf(sound?.tempoBpm ?: 120) }
    var musicalKey by remember { mutableStateOf(sound?.musicalKey ?: "D menor") }
    var description by remember { mutableStateOf(sound?.description ?: "") }
    var format by remember { mutableStateOf(sound?.format ?: "WAV 48kHz") }
    var boundProjectId by remember { mutableStateOf(sound?.projectId ?: selectedProjectId) }

    val soundTypes = listOf("Música", "Efeito SFX", "Ambiente", "Trilha Sonora", "Personalizado")
    val moodOptions = listOf("Épico / Heroico", "Aventura", "Suspense / Tensão", "Alegre / Infantil", "Cômico / Divertido", "Dramático / Emocionante", "Mistério", "Sci-Fi / Futurista", "Romântico", "Terror")
    val categoryOptions = when (soundType) {
        "Efeito SFX" -> listOf("Explosão / Impacto", "Magia / Poder", "Passos / Movimento", "Interface / UI", "Armas / Lutas", "Veículos / Motores", "Monstros / Criaturas")
        "Ambiente" -> listOf("Floresta Mágica", "Cidade Futurista", "Chuva & Trovões", "Espaço Sideral", "Castelo Antigo", "Praia Tropical", "Laboratório")
        else -> listOf("Abertura / Tema Principal", "Batalha / Ação", "Emocional / Piano", "Encerramento", "Suspense Orquestral", "Eletrônica Energética", "Acústico Suave")
    }

    var expandedType by remember { mutableStateOf(false) }
    var expandedCategory by remember { mutableStateOf(false) }
    var expandedMood by remember { mutableStateOf(false) }
    var expandedProject by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (sound == null) Icons.Default.MusicNote else Icons.Default.Tune,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Column {
                    Text(
                        text = if (sound == null) "Novo Áudio / Música" else "Editar Áudio",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )
                    Text(
                        text = "Configuração completa de metadados e áudio do estúdio",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da Faixa / Efeito") },
                    placeholder = { Text("Ex: Tema de Batalha Final") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Type Selector
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = soundType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Tipo de Áudio") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedType = true },
                            shape = RoundedCornerShape(10.dp),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = NavyDark,
                                disabledBorderColor = Color(0xFFCBD5E1),
                                disabledLabelColor = Color.DarkGray
                            )
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { expandedType = true }
                        )
                        DropdownMenu(
                            expanded = expandedType,
                            onDismissRequest = { expandedType = false }
                        ) {
                            soundTypes.forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t) },
                                    onClick = {
                                        soundType = t
                                        expandedType = false
                                    }
                                )
                            }
                        }
                    }

                    // Category Selector
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoria") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedCategory = true },
                            shape = RoundedCornerShape(10.dp),
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = NavyDark,
                                disabledBorderColor = Color(0xFFCBD5E1),
                                disabledLabelColor = Color.DarkGray
                            )
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { expandedCategory = true }
                        )
                        DropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }
                        ) {
                            categoryOptions.forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c) },
                                    onClick = {
                                        category = c
                                        expandedCategory = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Mood Selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = mood,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Clima / Emoção (Mood)") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedMood = true },
                        shape = RoundedCornerShape(10.dp),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = NavyDark,
                            disabledBorderColor = Color(0xFFCBD5E1),
                            disabledLabelColor = Color.DarkGray
                        )
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { expandedMood = true }
                    )
                    DropdownMenu(
                        expanded = expandedMood,
                        onDismissRequest = { expandedMood = false }
                    ) {
                        moodOptions.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = {
                                    mood = m
                                    expandedMood = false
                                }
                            )
                        }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = {
                            duration = it
                            // Attempt to parse seconds
                            val parts = it.split(":")
                            if (parts.size == 2) {
                                val min = parts[0].toIntOrNull() ?: 0
                                val sec = parts[1].toIntOrNull() ?: 0
                                durationSeconds = min * 60 + sec
                            }
                        },
                        label = { Text("Duração (mm:ss)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = "$tempoBpm BPM",
                        onValueChange = {
                            val clean = it.filter { ch -> ch.isDigit() }
                            tempoBpm = clean.toIntOrNull() ?: 120
                        },
                        label = { Text("Andamento (BPM)") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = musicalKey,
                        onValueChange = { musicalKey = it },
                        label = { Text("Tonalidade") },
                        placeholder = { Text("Ex: Lá Menor / Dm") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = format,
                        onValueChange = { format = it },
                        label = { Text("Formato de Áudio") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                // Project Selector
                Box(modifier = Modifier.fillMaxWidth()) {
                    val currentProjectName = projects.find { it.id == boundProjectId }?.name ?: "Nenhum (Biblioteca Geral)"
                    OutlinedTextField(
                        value = currentProjectName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Vincular ao Projeto") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedProject = true },
                        shape = RoundedCornerShape(10.dp),
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = NavyDark,
                            disabledBorderColor = Color(0xFFCBD5E1),
                            disabledLabelColor = Color.DarkGray
                        )
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { expandedProject = true }
                    )
                    DropdownMenu(
                        expanded = expandedProject,
                        onDismissRequest = { expandedProject = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Nenhum (Biblioteca Geral)") },
                            onClick = {
                                boundProjectId = null
                                expandedProject = false
                            }
                        )
                        projects.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    boundProjectId = p.id
                                    expandedProject = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Notas de Produção / Descrição") },
                    placeholder = { Text("Ex: Usar quando o herói ativa a armadura de cristal.") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val projName = projects.find { it.id == boundProjectId }?.name
                    val finalEntity = (sound ?: SoundMusicEntity(
                        title = title.ifBlank { "Sem Título" },
                        category = category,
                        mood = mood,
                        duration = duration.ifBlank { "01:30" },
                        durationSeconds = durationSeconds,
                        description = description
                    )).copy(
                        title = title.ifBlank { "Sem Título" },
                        type = soundType,
                        category = category,
                        mood = mood,
                        duration = duration.ifBlank { "01:30" },
                        durationSeconds = durationSeconds,
                        tempoBpm = tempoBpm,
                        musicalKey = musicalKey,
                        format = format,
                        projectId = boundProjectId,
                        projectName = projName ?: "Biblioteca Geral",
                        description = description
                    )
                    onSave(finalEntity)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (sound == null) "Adicionar ao Estúdio" else "Salvar Alterações")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun UploadSoundDialog(
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onUploadSuccess: (SoundMusicEntity) -> Unit
) {
    var fileName by remember { mutableStateOf("trilha_cinematica_orquestral_48k.wav") }
    var soundType by remember { mutableStateOf("Música") }
    var category by remember { mutableStateOf("Trilha Sonora") }
    var mood by remember { mutableStateOf("Épico / Heroico") }
    var duration by remember { mutableStateOf("01:48") }
    var selectedProjId by remember { mutableStateOf(selectedProjectId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, tint = NavyPrimary)
                }
                Column {
                    Text("Importar Arquivo de Áudio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Suporta WAV, MP3, OGG e FLAC (Alta Fidelidade)", fontSize = 12.sp, color = Color.Gray)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F5F9)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Arquivo Selecionado para o Estúdio",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyDark
                        )
                        Text(
                            text = fileName,
                            fontSize = 12.sp,
                            color = NavyPrimary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Nome do Arquivo / Título") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = soundType,
                        onValueChange = { soundType = it },
                        label = { Text("Tipo") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duração") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = mood,
                    onValueChange = { mood = it },
                    label = { Text("Clima / Emoção") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val projName = projects.find { it.id == selectedProjId }?.name
                    val sound = SoundMusicEntity(
                        title = fileName.substringBeforeLast("."),
                        type = soundType,
                        category = category,
                        mood = mood,
                        duration = duration,
                        durationSeconds = 108,
                        tempoBpm = 124,
                        musicalKey = "C menor",
                        format = "WAV 48kHz 24-bit",
                        projectId = selectedProjId,
                        projectName = projName ?: "Biblioteca Geral",
                        description = "Importado com sucesso via painel de upload do estúdio.",
                        sampleAudioDesc = "Trilha importada em alta resolução para produções RANGA."
                    )
                    onUploadSuccess(sound)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Confirmar Importação")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun CreateSoundWithAiDialog(
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onSaveAiSound: (SoundMusicEntity) -> Unit
) {
    var aiPrompt by remember { mutableStateOf("Trilha épica orquestral com cordas dramáticas e sintetizador suave para o clímax da série.") }
    var soundType by remember { mutableStateOf("Trilha Sonora") }
    var mood by remember { mutableStateOf("Épico / Heroico") }
    var duration by remember { mutableStateOf("01:30") }
    var instruments by remember { mutableStateOf("Cordas, Trompas, Sintetizador e Percussão Taiko") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(PurpleCreative, CyanAccent))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Column {
                    Text("Gerador Musical & SFX com IA", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("RANGA Studio AI Audio Generator Engine", fontSize = 12.sp, color = Color.Gray)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = aiPrompt,
                    onValueChange = { aiPrompt = it },
                    label = { Text("Prompt de Áudio / Descrição da Cena") },
                    placeholder = { Text("Descreva o clima sonoro, ritmo e instrumentos...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PurpleCreative,
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    )
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = soundType,
                        onValueChange = { soundType = it },
                        label = { Text("Tipo") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    OutlinedTextField(
                        value = mood,
                        onValueChange = { mood = it },
                        label = { Text("Clima") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                OutlinedTextField(
                    value = instruments,
                    onValueChange = { instruments = it },
                    label = { Text("Instrumentos e Texturas") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = PurpleCreative.copy(alpha = 0.08f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleCreative, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "A IA gerará a estrutura harmônica, parâmetros de síntese e mapa de sonoplastia integrado ao estúdio.",
                            fontSize = 11.sp,
                            color = NavyDark
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val projName = projects.find { it.id == selectedProjectId }?.name
                    val generatedSound = SoundMusicEntity(
                        title = "Trilha IA: " + mood.substringBefore("/").trim(),
                        type = soundType,
                        category = "Gerado por IA",
                        mood = mood,
                        duration = duration,
                        durationSeconds = 90,
                        tempoBpm = 120,
                        musicalKey = "Dó Menor",
                        format = "WAV 48kHz (AI Render)",
                        projectId = selectedProjectId,
                        projectName = projName ?: "Biblioteca Geral",
                        isAiGenerated = true,
                        aiPrompt = aiPrompt,
                        description = "Trilha gerada por IA com base no prompt: $aiPrompt"
                    )
                    onSaveAiSound(generatedSound)
                    onOpenAiWithPrompt("Gere o mapa de áudio detalhado e partitura para o seguinte prompt sonoro: $aiPrompt")
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleCreative),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Gerar & Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AttachToSceneDialog(
    sound: SoundMusicEntity,
    scenes: List<SceneEntity>,
    episodes: List<EpisodeEntity>,
    series: List<SeriesEntity>,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onAttach: (sceneId: Long, sceneName: String, episodeId: Long?, seriesId: Long?, projectId: Long?) -> Unit
) {
    var selectedSceneId by remember { mutableStateOf(scenes.firstOrNull()?.id ?: 0L) }
    var sceneVolume by remember { mutableFloatStateOf(0.85f) }
    var startTimeSeconds by remember { mutableFloatStateOf(0f) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Movie, contentDescription = null, tint = NavyPrimary)
                }
                Column {
                    Text("Vincular Áudio à Cena", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Faixa: “${sound.title}”", fontSize = 12.sp, color = NavyPrimary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Selecione a cena de destino no roteiro:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NavyDark)

                if (scenes.isEmpty()) {
                    Text("Nenhuma cena cadastrada no estúdio.", color = Color.Gray, fontSize = 13.sp)
                } else {
                    scenes.forEach { scene ->
                        val isSelected = scene.id == selectedSceneId
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) NavyPrimary.copy(alpha = 0.1f) else Color(0xFFF8FAFC)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NavyPrimary) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedSceneId = scene.id }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Cena ${scene.sceneOrder}: ${scene.name}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) NavyPrimary else NavyDark
                                    )
                                    Text(
                                        text = "${scene.scenarioName} • ${scene.scenarioLighting} • Status: ${scene.status}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Volume no Roteiro", fontSize = 12.sp, color = Color.DarkGray)
                        Text("${(sceneVolume * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                    Slider(
                        value = sceneVolume,
                        onValueChange = { sceneVolume = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetScene = scenes.find { it.id == selectedSceneId }
                    if (targetScene != null) {
                        onAttach(
                            targetScene.id,
                            "Cena ${targetScene.sceneOrder}: ${targetScene.name}",
                            targetScene.episodeId,
                            null,
                            targetScene.projectId
                        )
                    }
                },
                enabled = scenes.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Vincular à Cena")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AttachToEpisodeDialog(
    sound: SoundMusicEntity,
    episodes: List<EpisodeEntity>,
    onDismiss: () -> Unit,
    onAttach: (episodeId: Long, episodeName: String, bgVolume: Float, fadeIn: Boolean, fadeOut: Boolean) -> Unit
) {
    var selectedEpisodeId by remember { mutableStateOf(episodes.firstOrNull()?.id ?: 0L) }
    var bgVolume by remember { mutableFloatStateOf(0.7f) }
    var fadeIn by remember { mutableStateOf(true) }
    var fadeOut by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Tv, contentDescription = null, tint = NavyPrimary)
                }
                Column {
                    Text("Trilha de Fundo do Episódio", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Faixa: “${sound.title}”", fontSize = 12.sp, color = NavyPrimary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Selecione o episódio:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NavyDark)

                if (episodes.isEmpty()) {
                    Text("Nenhum episódio cadastrado no estúdio.", color = Color.Gray, fontSize = 13.sp)
                } else {
                    episodes.forEach { ep ->
                        val isSelected = ep.id == selectedEpisodeId
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) NavyPrimary.copy(alpha = 0.1f) else Color(0xFFF8FAFC)
                            ),
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NavyPrimary) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedEpisodeId = ep.id }
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Episódio ${ep.episodeNumber}: ${ep.title}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) NavyPrimary else NavyDark
                                    )
                                    Text(
                                        text = "Duração: ${ep.duration} • Status: ${ep.status}",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Volume da Trilha de Fundo (Background)", fontSize = 12.sp, color = Color.DarkGray)
                        Text("${(bgVolume * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                    Slider(
                        value = bgVolume,
                        onValueChange = { bgVolume = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Fade In Automático", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NavyDark)
                        Text("Entrada suave no início da cena", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = fadeIn,
                        onCheckedChange = { fadeIn = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyPrimary, checkedTrackColor = CyanGlow)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("Fade Out Automático", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NavyDark)
                        Text("Transição suave no final do corte", fontSize = 11.sp, color = Color.Gray)
                    }
                    Switch(
                        checked = fadeOut,
                        onCheckedChange = { fadeOut = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyPrimary, checkedTrackColor = CyanGlow)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val targetEp = episodes.find { it.id == selectedEpisodeId }
                    if (targetEp != null) {
                        onAttach(
                            targetEp.id,
                            "Episódio ${targetEp.episodeNumber}: ${targetEp.title}",
                            bgVolume,
                            fadeIn,
                            fadeOut
                        )
                    }
                },
                enabled = episodes.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Definir Trilha")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun AudioEditorDialog(
    sound: SoundMusicEntity,
    onDismiss: () -> Unit,
    onSaveEdits: (newDuration: String, durationSecs: Int, volume: Float, fadeIn: Boolean, fadeOut: Boolean, asNewFile: Boolean, newTitle: String?) -> Unit
) {
    var trimStartSeconds by remember { mutableFloatStateOf(0f) }
    var trimEndSeconds by remember { mutableFloatStateOf(sound.durationSeconds.toFloat().coerceAtLeast(10f)) }
    var volume by remember { mutableFloatStateOf(sound.bgVolume) }
    var fadeIn by remember { mutableStateOf(sound.bgFadeIn) }
    var fadeOut by remember { mutableStateOf(sound.bgFadeOut) }
    var saveAsNew by remember { mutableStateOf(false) }
    var newTitle by remember { mutableStateOf("${sound.title} (Editado)") }

    val calculatedDurationSeconds = (trimEndSeconds - trimStartSeconds).toInt().coerceAtLeast(1)
    val formattedDuration = String.format("%02d:%02d", calculatedDurationSeconds / 60, calculatedDurationSeconds % 60)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.GraphicEq, contentDescription = null, tint = NavyPrimary)
                }
                Column {
                    Text("Editor de Áudio & Cortes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    Text("Faixa: “${sound.title}”", fontSize = 12.sp, color = NavyPrimary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = NavyDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Duração Cortada: $formattedDuration", color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Original: ${sound.duration}", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Simulated waveform with start and end markers
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(36) { idx ->
                                    val heightPercent = ((Math.sin(idx.toDouble() * 0.45) * 0.5 + 0.5) * 40.0 + 8.0).dp
                                    val inCutRange = (idx.toFloat() / 36f) in (trimStartSeconds / sound.durationSeconds.toFloat())..(trimEndSeconds / sound.durationSeconds.toFloat())
                                    Box(
                                        modifier = Modifier
                                            .width(3.dp)
                                            .height(heightPercent)
                                            .clip(RoundedCornerShape(2.dp))
                                            .background(if (inCutRange) CyanAccent else Color.White.copy(alpha = 0.2f))
                                    )
                                }
                            }
                        }
                    }
                }

                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Ponto Inicial (Trim In)", fontSize = 12.sp, color = Color.DarkGray)
                        Text("${trimStartSeconds.toInt()}s", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                    Slider(
                        value = trimStartSeconds,
                        onValueChange = {
                            if (it < trimEndSeconds - 2f) {
                                trimStartSeconds = it
                            }
                        },
                        valueRange = 0f..sound.durationSeconds.toFloat(),
                        colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                    )
                }

                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Ponto Final (Trim Out)", fontSize = 12.sp, color = Color.DarkGray)
                        Text("${trimEndSeconds.toInt()}s", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                    Slider(
                        value = trimEndSeconds,
                        onValueChange = {
                            if (it > trimStartSeconds + 2f) {
                                trimEndSeconds = it
                            }
                        },
                        valueRange = 0f..sound.durationSeconds.toFloat(),
                        colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                    )
                }

                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Ganho / Volume Geral", fontSize = 12.sp, color = Color.DarkGray)
                        Text("${(volume * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                    }
                    Slider(
                        value = volume,
                        onValueChange = { volume = it },
                        valueRange = 0f..1f,
                        colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Fade In Inicial", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NavyDark)
                    Switch(
                        checked = fadeIn,
                        onCheckedChange = { fadeIn = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyPrimary, checkedTrackColor = CyanGlow)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Fade Out Final", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NavyDark)
                    Switch(
                        checked = fadeOut,
                        onCheckedChange = { fadeOut = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyPrimary, checkedTrackColor = CyanGlow)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Salvar como Novo Arquivo", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = NavyDark)
                    Switch(
                        checked = saveAsNew,
                        onCheckedChange = { saveAsNew = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = NavyPrimary, checkedTrackColor = CyanGlow)
                    )
                }

                if (saveAsNew) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text("Nome do Novo Áudio") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveEdits(formattedDuration, calculatedDurationSeconds, volume, fadeIn, fadeOut, saveAsNew, newTitle)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Aplicar & Salvar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun DeleteSoundConfirmDialog(
    sound: SoundMusicEntity,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Excluir Áudio", fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
        },
        text = {
            Text("Tem certeza que deseja excluir o áudio “${sound.title}”? Esta ação não pode ser desfeita.")
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Excluir Definitivamente")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
