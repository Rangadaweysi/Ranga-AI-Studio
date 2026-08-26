package com.example.data.repository

import com.example.data.local.StudioDao
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ExportRecordEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.RangaCreationEntity
import com.example.data.model.RoteiroBrancoItemEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.data.model.VoiceEntity
import kotlinx.coroutines.flow.Flow

class StudioRepository(private val dao: StudioDao) {
    // Projects
    val allProjects: Flow<List<ProjectEntity>> = dao.getAllProjects()
    val projectCount: Flow<Int> = dao.getProjectCount()
    suspend fun getProjectById(id: Long) = dao.getProjectById(id)
    suspend fun insertProject(project: ProjectEntity): Long {
        val id = dao.insertProject(project)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Novo Projeto",
                description = "Projeto '${project.name}' (${project.type}) criado no estúdio.",
                iconType = "project"
            )
        )
        return id
    }
    suspend fun updateProject(project: ProjectEntity) = dao.updateProject(project)
    suspend fun deleteProject(project: ProjectEntity) {
        dao.deleteProject(project)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Projeto Removido",
                description = "Projeto '${project.name}' foi excluído.",
                iconType = "delete"
            )
        )
    }

    // Characters
    val allCharacters: Flow<List<CharacterEntity>> = dao.getAllCharacters()
    val characterCount: Flow<Int> = dao.getCharacterCount()
    fun getCharactersByProject(projectId: Long): Flow<List<CharacterEntity>> = dao.getCharactersByProject(projectId)
    suspend fun insertCharacter(character: CharacterEntity): Long {
        val id = dao.insertCharacter(character)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Personagem Criado",
                description = "Personagem '${character.name}' adicionado ao elenco.",
                iconType = "character"
            )
        )
        return id
    }
    suspend fun updateCharacter(character: CharacterEntity) = dao.updateCharacter(character)
    suspend fun deleteCharacter(character: CharacterEntity) = dao.deleteCharacter(character)

    // Scenarios
    val allScenarios: Flow<List<ScenarioEntity>> = dao.getAllScenarios()
    val scenarioCount: Flow<Int> = dao.getScenarioCount()
    fun getScenariosByProject(projectId: Long): Flow<List<ScenarioEntity>> = dao.getScenariosByProject(projectId)
    suspend fun insertScenario(scenario: ScenarioEntity): Long {
        val id = dao.insertScenario(scenario)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Cenário Adicionado",
                description = "Cenário '${scenario.name}' cadastrado.",
                iconType = "scenario"
            )
        )
        return id
    }
    suspend fun updateScenario(scenario: ScenarioEntity) = dao.updateScenario(scenario)
    suspend fun deleteScenario(scenario: ScenarioEntity) = dao.deleteScenario(scenario)

    // Series
    val allSeries: Flow<List<SeriesEntity>> = dao.getAllSeries()
    fun getSeriesByProject(projectId: Long): Flow<List<SeriesEntity>> = dao.getSeriesByProject(projectId)
    suspend fun insertSeries(series: SeriesEntity) = dao.insertSeries(series)
    suspend fun updateSeries(series: SeriesEntity) = dao.updateSeries(series)
    suspend fun deleteSeries(series: SeriesEntity) = dao.deleteSeries(series)

    // Seasons
    val allSeasons: Flow<List<SeasonEntity>> = dao.getAllSeasons()
    fun getSeasonsBySeries(seriesId: Long): Flow<List<SeasonEntity>> = dao.getSeasonsBySeries(seriesId)
    suspend fun insertSeason(season: SeasonEntity) = dao.insertSeason(season)
    suspend fun updateSeason(season: SeasonEntity) = dao.updateSeason(season)
    suspend fun deleteSeason(season: SeasonEntity) = dao.deleteSeason(season)

    // Episodes
    val allEpisodes: Flow<List<EpisodeEntity>> = dao.getAllEpisodes()
    val episodeCount: Flow<Int> = dao.getEpisodeCount()
    fun getEpisodesByProject(projectId: Long): Flow<List<EpisodeEntity>> = dao.getEpisodesByProject(projectId)
    fun getEpisodesBySeason(seasonId: Long): Flow<List<EpisodeEntity>> = dao.getEpisodesBySeason(seasonId)
    suspend fun insertEpisode(episode: EpisodeEntity): Long {
        val id = dao.insertEpisode(episode)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Novo Episódio",
                description = "Episódio '${episode.title}' adicionado à produção.",
                iconType = "episode"
            )
        )
        return id
    }
    suspend fun updateEpisode(episode: EpisodeEntity) = dao.updateEpisode(episode)
    suspend fun deleteEpisode(episode: EpisodeEntity) = dao.deleteEpisode(episode)

    // Scenes
    val allScenes: Flow<List<SceneEntity>> = dao.getAllScenes()
    val sceneCount: Flow<Int> = dao.getSceneCount()
    fun getScenesByEpisode(episodeId: Long): Flow<List<SceneEntity>> = dao.getScenesByEpisode(episodeId)
    fun getScenesByProject(projectId: Long): Flow<List<SceneEntity>> = dao.getScenesByProject(projectId)
    suspend fun insertScene(scene: SceneEntity): Long {
        val id = dao.insertScene(scene)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Cena Atualizada",
                description = "Cena '${scene.name}' salva no roteiro.",
                iconType = "scene"
            )
        )
        return id
    }
    suspend fun updateScene(scene: SceneEntity) = dao.updateScene(scene)
    suspend fun deleteScene(scene: SceneEntity) = dao.deleteScene(scene)

    // Voices
    val allVoices: Flow<List<VoiceEntity>> = dao.getAllVoices()
    suspend fun insertVoice(voice: VoiceEntity) = dao.insertVoice(voice)
    suspend fun updateVoice(voice: VoiceEntity) = dao.updateVoice(voice)
    suspend fun deleteVoice(voice: VoiceEntity) = dao.deleteVoice(voice)

    // Sounds & Music
    val allSounds: Flow<List<SoundMusicEntity>> = dao.getAllSounds()
    fun getSoundsByProject(projectId: Long): Flow<List<SoundMusicEntity>> = dao.getSoundsByProject(projectId)
    suspend fun insertSound(sound: SoundMusicEntity) = dao.insertSound(sound)
    suspend fun updateSound(sound: SoundMusicEntity) = dao.updateSound(sound)
    suspend fun deleteSound(sound: SoundMusicEntity) = dao.deleteSound(sound)

    // Activity Logs
    val recentActivities: Flow<List<ActivityLogEntity>> = dao.getRecentActivities()
    suspend fun logActivity(title: String, description: String, iconType: String = "info") {
        dao.insertActivity(ActivityLogEntity(title = title, description = description, iconType = iconType))
    }

    // Creations
    val allCreations: Flow<List<RangaCreationEntity>> = dao.getAllCreations()
    val creationCount: Flow<Int> = dao.getCreationCount()
    val favoriteCreations: Flow<List<RangaCreationEntity>> = dao.getFavoriteCreations()
    fun getCreationsByProject(projectId: Long): Flow<List<RangaCreationEntity>> = dao.getCreationsByProject(projectId)
    suspend fun getCreationById(id: Long) = dao.getCreationById(id)
    suspend fun insertCreation(creation: RangaCreationEntity): Long {
        val id = dao.insertCreation(creation)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Nova Criação Visual",
                description = "Imagem '${creation.title}' gerada (${creation.style}).",
                iconType = "image"
            )
        )
        return id
    }
    suspend fun updateCreation(creation: RangaCreationEntity) = dao.updateCreation(creation)
    suspend fun deleteCreation(creation: RangaCreationEntity) {
        dao.deleteCreation(creation)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Criação Removida",
                description = "Imagem '${creation.title}' excluída da galeria.",
                iconType = "delete"
            )
        )
    }

    // Roteiro Branco Items
    val allRoteiroBrancoItems: Flow<List<RoteiroBrancoItemEntity>> = dao.getAllRoteiroBrancoItems()
    val roteiroBrancoItemCount: Flow<Int> = dao.getRoteiroBrancoItemCount()
    fun getRoteiroBrancoItemsByProject(projectId: Long): Flow<List<RoteiroBrancoItemEntity>> = dao.getRoteiroBrancoItemsByProject(projectId)
    suspend fun insertRoteiroBrancoItem(item: RoteiroBrancoItemEntity): Long {
        val id = dao.insertRoteiroBrancoItem(item)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Item no Roteiro Branco",
                description = "'${item.title}' adicionado ao Roteiro Branco.",
                iconType = "script"
            )
        )
        return id
    }
    suspend fun updateRoteiroBrancoItem(item: RoteiroBrancoItemEntity) = dao.updateRoteiroBrancoItem(item)
    suspend fun deleteRoteiroBrancoItem(item: RoteiroBrancoItemEntity) {
        dao.deleteRoteiroBrancoItem(item)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Item Removido do Roteiro",
                description = "'${item.title}' removido do Roteiro Branco.",
                iconType = "delete"
            )
        )
    }

    // Export Records
    val allExports: Flow<List<ExportRecordEntity>> = dao.getAllExports()
    val exportCount: Flow<Int> = dao.getExportCount()
    fun getExportsByProject(projectId: Long): Flow<List<ExportRecordEntity>> = dao.getExportsByProject(projectId)
    suspend fun getExportById(id: Long) = dao.getExportById(id)
    suspend fun insertExport(exportRecord: ExportRecordEntity): Long {
        val id = dao.insertExport(exportRecord)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Exportação Registrada",
                description = "Exportação '${exportRecord.name}' (${exportRecord.type} • ${exportRecord.format}).",
                iconType = "export"
            )
        )
        return id
    }
    suspend fun updateExport(exportRecord: ExportRecordEntity) = dao.updateExport(exportRecord)
    suspend fun deleteExport(exportRecord: ExportRecordEntity) {
        dao.deleteExport(exportRecord)
        dao.insertActivity(
            ActivityLogEntity(
                title = "Exportação Excluída",
                description = "Registro de exportação '${exportRecord.name}' foi removido.",
                iconType = "delete"
            )
        )
    }
}

