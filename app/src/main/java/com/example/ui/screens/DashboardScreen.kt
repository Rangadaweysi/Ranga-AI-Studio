package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.ActivityLogEntity
import com.example.data.model.ProjectEntity
import com.example.ui.components.SparklineWave
import com.example.ui.navigation.StudioDestination
import com.example.ui.theme.NavyDark
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.NavyPrimary

@Composable
fun DashboardScreen(
    projects: List<ProjectEntity>,
    projectCount: Int,
    characterCount: Int,
    sceneCount: Int,
    episodeCount: Int,
    recentActivities: List<ActivityLogEntity>,
    onNewProjectClick: () -> Unit,
    onOpenAiAssistant: () -> Unit,
    onNavigate: (StudioDestination) -> Unit,
    onSelectProject: (Long) -> Unit
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FAFC))) {
        val isWide = maxWidth >= 840.dp

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = if (isWide) 24.dp else 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Hero Welcome Card
            item {
                HeroWelcomeCard(
                    onNewProjectClick = onNewProjectClick
                )
            }

            // 2. Statistics Grid
            item {
                StatisticsRow(
                    projectCount = projectCount,
                    characterCount = characterCount,
                    sceneCount = sceneCount,
                    episodeCount = episodeCount,
                    isWide = isWide,
                    onNavigate = onNavigate
                )
            }

            // 3. Two-Column Split or Sequential Section
            if (isWide) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Left: Recent Projects (Weight 6.5)
                        Column(
                            modifier = Modifier.weight(0.64f),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            RecentProjectsSection(
                                projects = projects,
                                onNewProjectClick = onNewProjectClick,
                                onNavigate = onNavigate,
                                onSelectProject = onSelectProject,
                                isHorizontal = false
                            )

                            CreateInspirationBanner(
                                onNewProjectClick = onNewProjectClick
                            )
                        }

                        // Right: Recent Activities (Weight 3.5)
                        Column(
                            modifier = Modifier.weight(0.36f)
                        ) {
                            RecentActivitiesCard(
                                activities = recentActivities,
                                onViewAll = {
                                    Toast.makeText(context, "Visualizando todas as atividades", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    }
                }
            } else {
                // Mobile layout: Stacked
                item {
                    RecentProjectsSection(
                        projects = projects,
                        onNewProjectClick = onNewProjectClick,
                        onNavigate = onNavigate,
                        onSelectProject = onSelectProject,
                        isHorizontal = true
                    )
                }

                item {
                    RecentActivitiesCard(
                        activities = recentActivities,
                        onViewAll = {
                            Toast.makeText(context, "Visualizando todas as atividades", Toast.LENGTH_SHORT).show()
                        }
                    )
                }

                item {
                    CreateInspirationBanner(
                        onNewProjectClick = onNewProjectClick
                    )
                }
            }
        }
    }
}

@Composable
fun HeroWelcomeCard(
    onNewProjectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_hero_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Text + Action Button
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = "Bem-vindo ao RANGA AI STUDIO 👋",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 21.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Crie, organize e produza suas próprias novelas, séries, desenhos animados e filmes.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF64748B),
                            fontSize = 13.5.sp,
                            lineHeight = 20.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = onNewProjectClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A192F)),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        modifier = Modifier.testTag("dashboard_new_project_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+ Novo Projeto",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }

                // Right 3D Director Hero Artwork
                Box(
                    modifier = Modifier
                        .size(width = 160.dp, height = 110.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0A192F))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.hero_ranga_director),
                        contentDescription = "Ranga Studio Director 3D",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun StatisticsRow(
    projectCount: Int,
    characterCount: Int,
    sceneCount: Int,
    episodeCount: Int,
    isWide: Boolean,
    onNavigate: (StudioDestination) -> Unit
) {
    if (isWide) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            StatCard(
                title = "Projetos",
                count = projectCount.toString(),
                subtitle = "Projetos criados",
                icon = Icons.Default.Folder,
                iconTint = Color(0xFF0284C7),
                iconBg = Color(0xFFE0F2FE),
                waveColor = Color(0xFF38BDF8),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(StudioDestination.PROJECTS) }
            )

            StatCard(
                title = "Personagens",
                count = characterCount.toString(),
                subtitle = "Personagens criados",
                icon = Icons.Default.Person,
                iconTint = Color(0xFF10B981),
                iconBg = Color(0xFFD1FAE5),
                waveColor = Color(0xFF10B981),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(StudioDestination.CHARACTERS) }
            )

            StatCard(
                title = "Cenas",
                count = sceneCount.toString(),
                subtitle = "Cenas criadas",
                icon = Icons.Default.Movie,
                iconTint = Color(0xFF8B5CF6),
                iconBg = Color(0xFFEDE9FE),
                waveColor = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(StudioDestination.SCENES) }
            )

            StatCard(
                title = "Episódios",
                count = episodeCount.toString(),
                subtitle = "Episódios criados",
                icon = Icons.Default.PlayCircleOutline,
                iconTint = Color(0xFFF97316),
                iconBg = Color(0xFFFFEDD5),
                waveColor = Color(0xFFF97316),
                modifier = Modifier.weight(1f),
                onClick = { onNavigate(StudioDestination.EPISODES) }
            )
        }
    } else {
        // 2x2 Grid on Mobile
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Projetos",
                    count = projectCount.toString(),
                    subtitle = "Projetos criados",
                    icon = Icons.Default.Folder,
                    iconTint = Color(0xFF0284C7),
                    iconBg = Color(0xFFE0F2FE),
                    waveColor = Color(0xFF38BDF8),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(StudioDestination.PROJECTS) }
                )

                StatCard(
                    title = "Personagens",
                    count = characterCount.toString(),
                    subtitle = "Personagens criados",
                    icon = Icons.Default.Person,
                    iconTint = Color(0xFF10B981),
                    iconBg = Color(0xFFD1FAE5),
                    waveColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(StudioDestination.CHARACTERS) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = "Cenas",
                    count = sceneCount.toString(),
                    subtitle = "Cenas criadas",
                    icon = Icons.Default.Movie,
                    iconTint = Color(0xFF8B5CF6),
                    iconBg = Color(0xFFEDE9FE),
                    waveColor = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(StudioDestination.SCENES) }
                )

                StatCard(
                    title = "Episódios",
                    count = episodeCount.toString(),
                    subtitle = "Episódios criados",
                    icon = Icons.Default.PlayCircleOutline,
                    iconTint = Color(0xFFF97316),
                    iconBg = Color(0xFFFFEDD5),
                    waveColor = Color(0xFFF97316),
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigate(StudioDestination.EPISODES) }
                )
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    waveColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.testTag("stat_card_${title.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = count,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF0F172A),
                    fontSize = 24.sp
                )
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF94A3B8),
                    fontSize = 10.5.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            SparklineWave(
                color = waveColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun RecentProjectsSection(
    projects: List<ProjectEntity>,
    onNewProjectClick: () -> Unit,
    onNavigate: (StudioDestination) -> Unit,
    onSelectProject: (Long) -> Unit,
    isHorizontal: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Projetos recentes",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp
                )
            )

            Text(
                text = "Ver todos",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB),
                    fontSize = 12.5.sp
                ),
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onNavigate(StudioDestination.PROJECTS) }
                    .padding(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (projects.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ainda não existem projetos.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = onNewProjectClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A192F)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("+ Criar primeiro projeto", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            if (isHorizontal) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(projects.take(4)) { project ->
                        ProjectItemCard(
                            project = project,
                            modifier = Modifier.width(200.dp),
                            onOpen = { onSelectProject(project.id) }
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    projects.take(4).forEach { project ->
                        ProjectItemCard(
                            project = project,
                            modifier = Modifier.weight(1f),
                            onOpen = { onSelectProject(project.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectItemCard(
    project: ProjectEntity,
    modifier: Modifier = Modifier,
    onOpen: () -> Unit
) {
    val coverRes = when (project.coverUri) {
        "cover_frutinhas" -> R.drawable.cover_frutinhas
        "cover_herois_escola" -> R.drawable.cover_herois_escola
        "cover_misterio_cidade" -> R.drawable.cover_misterio_cidade
        "cover_reino_encantado" -> R.drawable.cover_reino_encantado
        "cover_missao_estelar" -> R.drawable.cover_missao_estelar
        "cover_fazenda_divertida" -> R.drawable.cover_fazenda_divertida
        "cover_piratas_kids" -> R.drawable.cover_piratas_kids
        "cover_mundo_dinossauros" -> R.drawable.cover_mundo_dinossauros
        else -> when (project.id % 8L) {
            1L -> R.drawable.cover_frutinhas
            2L -> R.drawable.cover_herois_escola
            3L -> R.drawable.cover_misterio_cidade
            4L -> R.drawable.cover_reino_encantado
            5L -> R.drawable.cover_missao_estelar
            6L -> R.drawable.cover_fazenda_divertida
            7L -> R.drawable.cover_piratas_kids
            else -> R.drawable.cover_mundo_dinossauros
        }
    }

    val typeBg = when (project.type) {
        "Desenho", "Desenho Animado" -> Color(0xFFF0FDF4)
        "Filme" -> Color(0xFFFAF5FF)
        else -> Color(0xFFEFF6FF)
    }

    val typeTextColor = when (project.type) {
        "Desenho", "Desenho Animado" -> Color(0xFF16A34A)
        "Filme" -> Color(0xFF9333EA)
        else -> Color(0xFF2563EB)
    }

    val relativeTime = when (project.id % 4L) {
        1L -> "Editado há 2 horas"
        2L -> "Editado há 1 dia"
        3L -> "Editado há 2 dias"
        else -> "Editado há 3 dias"
    }

    Card(
        modifier = modifier.testTag("recent_project_${project.id}"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            // Image Cover
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .background(Color(0xFF0F172A))
            ) {
                Image(
                    painter = painterResource(id = coverRes),
                    contentDescription = project.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 13.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opções",
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(typeBg)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = project.type,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = typeTextColor,
                            fontSize = 10.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = relativeTime,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onOpen,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A192F)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .testTag("open_project_btn_${project.id}"),
                    contentPadding = PaddingValues(vertical = 0.dp)
                ) {
                    Text(
                        text = "Abrir",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
fun RecentActivitiesCard(
    activities: List<ActivityLogEntity>,
    onViewAll: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("dashboard_recent_activities_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Atividade recente",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A),
                        fontSize = 16.sp
                    )
                )

                Text(
                    text = "Ver todas",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2563EB),
                        fontSize = 12.sp
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onViewAll() }
                        .padding(4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (activities.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nenhuma atividade recente",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0F172A),
                            fontSize = 13.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Suas criações e edições aparecerão aqui.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.5.sp
                        )
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    activities.take(5).forEach { item ->
                        val (icon, bg, tint) = when (item.iconType.lowercase()) {
                            "character" -> Triple(Icons.Default.Person, Color(0xFFF0FDF4), Color(0xFF16A34A))
                            "scenario" -> Triple(Icons.Default.Landscape, Color(0xFFFAF5FF), Color(0xFF9333EA))
                            "scene" -> Triple(Icons.Default.Movie, Color(0xFFF5F3FF), Color(0xFF7C3AED))
                            "episode" -> Triple(Icons.Default.PlayCircleOutline, Color(0xFFFFF7ED), Color(0xFFEA580C))
                            else -> Triple(Icons.Default.Folder, Color(0xFFEFF6FF), Color(0xFF2563EB))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF0F172A),
                                        fontSize = 12.5.sp
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF94A3B8),
                                        fontSize = 10.5.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateInspirationBanner(
    onNewProjectClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PostAdd,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = "Quer criar algo incrível?",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        text = "Comece agora seu primeiro projeto e dê vida às suas ideias.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF64748B),
                            fontSize = 11.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = onNewProjectClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A192F)),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ Criar primeiro projeto",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}
