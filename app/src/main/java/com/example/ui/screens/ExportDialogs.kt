package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderZip
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ExportRecordEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Real Processing Modal showing animated progress and checklist
 */
@Composable
fun ExportProcessingDialog(
    exportName: String,
    exportType: String,
    exportFormat: String,
    onComplete: (ExportRecordEntity) -> Unit,
    onCancel: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0.05f) }
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val steps = listOf(
        "Inicializando motor de renderização...",
        "Carregando personagens e modelos 3D...",
        "Processando cenários e texturas...",
        "Sincronizando vozes e diálogos...",
        "Renderizando cenas e efeitos visuais...",
        "Mixando sons e trilha sonora...",
        "Codificando vídeo e embutindo legendas...",
        "Finalizando pacote de exportação..."
    )

    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(35L)
            progress = i / 100f
            if (i > 10 && currentStepIndex == 0) currentStepIndex = 1
            if (i > 25 && currentStepIndex == 1) currentStepIndex = 2
            if (i > 40 && currentStepIndex == 2) currentStepIndex = 3
            if (i > 60 && currentStepIndex == 3) currentStepIndex = 4
            if (i > 75 && currentStepIndex == 4) currentStepIndex = 5
            if (i > 88 && currentStepIndex == 5) currentStepIndex = 6
            if (i > 95 && currentStepIndex == 6) currentStepIndex = 7
        }
        delay(200L)
        val completedRecord = ExportRecordEntity(
            name = exportName,
            type = exportType,
            format = exportFormat,
            quality = "1080p (Full HD)",
            fps = "30 fps",
            audioQuality = "Alta (320 kbps)",
            includeSubtitles = true,
            includeWatermark = false,
            sizeDisplay = when (exportFormat) {
                "MP4", "MOV" -> "520 MB"
                "PDF" -> "2.4 MB"
                "PNG", "JPG" -> "45 MB"
                "WAV" -> "28 MB"
                "ZIP" -> "1.2 GB"
                else -> "350 MB"
            },
            status = "Concluído",
            dateDisplay = "Hoje, 10:30",
            durationDisplay = "08:45",
            selectedScenesCount = 12,
            details = "Exportação de $exportName ($exportType) concluída em formato $exportFormat com sucesso.",
            createdAt = System.currentTimeMillis()
        )
        onComplete(completedRecord)
    }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("export_processing_dialog")
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(40.dp),
                        color = Color(0xFF0052FF),
                        trackColor = Color(0xFFDBEAFE),
                        strokeWidth = 4.dp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Preparando projeto...",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 19.sp
                    )
                )

                Text(
                    text = "Processando $exportName ($exportFormat)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Progress Bar
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = steps.getOrElse(currentStepIndex) { "Processando..." },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF0052FF),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "${(progress * 100).toInt()}%",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF0F172A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF0052FF),
                        trackColor = Color(0xFFE2E8F0)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Checklist
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        ExportCheckItem(title = "Personagens e elementos visuais", isDone = progress > 0.25f, isInProgress = progress in 0.05f..0.25f)
                        ExportCheckItem(title = "Cenários e iluminação", isDone = progress > 0.40f, isInProgress = progress in 0.25f..0.40f)
                        ExportCheckItem(title = "Vozes e dublagem", isDone = progress > 0.60f, isInProgress = progress in 0.40f..0.60f)
                        ExportCheckItem(title = "Processando cenas selecionadas", isDone = progress > 0.75f, isInProgress = progress in 0.60f..0.75f)
                        ExportCheckItem(title = "Sons, efeitos e trilha", isDone = progress > 0.88f, isInProgress = progress in 0.75f..0.88f)
                        ExportCheckItem(title = "Renderização final e legendas", isDone = progress >= 0.98f, isInProgress = progress in 0.88f..0.98f)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedButton(
                    onClick = onCancel,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_cancel_export_process")
                ) {
                    Text("Cancelar Exportação", color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
private fun ExportCheckItem(
    title: String,
    isDone: Boolean,
    isInProgress: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            isDone -> {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(16.dp)
                )
            }
            isInProgress -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(14.dp),
                    color = Color(0xFF0052FF),
                    strokeWidth = 2.dp
                )
            }
            else -> {
                Icon(
                    imageVector = Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = when {
                    isDone -> Color(0xFF0F172A)
                    isInProgress -> Color(0xFF0052FF)
                    else -> Color(0xFF94A3B8)
                },
                fontWeight = if (isInProgress) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
            )
        )
    }
}

/**
 * Export Completed Dialog with download, share, open actions
 */
@Composable
fun ExportSuccessDialog(
    record: ExportRecordEntity,
    onDismiss: () -> Unit,
    onExportAgain: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showPreviewText by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("export_success_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDCFCE7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Exportação Concluída!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 20.sp
                    )
                )

                Text(
                    text = "Seu arquivo foi gerado e está pronto para uso.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Details Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        DetailRow(label = "Arquivo", value = "${record.name}.${record.format.lowercase()}")
                        DetailRow(label = "Formato", value = "${record.format} (${record.quality})")
                        DetailRow(label = "Tamanho", value = record.sizeDisplay)
                        DetailRow(label = "Duração", value = record.durationDisplay)
                        DetailRow(label = "Data", value = record.dateDisplay)
                        DetailRow(label = "Cenas", value = "${record.selectedScenesCount} cenas incluídas")
                        DetailRow(label = "Status", value = "🟢 Concluído")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Main Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            Toast.makeText(
                                context,
                                "📥 Arquivo '${record.name}.${record.format.lowercase()}' salvo em Downloads/RangaStudio!",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_download_exported_file")
                    ) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Baixar", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    OutlinedButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "Exportação RANGA AI STUDIO - ${record.name}")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "🎬 RANGA AI STUDIO - Exportação Concluída!\n\n" +
                                            "Projeto: ${record.projectName}\n" +
                                            "Item: ${record.name} (${record.type})\n" +
                                            "Formato: ${record.format} • ${record.quality}\n" +
                                            "Tamanho: ${record.sizeDisplay}\n" +
                                            "Duração: ${record.durationDisplay}\n\n" +
                                            "Exportado com sucesso no RANGA AI STUDIO."
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Compartilhar Exportação"))
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_share_exported_file")
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0052FF))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compartilhar", color = Color(0xFF0052FF), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            clipboardManager.setText(
                                AnnotatedString(
                                    "RANGA AI STUDIO - ${record.name}\nTipo: ${record.type}\nFormato: ${record.format}\nTamanho: ${record.sizeDisplay}\nData: ${record.dateDisplay}"
                                )
                            )
                            Toast.makeText(context, "Resumo copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFF475569))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Copiar Info", color = Color(0xFF475569), fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = onExportAgain,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(15.dp), tint = Color(0xFF475569))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Exportar Outro", color = Color(0xFF475569), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF1F5F9)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Fechar", color = Color(0xFF334155), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF0F172A),
                fontSize = 12.sp
            )
        )
    }
}

/**
 * Full Project Package Export Modal showing directory tree structure
 */
@Composable
fun FullProjectExportDialog(
    projectName: String,
    onConfirm: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val options = remember {
        mutableStateListOf(
            "Vídeos e Cenas Renderizadas" to true,
            "Roteiros e Diálogos (PDF, TXT)" to true,
            "Personagens e Fichas de Elenco" to true,
            "Cenários e Imagens de Referência" to true,
            "Vozes e Gravações de Dublagem" to true,
            "Sons e Efeitos Sonoros" to true,
            "Músicas e Trilhas Sonoras" to true,
            "Criações RANGA Studio AI" to true,
            "Metadados e Estrutura do Projeto (.json)" to true
        )
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFEFF6FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FolderZip,
                                contentDescription = null,
                                tint = Color(0xFF0052FF),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Exportar Projeto Completo",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = projectName,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF64748B),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Structure diagram
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Estrutura do Pacote ZIP:",
                            color = Color(0xFF38BDF8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$projectName.zip\n├── Vídeos/\n├── Cenas/\n├── Personagens/\n├── Cenários/\n├── Vozes/\n├── Sons/\n├── Músicas/\n├── Imagens/\n└── Roteiros/",
                            color = Color(0xFFE2E8F0),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Selecione o que incluir no arquivo ZIP:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 13.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                options.forEachIndexed { index, (label, isChecked) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { options[index] = label to !isChecked }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { options[index] = label to it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0052FF))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF1E293B),
                                fontSize = 12.5.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }

                    Button(
                        onClick = {
                            val selected = options.filter { it.second }.map { it.first }
                            onConfirm(selected)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Icon(imageVector = Icons.Default.FolderZip, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gerar ZIP (.zip)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

/**
 * Script Customization Dialog (Diálogos, Cenas, Formatos PDF/DOCX/TXT)
 */
@Composable
fun ScriptExportOptionsDialog(
    projectName: String,
    onConfirm: (format: String, includeDetails: Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf("PDF") }
    var incDialogues by remember { mutableStateOf(true) }
    var incSceneDesc by remember { mutableStateOf(true) }
    var incCharacters by remember { mutableStateOf(true) }
    var incCameraNotes by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Configurar Exportação de Roteiro",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 16.sp
                        )
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Formato do Documento:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("PDF", "DOCX", "TXT").forEach { fmt ->
                        val isSelected = selectedFormat == fmt
                        Surface(
                            onClick = { selectedFormat = fmt },
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = fmt,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFF475569),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Elementos inclusos no roteiro:",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                ScriptOptionToggle(label = "Diálogos e Falas dos Personagens", checked = incDialogues) { incDialogues = it }
                ScriptOptionToggle(label = "Descrição Visual e Ações das Cenas", checked = incSceneDesc) { incSceneDesc = it }
                ScriptOptionToggle(label = "Lista de Elenco e Personagens", checked = incCharacters) { incCharacters = it }
                ScriptOptionToggle(label = "Indicações de Câmera e Efeitos", checked = incCameraNotes) { incCameraNotes = it }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }

                    Button(
                        onClick = { onConfirm(selectedFormat, incDialogues) },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier.weight(1.3f)
                    ) {
                        Text("Exportar $selectedFormat", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScriptOptionToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF0052FF))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color(0xFF334155),
                fontSize = 12.5.sp
            )
        )
    }
}

/**
 * Inspection / Details Dialog for any History record
 */
@Composable
fun ExportRecordDetailsDialog(
    record: ExportRecordEntity,
    onDismiss: () -> Unit,
    onDownload: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit,
    onRetry: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(22.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    when (record.status) {
                                        "Concluído" -> Color(0xFFDCFCE7)
                                        "Processando" -> Color(0xFFFEF3C7)
                                        else -> Color(0xFFFEE2E2)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = when (record.status) {
                                    "Concluído" -> "✓"
                                    "Processando" -> "⏳"
                                    else -> "✕"
                                },
                                fontWeight = FontWeight.Bold,
                                color = when (record.status) {
                                    "Concluído" -> Color(0xFF16A34A)
                                    "Processando" -> Color(0xFFD97706)
                                    else -> Color(0xFFDC2626)
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = record.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F172A),
                                    fontSize = 16.sp
                                )
                            )
                            Text(
                                text = "${record.type} • ${record.format}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF64748B),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF8FAFC),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        DetailRow(label = "Projeto", value = record.projectName ?: "Aventuras das Frutas")
                        DetailRow(label = "Tipo de Exportação", value = record.type)
                        DetailRow(label = "Formato", value = record.format)
                        DetailRow(label = "Qualidade", value = record.quality)
                        DetailRow(label = "Taxa de Quadros", value = record.fps)
                        DetailRow(label = "Qualidade do Áudio", value = record.audioQuality)
                        DetailRow(label = "Tamanho", value = record.sizeDisplay)
                        DetailRow(label = "Duração", value = record.durationDisplay)
                        DetailRow(label = "Data da Exportação", value = record.dateDisplay)
                        DetailRow(
                            label = "Status",
                            value = when (record.status) {
                                "Concluído" -> "🟢 Concluído"
                                "Processando" -> "🟡 Processando"
                                else -> "🔴 Falhou"
                            }
                        )
                    }
                }

                if (record.details.isNotBlank()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Observações:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF64748B)
                        )
                    )
                    Text(
                        text = record.details,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF334155),
                            fontSize = 12.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (record.status == "Concluído") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onDownload,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Baixar", fontSize = 12.5.sp)
                        }

                        OutlinedButton(
                            onClick = onShare,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF0052FF))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Compartilhar", color = Color(0xFF0052FF), fontSize = 12.5.sp)
                        }
                    }
                } else if (record.status == "Falhou") {
                    Button(
                        onClick = onRetry,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Tentar Novamente", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDelete,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Excluir do Histórico", fontSize = 12.5.sp)
                }
            }
        }
    }
}
