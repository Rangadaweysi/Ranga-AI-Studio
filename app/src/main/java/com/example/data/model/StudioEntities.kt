package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProjectType(val label: String, val iconName: String) {
    NOVELA("Novela", "Favorite"),
    SERIE("Série", "Tv"),
    DESENHO("Desenho Animado", "Animation"),
    FILME("Filme", "Movie")
}

enum class EpisodeStatus(val label: String) {
    DRAFT("Rascunho"),
    IN_PRODUCTION("Em produção"),
    COMPLETED("Concluído")
}

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val type: String, // Novela, Série, Desenho animado, Filme
    val category: String, // Drama, Aventura, Comédia, Fantasia, Sci-Fi, Romance, Suspense, Infantil
    val coverUri: String? = null,
    val status: String = "Em produção", // Rascunho, Em produção, Concluído, Arquivado
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long? = null,
    val name: String,
    val imageUri: String? = null,
    val personality: String,
    val age: String = "",
    val description: String = "",
    val history: String = "",
    val characterType: String = "Criança", // Criança, Adolescente, Jovem, Adulto, Idoso, Fruta, Animal, Robô, Outro
    val voice: String = "Voz Masculina 1",
    val role: String = "Protagonista",
    val avatarColorHex: String = "#1E3A8A",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scenarios")
data class ScenarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long? = null,
    val name: String,
    val imageUri: String? = null,
    val description: String = "",
    val category: String = "Casa", // Casa, Escola, Cidade, Rua, Loja, Floresta, Praia, Escritório, Quarto, Outro
    val visualStyle: String = "3D Cartoon", // 2D, 3D, Cartoon, Anime, Realista, Estilizado, Infantil, Fantasia, Personalizado
    val locationType: String = "Exterior", // Interior, Exterior, Misto
    val atmosphere: String = "Iluminação acolhedora",
    val consistentArchitecture: String = "Estilo 3D cartoon coerente com cores quentes",
    val versions: String = "Dia, Noite, Chuva, Pôr do sol, Vista frontal, Interior",
    val isArchived: Boolean = false,
    val scenesCount: Int = 1,
    val episodesCount: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "series")
data class SeriesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val projectId: Long = 1,
    val title: String,
    val synopsis: String = "",
    val type: String = "Desenho Animado", // Novela, Série, Desenho Animado, Filme
    val genre: String = "Infantil, Aventura",
    val status: String = "Em produção", // Em planejamento, Em produção, Em exibição, Em pausa, Concluída, Arquivada
    val coverUri: String? = null,
    val targetAudience: String = "Livre",
    val seasonsCount: Int = 2,
    val episodesCount: Int = 24,
    val scenesCount: Int = 156,
    val charactersCount: Int = 8,
    val scenariosCount: Int = 12,
    val totalDuration: String = "18h 45m",
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "seasons")
data class SeasonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val seriesId: Long = 1,
    val seasonNumber: Int = 1,
    val title: String = "Temporada 1",
    val synopsis: String = "",
    val episodesCount: Int = 12,
    val completedEpisodesCount: Int = 8,
    val inProductionEpisodesCount: Int = 2,
    val draftEpisodesCount: Int = 2,
    val progressPercent: Int = 67,
    val status: String = "Em produção",
    val startDate: String = "12/05/2024",
    val endDate: String = "12/12/2024",
    val scenesCount: Int = 86,
    val charactersCount: Int = 8,
    val scenariosCount: Int = 14,
    val totalDuration: String = "4h 32m",
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val coverUri: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val seasonId: Long = 0,
    val projectId: Long,
    val episodeNumber: Int,
    val title: String,
    val description: String,
    val coverUri: String? = null,
    val duration: String = "22 min",
    val status: String = "Em produção",
    val scenesCount: Int = 8,
    val progressPercent: Int = 60,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scenes")
data class SceneEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val episodeId: Long = 1,
    val projectId: Long = 1,
    val sceneOrder: Int = 1,
    val name: String,
    val description: String = "",
    val imageUri: String? = null,
    val status: String = "Em produção", // Rascunho, Em produção, Concluída
    val scenarioId: Long? = null,
    val scenarioName: String = "Cozinha da Casa",
    val scenarioLighting: String = "Dia", // Dia, Noite, Manhã, Tarde, Chuva, Sol, Neve, Personalizado
    val characterIds: String = "António, Bia",
    val charactersJson: String = "", // Serialized list of characters in scene
    val dialogues: String = "",
    val dialoguesJson: String = "", // Serialized list of dialogues
    val actions: String = "",
    val actionsJson: String = "", // Serialized list of actions
    val cameraShot: String = "Plano médio", // Plano geral, Plano médio, Primeiro plano, Close-up, Vista lateral, Vista frontal, Vista superior, Vista inferior, Câmera seguindo personagem, Câmera personalizada
    val cameraMovement: String = "Aproximar", // Aproximar, Afastar, Esquerda, Direita, Cima, Baixo, Rotação, Movimento livre
    val cameraDuration: String = "00:00:05",
    val sounds: String = "Passos, Porta rangendo, Ambiente cozinha",
    val music: String = "Música de Mistério",
    val duration: String = "00:00:25",
    val isArchived: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "voices")
data class VoiceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String = "Jovem", // Criança, Adolescente, Jovem, Adulto, Idoso, Narrador, Criatura, Robô, Personalizada
    val tone: String = "Alegre", // Enérgico, Sério, Amigável, Sombrio, Alegre
    val language: String = "Português (Brasil)",
    val sampleAudioDesc: String = "",
    val assignedCharacter: String? = null,
    val characterEmoji: String? = null,
    val characterId: Long? = null,
    val projectId: Long? = 1,
    val projectName: String = "Aventuras das Frutas",
    val seriesId: Long? = 1,
    val seasonId: Long? = 1,
    val avatarUri: String? = null,
    val statusTag: String = "Salva", // "Em uso", "Salva", "Rascunho", "Arquivada"
    val gender: String = "Masculina", // "Masculina", "Feminina", "Infantil", "Neutra"
    val ageCategory: String = "Jovem", // "Criança", "Jovem", "Adulta", "Idosa"
    val style: String = "Cartoon", // Cartoon, Animação, Natural, Dramática, Comédia, Infantil, Narrador, Fantasia, Futurista, etc.
    val styleTag2: String? = "Aventura", // Aventura, Infantil, Comédia, Divertida, Amável, Narrador, Robô, Vilão, etc.
    val speed: Float = 1.0f,
    val pitch: Float = 0f,
    val expressiveness: Float = 70f,
    val volume: Float = 100f,
    val sampleText: String = "Olá! Eu sou o António e estou muito feliz em começar esta aventura!",
    val approximateAge: String = "15 - 20 anos",
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val usageScenesCount: Int = 0,
    val usageEpisodesCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "sounds_music")
data class SoundMusicEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val type: String = "Música", // "Música", "Efeito SFX", "Ambiente", "Trilha Sonora", "Personalizado"
    val category: String = "Trilha sonora", // Trilha sonora, Som ambiente, Efeito sonoro, Foley, Abertura, etc.
    val mood: String = "Alegre", // Épico, Tenso, Feliz, Misterioso, Suave, Alegre, Animado, Dramático, Nostálgico, Espacial
    val duration: String = "02:45",
    val durationSeconds: Int = 165,
    val fileSize: String = "3.2 MB",
    val format: String = "WAV 48kHz", // WAV 48kHz, MP3 320kbps, OGG, FLAC
    val sampleRate: String = "48.0 kHz",
    val tempoBpm: Int = 120,
    val musicalKey: String = "Dó Menor",
    val projectId: Long? = 1,
    val projectName: String? = "Aventuras das Frutas",
    val seriesId: Long? = 1,
    val seasonId: Long? = 1,
    val episodeId: Long? = null,
    val episodeName: String? = null,
    val sceneId: Long? = null,
    val sceneName: String? = null,
    val imageType: String = "music_waves", // music_waves, forest_morning, wooden_door, busy_city, thunder_storm, hero_theme, rain_soft, magic_sparkle, footsteps_ground, calm_beach, space_travel, kid_laugh
    val description: String = "",
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val isAiGenerated: Boolean = false,
    val isLooping: Boolean = false,
    val isBackgroundMusic: Boolean = false,
    val bgVolume: Float = 0.8f,
    val bgStartTime: String = "00:00",
    val bgEndTime: String = "02:45",
    val bgFadeIn: Boolean = true,
    val bgFadeOut: Boolean = true,
    val usageScenesCount: Int = 1,
    val usageEpisodesCount: Int = 1,
    val waveformPreset: String = "dynamic",
    val soundToneType: String = "synth_melody", // synth_melody, nature_rain, nature_forest, sfx_door, sfx_thunder, sfx_magic, sfx_footsteps, sfx_laugh, sfx_explosion, ambient_city, ambient_beach, synth_space
    val aiPrompt: String = "",
    val sampleAudioDesc: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "activity_logs")
data class ActivityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val iconType: String = "edit"
)

@Entity(tableName = "ranga_creations")
data class RangaCreationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val prompt: String,
    val creationType: String = "Personagem", // Personagem, Cenário, Ambiente, Animal, Veículo, Objeto, Imagem, Elemento de cena, Outro
    val style: String = "Cartoon 3D", // Cartoon 3D, Cartoon, Anime, Realista, Cinemático, Fantasia, 2D, Infantil, Desenho animado, Estilizado, Personalizado
    val aspectRatio: String = "16:9", // Quadrado (1:1), Retrato (9:16), Paisagem (16:9), 4:3, 16:9
    val imageUri: String,
    val referenceImageUri: String? = null,
    val projectId: Long? = 1,
    val projectName: String? = "Aventuras das Frutas",
    val seriesId: Long? = 1,
    val seasonId: Long? = 1,
    val episodeId: Long? = null,
    val isFavorite: Boolean = false,
    val isInRoteiroBranco: Boolean = false,
    val isArchived: Boolean = false,
    val quality: String = "Alta Definição (3D)",
    val variationsJson: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "roteiro_branco_items")
data class RoteiroBrancoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val creationId: Long? = null,
    val title: String,
    val type: String = "Personagem", // Personagem, Cenário, Ambiente, Objeto, Imagem, Referência
    val imageUri: String,
    val notes: String = "",
    val orderIndex: Int = 0,
    val projectId: Long? = 1,
    val projectName: String? = "Aventuras das Frutas",
    val episodeId: Long? = null,
    val sceneId: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "export_records")
data class ExportRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String, // e.g. "Episódio 05", "Episódio 04", "Roteiro Temporada 1", "Personagens", "Trilha principal"
    val type: String = "Episódio", // "Episódio", "Cenas", "Séries", "Temporadas", "Vozes", "Sons e músicas", "Imagens", "Roteiros", "Projeto completo"
    val format: String = "MP4", // "MP4", "PDF", "PNG", "WAV", "ZIP", "DOCX", "TXT", "MOV"
    val quality: String = "1080p (Full HD)", // "1080p (Full HD)", "720p (HD)", "4K (Ultra HD)", "Alta (300 DPI)", "Lossless"
    val fps: String = "30 fps", // "24 fps", "30 fps", "60 fps"
    val audioQuality: String = "Alta (320 kbps)", // "Alta (320 kbps)", "Média (192 kbps)", "Lossless WAV"
    val includeSubtitles: Boolean = true,
    val includeWatermark: Boolean = false,
    val sizeDisplay: String = "520 MB",
    val status: String = "Concluído", // "Processando", "Concluído", "Falhou"
    val dateDisplay: String = "Hoje, 10:30",
    val projectId: Long? = 1,
    val projectName: String? = "Aventuras das Frutas",
    val seriesId: Long? = 1,
    val seasonId: Long? = 1,
    val episodeId: Long? = 5,
    val durationDisplay: String = "08:45",
    val selectedScenesCount: Int = 12,
    val details: String = "",
    val fileContent: String = "",
    val outputUri: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)


