package com.example.ui.screens

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.VoiceEntity
import com.example.ui.navigation.StudioDestination
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyLight
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PurpleCreative
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoicesScreen(
    voices: List<VoiceEntity>,
    characters: List<CharacterEntity> = emptyList(),
    projects: List<ProjectEntity> = emptyList(),
    series: List<SeriesEntity> = emptyList(),
    seasons: List<SeasonEntity> = emptyList(),
    recentActivities: List<ActivityLogEntity> = emptyList(),
    selectedProjectId: Long? = null,
    activeVoice: VoiceEntity? = null,
    showNewVoiceDialog: Boolean = false,
    editingVoice: VoiceEntity? = null,
    onSetActiveVoice: (VoiceEntity?) -> Unit = {},
    onOpenNewVoice: (VoiceEntity?) -> Unit = {},
    onCloseNewVoice: () -> Unit = {},
    onSaveVoice: (VoiceEntity, Boolean) -> Unit = { _, _ -> },
    onDeleteVoice: (VoiceEntity) -> Unit = {},
    onDuplicateVoice: (VoiceEntity) -> Unit = {},
    onToggleFavoriteVoice: (VoiceEntity) -> Unit = {},
    onArchiveVoice: (VoiceEntity) -> Unit = {},
    onAssociateCharacter: (VoiceEntity, String, String?, Long?) -> Unit = { _, _, _, _ -> },
    onAssociateProject: (VoiceEntity, Long, String) -> Unit = { _, _, _ -> },
    onUpdateVoiceTuning: (Long, Float, Float, Float, String) -> Unit = { _, _, _, _, _ -> },
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToDestination: (StudioDestination) -> Unit = {}
) {
    val context = LocalContext.current
    var ttsEngine by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsReady by remember { mutableStateOf(false) }
    var currentlyPlayingVoiceId by remember { mutableStateOf<Long?>(null) }

    // Initialize TTS
    DisposableEffect(context) {
        val tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isTtsReady = true
                ttsEngine?.language = Locale("pt", "BR")
            }
        }
        ttsEngine = tts

        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    fun speakVoice(voice: VoiceEntity, customText: String? = null) {
        val tts = ttsEngine ?: return
        if (currentlyPlayingVoiceId == voice.id) {
            tts.stop()
            currentlyPlayingVoiceId = null
            return
        }
        tts.stop()
        val speechRate = voice.speed.coerceIn(0.5f, 2.0f)
        val pitchMultiplier = when {
            voice.pitch > 0 -> 1.0f + (voice.pitch * 0.25f)
            voice.pitch < 0 -> 1.0f + (voice.pitch * 0.15f)
            else -> 1.0f
        }.coerceIn(0.5f, 2.0f)

        tts.setSpeechRate(speechRate)
        tts.setPitch(pitchMultiplier)

        val textToSay = customText?.ifBlank { null } ?: voice.sampleText.ifBlank {
            "Olá! Eu sou ${voice.name}, com perfil vocal ${voice.style} para as produções do estúdio RANGA!"
        }

        currentlyPlayingVoiceId = voice.id
        tts.speak(textToSay, TextToSpeech.QUEUE_FLUSH, null, "voice_${voice.id}")
    }

    fun stopSpeaking() {
        ttsEngine?.stop()
        currentlyPlayingVoiceId = null
    }

    // State & Filters
    var searchQuery by remember { mutableStateOf("") }
    var primaryTab by remember { mutableStateOf("Todas") } // Todas, Minhas vozes, Vozes salvas, Vozes dos personagens, Recentes, Favoritas
    var selectedGenderFilter by remember { mutableStateOf("Todas") }
    var selectedStyleFilter by remember { mutableStateOf("Todos") }
    var isGridView by remember { mutableStateOf(true) }
    var sortBy by remember { mutableStateOf("Mais recentes") }

    // Dialogs state
    var showAiVoiceDialog by remember { mutableStateOf(false) }
    var showImportAudioDialog by remember { mutableStateOf(false) }
    var voiceToAssociate by remember { mutableStateOf<VoiceEntity?>(null) }
    var voiceToDelete by remember { mutableStateOf<VoiceEntity?>(null) }

    // Currently selected voice for right panel detail
    val selectedVoice = activeVoice ?: voices.firstOrNull()

    // Filter computation
    val filteredVoices = voices.filter { v ->
        val matchesSearch = searchQuery.isBlank() ||
                v.name.contains(searchQuery, ignoreCase = true) ||
                v.type.contains(searchQuery, ignoreCase = true) ||
                v.tone.contains(searchQuery, ignoreCase = true) ||
                v.style.contains(searchQuery, ignoreCase = true) ||
                (v.styleTag2?.contains(searchQuery, ignoreCase = true) == true) ||
                (v.assignedCharacter?.contains(searchQuery, ignoreCase = true) == true) ||
                (v.projectName?.contains(searchQuery, ignoreCase = true) == true)

        val matchesTab = when (primaryTab) {
            "Minhas vozes" -> v.statusTag == "Salva" || v.statusTag == "Em uso"
            "Vozes salvas" -> v.statusTag == "Salva"
            "Vozes dos personagens" -> !v.assignedCharacter.isNullOrBlank()
            "Recentes" -> true
            "Favoritas" -> v.isFavorite
            else -> true
        }

        val matchesGender = when (selectedGenderFilter) {
            "Todas" -> true
            "Masculina" -> v.gender.equals("Masculina", ignoreCase = true)
            "Feminina" -> v.gender.equals("Feminina", ignoreCase = true)
            "Infantil" -> v.gender.equals("Infantil", ignoreCase = true) || v.ageCategory.equals("Criança", ignoreCase = true)
            "Jovem" -> v.ageCategory.equals("Jovem", ignoreCase = true)
            "Adulta" -> v.ageCategory.equals("Adulta", ignoreCase = true)
            "Idosa" -> v.ageCategory.equals("Idosa", ignoreCase = true)
            else -> true
        }

        val matchesStyle = when (selectedStyleFilter) {
            "Todos" -> true
            "Cartoon" -> v.style.contains("Cartoon", ignoreCase = true) || v.styleTag2?.contains("Cartoon", ignoreCase = true) == true
            "Natural" -> v.style.contains("Natural", ignoreCase = true) || v.styleTag2?.contains("Natural", ignoreCase = true) == true
            "Dramática" -> v.style.contains("Dramática", ignoreCase = true) || v.tone.contains("Dramática", ignoreCase = true)
            "Comédia" -> v.style.contains("Comédia", ignoreCase = true) || v.styleTag2?.contains("Divertida", ignoreCase = true) == true
            "Fantasia" -> v.style.contains("Fantasia", ignoreCase = true) || v.type.contains("Mágica", ignoreCase = true)
            "Futurista" -> v.style.contains("Futurista", ignoreCase = true) || v.styleTag2?.contains("Robô", ignoreCase = true) == true
            "Vilão" -> v.style.contains("Vilão", ignoreCase = true) || v.styleTag2?.contains("Sombria", ignoreCase = true) == true
            else -> true
        }

        matchesSearch && matchesTab && matchesGender && matchesStyle
    }.let { list ->
        when (sortBy) {
            "Nome A–Z" -> list.sortedBy { it.name }
            "Mais antigas" -> list.sortedBy { it.createdAt }
            "Favoritas" -> list.sortedByDescending { it.isFavorite }
            "Mais utilizadas" -> list.sortedByDescending { it.assignedCharacter != null }
            else -> list.sortedByDescending { it.updatedAt }
        }
    }

    val totalVoicesCount = voices.size
    val favoritesCount = voices.count { it.isFavorite }
    val characterVoicesCount = voices.count { !it.assignedCharacter.isNullOrBlank() }
    val inUseCount = voices.count { it.statusTag == "Em uso" }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFF6F8FC))) {
        val isWideScreen = maxWidth > 840.dp

        if (isWideScreen) {
            // Adaptive 2-column layout
            Row(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Left main list column
                Column(modifier = Modifier.weight(1.5f).fillMaxHeight()) {
                    VoiceTopHeaderAndContextBar(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        projects = projects,
                        series = series,
                        seasons = seasons,
                        onImportAudioClick = { showImportAudioDialog = true },
                        onAiCreateClick = { showAiVoiceDialog = true },
                        onNewVoiceClick = { onOpenNewVoice(null) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VoiceFilterPillSection(
                        primaryTab = primaryTab,
                        onTabSelect = { primaryTab = it },
                        totalCount = totalVoicesCount,
                        myVoicesCount = voices.count { it.statusTag != "Arquivada" },
                        savedCount = voices.count { it.statusTag == "Salva" },
                        characterVoicesCount = characterVoicesCount,
                        favoritesCount = favoritesCount,
                        selectedGender = selectedGenderFilter,
                        onGenderSelect = { selectedGenderFilter = it },
                        selectedStyle = selectedStyleFilter,
                        onStyleSelect = { selectedStyleFilter = it },
                        isGridView = isGridView,
                        onToggleGridView = { isGridView = it },
                        sortBy = sortBy,
                        onSortChange = { sortBy = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VoiceCardsContent(
                        voices = filteredVoices,
                        selectedVoiceId = selectedVoice?.id,
                        currentlyPlayingVoiceId = currentlyPlayingVoiceId,
                        isGridView = isGridView,
                        onSelectVoice = { onSetActiveVoice(it) },
                        onPlayVoice = { speakVoice(it) },
                        onToggleFavorite = { onToggleFavoriteVoice(it) },
                        onEdit = { onOpenNewVoice(it) },
                        onDuplicate = { onDuplicateVoice(it) },
                        onAssociateCharacter = { voiceToAssociate = it },
                        onArchive = { onArchiveVoice(it) },
                        onDelete = { voiceToDelete = it },
                        onNewVoiceClick = { onOpenNewVoice(null) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    VoiceBottomStatsSection(
                        totalCount = totalVoicesCount,
                        favoritesCount = favoritesCount,
                        characterCount = characterVoicesCount,
                        inUseCount = inUseCount,
                        recentActivities = recentActivities
                    )
                }

                // Right Panel Column (Tuning Studio, Voice Details & AI Assistant)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (selectedVoice != null) {
                        VoiceAudioTesterCard(
                            voice = selectedVoice,
                            isPlaying = currentlyPlayingVoiceId == selectedVoice.id,
                            onPlay = { customText -> speakVoice(selectedVoice, customText) },
                            onStop = { stopSpeaking() },
                            onTuningChange = { speed, pitch, exp, sample ->
                                onUpdateVoiceTuning(selectedVoice.id, speed, pitch, exp, sample)
                            }
                        )

                        VoiceDetailsCard(
                            voice = selectedVoice,
                            onEdit = { onOpenNewVoice(selectedVoice) },
                            onAssociate = { voiceToAssociate = selectedVoice }
                        )
                    }

                    VoiceAiAssistantPanel(
                        selectedVoice = selectedVoice,
                        onOpenAiWithPrompt = onOpenAiWithPrompt
                    )
                }
            }
        } else {
            // Single column vertical layout for compact devices
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 84.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    VoiceTopHeaderAndContextBar(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        projects = projects,
                        series = series,
                        seasons = seasons,
                        onImportAudioClick = { showImportAudioDialog = true },
                        onAiCreateClick = { showAiVoiceDialog = true },
                        onNewVoiceClick = { onOpenNewVoice(null) }
                    )
                }

                item {
                    VoiceFilterPillSection(
                        primaryTab = primaryTab,
                        onTabSelect = { primaryTab = it },
                        totalCount = totalVoicesCount,
                        myVoicesCount = voices.count { it.statusTag != "Arquivada" },
                        savedCount = voices.count { it.statusTag == "Salva" },
                        characterVoicesCount = characterVoicesCount,
                        favoritesCount = favoritesCount,
                        selectedGender = selectedGenderFilter,
                        onGenderSelect = { selectedGenderFilter = it },
                        selectedStyle = selectedStyleFilter,
                        onStyleSelect = { selectedStyleFilter = it },
                        isGridView = isGridView,
                        onToggleGridView = { isGridView = it },
                        sortBy = sortBy,
                        onSortChange = { sortBy = it }
                    )
                }

                // Voice Cards list
                if (filteredVoices.isEmpty()) {
                    item {
                        EmptyVoicesPlaceholder(onNewVoice = { onOpenNewVoice(null) })
                    }
                } else {
                    items(filteredVoices, key = { it.id }) { voice ->
                        VoiceCardItem(
                            voice = voice,
                            isSelected = selectedVoice?.id == voice.id,
                            isPlaying = currentlyPlayingVoiceId == voice.id,
                            onSelect = { onSetActiveVoice(voice) },
                            onPlay = { speakVoice(voice) },
                            onToggleFavorite = { onToggleFavoriteVoice(voice) },
                            onEdit = { onOpenNewVoice(voice) },
                            onDuplicate = { onDuplicateVoice(voice) },
                            onAssociateCharacter = { voiceToAssociate = voice },
                            onArchive = { onArchiveVoice(voice) },
                            onDelete = { voiceToDelete = voice }
                        )
                    }
                }

                if (selectedVoice != null) {
                    item {
                        Text(
                            text = "🎧 Estúdio de Modulação & Teste",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                        )
                    }

                    item {
                        VoiceAudioTesterCard(
                            voice = selectedVoice,
                            isPlaying = currentlyPlayingVoiceId == selectedVoice.id,
                            onPlay = { customText -> speakVoice(selectedVoice, customText) },
                            onStop = { stopSpeaking() },
                            onTuningChange = { speed, pitch, exp, sample ->
                                onUpdateVoiceTuning(selectedVoice.id, speed, pitch, exp, sample)
                            }
                        )
                    }

                    item {
                        VoiceDetailsCard(
                            voice = selectedVoice,
                            onEdit = { onOpenNewVoice(selectedVoice) },
                            onAssociate = { voiceToAssociate = selectedVoice }
                        )
                    }
                }

                item {
                    VoiceAiAssistantPanel(
                        selectedVoice = selectedVoice,
                        onOpenAiWithPrompt = onOpenAiWithPrompt
                    )
                }

                item {
                    VoiceBottomStatsSection(
                        totalCount = totalVoicesCount,
                        favoritesCount = favoritesCount,
                        characterCount = characterVoicesCount,
                        inUseCount = inUseCount,
                        recentActivities = recentActivities
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { onOpenNewVoice(null) },
            containerColor = NavyPrimary,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .testTag("fab_new_voice")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Nova Voz")
        }
    }

    // Dialogs
    if (showNewVoiceDialog) {
        VoiceFormDialog(
            editingVoice = editingVoice,
            characters = characters,
            projects = projects,
            selectedProjectId = selectedProjectId,
            onDismiss = onCloseNewVoice,
            onSave = { voice ->
                onSaveVoice(voice, true)
            },
            onAiSuggest = { prompt ->
                onCloseNewVoice()
                onOpenAiWithPrompt(prompt)
            }
        )
    }

    if (showAiVoiceDialog) {
        VoiceAiGeneratorDialog(
            onDismiss = { showAiVoiceDialog = false },
            onVoiceGenerated = { voice ->
                showAiVoiceDialog = false
                onSaveVoice(voice, true)
            },
            onOpenAiWithPrompt = { prompt ->
                showAiVoiceDialog = false
                onOpenAiWithPrompt(prompt)
            }
        )
    }

    if (showImportAudioDialog) {
        VoiceImportAudioDialog(
            onDismiss = { showImportAudioDialog = false },
            onImportSuccess = { voice ->
                showImportAudioDialog = false
                onSaveVoice(voice, true)
            }
        )
    }

    if (voiceToAssociate != null) {
        VoiceAssociateCharacterDialog(
            voice = voiceToAssociate!!,
            characters = characters,
            onDismiss = { voiceToAssociate = null },
            onAssociate = { charName, emoji, charId ->
                onAssociateCharacter(voiceToAssociate!!, charName, emoji, charId)
                voiceToAssociate = null
            }
        )
    }

    if (voiceToDelete != null) {
        AlertDialog(
            onDismissRequest = { voiceToDelete = null },
            title = { Text("Excluir Voz do Estúdio") },
            text = { Text("Deseja realmente remover a voz '${voiceToDelete?.name}' da biblioteca de vozes? Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(
                    onClick = {
                        voiceToDelete?.let { onDeleteVoice(it) }
                        voiceToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { voiceToDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

// -------------------------------------------------------------------------------------------------
// Subcomponents
// -------------------------------------------------------------------------------------------------

@Composable
fun VoiceTopHeaderAndContextBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    projects: List<ProjectEntity>,
    series: List<SeriesEntity>,
    seasons: List<SeasonEntity>,
    onImportAudioClick: () -> Unit,
    onAiCreateClick: () -> Unit,
    onNewVoiceClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎙️ Vozes",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyPrimary
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Surface(
                        color = NavyPrimary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Módulo Vocal",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                        )
                    }
                }
                Text(
                    text = "Crie e organize as vozes que darão vida aos seus personagens.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // User pill / Status icons
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(onClick = { /* Help */ }, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.HelpOutline, contentDescription = "Ajuda", tint = NavyPrimary)
                }
                IconButton(onClick = { /* Notif */ }, modifier = Modifier.size(36.dp)) {
                    Icon(imageVector = Icons.Default.NotificationsNone, contentDescription = "Notificações", tint = NavyPrimary)
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Brush.linearGradient(listOf(CyanAccent, NavyPrimary))),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Augusto", fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = NavyDark)
                    }
                }
            }
        }

        // Search Bar & Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Pesquisar vozes, personagens, estilos...", fontSize = 13.sp) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NavyPrimary) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Limpar")
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("voice_search_input"),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NavyPrimary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )

            // Import Audio Button
            OutlinedButton(
                onClick = onImportAudioClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
            ) {
                Icon(imageVector = Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Importar", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            // AI Voice Generation Button
            OutlinedButton(
                onClick = onAiCreateClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Criar com IA", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            // New Voice Primary CTA
            Button(
                onClick = onNewVoiceClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                modifier = Modifier.testTag("btn_new_voice")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("+ Nova voz", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VoiceFilterPillSection(
    primaryTab: String,
    onTabSelect: (String) -> Unit,
    totalCount: Int,
    myVoicesCount: Int,
    savedCount: Int,
    characterVoicesCount: Int,
    favoritesCount: Int,
    selectedGender: String,
    onGenderSelect: (String) -> Unit,
    selectedStyle: String,
    onStyleSelect: (String) -> Unit,
    isGridView: Boolean,
    onToggleGridView: (Boolean) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Primary Category Tabs
        val tabs = listOf(
            "Todas" to totalCount,
            "Minhas vozes" to myVoicesCount,
            "Vozes salvas" to savedCount,
            "Vozes dos personagens" to characterVoicesCount,
            "Recentes" to 4,
            "Favoritas" to favoritesCount
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEach { (title, count) ->
                val isSelected = primaryTab.equals(title, ignoreCase = true)
                Surface(
                    onClick = { onTabSelect(title) },
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) NavyPrimary else Color.White,
                    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)) else null,
                    shadowElevation = if (isSelected) 2.dp else 0.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = if (isSelected) Color.White else NavyDark
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFEDF2F7))
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "$count",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF64748B)
                            )
                        }
                    }
                }
            }
        }

        // Secondary Filters: Gender & Style + Grid Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Gênero da voz:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                )

                val genderFilters = listOf("Todas", "Masculina", "Feminina", "Infantil", "Jovem", "Adulta", "Idosa")
                genderFilters.forEach { g ->
                    val isSel = selectedGender.equals(g, ignoreCase = true)
                    FilterChip(
                        selected = isSel,
                        onClick = { onGenderSelect(g) },
                        label = { Text(g, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary.copy(alpha = 0.12f),
                            selectedLabelColor = NavyPrimary
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Estilo:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                )

                val styleFilters = listOf("Todos", "Cartoon", "Natural", "Dramática", "Comédia", "Fantasia", "Futurista", "Vilão")
                styleFilters.forEach { st ->
                    val isSel = selectedStyle.equals(st, ignoreCase = true)
                    FilterChip(
                        selected = isSel,
                        onClick = { onStyleSelect(st) },
                        label = { Text(st, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PurpleCreative.copy(alpha = 0.12f),
                            selectedLabelColor = PurpleCreative
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Grid / List toggles & Sort
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onToggleGridView(true) }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Grid",
                        tint = if (isGridView) NavyPrimary else Color(0xFF94A3B8)
                    )
                }
                IconButton(onClick = { onToggleGridView(false) }, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Default.ViewList,
                        contentDescription = "Lista",
                        tint = if (!isGridView) NavyPrimary else Color(0xFF94A3B8)
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceCardsContent(
    voices: List<VoiceEntity>,
    selectedVoiceId: Long?,
    currentlyPlayingVoiceId: Long?,
    isGridView: Boolean,
    onSelectVoice: (VoiceEntity) -> Unit,
    onPlayVoice: (VoiceEntity) -> Unit,
    onToggleFavorite: (VoiceEntity) -> Unit,
    onEdit: (VoiceEntity) -> Unit,
    onDuplicate: (VoiceEntity) -> Unit,
    onAssociateCharacter: (VoiceEntity) -> Unit,
    onArchive: (VoiceEntity) -> Unit,
    onDelete: (VoiceEntity) -> Unit,
    onNewVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (voices.isEmpty()) {
        EmptyVoicesPlaceholder(onNewVoice = onNewVoiceClick)
    } else {
        if (isGridView) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 270.dp),
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(voices, key = { it.id }) { voice ->
                    VoiceCardItem(
                        voice = voice,
                        isSelected = selectedVoiceId == voice.id,
                        isPlaying = currentlyPlayingVoiceId == voice.id,
                        onSelect = { onSelectVoice(voice) },
                        onPlay = { onPlayVoice(voice) },
                        onToggleFavorite = { onToggleFavorite(voice) },
                        onEdit = { onEdit(voice) },
                        onDuplicate = { onDuplicate(voice) },
                        onAssociateCharacter = { onAssociateCharacter(voice) },
                        onArchive = { onArchive(voice) },
                        onDelete = { onDelete(voice) }
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 12.dp)
            ) {
                items(voices, key = { it.id }) { voice ->
                    VoiceCardItem(
                        voice = voice,
                        isSelected = selectedVoiceId == voice.id,
                        isPlaying = currentlyPlayingVoiceId == voice.id,
                        onSelect = { onSelectVoice(voice) },
                        onPlay = { onPlayVoice(voice) },
                        onToggleFavorite = { onToggleFavorite(voice) },
                        onEdit = { onEdit(voice) },
                        onDuplicate = { onDuplicate(voice) },
                        onAssociateCharacter = { onAssociateCharacter(voice) },
                        onArchive = { onArchive(voice) },
                        onDelete = { onDelete(voice) }
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceCardItem(
    voice: VoiceEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onAssociateCharacter: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    val statusBgColor = when (voice.statusTag.lowercase()) {
        "em uso" -> EmeraldSuccess.copy(alpha = 0.15f)
        "rascunho" -> AmberGold.copy(alpha = 0.15f)
        "arquivada" -> Color(0xFF64748B).copy(alpha = 0.15f)
        else -> NavyPrimary.copy(alpha = 0.12f)
    }
    val statusTextColor = when (voice.statusTag.lowercase()) {
        "em uso" -> EmeraldSuccess
        "rascunho" -> AmberGold
        "arquivada" -> Color(0xFF64748B)
        else -> NavyPrimary
    }

    val avatarEmoji = when (voice.name.lowercase()) {
        "antónio", "antonio" -> "🍎"
        "bia" -> "🍌"
        "carlos" -> "🥭"
        "lima" -> "🍈"
        "dona fruta" -> "🍓"
        "mimi" -> "🐱"
        "lucas" -> "👦"
        "sofia" -> "👧"
        "robô r7", "robô z", "sintetizador 3000" -> "🤖"
        "narrador épico", "narrador" -> "🎙️"
        "lorde sombra", "vilão sombra" -> "🦹"
        "guia serena" -> "🧚"
        else -> "🎙️"
    }

    val gradientBrush = when (voice.style.lowercase()) {
        "cartoon" -> Brush.linearGradient(listOf(Color(0xFFFF8A00), Color(0xFFFF5252)))
        "dramática" -> Brush.linearGradient(listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)))
        "futurista" -> Brush.linearGradient(listOf(Color(0xFF00C6FF), Color(0xFF0072FF)))
        "vilão" -> Brush.linearGradient(listOf(Color(0xFF2C3E50), Color(0xFF000000)))
        else -> Brush.linearGradient(listOf(NavyLight, NavyDeep))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("voice_item_${voice.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F5FF) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) NavyPrimary else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Avatar + Name + Status Tag + Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(gradientBrush),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(avatarEmoji, fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = voice.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyDark),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = statusBgColor
                            ) {
                                Text(
                                    text = voice.statusTag,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = statusTextColor, fontSize = 10.sp)
                                )
                            }
                        }
                        Text(
                            text = "${voice.gender} • ${voice.ageCategory}",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 12.sp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(32.dp)) {
                        Icon(
                            imageVector = if (voice.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favoritar",
                            tint = if (voice.isFavorite) AmberGold else Color(0xFF94A3B8)
                        )
                    }

                    Box {
                        IconButton(onClick = { showMenu = true }, modifier = Modifier.size(32.dp)) {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color(0xFF64748B))
                        }
                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                text = { Text("✏️ Editar voz") },
                                onClick = {
                                    showMenu = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("📋 Duplicar voz") },
                                onClick = {
                                    showMenu = false
                                    onDuplicate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("👤 Associar a personagem") },
                                onClick = {
                                    showMenu = false
                                    onAssociateCharacter()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (voice.isFavorite) "⭐ Desfavoritar" else "⭐ Favoritar") },
                                onClick = {
                                    showMenu = false
                                    onToggleFavorite()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (voice.isArchived) "📦 Restaurar" else "📦 Arquivar") },
                                onClick = {
                                    showMenu = false
                                    onArchive()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("🗑️ Excluir voz", color = MaterialTheme.colorScheme.error) },
                                onClick = {
                                    showMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Style Tags Row
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = NavyPrimary.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = voice.style,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = NavyPrimary, fontSize = 11.sp)
                    )
                }

                voice.styleTag2?.let { tag2 ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = CyanAccent.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = tag2,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, color = NavyDeep, fontSize = 11.sp)
                        )
                    }
                }

                if (!voice.language.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "🌐 ${voice.language.take(15)}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF475569), fontSize = 10.sp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Character association indicator
            if (!voice.assignedCharacter.isNullOrBlank()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Personagem: ",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
                    )
                    Text(
                        text = "${voice.characterEmoji ?: "👤"} ${voice.assignedCharacter}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = NavyDark, fontSize = 11.sp)
                    )
                }
            }

            if (!voice.projectName.isNullOrBlank()) {
                Text(
                    text = "Projeto: ${voice.projectName}",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 11.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Audio Player Bar with Animated Visualizer
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isPlaying) NavyPrimary.copy(alpha = 0.08f) else Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isPlaying) NavyPrimary.copy(alpha = 0.3f) else Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        IconButton(
                            onClick = onPlay,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (isPlaying) EmeraldSuccess else NavyPrimary)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Parar áudio" else "Reproduzir amostra",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Waveform equalizer bars
                        SoundWaveformVisualizer(isPlaying = isPlaying)
                    }

                    Text(
                        text = String.format("%.1fx", voice.speed),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                    )
                }
            }
        }
    }
}

@Composable
fun SoundWaveformVisualizer(isPlaying: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")

    val h1 by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = if (isPlaying) 18f else 6f,
        animationSpec = infiniteRepeatable(tween(300, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h1"
    )
    val h2 by infiniteTransition.animateFloat(
        initialValue = 12f,
        targetValue = if (isPlaying) 22f else 8f,
        animationSpec = infiniteRepeatable(tween(450, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h2"
    )
    val h3 by infiniteTransition.animateFloat(
        initialValue = 8f,
        targetValue = if (isPlaying) 20f else 6f,
        animationSpec = infiniteRepeatable(tween(380, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h3"
    )
    val h4 by infiniteTransition.animateFloat(
        initialValue = 14f,
        targetValue = if (isPlaying) 16f else 10f,
        animationSpec = infiniteRepeatable(tween(280, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "h4"
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(24.dp)
    ) {
        listOf(h1, h2, h3, h4, h2, h1, h3).forEach { heightVal ->
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(heightVal.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (isPlaying) NavyPrimary else Color(0xFFCBD5E1))
            )
        }
    }
}

@Composable
fun VoiceAudioTesterCard(
    voice: VoiceEntity,
    isPlaying: Boolean,
    onPlay: (String) -> Unit,
    onStop: () -> Unit,
    onTuningChange: (Float, Float, Float, String) -> Unit
) {
    var sampleText by remember(voice.id) { mutableStateOf(voice.sampleText) }
    var speed by remember(voice.id) { mutableFloatStateOf(voice.speed) }
    var pitch by remember(voice.id) { mutableFloatStateOf(voice.pitch) }
    var expressiveness by remember(voice.id) { mutableFloatStateOf(voice.expressiveness) }
    var volume by remember(voice.id) { mutableFloatStateOf(100f) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.GraphicEq, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🎧 Testar voz",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isPlaying) EmeraldSuccess.copy(alpha = 0.15f) else Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = if (isPlaying) "Sintetizando..." else "Pronto",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isPlaying) EmeraldSuccess else Color(0xFF64748B)
                        )
                    )
                }
            }

            // Big Waveform Preview Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0F172A),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🔊", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Amostra Vocal: ${voice.name}",
                                style = MaterialTheme.typography.labelMedium.copy(color = Color.White, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "${voice.gender} • ${voice.style} • ${voice.language ?: "Português (BR)"}",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 11.sp)
                            )
                        }
                    }

                    SoundWaveformVisualizer(isPlaying = isPlaying)
                }
            }

            // Sample Text Area
            OutlinedTextField(
                value = sampleText,
                onValueChange = {
                    sampleText = it
                    onTuningChange(speed, pitch, expressiveness, it)
                },
                label = { Text("Frase de teste personalizada") },
                placeholder = { Text("Digite o texto que a voz irá ler para testar...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .testTag("voice_test_text_field"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NavyPrimary,
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                )
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Dica: Altere os parâmetros e clique em Testar", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 11.sp))
                Text("${sampleText.length}/300", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 11.sp))
            }

            // Sliders Section
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                // Speed Slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Velocidade", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(String.format("%.1fx", speed), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = NavyPrimary)
                }
                Slider(
                    value = speed,
                    onValueChange = {
                        speed = it
                        onTuningChange(it, pitch, expressiveness, sampleText)
                    },
                    valueRange = 0.5f..2.0f,
                    colors = SliderDefaults.colors(thumbColor = NavyPrimary, activeTrackColor = NavyPrimary)
                )

                // Pitch Slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Tom (Pitch)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text(String.format("%.1f", pitch), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = PurpleCreative)
                }
                Slider(
                    value = pitch,
                    onValueChange = {
                        pitch = it
                        onTuningChange(speed, it, expressiveness, sampleText)
                    },
                    valueRange = -5.0f..5.0f,
                    colors = SliderDefaults.colors(thumbColor = PurpleCreative, activeTrackColor = PurpleCreative)
                )

                // Expressiveness Slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Expressividade", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                    Text("${expressiveness.toInt()}%", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                }
                Slider(
                    value = expressiveness,
                    onValueChange = {
                        expressiveness = it
                        onTuningChange(speed, pitch, it, sampleText)
                    },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(thumbColor = CyanAccent, activeTrackColor = CyanAccent)
                )
            }

            // Play / Stop Test Button
            Button(
                onClick = {
                    if (isPlaying) onStop() else onPlay(sampleText)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_test_audio"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isPlaying) Color(0xFFDC2626) else NavyPrimary)
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isPlaying) "⏹ Parar áudio" else "▶ Testar áudio", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun VoiceDetailsCard(
    voice: VoiceEntity,
    onEdit: () -> Unit,
    onAssociate: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(CyanAccent.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, tint = NavyDeep, modifier = Modifier.size(18.dp))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ℹ️ Detalhes da voz",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                )
            }

            // Voice info sheet
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Brush.linearGradient(listOf(NavyPrimary, CyanAccent))),
                    contentAlignment = Alignment.Center
                ) {
                    Text(voice.characterEmoji ?: "🎙️", fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(voice.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("${voice.gender} • ${voice.ageCategory}", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Metadata key-values
            VoiceDetailRow(icon = "🌐", label = "Idioma", value = voice.language ?: "Português (Brasil)")
            VoiceDetailRow(icon = "🎭", label = "Estilo", value = "${voice.style} ${voice.styleTag2?.let { "• $it" } ?: ""}")
            VoiceDetailRow(icon = "🎂", label = "Idade aproximada", value = voice.approximateAge ?: "15 - 20 anos")
            VoiceDetailRow(icon = "👤", label = "Personagem", value = voice.assignedCharacter?.let { "${voice.characterEmoji ?: ""} $it" } ?: "Nenhum")
            VoiceDetailRow(icon = "👥", label = "Uso na Produção", value = "12 cenas • 4 episódios")

            Spacer(modifier = Modifier.height(6.dp))

            // Quick action buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar voz", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onAssociate,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Associar", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun VoiceDetailRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 12.sp))
        }
        Text(value, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = NavyDark, fontSize = 12.sp))
    }
}

@Composable
fun VoiceAiAssistantPanel(
    selectedVoice: VoiceEntity?,
    onOpenAiWithPrompt: (String) -> Unit
) {
    var aiQuery by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✨ Assistente RANGA",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
                )
            }

            Text(
                text = "Precisa de ajuda para criar ou escolher a voz ideal para seu personagem?",
                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 12.sp)
            )

            // Quick suggestion chips
            val quickChips = listOf(
                "Criar voz de um personagem" to "Ajude-me a criar o perfil vocal ideal para meu personagem principal.",
                "Sugerir estilo de voz" to "Sugira 3 estilos de vozes contrastantes para um trio de personagens cômicos.",
                "Melhorar voz existente" to "Como posso ajustar a velocidade e expressividade da voz '${selectedVoice?.name ?: "atual"}'?",
                "Analisar voz" to "Analise a harmonia vocal entre as vozes já cadastradas no projeto."
            )

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                quickChips.forEach { (label, prompt) ->
                    Surface(
                        onClick = { onOpenAiWithPrompt(prompt) },
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(label, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = NavyDark, fontSize = 12.sp))
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                        }
                    }
                }
            }

            // Input bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = aiQuery,
                    onValueChange = { aiQuery = it },
                    placeholder = { Text("Digite sua solicitação...", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                IconButton(
                    onClick = {
                        if (aiQuery.isNotBlank()) {
                            onOpenAiWithPrompt(aiQuery)
                            aiQuery = ""
                        }
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary)
                ) {
                    Icon(imageVector = Icons.Default.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
fun VoiceBottomStatsSection(
    totalCount: Int,
    favoritesCount: Int,
    characterCount: Int,
    inUseCount: Int,
    recentActivities: List<ActivityLogEntity>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "📊 Métricas Vocais & Uso Recente",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
            )

            // 4 Stat Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                VoiceStatTile(
                    icon = "🎙️",
                    number = "$totalCount",
                    label = "Total de vozes",
                    change = "+3 esta semana",
                    modifier = Modifier.weight(1f)
                )
                VoiceStatTile(
                    icon = "⭐",
                    number = "$favoritesCount",
                    label = "Favoritas",
                    change = "+2 esta semana",
                    modifier = Modifier.weight(1f)
                )
                VoiceStatTile(
                    icon = "👥",
                    number = "$characterCount",
                    label = "Com personagem",
                    change = "+4 esta semana",
                    modifier = Modifier.weight(1f)
                )
                VoiceStatTile(
                    icon = "〰️",
                    number = "$inUseCount",
                    label = "Em uso",
                    change = "+5 esta semana",
                    modifier = Modifier.weight(1f)
                )
            }

            // Recent activity log snippets
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "Atividade Recente no Estúdio Vocal",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                )

                VoiceActivitySnippet(
                    icon = "👤",
                    title = "António foi associada ao personagem António",
                    time = "Há 2 horas"
                )
                VoiceActivitySnippet(
                    icon = "🎬",
                    title = "Bia foi usada na cena 3 - A investigação",
                    time = "Há 4 horas"
                )
                VoiceActivitySnippet(
                    icon = "🎙️",
                    title = "Nova voz criada: Robô R7",
                    time = "Há 1 dia"
                )
            }
        }
    }
}

@Composable
fun VoiceStatTile(
    icon: String,
    number: String,
    label: String,
    change: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(icon, fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(number, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = NavyDark)
            }
            Text(label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 10.sp), maxLines = 1)
            Text(change, style = MaterialTheme.typography.bodySmall.copy(color = EmeraldSuccess, fontSize = 9.sp, fontWeight = FontWeight.Bold))
        }
    }
}

@Composable
fun VoiceActivitySnippet(icon: String, title: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Text(icon, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(title, style = MaterialTheme.typography.bodySmall.copy(color = NavyDark, fontSize = 11.sp), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Text(time, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp))
    }
}

@Composable
fun EmptyVoicesPlaceholder(onNewVoice: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(NavyPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(32.dp))
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Nenhuma voz encontrada",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NavyPrimary)
            )
            Text(
                text = "Tente ajustar os filtros ou crie uma nova voz para começar a dublagem.",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF64748B)),
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
            )
            Button(
                onClick = onNewVoice,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Criar Primeira Voz")
            }
        }
    }
}
