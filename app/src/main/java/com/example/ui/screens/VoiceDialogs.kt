package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharacterEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.VoiceEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PurpleCreative

@Composable
fun VoiceFormDialog(
    editingVoice: VoiceEntity?,
    characters: List<CharacterEntity>,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onSave: (VoiceEntity) -> Unit,
    onAiSuggest: (String) -> Unit
) {
    var name by remember { mutableStateOf(editingVoice?.name ?: "") }
    var gender by remember { mutableStateOf(editingVoice?.gender ?: "Masculina") }
    var ageCategory by remember { mutableStateOf(editingVoice?.ageCategory ?: "Jovem") }
    var type by remember { mutableStateOf(editingVoice?.type ?: "Jovem") }
    var style by remember { mutableStateOf(editingVoice?.style ?: "Cartoon") }
    var styleTag2 by remember { mutableStateOf(editingVoice?.styleTag2 ?: "Aventura") }
    var tone by remember { mutableStateOf(editingVoice?.tone ?: "Alegre") }
    var language by remember { mutableStateOf(editingVoice?.language ?: "Português (Brasil)") }
    var statusTag by remember { mutableStateOf(editingVoice?.statusTag ?: "Salva") }
    var sampleAudioDesc by remember { mutableStateOf(editingVoice?.sampleAudioDesc ?: "") }
    var sampleText by remember { mutableStateOf(editingVoice?.sampleText ?: "Olá! Eu sou uma nova voz pronta para ser gravada no estúdio!") }
    var approximateAge by remember { mutableStateOf(editingVoice?.approximateAge ?: "15 - 20 anos") }

    var selectedCharacterName by remember { mutableStateOf(editingVoice?.assignedCharacter ?: "") }
    var selectedCharacterEmoji by remember { mutableStateOf(editingVoice?.characterEmoji ?: "") }
    var selectedCharacterId by remember { mutableStateOf(editingVoice?.characterId) }
    var selectedProjId by remember { mutableStateOf(editingVoice?.projectId ?: selectedProjectId ?: projects.firstOrNull()?.id ?: 1L) }
    var selectedProjName by remember {
        mutableStateOf(
            editingVoice?.projectName ?: projects.find { it.id == selectedProjId }?.name ?: "Aventuras das Frutas"
        )
    }

    var speed by remember { mutableFloatStateOf(editingVoice?.speed ?: 1.0f) }
    var pitch by remember { mutableFloatStateOf(editingVoice?.pitch ?: 0.0f) }
    var expressiveness by remember { mutableFloatStateOf(editingVoice?.expressiveness ?: 70f) }

    val genderOptions = listOf("Masculina", "Feminina", "Infantil", "Neutra")
    val ageOptions = listOf("Criança", "Jovem", "Adulta", "Idosa", "Robô", "Criatura")
    val styleOptions = listOf("Cartoon", "Natural", "Dramática", "Comédia", "Infantil", "Fantasia", "Futurista", "Vilão")
    val secondaryStyleOptions = listOf("Aventura", "Divertida", "Amável", "Sábia", "Fofa", "Misteriosa", "Narrador", "Robô")
    val languageOptions = listOf("Português (Brasil)", "Inglês (US)", "Espanhol", "Francês", "Japonês")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (editingVoice != null) "Editar Perfil Vocal" else "Nova Voz para o Estúdio",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // AI Suggestion Banner
                Surface(
                    onClick = {
                        onAiSuggest("Gere parâmetros vocais, timbre, entonação e texto de apresentação para a voz de '$name' ($gender, estilo $style).")
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "✨ Assistente Vocal RANGA",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                            )
                            Text(
                                text = "Clique para auto-ajustar tom e parâmetros com Inteligência Artificial",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
                            )
                        }
                    }
                }

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome da Voz *") },
                    placeholder = { Text("Ex: António, Narrador, Robô Z...") },
                    modifier = Modifier.fillMaxWidth().testTag("voice_input_name"),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                // Gender chips
                Text("Gênero Vocal:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    genderOptions.forEach { g ->
                        val isSelected = gender.equals(g, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { gender = g },
                            label = { Text(g) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Age Category chips
                Text("Faixa Etária / Tipo:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ageOptions.forEach { age ->
                        val isSelected = ageCategory.equals(age, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                ageCategory = age
                                type = age
                            },
                            label = { Text(age) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleCreative,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Primary Style chips
                Text("Estilo Principal:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    styleOptions.forEach { st ->
                        val isSelected = style.equals(st, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { style = st },
                            label = { Text(st) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Secondary Style chips
                Text("Tag de Tom / Humor:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    secondaryStyleOptions.forEach { st2 ->
                        val isSelected = styleTag2?.equals(st2, ignoreCase = true) == true
                        FilterChip(
                            selected = isSelected,
                            onClick = { styleTag2 = st2 },
                            label = { Text(st2) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanAccent,
                                selectedLabelColor = NavyDark
                            )
                        )
                    }
                }

                // Character Association
                Text("Vincular ao Personagem:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val noneSelected = selectedCharacterName.isBlank()
                    FilterChip(
                        selected = noneSelected,
                        onClick = {
                            selectedCharacterName = ""
                            selectedCharacterEmoji = ""
                            selectedCharacterId = null
                        },
                        label = { Text("Nenhum (Voz Geral)") }
                    )
                    characters.forEach { char ->
                        val isSel = selectedCharacterName.equals(char.name, ignoreCase = true)
                        val emoji = when (char.name.lowercase()) {
                            "antónio", "antonio" -> "🍎"
                            "bia" -> "🍌"
                            "carlos" -> "🥭"
                            "lima" -> "🍈"
                            "dona fruta" -> "🍓"
                            "mimi" -> "🐱"
                            "lucas" -> "👦"
                            "sofia" -> "👧"
                            else -> "👤"
                        }
                        FilterChip(
                            selected = isSel,
                            onClick = {
                                selectedCharacterName = char.name
                                selectedCharacterEmoji = emoji
                                selectedCharacterId = char.id
                                statusTag = "Em uso"
                            },
                            label = { Text("$emoji ${char.name}") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldSuccess,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                // Tuning Sliders
                Text("Parâmetros Vocais:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Velocidade", style = MaterialTheme.typography.labelSmall)
                            Text(String.format("%.1fx", speed), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                        Slider(
                            value = speed,
                            onValueChange = { speed = it },
                            valueRange = 0.5f..2.0f,
                            colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Tom (Pitch)", style = MaterialTheme.typography.labelSmall)
                            Text(String.format("%.1f", pitch), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                        Slider(
                            value = pitch,
                            onValueChange = { pitch = it },
                            valueRange = -5.0f..5.0f,
                            colors = SliderDefaults.colors(thumbColor = PurpleCreative, activeTrackColor = PurpleCreative)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Expressividade", style = MaterialTheme.typography.labelSmall)
                            Text("${expressiveness.toInt()}%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall)
                        }
                        Slider(
                            value = expressiveness,
                            onValueChange = { expressiveness = it },
                            valueRange = 0f..100f,
                            colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent)
                        )
                    }
                }

                // Sample Text for Test
                OutlinedTextField(
                    value = sampleText,
                    onValueChange = { sampleText = it },
                    label = { Text("Texto de Teste / Frase Padrão") },
                    placeholder = { Text("Digite a frase que a voz usará para testar a modulação...") },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    shape = RoundedCornerShape(10.dp)
                )

                // Language
                Text("Idioma:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    languageOptions.forEach { lang ->
                        val isSelected = language.equals(lang, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { language = lang },
                            label = { Text(lang) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val voice = (editingVoice ?: VoiceEntity(
                            name = name,
                            type = type,
                            tone = tone,
                            language = language,
                            gender = gender,
                            ageCategory = ageCategory,
                            style = style,
                            styleTag2 = styleTag2,
                            sampleAudioDesc = sampleAudioDesc,
                            sampleText = sampleText,
                            assignedCharacter = if (selectedCharacterName.isNotBlank()) selectedCharacterName else null,
                            characterEmoji = if (selectedCharacterEmoji.isNotBlank()) selectedCharacterEmoji else null,
                            characterId = selectedCharacterId,
                            projectId = selectedProjId,
                            projectName = selectedProjName,
                            statusTag = statusTag,
                            speed = speed,
                            pitch = pitch,
                            expressiveness = expressiveness,
                            approximateAge = approximateAge
                        )).copy(
                            name = name,
                            type = type,
                            tone = tone,
                            language = language,
                            gender = gender,
                            ageCategory = ageCategory,
                            style = style,
                            styleTag2 = styleTag2,
                            sampleAudioDesc = sampleAudioDesc,
                            sampleText = sampleText,
                            assignedCharacter = if (selectedCharacterName.isNotBlank()) selectedCharacterName else null,
                            characterEmoji = if (selectedCharacterEmoji.isNotBlank()) selectedCharacterEmoji else null,
                            characterId = selectedCharacterId,
                            projectId = selectedProjId,
                            projectName = selectedProjName,
                            statusTag = statusTag,
                            speed = speed,
                            pitch = pitch,
                            expressiveness = expressiveness,
                            approximateAge = approximateAge,
                            updatedAt = System.currentTimeMillis()
                        )
                        onSave(voice)
                    }
                },
                enabled = name.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text(if (editingVoice != null) "Salvar Alterações" else "Criar Voz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun VoiceAiGeneratorDialog(
    onDismiss: () -> Unit,
    onVoiceGenerated: (VoiceEntity) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit
) {
    var promptDescription by remember { mutableStateOf("") }
    var selectedPreset by remember { mutableStateOf("Herói Jovem") }
    var isGenerating by remember { mutableStateOf(false) }

    val presets = listOf("Herói Jovem", "Criança Divertida", "Vilão Sarcástico", "Mentora Gentil", "Narrador Épico", "Robô Futurista", "Criatura Mágica")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Criar Voz com IA Studio", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Descreva as características da voz que deseja criar. A Inteligência Artificial ajustará timbre, idade, ressonância e emoções ideais.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                Text("Arquétipos Prontos:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presets.forEach { p ->
                        FilterChip(
                            selected = selectedPreset == p,
                            onClick = {
                                selectedPreset = p
                                promptDescription = when (p) {
                                    "Herói Jovem" -> "Voz masculina de 16 a 20 anos, entusiasmada, cheia de coragem e tom de aventura de animação."
                                    "Criança Divertida" -> "Voz infantil alegre de 8 anos, veloz, risonha e expressiva para comédias de desenho."
                                    "Vilão Sarcástico" -> "Voz imponente, grave, com gargalhada teatral e inflexões irônicas."
                                    "Mentora Gentil" -> "Voz doce e calorosa de 40 anos, serena e encorajadora."
                                    "Narrador Épico" -> "Voz profunda de rádio e cinema com dicção impecável para introduções dramáticas."
                                    "Robô Futurista" -> "Voz digital modulada com pequenas reverberações e entonação analítica."
                                    else -> "Voz sussurrada, etérea e misteriosa de ser mítico."
                                }
                            },
                            label = { Text(p) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleCreative,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = promptDescription,
                    onValueChange = { promptDescription = it },
                    label = { Text("Instruções de Personalização") },
                    placeholder = { Text("Ex: Voz feminina jovem, alegre, com sotaque carismático e tom brincalhão...") },
                    modifier = Modifier.fillMaxWidth().height(110.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val name = when (selectedPreset) {
                        "Herói Jovem" -> "Voz Heróica"
                        "Criança Divertida" -> "Voz Travessa"
                        "Vilão Sarcástico" -> "Lorde Sombra"
                        "Mentora Gentil" -> "Guia Serena"
                        "Narrador Épico" -> "Voz das Crônicas"
                        "Robô Futurista" -> "Sintetizador 3000"
                        else -> "Voz Mágica"
                    }
                    val gender = if (selectedPreset in listOf("Mentora Gentil", "Voz Mágica")) "Feminina" else "Masculina"
                    val style = if (selectedPreset == "Narrador Épico" || selectedPreset == "Mentora Gentil") "Natural" else "Cartoon"

                    val voice = VoiceEntity(
                        name = name,
                        type = selectedPreset,
                        tone = "Criada por IA",
                        language = "Português (Brasil)",
                        sampleAudioDesc = promptDescription.ifBlank { "Voz otimizada por inteligência artificial com equilíbrio harmônico." },
                        sampleText = "Esta é uma demonstração da voz gerada por IA no estúdio RANGA!",
                        gender = gender,
                        style = style,
                        styleTag2 = "Aventura",
                        statusTag = "Salva",
                        speed = 1.0f,
                        pitch = if (gender == "Feminina") 1.2f else -0.5f,
                        expressiveness = 80f
                    )
                    onVoiceGenerated(voice)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Gerar Voz com IA")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun VoiceAssociateCharacterDialog(
    voice: VoiceEntity,
    characters: List<CharacterEntity>,
    onDismiss: () -> Unit,
    onAssociate: (String, String?, Long?) -> Unit
) {
    var selectedName by remember { mutableStateOf(voice.assignedCharacter ?: "") }
    var selectedEmoji by remember { mutableStateOf(voice.characterEmoji ?: "👤") }
    var selectedId by remember { mutableStateOf(voice.characterId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Associar Voz a Personagem", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Selecione o personagem que utilizará a voz '${voice.name}' nas falas e roteiros do estúdio:",
                    style = MaterialTheme.typography.bodySmall
                )

                characters.forEach { char ->
                    val emoji = when (char.name.lowercase()) {
                        "antónio", "antonio" -> "🍎"
                        "bia" -> "🍌"
                        "carlos" -> "🥭"
                        "lima" -> "🍈"
                        "dona fruta" -> "🍓"
                        "mimi" -> "🐱"
                        "lucas" -> "👦"
                        "sofia" -> "👧"
                        else -> "👤"
                    }
                    val isSelected = selectedName.equals(char.name, ignoreCase = true)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedName = char.name
                                selectedEmoji = emoji
                                selectedId = char.id
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) NavyPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(emoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(char.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                                    Text(char.role, style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                                }
                            }
                            if (isSelected) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = NavyPrimary)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onAssociate(selectedName, selectedEmoji, selectedId)
                },
                enabled = selectedName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Confirmar Associação")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun VoiceImportAudioDialog(
    onDismiss: () -> Unit,
    onImportSuccess: (VoiceEntity) -> Unit
) {
    var voiceName by remember { mutableStateOf("") }
    var fileName by remember { mutableStateOf("amostra_locucao_antonio.wav") }
    var isSimulatedLoaded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, tint = NavyPrimary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Importar Arquivo de Áudio", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Faça upload de uma amostra de locução (.wav, .mp3, .ogg) para clonar ou indexar o perfil sonoro no estúdio.",
                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                )

                OutlinedTextField(
                    value = voiceName,
                    onValueChange = { voiceName = it },
                    label = { Text("Nome da Voz *") },
                    placeholder = { Text("Ex: Dublagem Externa António") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, modifier = Modifier.size(36.dp), tint = NavyPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(fileName, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                        Text("Tamanho: 1.4 MB • Duração: 00:15 • 44.1kHz WAV", style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalName = voiceName.ifBlank { "Voz Importada" }
                    val v = VoiceEntity(
                        name = finalName,
                        type = "Personalizada",
                        tone = "Importada",
                        sampleAudioDesc = "Áudio importado via arquivo externo: $fileName",
                        sampleText = "Áudio sincronizado com o banco de timbres do estúdio.",
                        statusTag = "Salva",
                        style = "Natural",
                        styleTag2 = "Aventura",
                        gender = "Masculina"
                    )
                    onImportSuccess(v)
                },
                enabled = voiceName.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Importar e Indexar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
