package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.ProjectEntity
import com.example.data.model.RangaCreationEntity
import com.example.data.model.RoteiroBrancoItemEntity

fun resolveCreationImageRes(context: android.content.Context, uri: String?): Int {
    if (uri.isNullOrBlank()) return R.drawable.char_preview_boy_1787037236644
    val resId = context.resources.getIdentifier(uri, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.char_preview_boy_1787037236644
}

// ==========================================
// 1. MODAL DETALHES DA CRIAÇÃO
// ==========================================
@Composable
fun CreationDetailsDialog(
    creation: RangaCreationEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onToggleFavorite: () -> Unit,
    onToggleRoteiro: () -> Unit,
    onUseAsCharacter: () -> Unit,
    onUseAsScenario: () -> Unit,
    onEditPrompt: () -> Unit,
    onDelete: () -> Unit
) {
    val context = LocalContext.current
    val imageRes = resolveCreationImageRes(context, creation.imageUri)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 680.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.size(40.dp)
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
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = creation.title,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF091124)
                                )
                            )
                            Text(
                                text = "${creation.creationType} • ${creation.style} • ${creation.aspectRatio}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF64748B)
                                )
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Image Preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF091124))
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = creation.title,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )

                    // Quick Top Right Badge
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xCC091124)
                        ) {
                            Text(
                                text = creation.aspectRatio,
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Prompt Section
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "PROMPT UTILIZADO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B),
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = creation.prompt,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF1E293B),
                                lineHeight = 20.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Projeto Vinculado", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                            Text(creation.projectName ?: "Geral", fontSize = 12.sp, color = Color(0xFF091124), fontWeight = FontWeight.Bold)
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF1F5F9),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Qualidade", fontSize = 10.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                            Text(creation.quality, fontSize = 12.sp, color = Color(0xFF091124), fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions Grid
                Text(
                    text = "AÇÕES DE PRODUÇÃO",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        fontSize = 10.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onUseAsCharacter,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.People, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Usar como Personagem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onUseAsScenario,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Landscape, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Usar como Cenário", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onToggleRoteiro,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (creation.isInRoteiroBranco) Color(0xFFEFF6FF) else Color.Transparent
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = if (creation.isInRoteiroBranco) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = if (creation.isInRoteiroBranco) Color(0xFF0052FF) else Color(0xFF334155),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (creation.isInRoteiroBranco) "No Roteiro Branco" else "Adicionar ao Roteiro",
                            fontSize = 11.sp,
                            color = if (creation.isInRoteiroBranco) Color(0xFF0052FF) else Color(0xFF334155),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = onToggleFavorite,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(0.7f)
                    ) {
                        Icon(
                            imageVector = if (creation.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null,
                            tint = if (creation.isFavorite) Color(0xFFE11D48) else Color(0xFF64748B),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (creation.isFavorite) "Favorito" else "Favoritar",
                            fontSize = 11.sp,
                            color = Color(0xFF334155)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onEditPrompt,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFF0052FF))
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Modificar Prompt", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFE11D48))
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Excluir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. MODAL USAR COMO PERSONAGEM
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UseAsCharacterDialog(
    creation: RangaCreationEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, role: String, personality: String, projectId: Long?) -> Unit
) {
    var charName by remember { mutableStateOf(creation.title) }
    var charRole by remember { mutableStateOf("Protagonista") }
    var charPersonality by remember { mutableStateOf("Carismático, divertido e engenhoso") }
    var selectedProjId by remember { mutableStateOf(creation.projectId ?: projects.firstOrNull()?.id) }
    var projDropdownExpanded by remember { mutableStateOf(false) }

    val roles = listOf("Protagonista", "Antagonista", "Coadjuvante", "Alívio Cômico", "Mentor", "Extra")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Cadastrar como Personagem", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF091124))
                        Text("A imagem gerada será vinculada ao elenco", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = charName,
                    onValueChange = { charName = it },
                    label = { Text("Nome do Personagem") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Role Selector Chips
                Text("Papel na História", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    roles.take(3).forEach { r ->
                        val isSel = charRole == r
                        Surface(
                            onClick = { charRole = r },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = r,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = charPersonality,
                    onValueChange = { charPersonality = it },
                    label = { Text("Personalidade / Traços") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Project Selector
                Text("Vincular ao Projeto", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = projDropdownExpanded,
                    onExpandedChange = { projDropdownExpanded = it }
                ) {
                    val currentProjName = projects.find { it.id == selectedProjId }?.name ?: "Nenhum projeto"
                    OutlinedTextField(
                        value = currentProjName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = projDropdownExpanded,
                        onDismissRequest = { projDropdownExpanded = false }
                    ) {
                        projects.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    selectedProjId = p.id
                                    projDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(charName, charRole, charPersonality, selectedProjId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Salvar Personagem", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. MODAL USAR COMO CENÁRIO
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UseAsScenarioDialog(
    creation: RangaCreationEntity,
    projects: List<ProjectEntity>,
    onDismiss: () -> Unit,
    onConfirm: (name: String, category: String, atmosphere: String, projectId: Long?) -> Unit
) {
    var scenName by remember { mutableStateOf(creation.title) }
    var scenCategory by remember { mutableStateOf("Exterior") }
    var scenAtmosphere by remember { mutableStateOf("Mágico, iluminado e vibrante") }
    var selectedProjId by remember { mutableStateOf(creation.projectId ?: projects.firstOrNull()?.id) }
    var projDropdownExpanded by remember { mutableStateOf(false) }

    val categories = listOf("Exterior", "Interior", "Mágico", "Urbano", "Espacial", "Natureza")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFECFDF5)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Landscape, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Cadastrar como Cenário", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF091124))
                        Text("A imagem gerada será vinculada aos cenários da série", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = scenName,
                    onValueChange = { scenName = it },
                    label = { Text("Nome do Cenário") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Categoria do Cenário", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        val isSel = scenCategory == cat
                        Surface(
                            onClick = { scenCategory = cat },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSel) Color(0xFF059669) else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = cat,
                                color = if (isSel) Color.White else Color(0xFF334155),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 8.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = scenAtmosphere,
                    onValueChange = { scenAtmosphere = it },
                    label = { Text("Atmosfera / Iluminação") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Vincular ao Projeto", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF64748B))
                Spacer(modifier = Modifier.height(6.dp))
                ExposedDropdownMenuBox(
                    expanded = projDropdownExpanded,
                    onExpandedChange = { projDropdownExpanded = it }
                ) {
                    val currentProjName = projects.find { it.id == selectedProjId }?.name ?: "Nenhum projeto"
                    OutlinedTextField(
                        value = currentProjName,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = projDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = projDropdownExpanded,
                        onDismissRequest = { projDropdownExpanded = false }
                    ) {
                        projects.forEach { p ->
                            DropdownMenuItem(
                                text = { Text(p.name) },
                                onClick = {
                                    selectedProjId = p.id
                                    projDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onConfirm(scenName, scenCategory, scenAtmosphere, selectedProjId)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Salvar Cenário", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. MODAL ADICIONAR AO ROTEIRO BRANCO
// ==========================================
@Composable
fun AddToRoteiroDialog(
    creation: RangaCreationEntity,
    onDismiss: () -> Unit,
    onConfirm: (notes: String) -> Unit
) {
    var notes by remember {
        mutableStateOf("Utilizar como referência visual principal da cena de introdução.")
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(22.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Adicionar ao Roteiro Branco", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF091124))
                        Text("Insere esta imagem no quadro de planejamento visual", fontSize = 11.sp, color = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Item: ${creation.title} (${creation.creationType})",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Anotações da Cena / Roteiro") },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onConfirm(notes) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Confirmar Adição", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. MODAL VISUALIZADOR DO ROTEIRO BRANCO
// ==========================================
@Composable
fun RoteiroBrancoViewerModal(
    items: List<RoteiroBrancoItemEntity>,
    onDismiss: () -> Unit,
    onDeleteItem: (RoteiroBrancoItemEntity) -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .heightIn(max = 680.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Quadro Roteiro Branco",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF091124)
                                )
                            )
                            Text(
                                text = "${items.size} itens visuais organizados para a produção",
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Explanatory banner
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCFCE7)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "O Roteiro Branco é um espaço independente para organizar cenas e conceitos visuais aprovados da Criação RANGA.",
                            fontSize = 11.5.sp,
                            color = Color(0xFF166534),
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (items.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum item adicionado ao Roteiro Branco ainda.\nClique em 'Adicionar ao Roteiro Branco' nas criações visuais.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            color = Color(0xFF94A3B8),
                            fontSize = 13.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(items) { item ->
                            val imgRes = resolveCreationImageRes(context, item.imageUri)
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        painter = painterResource(id = imgRes),
                                        contentDescription = item.title,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .clip(RoundedCornerShape(8.dp)),
                                        contentScale = ContentScale.Crop
                                    )

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
                                                color = Color(0xFFE0E7FF)
                                            ) {
                                                Text(
                                                    text = item.type,
                                                    fontSize = 9.5.sp,
                                                    color = Color(0xFF3730A3),
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        if (item.notes.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(3.dp))
                                            Text(
                                                text = item.notes,
                                                fontSize = 11.5.sp,
                                                color = Color(0xFF64748B),
                                                maxLines = 2
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { onDeleteItem(item) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remover",
                                            tint = Color(0xFF94A3B8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Fechar Roteiro Branco", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
