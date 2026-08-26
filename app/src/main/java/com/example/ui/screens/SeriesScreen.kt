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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartToy
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
fun SeriesScreen(
    seriesList: List<SeriesEntity>,
    projects: List<ProjectEntity>,
    seasons: List<SeasonEntity>,
    episodes: List<EpisodeEntity>,
    characters: List<CharacterEntity>,
    scenarios: List<ScenarioEntity>,
    scenes: List<SceneEntity>,
    selectedProjectId: Long?,
    activeSeries: SeriesEntity?,
    lastSaveTimestamp: Long,
    showNewSeriesDialog: Boolean,
    editingSeries: SeriesEntity?,
    onSetActiveSeries: (SeriesEntity?) -> Unit,
    onOpenNewSeries: (SeriesEntity?) -> Unit,
    onCloseNewSeries: () -> Unit,
    onSaveSeries: (SeriesEntity, Boolean) -> Unit,
    onDeleteSeries: (SeriesEntity) -> Unit,
    onDuplicateSeries: (SeriesEntity) -> Unit,
    onArchiveSeries: (SeriesEntity) -> Unit,
    onToggleFavoriteSeries: (SeriesEntity) -> Unit,
    onSaveSeason: (SeasonEntity) -> Unit,
    onDeleteSeason: (SeasonEntity) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onNavigateToDestination: (StudioDestination) -> Unit
) {
    val context = LocalContext.current

    // Local UI State
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("Todas") }
    var selectedSortOrder by remember { mutableStateOf("Mais recentes") }
    var isGridView by remember { mutableStateOf(true) }

    // Dialogs
    var seriesToDelete by remember { mutableStateOf<SeriesEntity?>(null) }
    var showSeasonModal by remember { mutableStateOf(false) }
    var showEpisodeModal by remember { mutableStateOf(false) }
    var showScriptModal by remember { mutableStateOf(false) }
    var showCoverModal by remember { mutableStateOf(false) }

    // Auto-select the first series if none is active
    val currentActive = activeSeries ?: seriesList.firstOrNull { s ->
        selectedProjectId == null || s.projectId == selectedProjectId
    } ?: seriesList.firstOrNull()

    LaunchedEffect(currentActive) {
        if (activeSeries == null && currentActive != null) {
            onSetActiveSeries(currentActive)
        }
    }

    // Filter and Sort Series
    val filteredSeries = seriesList.filter { series ->
        val matchProject = selectedProjectId == null || series.projectId == selectedProjectId
        val matchSearch = searchQuery.isBlank() ||
                series.title.contains(searchQuery, ignoreCase = true) ||
                series.synopsis.contains(searchQuery, ignoreCase = true) ||
                series.genre.contains(searchQuery, ignoreCase = true)
        val matchStatus = when (selectedStatusFilter) {
            "Todas" -> true
            "Em produção" -> series.status == "Em produção"
            "Em pausa" -> series.status == "Em pausa"
            "Concluídas" -> series.status == "Concluída"
            "Arquivadas" -> series.isArchived || series.status == "Arquivada"
            else -> true
        }
        matchProject && matchSearch && matchStatus
    }.sortedWith { a, b ->
        when (selectedSortOrder) {
            "Mais recentes" -> b.updatedAt.compareTo(a.updatedAt)
            "Mais antigas" -> a.createdAt.compareTo(b.createdAt)
            "Nome A-Z" -> a.title.compareTo(b.title, ignoreCase = true)
            "Nome Z-A" -> b.title.compareTo(a.title, ignoreCase = true)
            else -> 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF1F5F9))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Header (Professional RANGA AI STUDIO style)
            SeriesTopHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onNewSeriesClick = { onOpenNewSeries(null) },
                lastSaveTimestamp = lastSaveTimestamp
            )

            // 2. Filter & Sort Bar
            SeriesFilterBar(
                selectedStatus = selectedStatusFilter,
                onSelectStatus = { selectedStatusFilter = it },
                selectedSort = selectedSortOrder,
                onSelectSort = { selectedSortOrder = it },
                isGridView = isGridView,
                onToggleView = { isGridView = it },
                totalCount = filteredSeries.size
            )

            // 3. Main Content (Scrollable)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top Series Cards Carousel / Catalog
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Catálogo de Séries",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDeep
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = NavyPrimary.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        "${filteredSeries.size}",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NavyPrimary
                                    )
                                }
                            }
                            Text(
                                "Selecione uma série para gerenciar",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }

                        if (filteredSeries.isEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Tv,
                                        contentDescription = null,
                                        modifier = Modifier.size(48.dp),
                                        tint = Color(0xFF94A3B8)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        "Nenhuma série encontrada",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = NavyDeep
                                    )
                                    Text(
                                        "Crie uma nova série para organizar temporadas, episódios e cenas.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF64748B),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { onOpenNewSeries(null) },
                                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("+ Nova Série", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        } else {
                            // Horizontal Carousel of Series Cards
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                contentPadding = PaddingValues(vertical = 4.dp)
                            ) {
                                items(filteredSeries) { seriesItem ->
                                    val isSelected = currentActive?.id == seriesItem.id
                                    SeriesMiniCard(
                                        series = seriesItem,
                                        isSelected = isSelected,
                                        onClick = { onSetActiveSeries(seriesItem) },
                                        onEdit = { onOpenNewSeries(seriesItem) },
                                        onDuplicate = { onDuplicateSeries(seriesItem) },
                                        onArchive = { onArchiveSeries(seriesItem) },
                                        onDelete = { seriesToDelete = seriesItem }
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Series Management Profile (Full Dashboard if a series is selected)
                if (currentActive != null) {
                    item {
                        SeriesProfileDashboard(
                            series = currentActive,
                            projects = projects,
                            seasons = seasons.filter { it.seriesId == currentActive.id },
                            episodes = episodes.filter { it.projectId == currentActive.projectId },
                            characters = characters.filter { it.projectId == null || it.projectId == currentActive.projectId },
                            scenarios = scenarios.filter { it.projectId == null || it.projectId == currentActive.projectId },
                            onEditSeries = { onOpenNewSeries(currentActive) },
                            onToggleFavorite = { onToggleFavoriteSeries(currentActive) },
                            onOpenNewSeason = { showSeasonModal = true },
                            onOpenNewEpisode = { showEpisodeModal = true },
                            onOpenScriptModal = { showScriptModal = true },
                            onOpenCoverModal = { showCoverModal = true },
                            onOpenAiPrompt = onOpenAiWithPrompt,
                            onNavigateToDestination = onNavigateToDestination
                        )
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------
    // MODALS & DIALOGS
    // -------------------------------------------------------------

    // 1. Create / Edit Series Modal
    if (showNewSeriesDialog) {
        SeriesFormModal(
            initialSeries = editingSeries,
            projects = projects,
            onDismiss = onCloseNewSeries,
            onSave = { updatedSeries ->
                onSaveSeries(updatedSeries, true)
                Toast.makeText(context, "Série salva com sucesso!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 2. Delete Confirmation Dialog
    if (seriesToDelete != null) {
        AlertDialog(
            onDismissRequest = { seriesToDelete = null },
            icon = { Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError) },
            title = { Text("Excluir Série?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "Tem certeza que deseja excluir a série “${seriesToDelete?.title}”? Esta ação não pode ser desfeita.",
                    color = Color(0xFF475569)
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        seriesToDelete?.let { onDeleteSeries(it) }
                        seriesToDelete = null
                        Toast.makeText(context, "Série excluída com sucesso!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { seriesToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 3. New Season Modal
    if (showSeasonModal && currentActive != null) {
        SeasonFormModal(
            seriesId = currentActive.id,
            existingSeasonsCount = seasons.filter { it.seriesId == currentActive.id }.size,
            onDismiss = { showSeasonModal = false },
            onSave = { season ->
                onSaveSeason(season)
                showSeasonModal = false
                Toast.makeText(context, "Nova temporada criada!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 4. Quick Episode Modal
    if (showEpisodeModal && currentActive != null) {
        val seriesSeasons = seasons.filter { it.seriesId == currentActive.id }
        EpisodeQuickModal(
            projectId = currentActive.projectId,
            seasons = seriesSeasons,
            onDismiss = { showEpisodeModal = false },
            onSave = {
                showEpisodeModal = false
                Toast.makeText(context, "Episódio adicionado à produção!", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 5. Script Modal
    if (showScriptModal && currentActive != null) {
        SeriesScriptModal(
            series = currentActive,
            onDismiss = { showScriptModal = false },
            onOpenAi = { prompt ->
                showScriptModal = false
                onOpenAiWithPrompt(prompt)
            }
        )
    }

    // 6. Cover Selector / Generator Modal
    if (showCoverModal && currentActive != null) {
        SeriesCoverModal(
            series = currentActive,
            onDismiss = { showCoverModal = false },
            onSelectCover = { coverResName ->
                onSaveSeries(currentActive.copy(coverUri = coverResName), true)
                showCoverModal = false
                Toast.makeText(context, "Capa da série atualizada!", Toast.LENGTH_SHORT).show()
            },
            onGenerateWithAi = { prompt ->
                showCoverModal = false
                onOpenAiWithPrompt(prompt)
            }
        )
    }
}

// -------------------------------------------------------------
// 1. TOP HEADER
// -------------------------------------------------------------
@Composable
private fun SeriesTopHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNewSeriesClick: () -> Unit,
    lastSaveTimestamp: Long
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Title & Subtitle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(NavyPrimary, Color(0xFF1E40AF))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tv,
                            contentDescription = "Séries",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Séries",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = NavyDeep
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFE0F2FE))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    "ESTÚDIO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0284C7)
                                )
                            }
                        }
                        Text(
                            "Organize suas histórias em séries, temporadas, episódios e cenas.",
                            fontSize = 13.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Right: Search & Action Buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = { Text("Pesquisar séries...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpar",
                                        tint = Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedBorderColor = NavyPrimary,
                            unfocusedContainerColor = Color(0xFFF8FAFC),
                            focusedContainerColor = Color.White
                        ),
                        modifier = Modifier
                            .widthIn(max = 240.dp)
                            .height(44.dp)
                    )

                    // + Nova Série Button
                    Button(
                        onClick = onNewSeriesClick,
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("new_series_button")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("+ Nova Série", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    // Notification Icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(RoseError)
                                .align(Alignment.TopEnd)
                                .offset(x = (-6).dp, y = 6.dp)
                        )
                    }

                    // User Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("Augusto", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                            Text("Profissional", fontSize = 10.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. FILTER & SORT BAR
// -------------------------------------------------------------
@Composable
private fun SeriesFilterBar(
    selectedStatus: String,
    onSelectStatus: (String) -> Unit,
    selectedSort: String,
    onSelectSort: (String) -> Unit,
    isGridView: Boolean,
    onToggleView: (Boolean) -> Unit,
    totalCount: Int
) {
    var sortMenuOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Status Filter Chips
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                Text(
                    "Filtrar:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF64748B)
                )

                listOf("Todas", "Em produção", "Em pausa", "Concluídas", "Arquivadas").forEach { status ->
                    val isSelected = selectedStatus == status
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectStatus(status) },
                        label = {
                            Text(
                                status,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color(0xFF475569)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = if (isSelected) NavyPrimary else Color(0xFFCBD5E1),
                            selectedBorderColor = NavyPrimary,
                            enabled = true,
                            selected = isSelected
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            }

            // Right: Sort, View Switch & Status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Auto-save Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFFDCFCE7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF16A34A))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Salvo", fontSize = 11.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Medium)
                }

                // Sort Dropdown
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .clickable { sortMenuOpen = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Ordenar",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(selectedSort, fontSize = 12.sp, color = NavyDeep, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = sortMenuOpen,
                        onDismissRequest = { sortMenuOpen = false }
                    ) {
                        listOf("Mais recentes", "Mais antigas", "Nome A-Z", "Nome Z-A").forEach { opt ->
                            DropdownMenuItem(
                                text = { Text(opt, fontSize = 13.sp) },
                                onClick = {
                                    onSelectSort(opt)
                                    sortMenuOpen = false
                                }
                            )
                        }
                    }
                }

                // View Toggle (Grid vs List)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    IconButton(
                        onClick = { onToggleView(true) },
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isGridView) NavyPrimary else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Grade",
                            tint = if (isGridView) Color.White else Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(
                        onClick = { onToggleView(false) },
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isGridView) NavyPrimary else Color.Transparent)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ViewList,
                            contentDescription = "Lista",
                            tint = if (!isGridView) Color.White else Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. SERIES MINI CARD (CAROUSEL / CATALOG)
// -------------------------------------------------------------
@Composable
private fun SeriesMiniCard(
    series: SeriesEntity,
    isSelected: Boolean,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(260.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .testTag("series_card_${series.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) NavyPrimary else Color(0xFFE2E8F0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
    ) {
        Column {
            // Card Cover Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(0xFF1E293B))
            ) {
                Image(
                    painter = painterResource(id = getSeriesCoverDrawable(series.coverUri, series.title)),
                    contentDescription = series.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top Badges
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Status Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            series.status,
                            color = when (series.status) {
                                "Concluída" -> Color(0xFF4ADE80)
                                "Em produção" -> Color(0xFF60A5FA)
                                "Em pausa" -> Color(0xFFFBBF24)
                                else -> Color.White
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Card Menu Button
                    Box {
                        IconButton(
                            onClick = { menuOpen = true },
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = menuOpen,
                            onDismissRequest = { menuOpen = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar Série", fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuOpen = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Duplicar", fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuOpen = false
                                    onDuplicate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (series.isArchived) "Desarquivar" else "Arquivar", fontSize = 13.sp) },
                                leadingIcon = { Icon(Icons.Default.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuOpen = false
                                    onArchive()
                                }
                            )
                            HorizontalDivider()
                            DropdownMenuItem(
                                text = { Text("Excluir", fontSize = 13.sp, color = RoseError) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    menuOpen = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }

                // Selected Indicator Pill
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(NavyPrimary)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(2.dp))
                            Text("Selecionada", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Info Body
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    series.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Genres
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    series.genre.split(",").take(2).forEach { g ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFFEFF6FF))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(g.trim(), fontSize = 10.sp, color = NavyPrimary, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Stats row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "${series.seasonsCount} Temp • ${series.episodesCount} Eps",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B),
                        fontWeight = FontWeight.Medium
                    )

                    Text(
                        "${series.scenesCount} Cenas",
                        fontSize = 11.sp,
                        color = Color(0xFF0F766E),
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    "Atualizado há 2h",
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 4. SERIES PROFILE DASHBOARD (MATCHING REFERENCE EXACTLY)
// -------------------------------------------------------------
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeriesProfileDashboard(
    series: SeriesEntity,
    projects: List<ProjectEntity>,
    seasons: List<SeasonEntity>,
    episodes: List<EpisodeEntity>,
    characters: List<CharacterEntity>,
    scenarios: List<ScenarioEntity>,
    onEditSeries: () -> Unit,
    onToggleFavorite: () -> Unit,
    onOpenNewSeason: () -> Unit,
    onOpenNewEpisode: () -> Unit,
    onOpenScriptModal: () -> Unit,
    onOpenCoverModal: () -> Unit,
    onOpenAiPrompt: (String) -> Unit,
    onNavigateToDestination: (StudioDestination) -> Unit
) {
    val projName = projects.find { it.id == series.projectId }?.name ?: "Projeto Geral"

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val isWideScreen = maxWidth > 840.dp

        if (isWideScreen) {
            // 3-Column Layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // LEFT COLUMN (Poster & Seasons)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SeriesPosterCard(
                        series = series,
                        onEditSeries = onEditSeries,
                        onContinueProduction = { onNavigateToDestination(StudioDestination.SCENES) }
                    )

                    SeriesSeasonsCard(
                        seasons = seasons,
                        onNewSeasonClick = onOpenNewSeason
                    )
                }

                // CENTER COLUMN (Series Details, Stats, Episodes, Characters, Scenarios)
                Column(
                    modifier = Modifier
                        .weight(2.2f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SeriesMainInfoCard(
                        series = series,
                        projectName = projName,
                        onToggleFavorite = onToggleFavorite
                    )

                    SeriesStatsOverviewCard(series = series)

                    SeriesRecentEpisodesCard(
                        episodes = episodes,
                        onNewEpisodeClick = onOpenNewEpisode,
                        onViewAllEpisodes = { onNavigateToDestination(StudioDestination.EPISODES) }
                    )

                    SeriesCharactersCard(
                        characters = characters,
                        onViewAll = { onNavigateToDestination(StudioDestination.CHARACTERS) }
                    )

                    SeriesScenariosCard(
                        scenarios = scenarios,
                        onViewAll = { onNavigateToDestination(StudioDestination.SCENARIOS) }
                    )
                }

                // RIGHT COLUMN (Ranga AI Assistant, Scripts, Cover Art)
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SeriesRangaAiCard(
                        seriesTitle = series.title,
                        onPromptClick = onOpenAiPrompt
                    )

                    SeriesScriptsCard(
                        onNewScript = onOpenScriptModal,
                        onImportScript = onOpenScriptModal,
                        onGenerateScript = {
                            onOpenAiPrompt("Crie um roteiro cinematográfico completo para a série '${series.title}' com introdução de cena, ações dinâmicas e diálogos envolventes.")
                        }
                    )

                    SeriesCoverArtCard(
                        series = series,
                        onOpenCoverModal = onOpenCoverModal,
                        onGenerateWithAi = {
                            onOpenAiPrompt("Gere uma capa 3D cartoon cinematográfica e super colorida para a série '${series.title}'.")
                        }
                    )
                }
            }
        } else {
            // Stacked Layout for mobile / compact screens
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SeriesMainInfoCard(
                    series = series,
                    projectName = projName,
                    onToggleFavorite = onToggleFavorite
                )

                SeriesPosterCard(
                    series = series,
                    onEditSeries = onEditSeries,
                    onContinueProduction = { onNavigateToDestination(StudioDestination.SCENES) }
                )

                SeriesStatsOverviewCard(series = series)

                SeriesSeasonsCard(
                    seasons = seasons,
                    onNewSeasonClick = onOpenNewSeason
                )

                SeriesRecentEpisodesCard(
                    episodes = episodes,
                    onNewEpisodeClick = onOpenNewEpisode,
                    onViewAllEpisodes = { onNavigateToDestination(StudioDestination.EPISODES) }
                )

                SeriesCharactersCard(
                    characters = characters,
                    onViewAll = { onNavigateToDestination(StudioDestination.CHARACTERS) }
                )

                SeriesScenariosCard(
                    scenarios = scenarios,
                    onViewAll = { onNavigateToDestination(StudioDestination.SCENARIOS) }
                )

                SeriesRangaAiCard(
                    seriesTitle = series.title,
                    onPromptClick = onOpenAiPrompt
                )

                SeriesScriptsCard(
                    onNewScript = onOpenScriptModal,
                    onImportScript = onOpenScriptModal,
                    onGenerateScript = {
                        onOpenAiPrompt("Crie um roteiro cinematográfico para a série '${series.title}'.")
                    }
                )

                SeriesCoverArtCard(
                    series = series,
                    onOpenCoverModal = onOpenCoverModal,
                    onGenerateWithAi = {
                        onOpenAiPrompt("Gere uma capa 3D cartoon para a série '${series.title}'.")
                    }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 5. LEFT COLUMN: POSTER & ACTION BUTTONS
// -------------------------------------------------------------
@Composable
private fun SeriesPosterCard(
    series: SeriesEntity,
    onEditSeries: () -> Unit,
    onContinueProduction: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Poster Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0F172A))
            ) {
                Image(
                    painter = painterResource(id = getSeriesCoverDrawable(series.coverUri, series.title)),
                    contentDescription = "Capa da Série",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 3D Quality Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Tv, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(series.type, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onEditSeries,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                    border = BorderStroke(1.dp, NavyPrimary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Editar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = onContinueProduction,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    modifier = Modifier.weight(1.3f)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Produção", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. SEASONS SECTION CARD
// -------------------------------------------------------------
@Composable
private fun SeriesSeasonsCard(
    seasons: List<SeasonEntity>,
    onNewSeasonClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Temporadas",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            "${seasons.size}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                IconButton(
                    onClick = onNewSeasonClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Nova temporada", tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (seasons.isEmpty()) {
                Text(
                    "Nenhuma temporada adicionada.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    seasons.forEach { season ->
                        SeasonListItem(season = season)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onNewSeasonClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                border = BorderStroke(1.dp, Color(0xFFCBD5E1))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("+ Nova temporada", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SeasonListItem(season: SeasonEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Season Cover Thumbnail
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF1E293B))
                    ) {
                        Image(
                            painter = painterResource(id = getSeriesCoverDrawable(season.coverUri, season.title)),
                            contentDescription = season.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column {
                        Text(
                            season.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDeep
                        )
                        Text(
                            "${season.episodesCount} episódios",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.MoreHoriz,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LinearProgressIndicator(
                    progress = { season.progressPercent / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = if (season.progressPercent >= 60) Color(0xFF10B981) else Color(0xFF3B82F6),
                    trackColor = Color(0xFFE2E8F0)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "${season.progressPercent}%",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "${season.completedEpisodesCount} concluídos",
                fontSize = 10.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

// -------------------------------------------------------------
// 7. CENTER COLUMN: MAIN INFO HEADER
// -------------------------------------------------------------
@Composable
private fun SeriesMainInfoCard(
    series: SeriesEntity,
    projectName: String,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Top Row: Title + Favorite Star
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            series.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = NavyDeep
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = onToggleFavorite,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = if (series.isFavorite) Icons.Default.Star else Icons.Default.StarOutline,
                                contentDescription = "Favoritar",
                                tint = if (series.isFavorite) AmberGold else Color(0xFF94A3B8),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Text(
                        "Projeto: $projectName",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }

                // Status Badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = when (series.status) {
                        "Concluída" -> Color(0xFFDCFCE7)
                        "Em produção" -> Color(0xFFDBEAFE)
                        "Em pausa" -> Color(0xFFFEF3C7)
                        else -> Color(0xFFF1F5F9)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when (series.status) {
                                        "Concluída" -> Color(0xFF16A34A)
                                        "Em produção" -> Color(0xFF2563EB)
                                        "Em pausa" -> Color(0xFFD97706)
                                        else -> Color(0xFF64748B)
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            series.status,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (series.status) {
                                "Concluída" -> Color(0xFF15803D)
                                "Em produção" -> Color(0xFF1D4ED8)
                                "Em pausa" -> Color(0xFFB45309)
                                else -> Color(0xFF334155)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                series.synopsis.ifBlank { "Sem sinopse cadastrada." },
                fontSize = 13.sp,
                color = Color(0xFF334155),
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Genres & Meta
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    series.genre.split(",").forEach { genre ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFF1F5F9),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Text(
                                genre.trim(),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = NavyDeep
                            )
                        }
                    }
                }

                Text(
                    "Criada em 12/05/2024",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 8. CENTER COLUMN: STATS OVERVIEW (6 METRICS)
// -------------------------------------------------------------
@Composable
private fun SeriesStatsOverviewCard(series: SeriesEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Estatísticas Gerais",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = NavyDeep
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 2x3 Grid of Stat Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPill(
                    icon = Icons.Default.Tv,
                    label = "Temporadas",
                    value = "${series.seasonsCount}",
                    color = Color(0xFF3B82F6),
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = Icons.Default.VideoLibrary,
                    label = "Episódios",
                    value = "${series.episodesCount}",
                    color = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = Icons.Default.Movie,
                    label = "Cenas",
                    value = "${series.scenesCount}",
                    color = Color(0xFF06B6D4),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatPill(
                    icon = Icons.Default.People,
                    label = "Personagens",
                    value = "${series.charactersCount}",
                    color = Color(0xFFF59E0B),
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = Icons.Default.Landscape,
                    label = "Cenários",
                    value = "${series.scenariosCount}",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                StatPill(
                    icon = Icons.Default.Timer,
                    label = "Duração Total",
                    value = series.totalDuration,
                    color = Color(0xFFEC4899),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StatPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Column {
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                Text(label, fontSize = 10.sp, color = Color(0xFF64748B), maxLines = 1)
            }
        }
    }
}

// -------------------------------------------------------------
// 9. RECENT EPISODES CARD
// -------------------------------------------------------------
@Composable
private fun SeriesRecentEpisodesCard(
    episodes: List<EpisodeEntity>,
    onNewEpisodeClick: () -> Unit,
    onViewAllEpisodes: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Episódios Recentes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            "${episodes.size}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                Button(
                    onClick = onNewEpisodeClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Novo episódio", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (episodes.isEmpty()) {
                Text(
                    "Nenhum episódio cadastrado.",
                    fontSize = 12.sp,
                    color = Color(0xFF94A3B8),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    episodes.take(5).forEach { ep ->
                        EpisodeItemRow(episode = ep)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextButton(
                onClick = onViewAllEpisodes,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Ver todos os episódios", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = NavyPrimary)
            }
        }
    }
}

@Composable
private fun EpisodeItemRow(episode: EpisodeEntity) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Episode Cover Thumbnail
                Box(
                    modifier = Modifier
                        .size(42.dp, 30.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF1E293B))
                ) {
                    Image(
                        painter = painterResource(id = getSeriesCoverDrawable(episode.coverUri, episode.title)),
                        contentDescription = episode.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column {
                    Text(
                        "${episode.episodeNumber}. ${episode.title}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDeep,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "T1 • ${episode.duration}",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Status Chip
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = when (episode.status) {
                    "Concluído" -> Color(0xFFDCFCE7)
                    "Em produção" -> Color(0xFFDBEAFE)
                    else -> Color(0xFFFEF3C7)
                }
            ) {
                Text(
                    episode.status,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = when (episode.status) {
                        "Concluído" -> Color(0xFF15803D)
                        "Em produção" -> Color(0xFF1D4ED8)
                        else -> Color(0xFFB45309)
                    }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 10. CHARACTERS PREVIEW CARD
// -------------------------------------------------------------
@Composable
private fun SeriesCharactersCard(
    characters: List<CharacterEntity>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Personagens da Série",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )

                TextButton(onClick = onViewAll) {
                    Text("Ver todos", fontSize = 12.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Characters Avatar Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(characters.take(8)) { char ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(64.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0))
                                .border(2.dp, Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = getCharacterDrawable(char.imageUri, char.name)),
                                contentDescription = char.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            char.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyDeep,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 11. SCENARIOS PREVIEW CARD
// -------------------------------------------------------------
@Composable
private fun SeriesScenariosCard(
    scenarios: List<ScenarioEntity>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Cenários Utilizados",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDeep
                )

                TextButton(onClick = onViewAll) {
                    Text("Ver todos", fontSize = 12.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = NavyPrimary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scenarios Horizontal List
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(scenarios.take(6)) { scen ->
                    Surface(
                        modifier = Modifier.width(140.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(75.dp)
                                    .background(Color(0xFF1E293B))
                            ) {
                                Image(
                                    painter = painterResource(id = getScenarioDrawable(scen.imageUri, scen.name)),
                                    contentDescription = scen.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    scen.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NavyDeep,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    "${scen.scenesCount * 4} cenas",
                                    fontSize = 10.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 12. RIGHT COLUMN: RANGA AI ASSISTANT CARD
// -------------------------------------------------------------
@Composable
private fun SeriesRangaAiCard(
    seriesTitle: String,
    onPromptClick: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF8B5CF6), Color(0xFF6366F1))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.SmartToy, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                }

                Column {
                    Text("Assistente RANGA", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
                    Text("IA criativa para séries", fontSize = 10.sp, color = Color(0xFF64748B))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                "Seu assistente para criar e desenvolver séries incríveis.",
                fontSize = 11.sp,
                color = Color(0xFF475569)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Prompt Buttons
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                AiSuggestionButton(
                    icon = Icons.Default.Lightbulb,
                    text = "Criar ideia para nova série",
                    onClick = { onPromptClick("Crie uma ideia inovadora de série com premissa original, público-alvo e 3 arcos principais.") }
                )
                AiSuggestionButton(
                    icon = Icons.Default.AutoAwesome,
                    text = "Gerar ideias para episódios",
                    onClick = { onPromptClick("Gere 5 ideias de episódios dinâmicos e envolventes para a série '$seriesTitle' com conflitos e resoluções.") }
                )
                AiSuggestionButton(
                    icon = Icons.Default.Description,
                    text = "Criar sinopse da série",
                    onClick = { onPromptClick("Escreva uma sinopse profissional e envolvente para a série '$seriesTitle' destacando os temas centrais.") }
                )
                AiSuggestionButton(
                    icon = Icons.Default.People,
                    text = "Desenvolver personagens",
                    onClick = { onPromptClick("Crie 3 personagens complementares para a série '$seriesTitle' com personalidades, motivações e arcos dramáticos.") }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onPromptClick("Como posso enriquecer a narrativa e o ritmo da série '$seriesTitle'?") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Abrir Assistente IA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AiSuggestionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 11.sp, color = NavyDeep, fontWeight = FontWeight.Medium)
        }
    }
}

// -------------------------------------------------------------
// 13. RIGHT COLUMN: SCRIPTS CARD
// -------------------------------------------------------------
@Composable
private fun SeriesScriptsCard(
    onNewScript: () -> Unit,
    onImportScript: () -> Unit,
    onGenerateScript: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Roteiro da Série", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ScriptActionButton(
                    icon = Icons.Default.AutoAwesome,
                    text = "Criar roteiro com IA",
                    onClick = onGenerateScript
                )
                ScriptActionButton(
                    icon = Icons.Default.Description,
                    text = "Roteiro em Branco (+)",
                    onClick = onNewScript
                )
                ScriptActionButton(
                    icon = Icons.Default.UploadFile,
                    text = "Importar roteiro",
                    onClick = onImportScript
                )
            }
        }
    }
}

@Composable
private fun ScriptActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text, fontSize = 11.sp, color = NavyDeep, fontWeight = FontWeight.Medium)
        }
    }
}

// -------------------------------------------------------------
// 14. RIGHT COLUMN: COVER ART CARD
// -------------------------------------------------------------
@Composable
private fun SeriesCoverArtCard(
    series: SeriesEntity,
    onOpenCoverModal: () -> Unit,
    onGenerateWithAi: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Capa da Série", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDeep)
            Spacer(modifier = Modifier.height(10.dp))

            // Cover thumbnail preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1E293B))
            ) {
                Image(
                    painter = painterResource(id = getSeriesCoverDrawable(series.coverUri, series.title)),
                    contentDescription = "Miniatura da Capa",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                ScriptActionButton(
                    icon = Icons.Default.Edit,
                    text = "Alterar capa",
                    onClick = onOpenCoverModal
                )
                ScriptActionButton(
                    icon = Icons.Default.AutoAwesome,
                    text = "Criar com IA",
                    onClick = onGenerateWithAi
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 15. MODAL: CREATE / EDIT SERIES
// -------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SeriesFormModal(
    initialSeries: SeriesEntity?,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onSave: (SeriesEntity) -> Unit
) {
    var title by remember { mutableStateOf(initialSeries?.title ?: "") }
    var synopsis by remember { mutableStateOf(initialSeries?.synopsis ?: "") }
    var selectedProjectId by remember { mutableStateOf(initialSeries?.projectId ?: projects.firstOrNull()?.id ?: 1L) }
    var selectedType by remember { mutableStateOf(initialSeries?.type ?: "Desenho Animado") }
    var selectedStatus by remember { mutableStateOf(initialSeries?.status ?: "Em produção") }
    var targetAudience by remember { mutableStateOf(initialSeries?.targetAudience ?: "Livre") }
    var coverUri by remember { mutableStateOf(initialSeries?.coverUri ?: "cover_frutinhas") }

    val allGenres = listOf("Aventura", "Comédia", "Drama", "Infantil", "Fantasia", "Mistério", "Ação", "Romance", "Ficção científica", "Educativo", "Terror", "Outro")
    val selectedGenres = remember {
        mutableStateListOf<String>().apply {
            if (initialSeries != null && initialSeries.genre.isNotBlank()) {
                addAll(initialSeries.genre.split(",").map { it.trim() })
            } else {
                addAll(listOf("Infantil", "Aventura"))
            }
        }
    }

    var projectDropdownExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Tv, contentDescription = null, tint = NavyPrimary)
                Text(if (initialSeries == null) "Criar Nova Série" else "Editar Série", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da Série *") },
                    placeholder = { Text("Ex: Aventuras das Frutas") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    label = { Text("Sinopse / Descrição") },
                    placeholder = { Text("Conte um pouco sobre o universo e premissa...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                // Project Selector
                ExposedDropdownMenuBox(
                    expanded = projectDropdownExpanded,
                    onExpandedChange = { projectDropdownExpanded = it }
                ) {
                    val currentProjName = projects.find { it.id == selectedProjectId }?.name ?: "Projeto Geral"
                    OutlinedTextField(
                        value = currentProjName,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Projeto Relacionado") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = projectDropdownExpanded,
                        onDismissRequest = { projectDropdownExpanded = false }
                    ) {
                        projects.forEach { proj ->
                            DropdownMenuItem(
                                text = { Text(proj.name) },
                                onClick = {
                                    selectedProjectId = proj.id
                                    projectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Type Chips
                Text("Tipo de Produção:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Desenho Animado", "Série", "Novela", "Filme").forEach { tp ->
                        FilterChip(
                            selected = selectedType == tp,
                            onClick = { selectedType = tp },
                            label = { Text(tp, fontSize = 11.sp) }
                        )
                    }
                }

                // Genres Multi-select
                Text("Gêneros (Selecione):", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    allGenres.forEach { g ->
                        val isSel = selectedGenres.contains(g)
                        FilterChip(
                            selected = isSel,
                            onClick = {
                                if (isSel) selectedGenres.remove(g) else selectedGenres.add(g)
                            },
                            label = { Text(g, fontSize = 11.sp) }
                        )
                    }
                }

                // Status Chips
                Text("Status:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Em planejamento", "Em produção", "Em exibição", "Em pausa", "Concluída", "Arquivada").forEach { st ->
                        FilterChip(
                            selected = selectedStatus == st,
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 11.sp) }
                        )
                    }
                }

                // Cover Preset Selection
                Text("Capa da Série:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDeep)
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("cover_frutinhas", "cover_reino_encantado", "cover_missao_estelar", "cover_misterio_cidade", "cover_herois_escola", "cover_fazenda_divertida").forEach { cov ->
                        val isCovSel = coverUri == cov
                        Box(
                            modifier = Modifier
                                .size(60.dp, 45.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .border(if (isCovSel) 2.dp else 1.dp, if (isCovSel) NavyPrimary else Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                                .clickable { coverUri = cov }
                        ) {
                            Image(
                                painter = painterResource(id = getSeriesCoverDrawable(cov, "")),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val genresString = if (selectedGenres.isEmpty()) "Aventura" else selectedGenres.joinToString(", ")
                        val toSave = (initialSeries ?: SeriesEntity(title = title, projectId = selectedProjectId)).copy(
                            title = title,
                            synopsis = synopsis,
                            projectId = selectedProjectId,
                            type = selectedType,
                            genre = genresString,
                            status = selectedStatus,
                            targetAudience = targetAudience,
                            coverUri = coverUri
                        )
                        onSave(toSave)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Salvar Série", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// -------------------------------------------------------------
// 16. MODAL: CREATE SEASON
// -------------------------------------------------------------
@Composable
private fun SeasonFormModal(
    seriesId: Long,
    existingSeasonsCount: Int,
    onDismiss: () -> Unit,
    onSave: (SeasonEntity) -> Unit
) {
    var seasonNumber by remember { mutableIntStateOf(existingSeasonsCount + 1) }
    var title by remember { mutableStateOf("Temporada $seasonNumber") }
    var synopsis by remember { mutableStateOf("") }
    var episodesCount by remember { mutableIntStateOf(12) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nova Temporada", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título da Temporada") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = seasonNumber.toString(),
                        onValueChange = { seasonNumber = it.toIntOrNull() ?: 1 },
                        label = { Text("Número") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = episodesCount.toString(),
                        onValueChange = { episodesCount = it.toIntOrNull() ?: 12 },
                        label = { Text("Qtd. Episódios") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = synopsis,
                    onValueChange = { synopsis = it },
                    label = { Text("Sinopse da Temporada") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        SeasonEntity(
                            seriesId = seriesId,
                            seasonNumber = seasonNumber,
                            title = title,
                            synopsis = synopsis,
                            episodesCount = episodesCount,
                            completedEpisodesCount = 0,
                            progressPercent = 0
                        )
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Adicionar Temporada", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// -------------------------------------------------------------
// 17. MODAL: QUICK EPISODE CREATOR
// -------------------------------------------------------------
@Composable
private fun EpisodeQuickModal(
    projectId: Long,
    seasons: List<SeasonEntity>,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("22 min") }
    var selectedStatus by remember { mutableStateOf("Em produção") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Episódio", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Episódio") },
                    placeholder = { Text("Ex: O Mistério Revelado") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = duration,
                    onValueChange = { duration = it },
                    label = { Text("Duração Estimada") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Status Inicial:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Rascunho", "Em produção", "Concluído").forEach { st ->
                        FilterChip(
                            selected = selectedStatus == st,
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onSave,
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Criar Episódio", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// -------------------------------------------------------------
// 18. MODAL: SERIES SCRIPT EDITOR & AI
// -------------------------------------------------------------
@Composable
private fun SeriesScriptModal(
    series: SeriesEntity,
    onDismiss: () -> Unit,
    onOpenAi: (String) -> Unit
) {
    var scriptTitle by remember { mutableStateOf("Roteiro: ${series.title}") }
    var scriptContent by remember {
        mutableStateOf(
            """
            CENA 1 - INT. POMAR MÁGICO - DIA
            
            O sol brilha suavemente entre as folhas da Grande Árvore. António corre segurando um mapa antigo.
            
            ANTÓNIO
            (animado)
            Bia! Carlos! Vocês não vão acreditar no que encontrei!
            
            Bia se aproxima curiosa, com seu laço brilhando ao sol.
            
            BIA
            É o mapa secreto do Vale Dourado?
            """.trimIndent()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Description, contentDescription = null, tint = NavyPrimary)
                Text("Editor de Roteiro", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = scriptTitle,
                    onValueChange = { scriptTitle = it },
                    label = { Text("Título do Roteiro") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = scriptContent,
                    onValueChange = { scriptContent = it },
                    label = { Text("Conteúdo do Roteiro") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 8
                )

                Button(
                    onClick = {
                        onOpenAi("Expanda e enriqueça o seguinte roteiro da série '${series.title}' com diálogos bem humorados e ações detalhadas:\n\n$scriptContent")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Expandir Roteiro com IA")
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)) {
                Text("Salvar Roteiro")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Fechar") }
        }
    )
}

// -------------------------------------------------------------
// 19. MODAL: SERIES COVER SELECTOR & GENERATOR
// -------------------------------------------------------------
@Composable
private fun SeriesCoverModal(
    series: SeriesEntity,
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
                Text("Capa da Série", fontWeight = FontWeight.Bold)
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

                // Grid of Presets
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
                        onGenerateWithAi("Gere uma imagem de capa espetacular em formato pôster 3D estilizado para a série '${series.title}'.")
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
// 20. DRAWABLE RESOURCE RESOLVERS
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
