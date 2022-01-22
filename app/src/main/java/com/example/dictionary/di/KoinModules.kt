package com.example.dictionary.di

import androidx.room.Room
import com.example.dictionary.view.MainInteractor
import com.example.dictionary.view.MainViewModel
import com.example.historyscreen.HistoryInteractor
import com.example.historyscreen.HistoryViewModel
import com.example.model.DataModel
import com.example.repository.Repository
import com.example.repository.RepositoryImplementation
import com.example.repository.RepositoryImplementationLocal
import com.example.repository.RepositoryLocal
import com.example.repository.RetrofitImplementation
import com.example.repository.RoomImplementation
import com.example.repository.room.HistoryDataBase
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