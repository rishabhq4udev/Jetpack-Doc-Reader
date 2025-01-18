package com.example.jetpackpdfreader.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.jetpackpdfreader.presentation.screens.home.HomeScreen
import com.example.jetpackpdfreader.presentation.viewmodel.DocViewModel

@Composable
fun MainScreen(docViewModel: DocViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = {
                        Text(text = "Home")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = {
                        Text(text = "Favorites")
                    }
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = {
                        Text(text = "Settings")
                    }
                )
            }
        }
    ){ paddingValues ->  
        when(selectedTab){
            0 -> HomeScreen(docViewModel, modifier = Modifier.padding(paddingValues))
            1 -> HomeScreen(docViewModel, modifier = Modifier.padding(paddingValues))
            2 -> HomeScreen(docViewModel, modifier = Modifier.padding(paddingValues))
        }
    }
}