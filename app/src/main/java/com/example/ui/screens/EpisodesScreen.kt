package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActivityLogEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.EpisodeEntity
import com.example.data.model.EpisodeStatus
import com.example.data.model.ProjectEntity
import com.example.data.model.ScenarioEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.data.model.SoundMusicEntity
import com.example.data.model.VoiceEntity
import com.example.ui.navigation.StudioDestination
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary

@Composable
fun EpisodesScreen(
    episodes: List<EpisodeEntity>,
    projects: List<ProjectEntity>,
    series: List<SeriesEntity> = emptyList(),
    seasons: List<SeasonEntity> = emptyList(),
    scenes: List<SceneEntity> = emptyList(),
    characters: List<CharacterEntity> = emptyList(),
    scenarios: List<ScenarioEntity> = emptyList(),
    voices: List<VoiceEntity> = emptyList(),
    sounds: List<SoundMusicEntity> = emptyList(),
    recentActivities: List<ActivityLogEntity> = emptyList(),
    selectedProjectId: Long? = null,
    activeEpisode: EpisodeEntity? = null,
    activeSeason: SeasonEntity? = null,
    activeSeries: SeriesEntity? = null,
    lastSaveTimestamp: Long = System.currentTimeMillis(),
    showNewEpisodeDialog: Boolean = false,
    editingEpisode: EpisodeEntity? = null,
    onSetActiveEpisode: (EpisodeEntity?) -> Unit = {},
    onSetActiveSeason: (SeasonEntity?) -> Unit = {},
    onSetActiveSeries: (SeriesEntity?) -> Unit = {},
    onOpenNewEpisode: (EpisodeEntity?) -> Unit = {},
    onCloseNewEpisode: () -> Unit = {},
    onSaveEpisode: (EpisodeEntity, Boolean) -> Unit = { _, _ -> },
    onDeleteEpisode: (EpisodeEntity) -> Unit = {},
    onDuplicateEpisode: (EpisodeEntity) -> Unit = {},
    onArchiveEpisode: (EpisodeEntity) -> Unit = {},
    onOpenAiWithPrompt: (String) -> Unit = {},
    onNavigateToDestination: (StudioDestination) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf("Todos") }
    var episodeToDelete by remember { mutableStateOf<EpisodeEntity?>(null) }

    val filteredEpisodes = episodes.filter { ep ->
        val matchesProject = selectedProjectId == null || ep.projectId == selectedProjectId
        val matchesStatus = filterStatus == "Todos" || ep.status.equals(filterStatus, ignoreCase = true)
        val matchesSearch = searchQuery.isBlank() ||
                ep.title.contains(searchQuery, ignoreCase = true) ||
                ep.description.contains(searchQuery, ignoreCase = true)
        matchesProject && matchesStatus && matchesSearch
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Bar & AI Action
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar episódios, roteiros...") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("episodes_search_field"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            onOpenAiWithPrompt("Gere a sinopse e roteiro completo com estrutura de 3 atos para um novo episódio emocionante.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(54.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("IA", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Status Filter Chips
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val statusOptions = listOf("Todos", EpisodeStatus.DRAFT.label, EpisodeStatus.IN_PRODUCTION.label, EpisodeStatus.COMPLETED.label)
                    statusOptions.forEach { status ->
                        val isSelected = filterStatus.equals(status, ignoreCase = true)
                        FilterChip(
                            selected = isSelected,
                            onClick = { filterStatus = status },
                            label = { Text(status) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NavyPrimary,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            if (filteredEpisodes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.PlayCircleOutline, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Nenhum episódio cadastrado", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Text(
                                "Adicione episódios com minutagem, status de produção e vincule cenas.",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(onClick = { onOpenNewEpisode(null) }, colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("+ Adicionar Episódio")
                            }
                        }
                    }
                }
            } else {
                items(filteredEpisodes) { episode ->
                    val projName = projects.find { it.id == episode.projectId }?.name ?: "Projeto Geral"
                    EpisodeItemCard(
                        episode = episode,
                        projectName = projName,
                        onEdit = { onOpenNewEpisode(episode) },
                        onDelete = { episodeToDelete = episode }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { onOpenNewEpisode(null) },
            containerColor = NavyPrimary,
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp).testTag("fab_new_episode")
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Novo Episódio")
        }
    }

    if (episodeToDelete != null) {
        AlertDialog(
            onDismissRequest = { episodeToDelete = null },
            title = { Text("Excluir Episódio") },
            text = { Text("Deseja realmente remover o episódio '${episodeToDelete?.title}'?") },
            confirmButton = {
                Button(
                    onClick = {
                        episodeToDelete?.let { onDeleteEpisode(it) }
                        episodeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { episodeToDelete = null }) { Text("Cancelar") }
            }
        )
    }

    if (showNewEpisodeDialog) {
        EpisodeFormDialog(
            editingEpisode = editingEpisode,
            projects = projects,
            seasons = seasons,
            selectedProjectId = selectedProjectId,
            onDismiss = onCloseNewEpisode,
            onSave = { ep ->
                onSaveEpisode(ep, true)
            },
            onAiSuggest = { prompt ->
                onCloseNewEpisode()
                onOpenAiWithPrompt(prompt)
            }
        )
    }
}

@Composable
fun EpisodeItemCard(
    episode: EpisodeEntity,
    projectName: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("episode_item_${episode.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("EP ${episode.episodeNumber}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(episode.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        when (episode.status) {
                                            EpisodeStatus.COMPLETED.label -> EmeraldSuccess.copy(alpha = 0.15f)
                                            EpisodeStatus.IN_PRODUCTION.label -> AmberGold.copy(alpha = 0.15f)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = episode.status,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = when (episode.status) {
                                            EpisodeStatus.COMPLETED.label -> EmeraldSuccess
                                            EpisodeStatus.IN_PRODUCTION.label -> AmberGold
                                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                                        },
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "⏱️ ${episode.duration}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = NavyPrimary)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = episode.description,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun EpisodeFormDialog(
    editingEpisode: EpisodeEntity?,
    projects: List<ProjectEntity>,
    seasons: List<SeasonEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onSave: (EpisodeEntity) -> Unit,
    onAiSuggest: (String) -> Unit
) {
    var episodeNumber by remember { mutableStateOf(editingEpisode?.episodeNumber?.toString() ?: "1") }
    var title by remember { mutableStateOf(editingEpisode?.title ?: "") }
    var description by remember { mutableStateOf(editingEpisode?.description ?: "") }
    var duration by remember { mutableStateOf(editingEpisode?.duration ?: "22 min") }
    var status by remember { mutableStateOf(editingEpisode?.status ?: EpisodeStatus.DRAFT.label) }
    var targetProjectId by remember { mutableStateOf(editingEpisode?.projectId ?: selectedProjectId ?: projects.firstOrNull()?.id ?: 1L) }
    var targetSeasonId by remember { mutableStateOf(editingEpisode?.seasonId ?: seasons.firstOrNull()?.id ?: 1L) }

    val statusOptions = listOf(EpisodeStatus.DRAFT.label, EpisodeStatus.IN_PRODUCTION.label, EpisodeStatus.COMPLETED.label)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (editingEpisode != null) "Editar Episódio" else "Novo Episódio",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Surface(
                    onClick = { onAiSuggest("Crie a sinopse e roteiro para o Episódio $episodeNumber: '$title'.") },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = NavyPrimary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gerar roteiro com IA", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = NavyPrimary))
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = episodeNumber,
                        onValueChange = { episodeNumber = it },
                        label = { Text("Nº Ep *") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = duration,
                        onValueChange = { duration = it },
                        label = { Text("Duração") },
                        placeholder = { Text("Ex: 24 min") },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(10.dp),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título do Episódio *") },
                    placeholder = { Text("Ex: O Despertar da Força Oculta") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Text("Status da Produção:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold))
                Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    statusOptions.forEach { st ->
                        val isSelected = status == st
                        FilterChip(
                            selected = isSelected,
                            onClick = { status = st },
                            label = { Text(st) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = NavyPrimary, selectedLabelColor = Color.White)
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Sinopse / Resumo do Episódio") },
                    placeholder = { Text("Acontecimentos principais, ganchos e clímax...") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val num = episodeNumber.toIntOrNull() ?: 1
                    if (title.isNotBlank()) {
                        val ep = (editingEpisode ?: EpisodeEntity(
                            projectId = targetProjectId,
                            seasonId = targetSeasonId,
                            episodeNumber = num,
                            title = title,
                            description = description,
                            duration = duration,
                            status = status
                        )).copy(
                            projectId = targetProjectId,
                            seasonId = targetSeasonId,
                            episodeNumber = num,
                            title = title,
                            description = description,
                            duration = duration,
                            status = status,
                            updatedAt = System.currentTimeMillis()
                        )
                        onSave(ep)
                    }
                },
                enabled = title.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NavyPrimary)
            ) {
                Text(if (editingEpisode != null) "Salvar" else "Cadastrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
