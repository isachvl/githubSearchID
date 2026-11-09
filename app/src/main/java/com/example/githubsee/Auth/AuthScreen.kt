package com.example.githubsee.Auth


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubsee.ui.theme.ErrorRed
import com.example.githubsee.ui.theme.GreenButton
import com.example.githubsee.ui.theme.white
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.githubsee.R


@Composable
fun AuthScreenn(viewModel: AuthViewModel = koinViewModel(), Onclick:() -> Unit) {



    val token by viewModel.token.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val clickTextField: MutableState<Boolean> = remember { mutableStateOf(true) }
    var sizeText = 0.sp
    LaunchedEffect(state) {
        if (state == AuthViewModel.State.Authorized) {
            Onclick()
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.actions.collect { action ->

           if (action ==  AuthViewModel.Action.RouteToMain)  Onclick()



        }
    }
    var colorhelptext = MaterialTheme.colorScheme.primary
    var sizeTextholder = 16.sp
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.loading)
    )


    when (clickTextField.value) {

        false -> {
            sizeText = 0.sp
            sizeTextholder = 16.sp
        }

        true -> {
            sizeText = 12.sp
            sizeTextholder = 0.sp
        }

    }


    val errorState = state as? AuthViewModel.State.InvalidInput
    val isError = errorState != null


    colorhelptext = if (isError) ErrorRed else MaterialTheme.colorScheme.primary


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),

        horizontalAlignment = Alignment.CenterHorizontally


    ) {

        Box(

            contentAlignment = Alignment.TopCenter

        ) {

            Image(
                modifier = Modifier
                    .size(190.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                imageVector = ImageVector.vectorResource(R.drawable.logo1),
                contentDescription = stringResource(R.string.logo)

            )


        }
        Box(

            contentAlignment = Alignment.TopCenter

        ) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .safeDrawingPadding(),
                    horizontalAlignment = Alignment.Start


                ) {
                    Text(
                        text = stringResource(R.string.tokenPersonal),
                        fontSize = sizeText,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = colorhelptext
                    )
                    TextField(
                        value = token,singleLine = true,
                        onValueChange = { viewModel.onTokenChanged(it) },
                        placeholder = {
                            Text(

                                text = stringResource(R.string.tokenPersonal),
                                fontSize = sizeTextholder
                            )
                        },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)

                            .onFocusChanged {

                                clickTextField.value = it.isFocused

                            },isError = isError,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            errorIndicatorColor = ErrorRed

                        ),
                    )
                    if (errorState != null) {
                        Text(
                            text = stringResource(errorState.reason),
                            fontSize = 12.sp,
                            color = ErrorRed,
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 4.dp
                            )
                        )
                    }

                }
            }

        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter

        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenButton,
                    contentColor = white
                ),enabled = state != AuthViewModel.State.Loading,
                onClick = {viewModel.onSignButtonPressed()}) { if (state == AuthViewModel.State.Loading) {
                LottieAnimation(
                    composition = composition,
                    iterations = Int.MAX_VALUE,
                    modifier = Modifier.size(32.dp)
                )
            }


            else {
                Text(text = stringResource(R.string.singIn))
            } }


        }
    }

}

