package com.example.doctordeni.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doctordeni.R
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.models.states.ScreenStateEnum


@Composable
fun LoadingScreen() {
    LottieLoader(resource = R.raw.loading)
}

@Composable
fun ErrorScreen() {
    LottieLoader(resource = R.raw.error)
}

@Composable
fun EmptyScreen() {
    LottieLoader(resource = R.raw.empty_cat_purple)
}

@Composable
fun StateScreen(
    screenStateEnum: ScreenStateEnum,
    onClick: (() -> Unit)? = null,
    buttonText: String? = null
) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            when (screenStateEnum) {
                ScreenStateEnum.LOADING -> {
                    LoadingScreen()
                }

                ScreenStateEnum.ERROR -> {
                    ErrorScreen()
                }

                ScreenStateEnum.EMPTY -> {
                    EmptyScreen()
                }
            }

            if(buttonText != null) {
                Button(
                    onClick = onClick!!,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                ) {
                    Text(buttonText ?: "button text")
                }
            }
        }
}


@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StateScreenLoadingPreview() {
    DoctorDeniTheme {
        StateScreen(ScreenStateEnum.LOADING)
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StateScreenErrorPreview() {
    DoctorDeniTheme {
        StateScreen(ScreenStateEnum.ERROR)
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun StateScreenEmptyPreview() {
    DoctorDeniTheme {
        StateScreen(ScreenStateEnum.EMPTY)
    }
}