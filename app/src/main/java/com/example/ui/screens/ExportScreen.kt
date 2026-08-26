package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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
import com.example.ui.navigation.StudioDestination

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    projects: List<ProjectEntity>,
    series: List<SeriesEntity> = emptyList(),
    seasons: List<SeasonEntity> = emptyList(),
    episodes: List<EpisodeEntity> = emptyList(),
    scenes: List<SceneEntity> = emptyList(),
    characters: List<CharacterEntity> = emptyList(),
    scenarios: List<ScenarioEntity> = emptyList(),
    voices: List<VoiceEntity> = emptyList(),
    sounds: List<SoundMusicEntity> = emptyList(),
    creations: List<RangaCreationEntity> = emptyList(),
    roteiroBrancoItems: List<RoteiroBrancoItemEntity> = emptyList(),
    exportRecords: List<ExportRecordEntity> = emptyList(),
    selectedProjectId: Long?,
    onSaveExport: (ExportRecordEntity) -> Unit = {},
    onUpdateExport: (ExportRecordEntity) -> Unit = {},
    onDeleteExport: (ExportRecordEntity) -> Unit = {},
    onRetryExport: (ExportRecordEntity) -> Unit = {},
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToDestination: (StudioDestination) -> Unit = {}
) {
    val context = LocalContext.current

    // Active Selection State
    var selectedExportType by remember { mutableStateOf("Episódios") }
    var selectedProject by remember { mutableStateOf(projects.find { it.id == selectedProjectId } ?: projects.firstOrNull()) }
    var selectedSeriesName by remember { mutableStateOf("Aventuras das Frutas") }
    var selectedSeasonName by remember { mutableStateOf("Temporada 1") }
    var selectedEpisodeName by remember { mutableStateOf("Episódio 05") }

    // Export Settings
    var exportFormat by remember { mutableStateOf("MP4") }
    var exportQuality by remember { mutableStateOf("1080p (Full HD)") }
    var exportFps by remember { mutableStateOf("30 fps") }
    var exportAudioQuality by remember { mutableStateOf("Alta (320 kbps)") }
    var includeSubtitles by remember { mutableStateOf(true) }
    var includeWatermark by remember { mutableStateOf(false) }
    var showAdvancedOptions by remember { mutableStateOf(false) }

    // Advanced codec/bitrate/destination
    var videoCodec by remember { mutableStateOf("H.264 (AVC)") }
    var videoBitrate by remember { mutableStateOf("16 Mbps (Padrão)") }
    var audioChannels by remember { mutableStateOf("Estéreo 2.0") }
    var audioNormalization by remember { mutableStateOf("EBU R128 (-14 LUFS)") }

    // Search query
    var searchQuery by remember { mutableStateOf("") }

    // Assistant State
    var assistantInput by remember { mutableStateOf("") }
    var assistantResponse by remember {
        mutableStateOf("Olá, Augusto! Para o episódio 'Episódio 05', o formato MP4 (1080p a 30fps) com áudio a 320 kbps é ideal para publicação online e YouTube. Deseja aplicar essa recomendação?")
    }

    // Scene Selection (Mocked / Real list of 12 scenes)
    val sceneList = remember {
        listOf(
            "Cena 01" to "00:45",
            "Cena 02" to "00:32",
            "Cena 03" to "00:38",
            "Cena 04" to "00:41",
            "Cena 05" to "00:50",
            "Cena 06" to "00:29",
            "Cena 07" to "00:55",
            "Cena 08" to "00:48",
            "Cena 09" to "00:35",
            "Cena 10" to "00:42",
            "Cena 11" to "00:51",
            "Cena 12" to "00:39"
        )
    }
    val selectedScenes = remember { mutableStateListOf<String>().apply { addAll(sceneList.map { it.first }) } }

    // Dialogs State
    var showProcessingDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf<ExportRecordEntity?>(null) }
    var showDetailsDialog by remember { mutableStateOf<ExportRecordEntity?>(null) }
    var showFullProjectDialog by remember { mutableStateOf(false) }
    var showScriptDialog by remember { mutableStateOf(false) }

    // Categories list matching reference
    val exportCategories = listOf(
        ExportCategoryItem("Cenas", Icons.Default.Movie, "Exportar uma ou várias cenas."),
        ExportCategoryItem("Episódios", Icons.Default.PlayCircleOutline, "Exportar episódios completos."),
        ExportCategoryItem("Séries", Icons.Default.Tv, "Exportar uma série inteira."),
        ExportCategoryItem("Temporadas", Icons.Default.CollectionsBookmark, "Exportar uma temporada."),
        ExportCategoryItem("Vozes", Icons.Default.Mic, "Exportar arquivos de voz."),
        ExportCategoryItem("Sons e músicas", Icons.AutoMirrored.Filled.QueueMusic, "Exportar músicas e efeitos."),
        ExportCategoryItem("Imagens", Icons.Default.PhotoLibrary, "Exportar personagens, cenários e mais."),
        ExportCategoryItem("Roteiros", Icons.Default.Description, "Exportar roteiros em diversos formatos."),
        ExportCategoryItem("Projeto completo", Icons.Default.FolderZip, "Exportar todo o projeto em um pacote.")
    )

    // Layout
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("export_screen_root")
    ) {
        val isWide = maxWidth >= 1000.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // ==========================================
            // HEADER BAR
            // ==========================================
            item {
                ExportHeaderBar(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    onHelpClick = {
                        Toast.makeText(context, "Central de Ajuda RANGA AI STUDIO: Guia de Exportação", Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // ==========================================
            // MAIN CONTENT (Adaptive Grid)
            // ==========================================
            item {
                if (isWide) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Left Column (Sections 1, 2, 3)
                        Column(modifier = Modifier.weight(1.35f)) {
                            // Section 1: O que você deseja exportar?
                            ExportCategorySelectionCard(
                                categories = exportCategories,
                                selectedType = selectedExportType,
                                onSelectType = { type ->
                                    selectedExportType = type
                                    when (type) {
                                        "Projeto completo" -> showFullProjectDialog = true
                                        "Roteiros" -> {
                                            exportFormat = "PDF"
                                            showScriptDialog = true
                                        }
                                        "Imagens" -> exportFormat = "PNG"
                                        "Vozes", "Sons e músicas" -> exportFormat = "WAV"
                                        else -> exportFormat = "MP4"
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Section 2: Selecionar projeto e conteúdo
                            ExportContentSelectionCard(
                                projects = projects,
                                selectedProject = selectedProject,
                                onSelectProject = { selectedProject = it },
                                selectedSeries = selectedSeriesName,
                                onSelectSeries = { selectedSeriesName = it },
                                selectedSeason = selectedSeasonName,
                                onSelectSeason = { selectedSeasonName = it },
                                selectedEpisode = selectedEpisodeName,
                                onSelectEpisode = { selectedEpisodeName = it },
                                sceneList = sceneList,
                                selectedScenes = selectedScenes,
                                onToggleScene = { sc ->
                                    if (selectedScenes.contains(sc)) selectedScenes.remove(sc) else selectedScenes.add(sc)
                                },
                                onToggleAllScenes = {
                                    if (selectedScenes.size == sceneList.size) {
                                        selectedScenes.clear()
                                    } else {
                                        selectedScenes.clear()
                                        selectedScenes.addAll(sceneList.map { it.first })
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Section 3: Configurações de exportação
                            ExportConfigurationCard(
                                format = exportFormat,
                                onFormatChange = { exportFormat = it },
                                quality = exportQuality,
                                onQualityChange = { exportQuality = it },
                                fps = exportFps,
                                onFpsChange = { exportFps = it },
                                audioQuality = exportAudioQuality,
                                onAudioQualityChange = { exportAudioQuality = it },
                                includeSubtitles = includeSubtitles,
                                onSubtitlesChange = { includeSubtitles = it },
                                includeWatermark = includeWatermark,
                                onWatermarkChange = { includeWatermark = it },
                                showAdvanced = showAdvancedOptions,
                                onToggleAdvanced = { showAdvancedOptions = !showAdvancedOptions },
                                videoCodec = videoCodec,
                                onCodecChange = { videoCodec = it },
                                videoBitrate = videoBitrate,
                                onBitrateChange = { videoBitrate = it },
                                audioChannels = audioChannels,
                                onChannelsChange = { audioChannels = it },
                                audioNormalization = audioNormalization,
                                onNormalizationChange = { audioNormalization = it },
                                onReset = {
                                    exportFormat = "MP4"
                                    exportQuality = "1080p (Full HD)"
                                    exportFps = "30 fps"
                                    exportAudioQuality = "Alta (320 kbps)"
                                    includeSubtitles = true
                                    includeWatermark = false
                                },
                                onStartExport = {
                                    showProcessingDialog = true
                                }
                            )
                        }

                        // Right Column (Sections 4, 5, Recent, Assistant)
                        Column(modifier = Modifier.weight(0.95f)) {
                            // Section 4: Resumo da exportação
                            ExportSummaryCard(
                                projectName = selectedProject?.name ?: "Aventuras das Frutas",
                                seriesName = selectedSeriesName,
                                seasonName = selectedSeasonName,
                                episodeName = selectedEpisodeName,
                                sceneCount = selectedScenes.size,
                                duration = "08:45",
                                format = exportFormat,
                                quality = exportQuality,
                                audioQuality = exportAudioQuality,
                                subtitles = includeSubtitles
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Exportações Recentes
                            RecentExportsCard(
                                onDownload = { name, fmt ->
                                    Toast.makeText(context, "📥 Baixando $name.$fmt...", Toast.LENGTH_SHORT).show()
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Section 5: Histórico de Exportações
                            ExportHistoryCard(
                                historyRecords = exportRecords,
                                onRecordClick = { showDetailsDialog = it },
                                onDownload = { rec ->
                                    Toast.makeText(context, "📥 Baixando ${rec.name}.${rec.format.lowercase()}...", Toast.LENGTH_SHORT).show()
                                },
                                onRetry = { rec ->
                                    onRetryExport(rec)
                                    Toast.makeText(context, "🔄 Reiniciando exportação de ${rec.name}...", Toast.LENGTH_SHORT).show()
                                },
                                onShare = { rec ->
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "RANGA Studio - ${rec.name}")
                                        putExtra(Intent.EXTRA_TEXT, "🎬 Exportação de ${rec.name} (${rec.format}) concluída!")
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Compartilhar"))
                                }
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // Assistente RANGA
                            RangaExportAssistantCard(
                                assistantInput = assistantInput,
                                onInputChange = { assistantInput = it },
                                assistantResponse = assistantResponse,
                                onQuickSuggestion = { query ->
                                    assistantInput = query
                                    when (query) {
                                        "Qual formato devo usar?" -> {
                                            assistantResponse = "💡 Para streaming e YouTube: use MP4 (1080p, 30fps). Para cinema e festivais: use 4K ProRes (24fps). Para roteiro de leitura: use PDF."
                                        }
                                        "Exportar apenas imagens" -> {
                                            selectedExportType = "Imagens"
                                            exportFormat = "PNG"
                                            assistantResponse = "🖼️ Configurei o modo para Imagens em alta resolução PNG (transparência e 300 DPI)."
                                        }
                                        "Exportar projeto inteiro" -> {
                                            showFullProjectDialog = true
                                            assistantResponse = "📁 Abrindo configurações do pacote ZIP completo com todas as mídias e roteiros."
                                        }
                                        "Preparar para publicação" -> {
                                            exportFormat = "MP4"
                                            exportQuality = "1080p (Full HD)"
                                            exportFps = "30 fps"
                                            includeSubtitles = true
                                            includeWatermark = false
                                            assistantResponse = "✨ Configuração otimizada para YouTube / Redes Sociais aplicada com sucesso!"
                                        }
                                    }
                                },
                                onSend = {
                                    if (assistantInput.isNotBlank()) {
                                        val prompt = assistantInput
                                        assistantInput = ""
                                        assistantResponse = "🤖 Analisando '$prompt': Recomendo exportação em MP4 (H.264) com legendas ativadas para máxima compatibilidade."
                                    }
                                }
                            )
                        }
                    }
                } else {
                    // Mobile / Stacked layout
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Section 1: O que você deseja exportar?
                        ExportCategorySelectionCard(
                            categories = exportCategories,
                            selectedType = selectedExportType,
                            onSelectType = { type ->
                                selectedExportType = type
                                when (type) {
                                    "Projeto completo" -> showFullProjectDialog = true
                                    "Roteiros" -> {
                                        exportFormat = "PDF"
                                        showScriptDialog = true
                                    }
                                    "Imagens" -> exportFormat = "PNG"
                                    "Vozes", "Sons e músicas" -> exportFormat = "WAV"
                                    else -> exportFormat = "MP4"
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Section 2: Selecionar projeto e conteúdo
                        ExportContentSelectionCard(
                            projects = projects,
                            selectedProject = selectedProject,
                            onSelectProject = { selectedProject = it },
                            selectedSeries = selectedSeriesName,
                            onSelectSeries = { selectedSeriesName = it },
                            selectedSeason = selectedSeasonName,
                            onSelectSeason = { selectedSeasonName = it },
                            selectedEpisode = selectedEpisodeName,
                            onSelectEpisode = { selectedEpisodeName = it },
                            sceneList = sceneList,
                            selectedScenes = selectedScenes,
                            onToggleScene = { sc ->
                                if (selectedScenes.contains(sc)) selectedScenes.remove(sc) else selectedScenes.add(sc)
                            },
                            onToggleAllScenes = {
                                if (selectedScenes.size == sceneList.size) {
                                    selectedScenes.clear()
                                } else {
                                    selectedScenes.clear()
                                    selectedScenes.addAll(sceneList.map { it.first })
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Section 3: Configurações de exportação
                        ExportConfigurationCard(
                            format = exportFormat,
                            onFormatChange = { exportFormat = it },
                            quality = exportQuality,
                            onQualityChange = { exportQuality = it },
                            fps = exportFps,
                            onFpsChange = { exportFps = it },
                            audioQuality = exportAudioQuality,
                            onAudioQualityChange = { exportAudioQuality = it },
                            includeSubtitles = includeSubtitles,
                            onSubtitlesChange = { includeSubtitles = it },
                            includeWatermark = includeWatermark,
                            onWatermarkChange = { includeWatermark = it },
                            showAdvanced = showAdvancedOptions,
                            onToggleAdvanced = { showAdvancedOptions = !showAdvancedOptions },
                            videoCodec = videoCodec,
                            onCodecChange = { videoCodec = it },
                            videoBitrate = videoBitrate,
                            onBitrateChange = { videoBitrate = it },
                            audioChannels = audioChannels,
                            onChannelsChange = { audioChannels = it },
                            audioNormalization = audioNormalization,
                            onNormalizationChange = { audioNormalization = it },
                            onReset = {
                                exportFormat = "MP4"
                                exportQuality = "1080p (Full HD)"
                                exportFps = "30 fps"
                                exportAudioQuality = "Alta (320 kbps)"
                                includeSubtitles = true
                                includeWatermark = false
                            },
                            onStartExport = {
                                showProcessingDialog = true
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Section 4: Resumo da exportação
                        ExportSummaryCard(
                            projectName = selectedProject?.name ?: "Aventuras das Frutas",
                            seriesName = selectedSeriesName,
                            seasonName = selectedSeasonName,
                            episodeName = selectedEpisodeName,
                            sceneCount = selectedScenes.size,
                            duration = "08:45",
                            format = exportFormat,
                            quality = exportQuality,
                            audioQuality = exportAudioQuality,
                            subtitles = includeSubtitles
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Exportações Recentes
                        RecentExportsCard(
                            onDownload = { name, fmt ->
                                Toast.makeText(context, "📥 Baixando $name.$fmt...", Toast.LENGTH_SHORT).show()
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Section 5: Histórico de Exportações
                        ExportHistoryCard(
                            historyRecords = exportRecords,
                            onRecordClick = { showDetailsDialog = it },
                            onDownload = { rec ->
                                Toast.makeText(context, "📥 Baixando ${rec.name}.${rec.format.lowercase()}...", Toast.LENGTH_SHORT).show()
                            },
                            onRetry = { rec ->
                                onRetryExport(rec)
                                Toast.makeText(context, "🔄 Reiniciando exportação de ${rec.name}...", Toast.LENGTH_SHORT).show()
                            },
                            onShare = { rec ->
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "RANGA Studio - ${rec.name}")
                                    putExtra(Intent.EXTRA_TEXT, "🎬 Exportação de ${rec.name} (${rec.format}) concluída!")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar"))
                            }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Assistente RANGA
                        RangaExportAssistantCard(
                            assistantInput = assistantInput,
                            onInputChange = { assistantInput = it },
                            assistantResponse = assistantResponse,
                            onQuickSuggestion = { query ->
                                assistantInput = query
                                when (query) {
                                    "Qual formato devo usar?" -> {
                                        assistantResponse = "💡 Para streaming e YouTube: use MP4 (1080p, 30fps). Para cinema e festivais: use 4K ProRes (24fps). Para roteiro de leitura: use PDF."
                                    }
                                    "Exportar apenas imagens" -> {
                                        selectedExportType = "Imagens"
                                        exportFormat = "PNG"
                                        assistantResponse = "🖼️ Configurei o modo para Imagens em alta resolução PNG (transparência e 300 DPI)."
                                    }
                                    "Exportar projeto inteiro" -> {
                                        showFullProjectDialog = true
                                        assistantResponse = "📁 Abrindo configurações do pacote ZIP completo com todas as mídias e roteiros."
                                    }
                                    "Preparar para publicação" -> {
                                        exportFormat = "MP4"
                                        exportQuality = "1080p (Full HD)"
                                        exportFps = "30 fps"
                                        includeSubtitles = true
                                        includeWatermark = false
                                        assistantResponse = "✨ Configuração otimizada para YouTube / Redes Sociais aplicada com sucesso!"
                                    }
                                }
                            },
                            onSend = {
                                if (assistantInput.isNotBlank()) {
                                    val prompt = assistantInput
                                    assistantInput = ""
                                    assistantResponse = "🤖 Analisando '$prompt': Recomendo exportação em MP4 (H.264) com legendas ativadas para máxima compatibilidade."
                                }
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // ==========================================
    // MODALS & DIALOGS
    // ==========================================

    if (showProcessingDialog) {
        ExportProcessingDialog(
            exportName = selectedEpisodeName,
            exportType = selectedExportType,
            exportFormat = exportFormat,
            onComplete = { completedRecord ->
                showProcessingDialog = false
                onSaveExport(completedRecord)
                showSuccessDialog = completedRecord
            },
            onCancel = {
                showProcessingDialog = false
            }
        )
    }

    showSuccessDialog?.let { record ->
        ExportSuccessDialog(
            record = record,
            onDismiss = { showSuccessDialog = null },
            onExportAgain = {
                showSuccessDialog = null
                showProcessingDialog = true
            }
        )
    }

    showDetailsDialog?.let { record ->
        ExportRecordDetailsDialog(
            record = record,
            onDismiss = { showDetailsDialog = null },
            onDownload = {
                Toast.makeText(context, "📥 Baixando ${record.name}.${record.format.lowercase()}...", Toast.LENGTH_SHORT).show()
            },
            onShare = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "RANGA Studio - ${record.name}")
                    putExtra(Intent.EXTRA_TEXT, "🎬 Registro de ${record.name} (${record.format})")
                }
                context.startActivity(Intent.createChooser(shareIntent, "Compartilhar"))
            },
            onDelete = {
                onDeleteExport(record)
                showDetailsDialog = null
                Toast.makeText(context, "Registro excluído do histórico.", Toast.LENGTH_SHORT).show()
            },
            onRetry = {
                onRetryExport(record)
                showDetailsDialog = null
                Toast.makeText(context, "Exportação reiniciada.", Toast.LENGTH_SHORT).show()
            }
        )
    }

    if (showFullProjectDialog) {
        FullProjectExportDialog(
            projectName = selectedProject?.name ?: "Aventuras das Frutas",
            onConfirm = { included ->
                showFullProjectDialog = false
                showProcessingDialog = true
            },
            onDismiss = { showFullProjectDialog = false }
        )
    }

    if (showScriptDialog) {
        ScriptExportOptionsDialog(
            projectName = selectedProject?.name ?: "Aventuras das Frutas",
            onConfirm = { fmt, incDetails ->
                exportFormat = fmt
                showScriptDialog = false
                showProcessingDialog = true
            },
            onDismiss = { showScriptDialog = false }
        )
    }
}

data class ExportCategoryItem(
    val title: String,
    val icon: ImageVector,
    val description: String
)

// ==========================================
// HEADER COMPONENT
// ==========================================
@Composable
private fun ExportHeaderBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onHelpClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Title & Subtitle
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Exportar",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 20.sp
                        )
                    )
                    Text(
                        text = "Exporte suas criações para continuar seu projeto fora do RANGA AI STUDIO.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Right Actions & Search
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Pesquisar...", color = Color(0xFF94A3B8), fontSize = 12.5.sp) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Pesquisa", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier
                        .width(180.dp)
                        .height(44.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onHelpClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(imageVector = Icons.Default.HelpOutline, contentDescription = "Ajuda", tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                }

                IconButton(
                    onClick = {},
                    modifier = Modifier.size(36.dp)
                ) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = Color(0xFFDC2626)) {
                                Text("3", color = Color.White, fontSize = 9.sp)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificações", tint = Color(0xFF64748B), modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// ==========================================
// SECTION 1: CATEGORY SELECTION
// ==========================================
@Composable
private fun ExportCategorySelectionCard(
    categories: List<ExportCategoryItem>,
    selectedType: String,
    onSelectType: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "1. O que você deseja exportar?",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 9 Cards Grid (3 columns on normal screens, wrap on smaller)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                categories.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { item ->
                            val isSelected = selectedType == item.title
                            ExportCategoryTile(
                                item = item,
                                isSelected = isSelected,
                                onSelect = { onSelectType(item.title) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowItems.size < 3) {
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ExportCategoryTile(
    item: ExportCategoryItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onSelect,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color(0xFFEFF6FF) else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)
        ),
        modifier = modifier.testTag("export_cat_${item.title.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Color(0xFF0052FF) else Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color(0xFF475569),
                        modifier = Modifier.size(18.dp)
                    )
                }

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF0F172A),
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                ),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isSelected) Color(0xFF0052FF) else Color.White,
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFFCBD5E1)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier.padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isSelected) "Selecionado" else "Selecionar",
                        color = if (isSelected) Color.White else Color(0xFF334155),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.5.sp
                    )
                }
            }
        }
    }
}

// ==========================================
// SECTION 2: CONTENT SELECTION
// ==========================================
@Composable
private fun ExportContentSelectionCard(
    projects: List<ProjectEntity>,
    selectedProject: ProjectEntity?,
    onSelectProject: (ProjectEntity) -> Unit,
    selectedSeries: String,
    onSelectSeries: (String) -> Unit,
    selectedSeason: String,
    onSelectSeason: (String) -> Unit,
    selectedEpisode: String,
    onSelectEpisode: (String) -> Unit,
    sceneList: List<Pair<String, String>>,
    selectedScenes: List<String>,
    onToggleScene: (String) -> Unit,
    onToggleAllScenes: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "2. Selecionar projeto e conteúdo",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4 Dropdowns row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExportDropdownField(
                    label = "Projeto",
                    value = selectedProject?.name ?: "Aventuras das Frutas",
                    options = if (projects.isNotEmpty()) projects.map { it.name } else listOf("Aventuras das Frutas", "Mundo dos Dinossauros"),
                    onSelect = { name ->
                        val proj = projects.find { it.name == name }
                        if (proj != null) onSelectProject(proj)
                    },
                    modifier = Modifier.weight(1f)
                )

                ExportDropdownField(
                    label = "Série",
                    value = selectedSeries,
                    options = listOf("Aventuras das Frutas", "Os Frutinhas Especial"),
                    onSelect = onSelectSeries,
                    modifier = Modifier.weight(1f)
                )

                ExportDropdownField(
                    label = "Temporada",
                    value = selectedSeason,
                    options = listOf("Temporada 1", "Temporada 2", "Temporada 3"),
                    onSelect = onSelectSeason,
                    modifier = Modifier.weight(1f)
                )

                ExportDropdownField(
                    label = "Episódio",
                    value = selectedEpisode,
                    options = listOf("Episódio 01", "Episódio 02", "Episódio 03", "Episódio 04", "Episódio 05", "Episódio 06"),
                    onSelect = onSelectEpisode,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Split content card: Left Episode Info, Right Scene List
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Left Episode Preview Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Banner image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.scene_apple_banana_kitchen_1787050705242),
                                contentDescription = "Preview Episódio 05",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Status badge
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier
                                    .padding(6.dp)
                                    .align(Alignment.TopEnd)
                            ) {
                                Text(
                                    text = "Produzido",
                                    color = Color(0xFF16A34A),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = selectedEpisode,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 15.sp
                            )
                        )
                        Text(
                            text = "A nova casa",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 6 Metrics Pills (2 rows of 3)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                MetricBadge("12 Cenas", Modifier.weight(1f))
                                MetricBadge("08:45 Duração", Modifier.weight(1f))
                                MetricBadge("7 Personagens", Modifier.weight(1f))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                MetricBadge("6 Vozes", Modifier.weight(1f))
                                MetricBadge("5 Músicas", Modifier.weight(1f))
                                MetricBadge("18 Efeitos", Modifier.weight(1f))
                            }
                        }
                    }
                }

                // Right Scene Selection List
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        // Header with select all
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleAllScenes() },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Selecionar cenas",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    fontSize = 13.sp
                                )
                            )

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Selecionar todas (${sceneList.size})",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF64748B),
                                        fontSize = 11.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Checkbox(
                                    checked = selectedScenes.size == sceneList.size,
                                    onCheckedChange = { onToggleAllScenes() },
                                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0052FF)),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Scrollable Scene List
                        Column(
                            modifier = Modifier
                                .heightIn(max = 160.dp)
                                .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            sceneList.forEach { (sceneName, dur) ->
                                val isChecked = selectedScenes.contains(sceneName)
                                Surface(
                                    onClick = { onToggleScene(sceneName) },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isChecked) Color(0xFFEFF6FF) else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        width = 1.dp,
                                        color = if (isChecked) Color(0xFF93C5FD) else Color(0xFFE2E8F0)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color(0xFFE2E8F0)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Movie,
                                                    contentDescription = null,
                                                    tint = Color(0xFF64748B),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = sceneName,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF0F172A),
                                                    fontSize = 12.sp
                                                )
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = dur,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color(0xFF64748B),
                                                    fontSize = 10.sp
                                                )
                                            )
                                        }

                                        Checkbox(
                                            checked = isChecked,
                                            onCheckedChange = { onToggleScene(sceneName) },
                                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0052FF)),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Blue Info callout
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Você selecionou: $selectedEpisode com ${selectedScenes.size} cenas",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF0052FF),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.5.sp
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricBadge(text: String, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF475569),
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp
            )
        }
    }
}

// ==========================================
// SECTION 3: EXPORT CONFIGURATION
// ==========================================
@Composable
private fun ExportConfigurationCard(
    format: String,
    onFormatChange: (String) -> Unit,
    quality: String,
    onQualityChange: (String) -> Unit,
    fps: String,
    onFpsChange: (String) -> Unit,
    audioQuality: String,
    onAudioQualityChange: (String) -> Unit,
    includeSubtitles: Boolean,
    onSubtitlesChange: (Boolean) -> Unit,
    includeWatermark: Boolean,
    onWatermarkChange: (Boolean) -> Unit,
    showAdvanced: Boolean,
    onToggleAdvanced: () -> Unit,
    videoCodec: String,
    onCodecChange: (String) -> Unit,
    videoBitrate: String,
    onBitrateChange: (String) -> Unit,
    audioChannels: String,
    onChannelsChange: (String) -> Unit,
    audioNormalization: String,
    onNormalizationChange: (String) -> Unit,
    onReset: () -> Unit,
    onStartExport: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "3. Configurações de exportação",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // First Row: Formato, Qualidade, FPS, Áudio
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExportDropdownField(
                    label = "Formato",
                    value = format,
                    options = listOf("MP4", "MOV", "MKV", "PDF", "ZIP", "DOCX", "TXT", "PNG", "WAV"),
                    onSelect = onFormatChange,
                    modifier = Modifier.weight(1f)
                )

                ExportDropdownField(
                    label = "Qualidade",
                    value = quality,
                    options = listOf("1080p (Full HD)", "720p (HD)", "4K (Ultra HD)", "Alta (300 DPI)", "Lossless"),
                    onSelect = onQualityChange,
                    modifier = Modifier.weight(1.2f)
                )

                ExportDropdownField(
                    label = "FPS",
                    value = fps,
                    options = listOf("24 fps", "30 fps", "60 fps"),
                    onSelect = onFpsChange,
                    modifier = Modifier.weight(0.9f)
                )

                ExportDropdownField(
                    label = "Qualidade do áudio",
                    value = audioQuality,
                    options = listOf("Alta (320 kbps)", "Média (192 kbps)", "Lossless WAV"),
                    onSelect = onAudioQualityChange,
                    modifier = Modifier.weight(1.3f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Toggles Row: Legendas & Marca d'água
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSubtitlesChange(!includeSubtitles) }
                ) {
                    Switch(
                        checked = includeSubtitles,
                        onCheckedChange = onSubtitlesChange,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF0052FF))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Incluir legendas",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                            fontSize = 13.sp
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onWatermarkChange(!includeWatermark) }
                ) {
                    Switch(
                        checked = includeWatermark,
                        onCheckedChange = onWatermarkChange,
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF0052FF))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Incluir marca d'água",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Advanced Settings Accordion
            Surface(
                onClick = onToggleAdvanced,
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = Color(0xFF0052FF),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Opções avançadas de renderização",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0052FF),
                                fontSize = 12.5.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = if (showAdvanced) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = Color(0xFF64748B)
                    )
                }
            }

            AnimatedVisibility(visible = showAdvanced) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExportDropdownField(
                            label = "Codec de Vídeo",
                            value = videoCodec,
                            options = listOf("H.264 (AVC)", "H.265 (HEVC)", "Apple ProRes 422"),
                            onSelect = onCodecChange,
                            modifier = Modifier.weight(1f)
                        )
                        ExportDropdownField(
                            label = "Taxa de Bits",
                            value = videoBitrate,
                            options = listOf("16 Mbps (Padrão)", "25 Mbps (Alta)", "50 Mbps (Master)"),
                            onSelect = onBitrateChange,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ExportDropdownField(
                            label = "Canais de Áudio",
                            value = audioChannels,
                            options = listOf("Estéreo 2.0", "Surround 5.1"),
                            onSelect = onChannelsChange,
                            modifier = Modifier.weight(1f)
                        )
                        ExportDropdownField(
                            label = "Normalização",
                            value = audioNormalization,
                            options = listOf("EBU R128 (-14 LUFS)", "YouTube Standard (-14 LUFS)", "Cinema Reference (-24 LUFS)"),
                            onSelect = onNormalizationChange,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action Buttons Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF475569)),
                    modifier = Modifier.weight(0.8f)
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Voltar", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onStartExport,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("btn_start_export")
                ) {
                    Icon(imageVector = Icons.Default.Upload, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciar exportação", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

// ==========================================
// SECTION 4: EXPORT SUMMARY
// ==========================================
@Composable
private fun ExportSummaryCard(
    projectName: String,
    seriesName: String,
    seasonName: String,
    episodeName: String,
    sceneCount: Int,
    duration: String,
    format: String,
    quality: String,
    audioQuality: String,
    subtitles: Boolean
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "4. Resumo da exportação",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    SummaryItem("Projeto", projectName)
                    SummaryItem("Série", seriesName)
                    SummaryItem("Temporada", seasonName)
                    SummaryItem("Episódio", episodeName)
                    SummaryItem("Cenas selecionadas", "$sceneCount cenas")
                    SummaryItem("Duração total", duration)
                    SummaryItem("Formato", format)
                    SummaryItem("Qualidade", quality)
                    SummaryItem("Áudio", audioQuality)
                    SummaryItem("Legendas", if (subtitles) "Incluídas" else "Sem legendas")
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                fontSize = 12.sp
            )
        )
    }
}

// ==========================================
// RECENT EXPORTS CARD
// ==========================================
@Composable
private fun RecentExportsCard(
    onDownload: (name: String, format: String) -> Unit
) {
    val recents = listOf(
        Triple("Episódio 04", "MP4 • 1080p | 500 MB", "Hoje, 10:30"),
        Triple("Roteiro Temporada 1", "PDF | 2.4 MB", "Hoje, 09:15"),
        Triple("Personagens", "PNG | 45 MB", "Ontem, 18:20"),
        Triple("Trilha principal", "WAV | 28 MB", "Ontem, 16:45")
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Exportações recentes",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp
                    )
                )

                Text(
                    text = "Ver todas",
                    color = Color(0xFF0052FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                recents.forEach { (name, info, date) ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A),
                                        fontSize = 13.sp
                                    )
                                )
                                Text(
                                    text = "$info • $date",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF64748B),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            IconButton(
                                onClick = { onDownload(name, info.substringBefore(" ")) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Download,
                                    contentDescription = "Baixar",
                                    tint = Color(0xFF0052FF),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// SECTION 5: EXPORT HISTORY
// ==========================================
@Composable
private fun ExportHistoryCard(
    historyRecords: List<ExportRecordEntity>,
    onRecordClick: (ExportRecordEntity) -> Unit,
    onDownload: (ExportRecordEntity) -> Unit,
    onRetry: (ExportRecordEntity) -> Unit,
    onShare: (ExportRecordEntity) -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5. Histórico de exportações",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 15.sp
                    )
                )

                Text(
                    text = "Ver tudo",
                    color = Color(0xFF0052FF),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (historyRecords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhum registro de exportação ainda.", color = Color(0xFF94A3B8), fontSize = 12.sp)
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    historyRecords.take(6).forEach { record ->
                        Surface(
                            onClick = { onRecordClick(record) },
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Status icon
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (record.status) {
                                                    "Concluído" -> Color(0xFFDCFCE7)
                                                    "Processando" -> Color(0xFFFEF3C7)
                                                    else -> Color(0xFFFEE2E2)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = when (record.status) {
                                                "Concluído" -> "✓"
                                                "Processando" -> "⏳"
                                                else -> "✕"
                                            },
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = when (record.status) {
                                                "Concluído" -> Color(0xFF16A34A)
                                                "Processando" -> Color(0xFFD97706)
                                                else -> Color(0xFFDC2626)
                                            }
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Column {
                                        Text(
                                            text = record.name,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F172A),
                                                fontSize = 13.sp
                                            )
                                        )
                                        Text(
                                            text = "${record.type} • ${record.format} • ${record.sizeDisplay} • ${record.dateDisplay}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = Color(0xFF64748B),
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (record.status == "Concluído") {
                                        IconButton(
                                            onClick = { onDownload(record) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Download,
                                                contentDescription = "Baixar",
                                                tint = Color(0xFF0052FF),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else if (record.status == "Falhou") {
                                        IconButton(
                                            onClick = { onRetry(record) },
                                            modifier = Modifier.size(28.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Tentar",
                                                tint = Color(0xFFDC2626),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = null,
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ASSISTENTE RANGA CARD
// ==========================================
@Composable
private fun RangaExportAssistantCard(
    assistantInput: String,
    onInputChange: (String) -> Unit,
    assistantResponse: String,
    onQuickSuggestion: (String) -> Unit,
    onSend: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Assistente RANGA",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 14.5.sp
                        )
                    )
                    Text(
                        text = "Como posso ajudar na exportação?",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AI Response Bubble
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = assistantResponse,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF1E293B),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Suggestion Chips (2x2)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistantChip("Qual formato devo usar?", Modifier.weight(1f)) { onQuickSuggestion("Qual formato devo usar?") }
                    AssistantChip("Exportar apenas imagens", Modifier.weight(1f)) { onQuickSuggestion("Exportar apenas imagens") }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    AssistantChip("Exportar projeto inteiro", Modifier.weight(1f)) { onQuickSuggestion("Exportar projeto inteiro") }
                    AssistantChip("Preparar para publicação", Modifier.weight(1f)) { onQuickSuggestion("Preparar para publicação") }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Input field
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = assistantInput,
                    onValueChange = onInputChange,
                    placeholder = { Text("Digite sua pergunta...", color = Color(0xFF94A3B8), fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onSend,
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF0052FF))
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AssistantChip(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFEFF6FF),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(vertical = 5.dp, horizontal = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color(0xFF0052FF),
                fontWeight = FontWeight.Medium,
                fontSize = 10.5.sp
            )
        }
    }
}

// ==========================================
// REUSABLE DROPDOWN FIELD
// ==========================================
@Composable
private fun ExportDropdownField(
    label: String,
    value: String,
    options: List<String>,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF64748B),
                fontSize = 11.sp
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Surface(
            onClick = { expanded = true },
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF8FAFC),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF0F172A),
                        fontSize = 12.sp
                    ),
                    maxLines = 1
                )

                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(18.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontWeight = if (option == value) FontWeight.Bold else FontWeight.Normal,
                                color = if (option == value) Color(0xFF0052FF) else Color(0xFF0F172A),
                                fontSize = 12.5.sp
                            )
                        },
                        onClick = {
                            onSelect(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
