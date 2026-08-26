package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.StudioDatabase
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ActiveSession
import com.example.data.model.ApiKeyItem
import com.example.data.model.AppearanceSettings
import com.example.data.model.AudioSettings
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.EpisodeStatus
import com.example.data.model.ExportDefaultSettings
import com.example.data.model.ExportRecordEntity
import com.example.data.model.LanguageSettings
import com.example.data.model.NotificationSettings
import com.example.data.model.ProjectEntity
import com.example.data.model.ProjectType
import com.example.data.model.ProjectsProductionSettings
import com.example.data.model.RangaCreationEntity
import com.example.data.model.RangaCreationSettings
import com.example.data.model.RoteiroBrancoItemEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.data.model.StorageBreakdown
import com.example.data.model.StudioFullSettings
import com.example.data.model.SyncInfo
import com.example.data.model.UserProfileSettings
import com.example.data.model.VoiceEntity
import com.example.data.model.VoiceSettings
import com.example.data.remote.AiStudioTask
import com.example.data.remote.StudioAiService
import com.example.data.repository.StudioRepository
import com.example.ui.navigation.StudioDestination
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class StudioUiState(
    val currentDestination: StudioDestination = StudioDestination.DASHBOARD,
    val selectedProjectId: Long? = null,
    val searchQuery: String = "",
    val isAiAssistantOpen: Boolean = false,
    val aiSelectedTask: AiStudioTask = AiStudioTask.STORY_IDEAS,
    val aiPromptInput: String = "",
    val aiResultText: String = "",
    val isAiLoading: Boolean = false,
    val aiErrorMessage: String? = null,
    val showNewProjectDialog: Boolean = false,
    val showNewCharacterDialog: Boolean = false,
    val showNewScenarioDialog: Boolean = false,
    val showNewSceneDialog: Boolean = false,
    val showNewEpisodeDialog: Boolean = false,
    val showNewSeriesDialog: Boolean = false,
    val showNewSeasonDialog: Boolean = false,
    val showNewVoiceDialog: Boolean = false,
    val showNewSoundDialog: Boolean = false,
    val activeSeries: SeriesEntity? = null,
    val lastSeriesSaveTimestamp: Long = System.currentTimeMillis(),
    val editingSeries: SeriesEntity? = null,
    val activeSeason: SeasonEntity? = null,
    val lastSeasonSaveTimestamp: Long = System.currentTimeMillis(),
    val editingSeason: SeasonEntity? = null,
    val activeScene: SceneEntity? = null,
    val isSceneStudioMode: Boolean = true,
    val lastSceneSaveTimestamp: Long = System.currentTimeMillis(),
    val editingScene: SceneEntity? = null,
    val editingCharacter: CharacterEntity? = null,
    val editingScenario: ScenarioEntity? = null,
    val editingProject: ProjectEntity? = null,
    val activeEpisode: EpisodeEntity? = null,
    val lastEpisodeSaveTimestamp: Long = System.currentTimeMillis(),
    val editingEpisode: EpisodeEntity? = null,
    val activeVoice: VoiceEntity? = null,
    val lastVoiceSaveTimestamp: Long = System.currentTimeMillis(),
    val editingVoice: VoiceEntity? = null,
    val activeSound: SoundMusicEntity? = null,
    val lastSoundSaveTimestamp: Long = System.currentTimeMillis(),
    val editingSound: SoundMusicEntity? = null,
    val showAttachToSceneDialog: SoundMusicEntity? = null,
    val showAttachToEpisodeDialog: SoundMusicEntity? = null,
    val showAudioEditorDialog: SoundMusicEntity? = null,
    val showCreateSoundWithAiDialog: Boolean = false,
    val showUploadSoundDialog: Boolean = false,
    val activeCreation: RangaCreationEntity? = null,
    val lastCreationSaveTimestamp: Long = System.currentTimeMillis(),
    val showCreationDetailsDialog: RangaCreationEntity? = null,
    val showUseAsCharacterDialog: RangaCreationEntity? = null,
    val showUseAsScenarioDialog: RangaCreationEntity? = null,
    val showAddToRoteiroDialog: RangaCreationEntity? = null,
    val showEditCreationPromptDialog: RangaCreationEntity? = null
)

class StudioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: StudioRepository
    private val aiService = StudioAiService()

    private val _uiState = MutableStateFlow(StudioUiState())
    val uiState: StateFlow<StudioUiState> = _uiState.asStateFlow()

    init {
        val db = StudioDatabase.getDatabase(application, viewModelScope)
        repository = StudioRepository(db.studioDao())
    }

    val projects: StateFlow<List<ProjectEntity>> = repository.allProjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val characters: StateFlow<List<CharacterEntity>> = repository.allCharacters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scenarios: StateFlow<List<ScenarioEntity>> = repository.allScenarios
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val series: StateFlow<List<SeriesEntity>> = repository.allSeries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val seasons: StateFlow<List<SeasonEntity>> = repository.allSeasons
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val episodes: StateFlow<List<EpisodeEntity>> = repository.allEpisodes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val scenes: StateFlow<List<SceneEntity>> = repository.allScenes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val voices: StateFlow<List<VoiceEntity>> = repository.allVoices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sounds: StateFlow<List<SoundMusicEntity>> = repository.allSounds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentActivities: StateFlow<List<ActivityLogEntity>> = repository.recentActivities
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectCount: StateFlow<Int> = repository.projectCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val characterCount: StateFlow<Int> = repository.characterCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val scenarioCount: StateFlow<Int> = repository.scenarioCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val episodeCount: StateFlow<Int> = repository.episodeCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val sceneCount: StateFlow<Int> = repository.sceneCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val creations: StateFlow<List<RangaCreationEntity>> = repository.allCreations
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val roteiroBrancoItems: StateFlow<List<RoteiroBrancoItemEntity>> = repository.allRoteiroBrancoItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val exportRecords: StateFlow<List<ExportRecordEntity>> = repository.allExports
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _settingsState = MutableStateFlow(StudioFullSettings())
    val settingsState: StateFlow<StudioFullSettings> = _settingsState.asStateFlow()

    val creationCount: StateFlow<Int> = repository.creationCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val roteiroBrancoItemCount: StateFlow<Int> = repository.roteiroBrancoItemCount
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Navigation
    fun navigateTo(destination: StudioDestination) {
        _uiState.value = _uiState.value.copy(currentDestination = destination)
    }

    fun selectProject(projectId: Long?) {
        _uiState.value = _uiState.value.copy(selectedProjectId = projectId)
    }

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    // AI Assistant Actions
    fun openAiAssistant(task: AiStudioTask = AiStudioTask.STORY_IDEAS, prefilledPrompt: String = "") {
        _uiState.value = _uiState.value.copy(
            isAiAssistantOpen = true,
            aiSelectedTask = task,
            aiPromptInput = prefilledPrompt,
            aiErrorMessage = null
        )
    }

    fun closeAiAssistant() {
        _uiState.value = _uiState.value.copy(isAiAssistantOpen = false)
    }

    fun setAiTask(task: AiStudioTask) {
        _uiState.value = _uiState.value.copy(aiSelectedTask = task)
    }

    fun setAiPrompt(prompt: String) {
        _uiState.value = _uiState.value.copy(aiPromptInput = prompt)
    }

    fun generateWithAi() {
        val currentState = _uiState.value
        val prompt = currentState.aiPromptInput.ifBlank {
            when (currentState.aiSelectedTask) {
                AiStudioTask.STORY_IDEAS -> "Crie uma ideia inovadora de série de animação com temática espacial e robótica."
                AiStudioTask.CREATE_CHARACTER -> "Crie um personagem carismático, com visual marcante e grande arco de redenção."
                AiStudioTask.CREATE_DIALOGUE -> "Escreva um diálogo tenso e revelador entre o protagonista e seu mentor."
                AiStudioTask.CREATE_SCRIPT -> "Escreva uma cena de abertura eletrizante com ação e mistério."
                AiStudioTask.DEVELOP_SCENE -> "Desenvolva os detalhes visuais, sonoplastia e iluminação para a cena do clímax."
                AiStudioTask.CONTINUE_STORY -> "Continue a narrativa explorando a reação dos personagens após a revelação do segredo."
                AiStudioTask.IMPROVE_DIALOGUE -> "Melhore as falas para dar mais impacto emocional e naturalidade."
                AiStudioTask.SCENARIO_DESCRIPTION -> "Crie a descrição de um cenário cinematográfico futurista e acolhedor."
            }
        }

        _uiState.value = _uiState.value.copy(isAiLoading = true, aiErrorMessage = null)

        viewModelScope.launch {
            val context = buildString {
                currentState.selectedProjectId?.let { projId ->
                    val proj = projects.value.find { it.id == projId }
                    if (proj != null) {
                        append("Projeto Atual: ${proj.name} (${proj.type} - ${proj.category})\n")
                        append("Sinopse: ${proj.description}\n")
                    }
                }
            }

            val result = aiService.generateWithAi(currentState.aiSelectedTask, prompt, context)
            result.onSuccess { generatedText ->
                _uiState.value = _uiState.value.copy(
                    isAiLoading = false,
                    aiResultText = generatedText
                )
                repository.logActivity(
                    title = "Assistente de IA Utilizado",
                    description = "Geração de ${currentState.aiSelectedTask.label} concluída.",
                    iconType = "ai"
                )
            }.onFailure { err ->
                _uiState.value = _uiState.value.copy(
                    isAiLoading = false,
                    aiErrorMessage = "Erro ao processar com IA: ${err.message}"
                )
            }
        }
    }

    // Dialog state handlers
    fun openNewProjectDialog(project: ProjectEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewProjectDialog = true, editingProject = project)
    }
    fun closeNewProjectDialog() {
        _uiState.value = _uiState.value.copy(showNewProjectDialog = false, editingProject = null)
    }

    fun openNewCharacterDialog(character: CharacterEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewCharacterDialog = true, editingCharacter = character)
    }
    fun closeNewCharacterDialog() {
        _uiState.value = _uiState.value.copy(showNewCharacterDialog = false, editingCharacter = null)
    }

    fun openNewScenarioDialog(scenario: ScenarioEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewScenarioDialog = true, editingScenario = scenario)
    }
    fun closeNewScenarioDialog() {
        _uiState.value = _uiState.value.copy(showNewScenarioDialog = false, editingScenario = null)
    }

    fun openNewEpisodeDialog(episode: EpisodeEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewEpisodeDialog = true, editingEpisode = episode)
    }
    fun closeNewEpisodeDialog() {
        _uiState.value = _uiState.value.copy(showNewEpisodeDialog = false, editingEpisode = null)
    }

    fun openNewSeriesDialog(series: SeriesEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewSeriesDialog = true, editingSeries = series)
    }
    fun closeNewSeriesDialog() {
        _uiState.value = _uiState.value.copy(showNewSeriesDialog = false, editingSeries = null)
    }

    fun openNewSeasonDialog(season: SeasonEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewSeasonDialog = true, editingSeason = season)
    }
    fun closeNewSeasonDialog() {
        _uiState.value = _uiState.value.copy(showNewSeasonDialog = false, editingSeason = null)
    }

    fun setActiveSeason(season: SeasonEntity?) {
        _uiState.value = _uiState.value.copy(activeSeason = season)
    }

    fun setActiveSeries(series: SeriesEntity?) {
        _uiState.value = _uiState.value.copy(activeSeries = series)
    }

    fun setActiveEpisode(episode: EpisodeEntity?) {
        _uiState.value = _uiState.value.copy(activeEpisode = episode)
    }

    fun openNewSoundDialog(sound: SoundMusicEntity? = null) {
        _uiState.value = _uiState.value.copy(showNewSoundDialog = true, editingSound = sound)
    }
    fun closeNewSoundDialog() {
        _uiState.value = _uiState.value.copy(showNewSoundDialog = false, editingSound = null)
    }

    fun openUploadSoundDialog() {
        _uiState.value = _uiState.value.copy(showUploadSoundDialog = true)
    }
    fun closeUploadSoundDialog() {
        _uiState.value = _uiState.value.copy(showUploadSoundDialog = false)
    }

    fun openCreateSoundWithAiDialog() {
        _uiState.value = _uiState.value.copy(showCreateSoundWithAiDialog = true)
    }
    fun closeCreateSoundWithAiDialog() {
        _uiState.value = _uiState.value.copy(showCreateSoundWithAiDialog = false)
    }

    fun openAttachToSceneDialog(sound: SoundMusicEntity) {
        _uiState.value = _uiState.value.copy(showAttachToSceneDialog = sound)
    }
    fun closeAttachToSceneDialog() {
        _uiState.value = _uiState.value.copy(showAttachToSceneDialog = null)
    }

    fun openAttachToEpisodeDialog(sound: SoundMusicEntity) {
        _uiState.value = _uiState.value.copy(showAttachToEpisodeDialog = sound)
    }
    fun closeAttachToEpisodeDialog() {
        _uiState.value = _uiState.value.copy(showAttachToEpisodeDialog = null)
    }

    fun openAudioEditorDialog(sound: SoundMusicEntity) {
        _uiState.value = _uiState.value.copy(showAudioEditorDialog = sound)
    }
    fun closeAudioEditorDialog() {
        _uiState.value = _uiState.value.copy(showAudioEditorDialog = null)
    }

    fun setActiveSound(sound: SoundMusicEntity?) {
        _uiState.value = _uiState.value.copy(activeSound = sound)
    }

    // Data mutations
    fun saveProject(name: String, description: String, type: String, category: String, coverUri: String?, status: String = "Em produção") {
        viewModelScope.launch {
            val current = _uiState.value.editingProject
            if (current != null) {
                repository.updateProject(
                    current.copy(
                        name = name,
                        description = description,
                        type = type,
                        category = category,
                        coverUri = coverUri,
                        status = status,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Projeto atualizado",
                    description = "Projeto “$name” foi atualizado.",
                    iconType = "project"
                )
            } else {
                repository.insertProject(
                    ProjectEntity(
                        name = name,
                        description = description,
                        type = type,
                        category = category,
                        coverUri = coverUri,
                        status = status,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Projeto “$name” criado",
                    description = "Novo projeto do tipo $type adicionado.",
                    iconType = "project"
                )
            }
            closeNewProjectDialog()
        }
    }

    fun deleteProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.deleteProject(project)
            repository.logActivity(
                title = "Projeto excluído",
                description = "Projeto “${project.name}” foi removido.",
                iconType = "project"
            )
        }
    }

    fun duplicateProject(project: ProjectEntity) {
        viewModelScope.launch {
            val duplicate = project.copy(
                id = 0,
                name = "${project.name} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertProject(duplicate)
            repository.logActivity(
                title = "Projeto duplicado",
                description = "Projeto “${duplicate.name}” criado por duplicação.",
                iconType = "project"
            )
        }
    }

    fun archiveProject(project: ProjectEntity) {
        viewModelScope.launch {
            val updated = project.copy(
                status = if (project.status == "Arquivado") "Em produção" else "Arquivado",
                updatedAt = System.currentTimeMillis()
            )
            repository.updateProject(updated)
            repository.logActivity(
                title = if (updated.status == "Arquivado") "Projeto arquivado" else "Projeto desarquivado",
                description = "Status de “${project.name}” alterado para ${updated.status}.",
                iconType = "project"
            )
        }
    }

    fun updateProjectStatus(project: ProjectEntity, newStatus: String) {
        viewModelScope.launch {
            val updated = project.copy(
                status = newStatus,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateProject(updated)
        }
    }

    fun saveCharacter(
        name: String,
        personality: String,
        age: String,
        description: String,
        history: String = "",
        characterType: String = "Criança",
        voice: String = "Voz Masculina 1",
        role: String = "Protagonista",
        projectId: Long?,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            val current = _uiState.value.editingCharacter
            if (current != null) {
                repository.updateCharacter(
                    current.copy(
                        name = name,
                        personality = personality,
                        age = age,
                        description = description,
                        history = history,
                        characterType = characterType,
                        voice = voice,
                        role = role,
                        projectId = projectId,
                        imageUri = imageUri,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Personagem “$name” atualizado",
                    description = "Dados do personagem foram atualizados.",
                    iconType = "character"
                )
            } else {
                repository.insertCharacter(
                    CharacterEntity(
                        name = name,
                        personality = personality,
                        age = age,
                        description = description,
                        history = history,
                        characterType = characterType,
                        voice = voice,
                        role = role,
                        projectId = projectId,
                        imageUri = imageUri,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Personagem “$name” criado",
                    description = "Novo personagem ($characterType) adicionado ao projeto.",
                    iconType = "character"
                )
            }
            closeNewCharacterDialog()
        }
    }

    fun deleteCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            repository.deleteCharacter(character)
            repository.logActivity(
                title = "Personagem excluído",
                description = "Personagem “${character.name}” foi removido.",
                iconType = "character"
            )
        }
    }

    fun duplicateCharacter(character: CharacterEntity) {
        viewModelScope.launch {
            val duplicate = character.copy(
                id = 0,
                name = "${character.name} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertCharacter(duplicate)
            repository.logActivity(
                title = "Personagem duplicado",
                description = "Personagem “${duplicate.name}” criado por duplicação.",
                iconType = "character"
            )
        }
    }

    fun moveCharacterToProject(character: CharacterEntity, newProjectId: Long?) {
        viewModelScope.launch {
            repository.updateCharacter(
                character.copy(
                    projectId = newProjectId,
                    updatedAt = System.currentTimeMillis()
                )
            )
            repository.logActivity(
                title = "Personagem movido",
                description = "Personagem “${character.name}” transferido de projeto.",
                iconType = "character"
            )
        }
    }

    fun saveScenario(
        name: String,
        description: String,
        category: String = "Casa",
        visualStyle: String = "3D Cartoon",
        locationType: String = "Exterior",
        atmosphere: String = "Iluminação acolhedora",
        consistentArchitecture: String = "Estilo 3D cartoon coerente com cores quentes",
        versions: String = "Dia, Noite, Chuva, Pôr do sol, Vista frontal, Interior",
        projectId: Long?,
        imageUri: String? = null
    ) {
        viewModelScope.launch {
            val current = _uiState.value.editingScenario
            if (current != null) {
                repository.updateScenario(
                    current.copy(
                        name = name,
                        description = description,
                        category = category,
                        visualStyle = visualStyle,
                        locationType = locationType,
                        atmosphere = atmosphere,
                        consistentArchitecture = consistentArchitecture,
                        versions = versions,
                        projectId = projectId,
                        imageUri = imageUri,
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Cenário “$name” atualizado",
                    description = "Dados do cenário foram atualizados.",
                    iconType = "scenario"
                )
            } else {
                repository.insertScenario(
                    ScenarioEntity(
                        name = name,
                        description = description,
                        category = category,
                        visualStyle = visualStyle,
                        locationType = locationType,
                        atmosphere = atmosphere,
                        consistentArchitecture = consistentArchitecture,
                        versions = versions,
                        projectId = projectId,
                        imageUri = imageUri,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                repository.logActivity(
                    title = "Cenário “$name” criado",
                    description = "Novo cenário ($category) adicionado ao projeto.",
                    iconType = "scenario"
                )
            }
            closeNewScenarioDialog()
        }
    }

    fun deleteScenario(scenario: ScenarioEntity) {
        viewModelScope.launch {
            repository.deleteScenario(scenario)
            repository.logActivity(
                title = "Cenário excluído",
                description = "Cenário “${scenario.name}” foi removido.",
                iconType = "scenario"
            )
        }
    }

    fun duplicateScenario(scenario: ScenarioEntity) {
        viewModelScope.launch {
            val duplicate = scenario.copy(
                id = 0,
                name = "${scenario.name} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertScenario(duplicate)
            repository.logActivity(
                title = "Cenário duplicado",
                description = "Cenário “${duplicate.name}” criado por duplicação.",
                iconType = "scenario"
            )
        }
    }

    fun moveScenarioToProject(scenario: ScenarioEntity, newProjectId: Long?) {
        viewModelScope.launch {
            repository.updateScenario(
                scenario.copy(
                    projectId = newProjectId,
                    updatedAt = System.currentTimeMillis()
                )
            )
            repository.logActivity(
                title = "Cenário movido",
                description = "Cenário “${scenario.name}” transferido de projeto.",
                iconType = "scenario"
            )
        }
    }

    fun archiveScenario(scenario: ScenarioEntity) {
        viewModelScope.launch {
            val updated = scenario.copy(
                isArchived = !scenario.isArchived,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateScenario(updated)
            repository.logActivity(
                title = if (updated.isArchived) "Cenário arquivado" else "Cenário restaurado",
                description = "Cenário “${scenario.name}” foi ${if (updated.isArchived) "arquivado" else "restaurado"}.",
                iconType = "scenario"
            )
        }
    }

    fun saveFullSeries(series: SeriesEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (series.id > 0) {
                repository.updateSeries(series.copy(updatedAt = System.currentTimeMillis()))
                _uiState.value = _uiState.value.copy(
                    activeSeries = if (_uiState.value.activeSeries?.id == series.id) series else _uiState.value.activeSeries,
                    lastSeriesSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Série atualizada",
                        description = "Série “${series.title}” foi salva com sucesso.",
                        iconType = "series"
                    )
                }
            } else {
                val newId = repository.insertSeries(series.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
                val saved = series.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeSeries = saved,
                    lastSeriesSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Nova Série criada",
                        description = "Série “${series.title}” (${series.type}) adicionada ao estúdio.",
                        iconType = "series"
                    )
                }
            }
            closeNewSeriesDialog()
        }
    }

    fun duplicateSeries(series: SeriesEntity) {
        viewModelScope.launch {
            val duplicate = series.copy(
                id = 0,
                title = "${series.title} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val newId = repository.insertSeries(duplicate)
            repository.logActivity(
                title = "Série duplicada",
                description = "Cópia da série “${series.title}” criada.",
                iconType = "series"
            )
        }
    }

    fun archiveSeries(series: SeriesEntity) {
        viewModelScope.launch {
            val updated = series.copy(
                isArchived = !series.isArchived,
                status = if (!series.isArchived) "Arquivada" else "Em produção",
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSeries(updated)
            if (_uiState.value.activeSeries?.id == series.id) {
                _uiState.value = _uiState.value.copy(activeSeries = updated)
            }
            repository.logActivity(
                title = if (updated.isArchived) "Série arquivada" else "Série desarquivada",
                description = "Série “${series.title}” foi ${if (updated.isArchived) "arquivada" else "restaurada"}.",
                iconType = "series"
            )
        }
    }

    fun toggleFavoriteSeries(series: SeriesEntity) {
        viewModelScope.launch {
            val updated = series.copy(
                isFavorite = !series.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSeries(updated)
            if (_uiState.value.activeSeries?.id == series.id) {
                _uiState.value = _uiState.value.copy(activeSeries = updated)
            }
        }
    }

    fun saveSeries(projectId: Long, title: String, synopsis: String, genre: String, targetAudience: String) {
        saveFullSeries(
            SeriesEntity(
                projectId = projectId,
                title = title,
                synopsis = synopsis,
                genre = genre,
                targetAudience = targetAudience
            )
        )
    }

    fun deleteSeries(seriesEntity: SeriesEntity) {
        viewModelScope.launch {
            repository.deleteSeries(seriesEntity)
            if (_uiState.value.activeSeries?.id == seriesEntity.id) {
                _uiState.value = _uiState.value.copy(activeSeries = null)
            }
            repository.logActivity(
                title = "Série excluída",
                description = "Série “${seriesEntity.title}” foi removida do estúdio.",
                iconType = "series"
            )
        }
    }

    fun saveFullSeason(season: SeasonEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (season.id > 0) {
                repository.updateSeason(season.copy(updatedAt = System.currentTimeMillis()))
                _uiState.value = _uiState.value.copy(
                    activeSeason = if (_uiState.value.activeSeason?.id == season.id) season else _uiState.value.activeSeason,
                    lastSeasonSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Temporada atualizada",
                        description = "Temporada “${season.title}” foi salva com sucesso.",
                        iconType = "season"
                    )
                }
            } else {
                val newId = repository.insertSeason(season.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
                val saved = season.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeSeason = saved,
                    lastSeasonSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Nova Temporada criada",
                        description = "Temporada “${season.title}” adicionada à série.",
                        iconType = "season"
                    )
                }
            }
            closeNewSeasonDialog()
        }
    }

    fun duplicateSeason(season: SeasonEntity) {
        viewModelScope.launch {
            val duplicate = season.copy(
                id = 0,
                seasonNumber = season.seasonNumber + 1,
                title = "${season.title} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val newId = repository.insertSeason(duplicate)
            repository.logActivity(
                title = "Temporada duplicada",
                description = "Cópia da temporada “${season.title}” criada.",
                iconType = "season"
            )
        }
    }

    fun archiveSeason(season: SeasonEntity) {
        viewModelScope.launch {
            val updated = season.copy(
                isArchived = !season.isArchived,
                status = if (!season.isArchived) "Arquivada" else "Em produção",
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSeason(updated)
            if (_uiState.value.activeSeason?.id == season.id) {
                _uiState.value = _uiState.value.copy(activeSeason = updated)
            }
            repository.logActivity(
                title = if (updated.isArchived) "Temporada arquivada" else "Temporada restaurada",
                description = "Temporada “${season.title}” foi ${if (updated.isArchived) "arquivada" else "restaurada"}.",
                iconType = "season"
            )
        }
    }

    fun toggleFavoriteSeason(season: SeasonEntity) {
        viewModelScope.launch {
            val updated = season.copy(
                isFavorite = !season.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSeason(updated)
            if (_uiState.value.activeSeason?.id == season.id) {
                _uiState.value = _uiState.value.copy(activeSeason = updated)
            }
        }
    }

    fun saveSeason(seriesId: Long, seasonNumber: Int, title: String, synopsis: String) {
        saveFullSeason(
            SeasonEntity(
                seriesId = seriesId,
                seasonNumber = seasonNumber,
                title = title,
                synopsis = synopsis
            )
        )
    }

    fun deleteSeason(seasonEntity: SeasonEntity) {
        viewModelScope.launch {
            repository.deleteSeason(seasonEntity)
            if (_uiState.value.activeSeason?.id == seasonEntity.id) {
                _uiState.value = _uiState.value.copy(activeSeason = null)
            }
            repository.logActivity(
                title = "Temporada excluída",
                description = "Temporada “${seasonEntity.title}” foi removida do estúdio.",
                iconType = "season"
            )
        }
    }

    fun saveFullEpisode(episode: EpisodeEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (episode.id > 0) {
                repository.updateEpisode(episode.copy(updatedAt = System.currentTimeMillis()))
                if (_uiState.value.activeEpisode?.id == episode.id) {
                    _uiState.value = _uiState.value.copy(
                        activeEpisode = episode,
                        lastEpisodeSaveTimestamp = System.currentTimeMillis()
                    )
                }
                if (logActivity) {
                    repository.logActivity(
                        title = "Episódio atualizado",
                        description = "Episódio “${episode.title}” foi salvo.",
                        iconType = "episode"
                    )
                }
            } else {
                val newId = repository.insertEpisode(episode.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
                val saved = episode.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeEpisode = saved,
                    lastEpisodeSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Novo Episódio adicionado",
                        description = "Episódio “${episode.title}” adicionado à produção.",
                        iconType = "episode"
                    )
                }
            }
            closeNewEpisodeDialog()
        }
    }

    fun duplicateEpisode(episode: EpisodeEntity) {
        viewModelScope.launch {
            val duplicate = episode.copy(
                id = 0,
                episodeNumber = episode.episodeNumber + 1,
                title = "${episode.title} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val newId = repository.insertEpisode(duplicate)
            repository.logActivity(
                title = "Episódio duplicado",
                description = "Cópia do episódio “${episode.title}” criada.",
                iconType = "episode"
            )
        }
    }

    fun archiveEpisode(episode: EpisodeEntity) {
        viewModelScope.launch {
            val updatedStatus = if (episode.status == "Arquivado") "Em produção" else "Arquivado"
            val updated = episode.copy(
                status = updatedStatus,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateEpisode(updated)
            if (_uiState.value.activeEpisode?.id == episode.id) {
                _uiState.value = _uiState.value.copy(activeEpisode = updated)
            }
            repository.logActivity(
                title = if (updatedStatus == "Arquivado") "Episódio arquivado" else "Episódio restaurado",
                description = "Episódio “${episode.title}” foi ${if (updatedStatus == "Arquivado") "arquivado" else "restaurado"}.",
                iconType = "episode"
            )
        }
    }

    fun deleteEpisode(episodeEntity: EpisodeEntity) {
        viewModelScope.launch {
            repository.deleteEpisode(episodeEntity)
            if (_uiState.value.activeEpisode?.id == episodeEntity.id) {
                _uiState.value = _uiState.value.copy(activeEpisode = null)
            }
            repository.logActivity(
                title = "Episódio excluído",
                description = "Episódio “${episodeEntity.title}” foi removido.",
                iconType = "episode"
            )
        }
    }

    fun saveEpisode(
        projectId: Long,
        seasonId: Long,
        episodeNumber: Int,
        title: String,
        description: String,
        duration: String,
        status: String
    ) {
        viewModelScope.launch {
            val current = _uiState.value.editingEpisode
            if (current != null) {
                repository.updateEpisode(
                    current.copy(
                        projectId = projectId,
                        seasonId = seasonId,
                        episodeNumber = episodeNumber,
                        title = title,
                        description = description,
                        duration = duration,
                        status = status
                    )
                )
            } else {
                repository.insertEpisode(
                    EpisodeEntity(
                        projectId = projectId,
                        seasonId = seasonId,
                        episodeNumber = episodeNumber,
                        title = title,
                        description = description,
                        duration = duration,
                        status = status
                    )
                )
            }
            closeNewEpisodeDialog()
        }
    }

    fun openNewSceneDialog(scene: SceneEntity? = null) {
        _uiState.value = _uiState.value.copy(
            showNewSceneDialog = true,
            editingScene = scene
        )
    }

    fun closeNewSceneDialog() {
        _uiState.value = _uiState.value.copy(
            showNewSceneDialog = false,
            editingScene = null
        )
    }

    fun setActiveScene(scene: SceneEntity?) {
        _uiState.value = _uiState.value.copy(
            activeScene = scene,
            isSceneStudioMode = scene != null
        )
    }

    fun setSceneStudioMode(isStudioMode: Boolean) {
        _uiState.value = _uiState.value.copy(isSceneStudioMode = isStudioMode)
    }

    fun saveFullScene(scene: SceneEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (scene.id > 0) {
                repository.updateScene(scene.copy(updatedAt = System.currentTimeMillis()))
                _uiState.value = _uiState.value.copy(
                    activeScene = scene,
                    lastSceneSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    val statusText = if (scene.status == "Concluída") "concluída" else "atualizada"
                    repository.logActivity(
                        title = "Cena ${scene.sceneOrder} $statusText",
                        description = "Cena “${scene.name}” foi salva com sucesso.",
                        iconType = "scene"
                    )
                }
            } else {
                val newId = repository.insertScene(scene.copy(createdAt = System.currentTimeMillis(), updatedAt = System.currentTimeMillis()))
                val savedScene = scene.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeScene = savedScene,
                    lastSceneSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Cena ${scene.sceneOrder} criada",
                        description = "Cena “${scene.name}” adicionada à produção.",
                        iconType = "scene"
                    )
                }
            }
            closeNewSceneDialog()
        }
    }

    fun duplicateScene(scene: SceneEntity) {
        viewModelScope.launch {
            val duplicate = scene.copy(
                id = 0,
                sceneOrder = scene.sceneOrder + 1,
                name = "${scene.name} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertScene(duplicate)
            repository.logActivity(
                title = "Cena duplicada",
                description = "Cópia da Cena ${scene.sceneOrder} criada.",
                iconType = "scene"
            )
        }
    }

    fun moveSceneToProject(scene: SceneEntity, newProjectId: Long) {
        viewModelScope.launch {
            repository.updateScene(
                scene.copy(
                    projectId = newProjectId,
                    updatedAt = System.currentTimeMillis()
                )
            )
            repository.logActivity(
                title = "Cena movida",
                description = "Cena “${scene.name}” movida para outro projeto.",
                iconType = "scene"
            )
        }
    }

    fun archiveScene(scene: SceneEntity) {
        viewModelScope.launch {
            val updated = scene.copy(
                isArchived = !scene.isArchived,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateScene(updated)
            repository.logActivity(
                title = if (updated.isArchived) "Cena arquivada" else "Cena restaurada",
                description = "Cena “${scene.name}” foi ${if (updated.isArchived) "arquivada" else "restaurada"}.",
                iconType = "scene"
            )
        }
    }

    fun deleteScene(scene: SceneEntity) {
        viewModelScope.launch {
            repository.deleteScene(scene)
            if (_uiState.value.activeScene?.id == scene.id) {
                _uiState.value = _uiState.value.copy(activeScene = null)
            }
            repository.logActivity(
                title = "Cena excluída",
                description = "Cena “${scene.name}” foi removida.",
                iconType = "scene"
            )
        }
    }

    fun openNewVoiceDialog(voice: VoiceEntity? = null) {
        _uiState.value = _uiState.value.copy(
            showNewVoiceDialog = true,
            editingVoice = voice
        )
    }

    fun closeNewVoiceDialog() {
        _uiState.value = _uiState.value.copy(
            showNewVoiceDialog = false,
            editingVoice = null
        )
    }

    fun setActiveVoice(voice: VoiceEntity?) {
        _uiState.value = _uiState.value.copy(activeVoice = voice)
    }

    fun saveFullVoice(voice: VoiceEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (voice.id > 0) {
                repository.updateVoice(voice.copy(updatedAt = System.currentTimeMillis()))
                if (_uiState.value.activeVoice?.id == voice.id) {
                    _uiState.value = _uiState.value.copy(
                        activeVoice = voice,
                        lastVoiceSaveTimestamp = System.currentTimeMillis()
                    )
                }
                if (logActivity) {
                    repository.logActivity(
                        title = "Voz atualizada",
                        description = "Voz “${voice.name}” (${voice.style}) foi atualizada.",
                        iconType = "voice"
                    )
                }
            } else {
                val newId = repository.insertVoice(
                    voice.copy(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                val saved = voice.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeVoice = saved,
                    lastVoiceSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Nova voz criada",
                        description = "Voz “${voice.name}” adicionada à biblioteca de vozes.",
                        iconType = "voice"
                    )
                }
            }
            closeNewVoiceDialog()
        }
    }

    fun duplicateVoice(voice: VoiceEntity) {
        viewModelScope.launch {
            val duplicate = voice.copy(
                id = 0,
                name = "${voice.name} (Cópia)",
                statusTag = "Salva",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            repository.insertVoice(duplicate)
            repository.logActivity(
                title = "Voz duplicada",
                description = "Cópia da voz “${voice.name}” criada com sucesso.",
                iconType = "voice"
            )
        }
    }

    fun toggleFavoriteVoice(voice: VoiceEntity) {
        viewModelScope.launch {
            val updated = voice.copy(
                isFavorite = !voice.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateVoice(updated)
            if (_uiState.value.activeVoice?.id == voice.id) {
                _uiState.value = _uiState.value.copy(activeVoice = updated)
            }
            repository.logActivity(
                title = if (updated.isFavorite) "Voz favoritada" else "Voz desfavoritada",
                description = "Voz “${voice.name}” foi ${if (updated.isFavorite) "adicionada aos favoritos" else "removida dos favoritos"}.",
                iconType = "voice"
            )
        }
    }

    fun archiveVoice(voice: VoiceEntity) {
        viewModelScope.launch {
            val updated = voice.copy(
                isArchived = !voice.isArchived,
                statusTag = if (!voice.isArchived) "Arquivada" else "Salva",
                updatedAt = System.currentTimeMillis()
            )
            repository.updateVoice(updated)
            if (_uiState.value.activeVoice?.id == voice.id) {
                _uiState.value = _uiState.value.copy(activeVoice = updated)
            }
            repository.logActivity(
                title = if (updated.isArchived) "Voz arquivada" else "Voz restaurada",
                description = "Voz “${voice.name}” foi ${if (updated.isArchived) "arquivada" else "restaurada"}.",
                iconType = "voice"
            )
        }
    }

    fun associateVoiceToCharacter(voice: VoiceEntity, characterName: String, characterEmoji: String?, characterId: Long?) {
        viewModelScope.launch {
            val updated = voice.copy(
                assignedCharacter = characterName,
                characterEmoji = characterEmoji,
                characterId = characterId,
                statusTag = "Em uso",
                updatedAt = System.currentTimeMillis()
            )
            repository.updateVoice(updated)
            if (_uiState.value.activeVoice?.id == voice.id) {
                _uiState.value = _uiState.value.copy(activeVoice = updated)
            }
            repository.logActivity(
                title = "Voz associada",
                description = "Voz “${voice.name}” foi associada ao personagem $characterName.",
                iconType = "voice"
            )
        }
    }

    fun associateVoiceToProject(voice: VoiceEntity, projectId: Long, projectName: String) {
        viewModelScope.launch {
            val updated = voice.copy(
                projectId = projectId,
                projectName = projectName,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateVoice(updated)
            if (_uiState.value.activeVoice?.id == voice.id) {
                _uiState.value = _uiState.value.copy(activeVoice = updated)
            }
            repository.logActivity(
                title = "Voz vinculada ao projeto",
                description = "Voz “${voice.name}” vinculada ao projeto “$projectName”.",
                iconType = "voice"
            )
        }
    }

    fun updateVoiceTuning(voiceId: Long, speed: Float, pitch: Float, expressiveness: Float, sampleText: String) {
        viewModelScope.launch {
            val voice = _uiState.value.activeVoice ?: return@launch
            if (voice.id == voiceId) {
                val updated = voice.copy(
                    speed = speed,
                    pitch = pitch,
                    expressiveness = expressiveness,
                    sampleText = sampleText,
                    updatedAt = System.currentTimeMillis()
                )
                repository.updateVoice(updated)
                _uiState.value = _uiState.value.copy(
                    activeVoice = updated,
                    lastVoiceSaveTimestamp = System.currentTimeMillis()
                )
            }
        }
    }

    fun saveVoice(name: String, type: String, tone: String, language: String, sampleAudioDesc: String, assignedCharacter: String?) {
        viewModelScope.launch {
            repository.insertVoice(
                VoiceEntity(
                    name = name,
                    type = type,
                    tone = tone,
                    language = language,
                    sampleAudioDesc = sampleAudioDesc,
                    assignedCharacter = assignedCharacter
                )
            )
            closeNewVoiceDialog()
        }
    }

    fun deleteVoice(voice: VoiceEntity) {
        viewModelScope.launch {
            repository.deleteVoice(voice)
            if (_uiState.value.activeVoice?.id == voice.id) {
                _uiState.value = _uiState.value.copy(activeVoice = null)
            }
            repository.logActivity(
                title = "Voz excluída",
                description = "Voz “${voice.name}” foi excluída da biblioteca.",
                iconType = "voice"
            )
        }
    }

    fun saveFullSound(sound: SoundMusicEntity, logActivity: Boolean = true) {
        viewModelScope.launch {
            if (sound.id > 0) {
                repository.updateSound(sound.copy(updatedAt = System.currentTimeMillis()))
                if (_uiState.value.activeSound?.id == sound.id) {
                    _uiState.value = _uiState.value.copy(
                        activeSound = sound,
                        lastSoundSaveTimestamp = System.currentTimeMillis()
                    )
                }
                if (logActivity) {
                    repository.logActivity(
                        title = "Áudio atualizado",
                        description = "Áudio “${sound.title}” (${sound.category}) foi atualizado.",
                        iconType = "music"
                    )
                }
            } else {
                val newId = repository.insertSound(
                    sound.copy(
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                )
                val saved = sound.copy(id = newId)
                _uiState.value = _uiState.value.copy(
                    activeSound = saved,
                    lastSoundSaveTimestamp = System.currentTimeMillis()
                )
                if (logActivity) {
                    repository.logActivity(
                        title = "Novo áudio adicionado",
                        description = "Áudio “${sound.title}” adicionado à biblioteca de sons.",
                        iconType = "music"
                    )
                }
            }
            closeNewSoundDialog()
            closeUploadSoundDialog()
            closeCreateSoundWithAiDialog()
        }
    }

    fun duplicateSound(sound: SoundMusicEntity) {
        viewModelScope.launch {
            val duplicate = sound.copy(
                id = 0,
                title = "${sound.title} (Cópia)",
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            val newId = repository.insertSound(duplicate)
            repository.logActivity(
                title = "Áudio duplicado",
                description = "Cópia do áudio “${sound.title}” criada com sucesso.",
                iconType = "music"
            )
        }
    }

    fun toggleFavoriteSound(sound: SoundMusicEntity) {
        viewModelScope.launch {
            val updated = sound.copy(
                isFavorite = !sound.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSound(updated)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = updated)
            }
            repository.logActivity(
                title = if (updated.isFavorite) "Áudio favoritado" else "Áudio desfavoritado",
                description = "Áudio “${sound.title}” foi ${if (updated.isFavorite) "adicionado aos favoritos" else "removido dos favoritos"}.",
                iconType = "music"
            )
        }
    }

    fun archiveSound(sound: SoundMusicEntity) {
        viewModelScope.launch {
            val updated = sound.copy(
                isArchived = !sound.isArchived,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSound(updated)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = updated)
            }
            repository.logActivity(
                title = if (updated.isArchived) "Áudio arquivado" else "Áudio restaurado",
                description = "Áudio “${sound.title}” foi ${if (updated.isArchived) "arquivado" else "restaurado"}.",
                iconType = "music"
            )
        }
    }

    fun associateSoundToProject(sound: SoundMusicEntity, projectId: Long, projectName: String) {
        viewModelScope.launch {
            val updated = sound.copy(
                projectId = projectId,
                projectName = projectName,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSound(updated)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = updated)
            }
            repository.logActivity(
                title = "Áudio vinculado ao projeto",
                description = "Áudio “${sound.title}” vinculado ao projeto “$projectName”.",
                iconType = "music"
            )
        }
    }

    fun associateSoundToScene(
        sound: SoundMusicEntity,
        sceneId: Long,
        sceneName: String,
        episodeId: Long?,
        seriesId: Long?,
        projectId: Long?
    ) {
        viewModelScope.launch {
            val updated = sound.copy(
                sceneId = sceneId,
                sceneName = sceneName,
                episodeId = episodeId ?: sound.episodeId,
                seriesId = seriesId ?: sound.seriesId,
                projectId = projectId ?: sound.projectId,
                usageScenesCount = sound.usageScenesCount + 1,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSound(updated)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = updated)
            }
            repository.logActivity(
                title = "Efeito associado à cena",
                description = "Áudio “${sound.title}” associado à $sceneName.",
                iconType = "scene"
            )
            closeAttachToSceneDialog()
        }
    }

    fun associateSoundToEpisode(
        sound: SoundMusicEntity,
        episodeId: Long,
        episodeName: String,
        bgVolume: Float,
        fadeIn: Boolean,
        fadeOut: Boolean
    ) {
        viewModelScope.launch {
            val updated = sound.copy(
                episodeId = episodeId,
                episodeName = episodeName,
                isBackgroundMusic = true,
                bgVolume = bgVolume,
                bgFadeIn = fadeIn,
                bgFadeOut = fadeOut,
                usageEpisodesCount = sound.usageEpisodesCount + 1,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateSound(updated)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = updated)
            }
            repository.logActivity(
                title = "Trilha associada ao episódio",
                description = "Áudio “${sound.title}” definido como música de fundo no episódio $episodeName.",
                iconType = "music"
            )
            closeAttachToEpisodeDialog()
        }
    }

    fun updateAudioEditing(
        soundId: Long,
        newDuration: String,
        durationSecs: Int,
        volume: Float,
        fadeIn: Boolean,
        fadeOut: Boolean,
        asNewFile: Boolean,
        newTitle: String?
    ) {
        viewModelScope.launch {
            val sound = _uiState.value.activeSound ?: return@launch
            if (sound.id == soundId) {
                if (asNewFile) {
                    val newSound = sound.copy(
                        id = 0,
                        title = newTitle ?: "${sound.title} (Editado)",
                        duration = newDuration,
                        durationSeconds = durationSecs,
                        bgVolume = volume,
                        bgFadeIn = fadeIn,
                        bgFadeOut = fadeOut,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val newId = repository.insertSound(newSound)
                    repository.logActivity(
                        title = "Novo áudio editado",
                        description = "Versão editada “${newSound.title}” criada.",
                        iconType = "music"
                    )
                } else {
                    val updated = sound.copy(
                        duration = newDuration,
                        durationSeconds = durationSecs,
                        bgVolume = volume,
                        bgFadeIn = fadeIn,
                        bgFadeOut = fadeOut,
                        updatedAt = System.currentTimeMillis()
                    )
                    repository.updateSound(updated)
                    _uiState.value = _uiState.value.copy(
                        activeSound = updated,
                        lastSoundSaveTimestamp = System.currentTimeMillis()
                    )
                    repository.logActivity(
                        title = "Áudio editado",
                        description = "Cortes e ajustes aplicados a “${sound.title}”.",
                        iconType = "music"
                    )
                }
                closeAudioEditorDialog()
            }
        }
    }

    fun saveSound(projectId: Long?, title: String, category: String, mood: String, duration: String, description: String) {
        viewModelScope.launch {
            repository.insertSound(
                SoundMusicEntity(
                    projectId = projectId,
                    title = title,
                    category = category,
                    mood = mood,
                    duration = duration,
                    description = description
                )
            )
            closeNewSoundDialog()
        }
    }

    fun deleteSound(sound: SoundMusicEntity) {
        viewModelScope.launch {
            repository.deleteSound(sound)
            if (_uiState.value.activeSound?.id == sound.id) {
                _uiState.value = _uiState.value.copy(activeSound = null)
            }
            repository.logActivity(
                title = "Áudio excluído",
                description = "Áudio “${sound.title}” foi excluído da biblioteca.",
                iconType = "music"
            )
        }
    }

    // ==========================================
    // CRIAÇÃO RANGA & ROTEIRO BRANCO ACTIONS
    // ==========================================
    fun setActiveCreation(creation: RangaCreationEntity?) {
        _uiState.value = _uiState.value.copy(
            activeCreation = creation,
            lastCreationSaveTimestamp = System.currentTimeMillis()
        )
    }

    fun openCreationDetailsDialog(creation: RangaCreationEntity) {
        _uiState.value = _uiState.value.copy(showCreationDetailsDialog = creation)
    }

    fun closeCreationDetailsDialog() {
        _uiState.value = _uiState.value.copy(showCreationDetailsDialog = null)
    }

    fun openUseAsCharacterDialog(creation: RangaCreationEntity) {
        _uiState.value = _uiState.value.copy(showUseAsCharacterDialog = creation)
    }

    fun closeUseAsCharacterDialog() {
        _uiState.value = _uiState.value.copy(showUseAsCharacterDialog = null)
    }

    fun openUseAsScenarioDialog(creation: RangaCreationEntity) {
        _uiState.value = _uiState.value.copy(showUseAsScenarioDialog = creation)
    }

    fun closeUseAsScenarioDialog() {
        _uiState.value = _uiState.value.copy(showUseAsScenarioDialog = null)
    }

    fun openAddToRoteiroDialog(creation: RangaCreationEntity) {
        _uiState.value = _uiState.value.copy(showAddToRoteiroDialog = creation)
    }

    fun closeAddToRoteiroDialog() {
        _uiState.value = _uiState.value.copy(showAddToRoteiroDialog = null)
    }

    fun openEditCreationPromptDialog(creation: RangaCreationEntity) {
        _uiState.value = _uiState.value.copy(showEditCreationPromptDialog = creation)
    }

    fun closeEditCreationPromptDialog() {
        _uiState.value = _uiState.value.copy(showEditCreationPromptDialog = null)
    }

    fun saveCreation(creation: RangaCreationEntity) {
        viewModelScope.launch {
            val id = repository.insertCreation(creation)
            _uiState.value = _uiState.value.copy(
                activeCreation = creation.copy(id = id),
                lastCreationSaveTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateCreation(creation: RangaCreationEntity) {
        viewModelScope.launch {
            repository.updateCreation(creation)
            _uiState.value = _uiState.value.copy(
                activeCreation = creation,
                lastCreationSaveTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun deleteCreation(creation: RangaCreationEntity) {
        viewModelScope.launch {
            repository.deleteCreation(creation)
            if (_uiState.value.activeCreation?.id == creation.id) {
                _uiState.value = _uiState.value.copy(activeCreation = null)
            }
        }
    }

    fun toggleFavoriteCreation(creation: RangaCreationEntity) {
        viewModelScope.launch {
            val updated = creation.copy(
                isFavorite = !creation.isFavorite,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateCreation(updated)
            if (_uiState.value.activeCreation?.id == creation.id) {
                _uiState.value = _uiState.value.copy(activeCreation = updated)
            }
        }
    }

    fun toggleRoteiroBranco(creation: RangaCreationEntity) {
        viewModelScope.launch {
            val nextState = !creation.isInRoteiroBranco
            val updated = creation.copy(
                isInRoteiroBranco = nextState,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateCreation(updated)
            if (nextState) {
                repository.insertRoteiroBrancoItem(
                    RoteiroBrancoItemEntity(
                        creationId = creation.id,
                        title = creation.title,
                        type = creation.creationType,
                        imageUri = creation.imageUri,
                        notes = "Adicionado a partir da Criação RANGA (${creation.style}).",
                        projectId = creation.projectId,
                        projectName = creation.projectName
                    )
                )
            }
            if (_uiState.value.activeCreation?.id == creation.id) {
                _uiState.value = _uiState.value.copy(activeCreation = updated)
            }
        }
    }

    fun useCreationAsCharacter(
        creation: RangaCreationEntity,
        name: String,
        role: String,
        personality: String,
        projectId: Long?
    ) {
        viewModelScope.launch {
            repository.insertCharacter(
                CharacterEntity(
                    projectId = projectId ?: creation.projectId,
                    name = name.ifBlank { creation.title },
                    imageUri = creation.imageUri,
                    personality = personality.ifBlank { "Carismático e determinado" },
                    description = creation.prompt,
                    characterType = creation.creationType,
                    role = role.ifBlank { "Protagonista" }
                )
            )
            repository.logActivity(
                title = "Novo Personagem da Criação",
                description = "Personagem '$name' gerado a partir da imagem '${creation.title}'.",
                iconType = "character"
            )
            closeUseAsCharacterDialog()
        }
    }

    fun useCreationAsScenario(
        creation: RangaCreationEntity,
        name: String,
        category: String,
        atmosphere: String,
        projectId: Long?
    ) {
        viewModelScope.launch {
            repository.insertScenario(
                ScenarioEntity(
                    projectId = projectId ?: creation.projectId,
                    name = name.ifBlank { creation.title },
                    imageUri = creation.imageUri,
                    category = category.ifBlank { "Casa" },
                    atmosphere = atmosphere.ifBlank { "Acolhedora e mágica" },
                    description = creation.prompt,
                    visualStyle = creation.style
                )
            )
            repository.logActivity(
                title = "Novo Cenário da Criação",
                description = "Cenário '$name' gerado a partir da imagem '${creation.title}'.",
                iconType = "scenario"
            )
            closeUseAsScenarioDialog()
        }
    }

    fun addToRoteiroBranco(creation: RangaCreationEntity, notes: String) {
        viewModelScope.launch {
            repository.insertRoteiroBrancoItem(
                RoteiroBrancoItemEntity(
                    creationId = creation.id,
                    title = creation.title,
                    type = creation.creationType,
                    imageUri = creation.imageUri,
                    notes = notes.ifBlank { "Adicionado a partir da Criação RANGA." },
                    projectId = creation.projectId,
                    projectName = creation.projectName
                )
            )
            val updated = creation.copy(isInRoteiroBranco = true)
            repository.updateCreation(updated)
            if (_uiState.value.activeCreation?.id == creation.id) {
                _uiState.value = _uiState.value.copy(activeCreation = updated)
            }
            closeAddToRoteiroDialog()
        }
    }

    fun deleteRoteiroBrancoItem(item: RoteiroBrancoItemEntity) {
        viewModelScope.launch {
            repository.deleteRoteiroBrancoItem(item)
        }
    }

    // ==========================================
    // EXPORT COMPONENT ACTIONS
    // ==========================================

    fun saveExportRecord(exportRecord: ExportRecordEntity) {
        viewModelScope.launch {
            repository.insertExport(exportRecord)
        }
    }

    fun updateExportRecord(exportRecord: ExportRecordEntity) {
        viewModelScope.launch {
            repository.updateExport(exportRecord)
        }
    }

    fun deleteExportRecord(exportRecord: ExportRecordEntity) {
        viewModelScope.launch {
            repository.deleteExport(exportRecord)
        }
    }

    fun retryExportRecord(exportRecord: ExportRecordEntity) {
        viewModelScope.launch {
            val retried = exportRecord.copy(
                status = "Processando",
                dateDisplay = "Agora"
            )
            repository.updateExport(retried)
        }
    }

    // ==========================================
    // SETTINGS COMPONENT ACTIONS
    // ==========================================

    fun updateUserProfile(name: String, email: String, avatarColorHex: String) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                userProfile = _settingsState.value.userProfile.copy(
                    name = name,
                    email = email,
                    avatarColorHex = avatarColorHex,
                    avatarInitials = name.take(1).uppercase().ifBlank { "A" }
                ),
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateAppearance(themeMode: String, primaryColorHex: String, uiSize: String, density: String) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                appearance = AppearanceSettings(
                    themeMode = themeMode,
                    primaryColorHex = primaryColorHex,
                    uiSize = uiSize,
                    density = density
                ),
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateLanguage(language: String) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                language = _settingsState.value.language.copy(selectedLanguage = language),
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateRangaCreationSettings(settings: RangaCreationSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                rangaCreation = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateVoiceSettings(settings: VoiceSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                voices = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateAudioSettings(settings: AudioSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                audio = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateProjectsProductionSettings(settings: ProjectsProductionSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                projectsProduction = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateExportSettings(settings: ExportDefaultSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                exportDefaults = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun updateNotificationSettings(settings: NotificationSettings) {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                notifications = settings,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun saveApiKey(serviceName: String, apiKey: String) {
        viewModelScope.launch {
            val masked = if (apiKey.length > 4) "••••••••••••${apiKey.takeLast(4).uppercase()}" else "••••••••••••"
            val currentList = _settingsState.value.apiKeys.toMutableList()
            val existingIndex = currentList.indexOfFirst { it.serviceName.equals(serviceName, ignoreCase = true) }
            val newItem = ApiKeyItem(
                id = if (existingIndex >= 0) currentList[existingIndex].id else (currentList.size + 1).toString(),
                serviceName = serviceName,
                apiKey = apiKey,
                maskedKey = masked,
                status = "Conectado",
                description = "Chave configurada e autenticada com sucesso"
            )
            if (existingIndex >= 0) {
                currentList[existingIndex] = newItem
            } else {
                currentList.add(newItem)
            }
            _settingsState.value = _settingsState.value.copy(
                apiKeys = currentList,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun removeApiKey(id: String) {
        viewModelScope.launch {
            val currentList = _settingsState.value.apiKeys.map {
                if (it.id == id) it.copy(apiKey = "", maskedKey = "••••••••••••", status = "Configuração necessária")
                else it
            }
            _settingsState.value = _settingsState.value.copy(
                apiKeys = currentList,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun logoutOtherSessions() {
        viewModelScope.launch {
            val currentOnly = _settingsState.value.sessions.filter { it.isCurrent }
            _settingsState.value = _settingsState.value.copy(
                sessions = currentOnly,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun cleanTempFiles() {
        viewModelScope.launch {
            val updatedStorage = _settingsState.value.storage.copy(
                tempFilesMb = 0.0,
                usedGb = 46.4,
                percentageUsed = 46
            )
            _settingsState.value = _settingsState.value.copy(
                storage = updatedStorage,
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun syncNow() {
        viewModelScope.launch {
            _settingsState.value = _settingsState.value.copy(
                sync = _settingsState.value.sync.copy(
                    status = "Sincronizando...",
                    lastSyncTime = "Agora"
                )
            )
            kotlinx.coroutines.delay(1200)
            _settingsState.value = _settingsState.value.copy(
                sync = SyncInfo(
                    lastSyncTime = "Agora mesmo",
                    status = "Sincronizado",
                    cloudStorageConnected = true
                ),
                lastSavedTimestamp = System.currentTimeMillis()
            )
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            _settingsState.value = StudioFullSettings(
                userProfile = UserProfileSettings(name = "Usuário", email = "", plan = "Gratuito")
            )
        }
    }
}

