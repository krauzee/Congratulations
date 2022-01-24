package ru.krauzze.generatecongratulation.compose.text

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme

@Composable
fun DescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    color: Color = MaterialTheme.colors.onBackground,
    style: TextStyle = MaterialTheme.typography.body1
) = BaseText(
    maxLines = maxLines,
    overflow = overflow,
    textAlign = textAlign,
    modifier = modifier,
    style = style,
    text = text,
    color = color,
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    GenerateCongratulationTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                DescriptionText(
                    text = "Description text",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}