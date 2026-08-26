package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.model.ProjectEntity
import com.example.data.model.ScenarioEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Categories & Styles definition
val SCENARIO_CATEGORIES = listOf(
    "Todos",
    "Casas",
    "Escolas",
    "Cidades",
    "Ruas",
    "Lojas",
    "Florestas",
    "Praias",
    "Escritórios",
    "Quartos",
    "Outros"
)

val SCENARIO_TYPES_ALL = listOf(
    "Casa", "Escola", "Quarto", "Sala", "Cozinha", "Mercado", "Loja",
    "Restaurante", "Hospital", "Escritório", "Rua", "Cidade", "Parque",
    "Floresta", "Praia", "Montanha", "Campo", "Fazenda", "Castelo",
    "Espaço", "Mundo fantástico", "Outro"
)

val SCENARIO_STYLES = listOf(
    "Cartoon 3D",
    "2D",
    "3D",
    "Cartoon",
    "Anime",
    "Realista",
    "Estilizado",
    "Infantil",
    "Fantasia",
    "Personalizado"
)

enum class ScenarioViewMode { GRID, LIST }
enum class ScenarioSortMode(val label: String) {
    RECENT("Mais recentes"),
    OLDEST("Mais antigos"),
    NAME_AZ("Nome A–Z"),
    NAME_ZA("Nome Z–A")
}

@Composable
fun ScenariosScreen(
    scenarios: List<ScenarioEntity>,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    showNewScenarioDialog: Boolean,
    editingScenario: ScenarioEntity? = null,
    onOpenNewScenario: (ScenarioEntity?) -> Unit,
    onCloseNewScenario: () -> Unit,
    onSaveScenario: (
        name: String,
        description: String,
        category: String,
        visualStyle: String,
        locationType: String,
        atmosphere: String,
        consistentArchitecture: String,
        versions: String,
        projectId: Long?,
        imageUri: String?
    ) -> Unit,
    onDeleteScenario: (ScenarioEntity) -> Unit,
    onDuplicateScenario: (ScenarioEntity) -> Unit = {},
    onMoveScenario: (ScenarioEntity, Long?) -> Unit = { _, _ -> },
    onArchiveScenario: (ScenarioEntity) -> Unit = {},
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToRangaCreation: () -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var selectedFilterProjectId by remember { mutableStateOf(selectedProjectId) }
    var sortMode by remember { mutableStateOf(ScenarioSortMode.RECENT) }
    var viewMode by remember { mutableStateOf(ScenarioViewMode.GRID) }
    var currentPage by remember { mutableIntStateOf(1) }

    // Dialog & Modal states
    var scenarioToView by remember { mutableStateOf<ScenarioEntity?>(null) }
    var scenarioToDelete by remember { mutableStateOf<ScenarioEntity?>(null) }
    var scenarioToMove by remember { mutableStateOf<ScenarioEntity?>(null) }

    // Quick AI bar states
    var quickAiPrompt by remember { mutableStateOf("") }
    var quickAiStyle by remember { mutableStateOf("Cartoon 3D") }
    var isQuickAiGenerating by remember { mutableStateOf(false) }

    // Quick Reference bar states
    var quickRefPrompt by remember { mutableStateOf("") }
    var quickRefImageUri by remember { mutableStateOf<String?>("scenario_mountain_ref_1787049350992") }
    var isQuickRefGenerating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Sync project filter if passed from parent
    LaunchedEffect(selectedProjectId) {
        if (selectedProjectId != null) {
            selectedFilterProjectId = selectedProjectId
        }
    }

    // Filter & Sort
    val filteredScenarios = scenarios.filter { sc ->
        val matchesCategory = when (selectedCategory) {
            "Todos" -> true
            "Outros" -> sc.category !in listOf("Casas", "Escolas", "Cidades", "Ruas", "Lojas", "Florestas", "Praias", "Escritórios", "Quartos")
            else -> sc.category.equals(selectedCategory, ignoreCase = true) ||
                    (selectedCategory == "Casas" && sc.category.startsWith("Casa", ignoreCase = true)) ||
                    (selectedCategory == "Escolas" && sc.category.startsWith("Escola", ignoreCase = true)) ||
                    (selectedCategory == "Cidades" && sc.category.startsWith("Cidade", ignoreCase = true)) ||
                    (selectedCategory == "Quartos" && sc.category.startsWith("Quarto", ignoreCase = true)) ||
                    (selectedCategory == "Florestas" && sc.category.startsWith("Floresta", ignoreCase = true)) ||
                    (selectedCategory == "Praias" && sc.category.startsWith("Praia", ignoreCase = true)) ||
                    (selectedCategory == "Lojas" && sc.category.startsWith("Loja", ignoreCase = true)) ||
                    (selectedCategory == "Ruas" && sc.category.startsWith("Rua", ignoreCase = true)) ||
                    (selectedCategory == "Escritórios" && sc.category.startsWith("Escritório", ignoreCase = true))
        }

        val matchesProject = selectedFilterProjectId == null || sc.projectId == selectedFilterProjectId
        val matchesSearch = searchQuery.isBlank() ||
                sc.name.contains(searchQuery, ignoreCase = true) ||
                sc.category.contains(searchQuery, ignoreCase = true) ||
                sc.description.contains(searchQuery, ignoreCase = true) ||
                sc.visualStyle.contains(searchQuery, ignoreCase = true)

        matchesCategory && matchesProject && matchesSearch
    }.sortedWith { a, b ->
        when (sortMode) {
            ScenarioSortMode.RECENT -> b.createdAt.compareTo(a.createdAt)
            ScenarioSortMode.OLDEST -> a.createdAt.compareTo(b.createdAt)
            ScenarioSortMode.NAME_AZ -> a.name.compareTo(b.name, ignoreCase = true)
            ScenarioSortMode.NAME_ZA -> b.name.compareTo(a.name, ignoreCase = true)
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        val isWideScreen = maxWidth >= 1100.dp

        Row(modifier = Modifier.fillMaxSize()) {
            // Main content column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // 1. Header with Title, Search, View Mode, Actions
                ScenarioHeaderSection(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    viewMode = viewMode,
                    onViewModeChange = { viewMode = it },
                    onOpenNewScenario = { onOpenNewScenario(null) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                // 2. Filter Category Chips + Project/Sort Dropdowns
                ScenarioFilterRow(
                    selectedCategory = selectedCategory,
                    onCategorySelect = { selectedCategory = it },
                    projects = projects,
                    selectedProjectId = selectedFilterProjectId,
                    onSelectProject = { selectedFilterProjectId = it },
                    sortMode = sortMode,
                    onSortModeSelect = { sortMode = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. Scenarios List or Grid
                if (filteredScenarios.isEmpty()) {
                    EmptyScenarioState(
                        onOpenNewScenario = { onOpenNewScenario(null) }
                    )
                } else {
                    if (viewMode == ScenarioViewMode.GRID) {
                        ScenarioGridSection(
                            scenarios = filteredScenarios,
                            projects = projects,
                            onOpenScenario = { scenarioToView = it },
                            onEditScenario = { onOpenNewScenario(it) },
                            onDuplicateScenario = onDuplicateScenario,
                            onMoveScenario = { scenarioToMove = it },
                            onArchiveScenario = onArchiveScenario,
                            onDeleteScenario = { scenarioToDelete = it }
                        )
                    } else {
                        ScenarioListSection(
                            scenarios = filteredScenarios,
                            projects = projects,
                            onOpenScenario = { scenarioToView = it },
                            onEditScenario = { onOpenNewScenario(it) },
                            onDuplicateScenario = onDuplicateScenario,
                            onMoveScenario = { scenarioToMove = it },
                            onArchiveScenario = onArchiveScenario,
                            onDeleteScenario = { scenarioToDelete = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 4. Pagination
                    ScenarioPaginationRow(
                        currentPage = currentPage,
                        totalPages = 5,
                        onPageSelect = { currentPage = it }
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Bottom AI & Reference Quick Cards
                ScenarioBottomQuickSection(
                    quickAiPrompt = quickAiPrompt,
                    onQuickAiPromptChange = { quickAiPrompt = it },
                    quickAiStyle = quickAiStyle,
                    onQuickAiStyleChange = { quickAiStyle = it },
                    isQuickAiGenerating = isQuickAiGenerating,
                    onGenerateQuickAi = {
                        coroutineScope.launch {
                            isQuickAiGenerating = true
                            delay(1200)
                            isQuickAiGenerating = false
                            onSaveScenario(
                                if (quickAiPrompt.isNotBlank()) quickAiPrompt.take(24) else "Casa de Campo ao Pôr do Sol",
                                if (quickAiPrompt.isNotBlank()) quickAiPrompt else "Casa de campo charmosa com jardim florido durante o entardecer dourado.",
                                "Casas",
                                quickAiStyle,
                                "Exterior / Campo",
                                "Pôr do sol acolhedor",
                                "Construção rústica de madeira com telhas vermelhas",
                                "Dia, Noite, Chuva, Pôr do sol, Vista frontal, Interior",
                                selectedFilterProjectId ?: projects.firstOrNull()?.id,
                                "scenario_sunset_1787049334497"
                            )
                            Toast.makeText(context, "Cenário criado com sucesso via IA!", Toast.LENGTH_SHORT).show()
                            quickAiPrompt = ""
                        }
                    },
                    quickRefPrompt = quickRefPrompt,
                    onQuickRefPromptChange = { quickRefPrompt = it },
                    quickRefImageUri = quickRefImageUri,
                    onQuickRefImageSelect = { quickRefImageUri = it },
                    isQuickRefGenerating = isQuickRefGenerating,
                    onGenerateQuickRef = {
                        coroutineScope.launch {
                            isQuickRefGenerating = true
                            delay(1200)
                            isQuickRefGenerating = false
                            onSaveScenario(
                                if (quickRefPrompt.isNotBlank()) quickRefPrompt.take(24) else "Lago das Montanhas",
                                if (quickRefPrompt.isNotBlank()) quickRefPrompt else "Paisagem montanhosa com lago cristalino adaptada em estilo cartoon 3D.",
                                "Outros",
                                "Cartoon 3D",
                                "Exterior / Montanhas",
                                "Luz do meio-dia cristalina",
                                "Montanhas rochosas com pinheiros e reflexo nas águas",
                                "Dia, Noite, Neve, Manhã, Vista panorâmica",
                                selectedFilterProjectId ?: projects.firstOrNull()?.id,
                                "scenario_mountain_ref_1787049350992"
                            )
                            Toast.makeText(context, "Cenário criado a partir da referência!", Toast.LENGTH_SHORT).show()
                            quickRefPrompt = ""
                        }
                    }
                )
            }

            // Side Panel on Wide Screen
            if (isWideScreen && showNewScenarioDialog) {
                Box(
                    modifier = Modifier
                        .width(420.dp)
                        .fillMaxHeight()
                        .background(Color.White)
                        .border(BorderStroke(1.dp, Color(0xFFE2E8F0)))
                ) {
                    ScenarioSidePanelContent(
                        editingScenario = editingScenario,
                        projects = projects,
                        selectedProjectId = selectedFilterProjectId,
                        onClose = onCloseNewScenario,
                        onSave = { name, desc, cat, style, loc, atmos, arch, vers, projId, imgUri ->
                            onSaveScenario(name, desc, cat, style, loc, atmos, arch, vers, projId, imgUri)
                        }
                    )
                }
            }
        }

        // Dialog Modal on Small/Medium Screen
        if (!isWideScreen && showNewScenarioDialog) {
            Dialog(
                onDismissRequest = onCloseNewScenario,
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 8.dp
                ) {
                    ScenarioSidePanelContent(
                        editingScenario = editingScenario,
                        projects = projects,
                        selectedProjectId = selectedFilterProjectId,
                        onClose = onCloseNewScenario,
                        onSave = { name, desc, cat, style, loc, atmos, arch, vers, projId, imgUri ->
                            onSaveScenario(name, desc, cat, style, loc, atmos, arch, vers, projId, imgUri)
                        }
                    )
                }
            }
        }

        // Scenario Detail Profile Modal ("Abrir")
        if (scenarioToView != null) {
            ScenarioProfileDialog(
                scenario = scenarioToView!!,
                projects = projects,
                onDismiss = { scenarioToView = null },
                onEdit = {
                    val s = scenarioToView
                    scenarioToView = null
                    onOpenNewScenario(s)
                },
                onDuplicate = {
                    onDuplicateScenario(scenarioToView!!)
                    scenarioToView = null
                },
                onDelete = {
                    val s = scenarioToView
                    scenarioToView = null
                    scenarioToDelete = s
                }
            )
        }

        // Delete confirmation dialog
        if (scenarioToDelete != null) {
            AlertDialog(
                onDismissRequest = { scenarioToDelete = null },
                title = {
                    Text("Excluir Cenário", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                },
                text = {
                    Text("Tem certeza que deseja excluir o cenário “${scenarioToDelete!!.name}”? Esta ação não pode ser desfeita.", color = Color(0xFF64748B))
                },
                confirmButton = {
                    Button(
                        onClick = {
                            onDeleteScenario(scenarioToDelete!!)
                            scenarioToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                    ) {
                        Text("Excluir", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { scenarioToDelete = null }) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                }
            )
        }

        // Move to Project Dialog
        if (scenarioToMove != null) {
            MoveScenarioDialog(
                scenario = scenarioToMove!!,
                projects = projects,
                onDismiss = { scenarioToMove = null },
                onConfirm = { newProjId ->
                    onMoveScenario(scenarioToMove!!, newProjId)
                    scenarioToMove = null
                }
            )
        }
    }
}

// -------------------------------------------------------------
// 1. Header Section
// -------------------------------------------------------------
@Composable
fun ScenarioHeaderSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    viewMode: ScenarioViewMode,
    onViewModeChange: (ScenarioViewMode) -> Unit,
    onOpenNewScenario: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title and Subtitle with Icon
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(Color(0xFFEBF2FF), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Landscape,
                    contentDescription = null,
                    tint = Color(0xFF0052FF),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Cenários",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "Crie e organize os lugares onde suas histórias acontecem.",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
            }
        }

        // Right search + view switcher + user actions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Search Box
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Pesquisar cenários...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", tint = Color(0xFF94A3B8), modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF0052FF),
                    unfocusedBorderColor = Color(0xFFE2E8F0)
                ),
                modifier = Modifier
                    .width(260.dp)
                    .height(44.dp)
                    .testTag("search_scenarios_input")
            )

            // View Toggle buttons (Grid / List)
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(8.dp))
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (viewMode == ScenarioViewMode.GRID) Color(0xFF0052FF) else Color.Transparent)
                        .clickable { onViewModeChange(ScenarioViewMode.GRID) }
                        .testTag("view_mode_grid_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = "Visualização em Grade",
                        tint = if (viewMode == ScenarioViewMode.GRID) Color.White else Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (viewMode == ScenarioViewMode.LIST) Color(0xFF0052FF) else Color.Transparent)
                        .clickable { onViewModeChange(ScenarioViewMode.LIST) }
                        .testTag("view_mode_list_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ViewList,
                        contentDescription = "Visualização em Lista",
                        tint = if (viewMode == ScenarioViewMode.LIST) Color.White else Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Notification Bell with Badge
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificações",
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(20.dp)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 4.dp, end = 4.dp)
                        .size(16.dp)
                        .background(Color(0xFF0052FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("3", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            // User Avatar profile pill
            Row(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_augusto),
                    contentDescription = "Usuário Augusto",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Augusto", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A))
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
            }

            // + Novo Cenário Primary Button
            Button(
                onClick = onOpenNewScenario,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                modifier = Modifier
                    .height(44.dp)
                    .testTag("new_scenario_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("+ Novo Cenário", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// -------------------------------------------------------------
// 2. Filter Row: Chips & Dropdowns
// -------------------------------------------------------------
@Composable
fun ScenarioFilterRow(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onSelectProject: (Long?) -> Unit,
    sortMode: ScenarioSortMode,
    onSortModeSelect: (ScenarioSortMode) -> Unit
) {
    var isProjectDropdownExpanded by remember { mutableStateOf(false) }
    var isSortDropdownExpanded by remember { mutableStateOf(false) }
    var isOutrosDropdownExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Chips Scrollable
        Row(
            modifier = Modifier
                .weight(1f)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SCENARIO_CATEGORIES.forEach { category ->
                if (category == "Outros") {
                    Box {
                        val isOutrosActive = selectedCategory == "Outros" || (selectedCategory !in SCENARIO_CATEGORIES && selectedCategory != "Todos")
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isOutrosActive) Color(0xFF0052FF) else Color.White,
                            border = BorderStroke(1.dp, if (isOutrosActive) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .clickable { isOutrosDropdownExpanded = true }
                                .testTag("filter_category_outros")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isOutrosActive && selectedCategory != "Outros") selectedCategory else "Outros",
                                    fontSize = 13.sp,
                                    fontWeight = if (isOutrosActive) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isOutrosActive) Color.White else Color(0xFF475569)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = if (isOutrosActive) Color.White else Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = isOutrosDropdownExpanded,
                            onDismissRequest = { isOutrosDropdownExpanded = false }
                        ) {
                            listOf("Quartos", "Sala", "Cozinha", "Mercado", "Restaurante", "Hospital", "Parque", "Montanha", "Campo", "Fazenda", "Castelo", "Espaço", "Mundo fantástico").forEach { extra ->
                                DropdownMenuItem(
                                    text = { Text(extra, fontSize = 13.sp) },
                                    onClick = {
                                        onCategorySelect(extra)
                                        isOutrosDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                } else {
                    val isSelected = selectedCategory.equals(category, ignoreCase = true)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) Color(0xFF0052FF) else Color.White,
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .clickable { onCategorySelect(category) }
                            .testTag("filter_category_${category.lowercase()}")
                    ) {
                        Text(
                            text = category,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Dropdowns: Projects & Sort
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            // Project Selector Dropdown
            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .clickable { isProjectDropdownExpanded = true }
                        .testTag("filter_project_dropdown")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val currentProjectName = projects.firstOrNull { it.id == selectedProjectId }?.name ?: "Todos os projetos"
                        Text(currentProjectName, fontSize = 13.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    }
                }

                DropdownMenu(
                    expanded = isProjectDropdownExpanded,
                    onDismissRequest = { isProjectDropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos os projetos", fontSize = 13.sp, fontWeight = if (selectedProjectId == null) FontWeight.Bold else FontWeight.Normal) },
                        onClick = {
                            onSelectProject(null)
                            isProjectDropdownExpanded = false
                        }
                    )
                    projects.forEach { proj ->
                        DropdownMenuItem(
                            text = { Text(proj.name, fontSize = 13.sp, fontWeight = if (selectedProjectId == proj.id) FontWeight.Bold else FontWeight.Normal) },
                            onClick = {
                                onSelectProject(proj.id)
                                isProjectDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            // Sort Dropdown
            Box {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier
                        .clickable { isSortDropdownExpanded = true }
                        .testTag("filter_sort_dropdown")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(sortMode.label, fontSize = 13.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
                    }
                }

                DropdownMenu(
                    expanded = isSortDropdownExpanded,
                    onDismissRequest = { isSortDropdownExpanded = false }
                ) {
                    ScenarioSortMode.values().forEach { mode ->
                        DropdownMenuItem(
                            text = { Text(mode.label, fontSize = 13.sp, fontWeight = if (sortMode == mode) FontWeight.Bold else FontWeight.Normal) },
                            onClick = {
                                onSortModeSelect(mode)
                                isSortDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. Grid Section (Matching reference image precisely)
// -------------------------------------------------------------
@Composable
fun ScenarioGridSection(
    scenarios: List<ScenarioEntity>,
    projects: List<ProjectEntity>,
    onOpenScenario: (ScenarioEntity) -> Unit,
    onEditScenario: (ScenarioEntity) -> Unit,
    onDuplicateScenario: (ScenarioEntity) -> Unit,
    onMoveScenario: (ScenarioEntity) -> Unit,
    onArchiveScenario: (ScenarioEntity) -> Unit,
    onDeleteScenario: (ScenarioEntity) -> Unit
) {
    // 3 columns layout on desktop, responsive
    val chunked = scenarios.chunked(3)

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        chunked.forEach { rowScenarios ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                rowScenarios.forEach { scenario ->
                    Box(modifier = Modifier.weight(1f)) {
                        ScenarioGridCard(
                            scenario = scenario,
                            projects = projects,
                            onOpen = { onOpenScenario(scenario) },
                            onEdit = { onEditScenario(scenario) },
                            onDuplicate = { onDuplicateScenario(scenario) },
                            onMove = { onMoveScenario(scenario) },
                            onArchive = { onArchiveScenario(scenario) },
                            onDelete = { onDeleteScenario(scenario) }
                        )
                    }
                }
                // Fill empty slots in row
                for (i in 0 until (3 - rowScenarios.size)) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun ScenarioGridCard(
    scenario: ScenarioEntity,
    projects: List<ProjectEntity>,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onMove: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }
    val projectName = projects.firstOrNull { it.id == scenario.projectId }?.name ?: "Projeto Geral"
    val formattedDate = remember(scenario.createdAt) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(scenario.createdAt))
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("scenario_card_${scenario.id}")
    ) {
        Column {
            // Image Banner with Overflow Menu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                ScenarioImageLoader(
                    imageUri = scenario.imageUri,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // 3-dot overflow menu circular button on top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color.White.copy(alpha = 0.9f), CircleShape)
                            .clickable { isMenuOpen = true }
                            .testTag("scenario_menu_button_${scenario.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Opções",
                            tint = Color(0xFF334155),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { isMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Abrir detalhes", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0052FF)) },
                            onClick = {
                                onOpen()
                                isMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Editar", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0284C7)) },
                            onClick = {
                                onEdit()
                                isMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Duplicar", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B)) },
                            onClick = {
                                onDuplicate()
                                isMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mover para outro projeto", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.DriveFileMove, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFF59E0B)) },
                            onClick = {
                                onMove()
                                isMenuOpen = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (scenario.isArchived) "Desarquivar" else "Arquivar", fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF64748B)) },
                            onClick = {
                                onArchive()
                                isMenuOpen = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Excluir", fontSize = 13.sp, color = Color(0xFFEF4444)) },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFEF4444)) },
                            onClick = {
                                onDelete()
                                isMenuOpen = false
                            }
                        )
                    }
                }
            }

            // Card Body
            Column(modifier = Modifier.padding(14.dp)) {
                // Name + Category Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = scenario.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    // Category Pill Badge
                    ScenarioCategoryPill(category = scenario.category)
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Project Label
                Text(
                    text = "Projeto: $projectName",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Footer: Date + Abrir Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(formattedDate, fontSize = 11.sp, color = Color(0xFF64748B))
                    }

                    OutlinedButton(
                        onClick = onOpen,
                        shape = RoundedCornerShape(6.dp),
                        border = BorderStroke(1.dp, Color(0xFF0052FF)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .testTag("open_scenario_button_${scenario.id}")
                    ) {
                        Text(
                            text = "Abrir",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0052FF)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Category Badge with Custom Colors
// -------------------------------------------------------------
@Composable
fun ScenarioCategoryPill(category: String) {
    val (bg, textColor) = when (category.lowercase()) {
        "casas", "casa" -> Color(0xFFFEE2E2) to Color(0xFFDC2626) // Pink/Peach
        "escolas", "escola" -> Color(0xFFE0F2FE) to Color(0xFF0284C7) // Light Blue
        "cidades", "cidade" -> Color(0xFFDCFCE7) to Color(0xFF16A34A) // Light Green
        "quartos", "quarto" -> Color(0xFFF3E8FF) to Color(0xFF9333EA) // Lavender
        "praias", "praia" -> Color(0xFFFEF3C7) to Color(0xFFD97706) // Orange/Amber
        "florestas", "floresta" -> Color(0xFFD1FAE5) to Color(0xFF059669) // Emerald
        "lojas", "loja", "mercado" -> Color(0xFFFFEDD5) to Color(0xFFEA580C)
        "ruas", "rua" -> Color(0xFFCFFAFE) to Color(0xFF0891B2)
        "escritórios", "escritório" -> Color(0xFFF1F5F9) to Color(0xFF475569)
        else -> Color(0xFFE0E7FF) to Color(0xFF4F46E5)
    }

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = bg
    ) {
        Text(
            text = category,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

// -------------------------------------------------------------
// List View Section
// -------------------------------------------------------------
@Composable
fun ScenarioListSection(
    scenarios: List<ScenarioEntity>,
    projects: List<ProjectEntity>,
    onOpenScenario: (ScenarioEntity) -> Unit,
    onEditScenario: (ScenarioEntity) -> Unit,
    onDuplicateScenario: (ScenarioEntity) -> Unit,
    onMoveScenario: (ScenarioEntity) -> Unit,
    onArchiveScenario: (ScenarioEntity) -> Unit,
    onDeleteScenario: (ScenarioEntity) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        scenarios.forEach { scenario ->
            val projectName = projects.firstOrNull { it.id == scenario.projectId }?.name ?: "Projeto Geral"
            val formattedDate = remember(scenario.createdAt) {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(scenario.createdAt))
            }
            var isMenuOpen by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Thumbnail
                    Box(
                        modifier = Modifier
                            .size(72.dp, 52.dp)
                            .clip(RoundedCornerShape(6.dp))
                    ) {
                        ScenarioImageLoader(
                            imageUri = scenario.imageUri,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Title + Category + Project
                    Column(modifier = Modifier.weight(1.5f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = scenario.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            ScenarioCategoryPill(category = scenario.category)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Projeto: $projectName",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    // Style
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Estilo", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text(scenario.visualStyle, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                    }

                    // Date
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Data", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        Text(formattedDate, fontSize = 12.sp, color = Color(0xFF334155))
                    }

                    // Actions
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = { onOpenScenario(scenario) },
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFF0052FF)),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Abrir", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0052FF))
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Box {
                            IconButton(onClick = { isMenuOpen = true }) {
                                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color(0xFF64748B))
                            }
                            DropdownMenu(
                                expanded = isMenuOpen,
                                onDismissRequest = { isMenuOpen = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Editar", fontSize = 13.sp) },
                                    leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        onEditScenario(scenario)
                                        isMenuOpen = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Duplicar", fontSize = 13.sp) },
                                    leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        onDuplicateScenario(scenario)
                                        isMenuOpen = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Mover de projeto", fontSize = 13.sp) },
                                    leadingIcon = { Icon(Icons.Default.DriveFileMove, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                    onClick = {
                                        onMoveScenario(scenario)
                                        isMenuOpen = false
                                    }
                                )
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Excluir", fontSize = 13.sp, color = Color(0xFFEF4444)) },
                                    leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFEF4444)) },
                                    onClick = {
                                        onDeleteScenario(scenario)
                                        isMenuOpen = false
                                    }
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
// 4. Pagination Row
// -------------------------------------------------------------
@Composable
fun ScenarioPaginationRow(
    currentPage: Int,
    totalPages: Int,
    onPageSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Prev button
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(6.dp))
                .clickable { if (currentPage > 1) onPageSelect(currentPage - 1) },
            contentAlignment = Alignment.Center
        ) {
            Text("<", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.width(6.dp))

        (1..totalPages).forEach { page ->
            val isSelected = page == currentPage
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) Color(0xFF0052FF) else Color.White)
                    .border(BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)), RoundedCornerShape(6.dp))
                    .clickable { onPageSelect(page) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    fontSize = 12.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else Color(0xFF334155)
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        // Next button
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color.White, RoundedCornerShape(6.dp))
                .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(6.dp))
                .clickable { if (currentPage < totalPages) onPageSelect(currentPage + 1) },
            contentAlignment = Alignment.Center
        ) {
            Text(">", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Bold)
        }
    }
}

// -------------------------------------------------------------
// 5. Bottom Quick AI & Reference Cards (as in reference image)
// -------------------------------------------------------------
@Composable
fun ScenarioBottomQuickSection(
    quickAiPrompt: String,
    onQuickAiPromptChange: (String) -> Unit,
    quickAiStyle: String,
    onQuickAiStyleChange: (String) -> Unit,
    isQuickAiGenerating: Boolean,
    onGenerateQuickAi: () -> Unit,
    quickRefPrompt: String,
    onQuickRefPromptChange: (String) -> Unit,
    quickRefImageUri: String?,
    onQuickRefImageSelect: (String) -> Unit,
    isQuickRefGenerating: Boolean,
    onGenerateQuickRef: () -> Unit
) {
    var isStyleDropdownOpen by remember { mutableStateOf(false) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onQuickRefImageSelect(it.toString()) }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Card 1: Criar cenário com IA
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Criar cenário com IA",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0052FF)
                    )
                }
                Text(
                    text = "Descreva o cenário que você deseja criar.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left Input + Dropdown + Button
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = quickAiPrompt,
                            onValueChange = { if (it.length <= 500) onQuickAiPromptChange(it) },
                            placeholder = {
                                Text(
                                    "Ex: Crie uma casa de campo linda, com jardim, estilo cartoon 3D, durante o pôr do sol.",
                                    fontSize = 12.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )

                        Text(
                            text = "${quickAiPrompt.length}/500",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Style dropdown
                            Box {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFFF8FAFC),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.clickable { isStyleDropdownOpen = true }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Estilo: $quickAiStyle", fontSize = 11.sp, color = Color(0xFF334155))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(14.dp))
                                    }
                                }

                                DropdownMenu(
                                    expanded = isStyleDropdownOpen,
                                    onDismissRequest = { isStyleDropdownOpen = false }
                                ) {
                                    SCENARIO_STYLES.forEach { style ->
                                        DropdownMenuItem(
                                            text = { Text(style, fontSize = 12.sp) },
                                            onClick = {
                                                onQuickAiStyleChange(style)
                                                isStyleDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Generate Button
                            Button(
                                onClick = onGenerateQuickAi,
                                enabled = !isQuickAiGenerating,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                if (isQuickAiGenerating) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Gerar cenário", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Right Thumbnail Image Preview
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(134.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(8.dp))
                    ) {
                        ScenarioImageLoader(
                            imageUri = "scenario_sunset_1787049334497",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // Card 2: Imagem de referência
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.weight(1f)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Imagem de referência",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0052FF)
                    )
                }
                Text(
                    text = "Carregue uma imagem e transforme em um cenário.",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    // Left Dropzone + Input + Button
                    Column(modifier = Modifier.weight(1f)) {
                        // Dropzone Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(8.dp))
                                .clickable { galleryLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Clique para carregar ou arraste a imagem", fontSize = 11.sp, fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                                    Text("PNG, JPG ou WEBP (máx. 10MB)", fontSize = 9.sp, color = Color(0xFF94A3B8))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = quickRefPrompt,
                            onValueChange = { if (it.length <= 300) onQuickRefPromptChange(it) },
                            placeholder = {
                                Text(
                                    "Ex: Transforme esta imagem em um cenário cartoon 3D",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC),
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${quickRefPrompt.length}/300",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )

                            Button(
                                onClick = onGenerateQuickRef,
                                enabled = !isQuickRefGenerating,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                if (isQuickRefGenerating) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Criar com referência", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Right Reference Preview Photo
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(134.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(8.dp))
                    ) {
                        ScenarioImageLoader(
                            imageUri = quickRefImageUri ?: "scenario_mountain_ref_1787049350992",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 6. Side Panel / Modal for Creating New Scenario (with Tabs)
// -------------------------------------------------------------
@Composable
fun ScenarioSidePanelContent(
    editingScenario: ScenarioEntity?,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onClose: () -> Unit,
    onSave: (
        name: String,
        desc: String,
        cat: String,
        style: String,
        loc: String,
        atmos: String,
        arch: String,
        vers: String,
        projId: Long?,
        imgUri: String?
    ) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Manual, 1: IA, 2: Referência
    val tabs = listOf("Manual", "IA", "Referência")

    // Form fields
    var name by remember { mutableStateOf(editingScenario?.name ?: "") }
    var description by remember { mutableStateOf(editingScenario?.description ?: "") }
    var category by remember { mutableStateOf(editingScenario?.category ?: "Casa") }
    var visualStyle by remember { mutableStateOf(editingScenario?.visualStyle ?: "Cartoon 3D") }
    var locationType by remember { mutableStateOf(editingScenario?.locationType ?: "Exterior") }
    var atmosphere by remember { mutableStateOf(editingScenario?.atmosphere ?: "Iluminação acolhedora") }
    var consistentArch by remember { mutableStateOf(editingScenario?.consistentArchitecture ?: "Estilo 3D cartoon coerente com cores quentes") }
    var versions by remember { mutableStateOf(editingScenario?.versions ?: "Dia, Noite, Chuva, Pôr do sol, Vista frontal, Interior") }
    var projectId by remember { mutableStateOf(editingScenario?.projectId ?: selectedProjectId ?: projects.firstOrNull()?.id) }
    var imageUri by remember { mutableStateOf(editingScenario?.imageUri ?: "scenario_family_house_1787049241112") }

    // IA Generation tab states
    var aiPrompt by remember { mutableStateOf("Crie uma pequena casa de família em uma cidade, estilo cartoon 3D, durante o pôr do sol.") }
    var isAiGenerating by remember { mutableStateOf(false) }
    var generatedImageUri by remember { mutableStateOf<String?>("scenario_family_house_1787049241112") }

    // Reference tab states
    var refPrompt by remember { mutableStateOf("Transforme esta imagem em um cenário cartoon 3D.") }
    var refImageUri by remember { mutableStateOf<String?>("scenario_mountain_ref_1787049350992") }
    var isRefGenerating by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { imageUri = it.toString() }
    }
    val refGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { refImageUri = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with Close Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (editingScenario != null) "Editar cenário" else "Criar novo cenário",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Tabs: Manual / IA / Referência
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color(0xFFF1F5F9),
            contentColor = Color(0xFF0052FF),
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = Color(0xFF0052FF),
                    height = 2.dp
                )
            },
            modifier = Modifier.clip(RoundedCornerShape(8.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            when (index) {
                                0 -> Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                1 -> Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                                2 -> Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(14.dp))
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(title, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Tab Content
        when (selectedTab) {
            // TAB 0: MANUAL
            0 -> {
                // Nome do cenário *
                Text("Nome do cenário *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Ex: Casa da Família", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Descrição
                Text("Descrição", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Descreva o cenário...", fontSize = 13.sp, color = Color(0xFF94A3B8)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tipo de cenário *
                var isTypeDropdownOpen by remember { mutableStateOf(false) }
                Text("Tipo de cenário *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isTypeDropdownOpen = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isTypeDropdownOpen = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0052FF),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                    DropdownMenu(
                        expanded = isTypeDropdownOpen,
                        onDismissRequest = { isTypeDropdownOpen = false }
                    ) {
                        SCENARIO_TYPES_ALL.forEach { t ->
                            DropdownMenuItem(
                                text = { Text(t, fontSize = 13.sp) },
                                onClick = {
                                    category = t
                                    isTypeDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Projeto *
                var isProjectDropdownOpen by remember { mutableStateOf(false) }
                Text("Projeto *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    val currentProjName = projects.firstOrNull { it.id == projectId }?.name ?: "Selecione o projeto"
                    OutlinedTextField(
                        value = currentProjName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isProjectDropdownOpen = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isProjectDropdownOpen = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0052FF),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                    DropdownMenu(
                        expanded = isProjectDropdownOpen,
                        onDismissRequest = { isProjectDropdownOpen = false }
                    ) {
                        projects.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name, fontSize = 13.sp) },
                                onClick = {
                                    projectId = p.id
                                    isProjectDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Estilo visual
                var isStyleDropdownOpen by remember { mutableStateOf(false) }
                Text("Estilo visual", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                Box {
                    OutlinedTextField(
                        value = visualStyle,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { isStyleDropdownOpen = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isStyleDropdownOpen = true },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0052FF),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        )
                    )
                    DropdownMenu(
                        expanded = isStyleDropdownOpen,
                        onDismissRequest = { isStyleDropdownOpen = false }
                    ) {
                        SCENARIO_STYLES.forEach { st ->
                            DropdownMenuItem(
                                text = { Text(st, fontSize = 13.sp) },
                                onClick = {
                                    visualStyle = st
                                    isStyleDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Imagem do cenário
                Text("Imagem do cenário", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(8.dp))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Clique para carregar ou arraste a imagem", fontSize = 12.sp, color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                        Text("PNG, JPG ou WEBP (máx. 10MB)", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Save button
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            onSave(name, description, category, visualStyle, locationType, atmosphere, consistentArch, versions, projectId, imageUri)
                            onClose()
                        }
                    },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salvar Cenário", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // TAB 1: CRIAR CENÁRIO COM IA
            1 -> {
                Text("Descreva o cenário para a IA", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = aiPrompt,
                    onValueChange = { aiPrompt = it },
                    placeholder = { Text("Ex: Crie uma pequena casa de família em uma cidade, estilo cartoon 3D, durante o pôr do sol.", fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isAiGenerating = true
                            delay(1200)
                            isAiGenerating = false
                            generatedImageUri = "scenario_family_house_1787049241112"
                        }
                    },
                    enabled = !isAiGenerating,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isAiGenerating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("✨ Criar cenário com IA", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                if (generatedImageUri != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Resultado gerado:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        ScenarioImageLoader(
                            imageUri = generatedImageUri,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Actions after AI Generation: Gerar novamente, Salvar, Adicionar ao projeto
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                coroutineScope.launch {
                                    isAiGenerating = true
                                    delay(1000)
                                    isAiGenerating = false
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("🔄 Novamente", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                onSave(
                                    aiPrompt.take(24).ifBlank { "Novo Cenário IA" },
                                    aiPrompt,
                                    "Casa",
                                    "Cartoon 3D",
                                    "Exterior",
                                    "Pôr do sol",
                                    "Arquitetura consistente",
                                    "Dia, Noite, Chuva, Pôr do sol",
                                    projectId,
                                    generatedImageUri
                                )
                                onClose()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("💾 Salvar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // TAB 2: IMAGEM DE REFERÊNCIA
            2 -> {
                Text("Imagem de referência", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(Color(0xFFF8FAFC), RoundedCornerShape(8.dp))
                        .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(8.dp))
                        .clickable { refGalleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Carregue uma imagem de referência", fontSize = 12.sp, color = Color(0xFF334155))
                        Text("PNG, JPG ou WEBP", fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Instruções para a IA", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = refPrompt,
                    onValueChange = { refPrompt = it },
                    placeholder = { Text("Ex: Transforme esta imagem em um cenário cartoon 3D.", fontSize = 12.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isRefGenerating = true
                            delay(1200)
                            isRefGenerating = false
                            onSave(
                                "Cenário com Referência",
                                refPrompt,
                                "Montanha",
                                "Cartoon 3D",
                                "Exterior / Natureza",
                                "Luz equilibrada",
                                "Preserva relevo e cores da imagem de referência",
                                "Dia, Noite, Chuva, Pôr do sol",
                                projectId,
                                "scenario_mountain_ref_1787049350992"
                            )
                            onClose()
                        }
                    },
                    enabled = !isRefGenerating,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isRefGenerating) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Criar com referência", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 7. Scenario Profile Modal ("Abrir") with Consistency & Versions
// -------------------------------------------------------------
@Composable
fun ScenarioProfileDialog(
    scenario: ScenarioEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    val projectName = projects.firstOrNull { it.id == scenario.projectId }?.name ?: "Projeto Geral"
    val formattedDate = remember(scenario.createdAt) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(scenario.createdAt))
    }

    val scenarioVersions = listOf(
        "☀️ Dia",
        "🌙 Noite",
        "🌅 Manhã",
        "🌇 Tarde",
        "🌧️ Chuva",
        "❄️ Neve",
        "🏛️ Vista frontal",
        "📐 Vista lateral",
        "🏠 Interior",
        "🎉 Festa",
        "🎄 Natal"
    )

    var selectedVersion by remember { mutableStateOf("☀️ Dia") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = scenario.name,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            ScenarioCategoryPill(category = scenario.category)
                        }
                        Text(
                            text = "Projeto: $projectName • Criado em $formattedDate",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Large Image View
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(BorderStroke(1.dp, Color(0xFFE2E8F0)), RoundedCornerShape(12.dp))
                ) {
                    ScenarioImageLoader(
                        imageUri = scenario.imageUri,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Active version badge on image
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.65f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Versão ativa: $selectedVersion",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // CONSISTÊNCIA & VERSÕES DO CENÁRIO
                Text(
                    text = "Consistência & Versões do Cenário",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Text(
                    text = "O mesmo cenário mantém arquitetura, cores e objetos com variações de clima e iluminação:",
                    fontSize = 12.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    scenarioVersions.forEach { version ->
                        val isSelected = version == selectedVersion
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.clickable { selectedVersion = version }
                        ) {
                            Text(
                                text = version,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFF334155),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Metadata cards: Descrição, Arquitetura, Cenas & Episódios
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Descrição", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(scenario.description.ifBlank { "Sem descrição adicional." }, fontSize = 12.sp, color = Color(0xFF475569))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text("Estilo Visual", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(scenario.visualStyle, fontSize = 12.sp, color = Color(0xFF475569))
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Aparições na Produção", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Folder, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Cenas vinculadas: ${scenario.scenesCount} cenas", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Landscape, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Episódios que utilizam: ${scenario.episodesCount} episódios", fontSize = 12.sp, color = Color(0xFF334155))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                        border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Excluir cenário", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = onDuplicate,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1))
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Duplicar", fontSize = 12.sp, color = Color(0xFF334155))
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Button(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar cenário", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 8. Empty State
// -------------------------------------------------------------
@Composable
fun EmptyScenarioState(onOpenNewScenario: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(Color(0xFFEBF2FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Landscape,
                contentDescription = null,
                tint = Color(0xFF0052FF),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Você ainda não tem cenários",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Crie os lugares onde suas histórias vão acontecer.",
            fontSize = 13.sp,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onOpenNewScenario,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF))
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("+ Criar primeiro cenário", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

// -------------------------------------------------------------
// 9. Move Scenario Dialog
// -------------------------------------------------------------
@Composable
fun MoveScenarioDialog(
    scenario: ScenarioEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onConfirm: (Long?) -> Unit
) {
    var selectedProjId by remember { mutableStateOf(scenario.projectId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Mover Cenário para outro Projeto", fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
        },
        text = {
            Column {
                Text(
                    "Selecione o projeto de destino para “${scenario.name}”:",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )
                Spacer(modifier = Modifier.height(14.dp))
                projects.forEach { proj ->
                    val isSelected = proj.id == selectedProjId
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFFEBF2FF) else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedProjId = proj.id }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Folder,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF0052FF) else Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = proj.name,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) Color(0xFF0052FF) else Color(0xFF0F172A)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedProjId) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF))
            ) {
                Text("Confirmar", color = Color.White)
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
// Image Loader Helper for Local Drawables & URIs
// -------------------------------------------------------------
@Composable
fun ScenarioImageLoader(
    imageUri: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current

    if (imageUri.isNullOrBlank()) {
        Box(
            modifier = modifier.background(Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Landscape, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(36.dp))
        }
        return
    }

    val resId = remember(imageUri) {
        val cleanName = imageUri.removePrefix("drawable/").removePrefix("@drawable/")
        context.resources.getIdentifier(cleanName, "drawable", context.packageName)
    }

    if (resId != 0) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUri)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )
    }
}
