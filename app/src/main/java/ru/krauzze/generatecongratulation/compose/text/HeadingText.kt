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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme

@Composable
fun HeadingText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
    maxLines: Int = Int.MAX_VALUE,
    color: Color = MaterialTheme.colors.primary
) = BaseText(
    maxLines = maxLines,
    textAlign = textAlign,
    modifier = modifier,
    style = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        letterSpacing = 1.25.sp
    ),
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
                HeadingText(
                    text = "Description text",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}