package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveSession
import com.example.data.model.ApiKeyItem
import com.example.data.model.AppearanceSettings
import com.example.data.model.AudioSettings
import com.example.data.model.ExportDefaultSettings
import com.example.data.model.LanguageSettings
import com.example.data.model.NotificationSettings
import com.example.data.model.ProjectsProductionSettings
import com.example.data.model.RangaCreationSettings
import com.example.data.model.StudioFullSettings
import com.example.data.model.UserProfileSettings
import com.example.data.model.VoiceSettings
import kotlinx.coroutines.delay

enum class SettingsCategory(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    ACCOUNT("account", "Conta", "Informações e segurança da conta", Icons.Default.Person),
    APPEARANCE("appearance", "Aparência", "Tema, cores e interface", Icons.Default.Palette),
    LANGUAGE("language", "Idioma", "Idioma do aplicativo", Icons.Default.Language),
    RANGA_CREATION("ranga_creation", "Criação RANGA", "Configurações de geração de imagens", Icons.Default.AutoAwesome),
    VOICES("voices", "Vozes", "Configurações de vozes", Icons.Default.Mic),
    AUDIO("audio", "Áudio", "Músicas, efeitos e ambientes", Icons.AutoMirrored.Filled.QueueMusic),
    PROJECTS("projects", "Projetos e produção", "Salvamento e backups", Icons.Default.Folder),
    EXPORT("export", "Exportação", "Padrões de exportação", Icons.Default.Share),
    NOTIFICATIONS("notifications", "Notificações", "Alertas e mensagens", Icons.Default.Notifications),
    STORAGE("storage", "Armazenamento", "Gerencie seu armazenamento", Icons.Default.Storage),
    API_KEYS("api_keys", "Chaves e serviços", "APIs e serviços externos", Icons.Default.VpnKey),
    SECURITY("security", "Segurança", "Senha, sessões e privacidade", Icons.Default.Security),
    BACKUP("backup", "Backup e dados", "Backup, restauração e dados", Icons.Default.CloudUpload),
    HELP("help", "Ajuda", "Suporte e informações", Icons.Default.HelpOutline),
    ABOUT("about", "Sobre", "Informações do aplicativo", Icons.Default.Info)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    settings: StudioFullSettings = StudioFullSettings(),
    onUpdateUserProfile: (name: String, email: String, avatarColorHex: String) -> Unit = { _, _, _ -> },
    onUpdateAppearance: (themeMode: String, primaryColorHex: String, uiSize: String, density: String) -> Unit = { _, _, _, _ -> },
    onUpdateLanguage: (language: String) -> Unit = {},
    onUpdateRangaCreation: (RangaCreationSettings) -> Unit = {},
    onUpdateVoices: (VoiceSettings) -> Unit = {},
    onUpdateAudio: (AudioSettings) -> Unit = {},
    onUpdateProjects: (ProjectsProductionSettings) -> Unit = {},
    onUpdateExport: (ExportDefaultSettings) -> Unit = {},
    onUpdateNotifications: (NotificationSettings) -> Unit = {},
    onSaveApiKey: (serviceName: String, apiKey: String) -> Unit = { _, _ -> },
    onRemoveApiKey: (id: String) -> Unit = {},
    onLogoutOtherSessions: () -> Unit = {},
    onCleanTempFiles: () -> Unit = {},
    onSyncNow: () -> Unit = {},
    onDeleteAccount: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedCategory by remember { mutableStateOf<SettingsCategory?>(SettingsCategory.ACCOUNT) }
    var searchQuery by remember { mutableStateOf("") }
    var showSaveToast by remember { mutableStateOf(false) }

    // Dialog state controllers
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showActiveSessionsDialog by remember { mutableStateOf(false) }
    var showManageApiKeyDialog by remember { mutableStateOf(false) }
    var showManageStorageDialog by remember { mutableStateOf(false) }
    var showBackupDataDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var showHelpSupportDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    fun triggerSaveFeedback() {
        showSaveToast = true
    }

    LaunchedEffect(showSaveToast) {
        if (showSaveToast) {
            delay(2000)
            showSaveToast = false
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
    ) {
        val isWide = maxWidth >= 860.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isWide) 24.dp else 12.dp, vertical = 12.dp)
        ) {
            // ==========================================
            // TOP HEADER BAR (Matching reference)
            // ==========================================
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Title + Subtitle
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF0052FF)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Configurações",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
                                fontSize = if (isWide) 22.sp else 18.sp
                            )
                        )
                        Text(
                            text = "Personalize sua experiência e gerencie todas as configurações do RANGA AI STUDIO.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            ),
                            maxLines = 1
                        )
                    }
                }

                // Right: Search + Help + Notifications + User Pill
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isWide) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Pesquisar configurações...", fontSize = 12.sp, color = Color(0xFF94A3B8)) },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(18.dp))
                            },
                            singleLine = true,
                            modifier = Modifier
                                .width(240.dp)
                                .height(44.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = Color(0xFF0052FF),
                                unfocusedBorderColor = Color(0xFFE2E8F0)
                            )
                        )
                    }

                    // Help Icon
                    IconButton(
                        onClick = { showHelpSupportDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.HelpOutline, contentDescription = "Ajuda", tint = Color(0xFF475569), modifier = Modifier.size(20.dp))
                    }

                    // Notifications Bell with Badge
                    IconButton(
                        onClick = {
                            selectedCategory = SettingsCategory.NOTIFICATIONS
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color.White, CircleShape)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFF0052FF),
                                    contentColor = Color.White
                                ) {
                                    Text("3", fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notificações", tint = Color(0xFF475569), modifier = Modifier.size(20.dp))
                        }
                    }

                    // User Profile Pill (Augusto - Plano Profissional)
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.clickable { showEditProfileDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(
                                        runCatching { Color(android.graphics.Color.parseColor(settings.userProfile.avatarColorHex)) }
                                            .getOrDefault(Color(0xFF3B82F6))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = settings.userProfile.avatarInitials,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            if (isWide) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = settings.userProfile.name,
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), fontSize = 11.5.sp)
                                    )
                                    Text(
                                        text = "Plano ${settings.userProfile.plan}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 9.5.sp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Save feedback indicator
            AnimatedVisibility(
                visible = showSaveToast,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFD1FAE5),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "🟢 Alterações salvas com sucesso no estúdio!",
                            color = Color(0xFF065F46),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ==========================================
            // MAIN BODY: SIDEBAR CATEGORIES + SETTINGS CONTENT
            // ==========================================
            if (isWide) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left Sub-Navigation Menu Pane (Matching reference)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .width(260.dp)
                            .fillMaxHeight()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(SettingsCategory.values()) { category ->
                                val isSelected = selectedCategory == category
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFFEFF6FF) else Color.Transparent,
                                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)) else null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { selectedCategory = category }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = category.icon,
                                            contentDescription = null,
                                            tint = if (isSelected) Color(0xFF0052FF) else Color(0xFF64748B),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = category.title,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF334155),
                                                    fontSize = 12.sp
                                                )
                                            )
                                            Text(
                                                text = category.description,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = if (isSelected) Color(0xFF3B82F6) else Color(0xFF94A3B8),
                                                    fontSize = 10.sp
                                                ),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Right Main Content: Settings Cards Canvas
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SettingsMainPanels(
                            settings = settings,
                            isWide = true,
                            onTriggerSave = { triggerSaveFeedback() },
                            onEditProfile = { showEditProfileDialog = true },
                            onChangePassword = { showChangePasswordDialog = true },
                            onChangeEmail = { showChangeEmailDialog = true },
                            onActiveSessions = { showActiveSessionsDialog = true },
                            onManageApiKeys = { showManageApiKeyDialog = true },
                            onManageStorage = { showManageStorageDialog = true },
                            onBackupData = { showBackupDataDialog = true },
                            onDeleteAccount = { showDeleteAccountDialog = true },
                            onHelp = { showHelpSupportDialog = true },
                            onAbout = { showAboutDialog = true },
                            onUpdateAppearance = onUpdateAppearance,
                            onUpdateRangaCreation = onUpdateRangaCreation,
                            onUpdateProjects = onUpdateProjects,
                            onUpdateNotifications = onUpdateNotifications,
                            onCleanTempFiles = onCleanTempFiles,
                            onSyncNow = onSyncNow
                        )
                    }
                }
            } else {
                // Mobile layout: Horizontal tabs at top + single vertical scroll column
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Category Chips Bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        SettingsCategory.values().forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Color(0xFF0052FF) else Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                                modifier = Modifier.clickable { selectedCategory = cat }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else Color(0xFF64748B),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = cat.title,
                                        color = if (isSelected) Color.White else Color(0xFF334155),
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    SettingsMainPanels(
                        settings = settings,
                        isWide = false,
                        onTriggerSave = { triggerSaveFeedback() },
                        onEditProfile = { showEditProfileDialog = true },
                        onChangePassword = { showChangePasswordDialog = true },
                        onChangeEmail = { showChangeEmailDialog = true },
                        onActiveSessions = { showActiveSessionsDialog = true },
                        onManageApiKeys = { showManageApiKeyDialog = true },
                        onManageStorage = { showManageStorageDialog = true },
                        onBackupData = { showBackupDataDialog = true },
                        onDeleteAccount = { showDeleteAccountDialog = true },
                        onHelp = { showHelpSupportDialog = true },
                        onAbout = { showAboutDialog = true },
                        onUpdateAppearance = onUpdateAppearance,
                        onUpdateRangaCreation = onUpdateRangaCreation,
                        onUpdateProjects = onUpdateProjects,
                        onUpdateNotifications = onUpdateNotifications,
                        onCleanTempFiles = onCleanTempFiles,
                        onSyncNow = onSyncNow
                    )
                }
            }
        }
    }

    // ==========================================
    // DIALOGS
    // ==========================================
    if (showEditProfileDialog) {
        EditProfileDialog(
            currentProfile = settings.userProfile,
            onDismiss = { showEditProfileDialog = false },
            onSave = { name, email, colorHex ->
                onUpdateUserProfile(name, email, colorHex)
                triggerSaveFeedback()
                showEditProfileDialog = false
            }
        )
    }

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showChangePasswordDialog = false },
            onSuccess = {
                triggerSaveFeedback()
                showChangePasswordDialog = false
            }
        )
    }

    if (showChangeEmailDialog) {
        ChangeEmailDialog(
            currentEmail = settings.userProfile.email,
            onDismiss = { showChangeEmailDialog = false },
            onSuccess = { newEmail ->
                onUpdateUserProfile(settings.userProfile.name, newEmail, settings.userProfile.avatarColorHex)
                triggerSaveFeedback()
                showChangeEmailDialog = false
            }
        )
    }

    if (showActiveSessionsDialog) {
        ActiveSessionsDialog(
            sessions = settings.sessions,
            onDismiss = { showActiveSessionsDialog = false },
            onLogoutAllOthers = {
                onLogoutOtherSessions()
                triggerSaveFeedback()
                showActiveSessionsDialog = false
            }
        )
    }

    if (showManageApiKeyDialog) {
        ManageApiKeyDialog(
            apiKeys = settings.apiKeys,
            onDismiss = { showManageApiKeyDialog = false },
            onSaveKey = { svc, key ->
                onSaveApiKey(svc, key)
                triggerSaveFeedback()
                showManageApiKeyDialog = false
            },
            onRemoveKey = { id ->
                onRemoveApiKey(id)
                triggerSaveFeedback()
            }
        )
    }

    if (showManageStorageDialog) {
        ManageStorageDialog(
            storage = settings.storage,
            onDismiss = { showManageStorageDialog = false },
            onCleanTempFiles = {
                onCleanTempFiles()
                triggerSaveFeedback()
            }
        )
    }

    if (showBackupDataDialog) {
        BackupDataDialog(
            onDismiss = { showBackupDataDialog = false }
        )
    }

    if (showDeleteAccountDialog) {
        DangerZoneDeleteAccountDialog(
            onDismiss = { showDeleteAccountDialog = false },
            onConfirmDelete = {
                onDeleteAccount()
                showDeleteAccountDialog = false
            }
        )
    }

    if (showHelpSupportDialog) {
        HelpSupportDialog(
            onDismiss = { showHelpSupportDialog = false }
        )
    }

    if (showAboutDialog) {
        AboutStudioDialog(
            onDismiss = { showAboutDialog = false }
        )
    }
}

// ==========================================
// MAIN SETTINGS PANELS (Organized Cards)
// ==========================================
@Composable
fun SettingsMainPanels(
    settings: StudioFullSettings,
    isWide: Boolean,
    onTriggerSave: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onChangeEmail: () -> Unit,
    onActiveSessions: () -> Unit,
    onManageApiKeys: () -> Unit,
    onManageStorage: () -> Unit,
    onBackupData: () -> Unit,
    onDeleteAccount: () -> Unit,
    onHelp: () -> Unit,
    onAbout: () -> Unit,
    onUpdateAppearance: (String, String, String, String) -> Unit,
    onUpdateRangaCreation: (RangaCreationSettings) -> Unit,
    onUpdateProjects: (ProjectsProductionSettings) -> Unit,
    onUpdateNotifications: (NotificationSettings) -> Unit,
    onCleanTempFiles: () -> Unit,
    onSyncNow: () -> Unit
) {
    // Top Card: User Profile Information
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Informações da conta",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (isWide) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Profile Details
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1.3f)
                    ) {
                        // Avatar with camera badge
                        Box(contentAlignment = Alignment.BottomEnd) {
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(
                                        runCatching { Color(android.graphics.Color.parseColor(settings.userProfile.avatarColorHex)) }
                                            .getOrDefault(Color(0xFF3B82F6))
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = settings.userProfile.avatarInitials,
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0052FF))
                                    .clickable { onEditProfile() }
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = "Alterar foto",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Nome",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp)
                            )
                            Text(
                                text = settings.userProfile.name,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "E-mail",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp)
                            )
                            Text(
                                text = settings.userProfile.email,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = Color(0xFF334155))
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column {
                                    Text("Plano atual", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp))
                                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEFF6FF)) {
                                        Text(
                                            text = settings.userProfile.plan,
                                            color = Color(0xFF0052FF),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Column {
                                    Text("Membro desde", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp))
                                    Text(settings.userProfile.memberSince, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF1E293B), fontWeight = FontWeight.SemiBold))
                                }
                                Column {
                                    Text("ID da conta", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF94A3B8), fontSize = 10.sp))
                                    Text(settings.userProfile.accountId, style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.Normal))
                                }
                            }
                        }
                    }

                    // Right Quick Action Buttons
                    Column(
                        modifier = Modifier.weight(0.9f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Ações rápidas",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF64748B))
                        )

                        OutlinedButton(
                            onClick = onEditProfile,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF334155))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar perfil", color = Color(0xFF334155), fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onChangePassword,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF334155))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Alterar senha", color = Color(0xFF334155), fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onChangeEmail,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF334155))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Alterar e-mail", color = Color(0xFF334155), fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = onActiveSessions,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626))
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFDC2626))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sair de todos os dispositivos", color = Color(0xFFDC2626), fontSize = 12.sp)
                        }
                    }
                }
            } else {
                // Mobile stacked layout
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF3B82F6)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = settings.userProfile.avatarInitials,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(settings.userProfile.name, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                            Text(settings.userProfile.email, fontSize = 12.sp, color = Color(0xFF64748B))
                            Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFEFF6FF)) {
                                Text(
                                    text = "Plano ${settings.userProfile.plan}",
                                    color = Color(0xFF0052FF),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = onEditProfile, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                            Text("Editar", fontSize = 11.sp)
                        }
                        OutlinedButton(onClick = onChangePassword, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                            Text("Senha", fontSize = 11.sp)
                        }
                        OutlinedButton(onClick = onChangeEmail, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                            Text("E-mail", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    // ==========================================
    // 4 MAIN CARDS GRID (Aparência, Criação RANGA, Projetos, Notificações)
    // ==========================================
    if (isWide) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Aparência
            AppearanceCard(
                settings = settings.appearance,
                onUpdate = { theme, col, size, den ->
                    onUpdateAppearance(theme, col, size, den)
                    onTriggerSave()
                },
                modifier = Modifier.weight(1f)
            )

            // Card 2: Criação RANGA
            RangaCreationSettingsCard(
                settings = settings.rangaCreation,
                onUpdate = {
                    onUpdateRangaCreation(it)
                    onTriggerSave()
                },
                modifier = Modifier.weight(1.1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 3: Projetos e Produção
            ProjectsProductionCard(
                settings = settings.projectsProduction,
                onUpdate = {
                    onUpdateProjects(it)
                    onTriggerSave()
                },
                modifier = Modifier.weight(1f)
            )

            // Card 4: Notificações
            NotificationsSettingsCard(
                settings = settings.notifications,
                onUpdate = {
                    onUpdateNotifications(it)
                    onTriggerSave()
                },
                modifier = Modifier.weight(1f)
            )
        }
    } else {
        AppearanceCard(
            settings = settings.appearance,
            onUpdate = { theme, col, size, den ->
                onUpdateAppearance(theme, col, size, den)
                onTriggerSave()
            },
            modifier = Modifier.fillMaxWidth()
        )
        RangaCreationSettingsCard(
            settings = settings.rangaCreation,
            onUpdate = {
                onUpdateRangaCreation(it)
                onTriggerSave()
            },
            modifier = Modifier.fillMaxWidth()
        )
        ProjectsProductionCard(
            settings = settings.projectsProduction,
            onUpdate = {
                onUpdateProjects(it)
                onTriggerSave()
            },
            modifier = Modifier.fillMaxWidth()
        )
        NotificationsSettingsCard(
            settings = settings.notifications,
            onUpdate = {
                onUpdateNotifications(it)
                onTriggerSave()
            },
            modifier = Modifier.fillMaxWidth()
        )
    }

    // ==========================================
    // BOTTOM 3 CARDS: Armazenamento, Chaves e Serviços, Sincronização
    // ==========================================
    if (isWide) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Card 1: Armazenamento
            StorageCard(
                storage = settings.storage,
                onCleanTemp = onCleanTempFiles,
                onManage = onManageStorage,
                modifier = Modifier.weight(1.1f)
            )

            // Card 2: Chaves e serviços
            ApiKeysCard(
                apiKeys = settings.apiKeys,
                onManageAll = onManageApiKeys,
                modifier = Modifier.weight(1f)
            )

            // Card 3: Sincronização
            SyncCard(
                sync = settings.sync,
                onSyncNow = onSyncNow,
                modifier = Modifier.weight(0.9f)
            )
        }
    } else {
        StorageCard(
            storage = settings.storage,
            onCleanTemp = onCleanTempFiles,
            onManage = onManageStorage,
            modifier = Modifier.fillMaxWidth()
        )
        ApiKeysCard(
            apiKeys = settings.apiKeys,
            onManageAll = onManageApiKeys,
            modifier = Modifier.fillMaxWidth()
        )
        SyncCard(
            sync = settings.sync,
            onSyncNow = onSyncNow,
            modifier = Modifier.fillMaxWidth()
        )
    }

    // ==========================================
    // BOTTOM BANNER: ZONA DE PERIGO
    // ==========================================
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEF3C7)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Zona de perigo",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                    )
                    Text(
                        text = "Ações permanentes que não podem ser desfeitas. Tenha cuidado.",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309))
                    )
                }
            }

            OutlinedButton(
                onClick = onDeleteAccount,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF87171)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Excluir conta", color = Color(0xFFDC2626), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// CARD: 🎨 APARÊNCIA
// ==========================================
@Composable
fun AppearanceCard(
    settings: AppearanceSettings,
    onUpdate: (theme: String, colorHex: String, size: String, density: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var themeMode by remember(settings.themeMode) { mutableStateOf(settings.themeMode) }
    var primaryColor by remember(settings.primaryColorHex) { mutableStateOf(settings.primaryColorHex) }
    var uiSize by remember(settings.uiSize) { mutableStateOf(settings.uiSize) }
    var density by remember(settings.density) { mutableStateOf(settings.density) }

    val colorOptions = listOf(
        "#0052FF", "#7C3AED", "#10B981", "#F59E0B", "#EF4444", "#06B6D4"
    )

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Palette, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Aparência", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            // Tema (Claro, Escuro, Seguir sistema)
            Column {
                Text("Tema", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val themes = listOf("Claro" to Icons.Default.LightMode, "Escuro" to Icons.Default.DarkMode, "Seguir sistema" to Icons.Default.Computer)
                    themes.forEach { (tName, tIcon) ->
                        val isSelected = themeMode == tName
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    themeMode = tName
                                    onUpdate(tName, primaryColor, uiSize, density)
                                }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = tIcon,
                                    contentDescription = null,
                                    tint = if (isSelected) Color(0xFF0052FF) else Color(0xFF64748B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = tName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }

            // Cor principal
            Column {
                Text("Cor principal", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    colorOptions.forEach { colHex ->
                        val color = runCatching { Color(android.graphics.Color.parseColor(colHex)) }.getOrDefault(Color.Blue)
                        val isSelected = primaryColor.equals(colHex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable {
                                    primaryColor = colHex
                                    onUpdate(themeMode, colHex, uiSize, density)
                                }
                                .border(if (isSelected) 2.dp else 0.dp, Color.Black, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }

            // Tamanho da interface (Pequeno, Médio, Grande)
            Column {
                Text("Tamanho da interface", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Pequeno", "Médio", "Grande").forEach { s ->
                        val isSelected = uiSize == s
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    uiSize = s
                                    onUpdate(themeMode, primaryColor, s, density)
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = s,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }

            // Densidade (Compacta, Normal, Espaçosa)
            Column {
                Text("Densidade", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Compacta", "Normal", "Espaçosa").forEach { d ->
                        val isSelected = density == d
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    density = d
                                    onUpdate(themeMode, primaryColor, uiSize, d)
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = d,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// CARD: ✨ CRIAÇÃO RANGA
// ==========================================
@Composable
fun RangaCreationSettingsCard(
    settings: RangaCreationSettings,
    onUpdate: (RangaCreationSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    var generationModel by remember(settings.generationModel) { mutableStateOf(settings.generationModel) }
    var quality by remember(settings.defaultQuality) { mutableStateOf(settings.defaultQuality) }
    var aspectRatio by remember(settings.defaultAspectRatio) { mutableStateOf(settings.defaultAspectRatio) }
    var style by remember(settings.defaultStyle) { mutableStateOf(settings.defaultStyle) }
    var imagesCount by remember(settings.imagesPerGeneration) { mutableStateOf(settings.imagesPerGeneration) }
    var autoSave by remember(settings.autoSaveCreations) { mutableStateOf(settings.autoSaveCreations) }
    var savePrompts by remember(settings.savePromptsToHistory) { mutableStateOf(settings.savePromptsToHistory) }
    var showHistory by remember(settings.showCreationHistory) { mutableStateOf(settings.showCreationHistory) }
    var confirmDelete by remember(settings.confirmBeforeDelete) { mutableStateOf(settings.confirmBeforeDelete) }

    var expandedModel by remember { mutableStateOf(false) }
    var expandedQuality by remember { mutableStateOf(false) }
    var expandedFormat by remember { mutableStateOf(false) }
    var expandedStyle by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Criação RANGA", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            // Modelo de geração
            DropdownSettingRow(
                label = "Modelo de geração",
                selectedValue = generationModel,
                options = listOf("RANGA Image Pro", "RANGA Image Flash", "RANGA Ultra HQ", "Gemini 2.5 Image Studio"),
                expanded = expandedModel,
                onExpandChange = { expandedModel = it },
                onSelect = {
                    generationModel = it
                    onUpdate(settings.copy(generationModel = it))
                }
            )

            // Qualidade padrão
            DropdownSettingRow(
                label = "Qualidade padrão",
                selectedValue = quality,
                options = listOf("Alta", "Ultra (4K)", "Padrão", "Econômica"),
                expanded = expandedQuality,
                onExpandChange = { expandedQuality = it },
                onSelect = {
                    quality = it
                    onUpdate(settings.copy(defaultQuality = it))
                }
            )

            // Formato padrão
            DropdownSettingRow(
                label = "Formato padrão",
                selectedValue = aspectRatio,
                options = listOf("Paisagem (16:9)", "Retrato (9:16)", "Quadrado (1:1)", "4:3"),
                expanded = expandedFormat,
                onExpandChange = { expandedFormat = it },
                onSelect = {
                    aspectRatio = it
                    onUpdate(settings.copy(defaultAspectRatio = it))
                }
            )

            // Estilo padrão
            DropdownSettingRow(
                label = "Estilo padrão",
                selectedValue = style,
                options = listOf("Cartoon 3D", "Anime", "Realista", "Cinemático", "2D Infantil", "Estilizado"),
                expanded = expandedStyle,
                onExpandChange = { expandedStyle = it },
                onSelect = {
                    style = it
                    onUpdate(settings.copy(defaultStyle = it))
                }
            )

            // Imagens por geração
            Column {
                Text("Imagens por geração", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1, 2, 4, 8).forEach { count ->
                        val isSelected = imagesCount == count
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) Color(0xFFEFF6FF) else Color(0xFFF8FAFC),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Color(0xFF0052FF) else Color(0xFFE2E8F0)),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    imagesCount = count
                                    onUpdate(settings.copy(imagesPerGeneration = count))
                                }
                        ) {
                            Box(modifier = Modifier.padding(vertical = 6.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = count.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF0052FF) else Color(0xFF475569)
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            // Switches
            SwitchRow("Salvar automaticamente as criações", autoSave) {
                autoSave = it
                onUpdate(settings.copy(autoSaveCreations = it))
            }
            SwitchRow("Salvar prompts no histórico", savePrompts) {
                savePrompts = it
                onUpdate(settings.copy(savePromptsToHistory = it))
            }
            SwitchRow("Mostrar histórico de criações", showHistory) {
                showHistory = it
                onUpdate(settings.copy(showCreationHistory = it))
            }
            SwitchRow("Confirmar antes de excluir", confirmDelete) {
                confirmDelete = it
                onUpdate(settings.copy(confirmBeforeDelete = it))
            }
        }
    }
}

// ==========================================
// CARD: 🎬 PROJETOS E PRODUÇÃO
// ==========================================
@Composable
fun ProjectsProductionCard(
    settings: ProjectsProductionSettings,
    onUpdate: (ProjectsProductionSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    var autoSave by remember(settings.autoSave) { mutableStateOf(settings.autoSave) }
    var autoBackup by remember(settings.autoBackup) { mutableStateOf(settings.autoBackup) }
    var confirmDelete by remember(settings.confirmDelete) { mutableStateOf(settings.confirmDelete) }
    var showRecent by remember(settings.showRecentProjects) { mutableStateOf(settings.showRecentProjects) }
    var restoreLast by remember(settings.restoreLastOpenedProject) { mutableStateOf(settings.restoreLastOpenedProject) }
    var interval by remember(settings.autoSaveInterval) { mutableStateOf(settings.autoSaveInterval) }
    var expandedInterval by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Folder, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Projetos e produção", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            SwitchRow("Salvamento automático", autoSave) {
                autoSave = it
                onUpdate(settings.copy(autoSave = it))
            }
            SwitchRow("Criar backup automático", autoBackup) {
                autoBackup = it
                onUpdate(settings.copy(autoBackup = it))
            }
            SwitchRow("Confirmar exclusão", confirmDelete) {
                confirmDelete = it
                onUpdate(settings.copy(confirmDelete = it))
            }
            SwitchRow("Mostrar projetos recentes", showRecent) {
                showRecent = it
                onUpdate(settings.copy(showRecentProjects = it))
            }
            SwitchRow("Restaurar último projeto aberto", restoreLast) {
                restoreLast = it
                onUpdate(settings.copy(restoreLastOpenedProject = it))
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            DropdownSettingRow(
                label = "Intervalo de salvamento automático",
                selectedValue = interval,
                options = listOf("1 minuto", "5 minutos", "10 minutos", "15 minutos", "Desativado"),
                expanded = expandedInterval,
                onExpandChange = { expandedInterval = it },
                onSelect = {
                    interval = it
                    onUpdate(settings.copy(autoSaveInterval = it))
                }
            )
        }
    }
}

// ==========================================
// CARD: 🔔 NOTIFICAÇÕES
// ==========================================
@Composable
fun NotificationsSettingsCard(
    settings: NotificationSettings,
    onUpdate: (NotificationSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    var creationDone by remember(settings.creationCompleted) { mutableStateOf(settings.creationCompleted) }
    var exportDone by remember(settings.exportCompleted) { mutableStateOf(settings.exportCompleted) }
    var errorAlerts by remember(settings.errorAlerts) { mutableStateOf(settings.errorAlerts) }
    var storageWarn by remember(settings.storageWarnings) { mutableStateOf(settings.storageWarnings) }
    var appUpdates by remember(settings.appUpdates) { mutableStateOf(settings.appUpdates) }
    var projectActs by remember(settings.projectActivities) { mutableStateOf(settings.projectActivities) }
    var silentMode by remember(settings.silentMode) { mutableStateOf(settings.silentMode) }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.NotificationsActive, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Notificações", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            SwitchRow("Notificações de criação concluída", creationDone) {
                creationDone = it
                onUpdate(settings.copy(creationCompleted = it))
            }
            SwitchRow("Notificações de exportação concluída", exportDone) {
                exportDone = it
                onUpdate(settings.copy(exportCompleted = it))
            }
            SwitchRow("Notificações de erro", errorAlerts) {
                errorAlerts = it
                onUpdate(settings.copy(errorAlerts = it))
            }
            SwitchRow("Avisos de armazenamento", storageWarn) {
                storageWarn = it
                onUpdate(settings.copy(storageWarnings = it))
            }
            SwitchRow("Atualizações do aplicativo", appUpdates) {
                appUpdates = it
                onUpdate(settings.copy(appUpdates = it))
            }
            SwitchRow("Atividades dos projetos", projectActs) {
                projectActs = it
                onUpdate(settings.copy(projectActivities = it))
            }

            HorizontalDivider(color = Color(0xFFF1F5F9))

            SwitchRow("Modo silencioso", silentMode) {
                silentMode = it
                onUpdate(settings.copy(silentMode = it))
            }
        }
    }
}

// ==========================================
// CARD: 💾 ARMAZENAMENTO
// ==========================================
@Composable
fun StorageCard(
    storage: com.example.data.model.StorageBreakdown,
    onCleanTemp: () -> Unit,
    onManage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Storage, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Armazenamento", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${storage.usedGb} GB / ${storage.totalGb} GB utilizados",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
                )
                Text(
                    text = "${storage.percentageUsed}% usado",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0052FF), fontWeight = FontWeight.Bold)
                )
            }

            LinearProgressIndicator(
                progress = { (storage.usedGb / storage.totalGb).toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF0052FF),
                trackColor = Color(0xFFE2E8F0)
            )

            // 5 Blocks (Imagens, Vídeos, Áudios, Projetos, Outros)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StorageCategoryPill("Imagens", "${storage.imagesGb} GB", Icons.Default.Image, Modifier.weight(1f))
                StorageCategoryPill("Vídeos", "${storage.videosGb} GB", Icons.Default.Videocam, Modifier.weight(1f))
                StorageCategoryPill("Áudios", "${storage.audioGb} GB", Icons.AutoMirrored.Filled.QueueMusic, Modifier.weight(1f))
                StorageCategoryPill("Projetos", "${storage.projectsGb} GB", Icons.Default.Folder, Modifier.weight(1f))
                StorageCategoryPill("Outros", "${storage.othersGb} GB", Icons.Default.Storage, Modifier.weight(1f))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedButton(
                    onClick = onCleanTemp,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Limpar temporários", fontSize = 10.sp, maxLines = 1)
                }

                OutlinedButton(
                    onClick = onManage,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Storage, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Gerenciar arquivos", fontSize = 10.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun StorageCategoryPill(title: String, size: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFFF8FAFC),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(14.dp))
            Text(text = title, fontSize = 9.sp, color = Color(0xFF64748B), maxLines = 1)
            Text(text = size, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A), maxLines = 1)
        }
    }
}

// ==========================================
// CARD: 🔑 CHAVES E SERVIÇOS
// ==========================================
@Composable
fun ApiKeysCard(
    apiKeys: List<ApiKeyItem>,
    onManageAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Chaves e serviços", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            apiKeys.take(3).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onManageAll() }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = item.serviceName,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B)),
                        modifier = Modifier.weight(1f)
                    )

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
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = item.maskedKey,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 10.sp)
                    )
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(10.dp))
                }
            }

            OutlinedButton(
                onClick = onManageAll,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(imageVector = Icons.Default.VpnKey, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Gerenciar todas as chaves", fontSize = 11.sp)
            }
        }
    }
}

// ==========================================
// CARD: 🔄 SINCRONIZAÇÃO
// ==========================================
@Composable
fun SyncCard(
    sync: com.example.data.model.SyncInfo,
    onSyncNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, tint = Color(0xFF0052FF), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sincronização", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            Column {
                Text("Última sincronização", style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontSize = 10.sp))
                Text(sync.lastSyncTime, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = Color(0xFF0F172A)))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status:", style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF64748B)))
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (sync.status == "Sincronizado") Color(0xFF10B981) else Color(0xFFF59E0B))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sync.status,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (sync.status == "Sincronizado") Color(0xFF059669) else Color(0xFFD97706)
                    )
                )
            }

            Button(
                onClick = onSyncNow,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0052FF)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Sincronizar agora", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ==========================================
// REUSABLE UI HELPERS
// ==========================================
@Composable
fun SwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF334155), fontSize = 11.5.sp),
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF0052FF),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFCBD5E1)
            ),
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
fun DropdownSettingRow(
    label: String,
    selectedValue: String,
    options: List<String>,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onSelect: (String) -> Unit
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold, fontSize = 11.sp))
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth()) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF8FAFC),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandChange(true) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedValue,
                        fontSize = 11.5.sp,
                        color = Color(0xFF1E293B),
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF64748B),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandChange(false) },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontWeight = if (option == selectedValue) FontWeight.Bold else FontWeight.Normal,
                                color = if (option == selectedValue) Color(0xFF0052FF) else Color(0xFF1E293B)
                            )
                        },
                        onClick = {
                            onSelect(option)
                            onExpandChange(false)
                        }
                    )
                }
            }
        }
    }
}
