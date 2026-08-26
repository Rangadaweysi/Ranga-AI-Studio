package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.remote.AiStudioTask
import com.example.ui.StudioViewModel
import com.example.ui.components.AiAssistantModal
import com.example.ui.components.StudioNavDrawer
import com.example.ui.components.StudioTopBar
import com.example.ui.navigation.StudioDestination
import com.example.ui.screens.CharactersScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EpisodesScreen
import com.example.ui.screens.ExportScreen
import com.example.ui.screens.ModulePlaceholderScreen
import com.example.ui.screens.ProjectFormDialog
import com.example.ui.screens.ProjectsScreen
import com.example.ui.screens.RangaCreationScreen
import com.example.ui.screens.ScenariosScreen
import com.example.ui.screens.ScenesScreen
import com.example.ui.screens.SeasonsScreen
import com.example.ui.screens.SeriesScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SoundsScreen
import com.example.ui.screens.VoicesScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                RangaStudioApp()
            }
        }
    }
}

@Composable
fun RangaStudioApp(viewModel: StudioViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    val projects by viewModel.projects.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val scenarios by viewModel.scenarios.collectAsState()
    val series by viewModel.series.collectAsState()
    val seasons by viewModel.seasons.collectAsState()
    val episodes by viewModel.episodes.collectAsState()
    val scenes by viewModel.scenes.collectAsState()
    val voices by viewModel.voices.collectAsState()
    val sounds by viewModel.sounds.collectAsState()
    val creations by viewModel.creations.collectAsState()
    val roteiroBrancoItems by viewModel.roteiroBrancoItems.collectAsState()
    val exportRecords by viewModel.exportRecords.collectAsState()
    val settingsState by viewModel.settingsState.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()

    val projectCount by viewModel.projectCount.collectAsState()
    val characterCount by viewModel.characterCount.collectAsState()
    val sceneCount by viewModel.sceneCount.collectAsState()
    val episodeCount by viewModel.episodeCount.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            StudioNavDrawer(
                currentDestination = uiState.currentDestination,
                onDestinationSelect = { dest ->
                    viewModel.navigateTo(dest)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                StudioTopBar(
                    currentDestination = uiState.currentDestination,
                    projects = projects,
                    selectedProjectId = uiState.selectedProjectId,
                    onMenuClick = { scope.launch { drawerState.open() } },
                    onAiAssistantClick = { viewModel.openAiAssistant() },
                    onNewProjectClick = { viewModel.openNewProjectDialog() },
                    onSelectProject = { viewModel.selectProject(it) }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFF8FAFC))
            ) {
                when (uiState.currentDestination) {
                    StudioDestination.DASHBOARD -> {
                        DashboardScreen(
                            projects = projects,
                            projectCount = projectCount,
                            characterCount = characterCount,
                            sceneCount = sceneCount,
                            episodeCount = episodeCount,
                            recentActivities = recentActivities,
                            onNewProjectClick = { viewModel.openNewProjectDialog() },
                            onOpenAiAssistant = { viewModel.openAiAssistant() },
                            onNavigate = { viewModel.navigateTo(it) },
                            onSelectProject = { projId ->
                                viewModel.selectProject(projId)
                                viewModel.navigateTo(StudioDestination.PROJECTS)
                            }
                        )
                    }

                    StudioDestination.PROJECTS -> {
                        ProjectsScreen(
                            projects = projects,
                            selectedProjectId = uiState.selectedProjectId,
                            showNewProjectDialog = uiState.showNewProjectDialog,
                            editingProject = uiState.editingProject,
                            onOpenNewProject = { viewModel.openNewProjectDialog(it) },
                            onCloseNewProject = { viewModel.closeNewProjectDialog() },
                            onSaveProject = { name, desc, type, cat, cover, status ->
                                viewModel.saveProject(name, desc, type, cat, cover, status)
                            },
                            onDeleteProject = { viewModel.deleteProject(it) },
                            onDuplicateProject = { viewModel.duplicateProject(it) },
                            onArchiveProject = { viewModel.archiveProject(it) },
                            onSelectProject = { viewModel.selectProject(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.STORY_IDEAS, prompt)
                            }
                        )
                    }

                    StudioDestination.CHARACTERS -> {
                        CharactersScreen(
                            characters = characters,
                            projects = projects,
                            selectedProjectId = uiState.selectedProjectId,
                            showNewCharacterDialog = uiState.showNewCharacterDialog,
                            editingCharacter = uiState.editingCharacter,
                            onOpenNewCharacter = { viewModel.openNewCharacterDialog(it) },
                            onCloseNewCharacter = { viewModel.closeNewCharacterDialog() },
                            onSaveCharacter = { name, personality, age, desc, history, type, voice, role, projId, imgUri ->
                                viewModel.saveCharacter(name, personality, age, desc, history, type, voice, role, projId, imgUri)
                            },
                            onDeleteCharacter = { viewModel.deleteCharacter(it) },
                            onDuplicateCharacter = { viewModel.duplicateCharacter(it) },
                            onMoveCharacter = { char, newProjId -> viewModel.moveCharacterToProject(char, newProjId) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_CHARACTER, prompt)
                            },
                            onNavigateToRangaCreation = {
                                viewModel.navigateTo(StudioDestination.RANGA_CREATION)
                            }
                        )
                    }

                    StudioDestination.SCENARIOS -> {
                        ScenariosScreen(
                            scenarios = scenarios,
                            projects = projects,
                            selectedProjectId = uiState.selectedProjectId,
                            showNewScenarioDialog = uiState.showNewScenarioDialog,
                            editingScenario = uiState.editingScenario,
                            onOpenNewScenario = { viewModel.openNewScenarioDialog(it) },
                            onCloseNewScenario = { viewModel.closeNewScenarioDialog() },
                            onSaveScenario = { name, desc, category, visualStyle, locType, atmos, arch, versions, projId, imgUri ->
                                viewModel.saveScenario(name, desc, category, visualStyle, locType, atmos, arch, versions, projId, imgUri)
                            },
                            onDeleteScenario = { viewModel.deleteScenario(it) },
                            onDuplicateScenario = { viewModel.duplicateScenario(it) },
                            onMoveScenario = { sc, newProjId -> viewModel.moveScenarioToProject(sc, newProjId) },
                            onArchiveScenario = { viewModel.archiveScenario(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.SCENARIO_DESCRIPTION, prompt)
                            },
                            onNavigateToRangaCreation = {
                                viewModel.navigateTo(StudioDestination.RANGA_CREATION)
                            }
                        )
                    }

                    StudioDestination.SERIES -> {
                        SeriesScreen(
                            seriesList = series,
                            projects = projects,
                            seasons = seasons,
                            episodes = episodes,
                            characters = characters,
                            scenarios = scenarios,
                            scenes = scenes,
                            selectedProjectId = uiState.selectedProjectId,
                            activeSeries = uiState.activeSeries,
                            lastSaveTimestamp = uiState.lastSeriesSaveTimestamp,
                            showNewSeriesDialog = uiState.showNewSeriesDialog,
                            editingSeries = uiState.editingSeries,
                            onSetActiveSeries = { viewModel.setActiveSeries(it) },
                            onOpenNewSeries = { viewModel.openNewSeriesDialog(it) },
                            onCloseNewSeries = { viewModel.closeNewSeriesDialog() },
                            onSaveSeries = { s, log -> viewModel.saveFullSeries(s, log) },
                            onDeleteSeries = { viewModel.deleteSeries(it) },
                            onDuplicateSeries = { viewModel.duplicateSeries(it) },
                            onArchiveSeries = { viewModel.archiveSeries(it) },
                            onToggleFavoriteSeries = { viewModel.toggleFavoriteSeries(it) },
                            onSaveSeason = { viewModel.saveFullSeason(it) },
                            onDeleteSeason = { viewModel.deleteSeason(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.STORY_IDEAS, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.SEASONS -> {
                        SeasonsScreen(
                            seasons = seasons,
                            seriesList = series,
                            projects = projects,
                            episodes = episodes,
                            characters = characters,
                            scenarios = scenarios,
                            scenes = scenes,
                            selectedProjectId = uiState.selectedProjectId,
                            activeSeason = uiState.activeSeason,
                            activeSeries = uiState.activeSeries,
                            lastSaveTimestamp = uiState.lastSeasonSaveTimestamp,
                            showNewSeasonDialog = uiState.showNewSeasonDialog,
                            editingSeason = uiState.editingSeason,
                            onSetActiveSeason = { viewModel.setActiveSeason(it) },
                            onSetActiveSeries = { viewModel.setActiveSeries(it) },
                            onOpenNewSeason = { viewModel.openNewSeasonDialog(it) },
                            onCloseNewSeason = { viewModel.closeNewSeasonDialog() },
                            onSaveSeason = { s, log -> viewModel.saveFullSeason(s, log) },
                            onDeleteSeason = { viewModel.deleteSeason(it) },
                            onDuplicateSeason = { viewModel.duplicateSeason(it) },
                            onArchiveSeason = { viewModel.archiveSeason(it) },
                            onToggleFavoriteSeason = { viewModel.toggleFavoriteSeason(it) },
                            onSaveEpisode = { ep, log -> viewModel.saveFullEpisode(ep, log) },
                            onDeleteEpisode = { viewModel.deleteEpisode(it) },
                            onDuplicateEpisode = { viewModel.duplicateEpisode(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.STORY_IDEAS, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.EPISODES -> {
                        EpisodesScreen(
                            episodes = episodes,
                            projects = projects,
                            series = series,
                            seasons = seasons,
                            scenes = scenes,
                            characters = characters,
                            scenarios = scenarios,
                            voices = voices,
                            sounds = sounds,
                            recentActivities = recentActivities,
                            selectedProjectId = uiState.selectedProjectId,
                            activeEpisode = uiState.activeEpisode,
                            activeSeason = uiState.activeSeason,
                            activeSeries = uiState.activeSeries,
                            lastSaveTimestamp = uiState.lastEpisodeSaveTimestamp,
                            showNewEpisodeDialog = uiState.showNewEpisodeDialog,
                            editingEpisode = uiState.editingEpisode,
                            onSetActiveEpisode = { viewModel.setActiveEpisode(it) },
                            onSetActiveSeason = { viewModel.setActiveSeason(it) },
                            onSetActiveSeries = { viewModel.setActiveSeries(it) },
                            onOpenNewEpisode = { viewModel.openNewEpisodeDialog(it) },
                            onCloseNewEpisode = { viewModel.closeNewEpisodeDialog() },
                            onSaveEpisode = { ep, log -> viewModel.saveFullEpisode(ep, log) },
                            onDeleteEpisode = { viewModel.deleteEpisode(it) },
                            onDuplicateEpisode = { viewModel.duplicateEpisode(it) },
                            onArchiveEpisode = { viewModel.archiveEpisode(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_SCRIPT, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.SCENES -> {
                        ScenesScreen(
                            scenes = scenes,
                            episodes = episodes,
                            characters = characters,
                            scenarios = scenarios,
                            projects = projects,
                            selectedProjectId = uiState.selectedProjectId,
                            activeScene = uiState.activeScene,
                            isStudioMode = uiState.isSceneStudioMode,
                            lastSaveTimestamp = uiState.lastSceneSaveTimestamp,
                            onSetActiveScene = { viewModel.setActiveScene(it) },
                            onSetStudioMode = { viewModel.setSceneStudioMode(it) },
                            onSaveScene = { scene, log -> viewModel.saveFullScene(scene, log) },
                            onDeleteScene = { viewModel.deleteScene(it) },
                            onDuplicateScene = { viewModel.duplicateScene(it) },
                            onMoveScene = { sc, newProjId -> viewModel.moveSceneToProject(sc, newProjId) },
                            onArchiveScene = { viewModel.archiveScene(it) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_DIALOGUE, prompt)
                            },
                            onNavigateToRangaCreation = {
                                viewModel.navigateTo(StudioDestination.RANGA_CREATION)
                            }
                        )
                    }

                    StudioDestination.VOICES -> {
                        VoicesScreen(
                            voices = voices,
                            characters = characters,
                            projects = projects,
                            series = series,
                            seasons = seasons,
                            recentActivities = recentActivities,
                            selectedProjectId = uiState.selectedProjectId,
                            activeVoice = uiState.activeVoice,
                            showNewVoiceDialog = uiState.showNewVoiceDialog,
                            editingVoice = uiState.editingVoice,
                            onSetActiveVoice = { viewModel.setActiveVoice(it) },
                            onOpenNewVoice = { viewModel.openNewVoiceDialog(it) },
                            onCloseNewVoice = { viewModel.closeNewVoiceDialog() },
                            onSaveVoice = { voice, log -> viewModel.saveFullVoice(voice, log) },
                            onDeleteVoice = { viewModel.deleteVoice(it) },
                            onDuplicateVoice = { viewModel.duplicateVoice(it) },
                            onToggleFavoriteVoice = { viewModel.toggleFavoriteVoice(it) },
                            onArchiveVoice = { viewModel.archiveVoice(it) },
                            onAssociateCharacter = { voice, charName, emoji, charId ->
                                viewModel.associateVoiceToCharacter(voice, charName, emoji, charId)
                            },
                            onAssociateProject = { voice, projId, projName ->
                                viewModel.associateVoiceToProject(voice, projId, projName)
                            },
                            onUpdateVoiceTuning = { voiceId, speed, pitch, exp, sample ->
                                viewModel.updateVoiceTuning(voiceId, speed, pitch, exp, sample)
                            },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_CHARACTER, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.SOUNDS_MUSIC -> {
                        SoundsScreen(
                            sounds = sounds,
                            projects = projects,
                            series = series,
                            seasons = seasons,
                            episodes = episodes,
                            scenes = scenes,
                            recentActivities = recentActivities,
                            selectedProjectId = uiState.selectedProjectId,
                            activeSound = uiState.activeSound,
                            showNewSoundDialog = uiState.showNewSoundDialog,
                            editingSound = uiState.editingSound,
                            showAttachToSceneDialog = uiState.showAttachToSceneDialog,
                            showAttachToEpisodeDialog = uiState.showAttachToEpisodeDialog,
                            showAudioEditorDialog = uiState.showAudioEditorDialog,
                            showCreateSoundWithAiDialog = uiState.showCreateSoundWithAiDialog,
                            showUploadSoundDialog = uiState.showUploadSoundDialog,
                            onSetActiveSound = { viewModel.setActiveSound(it) },
                            onOpenNewSound = { viewModel.openNewSoundDialog(it) },
                            onCloseNewSound = { viewModel.closeNewSoundDialog() },
                            onOpenUploadSound = { viewModel.openUploadSoundDialog() },
                            onCloseUploadSound = { viewModel.closeUploadSoundDialog() },
                            onOpenCreateSoundWithAi = { viewModel.openCreateSoundWithAiDialog() },
                            onCloseCreateSoundWithAi = { viewModel.closeCreateSoundWithAiDialog() },
                            onOpenAttachToScene = { viewModel.openAttachToSceneDialog(it) },
                            onCloseAttachToScene = { viewModel.closeAttachToSceneDialog() },
                            onOpenAttachToEpisode = { viewModel.openAttachToEpisodeDialog(it) },
                            onCloseAttachToEpisode = { viewModel.closeAttachToEpisodeDialog() },
                            onOpenAudioEditor = { viewModel.openAudioEditorDialog(it) },
                            onCloseAudioEditor = { viewModel.closeAudioEditorDialog() },
                            onSaveSound = { sound, log -> viewModel.saveFullSound(sound, log) },
                            onDeleteSound = { viewModel.deleteSound(it) },
                            onDuplicateSound = { viewModel.duplicateSound(it) },
                            onToggleFavoriteSound = { viewModel.toggleFavoriteSound(it) },
                            onArchiveSound = { viewModel.archiveSound(it) },
                            onAssociateProject = { sound, projId, projName ->
                                viewModel.associateSoundToProject(sound, projId, projName)
                            },
                            onAssociateScene = { sound, sceneId, sceneName, epId, serId, projId ->
                                viewModel.associateSoundToScene(sound, sceneId, sceneName, epId, serId, projId)
                            },
                            onAssociateEpisode = { sound, epId, epName, vol, fadeIn, fadeOut ->
                                viewModel.associateSoundToEpisode(sound, epId, epName, vol, fadeIn, fadeOut)
                            },
                            onUpdateAudioEditing = { soundId, newDur, durSecs, vol, fadeIn, fadeOut, asNew, newTitle ->
                                viewModel.updateAudioEditing(soundId, newDur, durSecs, vol, fadeIn, fadeOut, asNew, newTitle)
                            },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.DEVELOP_SCENE, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.EXPORT -> {
                        ExportScreen(
                            projects = projects,
                            series = series,
                            seasons = seasons,
                            episodes = episodes,
                            scenes = scenes,
                            characters = characters,
                            scenarios = scenarios,
                            voices = voices,
                            sounds = sounds,
                            creations = creations,
                            roteiroBrancoItems = roteiroBrancoItems,
                            exportRecords = exportRecords,
                            selectedProjectId = uiState.selectedProjectId,
                            onSaveExport = { rec -> viewModel.saveExportRecord(rec) },
                            onUpdateExport = { rec -> viewModel.updateExportRecord(rec) },
                            onDeleteExport = { rec -> viewModel.deleteExportRecord(rec) },
                            onRetryExport = { rec -> viewModel.retryExportRecord(rec) },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_SCRIPT, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.RANGA_CREATION -> {
                        RangaCreationScreen(
                            creations = creations,
                            roteiroBrancoItems = roteiroBrancoItems,
                            projects = projects,
                            characters = characters,
                            scenarios = scenarios,
                            selectedProjectId = uiState.selectedProjectId,
                            onSaveCreation = { creation -> viewModel.saveCreation(creation) },
                            onUpdateCreation = { creation -> viewModel.updateCreation(creation) },
                            onDeleteCreation = { creation -> viewModel.deleteCreation(creation) },
                            onToggleFavoriteCreation = { creation -> viewModel.toggleFavoriteCreation(creation) },
                            onToggleRoteiroBranco = { creation -> viewModel.toggleRoteiroBranco(creation) },
                            onUseAsCharacter = { creation, name, role, pers, pId ->
                                viewModel.useCreationAsCharacter(creation, name, role, pers, pId)
                            },
                            onUseAsScenario = { creation, name, cat, atmos, pId ->
                                viewModel.useCreationAsScenario(creation, name, cat, atmos, pId)
                            },
                            onAddToRoteiroBranco = { creation, notes ->
                                viewModel.addToRoteiroBranco(creation, notes)
                            },
                            onDeleteRoteiroBrancoItem = { item ->
                                viewModel.deleteRoteiroBrancoItem(item)
                            },
                            onSaveCharacter = { name, personality, age, desc, history, type, voice, role, projId, imgUri ->
                                viewModel.saveCharacter(name, personality, age, desc, history, type, voice, role, projId, imgUri)
                            },
                            onOpenFullCharacterCreator = {
                                viewModel.openNewCharacterDialog(it)
                                viewModel.navigateTo(StudioDestination.CHARACTERS)
                            },
                            onOpenAiWithPrompt = { prompt ->
                                viewModel.openAiAssistant(AiStudioTask.CREATE_CHARACTER, prompt)
                            },
                            onNavigateToDestination = { dest ->
                                viewModel.navigateTo(dest)
                            }
                        )
                    }

                    StudioDestination.SETTINGS -> {
                        SettingsScreen(
                            settings = settingsState,
                            onUpdateUserProfile = { name, email, colorHex ->
                                viewModel.updateUserProfile(name, email, colorHex)
                            },
                            onUpdateAppearance = { theme, colorHex, uiSize, density ->
                                viewModel.updateAppearance(theme, colorHex, uiSize, density)
                            },
                            onUpdateLanguage = { lang ->
                                viewModel.updateLanguage(lang)
                            },
                            onUpdateRangaCreation = { set ->
                                viewModel.updateRangaCreationSettings(set)
                            },
                            onUpdateVoices = { set ->
                                viewModel.updateVoiceSettings(set)
                            },
                            onUpdateAudio = { set ->
                                viewModel.updateAudioSettings(set)
                            },
                            onUpdateProjects = { set ->
                                viewModel.updateProjectsProductionSettings(set)
                            },
                            onUpdateExport = { set ->
                                viewModel.updateExportSettings(set)
                            },
                            onUpdateNotifications = { set ->
                                viewModel.updateNotificationSettings(set)
                            },
                            onSaveApiKey = { svc, key ->
                                viewModel.saveApiKey(svc, key)
                            },
                            onRemoveApiKey = { id ->
                                viewModel.removeApiKey(id)
                            },
                            onLogoutOtherSessions = {
                                viewModel.logoutOtherSessions()
                            },
                            onCleanTempFiles = {
                                viewModel.cleanTempFiles()
                            },
                            onSyncNow = {
                                viewModel.syncNow()
                            },
                            onDeleteAccount = {
                                viewModel.deleteAccount()
                            }
                        )
                    }
                }

                // Global New Project Dialog for Dashboard & TopBar
                if (uiState.showNewProjectDialog && uiState.currentDestination == StudioDestination.DASHBOARD) {
                    ProjectFormDialog(
                        editingProject = uiState.editingProject,
                        onDismiss = { viewModel.closeNewProjectDialog() },
                        onSave = { name, desc, type, cat, cover, status ->
                            viewModel.saveProject(name, desc, type, cat, cover, status)
                        },
                        onAiSuggest = { prompt ->
                            viewModel.openAiAssistant(
                                AiStudioTask.STORY_IDEAS,
                                prompt
                            )
                        }
                    )
                }
            }
        }
    }

    // AI Assistant Modal Sheet
    AiAssistantModal(
        isOpen = uiState.isAiAssistantOpen,
        selectedTask = uiState.aiSelectedTask,
        promptInput = uiState.aiPromptInput,
        resultText = uiState.aiResultText,
        isLoading = uiState.isAiLoading,
        errorMessage = uiState.aiErrorMessage,
        onTaskSelect = { viewModel.setAiTask(it) },
        onPromptChange = { viewModel.setAiPrompt(it) },
        onGenerateClick = { viewModel.generateWithAi() },
        onDismiss = { viewModel.closeAiAssistant() }
    )
}
