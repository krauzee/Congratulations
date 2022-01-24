package ru.krauzze.generatecongratulation.util

import android.content.SharedPreferences
import javax.inject.Inject

/**
 * Класс для работы с Shared Preferences
 */
object DataStore {

    @Inject
    lateinit var pref: SharedPreferences

    object Theme {
        var themeColorIsGreen by pref.boolean(def = true)
    }

}