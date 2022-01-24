package ru.krauzze.generatecongratulation

import android.app.Application
import ru.krauzze.generatecongratulation.di.AppModule
import ru.krauzze.generatecongratulation.util.DataStore
import toothpick.Toothpick
import toothpick.configuration.Configuration
import toothpick.smoothie.module.SmoothieApplicationModule

class GenerateCongratulationApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initToothpick()
        initAppScope()
    }

    private fun initToothpick() {
        if (BuildConfig.DEBUG) {
            Toothpick.setConfiguration(Configuration.forDevelopment().preventMultipleRootScopes())
        } else {
            Toothpick.setConfiguration(Configuration.forProduction())
        }
    }

    private fun initAppScope() {
        val appScope = Toothpick.openScope(APP_SCOPE)
        appScope.installModules(
            SmoothieApplicationModule(this),
            AppModule(this)
        )
        listOf(DataStore).forEach {
            Toothpick.inject(it, appScope)
        }
    }

    companion object {
        const val APP_SCOPE: String = "APP_SCOPE"
        const val VIEW_MODEL_SCOPE: String = "VIEW_MODEL_SCOPE"
    }
}