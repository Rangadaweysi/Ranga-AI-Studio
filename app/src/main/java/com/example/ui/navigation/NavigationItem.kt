package com.example.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector

enum class StudioDestination(
    val title: String,
    val icon: ImageVector,
    val category: String,
    val description: String
) {
    DASHBOARD("Dashboard", Icons.Default.Dashboard, "Visão Geral", "Métricas, projetos recentes e atividade"),
    PROJECTS("Projetos", Icons.Default.Folder, "Produção Principal", "Novelas, séries, animações e filmes"),
    SERIES("Séries", Icons.Default.Tv, "Produção Principal", "Estrutura e bíblia de séries"),
    SEASONS("Temporadas", Icons.Default.CollectionsBookmark, "Produção Principal", "Arcos narrativos por temporada"),
    EPISODES("Episódios", Icons.Default.PlayCircleOutline, "Produção Principal", "Roteiros e status de produção"),
    SCENES("Cenas", Icons.Default.Movie, "Produção Principal", "Diálogos, ações, sonoplastia"),
    CHARACTERS("Personagens", Icons.Default.People, "Elementos Criativos", "Elenco, psicologia e vozes"),
    SCENARIOS("Cenários", Icons.Default.Landscape, "Elementos Criativos", "Locações e direção de arte"),
    VOICES("Vozes", Icons.Default.Mic, "Áudio & Som", "Elenco vocal e dublagem"),
    SOUNDS_MUSIC("Sons e Músicas", Icons.AutoMirrored.Filled.QueueMusic, "Áudio & Som", "Trilhas sonoras e efeitos SFX"),
    EXPORT("Exportar", Icons.Default.Share, "Finalização", "Exportação de roteiros e relatórios"),
    RANGA_CREATION("Criação RANGA", Icons.Default.AutoAwesome, "Inteligência Artificial", "Crie imagens, personagens e roteiros de forma rápida com IA"),
    SETTINGS("Configurações", Icons.Default.Settings, "Finalização", "Preferências do estúdio e IA")
}
