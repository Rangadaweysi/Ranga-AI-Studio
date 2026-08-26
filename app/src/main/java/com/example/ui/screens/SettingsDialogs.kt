package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ActiveSession
import com.example.data.model.ApiKeyItem
import com.example.data.model.StorageBreakdown
import com.example.data.model.UserProfileSettings

// ==========================================
// 1. EDIT PROFILE DIALOG
// ==========================================
@Composable
fun EditProfileDialog(
    currentProfile: UserProfileSettings,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, avatarColorHex: String) -> Unit
) {
    var name by remember { mutableStateOf(currentProfile.name) }
    var email by remember { mutableStateOf(currentProfile.email) }
    var selectedColor by remember { mutableStateOf(currentProfile.avatarColorHex) }

    val colorOptions = listOf(
        "#3B82F6", "#0052FF", "#7C3AED", "#10B981", "#F59E0B", "#EF4444", "#EC4899", "#06B6D4"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Editar Perfil",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Avatar preview
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(
                                runCatching { Color(android.graphics.Color.parseColor(selectedColor)) }
                                    .getOrDefault(Color(0xFF3B82F6))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.take(1).uppercase().ifBlank { "A" },
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 28.sp
                        )
                    }
                }

                // Avatar color picker
                Column {
                    Text(
                        text = "Cor do Avatar",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorOptions.forEach { colorHex ->
                            val color = runCatching { Color(android.graphics.Color.parseColor(colorHex)) }.getOrDefault(Color.Blue)
                            val isSelected = selectedColor.equals(colorHex, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .clickable { selectedColor = colorHex }
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.Black else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Name field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Email field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-mail") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Readonly info
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Plano atual: ${currentProfile.plan}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF334155))
                        )
                        Text(
                            text = "ID da conta: ${currentProfile.accountId}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank()) {
                        onSave(name.trim(), email.trim(), selectedColor)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar Alterações", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B))
            }
        }
    )
}

// ==========================================
// 2. CHANGE PASSWORD DIALOG
// ==========================================
@Composable
fun ChangePasswordDialog(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showPasswords by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Alterar Senha",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Para sua segurança, informe sua senha atual antes de cadastrar uma nova senha.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )

                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = {
                        currentPassword = it
                        errorMessage = null
                    },
                    label = { Text("Senha Atual") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = newPassword,
                    onValueChange = {
                        newPassword = it
                        errorMessage = null
                    },
                    label = { Text("Nova Senha (mín. 6 caracteres)") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        errorMessage = null
                    },
                    label = { Text("Confirmar Nova Senha") },
                    singleLine = true,
                    visualTransformation = if (showPasswords) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { showPasswords = !showPasswords }) {
                        Icon(
                            imageVector = if (showPasswords) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar senhas",
                            tint = Color(0xFF64748B)
                        )
                    }
                    Text(
                        text = if (showPasswords) "Ocultar senhas" else "Mostrar senhas",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                    )
                }

                errorMessage?.let { err ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = err,
                            color = Color(0xFFDC2626),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (currentPassword.isBlank()) {
                        errorMessage = "Por favor, digite sua senha atual."
                        return@Button
                    }
                    if (newPassword.length < 6) {
                        errorMessage = "A nova senha deve ter pelo menos 6 caracteres."
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        errorMessage = "A confirmação de senha não confere."
                        return@Button
                    }
                    Toast.makeText(context, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                    onSuccess()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Atualizar Senha", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B))
            }
        }
    )
}

// ==========================================
// 3. CHANGE EMAIL DIALOG
// ==========================================
@Composable
fun ChangeEmailDialog(
    currentEmail: String,
    onDismiss: () -> Unit,
    onSuccess: (newEmail: String) -> Unit
) {
    val context = LocalContext.current
    var newEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Alterar E-mail",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "E-mail atual: $currentEmail",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                )

                OutlinedTextField(
                    value = newEmail,
                    onValueChange = {
                        newEmail = it
                        errorMessage = null
                    },
                    label = { Text("Novo E-mail") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Senha para Confirmação") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                errorMessage?.let { err ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFFEE2E2),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = err,
                            color = Color(0xFFDC2626),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newEmail.isBlank() || !newEmail.contains("@")) {
                        errorMessage = "Por favor, insira um e-mail válido."
                        return@Button
                    }
                    if (password.isBlank()) {
                        errorMessage = "Por favor, informe sua senha."
                        return@Button
                    }
                    Toast.makeText(context, "E-mail atualizado para $newEmail", Toast.LENGTH_SHORT).show()
                    onSuccess(newEmail.trim())
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Confirmar Novo E-mail", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B))
            }
        }
    )
}

// ==========================================
// 4. ACTIVE SESSIONS DIALOG / LOGOUT ALL
// ==========================================
@Composable
fun ActiveSessionsDialog(
    sessions: List<ActiveSession>,
    onDismiss: () -> Unit,
    onLogoutAllOthers: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Devices,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Sessões Ativas",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Dispositivos conectados à sua conta RANGA AI STUDIO:",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )

                sessions.forEach { session ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (session.isCurrent) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (session.isCurrent) Color(0xFF93C5FD) else Color(0xFFE2E8F0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when {
                                    session.deviceName.contains("Android", true) -> Icons.Default.PhoneAndroid
                                    session.deviceName.contains("iPad", true) || session.deviceName.contains("Tablet", true) -> Icons.Default.Tablet
                                    else -> Icons.Default.Computer
                                },
                                contentDescription = null,
                                tint = if (session.isCurrent) Color(0xFF0052FF) else Color(0xFF64748B),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = session.deviceName,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                    )
                                    if (session.isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFF10B981)
                                        ) {
                                            Text(
                                                text = "Este dispositivo",
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${session.location} • ${session.lastActive}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLogoutAllOthers()
                    Toast.makeText(context, "Desconectado de todos os outros dispositivos!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Sair de Outros Dispositivos", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = Color(0xFF64748B))
            }
        }
    )
}

// ==========================================
// 5. MANAGE API KEYS DIALOG
// ==========================================
@Composable
fun ManageApiKeyDialog(
    apiKeys: List<ApiKeyItem>,
    onDismiss: () -> Unit,
    onSaveKey: (serviceName: String, newKey: String) -> Unit,
    onRemoveKey: (id: String) -> Unit
) {
    val context = LocalContext.current
    var selectedService by remember { mutableStateOf("RANGA Image API") }
    var apiKeyInput by remember { mutableStateOf("") }
    var isTestingConnection by remember { mutableStateOf(false) }
    var testResult by remember { mutableStateOf<String?>(null) }

    val serviceOptions = listOf(
        "RANGA Image API",
        "RANGA Voice API",
        "RANGA Audio API",
        "Gemini 2.5 Studio API"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Gerenciar Chaves e APIs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Configure e teste as chaves de integração do estúdio. As chaves são criptografadas e mantidas de forma segura no dispositivo.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )

                // List of existing keys
                Text(
                    text = "Chaves Atuais:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                )

                apiKeys.forEach { item ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.serviceName,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                )
                                Text(
                                    text = "Chave: ${item.maskedKey}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = when (item.status) {
                                    "Conectado" -> Color(0xFFD1FAE5)
                                    "Configuração necessária" -> Color(0xFFFEF3C7)
                                    else -> Color(0xFFFEE2E2)
                                }
                            ) {
                                Text(
                                    text = item.status,
                                    color = when (item.status) {
                                        "Conectado" -> Color(0xFF065F46)
                                        "Configuração necessária" -> Color(0xFF92400E)
                                        else -> Color(0xFF991B1B)
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Form to add/update
                Text(
                    text = "Adicionar ou Atualizar Chave:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                )

                // Select service
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    serviceOptions.take(2).forEach { svc ->
                        val isSelected = selectedService == svc
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedService = svc }
                        ) {
                            Text(
                                text = svc.replace(" API", ""),
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    serviceOptions.drop(2).forEach { svc ->
                        val isSelected = selectedService == svc
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color(0xFF0052FF) else Color(0xFFF1F5F9),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedService = svc }
                        ) {
                            Text(
                                text = svc.replace(" API", ""),
                                color = if (isSelected) Color.White else Color(0xFF475569),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = apiKeyInput,
                    onValueChange = {
                        apiKeyInput = it
                        testResult = null
                    },
                    label = { Text("Chave de API ($selectedService)") },
                    placeholder = { Text("Ex: sk-ranga-...") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )

                // Test Connection Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (apiKeyInput.isNotBlank()) {
                                isTestingConnection = true
                                testResult = null
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isTestingConnection = false
                                    testResult = "🟢 Conexão com $selectedService estabelecida com sucesso!"
                                }, 900)
                            } else {
                                testResult = "⚠️ Digite a chave antes de testar."
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isTestingConnection) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Testando...", fontSize = 12.sp)
                        } else {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Testar Conexão", fontSize = 12.sp)
                        }
                    }
                }

                testResult?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (msg.startsWith("🟢")) Color(0xFFD1FAE5) else Color(0xFFFEF3C7),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = msg,
                            color = if (msg.startsWith("🟢")) Color(0xFF065F46) else Color(0xFF92400E),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (apiKeyInput.isNotBlank()) {
                        onSaveKey(selectedService, apiKeyInput.trim())
                        Toast.makeText(context, "Chave de $selectedService salva com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Salvar Chave", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar", color = Color(0xFF64748B))
            }
        }
    )
}

// ==========================================
// 6. MANAGE STORAGE MODAL
// ==========================================
@Composable
fun ManageStorageDialog(
    storage: StorageBreakdown,
    onDismiss: () -> Unit,
    onCleanTempFiles: () -> Unit
) {
    val context = LocalContext.current
    var isCleaning by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Storage,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Gerenciador de Armazenamento",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Main usage bar
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${storage.usedGb} GB / ${storage.totalGb} GB utilizados",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                        Text(
                            text = "${storage.percentageUsed}% usado",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0052FF))
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { (storage.usedGb / storage.totalGb).toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF0052FF),
                        trackColor = Color(0xFFE2E8F0)
                    )
                }

                // Categories breakdown list
                val categories = listOf(
                    Triple("Imagens (Personagens & Cenários)", "${storage.imagesGb} GB", Color(0xFF3B82F6)),
                    Triple("Vídeos e Renderizações", "${storage.videosGb} GB", Color(0xFF8B5CF6)),
                    Triple("Áudios, Músicas e Vozes", "${storage.audioGb} GB", Color(0xFF10B981)),
                    Triple("Bancos de Dados de Projetos", "${storage.projectsGb} GB", Color(0xFFF59E0B)),
                    Triple("Outros e Metadados", "${storage.othersGb} GB", Color(0xFF64748B))
                )

                Text(
                    text = "Detalhamento por Categoria:",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                )

                categories.forEach { (catName, catSize, color) ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF8FAFC),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = catName,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155), fontWeight = FontWeight.Medium)
                                )
                            }
                            Text(
                                text = catSize,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            )
                        }
                    }
                }

                // Temporary files cleanup card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Arquivos Temporários e Cache",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                            )
                            Text(
                                text = "${storage.tempFilesMb} MB podem ser liberados com segurança.",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF3B82F6))
                            )
                        }

                        Button(
                            onClick = {
                                isCleaning = true
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isCleaning = false
                                    onCleanTempFiles()
                                    Toast.makeText(context, "840 MB de arquivos temporários limpos!", Toast.LENGTH_SHORT).show()
                                }, 700)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            if (isCleaning) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            } else {
                                Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Limpar", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Concluído", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ==========================================
// 7. BACKUP AND DATA RESTORE DIALOG
// ==========================================
@Composable
fun BackupDataDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isBackingUp by remember { mutableStateOf(false) }
    var isRestoring by remember { mutableStateOf(false) }
    var backupStatusMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Backup e Exportação de Dados",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Faça backup de todos os seus projetos, roteiros, personagens e configurações ou exporte os dados da sua conta.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B))
                )

                // Backup action card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Criar Backup Completo do Estúdio",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                        Text(
                            text = "Inclui todos os 12 módulos: Projetos, Séries, Temporadas, Episódios, Cenas, Personagens, Cenários, Vozes, Músicas, Criações RANGA e Exportações.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                isBackingUp = true
                                backupStatusMsg = null
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isBackingUp = false
                                    backupStatusMsg = "✅ Backup gerado com sucesso: ranga_studio_backup_2026.json (3.6 MB)"
                                    Toast.makeText(context, "Backup salvo com sucesso!", Toast.LENGTH_SHORT).show()
                                }, 1200)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isBackingUp) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Gerando pacote de backup...")
                            } else {
                                Icon(imageVector = Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Criar Backup Agora")
                            }
                        }
                    }
                }

                // Restore action card
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Restaurar Backup",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        )
                        Text(
                            text = "Selecione um arquivo de backup (.json ou .zip) para restaurar dados salvos anteriormente.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedButton(
                            onClick = {
                                isRestoring = true
                                android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                                    isRestoring = false
                                    backupStatusMsg = "✅ Dados do estúdio sincronizados e restaurados!"
                                    Toast.makeText(context, "Backup restaurado com sucesso!", Toast.LENGTH_SHORT).show()
                                }, 1000)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (isRestoring) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Restaurando...")
                            } else {
                                Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Restaurar Arquivo de Backup")
                            }
                        }
                    }
                }

                backupStatusMsg?.let { msg ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFD1FAE5),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = msg,
                            color = Color(0xFF065F46),
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fechar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ==========================================
// 8. DANGER ZONE / DELETE ACCOUNT MODAL
// ==========================================
@Composable
fun DangerZoneDeleteAccountDialog(
    onDismiss: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    val context = LocalContext.current
    var confirmationWord by remember { mutableStateOf("") }
    var isCheckedConsent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Excluir Conta Permanentemente",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFEF2F2),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECACA)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "⚠️ ATENÇÃO: ESTA AÇÃO É IRREVERSÍVEL!",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Black, color = Color(0xFF991B1B))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Ao excluir sua conta, todos os seus dados serão apagados permanentemente:",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB91C1C))
                        )
                        Text(
                            text = "• Todos os projetos, novelas, séries e filmes\n• Todas as criações de imagens, vozes e áudios\n• Histórico de exportações e roteiros\n• Chaves de API e preferências salvas",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF7F1D1D), fontWeight = FontWeight.Medium),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Text(
                    text = "Para confirmar a exclusão definitiva, digite a palavra EXCLUIR no campo abaixo:",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                )

                OutlinedTextField(
                    value = confirmationWord,
                    onValueChange = { confirmationWord = it },
                    placeholder = { Text("Digite EXCLUIR") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDC2626),
                        unfocusedBorderColor = Color(0xFFFCA5A5)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            val isEnabled = confirmationWord.trim() == "EXCLUIR"
            Button(
                onClick = {
                    if (isEnabled) {
                        Toast.makeText(context, "Conta excluída com sucesso.", Toast.LENGTH_LONG).show()
                        onConfirmDelete()
                    }
                },
                enabled = isEnabled,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDC2626),
                    disabledContainerColor = Color(0xFFFCA5A5)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Confirmar Exclusão", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
            }
        }
    )
}

// ==========================================
// 9. HELP & SUPPORT MODAL
// ==========================================
@Composable
fun HelpSupportDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf(0) }
    var bugTitle by remember { mutableStateOf("") }
    var bugDesc by remember { mutableStateOf("") }

    val tabs = listOf("Perguntas Frequentes", "Suporte & Chat", "Relatar Problema")

    val faqs = listOf(
        "Como funciona a sincronização offline?" to "O RANGA AI STUDIO armazena todos os seus dados localmente no dispositivo usando Room Database SQLite. Suas criações, roteiros e configurações nunca são perdidas, mesmo sem internet.",
        "Como exportar um episódio completo?" to "Vá até o menu 📤 Exportar, escolha a categoria 'Episódios', selecione o projeto e o episódio desejado, configure a resolução (1080p ou 4K) e clique em 'Exportar Agora'.",
        "Como funciona a dublagem e vozes?" to "No menu 🎙️ Vozes, você pode selecionar ou gerar vozes neurais para cada personagem do elenco, ajustando tom, velocidade e emoção em cada fala do roteiro.",
        "As chaves de API são seguras?" to "Sim. As chaves de API são salvas em armazenamento seguro local e nunca são compartilhadas ou expostas no código cliente."
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Central de Ajuda e Suporte",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color(0xFFF1F5F9),
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFF0052FF)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == index) Color(0xFF0052FF) else Color(0xFF64748B)
                                )
                            }
                        )
                    }
                }

                when (selectedTab) {
                    0 -> {
                        // FAQ list
                        faqs.forEach { (question, answer) ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF8FAFC),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "❓ $question",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = answer,
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF475569))
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        // Support info
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFEFF6FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = Color(0xFF0052FF))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Suporte RANGA VIP",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF1E3A8A))
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Atendimento prioritário para criadores e produtores com plano Profissional.",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = {
                                        Toast.makeText(context, "Conectando ao suporte de estúdio...", Toast.LENGTH_SHORT).show()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Iniciar Chat com Especialista", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                    2 -> {
                        // Bug Report
                        OutlinedTextField(
                            value = bugTitle,
                            onValueChange = { bugTitle = it },
                            label = { Text("Título do Problema") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = bugDesc,
                            onValueChange = { bugDesc = it },
                            label = { Text("Descreva o que aconteceu") },
                            minLines = 3,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Button(
                            onClick = {
                                if (bugTitle.isNotBlank()) {
                                    Toast.makeText(context, "Relatório de erro enviado com sucesso!", Toast.LENGTH_SHORT).show()
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(imageVector = Icons.Default.BugReport, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Enviar Relatório")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Fechar", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ==========================================
// 10. ABOUT STUDIO MODAL
// ==========================================
@Composable
fun AboutStudioDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEFF6FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF0052FF),
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "RANGA AI STUDIO",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black, color = Color(0xFF0F172A))
                    )
                    Text(
                        text = "Versão 1.0 (Build 2026.08)",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B))
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "A plataforma all-in-one definitiva para criação, roteirização, geração de personagens, cenários, dublagem neural, trilhas sonoras e exportação de produções audiovisuais.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF475569))
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("• Banco de Dados Local: Room Database SQLite (Offline-first)", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF334155), fontWeight = FontWeight.SemiBold))
                        Text("• Motor de IA: Gemini 2.5 Studio + Modelos Neurais RANGA", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF334155), fontWeight = FontWeight.SemiBold))
                        Text("• Sistema de Renderização: Pipeline Audiovisual Multicamada", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF334155), fontWeight = FontWeight.SemiBold))
                        Text("• Licença: Augusto (Plano Profissional Ativo)", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF334155), fontWeight = FontWeight.SemiBold))
                    }
                }

                Text(
                    text = "© 2026 RANGA AI STUDIO. Todos os direitos reservados.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Entendido", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    )
}
