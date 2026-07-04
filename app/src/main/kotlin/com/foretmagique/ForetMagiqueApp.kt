package com.foretmagique

import android.app.Application
import com.foretmagique.di.AppContainer

class ForetMagiqueApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
