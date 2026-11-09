package com.example.githubsee.ListRepo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubsee.R
import com.example.githubsee.ui.theme.GreenButton
import com.example.githubsee.ui.theme.white
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RepositoriesListScreen(
    viewModel: RepositoriesListViewModel = koinViewModel(),
    Onclick: () -> Unit, onRepositoryClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val errorState = state as? RepositoriesListViewModel.State.Error

    val isEmpty = state == RepositoriesListViewModel.State.Empty

    val showStatus = isEmpty || errorState != null

    val imageRes = when {
        isEmpty -> R.drawable.empty

        errorState?.error == R.string.repo_network_error ->
            R.drawable.connection_error

        else -> R.drawable.something_error
    }

    val titleRes = if (isEmpty) {
        R.string.Empty
    } else {
        R.string.repo_error_title
    }

    val infoRes = if (isEmpty) {
        R.string.Empty_info
    } else {
        errorState?.error ?: R.string.repo_loading_error
    }

    val titleColor = if (isEmpty) {
        colorResource(R.color.blue)
    } else {
        MaterialTheme.colorScheme.error
    }
    LaunchedEffect(state) {
        if (state == RepositoriesListViewModel.State.Unauthorized) {
            Onclick()
        }
    }


    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.repositories),
                        fontSize = 18.sp
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.logout()
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.vector),
                            contentDescription = stringResource(R.string.sign_out)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    )
    { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (val currentState = state) {

                is RepositoriesListViewModel.State.Loaded -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)

                    ) {
                        itemsIndexed(currentState.repos)
                        { index, repo ->

                            Column(


                                modifier = Modifier
                                    .fillMaxWidth()

                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.background)
                                    .border(
                                        0.2.dp,
                                        MaterialTheme.colorScheme.outlineVariant,

                                        )
                                    .padding(12.dp).clickable {
                                        onRepositoryClick(repo.fullName)
                                    }
                            ) {
                                Row(


                                    verticalAlignment = Alignment.CenterVertically

                                ) {

                                    Column(modifier = Modifier.padding(start = 10.dp, top = 5.dp)) {
                                        Box {

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {

                                                Text(
                                                    text = repo.name,
                                                    fontSize = 16.sp,
                                                    color = colorResource(R.color.blue)
                                                )
                                                Text(
                                                    text = repo.language.orEmpty(),
                                                    color = when (repo.language) {
                                                        "JavaScript" -> colorResource(R.color.JavaScriptYellow)
                                                        "Kotlin", "C#" -> colorResource(R.color.KotlinCSharpCPurple)
                                                        "Python" -> colorResource(R.color.Python)
                                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                                    }
                                                )
                                            }
                                        }

                                        Text(
                                            text = repo.description.orEmpty(),
                                            maxLines = 3,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }


                                }


                            }
                        }
                    }
                }


                RepositoriesListViewModel.State.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                RepositoriesListViewModel.State.Empty -> Unit

                is RepositoriesListViewModel.State.Error -> Unit

                RepositoriesListViewModel.State.Unauthorized -> Unit
            }
            if (showStatus) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )

                    Text(
                        text = stringResource(titleRes),
                        color = titleColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 12.dp)
                    )

                    Text(
                        text = stringResource(infoRes),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                if (errorState != null || isEmpty ) {
                    Button(
                        onClick = { viewModel.loadRepositories() },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenButton,
                            contentColor = white
                        )
                    ) {
                        Text(
                            text = stringResource(
                                if (isEmpty) R.string.REFRESH
                                else R.string.RETRY
                            )
                        )
                    }
                }
            }

        }
    }
}










