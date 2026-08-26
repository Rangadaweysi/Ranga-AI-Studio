package com.example.data.model

data class UserProfileSettings(
    val name: String = "Usuário",
    val email: String = "usuario@estudio.com",
    val plan: String = "Profissional",
    val memberSince: String = "Estúdio Criado",
    val accountId: String = "RANGA-STUDIO-01",
    val avatarInitials: String = "U",
    val avatarColorHex: String = "#0052FF",
    val profilePhotoUri: String? = null
)

data class AppearanceSettings(
    val themeMode: String = "Claro", // "Claro", "Escuro", "Seguir sistema"
    val primaryColorHex: String = "#0052FF",
    val uiSize: String = "Médio",
    val density: String = "Normal"
)

data class LanguageSettings(
    val selectedLanguage: String = "Português (Brasil)",
    val autoDetectLanguage: Boolean = true
)

data class RangaCreationSettings(
    val generationModel: String = "RANGA Image Pro",
    val defaultQuality: String = "Alta",
    val defaultAspectRatio: String = "Paisagem (16:9)",
    val defaultStyle: String = "Cartoon 3D",
    val imagesPerGeneration: Int = 2,
    val autoSaveCreations: Boolean = true,
    val savePromptsToHistory: Boolean = true,
    val showCreationHistory: Boolean = true,
    val confirmBeforeDelete: Boolean = true
)

data class VoiceSettings(
    val defaultLanguage: String = "Português (Brasil)",
    val defaultVoice: String = "Voz Neutra Padrão",
    val defaultSpeed: Float = 1.0f,
    val defaultPitch: String = "Natural",
    val voiceService: String = "RANGA Neural TTS v2",
    val autoSaveVoices: Boolean = true
)

data class AudioSettings(
    val defaultVolume: Float = 0.8f,
    val defaultFormat: String = "MP3 (320 kbps)",
    val defaultQuality: String = "Alta Fidelidade (320 kbps)",
    val autoSaveSounds: Boolean = true
)

data class ProjectsProductionSettings(
    val autoSave: Boolean = true,
    val autoBackup: Boolean = true,
    val confirmDelete: Boolean = true,
    val showRecentProjects: Boolean = true,
    val restoreLastOpenedProject: Boolean = true,
    val autoSaveInterval: String = "5 minutos"
)

data class ExportDefaultSettings(
    val defaultFormat: String = "MP4",
    val defaultQuality: String = "1080p (Full HD)",
    val defaultResolution: String = "1920x1080",
    val defaultFps: String = "30 fps",
    val defaultAudioQuality: String = "Alta (320 kbps)",
    val includeSubtitles: Boolean = true,
    val includeWatermark: Boolean = false
)

data class NotificationSettings(
    val creationCompleted: Boolean = true,
    val exportCompleted: Boolean = true,
    val errorAlerts: Boolean = true,
    val storageWarnings: Boolean = true,
    val appUpdates: Boolean = true,
    val projectActivities: Boolean = true,
    val silentMode: Boolean = false
)

data class ApiKeyItem(
    val id: String,
    val serviceName: String,
    val apiKey: String,
    val maskedKey: String,
    val status: String,
    val description: String = ""
)

data class ActiveSession(
    val id: String,
    val deviceName: String,
    val location: String,
    val ipAddress: String,
    val lastActive: String,
    val isCurrent: Boolean = false
)

data class StorageBreakdown(
    val totalGb: Double = 100.0,
    val usedGb: Double = 0.0,
    val percentageUsed: Int = 0,
    val imagesGb: Double = 0.0,
    val videosGb: Double = 0.0,
    val audioGb: Double = 0.0,
    val projectsGb: Double = 0.0,
    val othersGb: Double = 0.0,
    val tempFilesMb: Double = 0.0
)

data class SyncInfo(
    val lastSyncTime: String = "Nunca sincronizado",
    val status: String = "Pronto",
    val cloudStorageConnected: Boolean = true
)

data class StudioFullSettings(
    val userProfile: UserProfileSettings = UserProfileSettings(),
    val appearance: AppearanceSettings = AppearanceSettings(),
    val language: LanguageSettings = LanguageSettings(),
    val rangaCreation: RangaCreationSettings = RangaCreationSettings(),
    val voices: VoiceSettings = VoiceSettings(),
    val audio: AudioSettings = AudioSettings(),
    val projectsProduction: ProjectsProductionSettings = ProjectsProductionSettings(),
    val exportDefaults: ExportDefaultSettings = ExportDefaultSettings(),
    val notifications: NotificationSettings = NotificationSettings(),
    val apiKeys: List<ApiKeyItem> = listOf(
        ApiKeyItem("1", "RANGA Image API", "", "••••••••••••", "Configuração necessária", "Geração de imagens de personagens e cenários"),
        ApiKeyItem("2", "RANGA Voice API", "", "••••••••••••", "Configuração necessária", "Síntese vocal e dublagem neural"),
        ApiKeyItem("3", "RANGA Audio API", "", "••••••••••••", "Configuração necessária", "Geração e mixagem de trilhas sonoras"),
        ApiKeyItem("4", "Gemini 2.5 Studio API", "", "••••••••••••", "Configuração necessária", "Assistente de roteiro e diálogos")
    ),
    val sessions: List<ActiveSession> = listOf(
        ActiveSession("1", "Dispositivo Atual", "Local", "127.0.0.1", "Ativo agora", true)
    ),
    val storage: StorageBreakdown = StorageBreakdown(),
    val sync: SyncInfo = SyncInfo(),
    val requireConfirmationForDestructiveActions: Boolean = true,
    val isSavingChanges: Boolean = false,
    val lastSavedTimestamp: Long = System.currentTimeMillis()
)
