package ru.krauzze.generatecongratulation.compose.base

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ru.krauzze.generatecongratulation.R

@ExperimentalAnimationApi
@Composable
fun Background() {
    val backgroundResource = if (isSystemInDarkTheme())
        R.drawable.dark_background
    else
        R.drawable.light_background
    AnimatedVisibility(visible = true) {
        Image(
            contentScale = ContentScale.FillHeight,
            painter = painterResource(id = backgroundResource),
            contentDescription = "Background",
            //alpha = 0.1f
        )
    }
}