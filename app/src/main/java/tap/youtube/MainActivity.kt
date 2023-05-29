package tap.youtube

import Videos
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import tap.youtube.ui.theme.YouTubeTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YouTubeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    Scaffold(
                        bottomBar = { BottomBar(navController) }
                    ) {
                        NavGraph(navController, Modifier.padding(it))
                    }
                }
            }
        }
    }
}

enum class Screen(val route: String) {
    Search("screen1"),
    History("screen2"),
}

@Composable
fun BottomBar(navController: NavHostController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        Screen.values().forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.startDestinationRoute?.let { popUpTo(it) }
                        launchSingleTop = true
                    }
                },
                icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                label = { Text(screen.route) }
            )
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = Screen.Search.route, modifier = modifier) {
        composable(Screen.Search.route) { SearchScreen() }
        composable(Screen.History.route) { HistoryScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomBar(navController) }
    ) {
        NavGraph(navController, Modifier.padding(it))
    }
}
