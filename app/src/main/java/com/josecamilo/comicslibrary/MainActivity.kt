package com.josecamilo.comicslibrary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.josecamilo.comicslibrary.ui.theme.ComicsLibraryTheme
import com.josecamilo.comicslibrary.view.CharacterDetailScreen
import com.josecamilo.comicslibrary.view.CharactersBottomNav
import com.josecamilo.comicslibrary.view.CollectionScreen
import com.josecamilo.comicslibrary.view.LibraryScreen
import com.josecamilo.comicslibrary.viewmodel.LibraryApiViewModel
import dagger.hilt.android.AndroidEntryPoint

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character_detail/{characterId}") {
        fun createRoute(characterId: Int?) = "character_detail/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val libraryVM by viewModels<LibraryApiViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComicsLibraryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController, libraryVM = libraryVM)
                }
            }
        }
    }
}

@Composable
fun CharactersScaffold(
    navController: NavHostController,
    libraryVM: LibraryApiViewModel
) {

    Scaffold(
        bottomBar = { CharactersBottomNav(navController = navController) }
    ) { paddingValues ->
        NavHost(navController, startDestination = Destination.Library.route) {
            composable(Destination.Library.route) {
                LibraryScreen(
                    navController = navController,
                    libraryVM = libraryVM,
                    paddingValues = paddingValues
                )
            }
            composable(Destination.Collection.route){
                CollectionScreen()
            }
            composable(Destination.CharacterDetail.route) { navBackStackEntry ->
                val characterId = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                CharacterDetailScreen(characterId = characterId ?: 0)
            }
        }
    }
}
