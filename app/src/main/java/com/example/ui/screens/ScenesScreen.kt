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
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.RoseError
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScenesScreen(
    scenes: List<SceneEntity>,
    episodes: List<EpisodeEntity>,
    characters: List<CharacterEntity>,
    scenarios: List<ScenarioEntity>,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    activeScene: SceneEntity?,
    isStudioMode: Boolean,
    lastSaveTimestamp: Long,
    onSetActiveScene: (SceneEntity?) -> Unit,
    onSetStudioMode: (Boolean) -> Unit,
    onSaveScene: (SceneEntity, Boolean) -> Unit,
    onDeleteScene: (SceneEntity) -> Unit,
    onDuplicateScene: (SceneEntity) -> Unit,
    onMoveScene: (SceneEntity, Long) -> Unit,
    onArchiveScene: (SceneEntity) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onNavigateToRangaCreation: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("Todas") }
    var isGridView by remember { mutableStateOf(false) }
    var sceneToDelete by remember { mutableStateOf<SceneEntity?>(null) }
    var sceneToMove by remember { mutableStateOf<SceneEntity?>(null) }
    var showNewSceneModal by remember { mutableStateOf(false) }

    // Auto-select initial scene if none selected and scenes exist
    LaunchedEffect(scenes, activeScene) {
        if (activeScene == null && scenes.isNotEmpty()) {
            val defaultScene = scenes.find { it.sceneOrder == 3 } ?: scenes.first()
            onSetActiveScene(defaultScene)
        }
    }

    // Filter scenes
    val filteredScenes = scenes.filter { sc ->
        val matchesProject = selectedProjectId == null || sc.projectId == selectedProjectId
        val matchesStatus = when (selectedStatusFilter) {
            "Rascunhos" -> sc.status.contains("Rascunho", ignoreCase = true)
            "Em produção" -> sc.status.contains("produção", ignoreCase = true)
            "Concluídas" -> sc.status.contains("Conclu", ignoreCase = true)
            else -> true
        }
        val matchesSearch = searchQuery.isBlank() ||
                sc.name.contains(searchQuery, ignoreCase = true) ||
                sc.scenarioName.contains(searchQuery, ignoreCase = true) ||
                sc.characterIds.contains(searchQuery, ignoreCase = true) ||
                sc.description.contains(searchQuery, ignoreCase = true) ||
                sc.dialogues.contains(searchQuery, ignoreCase = true)
        matchesProject && matchesStatus && matchesSearch
    }.sortedBy { it.sceneOrder }

    val activeProject = projects.find { it.id == (activeScene?.projectId ?: selectedProjectId ?: 1L) }
        ?: projects.firstOrNull()
        ?: ProjectEntity(name = "Aventuras das Frutas", description = "", type = "Desenho Animado", category = "Infantil")

    val activeEpisode = episodes.find { it.id == (activeScene?.episodeId ?: 1L) }
        ?: episodes.firstOrNull()
        ?: EpisodeEntity(projectId = activeProject.id, episodeNumber = 1, title = "O Mistério do Desaparecimento", description = "")

    val currentWorkingScene = activeScene ?: filteredScenes.firstOrNull() ?: SceneEntity(
        episodeId = activeEpisode.id,
        projectId = activeProject.id,
        sceneOrder = 1,
        name = "A Discussão na Cozinha",
        description = "António e Bia discutem sobre o desaparecimento de Carlos.",
        imageUri = "scene_apple_banana_kitchen_1787050705242"
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isCompact = maxWidth < 900.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
        ) {
            // --- TOP HEADER BAR ---
            SceneTopHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onNewSceneClick = { showNewSceneModal = true },
                onHelpClick = {
                    Toast.makeText(context, "Editor de Cenas: Monte seus roteiros em momentos audiovisuais.", Toast.LENGTH_SHORT).show()
                }
            )

            // --- BREADCRUMB & TOOLBAR ---
            SceneBreadcrumbToolbar(
                project = activeProject,
                episode = activeEpisode,
                activeScene = currentWorkingScene,
                allScenes = filteredScenes,
                isStudioMode = isStudioMode,
                isGridView = isGridView,
                onToggleStudioMode = { onSetStudioMode(!isStudioMode) },
                onToggleGridView = { isGridView = !isGridView },
                onSelectScene = { onSetActiveScene(it) },
                onNewSceneClick = { showNewSceneModal = true }
            )

            // --- MAIN VIEW CONTENT ---
            if (isStudioMode && activeScene != null) {
                // Production Studio Editor Mode (matches reference screenshot!)
                SceneStudioEditorView(
                    scene = currentWorkingScene,
                    project = activeProject,
                    episode = activeEpisode,
                    characters = characters,
                    scenarios = scenarios,
                    isCompact = isCompact,
                    lastSaveTimestamp = lastSaveTimestamp,
                    onSaveScene = { updated, showLog -> onSaveScene(updated, showLog) },
                    onOpenAiWithPrompt = onOpenAiWithPrompt,
                    onNavigateToRangaCreation = onNavigateToRangaCreation,
                    onCloseStudioMode = { onSetStudioMode(false) }
                )
            } else {
                // Catalog / List View of Scenes
                SceneCatalogView(
                    scenes = filteredScenes,
                    projects = projects,
                    episodes = episodes,
                    selectedStatusFilter = selectedStatusFilter,
                    isGridView = isGridView,
                    onSelectStatusFilter = { selectedStatusFilter = it },
                    onOpenSceneStudio = { sc ->
                        onSetActiveScene(sc)
                        onSetStudioMode(true)
                    },
                    onEditScene = { sc ->
                        onSetActiveScene(sc)
                        showNewSceneModal = true
                    },
                    onDuplicateScene = onDuplicateScene,
                    onMoveScene = { sc -> sceneToMove = sc },
                    onArchiveScene = onArchiveScene,
                    onDeleteScene = { sc -> sceneToDelete = sc },
                    onNewSceneClick = { showNewSceneModal = true }
                )
            }
        }
    }

    // Modal: Delete Confirmation
    if (sceneToDelete != null) {
        val sc = sceneToDelete!!
        AlertDialog(
            onDismissRequest = { sceneToDelete = null },
            title = { Text("Excluir Cena", fontWeight = FontWeight.Bold) },
            text = { Text("Tem certeza que deseja excluir permanentemente a cena “${sc.name}” (Cena ${sc.sceneOrder})? Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteScene(sc)
                        sceneToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { sceneToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal: Move Scene
    if (sceneToMove != null) {
        val sc = sceneToMove!!
        var targetProjectId by remember { mutableStateOf(projects.firstOrNull()?.id ?: 1L) }
        AlertDialog(
            onDismissRequest = { sceneToMove = null },
            title = { Text("Mover Cena para Outro Projeto", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Selecione o projeto de destino para “${sc.name}”:")
                    projects.forEach { proj ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (proj.id == targetProjectId) NavyPrimary.copy(alpha = 0.12f) else Color.Transparent)
                                .clickable { targetProjectId = proj.id }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = NavyPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(proj.name, fontWeight = if (proj.id == targetProjectId) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onMoveScene(sc, targetProjectId)
                        sceneToMove = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Mover Cena", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { sceneToMove = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Modal: Create New Scene / Edit Full Form
    if (showNewSceneModal) {
        SceneFormModal(
            initialScene = activeScene,
            projects = projects,
            episodes = episodes,
            scenarios = scenarios,
            characters = characters,
            onDismiss = { showNewSceneModal = false },
            onSave = { newScene ->
                onSaveScene(newScene, true)
                onSetActiveScene(newScene)
                onSetStudioMode(true)
                showNewSceneModal = false
            }
        )
    }
}

// -------------------------------------------------------------
// 1. TOP HEADER COMPONENT
// -------------------------------------------------------------
@Composable
private fun SceneTopHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onNewSceneClick: () -> Unit,
    onHelpClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left: Title and subtitle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(NavyPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Text(
                        text = "Cenas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )
                    Text(
                        text = "Monte suas cenas e transforme seus roteiros em momentos da história.",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // Right: Search bar & User controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Search Input
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text("Pesquisar cenas...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                    },
                    modifier = Modifier
                        .widthIn(min = 220.dp, max = 340.dp)
                        .height(44.dp)
                        .testTag("scene_top_search_input"),
                    shape = RoundedCornerShape(22.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NavyPrimary,
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    singleLine = true
                )

                // Help Icon
                IconButton(onClick = onHelpClick, modifier = Modifier.size(38.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ajuda", tint = Color(0xFF64748B))
                }

                // Notifications
                BadgedBox(
                    badge = {
                        Badge(containerColor = RoseError) { Text("2", color = Color.White, fontSize = 10.sp) }
                    }
                ) {
                    Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificações", tint = Color(0xFF64748B))
                }

                // User Profile
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFF1F5F9))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Column {
                        Text("Augusto", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDark)
                        Text("Plano Profissional", fontSize = 10.sp, color = Color(0xFF64748B))
                    }
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. BREADCRUMB & TOOLBAR
// -------------------------------------------------------------
@Composable
private fun SceneBreadcrumbToolbar(
    project: ProjectEntity,
    episode: EpisodeEntity,
    activeScene: SceneEntity,
    allScenes: List<SceneEntity>,
    isStudioMode: Boolean,
    isGridView: Boolean,
    onToggleStudioMode: () -> Unit,
    onToggleGridView: () -> Unit,
    onSelectScene: (SceneEntity) -> Unit,
    onNewSceneClick: () -> Unit
) {
    var sceneDropdownOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8FAFC),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Breadcrumbs: Project > Episode > Scene
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                // Project Breadcrumb
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Projeto", fontSize = 9.sp, color = Color(0xFF94A3B8))
                        Text(project.name, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.padding(start = 6.dp).size(14.dp))
                }

                // Episode Breadcrumb
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Column {
                        Text("Episódio", fontSize = 9.sp, color = Color(0xFF94A3B8))
                        Text("${episode.episodeNumber} - ${episode.title}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                    }
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = Color(0xFFCBD5E1), modifier = Modifier.padding(start = 6.dp).size(14.dp))
                }

                // Scene Selector with Dropdown
                Box {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.White)
                            .border(1.dp, NavyPrimary.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .clickable { sceneDropdownOpen = true }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Column {
                            Text("Cena", fontSize = 9.sp, color = NavyPrimary)
                            Text("${activeScene.sceneOrder} - ${activeScene.name}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyPrimary)
                        }
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = NavyPrimary, modifier = Modifier.padding(start = 6.dp).size(16.dp))
                    }

                    DropdownMenu(
                        expanded = sceneDropdownOpen,
                        onDismissRequest = { sceneDropdownOpen = false }
                    ) {
                        allScenes.forEach { sc ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text("Cena ${sc.sceneOrder}:", fontWeight = FontWeight.Bold, color = NavyPrimary)
                                        Text(sc.name, color = NavyDark)
                                        if (sc.id == activeScene.id) {
                                            Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                },
                                onClick = {
                                    onSelectScene(sc)
                                    sceneDropdownOpen = false
                                }
                            )
                        }
                    }
                }
            }

            // Right side View Controls & Action Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Toggle between Studio Mode and Catalog Mode
                IconButton(
                    onClick = onToggleStudioMode,
                    modifier = Modifier
                        .size(38.dp)
                        .background(if (isStudioMode) NavyPrimary.copy(alpha = 0.12f) else Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, if (isStudioMode) NavyPrimary else Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = if (isStudioMode) Icons.Default.Tune else Icons.Default.Movie,
                        contentDescription = "Alternar Modo Studio",
                        tint = if (isStudioMode) NavyPrimary else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Grid / List Toggle
                IconButton(
                    onClick = onToggleGridView,
                    modifier = Modifier
                        .size(38.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = if (isGridView) Icons.Default.GridView else Icons.Default.ViewList,
                        contentDescription = "Visualização",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // + Nova Cena Button
                Button(
                    onClick = onNewSceneClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    modifier = Modifier.testTag("btn_new_scene_header")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("+ Nova Cena", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. PRODUCTION STUDIO EDITOR VIEW (Matches reference screenshot!)
// -------------------------------------------------------------
@Composable
private fun SceneStudioEditorView(
    scene: SceneEntity,
    project: ProjectEntity,
    episode: EpisodeEntity,
    characters: List<CharacterEntity>,
    scenarios: List<ScenarioEntity>,
    isCompact: Boolean,
    lastSaveTimestamp: Long,
    onSaveScene: (SceneEntity, Boolean) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onNavigateToRangaCreation: () -> Unit,
    onCloseStudioMode: () -> Unit
) {
    val context = LocalContext.current

    // Local mutable states for editing this scene in real time
    var sceneName by remember(scene.id) { mutableStateOf(scene.name) }
    var sceneDescription by remember(scene.id) { mutableStateOf(scene.description) }
    var sceneDuration by remember(scene.id) { mutableStateOf(scene.duration) }
    var sceneStatus by remember(scene.id) { mutableStateOf(scene.status) }
    var scenarioName by remember(scene.id) { mutableStateOf(scene.scenarioName) }
    var scenarioLighting by remember(scene.id) { mutableStateOf(scene.scenarioLighting) }
    var cameraShot by remember(scene.id) { mutableStateOf(scene.cameraShot) }
    var cameraMovement by remember(scene.id) { mutableStateOf(scene.cameraMovement) }
    var musicTitle by remember(scene.id) { mutableStateOf(scene.music) }

    // Dialogues list
    val dialoguesList = remember(scene.id) {
        mutableStateListOf(
            SceneDialogueItem("António", "Precisamos descobrir quem fez isso!", "Bravo", "Normal", "Voz Masculina 1"),
            SceneDialogueItem("Bia", "Eu acho que sei onde procurar.", "Assustada", "Normal", "Voz Feminina 1")
        )
    }

    // Actions list
    val actionsList = remember(scene.id) {
        mutableStateListOf(
            "António entra na cozinha.",
            "Bia está mexendo na mesa.",
            "António aponta para a janela.",
            "Bia se assusta e olha para a porta."
        )
    }

    // Characters in scene
    val sceneCharacters = remember(scene.id) {
        mutableStateListOf(
            SceneCharacterItem("António", "Esquerda", "Bravo", "Apontando", "00:00:00", "--:--:--"),
            SceneCharacterItem("Bia", "Direita", "Assustada", "Explicando", "00:00:02", "--:--:--")
        )
    }

    // Expressions state
    var selectedExpression by remember { mutableStateOf("Bravo") }

    // Video player playback controls state
    var isPlaying by remember { mutableStateOf(false) }
    var playbackProgress by remember { mutableFloatStateOf(0.28f) } // 00:00:07 of 00:00:25
    var videoQuality by remember { mutableStateOf("1080p") }
    var audioVolume by remember { mutableFloatStateOf(0.85f) }

    // Right inspector tabs
    var selectedInspectorTab by remember { mutableIntStateOf(0) } // 0: Configurações, 1: Assistente RANGA

    // Add Dialogue Modal
    var showAddDialogueDialog by remember { mutableStateOf(false) }
    var showAddActionDialog by remember { mutableStateOf(false) }
    var showAddCharacterDialog by remember { mutableStateOf(false) }

    // Auto-save notification formatted time
    val timeFormat = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val formattedSaveTime = timeFormat.format(Date(lastSaveTimestamp))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // TOP SECTION: Workspace Split into Canvas + Inspector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // LEFT / MAIN COLUMN (Canvas + Characters + Dialogues + Actions + Expressions)
            Column(
                modifier = Modifier
                    .weight(if (isCompact) 1f else 0.68f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 1. VIDEO PREVIEW CANVAS (16:9)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Canvas Header: Title & Quality Selector
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pré-visualização da cena",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark
                            )

                            // Quality selector
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("Qualidade", fontSize = 11.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(videoQuality, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Video Canvas Display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFF0F172A)),
                            contentAlignment = Alignment.Center
                        ) {
                            // Scene Image Preview
                            Image(
                                painter = painterResource(id = com.example.R.drawable.scene_apple_banana_kitchen_1787050705242),
                                contentDescription = "Pré-visualização da cena na cozinha",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Live Camera Indicator Badge
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopStart)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.Black.copy(alpha = 0.65f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(RoseError))
                                    Text("REC • $cameraShot", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Video Player Control Bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Play/Pause button
                            IconButton(
                                onClick = { isPlaying = !isPlaying },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = "Reproduzir",
                                    tint = Color.White
                                )
                            }

                            // Step forward / backward / replay
                            IconButton(onClick = { playbackProgress = 0f }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.Replay, contentDescription = "Reiniciar", tint = Color(0xFFCBD5E1), modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { playbackProgress = (playbackProgress - 0.05f).coerceAtLeast(0f) }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.SkipPrevious, contentDescription = "Anterior", tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
                            }
                            IconButton(onClick = { playbackProgress = (playbackProgress + 0.05f).coerceAtMost(1f) }, modifier = Modifier.size(28.dp)) {
                                Icon(imageVector = Icons.Default.SkipNext, contentDescription = "Próximo", tint = Color(0xFFCBD5E1), modifier = Modifier.size(18.dp))
                            }

                            // Timestamp display
                            Text(
                                text = "00:00:07 / 00:00:25",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )

                            // Seek progress slider
                            Slider(
                                value = playbackProgress,
                                onValueChange = { playbackProgress = it },
                                modifier = Modifier.weight(1f),
                                colors = SliderDefaults.colors(
                                    thumbColor = CyanAccent,
                                    activeTrackColor = CyanAccent,
                                    inactiveTrackColor = Color(0xFF334155)
                                )
                            )

                            // Volume control
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = "Volume", tint = Color(0xFFCBD5E1), modifier = Modifier.size(16.dp))
                            Slider(
                                value = audioVolume,
                                onValueChange = { audioVolume = it },
                                modifier = Modifier.width(60.dp),
                                colors = SliderDefaults.colors(
                                    thumbColor = Color.White,
                                    activeTrackColor = Color.White,
                                    inactiveTrackColor = Color(0xFF334155)
                                )
                            )

                            // Fullscreen
                            IconButton(
                                onClick = { Toast.makeText(context, "Modo tela cheia ativado", Toast.LENGTH_SHORT).show() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Fullscreen, contentDescription = "Tela Cheia", tint = Color.White, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // 2. MIDDLE ROW: DIÁLOGOS, AÇÕES, EXPRESSÕES RÁPIDAS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Card: Diálogos da cena
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Diálogos da cena", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                                TextButton(
                                    onClick = { showAddDialogueDialog = true },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                                    Text("Adicionar fala", fontSize = 11.sp, color = NavyPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            dialoguesList.forEach { diag ->
                                DialogueRowItem(item = diag, onPlayVoice = {
                                    Toast.makeText(context, "Reproduzindo fala de ${diag.character}...", Toast.LENGTH_SHORT).show()
                                })
                            }
                        }
                    }

                    // Card: Ações da cena
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Ações da cena", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                                TextButton(
                                    onClick = { showAddActionDialog = true },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                                    Text("Adicionar ação", fontSize = 11.sp, color = NavyPrimary, fontWeight = FontWeight.SemiBold)
                                }
                            }

                            actionsList.forEachIndexed { index, act ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF8FAFC))
                                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
                                        Text("⠿", color = Color(0xFF94A3B8), fontSize = 12.sp)
                                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AmberGold, modifier = Modifier.size(12.dp))
                                        Text(act, fontSize = 11.sp, color = NavyDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                    }
                                    IconButton(
                                        onClick = { if (actionsList.size > 1) actionsList.removeAt(index) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Remover", tint = Color(0xFF94A3B8), modifier = Modifier.size(12.dp))
                                    }
                                }
                            }
                        }
                    }

                    // Card: Expressões rápidas
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Expressões rápidas", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)

                            val emotions = listOf(
                                "Feliz" to "😊",
                                "Triste" to "😢",
                                "Bravo" to "😡",
                                "Assustado" to "😨",
                                "Surpreso" to "😲",
                                "Confuso" to "🤔",
                                "Pensativo" to "🧐",
                                "Rindo" to "😂",
                                "Chorando" to "😭",
                                "Personalizado" to "✨"
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(5),
                                modifier = Modifier.height(130.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(emotions) { (name, emoji) ->
                                    val isSelected = selectedExpression == name
                                    Column(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) NavyPrimary.copy(alpha = 0.1f) else Color(0xFFF8FAFC))
                                            .border(
                                                1.dp,
                                                if (isSelected) NavyPrimary else Color(0xFFE2E8F0),
                                                RoundedCornerShape(8.dp)
                                            )
                                            .clickable { selectedExpression = name }
                                            .padding(4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(emoji, fontSize = 18.sp)
                                        Text(
                                            text = name,
                                            fontSize = 9.sp,
                                            color = if (isSelected) NavyPrimary else Color(0xFF64748B),
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. PERSONAGENS NA CENA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Personagens na cena", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                            OutlinedButton(
                                onClick = { showAddCharacterDialog = true },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Adicionar", fontSize = 12.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            sceneCharacters.forEach { charItem ->
                                SceneCharacterCard(
                                    item = charItem,
                                    modifier = Modifier.weight(1f),
                                    onMoreClick = {
                                        Toast.makeText(context, "Opções de ${charItem.name}", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }

                            // Add character slot card
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(115.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFFF8FAFC))
                                    .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(10.dp))
                                    .clickable { showAddCharacterDialog = true },
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(18.dp))
                                    Text("Adicionar personagem", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyPrimary)
                                }
                            }
                        }
                    }
                }
            }

            // RIGHT COLUMN: INSPECTOR SIDEBAR WITH TABS
            Card(
                modifier = Modifier
                    .weight(if (isCompact) 1f else 0.32f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Tab Header: [ Configurações ] | [ Assistente RANGA ✨ ]
                    TabRow(
                        selectedTabIndex = selectedInspectorTab,
                        containerColor = Color.White,
                        contentColor = NavyPrimary,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedInspectorTab]),
                                color = NavyPrimary
                            )
                        }
                    ) {
                        Tab(
                            selected = selectedInspectorTab == 0,
                            onClick = { selectedInspectorTab = 0 },
                            text = { Text("Configurações", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedInspectorTab == 1,
                            onClick = { selectedInspectorTab = 1 },
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Assistente RANGA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AmberGold, modifier = Modifier.size(14.dp))
                                }
                            }
                        )
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    if (selectedInspectorTab == 0) {
                        // TAB 0: CONFIGURAÇÕES DA CENA
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text("Configurações da cena", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDark)

                            // Nome da cena
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Nome da cena", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = sceneName,
                                    onValueChange = { sceneName = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NavyPrimary,
                                        unfocusedBorderColor = Color(0xFFE2E8F0)
                                    )
                                )
                            }

                            // Descrição
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Descrição", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                OutlinedTextField(
                                    value = sceneDescription,
                                    onValueChange = { sceneDescription = it },
                                    modifier = Modifier.fillMaxWidth().height(70.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NavyPrimary,
                                        unfocusedBorderColor = Color(0xFFE2E8F0)
                                    )
                                )
                            }

                            // Duração & Status
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Duração", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                    OutlinedTextField(
                                        value = sceneDuration,
                                        onValueChange = { sceneDuration = it },
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true
                                    )
                                }

                                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("Status", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                    var statusMenuOpen by remember { mutableStateOf(false) }
                                    Box {
                                        OutlinedButton(
                                            onClick = { statusMenuOpen = true },
                                            modifier = Modifier.fillMaxWidth().height(52.dp),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(sceneStatus, fontSize = 11.sp, color = NavyDark, maxLines = 1)
                                            Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, modifier = Modifier.size(14.dp))
                                        }
                                        DropdownMenu(expanded = statusMenuOpen, onDismissRequest = { statusMenuOpen = false }) {
                                            listOf("Rascunho", "Em produção", "Concluída").forEach { st ->
                                                DropdownMenuItem(
                                                    text = { Text(st) },
                                                    onClick = {
                                                        sceneStatus = st
                                                        statusMenuOpen = false
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Cenário Card Selector
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Cenário", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                                var scenarioMenuOpen by remember { mutableStateOf(false) }
                                Box {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFFF8FAFC))
                                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
                                            .clickable { scenarioMenuOpen = true }
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = com.example.R.drawable.scenario_family_house_1787049241112),
                                            contentDescription = null,
                                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(6.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(scenarioName, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                                            Text("Interior • $scenarioLighting", fontSize = 10.sp, color = Color(0xFF64748B))
                                        }
                                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = null, tint = Color(0xFF64748B))
                                    }

                                    DropdownMenu(expanded = scenarioMenuOpen, onDismissRequest = { scenarioMenuOpen = false }) {
                                        scenarios.forEach { sc ->
                                            DropdownMenuItem(
                                                text = { Text(sc.name) },
                                                onClick = {
                                                    scenarioName = sc.name
                                                    scenarioMenuOpen = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Accordions: Câmera, Diálogos, Ações, Sons
                            SceneInspectorAccordion(
                                title = "Câmera e movimentos",
                                icon = Icons.Default.Videocam,
                                content = {
                                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text("Plano: $cameraShot", fontSize = 11.sp, color = NavyDark)
                                        Text("Movimento: $cameraMovement (Duração: 00:00:05)", fontSize = 11.sp, color = Color(0xFF64748B))
                                    }
                                }
                            )

                            SceneInspectorAccordion(
                                title = "Diálogos",
                                icon = Icons.Default.People,
                                content = {
                                    Text("${dialoguesList.size} falas configuradas com dublagem", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            )

                            SceneInspectorAccordion(
                                title = "Ações",
                                icon = Icons.Default.Movie,
                                content = {
                                    Text("${actionsList.size} ações sequenciadas na cena", fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                            )

                            SceneInspectorAccordion(
                                title = "Sons e músicas",
                                icon = Icons.Default.MusicNote,
                                content = {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text("Música: $musicTitle", fontSize = 11.sp, color = NavyDark)
                                        Text("SFX: Passos, Porta rangendo, Cozinha", fontSize = 10.sp, color = Color(0xFF64748B))
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            // Action: Salvar Cena Button
                            Button(
                                onClick = {
                                    val updated = scene.copy(
                                        name = sceneName,
                                        description = sceneDescription,
                                        duration = sceneDuration,
                                        status = sceneStatus,
                                        scenarioName = scenarioName,
                                        scenarioLighting = scenarioLighting,
                                        cameraShot = cameraShot,
                                        cameraMovement = cameraMovement,
                                        music = musicTitle,
                                        updatedAt = System.currentTimeMillis()
                                    )
                                    onSaveScene(updated, true)
                                    Toast.makeText(context, "Cena salva com sucesso!", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.fillMaxWidth().height(44.dp).testTag("btn_save_scene_inspector"),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Salvar cena", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            // Auto-save status line
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldSuccess))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Salvo automaticamente às $formattedSaveTime", fontSize = 10.sp, color = Color(0xFF64748B))
                            }
                        }
                    } else {
                        // TAB 1: ASSISTENTE RANGA ✨
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("Assistente RANGA ✨", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                            Text(
                                text = "Peça sugestões de diálogos, movimentos de câmera, ações e sonoplastia para esta cena:",
                                fontSize = 11.sp,
                                color = Color(0xFF64748B)
                            )

                            var aiPromptText by remember {
                                mutableStateOf("Crie uma cena onde duas frutas discutem na cozinha porque alguém desapareceu.")
                            }

                            OutlinedTextField(
                                value = aiPromptText,
                                onValueChange = { aiPromptText = it },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = NavyPrimary,
                                    unfocusedBorderColor = Color(0xFFE2E8F0)
                                )
                            )

                            Button(
                                onClick = {
                                    onOpenAiWithPrompt(aiPromptText)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Sugerir com IA", fontWeight = FontWeight.Bold, color = Color.White)
                            }

                            HorizontalDivider(color = Color(0xFFE2E8F0))

                            // Import from Script
                            OutlinedButton(
                                onClick = onNavigateToRangaCreation,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("📄 Importar do roteiro", fontSize = 12.sp, color = NavyPrimary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // BOTTOM SECTION: PROFESSIONAL TIMELINE (Matches reference image in every track!)
        SceneBottomTimeline(
            currentTime = "00:00:07",
            totalDuration = "00:00:30",
            progress = playbackProgress,
            onSeek = { playbackProgress = it }
        )
    }

    // Modal: Add Dialogue
    if (showAddDialogueDialog) {
        var charName by remember { mutableStateOf("António") }
        var speechText by remember { mutableStateOf("") }
        var emotionChoice by remember { mutableStateOf("Bravo") }

        AlertDialog(
            onDismissRequest = { showAddDialogueDialog = false },
            title = { Text("Adicionar Fala", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Personagem:")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("António", "Bia", "Carlos").forEach { c ->
                            FilterChip(
                                selected = charName == c,
                                onClick = { charName = c },
                                label = { Text(c) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = speechText,
                        onValueChange = { speechText = it },
                        label = { Text("Texto da fala") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (speechText.isNotBlank()) {
                            dialoguesList.add(SceneDialogueItem(charName, speechText, emotionChoice, "Normal", "Voz Masculina 1"))
                        }
                        showAddDialogueDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialogueDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Modal: Add Action
    if (showAddActionDialog) {
        var actionInput by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddActionDialog = false },
            title = { Text("Adicionar Ação na Cena", fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = actionInput,
                    onValueChange = { actionInput = it },
                    label = { Text("Descreva a ação (ex: António corre para a porta)") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (actionInput.isNotBlank()) {
                            actionsList.add(actionInput)
                        }
                        showAddActionDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddActionDialog = false }) { Text("Cancelar") }
            }
        )
    }

    // Modal: Add Character
    if (showAddCharacterDialog) {
        var selectedCharName by remember { mutableStateOf(characters.firstOrNull()?.name ?: "Carlos") }
        var positionChoice by remember { mutableStateOf("Centro") }
        var expressionChoice by remember { mutableStateOf("Curioso") }

        AlertDialog(
            onDismissRequest = { showAddCharacterDialog = false },
            title = { Text("Adicionar Personagem à Cena", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Selecione o personagem:")
                    characters.forEach { ch ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (selectedCharName == ch.name) NavyPrimary.copy(alpha = 0.1f) else Color.Transparent)
                                .clickable { selectedCharName = ch.name }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(ch.name, fontWeight = if (selectedCharName == ch.name) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        sceneCharacters.add(SceneCharacterItem(selectedCharName, positionChoice, expressionChoice, "Em espera", "00:00:05", "--:--:--"))
                        showAddCharacterDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                ) {
                    Text("Adicionar à Cena")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCharacterDialog = false }) { Text("Cancelar") }
            }
        )
    }
}

// -------------------------------------------------------------
// 4. TIMELINE COMPONENT (Full multi-track matching reference image)
// -------------------------------------------------------------
@Composable
private fun SceneBottomTimeline(
    currentTime: String,
    totalDuration: String,
    progress: Float,
    onSeek: (Float) -> Unit
) {
    val context = LocalContext.current
    var timelineZoom by remember { mutableFloatStateOf(0.5f) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Timeline Top Controls Bar: Undo, Redo, Cut, Duplicate, Delete, Zoom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color(0xFF1E293B)), RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Editing Actions
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TimelineActionBtn(icon = Icons.AutoMirrored.Filled.Undo, label = "Desfazer") {
                        Toast.makeText(context, "Desfazer ação", Toast.LENGTH_SHORT).show()
                    }
                    TimelineActionBtn(icon = Icons.AutoMirrored.Filled.Redo, label = "Refazer") {
                        Toast.makeText(context, "Refazer ação", Toast.LENGTH_SHORT).show()
                    }
                    Box(modifier = Modifier.width(1.dp).height(16.dp).background(Color(0xFF334155)))
                    TimelineActionBtn(icon = Icons.Default.ContentCut, label = "Cortar") {
                        Toast.makeText(context, "Dividir clipe no cursor", Toast.LENGTH_SHORT).show()
                    }
                    TimelineActionBtn(icon = Icons.Default.ContentCopy, label = "Duplicar") {
                        Toast.makeText(context, "Clipe duplicado", Toast.LENGTH_SHORT).show()
                    }
                    TimelineActionBtn(icon = Icons.Default.Delete, label = "Excluir") {
                        Toast.makeText(context, "Clipe removido", Toast.LENGTH_SHORT).show()
                    }
                }

                // Right Zoom Controls
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("—", color = Color(0xFF94A3B8), fontSize = 14.sp)
                    Slider(
                        value = timelineZoom,
                        onValueChange = { timelineZoom = it },
                        modifier = Modifier.width(80.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = CyanAccent,
                            activeTrackColor = CyanAccent,
                            inactiveTrackColor = Color(0xFF334155)
                        )
                    )
                    Text("+", color = Color(0xFF94A3B8), fontSize = 14.sp)
                }
            }

            // Timeline Tracks Container
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                // Track Headers (Left sidebar)
                Column(
                    modifier = Modifier.width(130.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Empty space for ruler alignment
                    Box(modifier = Modifier.height(20.dp))

                    TimelineTrackHeader(icon = Icons.Default.Movie, title = "Vídeo / Cena")
                    TimelineTrackHeader(icon = Icons.Default.Person, title = "Personagens")
                    TimelineTrackHeader(icon = Icons.Default.Mic, title = "Diálogos")
                    TimelineTrackHeader(icon = Icons.Default.MusicNote, title = "Música")
                    TimelineTrackHeader(icon = Icons.Default.VolumeUp, title = "Efeitos sonoros")
                }

                // Track Visual Timeline Lanes (Right area)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .horizontalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier.width(850.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Time Ruler
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            listOf("00:00", "00:05", "00:10", "00:15", "00:20", "00:25", "00:30").forEach { mark ->
                                Text(mark, fontSize = 9.sp, color = Color(0xFF94A3B8), fontFamily = FontFamily.Monospace)
                            }
                        }

                        // Track 1: Filmstrip / Video
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(38.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFF334155), RoundedCornerShape(4.dp))
                        ) {
                            Row(modifier = Modifier.fillMaxSize()) {
                                repeat(7) {
                                    Image(
                                        painter = painterResource(id = com.example.R.drawable.scene_apple_banana_kitchen_1787050705242),
                                        contentDescription = null,
                                        modifier = Modifier.weight(1f).fillMaxHeight().border(0.5.dp, Color.Black),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        // Track 2: Characters
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                // António block
                                Box(
                                    modifier = Modifier
                                        .width(360.dp)
                                        .fillMaxHeight(0.85f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF991B1B))
                                        .padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("António", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                // Bia block
                                Box(
                                    modifier = Modifier
                                        .width(340.dp)
                                        .fillMaxHeight(0.85f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF854D0E))
                                        .padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("Bia", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Track 3: Dialogues
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .width(240.dp)
                                        .fillMaxHeight(0.85f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NavyPrimary)
                                        .padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("António: Precisamos descobrir...", fontSize = 9.sp, color = Color.White, maxLines = 1)
                                }
                                Spacer(modifier = Modifier.width(120.dp))
                                Box(
                                    modifier = Modifier
                                        .width(260.dp)
                                        .fillMaxHeight(0.85f)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NavyPrimary)
                                        .padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    Text("Bia: Eu acho que sei onde procurar.", fontSize = 9.sp, color = Color.White, maxLines = 1)
                                }
                            }
                        }

                        // Track 4: Music (Green waveform)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.92f)
                                    .fillMaxHeight(0.85f)
                                    .align(Alignment.CenterStart)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color(0xFF047857))
                                    .padding(horizontal = 6.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text("🎵 Música de Mistério ~~~~~~~~~|~~~~~~|~~~~~~~~", fontSize = 9.sp, color = Color.White, maxLines = 1)
                            }
                        }

                        // Track 5: Sound Effects
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF1E293B))
                        ) {
                            Row(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier.width(90.dp).fillMaxHeight(0.85f).clip(RoundedCornerShape(4.dp)).background(Color(0xFF4338CA)).padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Passos", fontSize = 9.sp, color = Color.White)
                                }
                                Box(
                                    modifier = Modifier.width(110.dp).fillMaxHeight(0.85f).clip(RoundedCornerShape(4.dp)).background(Color(0xFF4338CA)).padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Porta rangendo", fontSize = 9.sp, color = Color.White)
                                }
                                Spacer(modifier = Modifier.width(60.dp))
                                Box(
                                    modifier = Modifier.width(150.dp).fillMaxHeight(0.85f).clip(RoundedCornerShape(4.dp)).background(Color(0xFF4338CA)).padding(horizontal = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Ambiente cozinha", fontSize = 9.sp, color = Color.White)
                                }
                            }
                        }
                    }

                    // Draggable Playhead Needle (Blue line with cursor top)
                    Box(
                        modifier = Modifier
                            .offset(x = (850.dp * progress) - 8.dp)
                            .width(16.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Needle Head
                            Box(
                                modifier = Modifier
                                    .size(16.dp, 12.dp)
                                    .clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp))
                                    .background(CyanAccent)
                            )
                            // Needle Line
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(180.dp)
                                    .background(CyanAccent)
                            )
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 5. SCENE CATALOG VIEW (List & Grid Cards)
// -------------------------------------------------------------
@Composable
private fun SceneCatalogView(
    scenes: List<SceneEntity>,
    projects: List<ProjectEntity>,
    episodes: List<EpisodeEntity>,
    selectedStatusFilter: String,
    isGridView: Boolean,
    onSelectStatusFilter: (String) -> Unit,
    onOpenSceneStudio: (SceneEntity) -> Unit,
    onEditScene: (SceneEntity) -> Unit,
    onDuplicateScene: (SceneEntity) -> Unit,
    onMoveScene: (SceneEntity) -> Unit,
    onArchiveScene: (SceneEntity) -> Unit,
    onDeleteScene: (SceneEntity) -> Unit,
    onNewSceneClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Filter Chips
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Todas", "Rascunhos", "Em produção", "Concluídas").forEach { filter ->
                    val isSelected = selectedStatusFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectStatusFilter(filter) },
                        label = { Text(filter, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = NavyDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) NavyPrimary else Color(0xFFCBD5E1)
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        // Scenes Count Summary
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${scenes.size} cenas encontradas",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        if (scenes.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(48.dp))
                        Text("Nenhuma cena encontrada", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                        Text("Crie sua primeira cena para começar a produzir a história.", fontSize = 12.sp, color = Color(0xFF64748B))
                        Button(
                            onClick = onNewSceneClick,
                            colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
                        ) {
                            Text("+ Nova Cena", color = Color.White)
                        }
                    }
                }
            }
        } else {
            // Render Cards
            items(scenes) { sc ->
                val proj = projects.find { it.id == sc.projectId }
                val ep = episodes.find { it.id == sc.episodeId }
                SceneCatalogCard(
                    scene = sc,
                    projectName = proj?.name ?: "Aventuras das Frutas",
                    episodeTitle = ep?.title ?: "O Mistério do Desaparecimento",
                    onOpen = { onOpenSceneStudio(sc) },
                    onEdit = { onEditScene(sc) },
                    onDuplicate = { onDuplicateScene(sc) },
                    onMove = { onMoveScene(sc) },
                    onArchive = { onArchiveScene(sc) },
                    onDelete = { onDeleteScene(sc) }
                )
            }
        }
    }
}

// -------------------------------------------------------------
// 6. HELPER SUB-COMPONENTS
// -------------------------------------------------------------
@Composable
private fun SceneCatalogCard(
    scene: SceneEntity,
    projectName: String,
    episodeTitle: String,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onMove: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 16:9 Thumbnail with Scene Number Badge
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A)),
                contentAlignment = Alignment.BottomStart
            ) {
                val imageRes = when (scene.sceneOrder) {
                    1 -> com.example.R.drawable.scene_forest_run_1787050741889
                    2 -> com.example.R.drawable.scene_school_mystery_1787050758639
                    else -> com.example.R.drawable.scene_apple_banana_kitchen_1787050705242
                }

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Number badge
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(NavyPrimary.copy(alpha = 0.85f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text("Cena ${scene.sceneOrder}", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Info Details
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = scene.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyDark
                    )

                    // Status Badge
                    val statusBg = when (scene.status) {
                        "Concluída" -> EmeraldSuccess.copy(alpha = 0.15f)
                        "Em produção" -> NavyPrimary.copy(alpha = 0.12f)
                        else -> Color(0xFFF1F5F9)
                    }
                    val statusColor = when (scene.status) {
                        "Concluída" -> EmeraldSuccess
                        "Em produção" -> NavyPrimary
                        else -> Color(0xFF64748B)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(statusBg)
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(scene.status, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "$projectName • $episodeTitle",
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )

                if (scene.description.isNotBlank()) {
                    Text(
                        text = scene.description,
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Tags: Cenário & Personagens & Duração
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Landscape, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(scene.scenarioName.ifBlank { "Cozinha da Casa" }, fontSize = 10.sp, color = Color(0xFF475569))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFF1F5F9))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(imageVector = Icons.Default.People, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(12.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(scene.characterIds.ifBlank { "António, Bia" }, fontSize = 10.sp, color = Color(0xFF475569))
                    }

                    Text("⏱ ${scene.duration}", fontSize = 10.sp, color = Color(0xFF94A3B8))
                }
            }

            // Actions: Abrir Button & Menu
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("Abrir", fontSize = 12.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }

                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color(0xFF64748B))
                    }

                    DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = NavyPrimary) },
                            onClick = {
                                menuExpanded = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Duplicar") },
                            leadingIcon = { Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, tint = CyanGlow) },
                            onClick = {
                                menuExpanded = false
                                onDuplicate()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mover para outro projeto") },
                            leadingIcon = { Icon(imageVector = Icons.Default.DriveFileMove, contentDescription = null, tint = AmberGold) },
                            onClick = {
                                menuExpanded = false
                                onMove()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (scene.isArchived) "Restaurar" else "Arquivar") },
                            leadingIcon = { Icon(imageVector = Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFF64748B)) },
                            onClick = {
                                menuExpanded = false
                                onArchive()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Excluir", color = RoseError) },
                            leadingIcon = { Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = RoseError) },
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

@Composable
private fun DialogueRowItem(
    item: SceneDialogueItem,
    onPlayVoice: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Character avatar
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(if (item.character == "António") Color(0xFFEF4444) else Color(0xFFEAB308)),
            contentAlignment = Alignment.Center
        ) {
            Text(item.character.take(1), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        // Speech & Character
        Column(modifier = Modifier.weight(1f)) {
            Text(item.character, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = NavyDark)
            Text("“${item.text}”", fontSize = 10.sp, color = Color(0xFF334155), maxLines = 2, overflow = TextOverflow.Ellipsis)
        }

        // Emotion dropdown pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(item.emotion, fontSize = 9.sp, color = NavyDark, fontWeight = FontWeight.Medium)
        }

        // Speed pill
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(item.speed, fontSize = 9.sp, color = Color(0xFF64748B))
        }

        // Audio play button
        IconButton(
            onClick = onPlayVoice,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Ouvir", tint = NavyPrimary, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun SceneCharacterCard(
    item: SceneCharacterItem,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit
) {
    Card(
        modifier = modifier.height(115.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (item.name == "António") Color(0xFFEF4444) else Color(0xFFEAB308)),
                contentAlignment = Alignment.Center
            ) {
                Text(if (item.name == "António") "🍎" else "🍌", fontSize = 26.sp)
            }

            // Specs
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(item.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                Text("Posição: ${item.position}", fontSize = 10.sp, color = Color(0xFF64748B))
                Text("Expressão: ${item.expression}", fontSize = 10.sp, color = Color(0xFF64748B))
                Text("Ação: ${item.action}", fontSize = 10.sp, color = Color(0xFF64748B))
                Text("Entrada: ${item.entryTime} • Saída: ${item.exitTime}", fontSize = 9.sp, color = Color(0xFF94A3B8))
            }

            IconButton(onClick = onMoreClick, modifier = Modifier.size(24.dp)) {
                Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
private fun SceneInspectorAccordion(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFFF8FAFC))
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = icon, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = NavyDark)
            }
            Icon(
                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(16.dp)
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                HorizontalDivider(color = Color(0xFFE2E8F0))
                Spacer(modifier = Modifier.height(6.dp))
                content()
            }
        }
    }
}

@Composable
private fun TimelineTrackHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(14.dp))
        Text(title, fontSize = 10.sp, color = Color(0xFFE2E8F0), fontWeight = FontWeight.Medium, maxLines = 1)
    }
}

@Composable
private fun TimelineActionBtn(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label, tint = Color(0xFFCBD5E1), modifier = Modifier.size(14.dp))
        Text(label, color = Color(0xFFCBD5E1), fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// 7. SCENE FORM MODAL (CREATE / EDIT)
// -------------------------------------------------------------
@Composable
private fun SceneFormModal(
    initialScene: SceneEntity?,
    projects: List<ProjectEntity>,
    episodes: List<EpisodeEntity>,
    scenarios: List<ScenarioEntity>,
    characters: List<CharacterEntity>,
    onDismiss: () -> Unit,
    onSave: (SceneEntity) -> Unit
) {
    var sceneName by remember { mutableStateOf(initialScene?.name ?: "") }
    var sceneOrder by remember { mutableIntStateOf(initialScene?.sceneOrder ?: 4) }
    var sceneDesc by remember { mutableStateOf(initialScene?.description ?: "") }
    var selectedProjId by remember { mutableStateOf(initialScene?.projectId ?: projects.firstOrNull()?.id ?: 1L) }
    var selectedEpId by remember { mutableStateOf(initialScene?.episodeId ?: episodes.firstOrNull()?.id ?: 1L) }
    var selectedScenarioName by remember { mutableStateOf(initialScene?.scenarioName ?: (scenarios.firstOrNull()?.name ?: "Cozinha da Casa")) }
    var selectedLighting by remember { mutableStateOf(initialScene?.scenarioLighting ?: "Dia") }
    var selectedStatus by remember { mutableStateOf(initialScene?.status ?: "Em produção") }
    var durationText by remember { mutableStateOf(initialScene?.duration ?: "00:00:25") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Movie, contentDescription = null, tint = NavyPrimary)
                Text(if (initialScene == null) "Criar Nova Cena" else "Editar Cena", fontWeight = FontWeight.Bold)
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
                    value = sceneName,
                    onValueChange = { sceneName = it },
                    label = { Text("Nome da Cena *") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = sceneOrder.toString(),
                        onValueChange = { sceneOrder = it.toIntOrNull() ?: 1 },
                        label = { Text("Nº da Cena") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = durationText,
                        onValueChange = { durationText = it },
                        label = { Text("Duração") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = sceneDesc,
                    onValueChange = { sceneDesc = it },
                    label = { Text("Descrição da Cena") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Status:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Rascunho", "Em produção", "Concluída").forEach { st ->
                        FilterChip(
                            selected = selectedStatus == st,
                            onClick = { selectedStatus = st },
                            label = { Text(st, fontSize = 11.sp) }
                        )
                    }
                }

                Text("Iluminação do Cenário:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Row(modifier = Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Dia", "Noite", "Manhã", "Tarde", "Chuva", "Sol", "Neve", "Personalizado").forEach { lt ->
                        FilterChip(
                            selected = selectedLighting == lt,
                            onClick = { selectedLighting = lt },
                            label = { Text(lt, fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (sceneName.isNotBlank()) {
                        val toSave = (initialScene ?: SceneEntity(
                            episodeId = selectedEpId,
                            projectId = selectedProjId,
                            sceneOrder = sceneOrder,
                            name = sceneName
                        )).copy(
                            name = sceneName,
                            sceneOrder = sceneOrder,
                            description = sceneDesc,
                            projectId = selectedProjId,
                            episodeId = selectedEpId,
                            scenarioName = selectedScenarioName,
                            scenarioLighting = selectedLighting,
                            status = selectedStatus,
                            duration = durationText
                        )
                        onSave(toSave)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text("Salvar Cena", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

// Data helper classes for Scene items
data class SceneDialogueItem(
    val character: String,
    val text: String,
    val emotion: String,
    val speed: String,
    val voice: String
)

data class SceneCharacterItem(
    val name: String,
    val position: String,
    val expression: String,
    val action: String,
    val entryTime: String,
    val exitTime: String
)
