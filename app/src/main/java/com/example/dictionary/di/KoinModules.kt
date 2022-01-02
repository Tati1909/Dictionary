package com.example.dictionary.di

import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.datasource.RetrofitImplementation
import com.example.dictionary.model.datasource.RoomDataBaseImplementation
import com.example.dictionary.model.repository.Repository
import com.example.dictionary.model.repository.RepositoryImplementation
import com.example.dictionary.view.main.MainInteractor
import com.example.dictionary.view.main.MainViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/** Создали два модуля:
 * в одном находятся зависимости, используемые во всём приложении,
 * во втором - зависимости конкретного экрана

Функция single сообщает Koin, что эта зависимость должна храниться
 * в виде синглтона (в Dagger есть похожая аннотация)
 * Аннотация named выполняет аналогичную Dagger функцию
 */
val application = module {

    single<Repository<List<DataModel>>>(named(NAME_REMOTE)) {
        RepositoryImplementation(RetrofitImplementation())
    }
    single<Repository<List<DataModel>>>(named(NAME_LOCAL)) {
        RepositoryImplementation(RoomDataBaseImplementation())
    }
}

/** Функция factory сообщает Koin, что эту зависимость нужно создавать каждый
 * раз заново, что как раз подходит для Activity и её компонентов.
 * get() — создание экземпляра класса.
 */
val mainScreen = module {

    factory {
        MainInteractor(
            remoteRepository = get(qualifier = named(NAME_REMOTE)),
            localRepository = get(qualifier = named(NAME_LOCAL))
        )
    }

    factory {
        MainViewModel(
            interactor = get()
        )
    }
}