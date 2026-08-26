package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.ui.navigation.StudioDestination

@Composable
fun SoundsScreen(
    sounds: List<SoundMusicEntity>,
    projects: List<ProjectEntity> = emptyList(),
    series: List<SeriesEntity> = emptyList(),
    seasons: List<SeasonEntity> = emptyList(),
    episodes: List<EpisodeEntity> = emptyList(),
    scenes: List<SceneEntity> = emptyList(),
    recentActivities: List<ActivityLogEntity> = emptyList(),
    selectedProjectId: Long? = null,
    activeSound: SoundMusicEntity? = null,
    showNewSoundDialog: Boolean = false,
    editingSound: SoundMusicEntity? = null,
    showAttachToSceneDialog: SoundMusicEntity? = null,
    showAttachToEpisodeDialog: SoundMusicEntity? = null,
    showAudioEditorDialog: SoundMusicEntity? = null,
    showCreateSoundWithAiDialog: Boolean = false,
    showUploadSoundDialog: Boolean = false,
    onSetActiveSound: (SoundMusicEntity?) -> Unit = {},
    onOpenNewSound: (SoundMusicEntity?) -> Unit = {},
    onCloseNewSound: () -> Unit = {},
    onOpenUploadSound: () -> Unit = {},
    onCloseUploadSound: () -> Unit = {},
    onOpenCreateSoundWithAi: () -> Unit = {},
    onCloseCreateSoundWithAi: () -> Unit = {},
    onOpenAttachToScene: (SoundMusicEntity) -> Unit = {},
    onCloseAttachToScene: () -> Unit = {},
    onOpenAttachToEpisode: (SoundMusicEntity) -> Unit = {},
    onCloseAttachToEpisode: () -> Unit = {},
    onOpenAudioEditor: (SoundMusicEntity) -> Unit = {},
    onCloseAudioEditor: () -> Unit = {},
    onSaveSound: (SoundMusicEntity, Boolean) -> Unit = { _, _ -> },
    onDeleteSound: (SoundMusicEntity) -> Unit = {},
    onDuplicateSound: (SoundMusicEntity) -> Unit = {},
    onToggleFavoriteSound: (SoundMusicEntity) -> Unit = {},
    onArchiveSound: (SoundMusicEntity) -> Unit = {},
    onAssociateProject: (SoundMusicEntity, Long, String) -> Unit = { _, _, _ -> },
    onAssociateScene: (SoundMusicEntity, Long, String, Long?, Long?, Long?) -> Unit = { _, _, _, _, _, _ -> },
    onAssociateEpisode: (SoundMusicEntity, Long, String, Float, Boolean, Boolean) -> Unit = { _, _, _, _, _, _ -> },
    onUpdateAudioEditing: (Long, String, Int, Float, Boolean, Boolean, Boolean, String?) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToDestination: (StudioDestination) -> Unit = {}
) {
    // Audio synthesis & playback engine
    val audioEngine = remember { SoundAudioEngine() }
    var currentlyPlayingSoundId by remember { mutableStateOf<Long?>(null) }
    var currentPlaybackSeconds by remember { mutableIntStateOf(0) }
    var playbackProgress by remember { mutableFloatStateOf(0f) }

    DisposableEffect(Unit) {
        onDispose {
            audioEngine.stop()
        }
    }

    fun playSoundPreview(sound: SoundMusicEntity) {
        if (currentlyPlayingSoundId == sound.id) {
            audioEngine.stop()
            currentlyPlayingSoundId = null
            playbackProgress = 0f
            currentPlaybackSeconds = 0
            return
        }

        currentlyPlayingSoundId = sound.id
        currentPlaybackSeconds = 0
        playbackProgress = 0f

        audioEngine.playSound(
            soundType = sound.type,
            category = sound.category,
            durationSeconds = sound.durationSeconds.coerceAtLeast(4),
            volume = sound.bgVolume,
            fadeIn = sound.bgFadeIn,
            fadeOut = sound.bgFadeOut,
            onProgress = { sec, prog ->
                currentPlaybackSeconds = sec
                playbackProgress = prog
            },
            onCompletion = {
                currentlyPlayingSoundId = null
                playbackProgress = 0f
                currentPlaybackSeconds = 0
            }
        )
    }

    fun stopSoundPreview() {
        audioEngine.stop()
        currentlyPlayingSoundId = null
        playbackProgress = 0f
        currentPlaybackSeconds = 0
    }

    // Filters and state
    var searchQuery by remember { mutableStateOf("") }
    var primaryTab by remember { mutableStateOf("Todos") }
    var selectedMoodFilter by remember { mutableStateOf("Todos") }
    var selectedDurationFilter by remember { mutableStateOf("Qualquer duração") }
    var isGridView by remember { mutableStateOf(true) }
    var sortBy by remember { mutableStateOf("Mais recentes") }

    // Dialog state for delete confirmation
    var soundToDelete by remember { mutableStateOf<SoundMusicEntity?>(null) }

    // Filter Logic
    val filteredSounds = sounds.filter { s ->
        val matchesProject = selectedProjectId == null || s.projectId == null || s.projectId == selectedProjectId

        val matchesSearch = searchQuery.isBlank() ||
                s.title.contains(searchQuery, ignoreCase = true) ||
                s.category.contains(searchQuery, ignoreCase = true) ||
                s.mood.contains(searchQuery, ignoreCase = true) ||
                s.type.contains(searchQuery, ignoreCase = true) ||
                s.description.contains(searchQuery, ignoreCase = true)

        val matchesTab = when (primaryTab) {
            "Todos" -> !s.isArchived
            "Músicas" -> !s.isArchived && (s.type.equals("Música", ignoreCase = true) || s.category.contains("Música", ignoreCase = true))
            "Efeitos (SFX)" -> !s.isArchived && (s.type.equals("SFX", ignoreCase = true) || s.type.contains("SFX", ignoreCase = true) || s.type.contains("Efeito", ignoreCase = true) || s.category.contains("Efeito", ignoreCase = true))
            "Ambientes" -> !s.isArchived && (s.type.equals("Ambiente", ignoreCase = true) || s.type.contains("Ambiente", ignoreCase = true) || s.category.contains("Ambiente", ignoreCase = true))
            "Trilhas" -> !s.isArchived && (s.type.contains("Trilha", ignoreCase = true) || s.category.contains("Trilha", ignoreCase = true))
            "Favoritos" -> !s.isArchived && s.isFavorite
            else -> true
        }

        val matchesMood = when (selectedMoodFilter) {
            "Todos" -> true
            else -> s.mood.contains(selectedMoodFilter, ignoreCase = true)
        }

        val matchesDuration = when (selectedDurationFilter) {
            "Qualquer duração" -> true
            "Curtos (< 15s)" -> s.durationSeconds < 15
            "Médios (15s - 1m)" -> s.durationSeconds in 15..60
            "Longos (> 1m)" -> s.durationSeconds > 60
            else -> true
        }

        matchesProject && matchesSearch && matchesTab && matchesMood && matchesDuration
    }.let { list ->
        when (sortBy) {
            "Título A–Z" -> list.sortedBy { it.title }
            "Maior duração" -> list.sortedByDescending { it.durationSeconds }
            "Mais utilizados" -> list.sortedByDescending { it.usageScenesCount + it.usageEpisodesCount }
            "Favoritos" -> list.sortedByDescending { it.isFavorite }
            else -> list.sortedByDescending { it.updatedAt }
        }
    }

    val totalSoundsCount = sounds.size
    val musicCount = sounds.count { !it.isArchived && (it.type.contains("Música", ignoreCase = true) || it.category.contains("Música", ignoreCase = true)) }
    val sfxCount = sounds.count { !it.isArchived && (it.type.contains("SFX", ignoreCase = true) || it.type.contains("Efeito", ignoreCase = true) || it.category.contains("Efeito", ignoreCase = true)) }
    val ambientCount = sounds.count { !it.isArchived && (it.type.contains("Ambiente", ignoreCase = true) || it.category.contains("Ambiente", ignoreCase = true)) }
    val soundtrackCount = sounds.count { !it.isArchived && (it.type.contains("Trilha", ignoreCase = true) || it.category.contains("Trilha", ignoreCase = true)) }
    val favoritesCount = sounds.count { it.isFavorite }
    val usedInScenesCount = sounds.count { it.sceneName != null || it.episodeName != null }

    val selectedSound = activeSound ?: filteredSounds.firstOrNull()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
    ) {
        val isWideScreen = maxWidth > 860.dp

        if (isWideScreen) {
            // Adaptive 2-column layout (matching VoicesScreen layout)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Left Column: Library List
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxHeight()
                ) {
                    SoundTopHeaderAndContextBar(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        totalCount = totalSoundsCount,
                        projects = projects,
                        selectedProjectId = selectedProjectId,
                        onUploadClick = onOpenUploadSound,
                        onAiCreateClick = onOpenCreateSoundWithAi,
                        onNewSoundClick = { onOpenNewSound(null) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SoundFilterPillSection(
                        primaryTab = primaryTab,
                        onTabSelect = { primaryTab = it },
                        totalCount = totalSoundsCount,
                        musicCount = musicCount,
                        sfxCount = sfxCount,
                        ambientCount = ambientCount,
                        soundtrackCount = soundtrackCount,
                        favoritesCount = favoritesCount,
                        selectedMood = selectedMoodFilter,
                        onMoodSelect = { selectedMoodFilter = it },
                        selectedDuration = selectedDurationFilter,
                        onDurationSelect = { selectedDurationFilter = it },
                        isGridView = isGridView,
                        onToggleGridView = { isGridView = it },
                        sortBy = sortBy,
                        onSortChange = { sortBy = it }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SoundCardsContent(
                        sounds = filteredSounds,
                        selectedSoundId = selectedSound?.id,
                        currentlyPlayingSoundId = currentlyPlayingSoundId,
                        isGridView = isGridView,
                        onSelectSound = { onSetActiveSound(it) },
                        onPlaySound = { playSoundPreview(it) },
                        onToggleFavorite = { onToggleFavoriteSound(it) },
                        onEdit = { onOpenNewSound(it) },
                        onDuplicate = { onDuplicateSound(it) },
                        onOpenAttachToScene = { onOpenAttachToScene(it) },
                        onOpenAttachToEpisode = { onOpenAttachToEpisode(it) },
                        onOpenEditor = { onOpenAudioEditor(it) },
                        onArchive = { onArchiveSound(it) },
                        onDelete = { soundToDelete = it },
                        onNewSoundClick = { onOpenNewSound(null) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SoundBottomStatsSection(
                        totalCount = totalSoundsCount,
                        musicCount = musicCount,
                        sfxCount = sfxCount,
                        usedCount = usedInScenesCount,
                        recentActivities = recentActivities
                    )
                }

                // Right Column: Detail Studio & Tuning Panel
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (selectedSound != null) {
                        SoundAudioPlayerCard(
                            sound = selectedSound,
                            isPlaying = currentlyPlayingSoundId == selectedSound.id,
                            currentPlaybackSeconds = currentPlaybackSeconds,
                            playbackProgress = playbackProgress,
                            onPlay = { playSoundPreview(selectedSound) },
                            onStop = { stopSoundPreview() },
                            onOpenEditor = { onOpenAudioEditor(selectedSound) }
                        )

                        SoundQuickEditorCard(
                            sound = selectedSound,
                            onOpenFullEditor = { onOpenAudioEditor(selectedSound) },
                            onEditMetadata = { onOpenNewSound(selectedSound) }
                        )

                        SoundSceneEpisodeLinkCard(
                            sound = selectedSound,
                            onAttachToScene = { onOpenAttachToScene(selectedSound) },
                            onAttachToEpisode = { onOpenAttachToEpisode(selectedSound) }
                        )

                        SoundTechnicalSpecsCard(sound = selectedSound)
                    }

                    SoundAiAssistantCard(
                        selectedSound = selectedSound,
                        onOpenAiWithPrompt = onOpenAiWithPrompt
                    )
                }
            }
        } else {
            // Single Column Vertical Layout for Compact Devices
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                contentPadding = PaddingValues(top = 14.dp, bottom = 84.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    SoundTopHeaderAndContextBar(
                        searchQuery = searchQuery,
                        onSearchChange = { searchQuery = it },
                        totalCount = totalSoundsCount,
                        projects = projects,
                        selectedProjectId = selectedProjectId,
                        onUploadClick = onOpenUploadSound,
                        onAiCreateClick = onOpenCreateSoundWithAi,
                        onNewSoundClick = { onOpenNewSound(null) }
                    )
                }

                item {
                    SoundFilterPillSection(
                        primaryTab = primaryTab,
                        onTabSelect = { primaryTab = it },
                        totalCount = totalSoundsCount,
                        musicCount = musicCount,
                        sfxCount = sfxCount,
                        ambientCount = ambientCount,
                        soundtrackCount = soundtrackCount,
                        favoritesCount = favoritesCount,
                        selectedMood = selectedMoodFilter,
                        onMoodSelect = { selectedMoodFilter = it },
                        selectedDuration = selectedDurationFilter,
                        onDurationSelect = { selectedDurationFilter = it },
                        isGridView = isGridView,
                        onToggleGridView = { isGridView = it },
                        sortBy = sortBy,
                        onSortChange = { sortBy = it }
                    )
                }

                if (selectedSound != null) {
                    item {
                        SoundAudioPlayerCard(
                            sound = selectedSound,
                            isPlaying = currentlyPlayingSoundId == selectedSound.id,
                            currentPlaybackSeconds = currentPlaybackSeconds,
                            playbackProgress = playbackProgress,
                            onPlay = { playSoundPreview(selectedSound) },
                            onStop = { stopSoundPreview() },
                            onOpenEditor = { onOpenAudioEditor(selectedSound) }
                        )
                    }
                }

                item {
                    SoundCardsContent(
                        sounds = filteredSounds,
                        selectedSoundId = selectedSound?.id,
                        currentlyPlayingSoundId = currentlyPlayingSoundId,
                        isGridView = isGridView,
                        onSelectSound = { onSetActiveSound(it) },
                        onPlaySound = { playSoundPreview(it) },
                        onToggleFavorite = { onToggleFavoriteSound(it) },
                        onEdit = { onOpenNewSound(it) },
                        onDuplicate = { onDuplicateSound(it) },
                        onOpenAttachToScene = { onOpenAttachToScene(it) },
                        onOpenAttachToEpisode = { onOpenAttachToEpisode(it) },
                        onOpenEditor = { onOpenAudioEditor(it) },
                        onArchive = { onArchiveSound(it) },
                        onDelete = { soundToDelete = it },
                        onNewSoundClick = { onOpenNewSound(null) }
                    )
                }

                if (selectedSound != null) {
                    item {
                        SoundSceneEpisodeLinkCard(
                            sound = selectedSound,
                            onAttachToScene = { onOpenAttachToScene(selectedSound) },
                            onAttachToEpisode = { onOpenAttachToEpisode(selectedSound) }
                        )
                    }
                    item {
                        SoundTechnicalSpecsCard(sound = selectedSound)
                    }
                }

                item {
                    SoundAiAssistantCard(
                        selectedSound = selectedSound,
                        onOpenAiWithPrompt = onOpenAiWithPrompt
                    )
                }

                item {
                    SoundBottomStatsSection(
                        totalCount = totalSoundsCount,
                        musicCount = musicCount,
                        sfxCount = sfxCount,
                        usedCount = usedInScenesCount,
                        recentActivities = recentActivities
                    )
                }
            }
        }
    }

    // Modal Dialogs
    if (showNewSoundDialog) {
        NewSoundDialog(
            sound = editingSound,
            projects = projects,
            selectedProjectId = selectedProjectId,
            onDismiss = onCloseNewSound,
            onSave = { sound ->
                onSaveSound(sound, true)
            }
        )
    }

    if (showUploadSoundDialog) {
        UploadSoundDialog(
            projects = projects,
            selectedProjectId = selectedProjectId,
            onDismiss = onCloseUploadSound,
            onUploadSuccess = { sound ->
                onSaveSound(sound, true)
            }
        )
    }

    if (showCreateSoundWithAiDialog) {
        CreateSoundWithAiDialog(
            projects = projects,
            selectedProjectId = selectedProjectId,
            onDismiss = onCloseCreateSoundWithAi,
            onOpenAiWithPrompt = onOpenAiWithPrompt,
            onSaveAiSound = { sound ->
                onSaveSound(sound, true)
            }
        )
    }

    showAttachToSceneDialog?.let { sound ->
        AttachToSceneDialog(
            sound = sound,
            scenes = scenes,
            episodes = episodes,
            series = series,
            projects = projects,
            onDismiss = onCloseAttachToScene,
            onAttach = { sceneId, sceneName, epId, serId, projId ->
                onAssociateScene(sound, sceneId, sceneName, epId, serId, projId)
            }
        )
    }

    showAttachToEpisodeDialog?.let { sound ->
        AttachToEpisodeDialog(
            sound = sound,
            episodes = episodes,
            onDismiss = onCloseAttachToEpisode,
            onAttach = { epId, epName, vol, fadeIn, fadeOut ->
                onAssociateEpisode(sound, epId, epName, vol, fadeIn, fadeOut)
            }
        )
    }

    showAudioEditorDialog?.let { sound ->
        AudioEditorDialog(
            sound = sound,
            onDismiss = onCloseAudioEditor,
            onSaveEdits = { newDur, durSecs, vol, fadeIn, fadeOut, asNew, newTitle ->
                onUpdateAudioEditing(sound.id, newDur, durSecs, vol, fadeIn, fadeOut, asNew, newTitle)
            }
        )
    }

    soundToDelete?.let { sound ->
        DeleteSoundConfirmDialog(
            sound = sound,
            onDismiss = { soundToDelete = null },
            onConfirm = {
                onDeleteSound(sound)
                soundToDelete = null
            }
        )
    }
}
