package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary
import com.example.ui.theme.PurpleCreative

@Composable
fun SoundTopHeaderAndContextBar(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    totalCount: Int,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onUploadClick: () -> Unit,
    onAiCreateClick: () -> Unit,
    onNewSoundClick: () -> Unit
) {
    val activeProjectName = projects.find { it.id == selectedProjectId }?.name

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // Main Title and Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Sons e Músicas",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NavyPrimary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "$totalCount faixas",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = if (activeProjectName != null) "Projeto ativo: $activeProjectName" else "Biblioteca musical e sonoplastia para produções",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            // Top Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedButton(
                    onClick = onUploadClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyPrimary),
                    modifier = Modifier.testTag("upload_sound_button")
                ) {
                    Icon(Icons.Default.UploadFile, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Upload", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                OutlinedButton(
                    onClick = onAiCreateClick,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleCreative),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PurpleCreative.copy(alpha = 0.6f)),
                    modifier = Modifier.testTag("ai_create_sound_button")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleCreative, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Gerar com IA", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }

                Button(
                    onClick = onNewSoundClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("new_sound_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Novo Áudio", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Pesquisar músicas, efeitos sonoros, climas ou instrumentos...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchChange("") }) {
                        Icon(Icons.Default.Close, contentDescription = "Limpar busca", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().testTag("sound_search_input"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = NavyPrimary,
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SoundFilterPillSection(
    primaryTab: String,
    onTabSelect: (String) -> Unit,
    totalCount: Int,
    musicCount: Int,
    sfxCount: Int,
    ambientCount: Int,
    soundtrackCount: Int,
    favoritesCount: Int,
    selectedMood: String,
    onMoodSelect: (String) -> Unit,
    selectedDuration: String,
    onDurationSelect: (String) -> Unit,
    isGridView: Boolean,
    onToggleGridView: (Boolean) -> Unit,
    sortBy: String,
    onSortChange: (String) -> Unit
) {
    var expandedMoodDropdown by remember { mutableStateOf(false) }
    var expandedDurationDropdown by remember { mutableStateOf(false) }
    var expandedSortDropdown by remember { mutableStateOf(false) }

    val tabs = listOf(
        "Todos" to totalCount,
        "Músicas" to musicCount,
        "Efeitos (SFX)" to sfxCount,
        "Ambientes" to ambientCount,
        "Trilhas" to soundtrackCount,
        "Favoritos" to favoritesCount
    )

    val moodOptions = listOf("Todos", "Épico", "Aventura", "Suspense", "Alegre", "Dramático", "Mistério", "Sci-Fi", "Cômico")
    val durationOptions = listOf("Qualquer duração", "Curtos (< 15s)", "Médios (15s - 1m)", "Longos (> 1m)")
    val sortOptions = listOf("Mais recentes", "Título A–Z", "Maior duração", "Mais utilizados", "Favoritos")

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // Tab Pills
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { (label, count) ->
                val isSelected = primaryTab == label
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (isSelected) NavyPrimary else Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) NavyPrimary else Color(0xFFE2E8F0)),
                    modifier = Modifier.clickable { onTabSelect(label) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else NavyDark
                        )
                        Surface(
                            shape = CircleShape,
                            color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFF1F5F9)
                        ) {
                            Text(
                                text = count.toString(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color.Gray,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        // Secondary Filters Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                // Mood Dropdown Filter
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { expandedMoodDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Clima: $selectedMood", fontSize = 11.sp, color = NavyDark, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = expandedMoodDropdown,
                        onDismissRequest = { expandedMoodDropdown = false }
                    ) {
                        moodOptions.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m, fontSize = 12.sp) },
                                onClick = {
                                    onMoodSelect(m)
                                    expandedMoodDropdown = false
                                }
                            )
                        }
                    }
                }

                // Duration Dropdown Filter
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { expandedDurationDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Duração: $selectedDuration", fontSize = 11.sp, color = NavyDark, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = expandedDurationDropdown,
                        onDismissRequest = { expandedDurationDropdown = false }
                    ) {
                        durationOptions.forEach { d ->
                            DropdownMenuItem(
                                text = { Text(d, fontSize = 12.sp) },
                                onClick = {
                                    onDurationSelect(d)
                                    expandedDurationDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // View toggle & Sorting
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                // Sort Dropdown
                Box {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { expandedSortDropdown = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(sortBy, fontSize = 11.sp, color = NavyDark, fontWeight = FontWeight.Medium)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        }
                    }
                    DropdownMenu(
                        expanded = expandedSortDropdown,
                        onDismissRequest = { expandedSortDropdown = false }
                    ) {
                        sortOptions.forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s, fontSize = 12.sp) },
                                onClick = {
                                    onSortChange(s)
                                    expandedSortDropdown = false
                                }
                            )
                        }
                    }
                }

                // Grid / List View Toggle
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(modifier = Modifier.padding(2.dp)) {
                        IconButton(
                            onClick = { onToggleGridView(true) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.GridView,
                                contentDescription = "Grade",
                                tint = if (isGridView) NavyPrimary else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        IconButton(
                            onClick = { onToggleGridView(false) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                Icons.Default.ViewList,
                                contentDescription = "Lista",
                                tint = if (!isGridView) NavyPrimary else Color.Gray,
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
fun SoundCardsContent(
    sounds: List<SoundMusicEntity>,
    selectedSoundId: Long?,
    currentlyPlayingSoundId: Long?,
    isGridView: Boolean,
    onSelectSound: (SoundMusicEntity) -> Unit,
    onPlaySound: (SoundMusicEntity) -> Unit,
    onToggleFavorite: (SoundMusicEntity) -> Unit,
    onEdit: (SoundMusicEntity) -> Unit,
    onDuplicate: (SoundMusicEntity) -> Unit,
    onOpenAttachToScene: (SoundMusicEntity) -> Unit,
    onOpenAttachToEpisode: (SoundMusicEntity) -> Unit,
    onOpenEditor: (SoundMusicEntity) -> Unit,
    onArchive: (SoundMusicEntity) -> Unit,
    onDelete: (SoundMusicEntity) -> Unit,
    onNewSoundClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (sounds.isEmpty()) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            modifier = modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(NavyPrimary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.QueueMusic,
                        contentDescription = null,
                        tint = NavyPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Nenhum áudio encontrado", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                Text(
                    "Tente ajustar os filtros ou adicione uma nova trilha/efeito sonoro à sua biblioteca.",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = onNewSoundClick,
                    colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Criar Novo Áudio")
                }
            }
        }
    } else if (isGridView) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 270.dp),
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(sounds, key = { it.id }) { sound ->
                SoundCard(
                    sound = sound,
                    isSelected = sound.id == selectedSoundId,
                    isPlaying = sound.id == currentlyPlayingSoundId,
                    onSelect = { onSelectSound(sound) },
                    onPlay = { onPlaySound(sound) },
                    onToggleFavorite = { onToggleFavorite(sound) },
                    onEdit = { onEdit(sound) },
                    onDuplicate = { onDuplicate(sound) },
                    onAttachToScene = { onOpenAttachToScene(sound) },
                    onAttachToEpisode = { onOpenAttachToEpisode(sound) },
                    onOpenEditor = { onOpenEditor(sound) },
                    onArchive = { onArchive(sound) },
                    onDelete = { onDelete(sound) }
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(sounds, key = { it.id }) { sound ->
                SoundCard(
                    sound = sound,
                    isSelected = sound.id == selectedSoundId,
                    isPlaying = sound.id == currentlyPlayingSoundId,
                    onSelect = { onSelectSound(sound) },
                    onPlay = { onPlaySound(sound) },
                    onToggleFavorite = { onToggleFavorite(sound) },
                    onEdit = { onEdit(sound) },
                    onDuplicate = { onDuplicate(sound) },
                    onAttachToScene = { onOpenAttachToScene(sound) },
                    onAttachToEpisode = { onOpenAttachToEpisode(sound) },
                    onOpenEditor = { onOpenEditor(sound) },
                    onArchive = { onArchive(sound) },
                    onDelete = { onDelete(sound) }
                )
            }
        }
    }
}

@Composable
fun SoundCard(
    sound: SoundMusicEntity,
    isSelected: Boolean,
    isPlaying: Boolean,
    onSelect: () -> Unit,
    onPlay: () -> Unit,
    onToggleFavorite: () -> Unit,
    onEdit: () -> Unit,
    onDuplicate: () -> Unit,
    onAttachToScene: () -> Unit,
    onAttachToEpisode: () -> Unit,
    onOpenEditor: () -> Unit,
    onArchive: () -> Unit,
    onDelete: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F9FF) else Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, CyanAccent) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("sound_card_${sound.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            // Header with Type Tag, Title & Action Menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Type Badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = when {
                            sound.type.contains("SFX", ignoreCase = true) || sound.type.contains("Efeito", ignoreCase = true) -> Color(0xFFFEF3C7)
                            sound.type.contains("Ambiente", ignoreCase = true) -> Color(0xFFD1FAE5)
                            else -> Color(0xFFE0E7FF)
                        }
                    ) {
                        Text(
                            text = sound.type,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                sound.type.contains("SFX", ignoreCase = true) || sound.type.contains("Efeito", ignoreCase = true) -> Color(0xFFB45309)
                                sound.type.contains("Ambiente", ignoreCase = true) -> Color(0xFF047857)
                                else -> NavyPrimary
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    if (sound.isAiGenerated) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PurpleCreative.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "IA",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PurpleCreative,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = sound.title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = NavyDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Right icons (Favorite star + 3 dots menu)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = if (sound.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Favoritar",
                            tint = if (sound.isFavorite) AmberGold else Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Box {
                        IconButton(
                            onClick = { expandedMenu = true },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color.Gray, modifier = Modifier.size(18.dp))
                        }
                        DropdownMenu(
                            expanded = expandedMenu,
                            onDismissRequest = { expandedMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar Informações") },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onEdit()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Editor de Onda / Cortes") },
                                leadingIcon = { Icon(Icons.Default.ContentCut, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onOpenEditor()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Duplicar Faixa") },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onDuplicate()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Vincular à Cena") },
                                leadingIcon = { Icon(Icons.Default.Movie, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onAttachToScene()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Usar no Episódio") },
                                leadingIcon = { Icon(Icons.Default.Tv, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onAttachToEpisode()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(if (sound.isArchived) "Restaurar" else "Arquivar") },
                                leadingIcon = { Icon(Icons.Outlined.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onArchive()
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Excluir Áudio", color = Color(0xFFDC2626)) },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp)) },
                                onClick = {
                                    expandedMenu = false
                                    onDelete()
                                }
                            )
                        }
                    }
                }
            }

            // Waveform & Playback Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Play / Pause Circle
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(if (isPlaying) CyanAccent else NavyPrimary)
                        .clickable { onPlay() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = "Ouvir",
                        tint = if (isPlaying) NavyDark else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Decorative waveform bars
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(18) { idx ->
                        val barHeight = ((Math.sin(idx.toDouble() * 0.6) * 0.5 + 0.5) * 18.0 + 4.0).dp
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(barHeight)
                                .clip(RoundedCornerShape(1.dp))
                                .background(if (isPlaying) CyanAccent else Color(0xFF94A3B8))
                        )
                    }
                }

                // Duration Text
                Text(
                    text = sound.duration,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = NavyDark
                )
            }

            // Tags (Mood, BPM, Key)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFF8FAFC)) {
                        Text(sound.mood, fontSize = 10.sp, color = Color.DarkGray, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFF8FAFC)) {
                        Text("${sound.tempoBpm} BPM", fontSize = 10.sp, color = Color.DarkGray, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFF8FAFC)) {
                        Text(sound.musicalKey, fontSize = 10.sp, color = Color.DarkGray, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                }

                Text(
                    text = sound.format,
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            // Associated Scene / Episode Pills if bound
            if (sound.sceneName != null || sound.episodeName != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    sound.sceneName?.let { scName ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = EmeraldSuccess.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(12.dp))
                                Text(scName, fontSize = 10.sp, color = EmeraldSuccess, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                        }
                    }

                    sound.episodeName?.let { epName ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = NavyPrimary.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Tv, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(12.dp))
                                Text(epName, fontSize = 10.sp, color = NavyPrimary, fontWeight = FontWeight.SemiBold, maxLines = 1)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SoundBottomStatsSection(
    totalCount: Int,
    musicCount: Int,
    sfxCount: Int,
    usedCount: Int,
    recentActivities: List<ActivityLogEntity>
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        // 4 Summary Metrics Cards
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            StatPillCard(title = "Total de Áudios", value = totalCount.toString(), icon = Icons.AutoMirrored.Filled.QueueMusic, color = NavyPrimary, modifier = Modifier.weight(1f))
            StatPillCard(title = "Músicas & Trilhas", value = musicCount.toString(), icon = Icons.Default.MusicNote, color = PurpleCreative, modifier = Modifier.weight(1f))
            StatPillCard(title = "Efeitos SFX", value = sfxCount.toString(), icon = Icons.Default.GraphicEq, color = AmberGold, modifier = Modifier.weight(1f))
            StatPillCard(title = "Usados em Cenas", value = usedCount.toString(), icon = Icons.Default.Movie, color = EmeraldSuccess, modifier = Modifier.weight(1f))
        }

        // Recent Activity Strip
        val soundLogs = recentActivities.filter { it.iconType.contains("music") || it.iconType.contains("sound") || it.title.contains("áudio", ignoreCase = true) }
        if (soundLogs.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                    Text(
                        text = "Última atividade: ${soundLogs.first().title} - ${soundLogs.first().description}",
                        fontSize = 11.sp,
                        color = NavyDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun StatPillCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Column {
                Text(text = value, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = NavyDark)
                Text(text = title, fontSize = 9.sp, color = Color.Gray, maxLines = 1)
            }
        }
    }
}
