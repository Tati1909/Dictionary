package com.example.dictionary.di

import android.app.Application
import com.example.dictionary.application.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Тут мы прописываем все наши модули, включая AndroidSupportInjectionModule.
 * Этот класс создаётся Dagger’ом. Он как раз связан с аннотацией
 * ContributesAndroidInjector выше и позволяет внедрять в Activity все необходимые зависимости
 */
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        InteractorModule::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ActivityModule::class,
    ]
)
@Singleton
interface AppComponent : AndroidInjector<App> {

    /**
     * Этот билдер мы вызовем из класса TranslatorApp, который наследует Application
     */
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withApplication(application: Application): Builder

        fun build(): AppComponent
    }
}