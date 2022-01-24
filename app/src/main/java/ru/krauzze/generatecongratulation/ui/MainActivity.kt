package ru.krauzze.generatecongratulation.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.base.Background
import ru.krauzze.generatecongratulation.compose.base.BaseColumnFullScreen
import ru.krauzze.generatecongratulation.compose.base.SplashScreen
import ru.krauzze.generatecongratulation.compose.text.*
import ru.krauzze.generatecongratulation.compose.uielements.CircularProgress
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme
import ru.krauzze.generatecongratulation.ui.theme.greenIndicatorColor
import ru.krauzze.generatecongratulation.ui.theme.purpleIndicatorColor
import ru.krauzze.generatecongratulation.util.obtainViewModel
import ru.krauzze.generatecongratulation.util.postDelayed

lateinit var viewModel: MainViewModel

const val SPLASH_DURATION = 2000L

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private lateinit var clipboard: ClipboardManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        viewModel = obtainViewModel(MainViewModel::class.java)
        viewModel.getScreenState().observeForever {
            reactOnScreenState(it)
        }
        viewModel.getIsRecreateNeed().observeForever {
            if (it) {
                this.recreate()
                viewModel.onThemeChangeShow()
            }
        }
        viewModel.getCopiedCongratulation().observeForever {
            if (it.isNotEmpty())
                copyCongratulationToClipboard(it)
        }
        viewModel.getSendCongratulation().observeForever {
            if (it.isNotEmpty())
                sendCongratulation(it)
        }
    }

    private fun reactOnScreenState(state: ScreenState) {
        when (state.activeScreen) {
            Screen.SPLASH -> showSplash()
            Screen.GREETING -> showGreeting(state)
            Screen.CONFIGURATION -> showConfiguration(state)
            Screen.CONGRATULATION -> {
                showProgress(state)
                postDelayed({ showCongratulation(state.congratulation) }, 1000)
            }
        }
    }

    private fun showSplash() {
        setContent {
            GenerateCongratulationTheme {
                SplashScreen()
                postDelayed({ viewModel.onSplashDone() }, SPLASH_DURATION)
            }
        }
    }

    private fun showGreeting(state: ScreenState) {
        setContent {
            GenerateCongratulationTheme {
                Greeting(true, state)
            }
        }
    }

    private fun showConfiguration(state: ScreenState) {
        setContent {
            GenerateCongratulationTheme {
                Greeting(false, state)
                ShowConfiguration(
                    visibility = true,
                    config = state.configuration,
                    isNeedMoreSettings = state.needMoreSettings
                )
            }
        }
    }

    private fun showCongratulation(congratulation: String) {
        setContent {
            GenerateCongratulationTheme {
                Background()
                ShowCongratulation(congratulation)
            }
        }
    }

    private fun showProgress(state: ScreenState) {
        setContent {
            ShowConfiguration(false, config = state.configuration, state.needMoreSettings)
            CircularProgress()
        }
    }

    private fun copyCongratulationToClipboard(congratulation: String) {
        val clip = ClipData.newPlainText("Поздравление", congratulation)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Скопировал!", Toast.LENGTH_SHORT).show()
    }

    private fun sendCongratulation(congratulation: String) {
        val shareIntent = Intent.createChooser(
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, congratulation)
                type = "text/plain"
            },
            null
        )
        startActivity(shareIntent)
    }

    override fun onBackPressed() {
        viewModel.onBackPressed()
    }
}

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
                            checked = screenState.themeIsGreen,
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
                            checked = !screenState.themeIsGreen,
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
fun ThemeDescription(themeName: String, color: Color) {
    FieldNameText(
        text = themeName,
        modifier = Modifier.padding(start = 4.dp),
        color = color,
        textAlign = TextAlign.Center
    )
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