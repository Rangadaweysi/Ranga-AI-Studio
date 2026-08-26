package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ExportRecordEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.data.model.RangaCreationEntity
import com.example.data.model.RoteiroBrancoItemEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.data.model.VoiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudioDao {
    // --- Projects ---
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: Long): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity): Long

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("SELECT COUNT(*) FROM projects")
    fun getProjectCount(): Flow<Int>

    // --- Characters ---
    @Query("SELECT * FROM characters ORDER BY name ASC")
    fun getAllCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE projectId = :projectId OR projectId IS NULL ORDER BY name ASC")
    fun getCharactersByProject(projectId: Long): Flow<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity): Long

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("SELECT COUNT(*) FROM characters")
    fun getCharacterCount(): Flow<Int>

    // --- Scenarios ---
    @Query("SELECT * FROM scenarios ORDER BY name ASC")
    fun getAllScenarios(): Flow<List<ScenarioEntity>>

    @Query("SELECT * FROM scenarios WHERE projectId = :projectId OR projectId IS NULL ORDER BY name ASC")
    fun getScenariosByProject(projectId: Long): Flow<List<ScenarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenario(scenario: ScenarioEntity): Long

    @Update
    suspend fun updateScenario(scenario: ScenarioEntity)

    @Delete
    suspend fun deleteScenario(scenario: ScenarioEntity)

    @Query("SELECT COUNT(*) FROM scenarios")
    fun getScenarioCount(): Flow<Int>

    // --- Series ---
    @Query("SELECT * FROM series ORDER BY id DESC")
    fun getAllSeries(): Flow<List<SeriesEntity>>

    @Query("SELECT * FROM series WHERE projectId = :projectId")
    fun getSeriesByProject(projectId: Long): Flow<List<SeriesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: SeriesEntity): Long

    @Update
    suspend fun updateSeries(series: SeriesEntity)

    @Delete
    suspend fun deleteSeries(series: SeriesEntity)

    // --- Seasons ---
    @Query("SELECT * FROM seasons ORDER BY seasonNumber ASC")
    fun getAllSeasons(): Flow<List<SeasonEntity>>

    @Query("SELECT * FROM seasons WHERE seriesId = :seriesId ORDER BY seasonNumber ASC")
    fun getSeasonsBySeries(seriesId: Long): Flow<List<SeasonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeason(season: SeasonEntity): Long

    @Update
    suspend fun updateSeason(season: SeasonEntity)

    @Delete
    suspend fun deleteSeason(season: SeasonEntity)

    // --- Episodes ---
    @Query("SELECT * FROM episodes ORDER BY episodeNumber ASC")
    fun getAllEpisodes(): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE projectId = :projectId ORDER BY episodeNumber ASC")
    fun getEpisodesByProject(projectId: Long): Flow<List<EpisodeEntity>>

    @Query("SELECT * FROM episodes WHERE seasonId = :seasonId ORDER BY episodeNumber ASC")
    fun getEpisodesBySeason(seasonId: Long): Flow<List<EpisodeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(episode: EpisodeEntity): Long

    @Update
    suspend fun updateEpisode(episode: EpisodeEntity)

    @Delete
    suspend fun deleteEpisode(episode: EpisodeEntity)

    @Query("SELECT COUNT(*) FROM episodes")
    fun getEpisodeCount(): Flow<Int>

    // --- Scenes ---
    @Query("SELECT * FROM scenes ORDER BY episodeId, sceneOrder ASC")
    fun getAllScenes(): Flow<List<SceneEntity>>

    @Query("SELECT * FROM scenes WHERE episodeId = :episodeId ORDER BY sceneOrder ASC")
    fun getScenesByEpisode(episodeId: Long): Flow<List<SceneEntity>>

    @Query("SELECT * FROM scenes WHERE projectId = :projectId ORDER BY sceneOrder ASC")
    fun getScenesByProject(projectId: Long): Flow<List<SceneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScene(scene: SceneEntity): Long

    @Update
    suspend fun updateScene(scene: SceneEntity)

    @Delete
    suspend fun deleteScene(scene: SceneEntity)

    @Query("SELECT COUNT(*) FROM scenes")
    fun getSceneCount(): Flow<Int>

    // --- Voices ---
    @Query("SELECT * FROM voices ORDER BY name ASC")
    fun getAllVoices(): Flow<List<VoiceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVoice(voice: VoiceEntity): Long

    @Update
    suspend fun updateVoice(voice: VoiceEntity)

    @Delete
    suspend fun deleteVoice(voice: VoiceEntity)

    // --- Sounds & Music ---
    @Query("SELECT * FROM sounds_music ORDER BY id DESC")
    fun getAllSounds(): Flow<List<SoundMusicEntity>>

    @Query("SELECT * FROM sounds_music WHERE projectId = :projectId OR projectId IS NULL ORDER BY id DESC")
    fun getSoundsByProject(projectId: Long): Flow<List<SoundMusicEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSound(sound: SoundMusicEntity): Long

    @Update
    suspend fun updateSound(sound: SoundMusicEntity)

    @Delete
    suspend fun deleteSound(sound: SoundMusicEntity)

    // --- Activity Logs ---
    @Query("SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 20")
    fun getRecentActivities(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityLogEntity): Long

    // --- Ranga Creations ---
    @Query("SELECT * FROM ranga_creations ORDER BY createdAt DESC")
    fun getAllCreations(): Flow<List<RangaCreationEntity>>

    @Query("SELECT * FROM ranga_creations WHERE projectId = :projectId OR projectId IS NULL ORDER BY createdAt DESC")
    fun getCreationsByProject(projectId: Long): Flow<List<RangaCreationEntity>>

    @Query("SELECT * FROM ranga_creations WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteCreations(): Flow<List<RangaCreationEntity>>

    @Query("SELECT * FROM ranga_creations WHERE id = :id LIMIT 1")
    suspend fun getCreationById(id: Long): RangaCreationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreation(creation: RangaCreationEntity): Long

    @Update
    suspend fun updateCreation(creation: RangaCreationEntity)

    @Delete
    suspend fun deleteCreation(creation: RangaCreationEntity)

    @Query("SELECT COUNT(*) FROM ranga_creations")
    fun getCreationCount(): Flow<Int>

    // --- Roteiro Branco Items ---
    @Query("SELECT * FROM roteiro_branco_items ORDER BY orderIndex ASC, createdAt ASC")
    fun getAllRoteiroBrancoItems(): Flow<List<RoteiroBrancoItemEntity>>

    @Query("SELECT * FROM roteiro_branco_items WHERE projectId = :projectId OR projectId IS NULL ORDER BY orderIndex ASC, createdAt ASC")
    fun getRoteiroBrancoItemsByProject(projectId: Long): Flow<List<RoteiroBrancoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoteiroBrancoItem(item: RoteiroBrancoItemEntity): Long

    @Update
    suspend fun updateRoteiroBrancoItem(item: RoteiroBrancoItemEntity)

    @Delete
    suspend fun deleteRoteiroBrancoItem(item: RoteiroBrancoItemEntity)

    @Query("SELECT COUNT(*) FROM roteiro_branco_items")
    fun getRoteiroBrancoItemCount(): Flow<Int>

    // --- Export Records ---
    @Query("SELECT * FROM export_records ORDER BY createdAt DESC")
    fun getAllExports(): Flow<List<ExportRecordEntity>>

    @Query("SELECT * FROM export_records WHERE projectId = :projectId OR projectId IS NULL ORDER BY createdAt DESC")
    fun getExportsByProject(projectId: Long): Flow<List<ExportRecordEntity>>

    @Query("SELECT * FROM export_records WHERE id = :id LIMIT 1")
    suspend fun getExportById(id: Long): ExportRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExport(exportRecord: ExportRecordEntity): Long

    @Update
    suspend fun updateExport(exportRecord: ExportRecordEntity)

    @Delete
    suspend fun deleteExport(exportRecord: ExportRecordEntity)

    @Query("SELECT COUNT(*) FROM export_records")
    fun getExportCount(): Flow<Int>
}

