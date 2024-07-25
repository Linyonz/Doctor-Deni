package com.example.doctordeni.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieLoader(modifier: Modifier? =Modifier, resource:Int) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resource))
    val progress by animateLottieCompositionAsState(composition)
    if (modifier != null) {
        LottieAnimation(
            modifier = modifier,
            composition = composition,
            progress = { progress },
        )
    }else {
        LottieAnimation(
            composition = composition,
            progress = { progress },
        )
    }
}