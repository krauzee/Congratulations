package ru.krauzze.generatecongratulation.compose.base

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@ExperimentalAnimationApi
@Composable
fun BaseColumnFullScreen(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier
        .fillMaxWidth()
        .fillMaxHeight(),
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
    content = content.apply { Background() }
)