package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.material.icons.filled.Animation
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.ProjectEntity

enum class ViewMode {
    GRID, LIST
}

enum class SortOption(val label: String) {
    NEWEST("Mais recentes"),
    OLDEST("Mais antigos"),
    NAME_AZ("Nome A–Z"),
    NAME_ZA("Nome Z–A")
}

@Composable
fun ProjectsScreen(
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    showNewProjectDialog: Boolean,
    editingProject: ProjectEntity?,
    onOpenNewProject: (ProjectEntity?) -> Unit,
    onCloseNewProject: () -> Unit,
    onSaveProject: (String, String, String, String, String?, String) -> Unit,
    onDeleteProject: (ProjectEntity) -> Unit,
    onDuplicateProject: (ProjectEntity) -> Unit,
    onArchiveProject: (ProjectEntity) -> Unit,
    onSelectProject: (Long) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    var currentSort by remember { mutableStateOf(SortOption.NEWEST) }
    var isSortMenuOpen by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }
    var currentPage by remember { mutableIntStateOf(1) }

    // Filter & Sort Logic
    val filteredProjects = remember(projects, searchQuery, selectedFilter, currentSort) {
        var list = projects.filter { project ->
            val matchesSearch = searchQuery.isBlank() ||
                    project.name.contains(searchQuery, ignoreCase = true) ||
                    project.description.contains(searchQuery, ignoreCase = true) ||
                    project.category.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Todos" -> true
                "Novelas" -> project.type.contains("Novela", ignoreCase = true)
                "Séries" -> project.type.contains("Série", ignoreCase = true)
                "Desenhos animados" -> project.type.contains("Desenho", ignoreCase = true)
                "Filmes" -> project.type.contains("Filme", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesFilter
        }

        list = when (currentSort) {
            SortOption.NEWEST -> list.sortedByDescending { it.updatedAt }
            SortOption.OLDEST -> list.sortedBy { it.updatedAt }
            SortOption.NAME_AZ -> list.sortedBy { it.name.lowercase() }
            SortOption.NAME_ZA -> list.sortedByDescending { it.name.lowercase() }
        }

        list
    }

    val pageSize = 8
    val totalPages = maxOf(1, (filteredProjects.size + pageSize - 1) / pageSize)
    val paginatedProjects = remember(filteredProjects, currentPage, totalPages) {
        val safePage = currentPage.coerceIn(1, totalPages)
        val fromIndex = (safePage - 1) * pageSize
        filteredProjects.drop(fromIndex).take(pageSize)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        val isWide = maxWidth >= 840.dp
        val gridColumns = when {
            maxWidth >= 1200.dp -> 4
            maxWidth >= 840.dp -> 3
            maxWidth >= 550.dp -> 2
            else -> 1
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isWide) 24.dp else 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Page Header (Title + Subtitle + "+ Novo Projeto" button)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Projetos",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Gerencie suas novelas, séries, desenhos animados e filmes.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { onOpenNewProject(null) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("projects_new_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Novo Projeto",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // 2. Search, Filters, Sort and View Switcher Row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Search & View Controls Bar
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Field
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                currentPage = 1
                            },
                            placeholder = {
                                Text(
                                    "Pesquisar projetos...",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF94A3B8),
                                        fontSize = 13.sp
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Buscar",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFCBD5E1),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("projects_search_field")
                        )

                        // Sort Menu Dropdown Button
                        Box {
                            OutlinedButton(
                                onClick = { isSortMenuOpen = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF0F172A)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.height(44.dp)
                            ) {
                                Text(
                                    text = currentSort.label,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.5.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("▾", fontSize = 12.sp, color = Color(0xFF64748B))
                            }

                            DropdownMenu(
                                expanded = isSortMenuOpen,
                                onDismissRequest = { isSortMenuOpen = false }
                            ) {
                                SortOption.values().forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option.label) },
                                        onClick = {
                                            currentSort = option
                                            isSortMenuOpen = false
                                        }
                                    )
                                }
                            }
                        }

                        // View Mode Switcher (Grid / List)
                        Row(
                            modifier = Modifier
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                .padding(3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (viewMode == ViewMode.GRID) Color(0xFF0052FF) else Color.Transparent)
                                    .clickable { viewMode = ViewMode.GRID },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = "Grade",
                                    tint = if (viewMode == ViewMode.GRID) Color.White else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (viewMode == ViewMode.LIST) Color(0xFF0052FF) else Color.Transparent)
                                    .clickable { viewMode = ViewMode.LIST },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ViewList,
                                    contentDescription = "Lista",
                                    tint = if (viewMode == ViewMode.LIST) Color.White else Color(0xFF64748B),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Filter Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val filters = listOf("Todos", "Novelas", "Séries", "Desenhos animados", "Filmes")
                        filters.forEach { filter ->
                            val isSelected = selectedFilter == filter
                            Surface(
                                onClick = {
                                    selectedFilter = filter
                                    currentPage = 1
                                },
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Color(0xFF0052FF) else Color.White,
                                border = if (isSelected) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.testTag("filter_chip_$filter")
                            ) {
                                Text(
                                    text = filter,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF475569),
                                        fontSize = 12.5.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Projects Content (Empty State, Grid View, or List View)
            if (filteredProjects.isEmpty()) {
                item {
                    EmptyProjectsView(
                        onNewProjectClick = { onOpenNewProject(null) }
                    )
                }
            } else {
                if (viewMode == ViewMode.GRID) {
                    // Responsive Grid using chunked Rows
                    val rows = paginatedProjects.chunked(gridColumns)
                    rows.forEach { rowItems ->
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                rowItems.forEach { project ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        ProjectGridCard(
                                            project = project,
                                            onOpen = { onSelectProject(project.id) },
                                            onEdit = { onOpenNewProject(project) },
                                            onDuplicate = { onDuplicateProject(project) },
                                            onArchive = { onArchiveProject(project) },
                                            onDelete = { onDeleteProject(project) }
                                        )
                                    }
                                }
                                // Fill remainder of row with empty spaces if last row has fewer items
                                val emptySlots = gridColumns - rowItems.size
                                repeat(emptySlots) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } else {
                    // List View
                    items(paginatedProjects, key = { it.id }) { project ->
                        ProjectListRow(
                            project = project,
                            onOpen = { onSelectProject(project.id) },
                            onEdit = { onOpenNewProject(project) },
                            onDuplicate = { onDuplicateProject(project) },
                            onArchive = { onArchiveProject(project) },
                            onDelete = { onDeleteProject(project) }
                        )
                    }
                }

                // 4. Pagination Controls Bar
                item {
                    PaginationBar(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        onPageSelect = { currentPage = it }
                    )
                }
            }
        }

        // 5. Create / Edit Project Modal Dialog
        if (showNewProjectDialog) {
            ProjectFormDialog(
                editingProject = editingProject,
                onDismiss = onCloseNewProject,
                onSave = onSaveProject,
                onAiSuggest = onOpenAiWithPrompt
            )
        }
    }
}

@Composable
fun ProjectGridCard(
    project: ProjectEntity,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    val coverRes = when (project.coverUri) {
        "cover_frutinhas" -> R.drawable.cover_frutinhas
        "cover_herois_escola" -> R.drawable.cover_herois_escola
        "cover_misterio_cidade" -> R.drawable.cover_misterio_cidade
        "cover_reino_encantado" -> R.drawable.cover_reino_encantado
        "cover_missao_estelar" -> R.drawable.cover_missao_estelar
        "cover_fazenda_divertida" -> R.drawable.cover_fazenda_divertida
        "cover_piratas_kids" -> R.drawable.cover_piratas_kids
        "cover_mundo_dinossauros" -> R.drawable.cover_mundo_dinossauros
        else -> when (project.id % 8L) {
            1L -> R.drawable.cover_frutinhas
            2L -> R.drawable.cover_herois_escola
            3L -> R.drawable.cover_misterio_cidade
            4L -> R.drawable.cover_reino_encantado
            5L -> R.drawable.cover_missao_estelar
            6L -> R.drawable.cover_fazenda_divertida
            7L -> R.drawable.cover_piratas_kids
            else -> R.drawable.cover_mundo_dinossauros
        }
    }

    val typeBg = when (project.type) {
        "Desenho", "Desenho animado", "Desenho Animado" -> Color(0xFFF3E8FF)
        "Filme" -> Color(0xFFFAF5FF)
        "Série" -> Color(0xFFEFF6FF)
        else -> Color(0xFFF5F3FF)
    }

    val typeTextColor = when (project.type) {
        "Desenho", "Desenho animado", "Desenho Animado" -> Color(0xFF7C3AED)
        "Filme" -> Color(0xFF9333EA)
        "Série" -> Color(0xFF2563EB)
        else -> Color(0xFF6D28D9)
    }

    val (statusBg, statusTextColor) = when (project.status) {
        "Rascunho" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Concluído" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "Arquivado" -> Color(0xFFF1F5F9) to Color(0xFF64748B)
        else -> Color(0xFFDBEAFE) to Color(0xFF2563EB) // Em produção
    }

    val relativeTime = when (project.id % 8L) {
        1L -> "Editado há 2 horas"
        2L -> "Editado há 1 dia"
        3L -> "Editado há 2 dias"
        4L -> "Editado há 3 dias"
        5L -> "Editado há 5 dias"
        6L -> "Editado há 6 dias"
        7L -> "Editado há 1 semana"
        else -> "Editado há 1 semana"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_card_${project.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image Cover with 3-dots Menu Button Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(Color(0xFF0F172A))
            ) {
                Image(
                    painter = painterResource(id = coverRes),
                    contentDescription = project.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top right 3-dots button
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .clickable { isMenuOpen = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = isMenuOpen,
                        onDismissRequest = { isMenuOpen = false }
                    ) {
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(18.dp)) },
                            text = { Text("Editar") },
                            onClick = {
                                isMenuOpen = false
                                onEdit()
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp)) },
                            text = { Text("Duplicar") },
                            onClick = {
                                isMenuOpen = false
                                onDuplicate()
                            }
                        )
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Archive, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp)) },
                            text = { Text(if (project.status == "Arquivado") "Desarquivar" else "Arquivar") },
                            onClick = {
                                isMenuOpen = false
                                onArchive()
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp)) },
                            text = { Text("Excluir", color = Color(0xFFDC2626)) },
                            onClick = {
                                isMenuOpen = false
                                onDelete()
                            }
                        )
                    }
                }
            }

            // Card Body
            Column(
                modifier = Modifier.padding(14.dp)
            ) {
                Text(
                    text = project.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Type Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = project.type,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = typeTextColor,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(34.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Footer Row: Edited time + Status badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🕒 $relativeTime",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(statusBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.status,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = statusTextColor,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons: "Abrir" + secondary menu
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = onOpen,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = Color(0xFF0052FF)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(34.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Abrir",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp
                        )
                    }

                    Box {
                        OutlinedButton(
                            onClick = { isMenuOpen = true },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                            modifier = Modifier
                                .size(34.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Mais opções",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectListRow(
    project: ProjectEntity,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    val coverRes = when (project.coverUri) {
        "cover_frutinhas" -> R.drawable.cover_frutinhas
        "cover_herois_escola" -> R.drawable.cover_herois_escola
        "cover_misterio_cidade" -> R.drawable.cover_misterio_cidade
        "cover_reino_encantado" -> R.drawable.cover_reino_encantado
        "cover_missao_estelar" -> R.drawable.cover_missao_estelar
        "cover_fazenda_divertida" -> R.drawable.cover_fazenda_divertida
        "cover_piratas_kids" -> R.drawable.cover_piratas_kids
        "cover_mundo_dinossauros" -> R.drawable.cover_mundo_dinossauros
        else -> R.drawable.cover_frutinhas
    }

    val (statusBg, statusTextColor) = when (project.status) {
        "Rascunho" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Concluído" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
        "Arquivado" -> Color(0xFFF1F5F9) to Color(0xFF64748B)
        else -> Color(0xFFDBEAFE) to Color(0xFF2563EB)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("project_list_row_${project.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Image(
                painter = painterResource(id = coverRes),
                contentDescription = project.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 14.sp
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = project.type,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 11.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Status Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusBg)
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = project.status,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = statusTextColor,
                        fontSize = 10.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Action Buttons
            Button(
                onClick = onOpen,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF6FF),
                    contentColor = Color(0xFF0052FF)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(34.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("Abrir", fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }

            Box {
                IconButton(onClick = { isMenuOpen = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opções",
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = isMenuOpen,
                    onDismissRequest = { isMenuOpen = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(18.dp)) },
                        text = { Text("Editar") },
                        onClick = {
                            isMenuOpen = false
                            onEdit()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(18.dp)) },
                        text = { Text("Duplicar") },
                        onClick = {
                            isMenuOpen = false
                            onDuplicate()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Archive, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp)) },
                        text = { Text(if (project.status == "Arquivado") "Desarquivar" else "Arquivar") },
                        onClick = {
                            isMenuOpen = false
                            onArchive()
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp)) },
                        text = { Text("Excluir", color = Color(0xFFDC2626)) },
                        onClick = {
                            isMenuOpen = false
                            onDelete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProjectsView(
    onNewProjectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .testTag("projects_empty_view"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFF6FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Folder,
                    contentDescription = null,
                    tint = Color(0xFF0052FF),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Você ainda não tem projetos",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Comece criando sua primeira novela, série, desenho animado ou filme.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onNewProjectClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ Criar primeiro projeto",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun PaginationBar(
    currentPage: Int,
    totalPages: Int,
    onPageSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Previous page
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                .clickable(enabled = currentPage > 1) { onPageSelect(currentPage - 1) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Anterior",
                tint = if (currentPage > 1) Color(0xFF0F172A) else Color(0xFFCBD5E1),
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Page Numbers
        val pagesToShow = (1..minOf(totalPages, 3)).toList()
        pagesToShow.forEach { page ->
            val isSelected = currentPage == page
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isSelected) Color(0xFF0052FF) else Color.White)
                    .border(
                        1.dp,
                        if (isSelected) Color.Transparent else Color(0xFFE2E8F0),
                        RoundedCornerShape(6.dp)
                    )
                    .clickable { onPageSelect(page) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else Color(0xFF0F172A),
                        fontSize = 12.sp
                    )
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
        }

        if (totalPages > 3) {
            Text("...", color = Color(0xFF94A3B8), fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (currentPage == totalPages) Color(0xFF0052FF) else Color.White)
                    .border(
                        1.dp,
                        if (currentPage == totalPages) Color.Transparent else Color(0xFFE2E8F0),
                        RoundedCornerShape(6.dp)
                    )
                    .clickable { onPageSelect(totalPages) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = totalPages.toString(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = if (currentPage == totalPages) FontWeight.Bold else FontWeight.Medium,
                        color = if (currentPage == totalPages) Color.White else Color(0xFF0F172A),
                        fontSize = 12.sp
                    )
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // Next page
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.White)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp))
                .clickable(enabled = currentPage < totalPages) { onPageSelect(currentPage + 1) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Próximo",
                tint = if (currentPage < totalPages) Color(0xFF0F172A) else Color(0xFFCBD5E1),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun ProjectFormDialog(
    editingProject: ProjectEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String?, String) -> Unit,
    onAiSuggest: (String) -> Unit
) {
    var name by remember { mutableStateOf(editingProject?.name ?: "") }
    var description by remember { mutableStateOf(editingProject?.description ?: "") }
    var selectedType by remember { mutableStateOf(editingProject?.type ?: "Novela") }
    var selectedCategory by remember { mutableStateOf(editingProject?.category ?: "Infantil / Aventura") }
    var selectedCover by remember { mutableStateOf(editingProject?.coverUri ?: "cover_frutinhas") }
    var selectedStatus by remember { mutableStateOf(editingProject?.status ?: "Em produção") }

    val typeOptions = listOf(
        Triple("Novela", Icons.Default.Favorite, "📖"),
        Triple("Série", Icons.Default.Tv, "📺"),
        Triple("Desenho animado", Icons.Default.Palette, "🎨"),
        Triple("Filme", Icons.Default.Movie, "🎬")
    )

    val availableCovers = listOf(
        "cover_frutinhas" to "Frutinhas",
        "cover_herois_escola" to "Heróis da Escola",
        "cover_misterio_cidade" to "Mistério na Cidade",
        "cover_reino_encantado" to "Reino Encantado",
        "cover_missao_estelar" to "Missão Estelar",
        "cover_fazenda_divertida" to "Fazenda Divertida",
        "cover_piratas_kids" to "Piratas Kids",
        "cover_mundo_dinossauros" to "Dinossauros"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header + Close button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (editingProject != null) "Editar projeto" else "Criar novo projeto",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 17.sp
                        )
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Escolha o tipo de projeto
                Text(
                    text = "Escolha o tipo de projeto",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    typeOptions.forEach { (typeLabel, icon, emoji) ->
                        val isSelected = selectedType == typeLabel
                        Surface(
                            onClick = { selectedType = typeLabel },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color.White,
                            border = BorderStroke(
                                1.5.dp,
                                if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = typeLabel,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF0052FF) else Color(0xFF475569),
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section: Nome do projeto
                Text(
                    text = "Nome do projeto",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Digite o nome do projeto...", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("project_name_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section: Descrição
                Text(
                    text = "Descrição",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Digite uma descrição para o projeto...", color = Color(0xFF94A3B8), fontSize = 13.sp) },
                    minLines = 3,
                    maxLines = 5,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("project_desc_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Section: Status
                Text(
                    text = "Status de Produção",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Rascunho", "Em produção", "Concluído", "Arquivado").forEach { st ->
                        val isSel = selectedStatus == st
                        Surface(
                            onClick = { selectedStatus = st },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = st,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) Color.White else Color(0xFF475569),
                                    fontSize = 10.5.sp
                                ),
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Section: Imagem de capa
                Text(
                    text = "Imagem de capa",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Dashed Dropzone Box
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Clique para selecionar ou arraste a imagem aqui",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "PNG, JPG ou WEBP (máx. 5MB)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cover Preset Quick Selector
                Text(
                    text = "Capas 3D do Estúdio:",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 11.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(availableCovers) { (cKey, cLabel) ->
                        val isCoverSel = selectedCover == cKey
                        Surface(
                            onClick = { selectedCover = cKey },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isCoverSel) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = cLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isCoverSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isCoverSel) Color.White else Color(0xFF475569),
                                    fontSize = 11.sp
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(22.dp))

                // Bottom Buttons: Cancelar & Criar projeto
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.weight(1f).height(42.dp)
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(
                                    name.trim(),
                                    description.trim(),
                                    selectedType,
                                    selectedCategory,
                                    selectedCover,
                                    selectedStatus
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(42.dp).testTag("save_project_button")
                    ) {
                        Text(
                            text = if (editingProject != null) "Salvar alterações" else "Criar projeto",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
