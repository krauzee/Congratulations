package ru.krauzze.generatecongratulation.ui

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.krauzze.generatecongratulation.data.Configuration
import ru.krauzze.generatecongratulation.data.Gender
import ru.krauzze.generatecongratulation.data.Reason
import ru.krauzze.generatecongratulation.generator.CongratulationsGenerator
import ru.krauzze.generatecongratulation.generator.SexDefine
import ru.krauzze.generatecongratulation.repositories.DesiresRepository
import ru.krauzze.generatecongratulation.ui.Screen.*
import ru.krauzze.generatecongratulation.ui.base.BaseViewModel
import ru.krauzze.generatecongratulation.util.DataStore
import javax.inject.Inject
import kotlin.random.Random

/**
 * Вью модель приложения
 * устанавливает [ScreenState] который будет отображен в активити и собирает
 * [Configuration], на основе которого будет сгенерировано поздравление
 */
class MainViewModel @Inject constructor(
    private val desiresRepository: DesiresRepository
) : BaseViewModel() {

    private var themeWasApplyNeedRecreate = MutableLiveData(false)

    private var configuration = Configuration()
    private var userConfiguration = MutableLiveData(configuration)

    private val screenStateValue = ScreenState(SPLASH, configuration)
    private val screenState = MutableLiveData(screenStateValue)

    private var congratulation = ""
    private var copyCongratulationState = MutableLiveData("")
    private var sendCongratulationState = MutableLiveData("")

    fun getScreenState() = screenState

    fun changeTheme(isGreen: Boolean) {
        DataStore.AppConfig.themeColorIsGreen = isGreen
        screenState.postValue(
            screenState.value?.copy(themeIsGreen = isGreen)
        )
        themeWasApplyNeedRecreate.postValue(true)
    }

    fun getIsRecreateNeed() = themeWasApplyNeedRecreate

    fun onThemeChangeShow() {
        themeWasApplyNeedRecreate.postValue(false)
    }

    /**
     * Срабатывает после того как был показан экран [Screen.SPLASH]
     */
    fun onSplashDone() {
        val activeScreen = if (!DataStore.AppConfig.isGreetingShown) GREETING else CONFIGURATION
        screenState.postValue(screenState.value?.copy(activeScreen = activeScreen))
    }

    /**
     * Срабатывает по нажатию на кнопку "Начнем" на экране [Screen.GREETING]
     */
    fun onStartClick() {
        DataStore.AppConfig.isGreetingShown = true
        screenState.postValue(screenState.value?.copy(activeScreen = CONFIGURATION))
    }

    /**
     * Срабатывает по нажатию на кнопку "Настройки" в тулбаре на экране [Screen.CONFIGURATION]
     */
    fun onSettingsClick() {
        screenState.postValue(screenState.value?.copy(activeScreen = SETTINGS))
    }

    /**
     * Устанавливает имя человека, для которого генерируется поздравление
     */
    fun setConfigurationName(name: String) {
        val gender = SexDefine.getSexByName(name)
        userConfiguration.postValue(userConfiguration.value?.copy(name = name, gender = gender))
        postChangedConfiguration(userConfiguration.value?.copy(name = name, gender = gender))
    }

    /**
     * Позволяет вручную установить пол человека, для которого генерируется поздравление
     */
    fun setConfigurationGender(gender: Gender) {
        userConfiguration.postValue(userConfiguration.value?.copy(gender = gender))
        postChangedConfiguration(userConfiguration.value?.copy(gender = gender))
    }

    //fun setConfigurationSalutation(salutation: String) = userConfiguration.postValue(configuration.copy(salutation = salutation))

    /**
     * Устанавливает повод, для которого будет сгенерировано поздравление
     */
    fun onReasonChose(reason: Reason) {
        userConfiguration.postValue(userConfiguration.value?.copy(reason = reason))
        postChangedConfiguration(userConfiguration.value?.copy(reason = reason))
    }

    /**
     * Устанавливает флаг, нужно ли отобразить большее количество настроек
     */
    fun setNeedMoreSettings(isNeedMore: Boolean) {
        screenState.postValue(
            screenState.value?.copy(
                activeScreen = CONFIGURATION,
                needMoreSettings = isNeedMore
            )
        )
    }

    /**
     * Устанавливает флаг, который указывает, нужно ли официальное поздравление
     */
    fun setConfigurationOfficially(isOfficial: Boolean) {
        userConfiguration.postValue(userConfiguration.value?.copy(isOfficial = isOfficial))
        postChangedConfiguration(userConfiguration.value?.copy(isOfficial = isOfficial))
    }

    /**
     * Устанавливает флаг, который указывает, нато, какой формы будет поздравление от меня или от нас
     */
    fun setConfigurationFromWe(isFromWe: Boolean) {
        userConfiguration.postValue(userConfiguration.value?.copy(isFromWe = isFromWe))
        postChangedConfiguration(userConfiguration.value?.copy(isFromWe = isFromWe))
    }

    /**
     * Устанавливает флаг, который указывает, нужно ли поздравление в стихотворной форме
     */
    fun setClosedOne(isClosedOne: Boolean) {
        userConfiguration.postValue(userConfiguration.value?.copy(closedOne = isClosedOne))
        postChangedConfiguration(userConfiguration.value?.copy(closedOne = isClosedOne))
    }

    /**
     * Устанавливает уровень близости человека, которому пишется поздравление
     * @param value - значение от 0 до n, возможные значения - [C_RANGE_STEPS_COUNT]
     */
    fun onLengthDegreeChange(value: Int) {
        val length = value * 25
        userConfiguration.postValue(userConfiguration.value?.copy(lengthDegree = length))
        postChangedConfiguration(userConfiguration.value?.copy(lengthDegree = length))
    }

    /**
     * Отвечает за обновление [Configuration] в [ScreenState]
     */
    private fun postChangedConfiguration(configuration: Configuration?) {
        screenState.postValue(
            screenState.value?.copy(
                configuration = configuration ?: Configuration()
            )
        )
    }

    /**
     * Срабатывает по нажатию на кнопку "Поехали" с экрана ConfigurationScreen и @param isRestart == false @see [ShowConfiguration],
     * или по нажатию на кнопку "Ещё раз" с экрана CongratulationScreen и @param isRestart == true @see [ShowCongratulation]
     * берет [Configuration] из userConfiguration
     */
    fun onStartGenerateBtnClick(isRestart: Boolean) {
        postProgressScreen()
        if (isRestart)
            userConfiguration.postValue(userConfiguration.value?.copy(lengthDegree = (Random.nextInt(0, 4) * 25)))

        var remoteDesires = ""
        launch {
            kotlin.runCatching {
                remoteDesires = desiresRepository.getBirthdayDesires(
                    userConfiguration.value?.reason?: Reason.NOT_CHOSEN,
                    userConfiguration.value?.lengthDegree ?: 50
                )
                congratulation = CongratulationsGenerator.generateCongratulation(
                    userConfiguration.value,
                    remoteDesires
                )
                postCongratulationScreen(congratulation)
            }.onFailure {
                it.printStackTrace()
                congratulation = CongratulationsGenerator.generateCongratulation(
                    userConfiguration.value,
                    remoteDesires
                )
                postCongratulationScreen(congratulation)
            }
        }
    }

    /**
     * Функция отвечает за отправку стейта отображения готового поздравления
     */
    private fun postCongratulationScreen(congratulation: String) {
        screenState.postValue(
            screenState.value?.copy(
                activeScreen = CONGRATULATION,
                configuration = userConfiguration.value ?: Configuration(),
                congratulation = congratulation
            )
        )
    }

    /**
     * Функция отвечает за отправку стейта отображения процесса загрузки
     */
    private fun postProgressScreen() {
        screenState.postValue(
            screenState.value?.copy(activeScreen = PROGRESS)
        )
    }

    /**
     * Отвечает за сохранение сгенерированного поздравления, КОТОРОЕ ОТРЕДАКТИРОВАЛ ПОЛЬЗОВАТЕЛЬ
     */
    fun setCongratulation(redactCongratulation: String) {
        congratulation = redactCongratulation
        postCongratulationScreen(redactCongratulation)
    }

    /**
     * Срабатывает по нажатию кнопки "Скопировать"
     */
    fun copyCongratulation() = copyCongratulationState.postValue(congratulation)
    fun getCopiedCongratulation() = copyCongratulationState

    /**
     * Срабатывает по нажатию кнопки "Отправить"
     */
    fun sendCongratulation() = sendCongratulationState.postValue(congratulation)
    fun getSendCongratulation() = sendCongratulationState

    fun onBackPressed() {
        if (screenState.value?.activeScreen == CONGRATULATION || screenState.value?.activeScreen == SETTINGS)
            screenState.postValue(
                ScreenState(
                    activeScreen = CONFIGURATION,
                    configuration = userConfiguration.value ?: Configuration(),
                    needMoreSettings = screenState.value?.needMoreSettings?: false
                )
            )
    }
}

/**
 * Стейт Активити, указывает какой экран отображать
 * и хранит конфигурацию для генерации поздравления
 */
data class ScreenState(
    val activeScreen: Screen = GREETING,
    val configuration: Configuration = Configuration(),
    val needMoreSettings: Boolean = false,
    val congratulation: String = "",
    val themeIsGreen: Boolean = DataStore.AppConfig.themeColorIsGreen
)

/**
 * Перечисление возможных экранов в приложении
 * [SPLASH] - экран загрузки/сплэш скрин
 * [GREETING] - экран приветствия и описания
 * [CONFIGURATION] - экран настройки генератора поздравлений
 * [CONGRATULATION] - экран сгенерированного поздравления (с него можно вернуться на [CONFIGURATION])
 * [CONFIGURATION] - экран индикации прогресса
 * [CONFIGURATION] - экран настроек
 */
enum class Screen {
    SPLASH, GREETING, CONFIGURATION, CONGRATULATION, PROGRESS, SETTINGS
}