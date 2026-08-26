package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.ui.navigation.StudioDestination
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RoseError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SeasonsScreen(
    seasons: List<SeasonEntity>,
    seriesList: List<SeriesEntity>,
    projects: List<ProjectEntity> = emptyList(),
    episodes: List<EpisodeEntity> = emptyList(),
    characters: List<CharacterEntity> = emptyList(),
    scenarios: List<ScenarioEntity> = emptyList(),
    scenes: List<SceneEntity> = emptyList(),
    selectedProjectId: Long? = null,
    activeSeason: SeasonEntity? = null,
    activeSeries: SeriesEntity? = null,
    lastSaveTimestamp: Long = System.currentTimeMillis(),
    showNewSeasonDialog: Boolean = false,
    editingSeason: SeasonEntity? = null,
    onSetActiveSeason: (SeasonEntity?) -> Unit = {},
    onSetActiveSeries: (SeriesEntity?) -> Unit = {},
    onOpenNewSeason: (SeasonEntity?) -> Unit = {},
    onCloseNewSeason: () -> Unit = {},
    onSaveSeason: (SeasonEntity, Boolean) -> Unit = { _, _ -> },
    onDeleteSeason: (SeasonEntity) -> Unit = {},
    onDuplicateSeason: (SeasonEntity) -> Unit = {},
    onArchiveSeason: (SeasonEntity) -> Unit = {},
    onToggleFavoriteSeason: (SeasonEntity) -> Unit = {},
    onSaveEpisode: (EpisodeEntity, Boolean) -> Unit = { _, _ -> },
    onDeleteEpisode: (EpisodeEntity) -> Unit = {},
    onDuplicateEpisode: (EpisodeEntity) -> Unit = {},
    onArchiveEpisode: (EpisodeEntity) -> Unit = {},
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToDestination: (StudioDestination) -> Unit = {}
) {
    val context = LocalContext.current

    // Local Filter & Search State
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("Todas") }
    var selectedSeriesFilterId by remember { mutableStateOf<Long?>(null) }
    var isSeriesDropdownExpanded by remember { mutableStateOf(false) }

    // Dialogs
    var seasonToDelete by remember { mutableStateOf<SeasonEntity?>(null) }
    var episodeToDelete by remember { mutableStateOf<EpisodeEntity?>(null) }
    var showEpisodeModal by remember { mutableStateOf(false) }
    var showScriptModal by remember { mutableStateOf(false) }
    var showCoverModal by remember { mutableStateOf(false) }
    var showAllEpisodesDialog by remember { mutableStateOf(false) }

    // Resolve current selected series (defaults to first series or activeSeries)
    val currentSelectedSeries = remember(seriesList, selectedSeriesFilterId, activeSeries) {
        if (selectedSeriesFilterId != null) {
            seriesList.firstOrNull { it.id == selectedSeriesFilterId }
        } else {
            activeSeries ?: seriesList.firstOrNull()
        }
    }

    LaunchedEffect(currentSelectedSeries) {
        if (selectedSeriesFilterId == null && currentSelectedSeries != null) {
            selectedSeriesFilterId = currentSelectedSeries.id
        }
    }

    // Filter seasons by current selected series, search query and status filter
    val currentSeriesSeasons = remember(seasons, currentSelectedSeries, searchQuery, selectedStatusFilter) {
        seasons.filter { season ->
            val matchSeries = currentSelectedSeries == null || season.seriesId == currentSelectedSeries.id
            val matchSearch = searchQuery.isBlank() ||
                    season.title.contains(searchQuery, ignoreCase = true) ||
                    season.synopsis.contains(searchQuery, ignoreCase = true)
            val matchStatus = when (selectedStatusFilter) {
                "Todas" -> !season.isArchived
                "Em produção" -> season.status.equals("Em produção", ignoreCase = true) && !season.isArchived
                "Concluídas" -> season.status.equals("Concluída", ignoreCase = true) && !season.isArchived
                "Em pausa" -> season.status.equals("Em pausa", ignoreCase = true) && !season.isArchived
                "Arquivadas" -> season.isArchived || season.status.equals("Arquivada", ignoreCase = true)
                else -> true
            }
            matchSeries && matchSearch && matchStatus
        }.sortedBy { it.seasonNumber }
    }

    // Resolve current active season
    val currentActiveSeason = activeSeason?.takeIf { currentSeriesSeasons.any { s -> s.id == it.id } }
        ?: currentSeriesSeasons.firstOrNull()

    LaunchedEffect(currentActiveSeason) {
        if (activeSeason == null && currentActiveSeason != null) {
            onSetActiveSeason(currentActiveSeason)
        }
    }

    // Filter episodes for active season
    val activeSeasonEpisodes = remember(episodes, currentActiveSeason) {
        if (currentActiveSeason == null) emptyList()
        else episodes.filter { it.seasonId == currentActiveSeason.id }.sortedBy { it.episodeNumber }
    }

    // Main Canvas
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        val isWideScreen = maxWidth >= 960.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isWideScreen) 24.dp else 14.dp, vertical = 12.dp)
        ) {
            // 1. TOP HEADER (Icon, Title, Subtitle, Series Selector, Search, Bell, Profile)
            SeasonsHeader(
                seriesList = seriesList,
                currentSeries = currentSelectedSeries,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onSelectSeries = { newSeries ->
                    selectedSeriesFilterId = newSeries.id
                    onSetActiveSeries(newSeries)
                    val firstSeason = seasons.firstOrNull { it.seriesId == newSeries.id }
                    onSetActiveSeason(firstSeason)
                },
                isDropdownExpanded = isSeriesDropdownExpanded,
                onToggleDropdown = { isSeriesDropdownExpanded = !isSeriesDropdownExpanded }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 2. STATUS FILTER BAR & NEW SEASON BUTTON
            SeasonsFilterBar(
                selectedFilter = selectedStatusFilter,
                onFilterSelected = { selectedStatusFilter = it },
                lastSaveTimestamp = lastSaveTimestamp,
                onOpenNewSeason = { onOpenNewSeason(null) }
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3. MAIN CONTENT: ADAPTIVE 2-PANEL / STACKED LAYOUT
            if (currentSeriesSeasons.isEmpty()) {
                EmptySeasonsState(
                    currentSeriesTitle = currentSelectedSeries?.title ?: "Série",
                    onOpenNewSeason = { onOpenNewSeason(null) }
                )
            } else {
                if (isWideScreen) {
                    // DESKTOP / TABLET HORIZONTAL SPLIT
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // LEFT PANEL: List of Season Cards
                        Column(
                            modifier = Modifier
                                .weight(0.36f)
                                .fillMaxHeight()
                        ) {
                            Text(
                                text = "Temporadas da série (${currentSeriesSeasons.size})",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(currentSeriesSeasons, key = { it.id }) { season ->
                                    SeasonCardItem(
                                        season = season,
                                        isSelected = currentActiveSeason?.id == season.id,
                                        onSelect = { onSetActiveSeason(season) },
                                        onEdit = { onOpenNewSeason(season) },
                                        onDuplicate = { onDuplicateSeason(season) },
                                        onArchive = { onArchiveSeason(season) },
                                        onDelete = { seasonToDelete = season }
                                    )
                                }
                            }
                        }

                        // RIGHT PANEL: Full Season Profile & Deep Production Hub
                        if (currentActiveSeason != null) {
                            Column(
                                modifier = Modifier
                                    .weight(0.64f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                // Hero Banner Card
                                SeasonHeroBanner(
                                    season = currentActiveSeason,
                                    series = currentSelectedSeries,
                                    onEdit = { onOpenNewSeason(currentActiveSeason) },
                                    onContinueProduction = {
                                        onNavigateToDestination(StudioDestination.SCENES)
                                    },
                                    onNewEpisode = { showEpisodeModal = true },
                                    onChangeCover = { showCoverModal = true }
                                )

                                // 6 Metric Stat Badges
                                SeasonStatsGrid(
                                    season = currentActiveSeason,
                                    episodes = activeSeasonEpisodes,
                                    scenesCount = currentActiveSeason.scenesCount,
                                    charactersCount = currentActiveSeason.charactersCount,
                                    scenariosCount = currentActiveSeason.scenariosCount,
                                    totalDuration = currentActiveSeason.totalDuration
                                )

                                // Two-Column Sub Grid: Episodes on Left, Characters/Scenarios/AI on Right
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                                ) {
                                    // Left Column: Episodes of the Season
                                    Column(
                                        modifier = Modifier.weight(0.55f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        SeasonEpisodesSection(
                                            season = currentActiveSeason,
                                            episodes = activeSeasonEpisodes,
                                            onNewEpisode = { showEpisodeModal = true },
                                            onEditEpisode = { ep ->
                                                Toast.makeText(context, "Abrindo episódio ${ep.episodeNumber}: ${ep.title}", Toast.LENGTH_SHORT).show()
                                            },
                                            onDeleteEpisode = { ep -> episodeToDelete = ep },
                                            onDuplicateEpisode = { ep -> onDuplicateEpisode(ep) },
                                            onViewAll = { showAllEpisodesDialog = true }
                                        )
                                    }

                                    // Right Column: Characters, Scenarios & AI Assistant
                                    Column(
                                        modifier = Modifier.weight(0.45f),
                                        verticalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        // Characters Section
                                        SeasonCharactersSection(
                                            characters = characters,
                                            onViewAll = { onNavigateToDestination(StudioDestination.CHARACTERS) }
                                        )

                                        // Scenarios Section
                                        SeasonScenariosSection(
                                            scenarios = scenarios,
                                            onViewAll = { onNavigateToDestination(StudioDestination.SCENARIOS) }
                                        )

                                        // Ranga AI Assistant Card
                                        SeasonAiAssistantCard(
                                            season = currentActiveSeason,
                                            series = currentSelectedSeries,
                                            onOpenAiWithPrompt = onOpenAiWithPrompt
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // MOBILE VERTICAL SCROLL LAYOUT
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 60.dp)
                    ) {
                        item {
                            Text(
                                text = "Temporadas da série",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep
                            )
                        }

                        // Horizontal season carousel for mobile selection
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(currentSeriesSeasons, key = { it.id }) { season ->
                                    SeasonCardItemMobile(
                                        season = season,
                                        isSelected = currentActiveSeason?.id == season.id,
                                        onSelect = { onSetActiveSeason(season) }
                                    )
                                }
                            }
                        }

                        if (currentActiveSeason != null) {
                            item {
                                SeasonHeroBanner(
                                    season = currentActiveSeason,
                                    series = currentSelectedSeries,
                                    onEdit = { onOpenNewSeason(currentActiveSeason) },
                                    onContinueProduction = {
                                        onNavigateToDestination(StudioDestination.SCENES)
                                    },
                                    onNewEpisode = { showEpisodeModal = true },
                                    onChangeCover = { showCoverModal = true }
                                )
                            }

                            item {
                                SeasonStatsGrid(
                                    season = currentActiveSeason,
                                    episodes = activeSeasonEpisodes,
                                    scenesCount = currentActiveSeason.scenesCount,
                                    charactersCount = currentActiveSeason.charactersCount,
                                    scenariosCount = currentActiveSeason.scenariosCount,
                                    totalDuration = currentActiveSeason.totalDuration
                                )
                            }

                            item {
                                SeasonEpisodesSection(
                                    season = currentActiveSeason,
                                    episodes = activeSeasonEpisodes,
                                    onNewEpisode = { showEpisodeModal = true },
                                    onEditEpisode = { ep ->
                                        Toast.makeText(context, "Abrindo episódio ${ep.episodeNumber}", Toast.LENGTH_SHORT).show()
                                    },
                                    onDeleteEpisode = { ep -> episodeToDelete = ep },
                                    onDuplicateEpisode = { ep -> onDuplicateEpisode(ep) },
                                    onViewAll = { showAllEpisodesDialog = true }
                                )
                            }

                            item {
                                SeasonCharactersSection(
                                    characters = characters,
                                    onViewAll = { onNavigateToDestination(StudioDestination.CHARACTERS) }
                                )
                            }

                            item {
                                SeasonScenariosSection(
                                    scenarios = scenarios,
                                    onViewAll = { onNavigateToDestination(StudioDestination.SCENARIOS) }
                                )
                            }

                            item {
                                SeasonAiAssistantCard(
                                    season = currentActiveSeason,
                                    series = currentSelectedSeries,
                                    onOpenAiWithPrompt = onOpenAiWithPrompt
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // MODALS & DIALOGS
    // -------------------------------------------------------------

    // 1. New / Edit Season Modal
    if (showNewSeasonDialog) {
        SeasonFormModal(
            season = editingSeason,
            seriesList = seriesList,
            defaultSeriesId = currentSelectedSeries?.id ?: 1L,
            onDismiss = onCloseNewSeason,
            onSave = { updatedSeason ->
                onSaveSeason(updatedSeason, true)
            },
            onOpenAiWithPrompt = onOpenAiWithPrompt
        )
    }

    // 2. New Quick Episode Modal
    if (showEpisodeModal && currentActiveSeason != null) {
        QuickEpisodeModal(
            season = currentActiveSeason,
            projectId = currentSelectedSeries?.projectId ?: 1L,
            nextEpisodeNumber = (activeSeasonEpisodes.maxOfOrNull { it.episodeNumber } ?: 0) + 1,
            onDismiss = { showEpisodeModal = false },
            onSave = { newEpisode ->
                onSaveEpisode(newEpisode, true)
                showEpisodeModal = false
            }
        )
    }

    // 3. Season Cover Art Gallery & AI Generator Modal
    if (showCoverModal && currentActiveSeason != null) {
        SeasonCoverModal(
            season = currentActiveSeason,
            onDismiss = { showCoverModal = false },
            onSelectCover = { coverKey ->
                onSaveSeason(currentActiveSeason.copy(coverUri = coverKey), false)
                showCoverModal = false
            },
            onGenerateWithAi = { prompt ->
                onOpenAiWithPrompt(prompt)
                showCoverModal = false
            }
        )
    }

    // 4. View All Episodes Modal
    if (showAllEpisodesDialog && currentActiveSeason != null) {
        AllEpisodesDialog(
            season = currentActiveSeason,
            episodes = activeSeasonEpisodes,
            onDismiss = { showAllEpisodesDialog = false },
            onNewEpisode = {
                showAllEpisodesDialog = false
                showEpisodeModal = true
            },
            onDeleteEpisode = { ep ->
                onDeleteEpisode(ep)
            }
        )
    }

    // 5. Delete Season Confirmation Dialog
    seasonToDelete?.let { targetSeason ->
        AlertDialog(
            onDismissRequest = { seasonToDelete = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError)
                    Text("Excluir Temporada", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Text(
                    "Tem certeza que deseja excluir '${targetSeason.title}'? Esta ação removerá a organização da temporada.",
                    color = NavyDark,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteSeason(targetSeason)
                        seasonToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { seasonToDelete = null }) {
                    Text("Cancelar", color = NavyPrimary)
                }
            }
        )
    }

    // 6. Delete Episode Confirmation Dialog
    episodeToDelete?.let { targetEp ->
        AlertDialog(
            onDismissRequest = { episodeToDelete = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError)
                    Text("Excluir Episódio", fontWeight = FontWeight.Bold, color = NavyDeep)
                }
            },
            text = {
                Text(
                    "Deseja excluir o episódio ${targetEp.episodeNumber}: '${targetEp.title}'?",
                    color = NavyDark,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteEpisode(targetEp)
                        episodeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { episodeToDelete = null }) {
                    Text("Cancelar", color = NavyPrimary)
                }
            }
        )
    }
}

// -------------------------------------------------------------
// 1. HEADER COMPONENT WITH SERIES SELECTOR
// -------------------------------------------------------------
@Composable
private fun SeasonsHeader(
    seriesList: List<SeriesEntity>,
    currentSeries: SeriesEntity?,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSelectSeries: (SeriesEntity) -> Unit,
    isDropdownExpanded: Boolean,
    onToggleDropdown: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Title & Subtitle
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f, fill = false)) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFEFF6FF),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CollectionsBookmark,
                        contentDescription = "Temporadas",
                        tint = NavyPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Temporadas",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
                Text(
                    text = "Organize cada fase da sua série em temporadas e acompanhe os episódios.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Right Action Group: Series Selector, Search, Bell, User Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // SERIES SELECTOR DROPDOWN
            Box {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier
                        .clickable { onToggleDropdown() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Series Thumbnail
                        Image(
                            painter = painterResource(
                                id = getSeriesCoverDrawable(currentSeries?.coverUri, currentSeries?.title ?: "")
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Série atual", fontSize = 9.sp, color = Color(0xFF64748B), lineHeight = 10.sp)
                            Text(
                                text = currentSeries?.title ?: "Selecione uma série",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Selecionar série",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = onToggleDropdown,
                    modifier = Modifier.background(Color.White)
                ) {
                    seriesList.forEach { s ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(
                                        painter = painterResource(id = getSeriesCoverDrawable(s.coverUri, s.title)),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(s.title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = NavyDeep)
                                        Text("${s.seasonsCount} Temporadas • ${s.episodesCount} Eps", fontSize = 10.sp, color = Color(0xFF64748B))
                                    }
                                }
                            },
                            onClick = {
                                onSelectSeries(s)
                                onToggleDropdown()
                            }
                        )
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text("Pesquisar temporadas...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }, modifier = Modifier.size(20.dp)) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NavyPrimary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                ),
                modifier = Modifier
                    .width(210.dp)
                    .height(44.dp)
            )

            // Notifications Bell with Badge
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(10.dp))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificações",
                    tint = NavyDeep,
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-4).dp, y = 4.dp)
                        .size(16.dp)
                        .background(Color(0xFFEF4444), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            // User Profile Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.char_antonio),
                    contentDescription = "Augusto",
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("Augusto", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("Plano Profissional", fontSize = 9.sp, color = NavyPrimary)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. STATUS FILTER BAR & NEW SEASON BUTTON
// -------------------------------------------------------------
@Composable
private fun SeasonsFilterBar(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    lastSaveTimestamp: Long,
    onOpenNewSeason: () -> Unit
) {
    val filterOptions = listOf("Todas", "Em produção", "Concluídas", "Em pausa", "Arquivadas")

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Status Filter Chips
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            filterOptions.forEach { filter ->
                val isSelected = selectedFilter == filter
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) NavyDeep else Color.White,
                    border = BorderStroke(1.dp, if (isSelected) NavyDeep else Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .clickable { onFilterSelected(filter) }
                        .padding(horizontal = 14.dp, vertical = 7.dp)
                ) {
                    Text(
                        text = filter,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Auto-save status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color(0xFFECFDF5), RoundedCornerShape(12.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Box(modifier = Modifier.size(6.dp).background(Color(0xFF10B981), CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Salvo", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF059669))
            }
        }

        // Action Button: + Nova Temporada
        Button(
            onClick = onOpenNewSeason,
            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            modifier = Modifier.testTag("new_season_button")
        ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Nova Temporada", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

// -------------------------------------------------------------
// 3. SEASON CARD ITEM (MASTER LIST)
// -------------------------------------------------------------
@Composable
private fun SeasonCardItem(
    season: SeasonEntity,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F7FF) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cover Thumbnail
                Box(
                    modifier = Modifier
                        .size(width = 80.dp, height = 95.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = getSeriesCoverDrawable(season.coverUri, season.title)),
                        contentDescription = season.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Season Title & Details
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Temporada ${season.seasonNumber}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )

                        if (isSelected) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF2563EB),
                                modifier = Modifier.size(20.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Check, contentDescription = "Selecionada", tint = Color.White, modifier = Modifier.size(12.dp))
                                }
                            }
                        }
                    }

                    Text(
                        text = season.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = season.synopsis.ifBlank { "Nossos amigos frutas descobrem um mistério que mudará suas vidas." },
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // 4 Mini Stats Row (Episódios, Concluídos, Em produção, Rascunhos)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MiniStatItem("${season.episodesCount}", "Episódios", Color(0xFF0F172A))
                        MiniStatItem("✔ ${season.completedEpisodesCount}", "Concluídos", Color(0xFF10B981))
                        MiniStatItem("⚡ ${season.inProductionEpisodesCount}", "Em produção", Color(0xFF0284C7))
                        MiniStatItem("✏ ${season.draftEpisodesCount}", "Rascunhos", Color(0xFFF59E0B))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress Bar & Percentage
            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { season.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (season.progressPercent >= 100) Color(0xFF10B981) else Color(0xFF2563EB),
                    trackColor = Color(0xFFE2E8F0)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "${season.progressPercent}% concluído",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Footer: Status Badge, Last Update, Menu ⋮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Badge
                StatusPill(status = season.status)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Atualizada há 2 horas",
                        fontSize = 10.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Box {
                        IconButton(
                            onClick = { menuExpanded = true },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Opções",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar temporada", fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Duplicar temporada", fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onDuplicate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Arquivar temporada", fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onArchive()
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Excluir", color = RoseError, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuExpanded = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. MOBILE SEASON CARD (CAROUSEL ITEM)
// -------------------------------------------------------------
@Composable
private fun SeasonCardItemMobile(
    season: SeasonEntity,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onSelect() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F7FF) else Color.White
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) Color(0xFF2563EB) else Color(0xFFE2E8F0)
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = getSeriesCoverDrawable(season.coverUri, season.title)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Temp. ${season.seasonNumber}", fontSize = 10.sp, color = Color(0xFF64748B))
                    Text(season.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep, maxLines = 1)
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { season.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color(0xFF2563EB),
                trackColor = Color(0xFFE2E8F0)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("${season.episodesCount} eps", fontSize = 10.sp, color = Color(0xFF64748B))
                Text("${season.progressPercent}%", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
            }
        }
    }
}

// -------------------------------------------------------------
// 5. SEASON HERO BANNER (PROFILE OF SELECTED SEASON)
// -------------------------------------------------------------
@Composable
private fun SeasonHeroBanner(
    season: SeasonEntity,
    series: SeriesEntity?,
    onEdit: () -> Unit,
    onContinueProduction: () -> Unit,
    onNewEpisode: () -> Unit,
    onChangeCover: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Big 3D Cover Image with hover edit button
            Box(
                modifier = Modifier
                    .width(190.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onChangeCover() }
            ) {
                Image(
                    painter = painterResource(id = getSeriesCoverDrawable(season.coverUri, season.title)),
                    contentDescription = season.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = Color.White, modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Trocar", color = Color.White, fontSize = 9.sp)
                    }
                }
            }

            // Info Details
            Column(modifier = Modifier.weight(1f)) {
                // Top Row: Season Label, Title, Edit & Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Temporada ${season.seasonNumber}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF64748B)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = season.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = onEdit, modifier = Modifier.size(22.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                            }
                        }
                    }

                    // Action Buttons (Editar, Continuar Produção, + Novo Episódio)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = onEdit,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp), tint = NavyDeep)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar temporada", fontSize = 11.sp, color = NavyDeep, fontWeight = FontWeight.SemiBold)
                        }

                        Button(
                            onClick = onContinueProduction,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Continuar produção", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onNewEpisode,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFF6FF)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2563EB))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Novo episódio", fontSize = 11.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Description
                Text(
                    text = season.synopsis.ifBlank {
                        "Primeira temporada da série ${series?.title ?: "Aventuras das Frutas"}. Nossos heróis vivem grandes aventuras enquanto descobrem o valor da amizade."
                    },
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    lineHeight = 16.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Meta Chips Row: Start Date, End Date, Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MetaDateChip(label = "Início", date = season.startDate)
                    MetaDateChip(label = "Previsão de término", date = season.endDate)
                    StatusPill(status = season.status)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. 6 KEY METRIC STAT BADGES
// -------------------------------------------------------------
@Composable
private fun SeasonStatsGrid(
    season: SeasonEntity,
    episodes: List<EpisodeEntity>,
    scenesCount: Int,
    charactersCount: Int,
    scenariosCount: Int,
    totalDuration: String
) {
    val completedEps = episodes.count { it.status.equals("Concluído", ignoreCase = true) }
        .coerceAtLeast(season.completedEpisodesCount)
    val totalEps = episodes.size.coerceAtLeast(season.episodesCount)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatBox(icon = Icons.Default.Movie, count = "$totalEps", label = "Episódios", color = Color(0xFF2563EB), modifier = Modifier.weight(1f))
        StatBox(icon = Icons.Default.VideoLibrary, count = "$scenesCount", label = "Cenas", color = Color(0xFF6366F1), modifier = Modifier.weight(1f))
        StatBox(icon = Icons.Default.Person, count = "$charactersCount", label = "Personagens", color = Color(0xFF0284C7), modifier = Modifier.weight(1f))
        StatBox(icon = Icons.Default.Landscape, count = "$scenariosCount", label = "Cenários", color = Color(0xFF0D9488), modifier = Modifier.weight(1f))
        StatBox(icon = Icons.Default.Timer, count = totalDuration, label = "Duração total", color = Color(0xFF7C3AED), modifier = Modifier.weight(1f))
        StatBox(icon = Icons.Default.CheckCircle, count = "$completedEps", label = "Concluídos", color = Color(0xFF10B981), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                }
            }
            Column {
                Text(count, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                Text(label, fontSize = 10.sp, color = Color(0xFF64748B), maxLines = 1)
            }
        }
    }
}

// -------------------------------------------------------------
// 7. EPISODES OF THE SEASON SECTION
// -------------------------------------------------------------
@Composable
private fun SeasonEpisodesSection(
    season: SeasonEntity,
    episodes: List<EpisodeEntity>,
    onNewEpisode: () -> Unit,
    onEditEpisode: (EpisodeEntity) -> Unit,
    onDeleteEpisode: (EpisodeEntity) -> Unit,
    onDuplicateEpisode: (EpisodeEntity) -> Unit,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Episódios da temporada",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )

                TextButton(
                    onClick = onNewEpisode,
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2563EB))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Novo episódio", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2563EB))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (episodes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Nenhum episódio cadastrado nesta temporada.", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = onNewEpisode,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+ Criar primeiro episódio", fontSize = 11.sp)
                        }
                    }
                }
            } else {
                // List of Episode rows
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    episodes.take(5).forEach { ep ->
                        EpisodeListItem(
                            episode = ep,
                            onClick = { onEditEpisode(ep) },
                            onDelete = { onDeleteEpisode(ep) },
                            onDuplicate = { onDuplicateEpisode(ep) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Footer button: Ver todos os episódios
                OutlinedButton(
                    onClick = onViewAll,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Text("Ver todos os episódios (${episodes.size})", fontSize = 12.sp, color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun EpisodeListItem(
    episode: EpisodeEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onDuplicate: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Episode Thumbnail
            Image(
                painter = painterResource(id = getSeriesCoverDrawable(episode.coverUri, episode.title)),
                contentDescription = null,
                modifier = Modifier
                    .size(width = 54.dp, height = 48.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop
            )

            // Middle Column: Number, Title, Desc, Meta
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${episode.episodeNumber}. ${episode.title}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = episode.description.ifBlank { "Nossos heróis começam a investigar o caso." },
                    fontSize = 10.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VideoLibrary, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color(0xFF94A3B8))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text("${episode.scenesCount} cenas", fontSize = 9.sp, color = Color(0xFF64748B))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(10.dp), tint = Color(0xFF94A3B8))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(episode.duration, fontSize = 9.sp, color = Color(0xFF64748B))
                    }
                }
            }

            // Right Column: Status pill & mini progress bar
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(85.dp)
            ) {
                StatusPill(status = episode.status)
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { episode.progressPercent / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = when {
                        episode.progressPercent >= 100 -> Color(0xFF10B981)
                        episode.progressPercent >= 50 -> Color(0xFF2563EB)
                        else -> Color(0xFFF59E0B)
                    },
                    trackColor = Color(0xFFE2E8F0)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text("${episode.progressPercent}%", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
            }
        }
    }
}

// -------------------------------------------------------------
// 8. CHARACTERS OF THE SEASON SECTION
// -------------------------------------------------------------
@Composable
private fun SeasonCharactersSection(
    characters: List<CharacterEntity>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Personagens principais",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
                Text(
                    text = "Ver todos",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onViewAll() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Character Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val displayChars = if (characters.isNotEmpty()) {
                    characters.take(4)
                } else {
                    listOf(
                        CharacterEntity(id = 1, name = "António", imageUri = "char_antonio", role = "Protagonista", personality = ""),
                        CharacterEntity(id = 2, name = "Bia", imageUri = "char_bia", role = "Protagonista", personality = ""),
                        CharacterEntity(id = 3, name = "Carlos", imageUri = "char_carlos", role = "Coadjuvante", personality = ""),
                        CharacterEntity(id = 4, name = "Lima", imageUri = "char_lima", role = "Coadjuvante", personality = "")
                    )
                }

                displayChars.forEach { ch ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onViewAll() }
                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = getCharacterDrawable(ch.imageUri, ch.name)),
                                contentDescription = ch.name,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = ch.name,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                maxLines = 1
                            )
                            Text(
                                text = "8 episódios",
                                fontSize = 9.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 9. SCENARIOS OF THE SEASON SECTION
// -------------------------------------------------------------
@Composable
private fun SeasonScenariosSection(
    scenarios: List<ScenarioEntity>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Cenários principais",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
                Text(
                    text = "Ver todos",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.clickable { onViewAll() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4 Scenario Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val displayScenarios = if (scenarios.isNotEmpty()) {
                    scenarios.take(4)
                } else {
                    listOf(
                        ScenarioEntity(id = 1, name = "Cidade Frutal", imageUri = "scenario_city_1787049273004"),
                        ScenarioEntity(id = 2, name = "Casa do António", imageUri = "scenario_family_house_1787049241112"),
                        ScenarioEntity(id = 3, name = "Escola Frutal", imageUri = "scenario_school_1787049257901"),
                        ScenarioEntity(id = 4, name = "Parque Central", imageUri = "scenario_forest_1787049320048")
                    )
                }

                displayScenarios.forEach { sc ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onViewAll() }
                    ) {
                        Column(
                            modifier = Modifier.padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = getScenarioDrawable(sc.imageUri, sc.name)),
                                contentDescription = sc.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = sc.name,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDeep,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "9 episódios",
                                fontSize = 8.sp,
                                color = Color(0xFF64748B),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 10. RANGA AI ASSISTANT CARD FOR SEASONS
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeasonAiAssistantCard(
    season: SeasonEntity,
    series: SeriesEntity?,
    onOpenAiWithPrompt: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FDF4).copy(alpha = 0.8f)
        ),
        border = BorderStroke(1.dp, Color(0xFFBBF7D0))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF10B981).copy(alpha = 0.2f),
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("Assistente RANGA", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("Como posso ajudar no desenvolvimento desta temporada?", fontSize = 10.sp, color = Color(0xFF475569))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // AI Prompt Pills
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                AiPromptPill(
                    text = "Ideias de episódios",
                    onClick = {
                        onOpenAiWithPrompt("Gere 5 ideias detalhadas de episódios para a Temporada ${season.seasonNumber} ('${season.title}') da série '${series?.title ?: "Aventuras das Frutas"}'.")
                    }
                )
                AiPromptPill(
                    text = "Arco da história",
                    onClick = {
                        onOpenAiWithPrompt("Desenvolva um arco narrativo completo com início, clímax e desfecho para a Temporada ${season.seasonNumber} ('${season.title}').")
                    }
                )
                AiPromptPill(
                    text = "Desenvolver personagens",
                    onClick = {
                        onOpenAiWithPrompt("Sugira como os personagens principais (António, Bia, Carlos) podem evoluir e superar conflitos ao longo desta temporada.")
                    }
                )
                AiPromptPill(
                    text = "Criar sinopse da temporada",
                    onClick = {
                        onOpenAiWithPrompt("Escreva uma sinopse cativante e profissional para a Temporada ${season.seasonNumber} da série '${series?.title ?: "Aventuras das Frutas"}'.")
                    }
                )
            }
        }
    }
}

@Composable
private fun AiPromptPill(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFF86EFAC)),
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = NavyDeep)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(10.dp))
        }
    }
}

// -------------------------------------------------------------
// 11. EMPTY STATE
// -------------------------------------------------------------
@Composable
private fun EmptySeasonsState(
    currentSeriesTitle: String,
    onOpenNewSeason: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFEFF6FF),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CollectionsBookmark,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nenhuma temporada encontrada",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDeep
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Crie a primeira temporada da série '$currentSeriesTitle' para organizar episódios e cenas.",
                fontSize = 13.sp,
                color = Color(0xFF64748B),
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 420.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onOpenNewSeason,
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Criar Nova Temporada", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// 12. HELPER PILLS & METAS
// -------------------------------------------------------------
@Composable
private fun StatusPill(status: String) {
    val (bg, textColor) = when (status) {
        "Concluída", "Concluído" -> Color(0xFFECFDF5) to Color(0xFF059669)
        "Em produção" -> Color(0xFFEFF6FF) to Color(0xFF2563EB)
        "Planejamento", "Rascunho" -> Color(0xFFFFFBEB) to Color(0xFFD97706)
        "Em pausa" -> Color(0xFFF1F5F9) to Color(0xFF475569)
        "Arquivada" -> Color(0xFFF3F4F6) to Color(0xFF1F2937)
        else -> Color(0xFFEFF6FF) to Color(0xFF2563EB)
    }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(textColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
private fun MetaDateChip(label: String, date: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(12.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$label: $date",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF475569)
            )
        }
    }
}

@Composable
private fun MiniStatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 8.sp, color = Color(0xFF64748B))
    }
}

// -------------------------------------------------------------
// 13. NEW / EDIT SEASON FORM MODAL
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeasonFormModal(
    season: SeasonEntity?,
    seriesList: List<SeriesEntity>,
    defaultSeriesId: Long,
    onDismiss: () -> Unit,
    onSave: (SeasonEntity) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit
) {
    var title by remember { mutableStateOf(season?.title ?: "") }
    var seasonNumber by remember { mutableIntStateOf(season?.seasonNumber ?: 1) }
    var selectedSeriesId by remember { mutableStateOf(season?.seriesId ?: defaultSeriesId) }
    var synopsis by remember { mutableStateOf(season?.synopsis ?: "") }
    var startDate by remember { mutableStateOf(season?.startDate ?: "12/05/2024") }
    var endDate by remember { mutableStateOf(season?.endDate ?: "12/12/2024") }
    var status by remember { mutableStateOf(season?.status ?: "Em produção") }
    var coverUri by remember { mutableStateOf(season?.coverUri ?: "cover_frutinhas") }

    val statusOptions = listOf("Planejamento", "Em produção", "Concluída", "Em pausa", "Arquivada")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (season == null) Icons.Default.Add else Icons.Default.Edit,
                            contentDescription = null,
                            tint = NavyPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                Text(
                    text = if (season == null) "Criar Nova Temporada" else "Editar Temporada",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
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
                // Season Title Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nome da temporada") },
                    placeholder = { Text("ex: A Grande Aventura") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Season Number & Series
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = seasonNumber.toString(),
                        onValueChange = { seasonNumber = it.toIntOrNull() ?: seasonNumber },
                        label = { Text("Número da temporada") },
                        singleLine = true,
                        modifier = Modifier.weight(0.4f)
                    )

                    // Series selector
                    var seriesMenuExpanded by remember { mutableStateOf(false) }
                    val currentSeriesName = seriesList.firstOrNull { it.id == selectedSeriesId }?.title ?: "Série"

                    Box(modifier = Modifier.weight(0.6f)) {
                        OutlinedTextField(
                            value = currentSeriesName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Série") },
                            trailingIcon = {
                                IconButton(onClick = { seriesMenuExpanded = true }) {
                                    Icon(Icons.Default.ExpandMore, contentDescription = null)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { seriesMenuExpanded = true }
                        )

                        DropdownMenu(
                            expanded = seriesMenuExpanded,
                            onDismissRequest = { seriesMenuExpanded = false }
                        ) {
                            seriesList.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s.title) },
                                    onClick = {
                                        selectedSeriesId = s.id
                                        seriesMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Description / Synopsis
                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    label = { Text("Descrição / Sinopse da temporada") },
                    placeholder = { Text("Descreva o que acontece nesta temporada...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                // Dates (Start & End)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Data de início") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Previsão de término") },
                        placeholder = { Text("dd/mm/aaaa") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Status Chips
                Text("Status da temporada:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statusOptions.forEach { opt ->
                        FilterChip(
                            selected = status == opt,
                            onClick = { status = opt },
                            label = { Text(opt, fontSize = 11.sp) }
                        )
                    }
                }

                // AI Assist Button
                Button(
                    onClick = {
                        onOpenAiWithPrompt("Gere um título e uma sinopse cativante para a Temporada $seasonNumber da série.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gerar Ideias com IA para esta Temporada", fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalSeason = (season ?: SeasonEntity(seriesId = selectedSeriesId)).copy(
                        seriesId = selectedSeriesId,
                        seasonNumber = seasonNumber,
                        title = title.ifBlank { "Temporada $seasonNumber" },
                        synopsis = synopsis,
                        startDate = startDate,
                        endDate = endDate,
                        status = status,
                        coverUri = coverUri,
                        updatedAt = System.currentTimeMillis()
                    )
                    onSave(finalSeason)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep)
            ) {
                Text("Salvar Temporada", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B))
            }
        }
    )
}

// -------------------------------------------------------------
// 14. QUICK EPISODE MODAL
// -------------------------------------------------------------
@Composable
private fun QuickEpisodeModal(
    season: SeasonEntity,
    projectId: Long,
    nextEpisodeNumber: Int,
    onDismiss: () -> Unit,
    onSave: (EpisodeEntity) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var episodeNumber by remember { mutableIntStateOf(nextEpisodeNumber) }
    var duration by remember { mutableStateOf("22 min") }
    var scenesCount by remember { mutableIntStateOf(8) }
    var status by remember { mutableStateOf("Em produção") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Movie, contentDescription = null, tint = NavyPrimary)
                Text("Novo Episódio — ${season.title}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = episodeNumber.toString(),
                        onValueChange = { episodeNumber = it.toIntOrNull() ?: episodeNumber },
                        label = { Text("Nº") },
                        modifier = Modifier.weight(0.3f)
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título do episódio") },
                        placeholder = { Text("ex: O Desaparecimento") },
                        modifier = Modifier.weight(0.7f)
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Sinopse rápida") },
                    placeholder = { Text("O que acontece neste episódio?") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duração") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = scenesCount.toString(),
                        onValueChange = { scenesCount = it.toIntOrNull() ?: scenesCount },
                        label = { Text("Cenas estimadas") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        EpisodeEntity(
                            seasonId = season.id,
                            projectId = projectId,
                            episodeNumber = episodeNumber,
                            title = title.ifBlank { "Episódio $episodeNumber" },
                            description = description,
                            duration = duration,
                            scenesCount = scenesCount,
                            status = status,
                            progressPercent = 10,
                            coverUri = season.coverUri
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep)
            ) {
                Text("Adicionar Episódio", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B))
            }
        }
    )
}

// -------------------------------------------------------------
// 15. SEASON COVER MODAL (GALLERY & AI)
// -------------------------------------------------------------
@Composable
private fun SeasonCoverModal(
    season: SeasonEntity,
    onDismiss: () -> Unit,
    onSelectCover: (String) -> Unit,
    onGenerateWithAi: (String) -> Unit
) {
    val presets = listOf(
        "cover_frutinhas" to "Frutinhas 3D",
        "cover_reino_encantado" to "Reino Encantado",
        "cover_missao_estelar" to "Missão Estelar",
        "cover_misterio_cidade" to "Mistério na Cidade",
        "cover_herois_escola" to "Heróis da Escola",
        "cover_fazenda_divertida" to "Fazenda Divertida",
        "cover_piratas_kids" to "Piratas Kids",
        "cover_mundo_dinossauros" to "Mundo Dinossauros"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Image, contentDescription = null, tint = NavyPrimary)
                Text("Capa da Temporada", fontWeight = FontWeight.Bold, color = NavyDeep)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Escolha uma capa da galeria:", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(presets) { (resKey, name) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clickable { onSelectCover(resKey) },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = getSeriesCoverDrawable(resKey, "")),
                                    contentDescription = name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(4.dp)
                                ) {
                                    Text(name, color = Color.White, fontSize = 10.sp, maxLines = 1)
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()

                Button(
                    onClick = {
                        onGenerateWithAi("Crie uma capa espetacular em formato pôster 3D para a Temporada ${season.seasonNumber} ('${season.title}') de uma série infantil com frutas antropomórficas 3D.")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gerar Nova Capa com IA")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Fechar") }
        }
    )
}

// -------------------------------------------------------------
// 16. ALL EPISODES DIALOG
// -------------------------------------------------------------
@Composable
private fun AllEpisodesDialog(
    season: SeasonEntity,
    episodes: List<EpisodeEntity>,
    onDismiss: () -> Unit,
    onNewEpisode: () -> Unit,
    onDeleteEpisode: (EpisodeEntity) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Episódios — ${season.title}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Fechar")
                }
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(episodes, key = { it.id }) { ep ->
                    EpisodeListItem(
                        episode = ep,
                        onClick = { },
                        onDelete = { onDeleteEpisode(ep) },
                        onDuplicate = { }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onNewEpisode,
                colors = ButtonDefaults.buttonColors(containerColor = NavyDeep)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Adicionar Novo Episódio")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = Color(0xFF64748B))
            }
        }
    )
}

// -------------------------------------------------------------
// 17. DRAWABLE RESOURCE RESOLVERS
// -------------------------------------------------------------
private fun getSeriesCoverDrawable(coverUri: String?, title: String): Int {
    val key = (coverUri ?: title).lowercase()
    return when {
        key.contains("frut") -> R.drawable.cover_frutinhas
        key.contains("reino") || key.contains("magia") || key.contains("guardi") -> R.drawable.cover_reino_encantado
        key.contains("miss") || key.contains("robot") || key.contains("estelar") -> R.drawable.cover_missao_estelar
        key.contains("misterio") || key.contains("rua") || key.contains("cidade") -> R.drawable.cover_misterio_cidade
        key.contains("heroi") || key.contains("escola") -> R.drawable.cover_herois_escola
        key.contains("fazenda") -> R.drawable.cover_fazenda_divertida
        key.contains("pirata") -> R.drawable.cover_piratas_kids
        key.contains("dino") -> R.drawable.cover_mundo_dinossauros
        else -> R.drawable.cover_frutinhas
    }
}

private fun getCharacterDrawable(imageUri: String?, name: String): Int {
    val key = (imageUri ?: name).lowercase()
    return when {
        key.contains("antonio") || key.contains("antónio") || key.contains("maçã") -> R.drawable.char_antonio
        key.contains("bia") || key.contains("banana") -> R.drawable.char_bia
        key.contains("carlos") || key.contains("laranja") -> R.drawable.char_carlos
        key.contains("lima") || key.contains("limão") -> R.drawable.char_antonio
        key.contains("lucas") -> R.drawable.char_lucas
        key.contains("sofia") -> R.drawable.char_sofia
        key.contains("manuel") -> R.drawable.char_sr_manuel
        key.contains("mimi") -> R.drawable.char_mimi
        key.contains("robot") || key.contains("r7") || key.contains("r-7") -> R.drawable.char_r7_robot
        else -> R.drawable.char_antonio
    }
}

private fun getScenarioDrawable(imageUri: String?, name: String): Int {
    val key = (imageUri ?: name).lowercase()
    return when {
        key.contains("casa") || key.contains("familia") -> R.drawable.scenario_family_house_1787049241112
        key.contains("escola") -> R.drawable.scenario_school_1787049257901
        key.contains("cidade") || key.contains("frutal") -> R.drawable.scenario_city_1787049273004
        key.contains("quarto") -> R.drawable.scenario_bedroom_1787049287724
        key.contains("praia") -> R.drawable.scenario_beach_1787049303788
        key.contains("floresta") || key.contains("parque") -> R.drawable.scenario_forest_1787049320048
        else -> R.drawable.scenario_city_1787049273004
    }
}
