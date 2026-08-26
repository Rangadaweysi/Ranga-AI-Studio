package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.EpisodeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.SceneEntity
import com.example.data.model.SeasonEntity
import com.example.data.model.SeriesEntity
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeEditModal(
    episode: EpisodeEntity?,
    projects: List<ProjectEntity>,
    series: List<SeriesEntity>,
    seasons: List<SeasonEntity>,
    defaultSeriesId: Long?,
    defaultSeasonId: Long?,
    onDismiss: () -> Unit,
    onSave: (EpisodeEntity) -> Unit
) {
    val context = LocalContext.current
    val isEdit = episode != null

    var episodeNumber by remember { mutableIntStateOf(episode?.episodeNumber ?: 1) }
    var title by remember { mutableStateOf(episode?.title ?: "") }
    var description by remember { mutableStateOf(episode?.description ?: "") }
    var duration by remember { mutableStateOf(episode?.duration ?: "22 min") }
    var status by remember { mutableStateOf(episode?.status ?: "Em produção") }
    var coverUri by remember { mutableStateOf(episode?.coverUri ?: "cover_frutinhas") }
    var scenesCount by remember { mutableIntStateOf(episode?.scenesCount ?: 8) }
    var progressPercent by remember { mutableIntStateOf(episode?.progressPercent ?: 60) }

    var selectedProjectId by remember {
        mutableStateOf(episode?.projectId ?: defaultSeriesId ?: projects.firstOrNull()?.id ?: 1L)
    }
    var selectedSeasonId by remember {
        mutableStateOf(episode?.seasonId ?: defaultSeasonId ?: seasons.firstOrNull()?.id ?: 1L)
    }

    var showCoverPicker by remember { mutableStateOf(false) }
    var showAiCoverModal by remember { mutableStateOf(false) }

    val statusOptions = listOf("Rascunho", "Em produção", "Concluído", "Arquivado")

    val sampleCovers = listOf(
        "cover_frutinhas" to "Frutinhas Aventura",
        "cover_herois_escola" to "Heróis da Escola",
        "cover_reino_encantado" to "Reino Encantado",
        "cover_misterio_cidade" to "Mistério da Cidade",
        "cover_fazenda_divertida" to "Fazenda Divertida",
        "cover_piratas_kids" to "Piratas Kids",
        "cover_missao_estelar" to "Missão Estelar"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 680.dp)
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White,
            shadowElevation = 16.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Movie,
                                contentDescription = null,
                                tint = NavyPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = if (isEdit) "Editar Episódio" else "Criar Novo Episódio",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark
                            )
                            Text(
                                text = "Preencha as informações do episódio para gerenciar a produção",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF1F5F9))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = NavyDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFE2E8F0))

                // Scrollable Form Body
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Cover Selection Row
                    Text(
                        text = "Capa do Episódio",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = NavyDark
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 130.dp, height = 80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
                        ) {
                            val drawableRes = getDrawableResource(coverUri)
                            Image(
                                painter = painterResource(id = drawableRes),
                                contentDescription = "Capa Selecionada",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = { showAiCoverModal = true },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF3B82F6),
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Criar capa com IA", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                }

                                OutlinedButton(
                                    onClick = { showCoverPicker = !showCoverPicker },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyDark),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Escolher Imagem", fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Cover selector preview chips
                    if (showCoverPicker) {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(sampleCovers) { (cKey, cName) ->
                                val isSelected = coverUri == cKey
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(90.dp)
                                        .clickable {
                                            coverUri = cKey
                                            showCoverPicker = false
                                        }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 90.dp, height = 55.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) Color(0xFF3B82F6) else Color(0xFFCBD5E1),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Image(
                                            painter = painterResource(id = getDrawableResource(cKey)),
                                            contentDescription = cName,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                    Text(
                                        text = cName,
                                        fontSize = 10.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = NavyDark,
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Episode Number and Title Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = if (episodeNumber > 0) episodeNumber.toString() else "",
                            onValueChange = {
                                episodeNumber = it.toIntOrNull() ?: 1
                            },
                            label = { Text("Número do episódio", fontSize = 12.sp) },
                            placeholder = { Text("Ex: 1") },
                            modifier = Modifier
                                .width(130.dp)
                                .testTag("episode_number_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Título do episódio", fontSize = 12.sp) },
                            placeholder = { Text("Ex: O Desaparecimento") },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("episode_title_input"),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                    }

                    // Series and Season Selectors
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Série",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyDark,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            var seriesDropdownOpen by remember { mutableStateOf(false) }
                            val currentSeries = series.find { it.id == selectedProjectId } ?: series.firstOrNull()

                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = { seriesDropdownOpen = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyDark)
                                ) {
                                    Text(
                                        text = currentSeries?.title ?: "Aventuras das Frutas",
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )
                                    Text("▼", fontSize = 10.sp, color = Color(0xFF64748B))
                                }
                                DropdownMenu(
                                    expanded = seriesDropdownOpen,
                                    onDismissRequest = { seriesDropdownOpen = false }
                                ) {
                                    series.forEach { s ->
                                        DropdownMenuItem(
                                            text = { Text(s.title, fontSize = 13.sp) },
                                            onClick = {
                                                selectedProjectId = s.id
                                                seriesDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Temporada",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = NavyDark,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            var seasonDropdownOpen by remember { mutableStateOf(false) }
                            val currentSeason = seasons.find { it.id == selectedSeasonId } ?: seasons.firstOrNull()

                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedButton(
                                    onClick = { seasonDropdownOpen = true },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyDark)
                                ) {
                                    Text(
                                        text = "${currentSeason?.title ?: "Temporada 1"}",
                                        fontSize = 13.sp,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )
                                    Text("▼", fontSize = 10.sp, color = Color(0xFF64748B))
                                }
                                DropdownMenu(
                                    expanded = seasonDropdownOpen,
                                    onDismissRequest = { seasonDropdownOpen = false }
                                ) {
                                    seasons.forEach { sea ->
                                        DropdownMenuItem(
                                            text = { Text("${sea.title} (${sea.synopsis})", fontSize = 13.sp) },
                                            onClick = {
                                                selectedSeasonId = sea.id
                                                seasonDropdownOpen = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Description / Synopsis
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Sinopse / Descrição curta", fontSize = 12.sp) },
                        placeholder = { Text("Ex: Tudo começa quando Carlos some misteriosamente da cidade e seus amigos começam a busca.") },
                        minLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("episode_desc_input"),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            unfocusedBorderColor = Color(0xFFCBD5E1)
                        )
                    )

                    // Duration and Status Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = duration,
                            onValueChange = { duration = it },
                            label = { Text("Duração", fontSize = 12.sp) },
                            placeholder = { Text("Ex: 22 min") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )

                        OutlinedTextField(
                            value = scenesCount.toString(),
                            onValueChange = { scenesCount = it.toIntOrNull() ?: 8 },
                            label = { Text("Nº de cenas", fontSize = 12.sp) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF3B82F6),
                                unfocusedBorderColor = Color(0xFFCBD5E1)
                            )
                        )
                    }

                    // Status Chips
                    Column {
                        Text(
                            text = "Status da Produção",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = NavyDark,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            statusOptions.forEach { st ->
                                val isSelected = status == st
                                FilterChip(
                                    selected = isSelected,
                                    onClick = {
                                        status = st
                                        progressPercent = when (st) {
                                            "Concluído" -> 100
                                            "Em produção" -> 60
                                            "Rascunho" -> 20
                                            else -> 0
                                        }
                                    },
                                    label = { Text(st, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NavyPrimary,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), color = Color(0xFFE2E8F0))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B)),
                        modifier = Modifier.height(44.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (title.isBlank()) {
                                Toast.makeText(context, "Insira um título para o episódio", Toast.LENGTH_SHORT).show()
                                return@Button
                            }
                            val ep = (episode ?: EpisodeEntity(
                                projectId = selectedProjectId,
                                seasonId = selectedSeasonId,
                                episodeNumber = episodeNumber,
                                title = title,
                                description = description,
                                duration = duration,
                                status = status,
                                coverUri = coverUri,
                                scenesCount = scenesCount,
                                progressPercent = progressPercent
                            )).copy(
                                projectId = selectedProjectId,
                                seasonId = selectedSeasonId,
                                episodeNumber = episodeNumber,
                                title = title,
                                description = description,
                                duration = duration,
                                status = status,
                                coverUri = coverUri,
                                scenesCount = scenesCount,
                                progressPercent = progressPercent
                            )
                            onSave(ep)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NavyDark,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("save_episode_button")
                    ) {
                        Text(if (isEdit) "Salvar Alterações" else "Criar Episódio", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // AI Cover Generator Sub-Modal
    if (showAiCoverModal) {
        EpisodeAiCoverDialog(
            initialPrompt = "Crie uma capa para o episódio $episodeNumber ($title) de uma série infantil em 3D sobre frutas que vivem em uma cidade.",
            onDismiss = { showAiCoverModal = false },
            onApplyCover = { newCover ->
                coverUri = newCover
                showAiCoverModal = false
            }
        )
    }
}

@Composable
fun EpisodeAiCoverDialog(
    initialPrompt: String,
    onDismiss: () -> Unit,
    onApplyCover: (String) -> Unit
) {
    var prompt by remember { mutableStateOf(initialPrompt) }
    var isGenerating by remember { mutableStateOf(false) }
    var currentCoverKey by remember { mutableStateOf("cover_frutinhas") }
    var historyIndex by remember { mutableIntStateOf(0) }

    val generatedStyles = listOf("cover_frutinhas", "cover_herois_escola", "cover_reino_encantado", "cover_piratas_kids", "cover_misterio_cidade")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .widthIn(max = 540.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp)),
            color = Color.White,
            shadowElevation = 20.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6)
                        )
                        Text(
                            text = "Criar Capa com IA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    label = { Text("Prompt de Geração da Capa", fontSize = 12.sp) },
                    placeholder = { Text("Descreva o visual da capa do episódio...") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Generated Preview Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0F172A)),
                    contentAlignment = Alignment.Center
                ) {
                    if (isGenerating) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(color = Color(0xFF38BDF8), modifier = Modifier.size(36.dp))
                            Text("Gerando ilustração com IA...", color = Color.White, fontSize = 12.sp)
                        }
                    } else {
                        Image(
                            painter = painterResource(id = getDrawableResource(currentCoverKey)),
                            contentDescription = "Capa Gerada",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons: Gerar novamente, Editar, Usar capa, Excluir
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            isGenerating = true
                            historyIndex = (historyIndex + 1) % generatedStyles.size
                            currentCoverKey = generatedStyles[historyIndex]
                            isGenerating = false
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Gerar novamente", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { onApplyCover(currentCoverKey) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Usar capa", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodePreviewModal(
    episode: EpisodeEntity,
    scenes: List<SceneEntity>,
    onDismiss: () -> Unit
) {
    var isPlaying by remember { mutableStateOf(false) }
    var currentSceneIndex by remember { mutableIntStateOf(0) }
    var playbackProgress by remember { mutableFloatStateOf(0.35f) }
    var volume by remember { mutableFloatStateOf(0.85f) }

    val episodeScenes = scenes.ifEmpty {
        listOf(
            SceneEntity(
                episodeId = episode.id,
                projectId = episode.projectId,
                name = "Abertura e Desaparecimento",
                imageUri = "scene_forest_run_1787050741889",
                scenarioName = "Rua Central da Cidade",
                duration = "04:30"
            ),
            SceneEntity(
                episodeId = episode.id,
                projectId = episode.projectId,
                name = "A Investigação no Pomar",
                imageUri = "scene_school_mystery_1787050758639",
                scenarioName = "Casa do António",
                duration = "04:10"
            ),
            SceneEntity(
                episodeId = episode.id,
                projectId = episode.projectId,
                name = "A Discussão e Pistas",
                imageUri = "scene_apple_banana_kitchen_1787050705242",
                scenarioName = "Parque Central",
                duration = "03:50"
            )
        )
    }

    val activeScene = episodeScenes.getOrNull(currentSceneIndex) ?: episodeScenes.first()

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (isPlaying) {
                delay(400)
                playbackProgress = (playbackProgress + 0.02f)
                if (playbackProgress >= 1f) {
                    playbackProgress = 0f
                    currentSceneIndex = (currentSceneIndex + 1) % episodeScenes.size
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 840.dp)
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color(0xFF0F172A),
            shadowElevation = 24.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Pré-visualização: EP ${String.format("%02d", episode.episodeNumber)} — ${episode.title}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF22C55E))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text("1080p Full HD", fontSize = 10.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Cinema Stage Screen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = getDrawableResource(activeScene.imageUri ?: episode.coverUri ?: "cover_frutinhas")),
                        contentDescription = "Cena em Reprodução",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // On-screen Scene Title HUD
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(14.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.65f))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Cena ${String.format("%02d", currentSceneIndex + 1)}: ${activeScene.name} • ${activeScene.scenarioName}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Watermark / Studio Branding
                    Text(
                        text = "RANGA AI STUDIO",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Timeline Scrubber
                Slider(
                    value = playbackProgress,
                    onValueChange = { playbackProgress = it },
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF38BDF8),
                        activeTrackColor = Color(0xFF0284C7),
                        inactiveTrackColor = Color(0xFF334155)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Controls Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = { isPlaying = !isPlaying },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7))
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pausar" else "Reproduzir",
                                tint = Color.White
                            )
                        }

                        IconButton(
                            onClick = {
                                isPlaying = false
                                playbackProgress = 0f
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Stop, contentDescription = "Parar", tint = Color(0xFF94A3B8))
                        }

                        Text(
                            text = "${String.format("%02d", (playbackProgress * 22).toInt())}:00 / ${episode.duration}",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }

                    // Scene Quick Switcher Buttons
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        items(episodeScenes.indices.toList()) { idx ->
                            val isSelected = currentSceneIndex == idx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isSelected) Color(0xFF0284C7) else Color(0xFF1E293B))
                                    .clickable {
                                        currentSceneIndex = idx
                                        playbackProgress = 0f
                                    }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Cena ${idx + 1}",
                                    color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Volume & Fullscreen
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Volume", tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp))
                        IconButton(onClick = { /* Fullscreen */ }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Tela Cheia", tint = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeExportModal(
    episode: EpisodeEntity,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedFormat by remember { mutableStateOf("Vídeo (MP4)") }
    var isExporting by remember { mutableStateOf(false) }
    var exportSuccess by remember { mutableStateOf(false) }

    val formats = listOf(
        "Vídeo (MP4 1080p)" to "Renderização completa de vídeo com animações e trilhas",
        "Áudio Master (WAV)" to "Master de voz, música e efeitos sonoros mixados",
        "Roteiro (PDF / Fountain)" to "Documento de roteiro cinematográfico formatado",
        "Pacote de Imagens (ZIP)" to "Todas as capas, cenários e poses em alta resolução",
        "Projeto RANGA (.ranga)" to "Arquivo completo do projeto para backup e intercâmbio"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .widthIn(max = 540.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp)),
            color = Color.White,
            shadowElevation = 18.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = NavyPrimary)
                        Text(
                            text = "Exportar Episódio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyDark
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Escolha o formato de saída para exportar “${episode.title}”:",
                    fontSize = 13.sp,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    formats.forEach { (fmt, desc) ->
                        val isSelected = selectedFormat == fmt
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFormat = fmt },
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC)
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) Color(0xFF3B82F6) else Color(0xFFE2E8F0)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = fmt,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) NavyDark else Color(0xFF334155)
                                    )
                                    Text(text = desc, fontSize = 11.sp, color = Color(0xFF64748B))
                                }
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF3B82F6),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (isExporting) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color(0xFF3B82F6))
                        Text("Preparando exportação de $selectedFormat...", fontSize = 12.sp, color = Color(0xFF64748B))
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        OutlinedButton(
                            onClick = onDismiss,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                isExporting = true
                                Toast.makeText(context, "Exportando $selectedFormat...", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Iniciar Exportação", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeScriptModal(
    episode: EpisodeEntity,
    initialScript: String = "",
    onDismiss: () -> Unit,
    onSaveScript: (String) -> Unit,
    onAiGenerateScript: (String) -> Unit
) {
    val context = LocalContext.current
    var scriptText by remember {
        mutableStateOf(
            if (initialScript.isNotBlank()) initialScript else ""
        )
    }
    var isSaving by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 780.dp)
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(20.dp)),
            color = Color.White,
            shadowElevation = 20.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(22.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Description, contentDescription = null, tint = NavyPrimary)
                        }
                        Column {
                            Text(
                                text = "Roteiro: ${episode.title}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = NavyDark
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isSaving) AmberGold else EmeraldSuccess)
                                )
                                Text(
                                    text = if (isSaving) "Salvando..." else "Salvo",
                                    fontSize = 11.sp,
                                    color = if (isSaving) AmberGold else EmeraldSuccess,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action Bar: AI Roteiro, Roteiro Branco, Importar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onAiGenerateScript("Crie o roteiro completo para o episódio ${episode.episodeNumber} - ${episode.title} com diálogos e ações dos personagens.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Criar roteiro com IA", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = {
                            // Roteiro Branco: clear completely
                            scriptText = ""
                            Toast.makeText(context, "Editor limpo para escrita manual", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyDark)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Roteiro Branco", fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            Toast.makeText(context, "Importação de arquivo pronta para integração", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF64748B))
                    ) {
                        Text("Importar roteiro", fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Text Script Editor Area
                OutlinedTextField(
                    value = scriptText,
                    onValueChange = {
                        scriptText = it
                        isSaving = true
                    },
                    placeholder = {
                        Text("Escreva seu roteiro aqui...\n\nExemplo:\nCENA 01 - RUA CENTRAL - DIA\n\nANTÓNIO\n(olhando para todos os lados)\nOnde será que o Carlos foi parar?")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("script_editor_input"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF3B82F6),
                        unfocusedBorderColor = Color(0xFFE2E8F0),
                        focusedContainerColor = Color(0xFFF8FAFC),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Footer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${scriptText.split("\\s+".toRegex()).filter { it.isNotBlank() }.size} palavras",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(onClick = onDismiss, shape = RoundedCornerShape(8.dp)) {
                            Text("Fechar")
                        }
                        Button(
                            onClick = {
                                isSaving = false
                                onSaveScript(scriptText)
                                Toast.makeText(context, "Roteiro salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = NavyDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Salvar Roteiro", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

internal fun getDrawableResource(coverUri: String?): Int {
    val key = (coverUri ?: "").lowercase()
    return when {
        key.contains("frutinha") || key.contains("fruta") -> R.drawable.cover_frutinhas
        key.contains("heroi") || key.contains("escola") -> R.drawable.cover_herois_escola
        key.contains("reino") || key.contains("encantado") || key.contains("magia") -> R.drawable.cover_reino_encantado
        key.contains("misterio") || key.contains("cidade") -> R.drawable.cover_misterio_cidade
        key.contains("fazenda") -> R.drawable.cover_fazenda_divertida
        key.contains("pirata") -> R.drawable.cover_piratas_kids
        key.contains("missao") || key.contains("estelar") || key.contains("robot") -> R.drawable.cover_missao_estelar
        key.contains("dino") -> R.drawable.cover_mundo_dinossauros
        key.contains("forest") || key.contains("floresta") -> R.drawable.scene_forest_run_1787050741889
        key.contains("kitchen") || key.contains("cozinha") -> R.drawable.scene_apple_banana_kitchen_1787050705242
        key.contains("school") -> R.drawable.scene_school_mystery_1787050758639
        else -> R.drawable.cover_frutinhas
    }
}

