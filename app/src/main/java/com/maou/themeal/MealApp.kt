package com.maou.themeal

import android.app.Application
import com.maou.themeal.data.di.dataModule
import com.maou.themeal.data.di.roomModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MealApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MealApp)
            androidLogger(Level.ERROR)

            loadKoinModules(
                listOf(
                    dataModule,
                    roomModule
                )
            )
        }
    }
}