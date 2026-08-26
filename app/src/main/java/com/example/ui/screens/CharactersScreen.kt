package com.example.ui.screens

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DriveFileMove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
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
import com.example.data.model.CharacterEntity
import com.example.data.model.ProjectEntity

@Composable
fun CharactersScreen(
    characters: List<CharacterEntity>,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    showNewCharacterDialog: Boolean,
    editingCharacter: CharacterEntity?,
    onOpenNewCharacter: (CharacterEntity?) -> Unit,
    onCloseNewCharacter: () -> Unit,
    onSaveCharacter: (String, String, String, String, String, String, String, String, Long?, String?) -> Unit,
    onDeleteCharacter: (CharacterEntity) -> Unit,
    onDuplicateCharacter: (CharacterEntity) -> Unit,
    onMoveCharacter: (CharacterEntity, Long?) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onNavigateToRangaCreation: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTypeFilter by remember { mutableStateOf("Todos") }
    var selectedProjectFilterId by remember { mutableStateOf(selectedProjectId) }
    var isProjectDropdownOpen by remember { mutableStateOf(false) }
    var currentSort by remember { mutableStateOf(SortOption.NEWEST) }
    var isSortMenuOpen by remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf(ViewMode.GRID) }
    var currentPage by remember { mutableIntStateOf(1) }

    // Character Detail Profile Modal
    var viewingCharacter by remember { mutableStateOf<CharacterEntity?>(null) }
    var movingCharacter by remember { mutableStateOf<CharacterEntity?>(null) }

    // Filter & Sort Logic
    val filteredCharacters = remember(
        characters,
        searchQuery,
        selectedTypeFilter,
        selectedProjectFilterId,
        currentSort
    ) {
        var list = characters.filter { character ->
            val matchesSearch = searchQuery.isBlank() ||
                    character.name.contains(searchQuery, ignoreCase = true) ||
                    character.personality.contains(searchQuery, ignoreCase = true) ||
                    character.description.contains(searchQuery, ignoreCase = true) ||
                    character.characterType.contains(searchQuery, ignoreCase = true)

            val matchesType = when (selectedTypeFilter) {
                "Todos" -> true
                "Crianças" -> character.characterType.contains("Criança", ignoreCase = true)
                "Adolescentes" -> character.characterType.contains("Adolescente", ignoreCase = true)
                "Jovens" -> character.characterType.contains("Jovem", ignoreCase = true)
                "Adultos" -> character.characterType.contains("Adulto", ignoreCase = true)
                "Idosos" -> character.characterType.contains("Idoso", ignoreCase = true)
                "Outros" -> !listOf("Criança", "Adolescente", "Jovem", "Adulto", "Idoso").any {
                    character.characterType.contains(it, ignoreCase = true)
                }
                else -> true
            }

            val matchesProject = selectedProjectFilterId == null || character.projectId == selectedProjectFilterId

            matchesSearch && matchesType && matchesProject
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
    val totalPages = maxOf(1, (filteredCharacters.size + pageSize - 1) / pageSize)
    val paginatedCharacters = remember(filteredCharacters, currentPage, totalPages) {
        val safePage = currentPage.coerceIn(1, totalPages)
        val fromIndex = (safePage - 1) * pageSize
        filteredCharacters.drop(fromIndex).take(pageSize)
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
            // 1. Page Header (Title + Subtitle + "+ Novo Personagem" button)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Personagens",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 22.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Crie e organize os personagens das suas novelas, séries, desenhos animados e filmes.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Button(
                            onClick = onNavigateToRangaCreation,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F172A)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
                            modifier = Modifier.testTag("ranga_creation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF38BDF8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "✨ Criação RANGA",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = { onOpenNewCharacter(null) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                            modifier = Modifier.testTag("new_character_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+ Novo Personagem",
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // 2. Search, Filters, Project Filter, Sort and View Switcher Row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Top Search & Category Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                                currentPage = 1
                            },
                            placeholder = {
                                Text(
                                    "Pesquisar personagens...",
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
                                .testTag("characters_search_field")
                        )

                        // Type Filter Chips (Horizontal list)
                        Row(
                            modifier = Modifier
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val typeFilters = listOf("Todos", "Crianças", "Adolescentes", "Jovens", "Adultos", "Idosos", "Outros")
                            typeFilters.forEach { f ->
                                val isSel = selectedTypeFilter == f
                                Surface(
                                    onClick = {
                                        selectedTypeFilter = f
                                        currentPage = 1
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSel) Color(0xFF0052FF) else Color.White,
                                    border = if (isSel) null else BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.testTag("char_filter_$f")
                                ) {
                                    Text(
                                        text = f,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSel) Color.White else Color(0xFF475569),
                                            fontSize = 12.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Second Row: Project Dropdown + Sort Menu + View Switcher
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Project Filter Dropdown
                        Box {
                            val selectedProjectName = projects.find { it.id == selectedProjectFilterId }?.name ?: "Todos os projetos"
                            OutlinedButton(
                                onClick = { isProjectDropdownOpen = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color(0xFF0F172A)
                                ),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Folder,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = selectedProjectName,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.5.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("▾", fontSize = 12.sp, color = Color(0xFF64748B))
                            }

                            DropdownMenu(
                                expanded = isProjectDropdownOpen,
                                onDismissRequest = { isProjectDropdownOpen = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("📁 Todos os projetos") },
                                    onClick = {
                                        selectedProjectFilterId = null
                                        isProjectDropdownOpen = false
                                        currentPage = 1
                                    }
                                )
                                HorizontalDivider()
                                projects.forEach { proj ->
                                    DropdownMenuItem(
                                        text = { Text(proj.name) },
                                        onClick = {
                                            selectedProjectFilterId = proj.id
                                            isProjectDropdownOpen = false
                                            currentPage = 1
                                        }
                                    )
                                }
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Sort Menu Dropdown
                            Box {
                                OutlinedButton(
                                    onClick = { isSortMenuOpen = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = Color.White,
                                        contentColor = Color(0xFF0F172A)
                                    ),
                                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                    modifier = Modifier.height(40.dp)
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

                            // View Mode Toggle (Grid / List)
                            Row(
                                modifier = Modifier
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                    .padding(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (viewMode == ViewMode.GRID) Color(0xFF0052FF) else Color.Transparent)
                                        .clickable { viewMode = ViewMode.GRID },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.GridView,
                                        contentDescription = "Grade",
                                        tint = if (viewMode == ViewMode.GRID) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (viewMode == ViewMode.LIST) Color(0xFF0052FF) else Color.Transparent)
                                        .clickable { viewMode = ViewMode.LIST },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ViewList,
                                        contentDescription = "Lista",
                                        tint = if (viewMode == ViewMode.LIST) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 3. Characters Content (Empty State, Grid View, or List View)
            if (filteredCharacters.isEmpty()) {
                item {
                    EmptyCharactersView(
                        onNewCharacterClick = { onOpenNewCharacter(null) }
                    )
                }
            } else {
                if (viewMode == ViewMode.GRID) {
                    val rows = paginatedCharacters.chunked(gridColumns)
                    rows.forEach { rowItems ->
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                rowItems.forEach { character ->
                                    val projectName = projects.find { it.id == character.projectId }?.name ?: "Projeto Geral"
                                    Box(modifier = Modifier.weight(1f)) {
                                        CharacterGridCard(
                                            character = character,
                                            projectName = projectName,
                                            onOpen = { viewingCharacter = character },
                                            onEdit = { onOpenNewCharacter(character) },
                                            onDuplicate = { onDuplicateCharacter(character) },
                                            onMove = { movingCharacter = character },
                                            onDelete = { onDeleteCharacter(character) }
                                        )
                                    }
                                }
                                val emptySlots = gridColumns - rowItems.size
                                repeat(emptySlots) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                } else {
                    items(paginatedCharacters, key = { it.id }) { character ->
                        val projectName = projects.find { it.id == character.projectId }?.name ?: "Projeto Geral"
                        CharacterListRow(
                            character = character,
                            projectName = projectName,
                            onOpen = { viewingCharacter = character },
                            onEdit = { onOpenNewCharacter(character) },
                            onDuplicate = { onDuplicateCharacter(character) },
                            onMove = { movingCharacter = character },
                            onDelete = { onDeleteCharacter(character) }
                        )
                    }
                }

                // 4. Pagination Bar
                item {
                    PaginationBar(
                        currentPage = currentPage,
                        totalPages = totalPages,
                        onPageSelect = { currentPage = it }
                    )
                }
            }

            // 5. Empty State Card Box at bottom as in reference
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEFF6FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color(0xFF0052FF),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = "Você ainda não tem personagens",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0F172A),
                                        fontSize = 14.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Crie seus primeiros personagens para começar a dar vida à sua história.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF64748B),
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }

                        Button(
                            onClick = { onOpenNewCharacter(null) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text("+ Criar primeiro personagem", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // 6. Create / Edit Character Form Modal
        if (showNewCharacterDialog) {
            CharacterCreatorModal(
                editingCharacter = editingCharacter,
                projects = projects,
                selectedProjectId = selectedProjectId,
                onDismiss = onCloseNewCharacter,
                onSave = onSaveCharacter,
                onAiSuggest = onOpenAiWithPrompt
            )
        }

        // 7. Character Full Detail Profile Dialog (Abrir)
        viewingCharacter?.let { char ->
            val charProjectName = projects.find { it.id == char.projectId }?.name ?: "Projeto Geral"
            CharacterProfileDialog(
                character = char,
                projectName = charProjectName,
                onDismiss = { viewingCharacter = null },
                onEdit = {
                    viewingCharacter = null
                    onOpenNewCharacter(char)
                },
                onDuplicate = {
                    viewingCharacter = null
                    onDuplicateCharacter(char)
                },
                onDelete = {
                    viewingCharacter = null
                    onDeleteCharacter(char)
                }
            )
        }

        // 8. Move Character to Project Dialog
        movingCharacter?.let { char ->
            MoveCharacterDialog(
                character = char,
                projects = projects,
                onDismiss = { movingCharacter = null },
                onMove = { targetProjId ->
                    onMoveCharacter(char, targetProjId)
                    movingCharacter = null
                }
            )
        }
    }
}

@Composable
fun CharacterGridCard(
    character: CharacterEntity,
    projectName: String,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    val avatarRes = when (character.imageUri) {
        "char_antonio" -> R.drawable.char_antonio
        "char_bia" -> R.drawable.char_bia
        "char_carlos" -> R.drawable.char_carlos
        "char_lucas" -> R.drawable.char_lucas
        "char_sofia" -> R.drawable.char_sofia
        "char_sr_manuel" -> R.drawable.char_sr_manuel
        "char_mimi" -> R.drawable.char_mimi
        "char_r7_robot" -> R.drawable.char_r7_robot
        "char_preview_boy" -> R.drawable.char_preview_boy_1787037236644
        else -> when (character.id % 8L) {
            1L -> R.drawable.char_antonio
            2L -> R.drawable.char_bia
            3L -> R.drawable.char_carlos
            4L -> R.drawable.char_lucas
            5L -> R.drawable.char_sofia
            6L -> R.drawable.char_sr_manuel
            7L -> R.drawable.char_mimi
            else -> R.drawable.char_r7_robot
        }
    }

    val (badgeBg, badgeTextColor) = when (character.characterType) {
        "Fruta" -> Color(0xFFF3E8FF) to Color(0xFF7C3AED)
        "Criança" -> Color(0xFFEFF6FF) to Color(0xFF2563EB)
        "Idoso" -> Color(0xFFECFDF5) to Color(0xFF059669)
        "Animal" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "Robô" -> Color(0xFFEEF2FF) to Color(0xFF4F46E5)
        "Adolescente" -> Color(0xFFFDF2F8) to Color(0xFFDB2777)
        "Jovem" -> Color(0xFFF0FDF4) to Color(0xFF16A34A)
        else -> Color(0xFFF1F5F9) to Color(0xFF475569)
    }

    val creationDateStr = when (character.id % 8L) {
        1L, 2L, 3L -> "Criado em: 18/05/2024"
        4L, 5L -> "Criado em: 17/05/2024"
        6L, 7L -> "Criado em: 16/05/2024"
        else -> "Criado em: 15/05/2024"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("char_card_${character.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Character Image Header with 3-dots Menu Button Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .background(Color(0xFFF1F5F9)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = avatarRes),
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top right 3-dots menu button
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
                            leadingIcon = { Icon(Icons.Default.DriveFileMove, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp)) },
                            text = { Text("Mover para outro projeto") },
                            onClick = {
                                isMenuOpen = false
                                onMove()
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
                modifier = Modifier.padding(12.dp)
            ) {
                // Name & Type Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 14.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(badgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = character.characterType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = badgeTextColor,
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Project & Age info
                Text(
                    text = "Projeto: $projectName",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF475569),
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Idade: ${character.age.ifBlank { "-" }}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Personality
                Text(
                    text = "Personalidade: ${character.personality}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.height(30.dp)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Voice indicator
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎙️ Voz: ${character.voice}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF0F172A),
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("🔊", fontSize = 10.sp)
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Creation Date
                Text(
                    text = creationDateStr,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Open Button
                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEFF6FF),
                        contentColor = Color(0xFF0052FF)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Abrir",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CharacterListRow(
    character: CharacterEntity,
    projectName: String,
    onOpen: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onMove: () -> Unit,
    onDelete: () -> Unit
) {
    var isMenuOpen by remember { mutableStateOf(false) }

    val avatarRes = when (character.imageUri) {
        "char_antonio" -> R.drawable.char_antonio
        "char_bia" -> R.drawable.char_bia
        "char_carlos" -> R.drawable.char_carlos
        "char_lucas" -> R.drawable.char_lucas
        "char_sofia" -> R.drawable.char_sofia
        "char_sr_manuel" -> R.drawable.char_sr_manuel
        "char_mimi" -> R.drawable.char_mimi
        "char_r7_robot" -> R.drawable.char_r7_robot
        else -> R.drawable.char_antonio
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("char_row_${character.id}"),
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
            Image(
                painter = painterResource(id = avatarRes),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = character.name,
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
                            text = character.characterType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB),
                                fontSize = 10.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Projeto: $projectName • Voz: ${character.voice}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF64748B),
                        fontSize = 11.5.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

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
                        leadingIcon = { Icon(Icons.Default.DriveFileMove, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(18.dp)) },
                        text = { Text("Mover para outro projeto") },
                        onClick = {
                            isMenuOpen = false
                            onMove()
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
fun EmptyCharactersView(
    onNewCharacterClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .testTag("characters_empty_view"),
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
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF0052FF),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Você ainda não tem personagens",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp
                )
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Crie seus primeiros personagens para começar a dar vida à sua história.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onNewCharacterClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ Criar primeiro personagem",
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun CharacterProfileDialog(
    character: CharacterEntity,
    projectName: String,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    val avatarRes = when (character.imageUri) {
        "char_antonio" -> R.drawable.char_antonio
        "char_bia" -> R.drawable.char_bia
        "char_carlos" -> R.drawable.char_carlos
        "char_lucas" -> R.drawable.char_lucas
        "char_sofia" -> R.drawable.char_sofia
        "char_sr_manuel" -> R.drawable.char_sr_manuel
        "char_mimi" -> R.drawable.char_mimi
        "char_r7_robot" -> R.drawable.char_r7_robot
        else -> R.drawable.char_antonio
    }

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
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF64748B))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Perfil do Personagem",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 17.sp
                            )
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Hero Top: Large Image & Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Image(
                        painter = painterResource(id = avatarRes),
                        contentDescription = character.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(110.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFF1F5F9))
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 20.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFEFF6FF))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = character.characterType,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0052FF),
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFFF1F5F9))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Idade: ${character.age.ifBlank { "N/A" }}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF475569),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "📁 Projeto: $projectName",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF0F172A),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )

                        Text(
                            text = "🎙️ Voz do personagem: ${character.voice}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Section: Personalidade
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Personalidade",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = character.personality,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF334155),
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section: Descrição Visual
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Descrição e Aparência",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = character.description.ifBlank { "Sem descrição adicional informada." },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF334155),
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section: História do Personagem
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "História e Passado",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = character.history.ifBlank { "História do personagem a ser desenvolvida nas próximas cenas." },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF334155),
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section: 🔒 Consistência do Personagem (Identidade Visual Fixa)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔒", fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Consistência do Personagem (Identidade Visual)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534),
                                    fontSize = 12.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Características visuais sincronizadas para manter o mesmo rosto, paleta de cores, estilo artístico e figurino padrão em todas as cenas e episódios.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF15803D),
                                fontSize = 11.5.sp,
                                lineHeight = 16.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("🎨 Estilo: 3D / Cartoon", fontSize = 10.5.sp, color = Color(0xFF166534), fontWeight = FontWeight.SemiBold)
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFF86EFAC), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text("✨ Traço Consistente", fontSize = 10.5.sp, color = Color(0xFF166534), fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Section: 🎭 Versões do Personagem (Variações / Figurinos / Expressões)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎭 Versões do Personagem",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = "+ Nova Versão",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0052FF),
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        val versions = listOf(
                            Pair("Versão Normal (Padrão)", "🌟 Principal"),
                            Pair("Versão Criança", "👶 Variação"),
                            Pair("Roupa de Escola / Uniforme", "🎒 Figurino"),
                            Pair("Roupa de Dormir (Pijama)", "🌙 Figurino"),
                            Pair("Expressão: Determinado / Bravo", "😠 Expressão")
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            versions.forEach { (vTitle, vBadge) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color.White)
                                        .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = vTitle,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF334155),
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(Color(0xFFEFF6FF))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = vBadge,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF0052FF),
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cenas e Episódios
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("🎬 Cenas", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0052FF)))
                            Text("Aparece na Cena 1, Cena 3", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF1E3A8A)))
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                        border = BorderStroke(1.dp, Color(0xFFF3E8FF))
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("🎞️ Episódios", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF9333EA)))
                            Text("Episódio 1, Episódio 2", style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = Color(0xFF581C87)))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons: Editar, Duplicar, Excluir
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onEdit,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF0052FF)),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = Color(0xFF0052FF), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onDuplicate,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFF059669)),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Duplicar", color = Color(0xFF059669), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onDelete,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFDC2626)),
                        modifier = Modifier.weight(1f).height(40.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Excluir", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun MoveCharacterDialog(
    character: CharacterEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onMove: (Long?) -> Unit
) {
    var selectedTargetProjId by remember { mutableStateOf(character.projectId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Mover Personagem para Outro Projeto", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Escolha o projeto de destino para “${character.name}”:")
                projects.forEach { proj ->
                    val isSel = selectedTargetProjId == proj.id
                    Surface(
                        onClick = { selectedTargetProjId = proj.id },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) Color(0xFFEFF6FF) else Color.White,
                        border = BorderStroke(1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = proj.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFF0F172A)
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onMove(selectedTargetProjId) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF))
            ) {
                Text("Mover Personagem", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun CharacterFormDialog(
    editingCharacter: CharacterEntity?,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, String, Long?, String?) -> Unit,
    onAiSuggest: (String) -> Unit
) {
    var name by remember { mutableStateOf(editingCharacter?.name ?: "") }
    var age by remember { mutableStateOf(editingCharacter?.age ?: "") }
    var selectedType by remember { mutableStateOf(editingCharacter?.characterType ?: "Criança") }
    var selectedProjId by remember { mutableStateOf(editingCharacter?.projectId ?: selectedProjectId ?: projects.firstOrNull()?.id) }
    var description by remember { mutableStateOf(editingCharacter?.description ?: "") }
    var personality by remember { mutableStateOf(editingCharacter?.personality ?: "") }
    var history by remember { mutableStateOf(editingCharacter?.history ?: "") }
    var voice by remember { mutableStateOf(editingCharacter?.voice ?: "Voz Masculina 1") }
    var role by remember { mutableStateOf(editingCharacter?.role ?: "Protagonista") }
    var selectedAvatarKey by remember { mutableStateOf(editingCharacter?.imageUri ?: "char_antonio") }

    var isTypeDropdownOpen by remember { mutableStateOf(false) }
    var isProjectDropdownOpen by remember { mutableStateOf(false) }
    var isVoiceDropdownOpen by remember { mutableStateOf(false) }

    val characterTypes = listOf("Criança", "Adolescente", "Jovem", "Adulto", "Idoso", "Fruta", "Animal", "Robô", "Outro")
    val voiceOptions = listOf("Voz Masculina 1", "Voz Masculina 2", "Voz Masculina 3", "Voz Masculina 4", "Voz Feminina 1", "Voz Feminina 2", "Voz Feminina 3", "Voz Robô 1", "Voz Infantil")
    val availableAvatars = listOf(
        Pair("char_antonio", "António (Maçã)"),
        Pair("char_bia", "Bia (Banana)"),
        Pair("char_carlos", "Carlos (Laranja)"),
        Pair("char_lucas", "Lucas (Menino)"),
        Pair("char_sofia", "Sofia (Menina)"),
        Pair("char_sr_manuel", "Sr. Manuel"),
        Pair("char_mimi", "Mimi (Gatinha)"),
        Pair("char_r7_robot", "R-7 (Robô)")
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
                // Header + Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (editingCharacter != null) "Editar personagem" else "Criar novo personagem",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 17.sp
                        )
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar Selector / Dropzone
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEFF6FF))
                            .border(1.5.dp, Color(0xFF0052FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(24.dp))
                            Text("Adicionar", fontSize = 9.sp, color = Color(0xFF0052FF), fontWeight = FontWeight.Bold)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Imagem do personagem",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp)
                        )
                        Text(
                            text = "PNG, JPG ou WEBP (máx. 5MB)",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items(availableAvatars) { item ->
                                val (aKey, aLabel) = item
                                val isSel = selectedAvatarKey == aKey
                                Surface(
                                    onClick = { selectedAvatarKey = aKey },
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                                ) {
                                    Text(
                                        text = aLabel,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSel) Color.White else Color(0xFF475569),
                                            fontSize = 10.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Field: Nome do personagem *
                Text("Nome do personagem *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    placeholder = { Text("Digite o nome do personagem", color = Color(0xFF94A3B8), fontSize = 12.5.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("char_name_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Row: Idade & Tipo de personagem
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Idade", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            placeholder = { Text("Ex: 10", color = Color(0xFF94A3B8), fontSize = 12.5.sp) },
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.weight(1.5f)) {
                        Text("Tipo de personagem", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Box {
                            OutlinedButton(
                                onClick = { isTypeDropdownOpen = true },
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(selectedType, color = Color(0xFF0F172A), fontSize = 12.sp)
                                    Text("▾", color = Color(0xFF64748B))
                                }
                            }

                            DropdownMenu(
                                expanded = isTypeDropdownOpen,
                                onDismissRequest = { isTypeDropdownOpen = false }
                            ) {
                                characterTypes.forEach { t ->
                                    DropdownMenuItem(
                                        text = { Text(t) },
                                        onClick = {
                                            selectedType = t
                                            isTypeDropdownOpen = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Field: Projeto *
                Text("Projeto *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    val currentProjName = projects.find { it.id == selectedProjId }?.name ?: "Selecione o projeto"
                    OutlinedButton(
                        onClick = { isProjectDropdownOpen = true },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(currentProjName, color = Color(0xFF0F172A), fontSize = 12.5.sp)
                            Text("▾", color = Color(0xFF64748B))
                        }
                    }

                    DropdownMenu(
                        expanded = isProjectDropdownOpen,
                        onDismissRequest = { isProjectDropdownOpen = false }
                    ) {
                        projects.forEach { proj ->
                            DropdownMenuItem(
                                text = { Text(proj.name) },
                                onClick = {
                                    selectedProjId = proj.id
                                    isProjectDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Field: Descrição
                Text("Descrição (Aparência)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Descreva a aparência do personagem...", color = Color(0xFF94A3B8), fontSize = 12.5.sp) },
                    minLines = 2,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0052FF),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Row: Personalidade & História
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Personalidade", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = personality,
                            onValueChange = { personality = it },
                            placeholder = { Text("Ex: Corajoso, divertido...", color = Color(0xFF94A3B8), fontSize = 12.sp) },
                            minLines = 2,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("História do personagem", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = history,
                            onValueChange = { history = it },
                            placeholder = { Text("Fale sobre o passado...", color = Color(0xFF94A3B8), fontSize = 12.sp) },
                            minLines = 2,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedContainerColor = Color(0xFFF8FAFC),
                                unfocusedContainerColor = Color(0xFFF8FAFC)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Field: Voz
                Text("Voz", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF0F172A), fontSize = 12.sp))
                Spacer(modifier = Modifier.height(4.dp))
                Box {
                    OutlinedButton(
                        onClick = { isVoiceDropdownOpen = true },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🎙️ $voice", color = Color(0xFF0F172A), fontSize = 12.5.sp)
                            Text("▾", color = Color(0xFF64748B))
                        }
                    }

                    DropdownMenu(
                        expanded = isVoiceDropdownOpen,
                        onDismissRequest = { isVoiceDropdownOpen = false }
                    ) {
                        voiceOptions.forEach { v ->
                            DropdownMenuItem(
                                text = { Text("🎙️ $v") },
                                onClick = {
                                    voice = v
                                    isVoiceDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bottom AI Assistant Prompt Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    border = BorderStroke(1.dp, Color(0xFFDCFCE7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("✨ Criar com IA", fontWeight = FontWeight.Bold, color = Color(0xFF16A34A), fontSize = 12.5.sp)
                            Text("Deixe a IA te ajudar a criar seu personagem", color = Color(0xFF15803D), fontSize = 11.sp)
                        }
                        OutlinedButton(
                            onClick = {
                                onAiSuggest("Crie um personagem completo (nome, idade, tipo, personalidade, aparência e história) do tipo $selectedType para uma produção audiovisual.")
                            },
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, Color(0xFF16A34A)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                        ) {
                            Text("✨ Gerar com IA", color = Color(0xFF16A34A), fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Buttons: Cancelar & Criar personagem
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
                        Text("Cancelar", color = Color(0xFF475569), fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSave(
                                    name.trim(),
                                    personality.trim().ifBlank { "Corajoso e leal" },
                                    age.trim(),
                                    description.trim(),
                                    history.trim(),
                                    selectedType,
                                    voice,
                                    role,
                                    selectedProjId,
                                    selectedAvatarKey
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(42.dp).testTag("save_character_button")
                    ) {
                        Text(
                            text = if (editingCharacter != null) "Salvar alterações" else "Criar personagem",
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
