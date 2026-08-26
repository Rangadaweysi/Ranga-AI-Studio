package com.example.ui.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.model.ProjectEntity
import com.example.data.model.RangaCreationEntity
import com.example.data.model.RoteiroBrancoItemEntity
import com.example.data.model.ScenarioEntity
import com.example.ui.navigation.StudioDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class CreationFilter(val label: String) {
    ALL("Todos"),
    CHARACTERS("Personagens"),
    SCENARIOS("Cenários"),
    ENVIRONMENTS("Ambientes"),
    FAVORITES("Favoritos"),
    ROTEIRO("No Roteiro")
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RangaCreationScreen(
    creations: List<RangaCreationEntity>,
    roteiroBrancoItems: List<RoteiroBrancoItemEntity>,
    projects: List<ProjectEntity>,
    characters: List<CharacterEntity>,
    scenarios: List<ScenarioEntity>,
    selectedProjectId: Long?,
    onSaveCreation: (RangaCreationEntity) -> Unit,
    onUpdateCreation: (RangaCreationEntity) -> Unit,
    onDeleteCreation: (RangaCreationEntity) -> Unit,
    onToggleFavoriteCreation: (RangaCreationEntity) -> Unit,
    onToggleRoteiroBranco: (RangaCreationEntity) -> Unit,
    onUseAsCharacter: (RangaCreationEntity, String, String, String, Long?) -> Unit,
    onUseAsScenario: (RangaCreationEntity, String, String, String, Long?) -> Unit,
    onAddToRoteiroBranco: (RangaCreationEntity, String) -> Unit,
    onDeleteRoteiroBrancoItem: (RoteiroBrancoItemEntity) -> Unit,
    onSaveCharacter: (String, String, String, String, String, String, String, String, Long?, String?) -> Unit,
    onOpenFullCharacterCreator: (CharacterEntity?) -> Unit,
    onOpenAiWithPrompt: (String) -> Unit,
    onNavigateToDestination: (StudioDestination) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Prompt & Configuration States
    var textPrompt by remember {
        mutableStateOf("Crie um garoto adolescente em uma cidade, estilo cartoon 3D, camiseta azul, moletom, cabelos castanhos e olhar expressivo.")
    }
    var promptTitle by remember { mutableStateOf("Garoto adolescente") }
    var selectedType by remember { mutableStateOf("Personagem") }
    var selectedStyle by remember { mutableStateOf("Cartoon 3D") }
    var selectedAspectRatio by remember { mutableStateOf("16:9") }
    var selectedQuality by remember { mutableStateOf("Alta Definição (3D)") }
    var selectedProjectIdForCreation by remember { mutableStateOf(selectedProjectId ?: projects.firstOrNull()?.id ?: 1L) }

    // Reference Image States
    var hasReferenceImage by remember { mutableStateOf(true) }
    var referenceImageUri by remember { mutableStateOf("char_preview_boy_1787037236644") }
    var referenceImageName by remember { mutableStateOf("referencia_garoto.png") }
    var referenceInstruction by remember {
        mutableStateOf("Crie um personagem baseado nesta imagem de referência, preservando a paleta de cores e o moletom azul.")
    }

    // Generation Progress State
    var isGenerating by remember { mutableStateOf(false) }
    var activeCreationSelection by remember {
        mutableStateOf<RangaCreationEntity?>(creations.firstOrNull())
    }

    // Library Filtering & Search
    var currentFilter by remember { mutableStateOf(CreationFilter.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    // Modals
    var dialogDetailsCreation by remember { mutableStateOf<RangaCreationEntity?>(null) }
    var dialogUseAsCharCreation by remember { mutableStateOf<RangaCreationEntity?>(null) }
    var dialogUseAsScenCreation by remember { mutableStateOf<RangaCreationEntity?>(null) }
    var dialogAddToRoteiroCreation by remember { mutableStateOf<RangaCreationEntity?>(null) }
    var showRoteiroBrancoViewer by remember { mutableStateOf(false) }

    val creationTypes = listOf("Personagem", "Cenário", "Ambiente", "Objeto", "Outro")
    val creationStyles = listOf("Cartoon 3D", "Cartoon", "Anime", "Realista", "Cinemático", "Fantasia", "2D")
    val aspectRatios = listOf("16:9", "9:16", "1:1", "4:3")

    val promptPresets = listOf(
        "Garoto adolescente com moletom azul e expressão viva" to "Personagem",
        "Banana estilosa 3D com moletom amarelo e capuz" to "Personagem",
        "Casa aconchegante iluminada ao anoitecer com jardim" to "Cenário",
        "Floresta mágica com raios de sol e trilha de pedras" to "Cenário",
        "Maçã vendedor clássico com terno e bigode elegante" to "Personagem",
        "Sala de estar moderna com iluminação quente e sofá" to "Ambiente"
    )

    // Filter creations list
    val filteredCreations = remember(creations, currentFilter, searchQuery, selectedProjectId) {
        creations.filter { creation ->
            val matchesFilter = when (currentFilter) {
                CreationFilter.ALL -> true
                CreationFilter.CHARACTERS -> creation.creationType == "Personagem"
                CreationFilter.SCENARIOS -> creation.creationType == "Cenário"
                CreationFilter.ENVIRONMENTS -> creation.creationType == "Ambiente"
                CreationFilter.FAVORITES -> creation.isFavorite
                CreationFilter.ROTEIRO -> creation.isInRoteiroBranco
            }
            val matchesSearch = searchQuery.isBlank() ||
                    creation.title.contains(searchQuery, ignoreCase = true) ||
                    creation.prompt.contains(searchQuery, ignoreCase = true) ||
                    creation.style.contains(searchQuery, ignoreCase = true)

            matchesFilter && matchesSearch
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .testTag("ranga_creation_screen")
    ) {
        val isWideScreen = maxWidth >= 1024.dp
        val isMediumScreen = maxWidth in 700.dp..1023.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isWideScreen) 24.dp else 16.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // ==========================================
            // 1. TOP BAR / SCREEN HEADER
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF0052FF),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Criação RANGA",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF091124),
                                letterSpacing = (-0.5).sp
                            )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0052FF)
                        ) {
                            Text(
                                text = "GERADOR VISUAL",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Text(
                        text = "Gere personagens, cenários e conceitos visuais para suas séries e roteiros com IA.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 12.5.sp
                        ),
                        modifier = Modifier.padding(start = 44.dp, top = 2.dp)
                    )
                }

                // Quick Header Actions
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { showRoteiroBrancoViewer = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        modifier = Modifier.testTag("open_roteiro_branco_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = Color(0xFF0052FF),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Roteiro Branco (${roteiroBrancoItems.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF091124)
                        )
                    }

                    Button(
                        onClick = {
                            onOpenAiWithPrompt("Sugira 3 ideias visuais inovadoras de personagens e cenários para a série.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF091124)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Assistente IA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ==========================================
            // 2. MAIN WORKSPACE (2 Columns on Wide Screens)
            // ==========================================
            if (isWideScreen) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // LEFT COLUMN: Creation Studio Controls
                    Column(modifier = Modifier.weight(0.48f)) {
                        CreationStudioControlsCard(
                            promptTitle = promptTitle,
                            onPromptTitleChange = { promptTitle = it },
                            textPrompt = textPrompt,
                            onTextPromptChange = { textPrompt = it },
                            selectedType = selectedType,
                            onSelectType = { selectedType = it },
                            selectedStyle = selectedStyle,
                            onSelectStyle = { selectedStyle = it },
                            selectedAspectRatio = selectedAspectRatio,
                            onSelectAspectRatio = { selectedAspectRatio = it },
                            creationTypes = creationTypes,
                            creationStyles = creationStyles,
                            aspectRatios = aspectRatios,
                            hasReferenceImage = hasReferenceImage,
                            referenceImageName = referenceImageName,
                            referenceImageUri = referenceImageUri,
                            referenceInstruction = referenceInstruction,
                            onReferenceInstructionChange = { referenceInstruction = it },
                            onToggleReferenceImage = { hasReferenceImage = !hasReferenceImage },
                            onSelectPreset = { preset, type ->
                                textPrompt = preset
                                promptTitle = preset.take(24)
                                selectedType = type
                            },
                            promptPresets = promptPresets,
                            isGenerating = isGenerating,
                            onGenerate = {
                                coroutineScope.launch {
                                    isGenerating = true
                                    delay(1800) // realistic synthesis feel
                                    val assignedImage = when (selectedType) {
                                        "Personagem" -> if (textPrompt.contains("banana", ignoreCase = true)) {
                                            "creation_banana_hoodie_1787395173917"
                                        } else if (textPrompt.contains("maçã", ignoreCase = true) || textPrompt.contains("maca", ignoreCase = true)) {
                                            "creation_apple_merchant_1787395195191"
                                        } else {
                                            "char_preview_boy_1787037236644"
                                        }
                                        "Cenário" -> if (textPrompt.contains("floresta", ignoreCase = true)) {
                                            "scenario_forest_1787049320048"
                                        } else {
                                            "scenario_family_house_1787049241112"
                                        }
                                        "Ambiente" -> "creation_living_room_1787395214400"
                                        else -> "char_preview_boy_1787037236644"
                                    }

                                    val proj = projects.find { it.id == selectedProjectIdForCreation }
                                    val newCreation = RangaCreationEntity(
                                        title = promptTitle.ifBlank { "Nova Criação ${creations.size + 1}" },
                                        prompt = textPrompt,
                                        creationType = selectedType,
                                        style = selectedStyle,
                                        aspectRatio = selectedAspectRatio,
                                        imageUri = assignedImage,
                                        referenceImageUri = if (hasReferenceImage) referenceImageUri else null,
                                        projectId = selectedProjectIdForCreation,
                                        projectName = proj?.name ?: "Aventuras das Frutas",
                                        quality = selectedQuality,
                                        isFavorite = false,
                                        isInRoteiroBranco = false
                                    )
                                    onSaveCreation(newCreation)
                                    activeCreationSelection = newCreation
                                    isGenerating = false
                                    Toast.makeText(context, "Imagem gerada com sucesso na Criação RANGA!", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }

                    // RIGHT COLUMN: Active Canvas & Gallery
                    Column(modifier = Modifier.weight(0.52f)) {
                        ActiveCreationCanvas(
                            activeCreation = activeCreationSelection ?: creations.firstOrNull(),
                            projects = projects,
                            onToggleFavorite = { creation ->
                                onToggleFavoriteCreation(creation)
                                if (activeCreationSelection?.id == creation.id) {
                                    activeCreationSelection = creation.copy(isFavorite = !creation.isFavorite)
                                }
                            },
                            onToggleRoteiro = { creation ->
                                onToggleRoteiroBranco(creation)
                                if (activeCreationSelection?.id == creation.id) {
                                    activeCreationSelection = creation.copy(isInRoteiroBranco = !creation.isInRoteiroBranco)
                                }
                            },
                            onUseAsCharacterClick = { dialogUseAsCharCreation = it },
                            onUseAsScenarioClick = { dialogUseAsScenCreation = it },
                            onOpenDetails = { dialogDetailsCreation = it },
                            onDelete = {
                                onDeleteCreation(it)
                                if (activeCreationSelection?.id == it.id) {
                                    activeCreationSelection = creations.filterNot { c -> c.id == it.id }.firstOrNull()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        CreationsGallerySection(
                            creations = filteredCreations,
                            currentFilter = currentFilter,
                            onSelectFilter = { currentFilter = it },
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it },
                            selectedCreation = activeCreationSelection,
                            onSelectCreation = { activeCreationSelection = it },
                            onOpenDetails = { dialogDetailsCreation = it },
                            onToggleFavorite = { onToggleFavoriteCreation(it) },
                            onUseAsCharacter = { dialogUseAsCharCreation = it },
                            onUseAsScenario = { dialogUseAsScenCreation = it },
                            onAddToRoteiro = { dialogAddToRoteiroCreation = it },
                            onDelete = { onDeleteCreation(it) }
                        )
                    }
                }
            } else {
                // Single Column for Phone / Medium screens
                CreationStudioControlsCard(
                    promptTitle = promptTitle,
                    onPromptTitleChange = { promptTitle = it },
                    textPrompt = textPrompt,
                    onTextPromptChange = { textPrompt = it },
                    selectedType = selectedType,
                    onSelectType = { selectedType = it },
                    selectedStyle = selectedStyle,
                    onSelectStyle = { selectedStyle = it },
                    selectedAspectRatio = selectedAspectRatio,
                    onSelectAspectRatio = { selectedAspectRatio = it },
                    creationTypes = creationTypes,
                    creationStyles = creationStyles,
                    aspectRatios = aspectRatios,
                    hasReferenceImage = hasReferenceImage,
                    referenceImageName = referenceImageName,
                    referenceImageUri = referenceImageUri,
                    referenceInstruction = referenceInstruction,
                    onReferenceInstructionChange = { referenceInstruction = it },
                    onToggleReferenceImage = { hasReferenceImage = !hasReferenceImage },
                    onSelectPreset = { preset, type ->
                        textPrompt = preset
                        promptTitle = preset.take(24)
                        selectedType = type
                    },
                    promptPresets = promptPresets,
                    isGenerating = isGenerating,
                    onGenerate = {
                        coroutineScope.launch {
                            isGenerating = true
                            delay(1600)
                            val assignedImage = when (selectedType) {
                                "Personagem" -> if (textPrompt.contains("banana", ignoreCase = true)) {
                                    "creation_banana_hoodie_1787395173917"
                                } else if (textPrompt.contains("maçã", ignoreCase = true) || textPrompt.contains("maca", ignoreCase = true)) {
                                    "creation_apple_merchant_1787395195191"
                                } else {
                                    "char_preview_boy_1787037236644"
                                }
                                "Cenário" -> if (textPrompt.contains("floresta", ignoreCase = true)) {
                                    "scenario_forest_1787049320048"
                                } else {
                                    "scenario_family_house_1787049241112"
                                }
                                "Ambiente" -> "creation_living_room_1787395214400"
                                else -> "char_preview_boy_1787037236644"
                            }
                            val proj = projects.find { it.id == selectedProjectIdForCreation }
                            val newCreation = RangaCreationEntity(
                                title = promptTitle.ifBlank { "Nova Criação ${creations.size + 1}" },
                                prompt = textPrompt,
                                creationType = selectedType,
                                style = selectedStyle,
                                aspectRatio = selectedAspectRatio,
                                imageUri = assignedImage,
                                referenceImageUri = if (hasReferenceImage) referenceImageUri else null,
                                projectId = selectedProjectIdForCreation,
                                projectName = proj?.name ?: "Aventuras das Frutas",
                                quality = selectedQuality
                            )
                            onSaveCreation(newCreation)
                            activeCreationSelection = newCreation
                            isGenerating = false
                            Toast.makeText(context, "Imagem gerada com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(18.dp))

                ActiveCreationCanvas(
                    activeCreation = activeCreationSelection ?: creations.firstOrNull(),
                    projects = projects,
                    onToggleFavorite = { onToggleFavoriteCreation(it) },
                    onToggleRoteiro = { onToggleRoteiroBranco(it) },
                    onUseAsCharacterClick = { dialogUseAsCharCreation = it },
                    onUseAsScenarioClick = { dialogUseAsScenCreation = it },
                    onOpenDetails = { dialogDetailsCreation = it },
                    onDelete = { onDeleteCreation(it) }
                )

                Spacer(modifier = Modifier.height(18.dp))

                CreationsGallerySection(
                    creations = filteredCreations,
                    currentFilter = currentFilter,
                    onSelectFilter = { currentFilter = it },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedCreation = activeCreationSelection,
                    onSelectCreation = { activeCreationSelection = it },
                    onOpenDetails = { dialogDetailsCreation = it },
                    onToggleFavorite = { onToggleFavoriteCreation(it) },
                    onUseAsCharacter = { dialogUseAsCharCreation = it },
                    onUseAsScenario = { dialogUseAsScenCreation = it },
                    onAddToRoteiro = { dialogAddToRoteiroCreation = it },
                    onDelete = { onDeleteCreation(it) }
                )
            }
        }

        // ==========================================
        // 3. DIALOGS & OVERLAYS
        // ==========================================
        dialogDetailsCreation?.let { creation ->
            CreationDetailsDialog(
                creation = creation,
                projects = projects,
                onDismiss = { dialogDetailsCreation = null },
                onToggleFavorite = {
                    onToggleFavoriteCreation(creation)
                    dialogDetailsCreation = creation.copy(isFavorite = !creation.isFavorite)
                },
                onToggleRoteiro = {
                    onToggleRoteiroBranco(creation)
                    dialogDetailsCreation = creation.copy(isInRoteiroBranco = !creation.isInRoteiroBranco)
                },
                onUseAsCharacter = {
                    dialogDetailsCreation = null
                    dialogUseAsCharCreation = creation
                },
                onUseAsScenario = {
                    dialogDetailsCreation = null
                    dialogUseAsScenCreation = creation
                },
                onEditPrompt = {
                    textPrompt = creation.prompt
                    promptTitle = creation.title
                    selectedType = creation.creationType
                    selectedStyle = creation.style
                    selectedAspectRatio = creation.aspectRatio
                    dialogDetailsCreation = null
                    Toast.makeText(context, "Prompt carregado no estúdio para edição!", Toast.LENGTH_SHORT).show()
                },
                onDelete = {
                    onDeleteCreation(creation)
                    dialogDetailsCreation = null
                }
            )
        }

        dialogUseAsCharCreation?.let { creation ->
            UseAsCharacterDialog(
                creation = creation,
                projects = projects,
                onDismiss = { dialogUseAsCharCreation = null },
                onConfirm = { name, role, pers, pId ->
                    onUseAsCharacter(creation, name, role, pers, pId)
                    dialogUseAsCharCreation = null
                    Toast.makeText(context, "Personagem '$name' cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        dialogUseAsScenCreation?.let { creation ->
            UseAsScenarioDialog(
                creation = creation,
                projects = projects,
                onDismiss = { dialogUseAsScenCreation = null },
                onConfirm = { name, cat, atmos, pId ->
                    onUseAsScenario(creation, name, cat, atmos, pId)
                    dialogUseAsScenCreation = null
                    Toast.makeText(context, "Cenário '$name' cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        dialogAddToRoteiroCreation?.let { creation ->
            AddToRoteiroDialog(
                creation = creation,
                onDismiss = { dialogAddToRoteiroCreation = null },
                onConfirm = { notes ->
                    onAddToRoteiroBranco(creation, notes)
                    dialogAddToRoteiroCreation = null
                    Toast.makeText(context, "Item adicionado ao Roteiro Branco!", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showRoteiroBrancoViewer) {
            RoteiroBrancoViewerModal(
                items = roteiroBrancoItems,
                onDismiss = { showRoteiroBrancoViewer = false },
                onDeleteItem = { item -> onDeleteRoteiroBrancoItem(item) }
            )
        }
    }
}

// ==========================================
// SUBCOMPONENT: Studio Controls Card (Left)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreationStudioControlsCard(
    promptTitle: String,
    onPromptTitleChange: (String) -> Unit,
    textPrompt: String,
    onTextPromptChange: (String) -> Unit,
    selectedType: String,
    onSelectType: (String) -> Unit,
    selectedStyle: String,
    onSelectStyle: (String) -> Unit,
    selectedAspectRatio: String,
    onSelectAspectRatio: (String) -> Unit,
    creationTypes: List<String>,
    creationStyles: List<String>,
    aspectRatios: List<String>,
    hasReferenceImage: Boolean,
    referenceImageName: String,
    referenceImageUri: String,
    referenceInstruction: String,
    onReferenceInstructionChange: (String) -> Unit,
    onToggleReferenceImage: () -> Unit,
    onSelectPreset: (String, String) -> Unit,
    promptPresets: List<Pair<String, String>>,
    isGenerating: Boolean,
    onGenerate: () -> Unit
) {
    val context = LocalContext.current
    val refImgRes = resolveCreationImageRes(context, referenceImageUri)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            // Header of the card
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Estúdio de Criação",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF091124)
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "Gemini Vision Pro",
                        fontSize = 10.5.sp,
                        color = Color(0xFF475569),
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Tipo de Criação (Chips)
            Text(
                text = "TIPO DE ELEMENTO",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    fontSize = 10.sp,
                    letterSpacing = 0.5.sp
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                creationTypes.forEach { type ->
                    val isSelected = selectedType == type
                    Surface(
                        onClick = { onSelectType(type) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFF0052FF) else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = type,
                            color = if (isSelected) Color.White else Color(0xFF334155),
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2. Estilo Visual & Proporção
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Style Selector
                Column(modifier = Modifier.weight(1f)) {
                    Text("ESTILO VISUAL", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        creationStyles.take(4).forEach { st ->
                            val isSel = selectedStyle == st
                            Surface(
                                onClick = { onSelectStyle(st) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF091124) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = st,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Aspect Ratio Selector
                Column(modifier = Modifier.weight(0.7f)) {
                    Text("PROPORÇÃO", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        aspectRatios.forEach { ratio ->
                            val isSel = selectedAspectRatio == ratio
                            Surface(
                                onClick = { onSelectAspectRatio(ratio) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = ratio,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. Imagem de Referência (Drag / Upload / Remove)
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (hasReferenceImage) Color(0xFFF8FAFC) else Color(0xFFFAFAFA),
                border = BorderStroke(1.dp, if (hasReferenceImage) Color(0xFF93C5FD) else Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = if (hasReferenceImage) Color(0xFF0052FF) else Color(0xFF64748B),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Imagem de Referência",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF091124)
                            )
                        }

                        TextButton(
                            onClick = onToggleReferenceImage,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (hasReferenceImage) "Remover" else "+ Carregar Imagem",
                                fontSize = 11.sp,
                                color = if (hasReferenceImage) Color(0xFFE11D48) else Color(0xFF0052FF),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (hasReferenceImage) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = refImgRes),
                                contentDescription = "Referência",
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = referenceImageName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF091124)
                                )
                                Text(
                                    text = "Base para consistência de estilo e cores",
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = referenceInstruction,
                            onValueChange = onReferenceInstructionChange,
                            placeholder = { Text("Ex: Transforme esta foto em desenho cartoon 3D...", fontSize = 11.5.sp) },
                            textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Campo de Título e Prompt Principal
            OutlinedTextField(
                value = promptTitle,
                onValueChange = onPromptTitleChange,
                label = { Text("Nome da Criação / Título", fontSize = 12.sp) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = textPrompt,
                onValueChange = onTextPromptChange,
                label = { Text("Prompt Descritivo da Imagem", fontSize = 12.sp) },
                minLines = 4,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ranga_prompt_input"),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                )
            )

            // Characters count & AI magic assist
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${textPrompt.length} caracteres",
                    fontSize = 10.5.sp,
                    color = Color(0xFF94A3B8)
                )

                TextButton(
                    onClick = {
                        onTextPromptChange("$textPrompt, iluminação de cinema 3D, texturas ricas em detalhes, render estilo animação Pixar, cores vibrantes")
                    },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Melhorar Prompt ✨", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0052FF))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Suggestions Chips
            Text(
                text = "SUGESTÕES RÁPIDAS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    fontSize = 9.5.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                promptPresets.take(4).forEach { (preset, type) ->
                    Surface(
                        onClick = { onSelectPreset(preset, type) },
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = preset.take(30) + "...",
                            fontSize = 10.5.sp,
                            color = Color(0xFF475569),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Generate Button
            Button(
                onClick = onGenerate,
                enabled = !isGenerating && textPrompt.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("generate_image_button")
            ) {
                if (isGenerating) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Gerando Imagem com IA...", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                } else {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gerar Imagem com IA ✨", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

// ==========================================
// SUBCOMPONENT: Active Creation Canvas Preview
// ==========================================
@Composable
fun ActiveCreationCanvas(
    activeCreation: RangaCreationEntity?,
    projects: List<ProjectEntity>,
    onToggleFavorite: (RangaCreationEntity) -> Unit,
    onToggleRoteiro: (RangaCreationEntity) -> Unit,
    onUseAsCharacterClick: (RangaCreationEntity) -> Unit,
    onUseAsScenarioClick: (RangaCreationEntity) -> Unit,
    onOpenDetails: (RangaCreationEntity) -> Unit,
    onDelete: (RangaCreationEntity) -> Unit
) {
    val context = LocalContext.current
    if (activeCreation == null) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma criação selecionada", color = Color(0xFF94A3B8), fontSize = 13.sp)
            }
        }
        return
    }

    val imageRes = resolveCreationImageRes(context, activeCreation.imageUri)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with info & badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = activeCreation.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF091124)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEFF6FF)
                    ) {
                        Text(
                            text = activeCreation.creationType,
                            color = Color(0xFF0052FF),
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { onToggleFavorite(activeCreation) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (activeCreation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favoritar",
                            tint = if (activeCreation.isFavorite) Color(0xFFE11D48) else Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = { onOpenDetails(activeCreation) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Mais opções",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Visual Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF091124))
                    .clickable { onOpenDetails(activeCreation) }
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = activeCreation.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top Left Overlay: Style & Aspect
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xCC091124)
                    ) {
                        Text(
                            text = activeCreation.style,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xCC0052FF)
                    ) {
                        Text(
                            text = activeCreation.aspectRatio,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Bottom Status Badge (Roteiro Branco)
                if (activeCreation.isInRoteiroBranco) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 8.dp),
                        color = Color(0xFF0052FF),
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = Color.White, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("No Roteiro Branco", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onUseAsCharacterClick(activeCreation) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Usar como Personagem", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onUseAsScenarioClick(activeCreation) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Usar como Cenário", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            OutlinedButton(
                onClick = { onToggleRoteiro(activeCreation) },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (activeCreation.isInRoteiroBranco) Color(0xFFEFF6FF) else Color.Transparent
                ),
                border = BorderStroke(1.dp, if (activeCreation.isInRoteiroBranco) Color(0xFF0052FF) else Color(0xFFCBD5E1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = if (activeCreation.isInRoteiroBranco) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = null,
                    tint = if (activeCreation.isInRoteiroBranco) Color(0xFF0052FF) else Color(0xFF334155),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (activeCreation.isInRoteiroBranco) "Remover do Roteiro Branco" else "Adicionar ao Roteiro Branco",
                    fontSize = 12.sp,
                    color = if (activeCreation.isInRoteiroBranco) Color(0xFF0052FF) else Color(0xFF334155),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

// ==========================================
// SUBCOMPONENT: Creations Gallery & Grid
// ==========================================
@Composable
fun CreationsGallerySection(
    creations: List<RangaCreationEntity>,
    currentFilter: CreationFilter,
    onSelectFilter: (CreationFilter) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCreation: RangaCreationEntity?,
    onSelectCreation: (RangaCreationEntity) -> Unit,
    onOpenDetails: (RangaCreationEntity) -> Unit,
    onToggleFavorite: (RangaCreationEntity) -> Unit,
    onUseAsCharacter: (RangaCreationEntity) -> Unit,
    onUseAsScenario: (RangaCreationEntity) -> Unit,
    onAddToRoteiro: (RangaCreationEntity) -> Unit,
    onDelete: (RangaCreationEntity) -> Unit
) {
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header & Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Galeria de Criações",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF091124)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = "${creations.size} imagens",
                            fontSize = 11.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Buscar criação por título, prompt ou estilo...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(18.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchQueryChange("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpar", modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Tabs Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CreationFilter.values().forEach { filter ->
                    val isSelected = currentFilter == filter
                    Surface(
                        onClick = { onSelectFilter(filter) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSelected) Color(0xFF091124) else Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = filter.label,
                            color = if (isSelected) Color.White else Color(0xFF475569),
                            fontSize = 11.5.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Creation Grid / Cards
            if (creations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma criação visual encontrada com este filtro.",
                        color = Color(0xFF94A3B8),
                        fontSize = 13.sp
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    creations.forEach { item ->
                        val isSelected = selectedCreation?.id == item.id
                        val imgRes = resolveCreationImageRes(context, item.imageUri)
                        var showMenu by remember { mutableStateOf(false) }

                        Surface(
                            onClick = { onSelectCreation(item) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF091124))
                                ) {
                                    Image(
                                        painter = painterResource(id = imgRes),
                                        contentDescription = item.title,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.5.sp,
                                            color = Color(0xFF091124)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFE2E8F0)
                                        ) {
                                            Text(
                                                text = item.style,
                                                fontSize = 9.5.sp,
                                                color = Color(0xFF475569),
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Text(
                                        text = item.prompt,
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF64748B),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${item.creationType} • ${item.aspectRatio}",
                                            fontSize = 10.5.sp,
                                            color = Color(0xFF94A3B8),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (item.isInRoteiroBranco) {
                                            Surface(
                                                shape = RoundedCornerShape(3.dp),
                                                color = Color(0xFF0052FF)
                                            ) {
                                                Text(
                                                    text = "Roteiro",
                                                    color = Color.White,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onToggleFavorite(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (item.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favoritar",
                                            tint = if (item.isFavorite) Color(0xFFE11D48) else Color(0xFF94A3B8),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }

                                    Box {
                                        IconButton(
                                            onClick = { showMenu = true },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MoreVert,
                                                contentDescription = "Opções",
                                                tint = Color(0xFF64748B),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }

                                        DropdownMenu(
                                            expanded = showMenu,
                                            onDismissRequest = { showMenu = false }
                                        ) {
                                            DropdownMenuItem(
                                                text = { Text("Ver Detalhes") },
                                                onClick = {
                                                    showMenu = false
                                                    onOpenDetails(item)
                                                },
                                                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Usar como Personagem") },
                                                onClick = {
                                                    showMenu = false
                                                    onUseAsCharacter(item)
                                                },
                                                leadingIcon = { Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Usar como Cenário") },
                                                onClick = {
                                                    showMenu = false
                                                    onUseAsScenario(item)
                                                },
                                                leadingIcon = { Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                            )
                                            DropdownMenuItem(
                                                text = { Text("Adicionar ao Roteiro Branco") },
                                                onClick = {
                                                    showMenu = false
                                                    onAddToRoteiro(item)
                                                },
                                                leadingIcon = { Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                            )
                                            HorizontalDivider()
                                            DropdownMenuItem(
                                                text = { Text("Excluir Criação", color = Color(0xFFE11D48)) },
                                                onClick = {
                                                    showMenu = false
                                                    onDelete(item)
                                                },
                                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFE11D48), modifier = Modifier.size(16.dp)) }
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
}
