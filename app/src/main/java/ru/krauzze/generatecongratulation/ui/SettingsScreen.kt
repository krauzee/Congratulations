package ru.krauzze.generatecongratulation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.base.BaseColumnFullScreen
import ru.krauzze.generatecongratulation.compose.uielements.Toolbar
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme

/**
 * Экран настроек приложения [Screen.SETTINGS]
 */
@ExperimentalAnimationApi
@Composable
fun ShowSettings(visibility: Boolean, screenState: ScreenState) {
    AnimatedVisibility(visible = visibility) {
        BaseColumnFullScreen {
            Toolbar(text = R.string.settings_title, needBackBtn = true, null)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                ThemeChecker(screenState.themeIsGreen)
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview(
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
    showBackground = true
)
@Preview(
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun SettingsPreview() {
    GenerateCongratulationTheme {
        ShowSettings(true, ScreenState())
    }
}