package ru.krauzze.generatecongratulation.compose.uielements

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.text.ToolbarText
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme
import ru.krauzze.generatecongratulation.ui.viewModel

@ExperimentalAnimationApi
@Composable
fun Toolbar(@StringRes text: Int, needBackBtn: Boolean) {
    Row(
        Modifier
            .padding()
            .background(MaterialTheme.colors.secondary)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(visible = needBackBtn) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back button",
                colorFilter = ColorFilter.tint(MaterialTheme.colors.background),
                modifier = Modifier
                    .size(45.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            viewModel.onBackPressed()
                        }
                    )
            )
        }
        ToolbarText(
            text = stringResource(id = text),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@ExperimentalAnimationApi
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, device = Devices.PIXEL_4)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, device = Devices.PIXEL_4)
@Composable
private fun Preview() {
    GenerateCongratulationTheme {
        Scaffold {
            Box(Modifier.fillMaxSize()) {
                Toolbar(
                    text = R.string.congratulation_title,
                    false
                )
            }
        }
    }
}