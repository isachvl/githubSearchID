package com.example.githubsee.InfoRepo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import com.mikepenz.markdown.m3.Markdown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubsee.R
import com.example.githubsee.ui.theme.GreenButton
import com.example.githubsee.ui.theme.white
import com.example.githubsee.ui.theme.star
import com.example.githubsee.ui.theme.forks
import com.example.githubsee.ui.theme.watches
import com.example.githubsee.ui.theme.link
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailInfoScreen(
    fullName: String,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: RepositoryInfoViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(fullName) {
        viewModel.loadRepository(fullName)
    }

    val repository = (state as? RepositoryInfoViewModel.State.Loaded)?.githubRepo

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = repository?.name ?: fullName.substringAfterLast("/"),
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },

                navigationIcon = {

                    IconButton(onClick = onBack ) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = stringResource(R.string.detail_back)
                        )
                    }
                },

                actions = {
                    IconButton(onClick = { viewModel.logout()
                        onLogout()
                        }) {
                        Icon(
                            painter = painterResource(R.drawable.vector),
                            contentDescription = stringResource(R.string.sign_out)
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },

        containerColor = MaterialTheme.colorScheme.background

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())


        ) {

            when (val currentState = state) {

                RepositoryInfoViewModel.State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is RepositoryInfoViewModel.State.Error -> {

                    DetailStatus(
                        imageRes = if (currentState.error == R.string.repo_network_error) {
                            R.drawable.connection_error
                        } else {
                            R.drawable.something_error
                        },
                        title = stringResource(R.string.repo_error_title),
                        description = stringResource(currentState.error),
                        onRetry = {
                            viewModel.loadRepository(fullName)
                        }
                    )
                }

                is RepositoryInfoViewModel.State.Loaded -> {

                    val repo = currentState.githubRepo

                    // Информация о репозитории
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {

                        // Ссылка на GitHub
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    uriHandler.openUri(repo.htmlUrl)
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                painter = painterResource(R.drawable.link),
                                contentDescription = null,
                                tint = link,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = repo.htmlUrl.removePrefix("https://"),
                                color = link,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Лицензия
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Icon(
                                    painter = painterResource(R.drawable.license),
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Text(
                                    text = stringResource(R.string.detail_license),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
                                )
                            }

                            Text(
                                text = repo.license?.spdxId
                                    ?.takeUnless { it.isBlank() || it == "NOASSERTION" }
                                    ?: stringResource(R.string.detail_no_license),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }

                        // Звёзды, форки и наблюдатели
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            DetailMetric(
                                imageRes = R.drawable.start,
                                text = stringResource(R.string.detail_stars, repo.stars),
                                tint = star
                            )

                            DetailMetric(
                                imageRes = R.drawable.forks,
                                text = stringResource(R.string.detail_forks, repo.forks),
                                tint = forks
                            )

                            DetailMetric(
                                imageRes = R.drawable.watchers,
                                text = stringResource(R.string.detail_watchers, repo.watchers),
                                tint = watches
                            )
                        }
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )


                    // Состояния README
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {

                        when (val readmeState = currentState.readmeState) {

                            RepositoryInfoViewModel.ReadmeState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }

                            RepositoryInfoViewModel.ReadmeState.Empty -> {

                                DetailStatus(

                                    imageRes = null,
                                    title = stringResource(R.string.detail_no_readme_title),
                                    description = null
                                )
                            }

                            is RepositoryInfoViewModel.ReadmeState.Error -> {

                                DetailStatus(
                                    imageRes = if (readmeState.error == R.string.repo_network_error) {
                                        R.drawable.connection_error
                                    } else {
                                        R.drawable.something_error
                                    },
                                    title = stringResource(R.string.repo_error_title),
                                    description = stringResource(readmeState.error),
                                    onRetry = {
                                        viewModel.loadRepository(fullName)
                                    }
                                )
                            }

                            is RepositoryInfoViewModel.ReadmeState.Loaded -> {

                                SelectionContainer {
                                    Markdown(
                                        content = readmeState.markdown,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxSize()

                                       )



                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Одна ячейка статистики
@Composable
private fun DetailMetric(
    imageRes: Int,
    text: String,
    tint: Color
) {

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(imageRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(5.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// Общий интерфейс для пустого README и ошибок
@Composable
private fun DetailStatus(
    imageRes: Int?,
    title: String,
    description: String?,
    onRetry: (() -> Unit)? = null
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        var columAligment =  if (description!=null) Alignment.CenterHorizontally else Alignment.Start


        Column(
            modifier = Modifier.then( if (imageRes!=null) Modifier.padding(vertical = 100.dp) else Modifier)

                ,
            horizontalAlignment = columAligment,
            verticalArrangement = Arrangement.Top

        ) {
            if (imageRes!=null){
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            }

            if (description!=null) {
                Text(
                    text = title,
                    color = if (onRetry == null) {
                        link
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 12.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            else{  Text(
                text = title,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp),

            )}
        }

        if (onRetry != null) {

            Button(
                onClick = onRetry,
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
                Text(text = stringResource(R.string.RETRY))
            }
        }
    }
}
