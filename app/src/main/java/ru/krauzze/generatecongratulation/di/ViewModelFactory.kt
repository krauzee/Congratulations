package ru.krauzze.generatecongratulation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.krauzze.generatecongratulation.GenerateCongratulationApplication.Companion.APP_SCOPE
import toothpick.Toothpick
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor() : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        Toothpick.openScope(APP_SCOPE).getInstance(modelClass) as T

}