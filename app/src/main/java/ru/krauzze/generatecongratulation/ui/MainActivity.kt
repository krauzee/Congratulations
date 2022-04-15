package ru.krauzze.generatecongratulation.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import ru.krauzze.generatecongratulation.compose.base.Background
import ru.krauzze.generatecongratulation.compose.base.SplashScreen
import ru.krauzze.generatecongratulation.compose.uielements.CircularProgress
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme
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
            Screen.PROGRESS -> showProgress(state)
            Screen.CONGRATULATION -> showCongratulation(state.congratulation)
            Screen.SETTINGS -> showSettings(state)
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

    private fun showSettings(state: ScreenState) {
        setContent {
            GenerateCongratulationTheme {
                ShowConfiguration(false, config = state.configuration, state.needMoreSettings)
                ShowSettings(visibility = true, screenState = state)
            }
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