package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.BayanTopAppBar
import com.example.ui.components.ModelParametersSheet
import com.example.ui.screens.ChatScreen
import com.example.ui.screens.LinguisticLabScreen
import com.example.ui.screens.PlaygroundScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.BayanAiTheme
import com.example.ui.theme.BayanGold
import com.example.ui.theme.BayanNavyBase
import com.example.ui.theme.BayanNavyDark
import com.example.ui.theme.BayanNavySurfaceVariant
import com.example.ui.viewmodel.AppScreenTab
import com.example.ui.viewmodel.BayanAiViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BayanAiTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    BayanApp()
                }
            }
        }
    }
}

@Composable
fun BayanApp(
    viewModel: BayanAiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showParametersSheet by remember { mutableStateOf(false) }

    // Handle back press to return to CHAT tab
    if (uiState.currentTab != AppScreenTab.CHAT) {
        BackHandler {
            viewModel.selectTab(AppScreenTab.CHAT)
        }
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BayanNavyDark,
        topBar = {
            BayanTopAppBar(
                selectedModel = uiState.parameters.modelType,
                onModelSelected = { viewModel.updateModelType(it) },
                onOpenParameters = { showParametersSheet = true },
                onClearChat = { viewModel.clearChat() },
                showClearOption = uiState.currentTab == AppScreenTab.CHAT,
                isApiKeySet = uiState.isApiKeyConfigured
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = BayanNavyBase,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bayan_navigation_bar")
            ) {
                val tabs = listOf(
                    Triple(AppScreenTab.CHAT, Icons.Default.ChatBubble, Icons.Outlined.ChatBubbleOutline),
                    Triple(AppScreenTab.LAB, Icons.Default.AutoAwesome, Icons.Outlined.AutoAwesome),
                    Triple(AppScreenTab.PLAYGROUND, Icons.Default.Tune, Icons.Outlined.Tune),
                    Triple(AppScreenTab.SAVED, Icons.Default.Bookmarks, Icons.Outlined.Bookmarks)
                )

                tabs.forEach { (tab, filledIcon, outlinedIcon) ->
                    val isSelected = uiState.currentTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.selectTab(tab) },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) filledIcon else outlinedIcon,
                                contentDescription = tab.titleAr
                            )
                        },
                        label = {
                            Text(
                                text = tab.titleAr,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BayanNavyDark,
                            selectedTextColor = BayanGold,
                            indicatorColor = BayanGold,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.testTag("nav_item_${tab.name}")
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when (uiState.currentTab) {
            AppScreenTab.CHAT -> ChatScreen(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppScreenTab.LAB -> LinguisticLabScreen(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppScreenTab.PLAYGROUND -> PlaygroundScreen(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
            AppScreenTab.SAVED -> SavedScreen(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (showParametersSheet) {
        ModelParametersSheet(
            parameters = uiState.parameters,
            onTemperatureChange = { viewModel.updateTemperature(it) },
            onTopPChange = { viewModel.updateTopP(it) },
            onThinkingLevelChange = { viewModel.updateThinkingLevel(it) },
            onPersonaChange = { viewModel.updatePersona(it) },
            onCustomPromptChange = { viewModel.updateCustomSystemPrompt(it) },
            onDismiss = { showParametersSheet = false }
        )
    }
}
