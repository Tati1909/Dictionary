package com.example.dictionary.di

import androidx.room.Room
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.datasource.RetrofitImplementation
import com.example.dictionary.model.datasource.RoomImplementation
import com.example.dictionary.model.repository.Repository
import com.example.dictionary.model.repository.RepositoryImplementation
import com.example.dictionary.model.repository.RepositoryImplementationLocal
import com.example.dictionary.model.repository.RepositoryLocal
import com.example.dictionary.room.HistoryDataBase
import com.example.dictionary.view.history.HistoryInteractor
import com.example.dictionary.view.history.HistoryViewModel
import com.example.dictionary.view.main.MainInteractor
import com.example.dictionary.view.main.MainViewModel
import org.koin.dsl.module

/** Создали три модуля:
 * в application находятся зависимости, используемые во всём приложении,
 * в mainScreen - зависимости конкретного экрана
 * в historyScreen - зависимости базы данных

 * Функция single - зависимость должна храниться в виде синглтона (в Dagger есть похожая аннотация)
 */
val application = module {

    // Создаем базу данных
    single { Room.databaseBuilder(get(), HistoryDataBase::class.java, "HistoryDB").build() }

    //Получаем DAO
    single { get<HistoryDataBase>().historyDao() }

    single<Repository<List<DataModel>>> {
        RepositoryImplementation(RetrofitImplementation())
    }

    single<RepositoryLocal<List<DataModel>>> {
        RepositoryImplementationLocal(RoomImplementation(get()))
    }
}

/**
 * Внедрение зависимостей экрана MainActitvity
 * Функция factory сообщает Koin, что эту зависимость нужно создавать каждый
 * раз заново, что как раз подходит для Activity и её компонентов.
 * get() — создание экземпляра класса.
 */
val mainScreen = module {

    factory { MainViewModel(interactor = get()) }
    factory { MainInteractor(remoteRepository = get(), localRepository = get()) }
}

/**
 * Внедрение зависимостей базы данных
 */
val historyScreen = module {
    factory { HistoryViewModel(interactor = get()) }
    factory { HistoryInteractor(remoteRepository = get(), localRepository = get()) }
}