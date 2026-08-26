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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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

// Models for visual choices
data class CharacterTypeCard(
    val id: String,
    val title: String,
    val emoji: String,
    val drawableRes: Int? = null,
    val tag: String = ""
)

@Composable
fun CharacterCreatorModal(
    editingCharacter: CharacterEntity?,
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, String, String, String, Long?, String?) -> Unit,
    onAiSuggest: (String) -> Unit
) {
    // 1. Character Type Selection
    var selectedTypeId by remember { mutableStateOf(editingCharacter?.characterType ?: "Cartoon 2D") }
    var selectedVisualStyle by remember { mutableStateOf("2D") }

    // 2. Basic Info
    var name by remember { mutableStateOf(editingCharacter?.name ?: "") }
    var age by remember { mutableStateOf(editingCharacter?.age ?: "") }
    var selectedTypeDropdown by remember { mutableStateOf(editingCharacter?.characterType ?: "Criança") }
    var selectedProjId by remember { mutableStateOf(editingCharacter?.projectId ?: selectedProjectId ?: projects.firstOrNull()?.id) }
    var selectedVoice by remember { mutableStateOf(editingCharacter?.voice ?: "Voz Masculina 1") }
    var role by remember { mutableStateOf(editingCharacter?.role ?: "Protagonista") }
    var personality by remember { mutableStateOf(editingCharacter?.personality ?: "") }
    var history by remember { mutableStateOf(editingCharacter?.history ?: "") }

    // 3. Visual Traits - Human / General
    var selectedFaceShape by remember { mutableStateOf("Oval") }
    var selectedHairStyle by remember { mutableStateOf("Despojado") }
    var selectedSkinColor by remember { mutableStateOf("#FFE0BD") }
    var selectedEyeColor by remember { mutableStateOf("#5D4037") }
    var selectedEyebrows by remember { mutableStateOf("Natural") }
    var selectedMouth by remember { mutableStateOf("Sorridente") }
    var selectedClothing by remember { mutableStateOf("Moletom Azul") }
    var selectedAccessory by remember { mutableStateOf("Nenhum") }
    var selectedExpression by remember { mutableStateOf("Alegre") }
    var selectedPrimaryColor by remember { mutableStateOf("#0052FF") }

    // Adaptive non-human traits
    var selectedFruitType by remember { mutableStateOf("Maçã") }
    var selectedAnimalSpecies by remember { mutableStateOf("Cachorro") }
    var selectedRobotModel by remember { mutableStateOf("Androide 3D") }
    var selectedCreatureType by remember { mutableStateOf("Monstrinho Fofo") }
    var selectedSpecialFeature by remember { mutableStateOf("Asas Brilhantes") }

    // AI Generation & Preview State
    var aiPromptAssistant by remember { mutableStateOf("") }
    var isGeneratingPreview by remember { mutableStateOf(false) }
    var previewGenerated by remember { mutableStateOf(true) }
    var currentPreviewImageKey by remember { mutableStateOf(editingCharacter?.imageUri ?: "char_preview_boy") }

    // Dropdown toggles
    var isTypeMenuOpen by remember { mutableStateOf(false) }
    var isProjMenuOpen by remember { mutableStateOf(false) }
    var isVoiceMenuOpen by remember { mutableStateOf(false) }
    var showAllTypesModal by remember { mutableStateOf(false) }

    val characterTypeCards = listOf(
        CharacterTypeCard("Cartoon 2D", "Cartoon 2D", "🎨", R.drawable.char_lucas),
        CharacterTypeCard("Cartoon 3D", "Cartoon 3D", "🧊", R.drawable.char_carlos),
        CharacterTypeCard("Desenho 2D", "Desenho 2D", "✏️", R.drawable.char_sofia),
        CharacterTypeCard("Boneco 3D", "Boneco 3D", "🧸", R.drawable.char_sr_manuel),
        CharacterTypeCard("Personagem humano", "Personagem humano", "👤", R.drawable.char_lucas),
        CharacterTypeCard("Animal", "Animal", "🐶", R.drawable.char_mimi),
        CharacterTypeCard("Fruta / Alimento", "Fruta / Alimento", "🍎", R.drawable.char_antonio),
        CharacterTypeCard("Robô", "Robô", "🤖", R.drawable.char_r7_robot),
        CharacterTypeCard("Criatura fantástica", "Criatura fantástica", "👾", R.drawable.char_bia),
        CharacterTypeCard("Super-herói", "Super-herói", "🦸", R.drawable.char_lucas),
        CharacterTypeCard("Personagem de fantasia", "Personagem de fantasia", "🧙", R.drawable.char_sofia),
        CharacterTypeCard("Outro", "Outro", "•••", null)
    )

    val visualStyles = listOf("2D", "3D", "Cartoon", "Anime", "Realista", "Estilizado", "Infantil", "Fantasia", "Personalizado")

    val skinColors = listOf("#FFE0BD", "#F1C27D", "#E0AC69", "#C68642", "#8D5524", "#3B2219")
    val eyeColors = listOf("#5D4037", "#1976D2", "#388E3C", "#78909C", "#D97706")
    val primaryColors = listOf("#0052FF", "#DC2626", "#F59E0B", "#16A34A", "#9333EA")

    val voiceOptions = listOf(
        "Voz Masculina 1", "Voz Masculina 2", "Voz Masculina 3", "Voz Masculina 4",
        "Voz Feminina 1", "Voz Feminina 2", "Voz Feminina 3",
        "Voz Infantil", "Voz Robô 1", "Voz Narrador Épico"
    )

    val allCharacterTypesList = listOf(
        "Cartoon 2D", "Cartoon 3D", "Desenho 2D", "Boneco 3D", "Personagem de animação",
        "Personagem humano", "Criança", "Adolescente", "Adulto", "Idoso",
        "Animal", "Animal antropomórfico", "Fruta ou alimento", "Boneco/brinquedo",
        "Robô", "Criatura fantástica", "Personagem de fantasia", "Personagem mágico",
        "Super-herói", "Monstro", "Personagem de conto/fábula", "Personagem estilo videojogo", "Outro"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.94f)
                .padding(vertical = 12.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header (Criar novo personagem + Subtitle + Close button)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (editingCharacter != null) "Editar personagem" else "Criar novo personagem",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 20.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Crie e personalize seu personagem do jeito que você imaginar.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color(0xFF64748B),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Modal Body - Responsive Layout (Columns / Scroll)
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFFCFDFE))
                ) {
                    val isLargeScreen = maxWidth >= 960.dp

                    if (isLargeScreen) {
                        // 3-Column / 4-Section Wide Layout matching reference image exactly
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            // Column 1: 1. Escolha o tipo de personagem + Estilo visual + 2. Informações básicas
                            Column(
                                modifier = Modifier
                                    .weight(1.15f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                SectionTypeAndStyle(
                                    selectedTypeId = selectedTypeId,
                                    onSelectType = {
                                        selectedTypeId = it
                                        selectedTypeDropdown = it
                                        if (it.contains("Fruta")) currentPreviewImageKey = "char_antonio"
                                        else if (it.contains("Animal")) currentPreviewImageKey = "char_mimi"
                                        else if (it.contains("Robô")) currentPreviewImageKey = "char_r7_robot"
                                        else currentPreviewImageKey = "char_preview_boy"
                                    },
                                    selectedVisualStyle = selectedVisualStyle,
                                    onSelectVisualStyle = { selectedVisualStyle = it },
                                    typeCards = characterTypeCards,
                                    onOpenAllTypes = { showAllTypesModal = true }
                                )

                                SectionBasicInfo(
                                    name = name,
                                    onNameChange = { name = it },
                                    age = age,
                                    onAgeChange = { age = it },
                                    selectedType = selectedTypeDropdown,
                                    onSelectType = { selectedTypeDropdown = it },
                                    projects = projects,
                                    selectedProjId = selectedProjId,
                                    onSelectProject = { selectedProjId = it },
                                    selectedVoice = selectedVoice,
                                    onSelectVoice = { selectedVoice = it },
                                    allTypes = allCharacterTypesList,
                                    voiceOptions = voiceOptions,
                                    isTypeOpen = isTypeMenuOpen,
                                    onTypeOpenChange = { isTypeMenuOpen = it },
                                    isProjOpen = isProjMenuOpen,
                                    onProjOpenChange = { isProjMenuOpen = it },
                                    isVoiceOpen = isVoiceMenuOpen,
                                    onVoiceOpenChange = { isVoiceMenuOpen = it }
                                )
                            }

                            // Column 2: 3. Criar aparência do personagem (Adaptive)
                            Column(
                                modifier = Modifier
                                    .weight(1.25f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                SectionAppearanceCustomizer(
                                    selectedTypeId = selectedTypeId,
                                    selectedFaceShape = selectedFaceShape,
                                    onSelectFaceShape = { selectedFaceShape = it },
                                    selectedHairStyle = selectedHairStyle,
                                    onSelectHairStyle = { selectedHairStyle = it },
                                    selectedSkinColor = selectedSkinColor,
                                    onSelectSkinColor = { selectedSkinColor = it },
                                    selectedEyeColor = selectedEyeColor,
                                    onSelectEyeColor = { selectedEyeColor = it },
                                    selectedEyebrows = selectedEyebrows,
                                    onSelectEyebrows = { selectedEyebrows = it },
                                    selectedMouth = selectedMouth,
                                    onSelectMouth = { selectedMouth = it },
                                    selectedClothing = selectedClothing,
                                    onSelectClothing = { selectedClothing = it },
                                    selectedAccessory = selectedAccessory,
                                    onSelectAccessory = { selectedAccessory = it },
                                    selectedExpression = selectedExpression,
                                    onSelectExpression = { selectedExpression = it },
                                    selectedPrimaryColor = selectedPrimaryColor,
                                    onSelectPrimaryColor = { selectedPrimaryColor = it },
                                    // Non-human adaptive
                                    selectedFruitType = selectedFruitType,
                                    onSelectFruitType = { selectedFruitType = it },
                                    selectedAnimalSpecies = selectedAnimalSpecies,
                                    onSelectAnimalSpecies = { selectedAnimalSpecies = it },
                                    selectedRobotModel = selectedRobotModel,
                                    onSelectRobotModel = { selectedRobotModel = it },
                                    selectedCreatureType = selectedCreatureType,
                                    onSelectCreatureType = { selectedCreatureType = it },
                                    skinColors = skinColors,
                                    eyeColors = eyeColors,
                                    primaryColors = primaryColors,
                                    onGenerateWithAi = {
                                        isGeneratingPreview = true
                                        previewGenerated = true
                                        if (name.isBlank()) {
                                            name = when {
                                                selectedTypeId.contains("Fruta") -> "Mimi Frutinha"
                                                selectedTypeId.contains("Animal") -> "Tobi"
                                                selectedTypeId.contains("Robô") -> "Z-9 Bot"
                                                else -> "Lucas"
                                            }
                                        }
                                        if (personality.isBlank()) personality = "Corajoso, alegre e muito companheiro"
                                    }
                                )
                            }

                            // Column 3: 4. Pré-visualização + IA Assistente
                            Column(
                                modifier = Modifier
                                    .weight(1.1f)
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                SectionCharacterPreview(
                                    selectedTypeId = selectedTypeId,
                                    selectedClothing = selectedClothing,
                                    selectedExpression = selectedExpression,
                                    isGenerating = isGeneratingPreview,
                                    currentPreviewKey = currentPreviewImageKey,
                                    onGenerateAgain = {
                                        isGeneratingPreview = true
                                    },
                                    onUseCharacter = {
                                        // Auto-confirm
                                    },
                                    onDeletePreview = {
                                        previewGenerated = false
                                    },
                                    aiPrompt = aiPromptAssistant,
                                    onAiPromptChange = { aiPromptAssistant = it },
                                    onAiGenerateIdeas = {
                                        val prompt = aiPromptAssistant.ifBlank {
                                            "Crie um personagem carismático do tipo $selectedTypeId com personalidade viva e marcante."
                                        }
                                        onAiSuggest(prompt)
                                        if (name.isBlank()) name = "Lucas"
                                        if (age.isBlank()) age = "10"
                                        personality = "Menino curioso, adora aventuras e ciência."
                                        history = "Líder de explorações e grande inventor do clube."
                                    }
                                )
                            }
                        }
                    } else {
                        // Stacked layout for compact / mobile screens
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(18.dp)
                        ) {
                            SectionTypeAndStyle(
                                selectedTypeId = selectedTypeId,
                                onSelectType = {
                                    selectedTypeId = it
                                    selectedTypeDropdown = it
                                },
                                selectedVisualStyle = selectedVisualStyle,
                                onSelectVisualStyle = { selectedVisualStyle = it },
                                typeCards = characterTypeCards,
                                onOpenAllTypes = { showAllTypesModal = true }
                            )

                            SectionBasicInfo(
                                name = name,
                                onNameChange = { name = it },
                                age = age,
                                onAgeChange = { age = it },
                                selectedType = selectedTypeDropdown,
                                onSelectType = { selectedTypeDropdown = it },
                                projects = projects,
                                selectedProjId = selectedProjId,
                                onSelectProject = { selectedProjId = it },
                                selectedVoice = selectedVoice,
                                onSelectVoice = { selectedVoice = it },
                                allTypes = allCharacterTypesList,
                                voiceOptions = voiceOptions,
                                isTypeOpen = isTypeMenuOpen,
                                onTypeOpenChange = { isTypeMenuOpen = it },
                                isProjOpen = isProjMenuOpen,
                                onProjOpenChange = { isProjMenuOpen = it },
                                isVoiceOpen = isVoiceMenuOpen,
                                onVoiceOpenChange = { isVoiceMenuOpen = it }
                            )

                            SectionAppearanceCustomizer(
                                selectedTypeId = selectedTypeId,
                                selectedFaceShape = selectedFaceShape,
                                onSelectFaceShape = { selectedFaceShape = it },
                                selectedHairStyle = selectedHairStyle,
                                onSelectHairStyle = { selectedHairStyle = it },
                                selectedSkinColor = selectedSkinColor,
                                onSelectSkinColor = { selectedSkinColor = it },
                                selectedEyeColor = selectedEyeColor,
                                onSelectEyeColor = { selectedEyeColor = it },
                                selectedEyebrows = selectedEyebrows,
                                onSelectEyebrows = { selectedEyebrows = it },
                                selectedMouth = selectedMouth,
                                onSelectMouth = { selectedMouth = it },
                                selectedClothing = selectedClothing,
                                onSelectClothing = { selectedClothing = it },
                                selectedAccessory = selectedAccessory,
                                onSelectAccessory = { selectedAccessory = it },
                                selectedExpression = selectedExpression,
                                onSelectExpression = { selectedExpression = it },
                                selectedPrimaryColor = selectedPrimaryColor,
                                onSelectPrimaryColor = { selectedPrimaryColor = it },
                                selectedFruitType = selectedFruitType,
                                onSelectFruitType = { selectedFruitType = it },
                                selectedAnimalSpecies = selectedAnimalSpecies,
                                onSelectAnimalSpecies = { selectedAnimalSpecies = it },
                                selectedRobotModel = selectedRobotModel,
                                onSelectRobotModel = { selectedRobotModel = it },
                                selectedCreatureType = selectedCreatureType,
                                onSelectCreatureType = { selectedCreatureType = it },
                                skinColors = skinColors,
                                eyeColors = eyeColors,
                                primaryColors = primaryColors,
                                onGenerateWithAi = {
                                    isGeneratingPreview = true
                                    previewGenerated = true
                                }
                            )

                            SectionCharacterPreview(
                                selectedTypeId = selectedTypeId,
                                selectedClothing = selectedClothing,
                                selectedExpression = selectedExpression,
                                isGenerating = isGeneratingPreview,
                                currentPreviewKey = currentPreviewImageKey,
                                onGenerateAgain = { isGeneratingPreview = true },
                                onUseCharacter = {},
                                onDeletePreview = { previewGenerated = false },
                                aiPrompt = aiPromptAssistant,
                                onAiPromptChange = { aiPromptAssistant = it },
                                onAiGenerateIdeas = {
                                    onAiSuggest("Crie ideias de personagem para o estúdio.")
                                    if (name.isBlank()) name = "Lucas"
                                }
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color(0xFFF1F5F9))

                // Modal Footer (Cancelar + Criar personagem)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFF334155)
                        ),
                        modifier = Modifier.height(42.dp)
                    ) {
                        Text("Cancelar", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            val finalName = name.trim().ifBlank { "Novo Personagem" }
                            val finalDesc = "Tipo: $selectedTypeId • Estilo: $selectedVisualStyle • Roupa: $selectedClothing • Expressão: $selectedExpression • Acessório: $selectedAccessory"
                            val finalPersonality = personality.trim().ifBlank { "Alegre, expressivo e aventureiro" }
                            val finalHistory = history.trim().ifBlank { "Personagem criado para as produções audiovisuais do RANGA AI STUDIO." }

                            onSave(
                                finalName,
                                finalPersonality,
                                age.trim(),
                                finalDesc,
                                finalHistory,
                                selectedTypeDropdown,
                                selectedVoice,
                                role,
                                selectedProjId,
                                currentPreviewImageKey
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier
                            .height(42.dp)
                            .testTag("submit_create_character_button")
                    ) {
                        Text(
                            text = if (editingCharacter != null) "Salvar alterações" else "Criar personagem",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    // Modal with all 23 character types if user clicks "Ver mais" or "Outro"
    if (showAllTypesModal) {
        Dialog(onDismissRequest = { showAllTypesModal = false }) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Todos os Tipos de Personagem",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(allCharacterTypesList) { typeName ->
                            Surface(
                                onClick = {
                                    selectedTypeId = typeName
                                    selectedTypeDropdown = typeName
                                    showAllTypesModal = false
                                },
                                shape = RoundedCornerShape(6.dp),
                                color = if (selectedTypeId == typeName) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = typeName,
                                    color = if (selectedTypeId == typeName) Color.White else Color(0xFF334155),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showAllTypesModal = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Fechar")
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 1: Escolha o tipo de personagem & Estilo visual
// -------------------------------------------------------------
@Composable
fun SectionTypeAndStyle(
    selectedTypeId: String,
    onSelectType: (String) -> Unit,
    selectedVisualStyle: String,
    onSelectVisualStyle: (String) -> Unit,
    typeCards: List<CharacterTypeCard>,
    onOpenAllTypes: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1. Escolha o tipo de personagem",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.5.sp
                    )
                )
                Text(
                    text = "Ver todos",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF0052FF),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier.clickable { onOpenAllTypes() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4-Column Grid of 12 Character Type Cards
            val chunkedCards = typeCards.chunked(4)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                chunkedCards.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowItems.forEach { card ->
                            val isSelected = selectedTypeId == card.id
                            Surface(
                                onClick = { onSelectType(card.id) },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFFAFAFA),
                                border = BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(84.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    if (card.drawableRes != null) {
                                        Image(
                                            painter = painterResource(id = card.drawableRes),
                                            contentDescription = card.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                        )
                                    } else {
                                        Text(
                                            text = card.emoji,
                                            fontSize = 20.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = card.title,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color(0xFF0052FF) else Color(0xFF334155),
                                            fontSize = 9.5.sp,
                                            textAlign = TextAlign.Center
                                        ),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Estilo visual section
            Text(
                text = "Estilo visual",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            val styles = listOf("2D", "3D", "Cartoon", "Anime", "Realista", "Estilizado", "Infantil", "Fantasia", "Personalizado")
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                styles.forEach { style ->
                    val isSel = selectedVisualStyle == style
                    Surface(
                        onClick = { onSelectVisualStyle(style) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isSel) Color.White else Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = style,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFF475569),
                                fontSize = 11.sp
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 2: Informações básicas
// -------------------------------------------------------------
@Composable
fun SectionBasicInfo(
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    selectedType: String,
    onSelectType: (String) -> Unit,
    projects: List<ProjectEntity>,
    selectedProjId: Long?,
    onSelectProject: (Long) -> Unit,
    selectedVoice: String,
    onSelectVoice: (String) -> Unit,
    allTypes: List<String>,
    voiceOptions: List<String>,
    isTypeOpen: Boolean,
    onTypeOpenChange: (Boolean) -> Unit,
    isProjOpen: Boolean,
    onProjOpenChange: (Boolean) -> Unit,
    isVoiceOpen: Boolean,
    onVoiceOpenChange: (Boolean) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "2. Informações básicas",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Row: Nome do personagem * & Idade
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        text = "Nome do personagem *",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155),
                            fontSize = 11.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        placeholder = { Text("Digite o nome do personagem", color = Color(0xFF94A3B8), fontSize = 11.5.sp) },
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Idade",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155),
                            fontSize = 11.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = age,
                        onValueChange = onAgeChange,
                        placeholder = { Text("Ex: 10", color = Color(0xFF94A3B8), fontSize = 11.5.sp) },
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
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row: Tipo de personagem Dropdown & Projeto * Dropdown
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tipo de personagem Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tipo de personagem",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155),
                            fontSize = 11.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        OutlinedButton(
                            onClick = { onTypeOpenChange(true) },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedType.ifBlank { "Selecione o tipo" },
                                    color = Color(0xFF0F172A),
                                    fontSize = 11.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text("▾", color = Color(0xFF64748B), fontSize = 11.sp)
                            }
                        }

                        DropdownMenu(
                            expanded = isTypeOpen,
                            onDismissRequest = { onTypeOpenChange(false) }
                        ) {
                            allTypes.take(12).forEach { t ->
                                DropdownMenuItem(
                                    text = { Text(t, fontSize = 12.sp) },
                                    onClick = {
                                        onSelectType(t)
                                        onTypeOpenChange(false)
                                    }
                                )
                            }
                        }
                    }
                }

                // Projeto * Dropdown
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Projeto *",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155),
                            fontSize = 11.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box {
                        val currentProjName = projects.find { it.id == selectedProjId }?.name ?: "Selecione o projeto"
                        OutlinedButton(
                            onClick = { onProjOpenChange(true) },
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                            modifier = Modifier.fillMaxWidth().height(42.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = currentProjName,
                                    color = Color(0xFF0F172A),
                                    fontSize = 11.5.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text("▾", color = Color(0xFF64748B), fontSize = 11.sp)
                            }
                        }

                        DropdownMenu(
                            expanded = isProjOpen,
                            onDismissRequest = { onProjOpenChange(false) }
                        ) {
                            projects.forEach { proj ->
                                DropdownMenuItem(
                                    text = { Text(proj.name, fontSize = 12.sp) },
                                    onClick = {
                                        onSelectProject(proj.id)
                                        onProjOpenChange(false)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Voz Dropdown
            Text(
                text = "Voz",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF334155),
                    fontSize = 11.5.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box {
                OutlinedButton(
                    onClick = { onVoiceOpenChange(true) },
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF8FAFC)),
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🎙️ $selectedVoice",
                            color = Color(0xFF0F172A),
                            fontSize = 11.5.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text("▾", color = Color(0xFF64748B), fontSize = 11.sp)
                    }
                }

                DropdownMenu(
                    expanded = isVoiceOpen,
                    onDismissRequest = { onVoiceOpenChange(false) }
                ) {
                    voiceOptions.forEach { v ->
                        DropdownMenuItem(
                            text = { Text("🎙️ $v", fontSize = 12.sp) },
                            onClick = {
                                onSelectVoice(v)
                                onVoiceOpenChange(false)
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 3: Criar aparência do personagem (Adaptive)
// -------------------------------------------------------------
@Composable
fun SectionAppearanceCustomizer(
    selectedTypeId: String,
    selectedFaceShape: String,
    onSelectFaceShape: (String) -> Unit,
    selectedHairStyle: String,
    onSelectHairStyle: (String) -> Unit,
    selectedSkinColor: String,
    onSelectSkinColor: (String) -> Unit,
    selectedEyeColor: String,
    onSelectEyeColor: (String) -> Unit,
    selectedEyebrows: String,
    onSelectEyebrows: (String) -> Unit,
    selectedMouth: String,
    onSelectMouth: (String) -> Unit,
    selectedClothing: String,
    onSelectClothing: (String) -> Unit,
    selectedAccessory: String,
    onSelectAccessory: (String) -> Unit,
    selectedExpression: String,
    onSelectExpression: (String) -> Unit,
    selectedPrimaryColor: String,
    onSelectPrimaryColor: (String) -> Unit,
    selectedFruitType: String,
    onSelectFruitType: (String) -> Unit,
    selectedAnimalSpecies: String,
    onSelectAnimalSpecies: (String) -> Unit,
    selectedRobotModel: String,
    onSelectRobotModel: (String) -> Unit,
    selectedCreatureType: String,
    onSelectCreatureType: (String) -> Unit,
    skinColors: List<String>,
    eyeColors: List<String>,
    primaryColors: List<String>,
    onGenerateWithAi: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "3. Criar aparência do personagem",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Check if Non-Human (Fruit, Animal, Robot, Creature)
            val isFruit = selectedTypeId.contains("Fruta") || selectedTypeId.contains("Alimento")
            val isAnimal = selectedTypeId.contains("Animal")
            val isRobot = selectedTypeId.contains("Robô")
            val isCreature = selectedTypeId.contains("Criatura") || selectedTypeId.contains("Monstro")

            if (isFruit) {
                // Adaptive Fruit Fields
                AppearanceRowText(title = "🍎 Tipo de fruta") {
                    val fruits = listOf("Maçã", "Banana", "Laranja", "Morango", "Uva", "Melancia")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        fruits.forEach { fr ->
                            val isSel = selectedFruitType == fr
                            Surface(
                                onClick = { onSelectFruitType(fr) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = fr,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else if (isAnimal) {
                // Adaptive Animal Fields
                AppearanceRowText(title = "🐶 Espécie") {
                    val species = listOf("Cachorro", "Gato", "Coelho", "Leão", "Urso", "Raposa")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        species.forEach { sp ->
                            val isSel = selectedAnimalSpecies == sp
                            Surface(
                                onClick = { onSelectAnimalSpecies(sp) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = sp,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else if (isRobot) {
                // Adaptive Robot Fields
                AppearanceRowText(title = "🤖 Modelo") {
                    val models = listOf("Androide 3D", "Compacto", "Cibernético", "Retro", "Drone")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        models.forEach { mo ->
                            val isSel = selectedRobotModel == mo
                            Surface(
                                onClick = { onSelectRobotModel(mo) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = mo,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            } else if (isCreature) {
                // Adaptive Creature Fields
                AppearanceRowText(title = "👾 Criatura") {
                    val creatures = listOf("Monstrinho", "Dragãozinho", "Fadinha", "Alienígena")
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        creatures.forEach { cr ->
                            val isSel = selectedCreatureType == cr
                            Surface(
                                onClick = { onSelectCreatureType(cr) },
                                shape = RoundedCornerShape(6.dp),
                                color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = cr,
                                    color = if (isSel) Color.White else Color(0xFF334155),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 1. Rosto (Face Shapes)
            AppearanceRow(title = "👤 Rosto") {
                val faces = listOf("Oval", "Redondo", "Quadrado", "Coração", "Diamante")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    faces.forEach { f ->
                        val isSel = selectedFaceShape == f
                        Surface(
                            onClick = { onSelectFaceShape(f) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color.White,
                            border = BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(RoundedCornerShape(if (f == "Oval") 10.dp else if (f == "Redondo") 12.dp else 4.dp))
                                        .background(Color(0xFFFFDFBA))
                                        .border(1.dp, Color(0xFFE0AC69), RoundedCornerShape(if (f == "Oval") 10.dp else if (f == "Redondo") 12.dp else 4.dp))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 2. Cabelo (Hair Swatches)
            AppearanceRow(title = "💇 Cabelo") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val hairStyles = listOf("Despojado", "Liso", "Topete", "Cacheado")
                    hairStyles.forEach { h ->
                        val isSel = selectedHairStyle == h
                        Surface(
                            onClick = { onSelectHairStyle(h) },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color.White,
                            border = BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF3E2723)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("💇", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                    Text("Ver mais", fontSize = 10.sp, color = Color(0xFF0052FF), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { })
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Cor da pele (Skin Color Swatches)
            AppearanceRow(title = "👶 Cor da pele") {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    skinColors.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSel = selectedSkinColor == hex
                        Box(
                            modifier = Modifier
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) Color(0xFF0052FF) else Color(0xFFCBD5E1),
                                    CircleShape
                                )
                                .clickable { onSelectSkinColor(hex) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 4. Cor dos olhos (Eye Color Swatches)
            AppearanceRow(title = "👁️ Cor dos olhos") {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    eyeColors.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSel = selectedEyeColor == hex
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) Color(0xFF0052FF) else Color(0xFFCBD5E1),
                                    CircleShape
                                )
                                .clickable { onSelectEyeColor(hex) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Sobrancelhas
            AppearanceRow(title = "〰️ Sobrancelhas") {
                val brows = listOf("Natural", "Fina", "Reta", "Arqueada", "Espessa")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    brows.forEach { b ->
                        val isSel = selectedEyebrows == b
                        Surface(
                            onClick = { onSelectEyebrows(b) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(width = 30.dp, height = 24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("〰", fontSize = 11.sp, color = if (isSel) Color(0xFF0052FF) else Color(0xFF475569))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 6. Boca
            AppearanceRow(title = "👄 Boca") {
                val mouths = listOf("Sorriso", "Neutro", "Aberto", "Discreto")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    mouths.forEach { m ->
                        val isSel = selectedMouth == m
                        Surface(
                            onClick = { onSelectMouth(m) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(width = 32.dp, height = 26.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (m == "Sorriso") "👄" else if (m == "Aberto") "😃" else "—",
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 7. Roupa (Clothing)
            AppearanceRow(title = "👕 Roupa") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val clothes = listOf(
                        Triple("Camiseta Vermelha", Color(0xFFDC2626), "👕"),
                        Triple("Moletom Azul", Color(0xFF0052FF), "🧥"),
                        Triple("Camiseta Amarela", Color(0xFFF59E0B), "👕"),
                        Triple("Blusa Verde", Color(0xFF16A34A), "👚")
                    )
                    clothes.forEach { (cName, cColor, cIcon) ->
                        val isSel = selectedClothing == cName
                        Surface(
                            onClick = { onSelectClothing(cName) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(cIcon, fontSize = 14.sp)
                            }
                        }
                    }
                    Text("Ver mais", fontSize = 10.sp, color = Color(0xFF0052FF), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { })
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 8. Acessórios
            AppearanceRow(title = "👓 Acessórios") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val accessories = listOf(
                        Pair("Óculos", "👓"),
                        Pair("Chapéu", "🎩"),
                        Pair("Fone", "🎧"),
                        Pair("Boné", "🧢")
                    )
                    accessories.forEach { (aName, aIcon) ->
                        val isSel = selectedAccessory == aName
                        Surface(
                            onClick = { onSelectAccessory(if (isSel) "Nenhum" else aName) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(aIcon, fontSize = 14.sp)
                            }
                        }
                    }
                    Text("Ver mais", fontSize = 10.sp, color = Color(0xFF0052FF), fontWeight = FontWeight.SemiBold, modifier = Modifier.clickable { })
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 9. Expressão
            AppearanceRow(title = "😀 Expressão") {
                val expressions = listOf(
                    Pair("Neutro", "😐"),
                    Pair("Alegre", "😃"),
                    Pair("Bravo", "😠"),
                    Pair("Confiante", "😎"),
                    Pair("Assustado", "😱")
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    expressions.forEach { (expName, expEmoji) ->
                        val isSel = selectedExpression == expName
                        Surface(
                            onClick = { onSelectExpression(expName) },
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSel) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = BorderStroke(if (isSel) 1.5.dp else 1.dp, if (isSel) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(expEmoji, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 10. Cores principais
            AppearanceRow(title = "🎨 Cores principais") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    primaryColors.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        val isSel = selectedPrimaryColor == hex
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    if (isSel) 2.dp else 1.dp,
                                    if (isSel) Color(0xFF0052FF) else Color(0xFFCBD5E1),
                                    CircleShape
                                )
                                .clickable { onSelectPrimaryColor(hex) }
                        )
                    }
                    // Rainbow color picker icon
                    Box(
                        modifier = Modifier
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)
                                )
                            )
                            .border(1.dp, Color(0xFFCBD5E1), CircleShape)
                            .clickable { }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Prominent button: ✨ Gerar personagem com IA
            Button(
                onClick = onGenerateWithAi,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF6FF),
                    contentColor = Color(0xFF0052FF)
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF0052FF),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "✨ Gerar personagem com IA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = Color(0xFF0052FF)
                )
            }
        }
    }
}

// -------------------------------------------------------------
// SECTION 4: Pré-visualização & IA Assistente
// -------------------------------------------------------------
@Composable
fun SectionCharacterPreview(
    selectedTypeId: String,
    selectedClothing: String,
    selectedExpression: String,
    isGenerating: Boolean,
    currentPreviewKey: String,
    onGenerateAgain: () -> Unit,
    onUseCharacter: () -> Unit,
    onDeletePreview: () -> Unit,
    aiPrompt: String,
    onAiPromptChange: (String) -> Unit,
    onAiGenerateIdeas: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "4. Pré-visualização",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.5.sp
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Main 3D Character Preview Render Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF8FAFC))
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                val previewRes = when (currentPreviewKey) {
                    "char_antonio" -> R.drawable.char_antonio
                    "char_bia" -> R.drawable.char_bia
                    "char_carlos" -> R.drawable.char_carlos
                    "char_mimi" -> R.drawable.char_mimi
                    "char_r7_robot" -> R.drawable.char_r7_robot
                    "char_sofia" -> R.drawable.char_sofia
                    "char_sr_manuel" -> R.drawable.char_sr_manuel
                    else -> R.drawable.char_preview_boy_1787037236644
                }

                Image(
                    painter = painterResource(id = previewRes),
                    contentDescription = "Pré-visualização do Personagem",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )

                if (isGenerating) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Renderizando personagem 3D...",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Esta é uma prévia. Você poderá editar depois.",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF94A3B8),
                    fontSize = 10.5.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action row: 🔄 Gerar novamente • ✏️ Editar • 💾 Usar • 🗑️ Excluir
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Surface(
                    onClick = onGenerateAgain,
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.weight(1f).height(30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Gerar", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0052FF))
                    }
                }

                Surface(
                    onClick = onUseCharacter,
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFEFF6FF),
                    modifier = Modifier.weight(1f).height(30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Usar", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF0052FF))
                    }
                }

                Surface(
                    onClick = onDeletePreview,
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFFEF2F2),
                    modifier = Modifier.weight(1f).height(30.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Excluir", fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFDC2626))
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ✨ IA Assistente Card
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF9333EA),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "IA Assistente",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Precisa de ajuda? Descreva o personagem que você quer criar.",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 10.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = aiPrompt,
                        onValueChange = onAiPromptChange,
                        placeholder = {
                            Text(
                                "Ex: Um menino corajoso, cabelo castanho, camiseta azul, óculos...",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                lineHeight = 14.sp
                            )
                        },
                        minLines = 2,
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF0052FF),
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onAiGenerateIdeas,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEFF6FF),
                            contentColor = Color(0xFF0052FF)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier
                            .align(Alignment.End)
                            .height(32.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFF0052FF),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Gerar ideias",
                            fontSize = 11.sp,
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
// Row layout helper for appearance traits
// -------------------------------------------------------------
@Composable
private fun AppearanceRow(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF334155),
                fontSize = 11.sp
            ),
            modifier = Modifier.width(105.dp)
        )
        content()
    }
}

@Composable
private fun AppearanceRowText(
    title: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0052FF),
                fontSize = 11.sp
            ),
            modifier = Modifier.width(105.dp)
        )
        content()
    }
}
