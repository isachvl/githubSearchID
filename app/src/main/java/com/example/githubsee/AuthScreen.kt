package com.example.githubsee


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.githubsee.ui.theme.GreenButton
import com.example.githubsee.ui.theme.white


@Preview(showBackground = true)
@Composable
fun AuthScreenn(){
    val clickTextField : MutableState<Boolean> = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background).safeDrawingPadding(),

        horizontalAlignment = Alignment.CenterHorizontally


    ) {

        Box(

             contentAlignment = Alignment.TopCenter

        ){

            Image(
                modifier = Modifier.size(190.dp).padding(horizontal = 16.dp, vertical = 8.dp )
                    ,
                imageVector = ImageVector.vectorResource(R.drawable.logo1),
                contentDescription =  stringResource(R.string.logo)

            )



        }
        Box(

            contentAlignment = Alignment.TopCenter

        ){


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background).safeDrawingPadding(),

                horizontalAlignment = Alignment.CenterHorizontally


            ) {
                Text(text = "SING IN")
                TextField(
                value ="",
                onValueChange = {},
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp )
                    .onFocusChanged{

                    if (it.isFocused) clickTextField.value = !clickTextField.value else clickTextField.value = !clickTextField.value

                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
            )
            }

        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter

        ){
            Button(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp ),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenButton,contentColor = white),
                onClick = {}) { Text(text = "SING IN") }

        }
    }

}