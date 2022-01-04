package com.example.dictionary.view.main

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.repository.Interactor
import com.example.dictionary.model.repository.Repository

/**
 * Наш Interactor  c бизнес-правилами(которые пишет системный аналитик).
 * Снабжаем интерактор репозиторием для получения внешних или локальных данных
 * (у репозиториев разная реализация, но один тип Repository<List<DataModel>>).
 * <AppState> можем применять в бизнес-логике(интеракторе) приложения,
 * т к состояние приложения не зависит от фреймворков.
 */
class MainInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: Repository<List<DataModel>>
) : Interactor<AppState> {

    /** Интерактор лишь запрашивает у репозитория данные,
     * детали имплементации интерактору неизвестны
     */
    override suspend fun getData(word: String, fromRemoteSource: Boolean):
            AppState {

        return AppState.Success(
            if (fromRemoteSource) {
                remoteRepository
            } else {
                localRepository
            }.getData(word)
        )
    }
}