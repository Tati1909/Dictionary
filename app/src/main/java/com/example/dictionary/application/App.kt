package com.example.dictionary.application

import android.app.Application
import com.example.dictionary.di.application
import com.example.dictionary.di.mainScreen
import org.koin.core.context.startKoin

class App : Application() {

    /**
     * Инициализируем Koin в приложении и прописываем все модули
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(application, mainScreen))
        }
    }
}