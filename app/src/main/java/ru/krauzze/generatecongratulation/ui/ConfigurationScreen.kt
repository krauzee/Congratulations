package ru.krauzze.generatecongratulation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.krauzze.generatecongratulation.R
import ru.krauzze.generatecongratulation.compose.base.BaseColumnFullScreen
import ru.krauzze.generatecongratulation.compose.text.DescriptionText
import ru.krauzze.generatecongratulation.compose.text.FieldNameText
import ru.krauzze.generatecongratulation.compose.text.SecondaryDescriptionText
import ru.krauzze.generatecongratulation.compose.text.SecondaryHeadingText
import ru.krauzze.generatecongratulation.compose.uielements.Toolbar
import ru.krauzze.generatecongratulation.data.*
import ru.krauzze.generatecongratulation.data.Reason.*
import ru.krauzze.generatecongratulation.ui.theme.GenerateCongratulationTheme

/**
 * Экран настройки поздравления [Screen.CONFIGURATION]
 */
@ExperimentalAnimationApi
@Composable
fun ShowConfiguration(visibility: Boolean, config: Configuration, isNeedMoreSettings: Boolean) {
    AnimatedVisibility(visible = visibility) {
        BaseColumnFullScreen {
            Toolbar(text = R.string.config_title, needBackBtn = false)
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                DescriptionText(text = stringResource(id = R.string.person_name_title))
                var name: String by remember { mutableStateOf("") }
                name = config.name
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    elevation = 3.dp,
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { changedValue ->
                            name = changedValue
                            viewModel.setConfigurationName(name)
                        },
                        label = {
                            Text(
                                text = stringResource(id = R.string.person_name),
                                color = MaterialTheme.colors.onBackground
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }
                FieldDescription(R.string.person_name_description)

                SecondaryHeadingText(
                    modifier = Modifier.padding(top = 32.dp),
                    text = stringResource(id = R.string.reason_description)
                )
                val allReasonList = mutableListOf(
                    BIRTHDAY,
                    WEDDING_ANNIVERSARY,
                    NEW_YEAR,
                    MOTHERS_DAY,
                    FATHERS_DAY,
                    FEMALE_DAY,
                    MALE_DAY,
                    INDEPENDENCE_DAY,
                    UNDEFINED
                )
                val reasonsList = when (config.gender) {
                    Gender.MALE -> allReasonList.filter { it != FEMALE_DAY && it != MOTHERS_DAY}
                    Gender.FEMALE -> allReasonList.filter { it != MALE_DAY && it != FATHERS_DAY}
                    Gender.UNDEFINED -> allReasonList
                }
                var expanded by remember { mutableStateOf(false) }
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.TopStart) {
                    Row(
                        Modifier
                            .padding(top = 16.dp)
                            .clickable { expanded = !expanded },
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FieldNameText(
                            text = stringResource(id = config.reason.value),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = "ArrowDown",
                            tint = MaterialTheme.colors.onBackground
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            reasonsList.forEach { chosenReason ->
                                DropdownMenuItem(onClick = {
                                    expanded = false
                                    viewModel.onReasonChose(chosenReason)
                                }) {
                                    FieldNameText(text = stringResource(id = chosenReason.value))
                                }
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = !isNeedMoreSettings) {
                    LinkText(
                        R.string.need_more_settings_title,
                        textAlign = TextAlign.Center
                    ) {
                        viewModel.setNeedMoreSettings(true)
                    }
                }

                AnimatedVisibility(visible = isNeedMoreSettings) {
                    Column(Modifier.animateContentSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 32.dp)
                        ) {
                            Checkbox(
                                checked = config.gender == Gender.MALE,
                                onCheckedChange = { viewModel.setConfigurationGender(Gender.MALE) }
                            )
                            FieldNameText(
                                stringResource(id = R.string.male_gender_title),
                                Modifier.padding(start = 4.dp)
                            )
                            Checkbox(
                                checked = config.gender == Gender.FEMALE,
                                onCheckedChange = { viewModel.setConfigurationGender(Gender.FEMALE) },
                                modifier = Modifier.padding(start = 16.dp)
                            )
                            FieldNameText(
                                stringResource(id = R.string.female_gender_title),
                                Modifier.padding(start = 4.dp)
                            )
                        }
                        FieldDescription(R.string.person_gender)

                        Row(modifier = Modifier.padding(top = 32.dp)) {
                            FieldNameText(stringResource(id = R.string.non_officially_title))
                            Switch(
                                checked = config.isOfficial,
                                onCheckedChange = { isChecked ->
                                    viewModel.setConfigurationOfficially(isChecked)
                                },
                                Modifier.padding(start = 4.dp, end = 4.dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colors.primary,
                                    uncheckedThumbColor = MaterialTheme.colors.primary
                                )
                            )
                            FieldNameText(stringResource(id = R.string.officially_title))
                        }

                        Row(modifier = Modifier.padding(top = 32.dp)) {
                            FieldNameText(stringResource(id = R.string.from_me_title))
                            Switch(
                                checked = config.isFromWe,
                                onCheckedChange = { isChecked ->
                                    viewModel.setConfigurationFromWe(isChecked)
                                },
                                Modifier.padding(start = 4.dp, end = 4.dp),
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colors.primary,
                                    uncheckedThumbColor = MaterialTheme.colors.primary
                                )
                            )
                            FieldNameText(stringResource(id = R.string.from_we_title))
                        }

                        Row(modifier = Modifier.padding(top = 32.dp)) {
                            FieldNameText(text = stringResource(id = R.string.is_closed_one_title))
                            Switch(
                                checked = config.closedOne,
                                onCheckedChange = { isChecked ->
                                    viewModel.setClosedOne(isChecked)
                                },
                                Modifier.padding(start = 4.dp)
                            )
                        }

                        var lengthDegree by remember { mutableStateOf(C_LENGTH_DEF_VALUE) }
                        //lengthDegree = config.lengthDegree.toFloat()
                        Slider(
                            modifier = Modifier.padding(top = 32.dp, start = 16.dp, end = 16.dp),
                            value = lengthDegree,
                            onValueChange = { lengthDegree = it },
                            onValueChangeFinished = { viewModel.onLengthDegreeChange(lengthDegree.toInt()) },
                            valueRange = C_RANGE_STEPS_COUNT,
                            steps = C_LENGTH_STEPS_COUNT,
                            colors = SliderDefaults.colors(
                                thumbColor = MaterialTheme.colors.primary,
                                activeTrackColor = MaterialTheme.colors.primary,
                                inactiveTickColor = MaterialTheme.colors.primary,
                                inactiveTrackColor = MaterialTheme.colors.onSurface
                            )
                        )
                        SecondaryDescriptionText(text = stringResource(id = R.string.congratulation_length_title))
                    }
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
                        onClick = { viewModel.onStartGenerateBtnClick(false) },
                        contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.start_generate_btn))
                    }
                }
            }
        }
    }
}

@Composable
fun FieldDescription(stringRes: Int) {
    SecondaryDescriptionText(
        text = stringResource(id = stringRes),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp)
    )
}

@Composable
fun LinkText(stringRes: Int, textAlign: TextAlign, action: () -> Unit) {
    DescriptionText(
        text = stringResource(id = stringRes),
        color = MaterialTheme.colors.primary,
        textAlign = textAlign,
        style = TextStyle(
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = 0.5.sp,
            textDecoration = TextDecoration.Underline
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp)
            .clickable(
                enabled = true,
                onClick = action
            )
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
fun ConfigPreview() {
    GenerateCongratulationTheme {
        ShowConfiguration(true, Configuration(), true)
    }
}