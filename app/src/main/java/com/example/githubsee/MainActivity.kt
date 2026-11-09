package com.example.githubsee

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubsee.Auth.AuthScreenn
import com.example.githubsee.Auth.AuthViewModel
import com.example.githubsee.InfoRepo.DetailInfoScreen
import com.example.githubsee.ListRepo.RepositoriesListScreen
import com.example.githubsee.ui.theme.GitHubSEETheme
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel

class MainActivity (): ComponentActivity() {
    lateinit var startScreen: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val authViewModel: AuthViewModel = koinViewModel()
            startScreen = if (authViewModel.Authflag ) "screen_1" else "screen_2"

            val navController = rememberNavController()
            GitHubSEETheme {


                NavHost(navController = navController, startDestination = startScreen)
                {
                    composable("screen_1") {

                        AuthScreenn {
                            navController.navigate("screen_2") {
                                popUpTo("screen_1") {
                                    inclusive = true
                                }

                            }
                        }

                    }
                    composable("screen_2") {
                        RepositoriesListScreen(
                            Onclick = {
                                navController.navigate("screen_1") {
                                    popUpTo("screen_2") {
                                        inclusive = true
                                    }
                                }
                            },
                            onRepositoryClick = { fullName ->
                                navController.navigate("screen_3/${Uri.encode(fullName)}")
                            }

                        )
                    }
                    composable("screen_3/{fullName}") { backStackEntry ->

                        val fullName = backStackEntry.arguments
                            ?.getString("fullName")
                            .orEmpty()

                        DetailInfoScreen(
                            fullName = fullName,
                            onBack = {
                                navController.popBackStack()
                            },
                            onLogout = {
                                navController.navigate("screen_1") {
                                    popUpTo("screen_2") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }


                }

            }


        }
    }
}

