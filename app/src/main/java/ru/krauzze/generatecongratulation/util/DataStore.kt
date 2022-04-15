package ru.krauzze.generatecongratulation.util

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Класс для работы с Shared Preferences
 */
object DataStore {

    @Inject
    lateinit var pref: SharedPreferences

    object AppConfig {
        var themeColorIsGreen by pref.boolean(def = true)
        var isGreetingShown by pref.boolean(def = false)
    }
}