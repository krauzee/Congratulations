package ru.krauzze.generatecongratulation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.base.BaseColumnFullScreen
import ru.krauzze.generatecongratulation.compose.text.SecondaryDescriptionText
import ru.krauzze.generatecongratulation.compose.uielements.Toolbar
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme

/**
 * Экран сгенерированного поздравления [Screen.CONGRATULATION]
 * todo добавить кнопку "Сгенерировать еще раз"
 */
@ExperimentalAnimationApi
@Composable
fun ShowCongratulation(congratulation: String) {
    BaseColumnFullScreen {
        Toolbar(text = R.string.congratulation_title, needBackBtn = true, null)
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                elevation = 3.dp,
                color = MaterialTheme.colors.background,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                OutlinedTextField(
                    value = congratulation,
                    onValueChange = { changedValue -> viewModel.setCongratulation(changedValue) },
                    label = {
                        SecondaryDescriptionText(text = stringResource(id = R.string.generated_congratulation_title))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onBackground)
                )
            }

            Button(
                onClick = { viewModel.onStartGenerateBtnClick(true) },
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = stringResource(id = R.string.once_more_title))
            }

            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Button(
                    onClick = { viewModel.sendCongratulation() },
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.send_title))
                }
                Spacer(modifier = Modifier.height(16.dp).fillMaxWidth())
                Button(
                    onClick = { viewModel.copyCongratulation() },
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.copy_title))
                }
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
fun CongratulationPreview() {
    GenerateCongratulationTheme {
        ShowCongratulation("Тестовое поздравление, просто проверить проверить...")
    }
}