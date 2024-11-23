package com.example.metro_mate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.metro_mate.ui.theme.MetroMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetroMateTheme {
                MetroMateApp()
            }
        }
    }
}

@Composable
fun MetroMateApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavigationHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", "Home", R.drawable.ic_home),
        BottomNavItem("timetable", "Timetable", R.drawable.ic_timetable),
        BottomNavItem("fare", "Fare", R.drawable.ic_fare),
        BottomNavItem("settings", "Settings", R.drawable.ic_settings)
    )
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = navController.currentDestination?.route == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(painterResource(item.icon), contentDescription = item.title)
                },
                label = {
                    Text(item.title)
                }
            )
        }
    }
}


@Composable
fun NavigationHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") { HomeScreen() }
        composable("timetable") { TimetableScreen() }
        composable("fare") { FareScreen() }
        composable("settings") { SettingsScreen() }
    }
}

// 아래는 각 화면의 Composable 함수 예시
@Composable
fun HomeScreen() {
    Text("Home Screen")
}

@Composable
fun TimetableScreen() {
    Text("Timetable Screen")
}

@Composable
fun FareScreen() {
    Text("Fare Screen")
}

@Composable
fun SettingsScreen() {
    Text("Settings Screen")
}

// 네비게이션 아이템 데이터 클래스
data class BottomNavItem(val route: String, val title: String, val icon: Int)
