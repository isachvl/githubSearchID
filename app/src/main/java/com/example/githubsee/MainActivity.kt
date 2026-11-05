package com.example.githubsee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.githubsee.ui.theme.GitHubSEETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {


            val navController = rememberNavController()
            GitHubSEETheme {


                NavHost(navController = navController, startDestination = "screen_1")
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
                        RepositoriesListScreen { navController.navigate("screen_1") }

                    }


                }

            }


        }
    }
}

