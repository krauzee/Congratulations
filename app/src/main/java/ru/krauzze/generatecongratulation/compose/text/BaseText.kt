package ru.krauzze.generatecongratulation.compose.text

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Base text element
 */
@Composable
fun BaseText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    color: Color = MaterialTheme.colors.onBackground
) = Text(
    maxLines = maxLines,
    overflow = overflow,
    textAlign = textAlign,
    modifier = modifier,
    style = style,
    text = text,
    color = color
)