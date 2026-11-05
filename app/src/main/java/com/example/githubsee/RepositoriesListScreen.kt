package com.example.githubsee

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.githubsee.ui.theme.GreenButton
import com.example.githubsee.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun RepositoriesListScreen(Onclick:() -> Unit) {
    var showError by remember { mutableStateOf(false) }
    var Textbutton = stringResource(R.string.RETRY)
    var imageVectorError = ImageVector.vectorResource(R.drawable.empty)
    var TextError = stringResource(R.string.Empty)
    var TextErrorInfo = stringResource(R.string.Empty_info)
    var buttonzise = 0.dp
    var color = Red


    Scaffold (

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
                            // Здесь позже будет выход из аккаунта
                        }
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.vector),
                            contentDescription = "Sign out"
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .background(Color.Gray)

            ) {
                itemsIndexed(listOf("123"))
                {
                    index, item ->

                        Column(


                            modifier = Modifier
                                .fillMaxWidth()

                                .background(Color.White)
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

                                            Text(text = "ads2123123", fontSize = 16.sp)
                                            Text(text = "ad3s", fontSize = 16.sp)
                                        }
                                    }

                                    Text(
                                        text = "adasdadasdsaasdasdasddsaasdfffffffffffffffffffffffffffffffafdssssssssssssssssssssssssssas",
                                        modifier = Modifier,
                                        maxLines = 3,
                                        fontSize = 16.sp
                                    )

                                }


                            }


                        }
                    }
                }
            }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter

    ) { if (showError) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenButton,
                contentColor = white
            ),
            onClick = {}) { Text(text = stringResource(R.string.singIn)) }
    }
    }
    if (showError) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Image(
                modifier = Modifier
                    .size(100.dp),
                imageVector = imageVectorError,
                contentDescription = TextError,
                alignment = Alignment.Center

            )
            Text(
                text = TextError,
                modifier = Modifier.padding(8.dp),
                maxLines = 3,
                fontSize = 16.sp
            )
            Text(
                text = TextErrorInfo,
                modifier = Modifier.padding(8.dp),
                maxLines = 3,
                fontSize = 14.sp,
                color = colorResource(R.color.black)
            )
        }


    }




}





