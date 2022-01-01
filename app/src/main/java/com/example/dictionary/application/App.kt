package com.example.dictionary.application

import com.example.dictionary.di.AppComponent
import com.example.dictionary.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * Мы переопределяем метод androidInjector для внедрения зависимостей в Activity.
 * По своей сути — это вспомогательный метод для разработчиков для эффективного внедрения
 * компонентов платформы, таких как Активити, Сервис и т. п.
 */
class App : DaggerApplication() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@App
    }

    override fun applicationInjector(): AndroidInjector<App> =
        applicationComponent

    val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .withApplication(this)
            .build()
    }
}