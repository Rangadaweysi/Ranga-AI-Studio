package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.remote.AiStudioTask
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantModal(
    isOpen: Boolean,
    selectedTask: AiStudioTask,
    promptInput: String,
    resultText: String,
    isLoading: Boolean,
    errorMessage: String?,
    onTaskSelect: (AiStudioTask) -> Unit,
    onPromptChange: (String) -> Unit,
    onGenerateClick: () -> Unit,
    onDismiss: () -> Unit,
    onApplyToScene: ((String) -> Unit)? = null
) {
    if (!isOpen) return

    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var copied by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.testTag("ai_assistant_modal")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NavyPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = CyanGlow,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Assistente Criativo de IA",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Geração de roteiros, diálogos e personagens",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        )
                    }
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // AI Task Selector Chips
            Text(
                text = "Selecione o que deseja criar:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AiStudioTask.values().forEach { task ->
                    val isSelected = selectedTask == task
                    FilterChip(
                        selected = isSelected,
                        onClick = { onTaskSelect(task) },
                        label = {
                            Text(
                                text = task.label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NavyPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("ai_task_chip_${task.name.lowercase()}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Suggestion chips
            val suggestions = when (selectedTask) {
                AiStudioTask.STORY_IDEAS -> listOf(
                    "Novela de época com segredo de família",
                    "Série de animação sobre dragões cibernéticos",
                    "Filme de suspense em estação polar"
                )
                AiStudioTask.CREATE_CHARACTER -> listOf(
                    "Vilão carismático e calculista",
                    "Jovem prodígio inventor da periferia",
                    "Mentora sábia com passado sombrio"
                )
                AiStudioTask.CREATE_DIALOGUE -> listOf(
                    "Discussão dramática de reconciliação",
                    "Interrogatório tenso com reviravolta",
                    "Comédia rápida em momento de perigo"
                )
                AiStudioTask.CREATE_SCRIPT -> listOf(
                    "Cena de abertura com perseguição",
                    "Revelação do traidor no banquete",
                    "Despedida emocionante na ponte"
                )
                AiStudioTask.DEVELOP_SCENE -> listOf(
                    "Cena de suspense em noite chuvosa",
                    "Batalha de naves na atmosfera",
                    "Encontro secreto no antiquário"
                )
                AiStudioTask.CONTINUE_STORY -> listOf(
                    "Descobrem que o mapa era falso",
                    "O alarme dispara inesperadamente",
                    "Um novo aliado misterioso surge"
                )
                AiStudioTask.IMPROVE_DIALOGUE -> listOf(
                    "Adicionar mais subtexto e ironia",
                    "Tornar as falas mais poéticas e dramáticas",
                    "Aumentar o ritmo e dinamismo cômico"
                )
                AiStudioTask.SCENARIO_DESCRIPTION -> listOf(
                    "Catedral gótica abandonada com hologramas",
                    "Vila costeira mágica ao entardecer",
                    "Laboratório secreto submerso"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                suggestions.forEach { suggestion ->
                    Surface(
                        onClick = { onPromptChange(suggestion) },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            text = "💡 $suggestion",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // User Prompt Input
            OutlinedTextField(
                value = promptInput,
                onValueChange = onPromptChange,
                label = { Text("Instruções ou detalhes adicionais para a IA") },
                placeholder = { Text("Ex: Foque no tom dramático e crie 3 opções de conflito...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .testTag("ai_prompt_input"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NavyPrimary,
                    focusedLabelColor = NavyPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Generate Button
            Button(
                onClick = onGenerateClick,
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("ai_generate_button"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NavyPrimary,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Gerando com IA do Estúdio...")
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyanGlow)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Gerar ${selectedTask.label}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Results Section
            if (resultText.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resultado Gerado",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NavyPrimary
                                )
                            )

                            Row {
                                OutlinedButton(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Ranga AI Studio Output", resultText)
                                        clipboard.setPrimaryClip(clip)
                                        copied = true
                                        Toast.makeText(context, "Copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.height(34.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = if (copied) Icons.Default.Check else Icons.Default.ContentCopy,
                                        contentDescription = "Copiar",
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (copied) "Copiado" else "Copiar", fontSize = 11.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = resultText,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
