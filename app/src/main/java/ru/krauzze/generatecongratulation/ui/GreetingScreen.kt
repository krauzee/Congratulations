package ru.krauzze.generatecongratulation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.base.BaseColumnFullScreen
import ru.krauzze.generatecongratulation.compose.text.*
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme
import ru.krauzze.generatecongratulation.ui.theme.greenIndicatorColor
import ru.krauzze.generatecongratulation.ui.theme.purpleIndicatorColor

@ExperimentalAnimationApi
@Composable
fun Greeting(visibility: Boolean, screenState: ScreenState) {
    var state by remember { mutableStateOf(visibility) }
    AnimatedVisibility(visible = state) {
        BaseColumnFullScreen(modifier = Modifier.fillMaxHeight()) {
            HeadingText(
                text = stringResource(id = R.string.hello_text),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 250.dp, start = 16.dp, end = 16.dp)
            )
            DescriptionText(
                text = stringResource(id = R.string.welcome_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            Column(
                Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                ThemeChecker(themeIsGreen = screenState.themeIsGreen)
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        state = !state
                        viewModel.onStartClick()
                    },
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.start_btn))
                }

                SecondaryDescriptionText(
                    text = stringResource(id = R.string.tester_star),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 12.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ThemeChecker(themeIsGreen: Boolean) {
    SecondaryHeadingText(
        text = stringResource(id = R.string.app_theme_title),
        modifier = Modifier.padding(top = 32.dp)
    )
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f, true)
        ) {
            Checkbox(
                checked = themeIsGreen,
                onCheckedChange = {
                    viewModel.changeTheme(it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = greenIndicatorColor(),
                    uncheckedColor = greenIndicatorColor()
                )
            )
            ThemeDescription(
                stringResource(id = R.string.green_theme_title),
                greenIndicatorColor()
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f, true)
        ) {
            Checkbox(
                checked = !themeIsGreen,
                onCheckedChange = {
                    viewModel.changeTheme(!it)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = purpleIndicatorColor(),
                    uncheckedColor = purpleIndicatorColor()
                )
            )
            ThemeDescription(
                stringResource(id = R.string.purple_theme_title),
                purpleIndicatorColor()
            )
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
fun DefaultPreview() {
    GenerateCongratulationTheme {
        Greeting(true, ScreenState())
    }
}