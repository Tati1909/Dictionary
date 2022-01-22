package com.example.dictionary.view

import com.example.model.DataModel

/**
 * Наш Interactor  c бизнес-правилами(которые пишет системный аналитик).
 * Снабжаем интерактор репозиторием для получения внешних или локальных данных
 * (у репозиториев разная реализация, но один тип Repository<List<DataModel>>).
 * <AppState> можем применять в бизнес-логике(интеракторе) приложения,
 * т к состояние приложения не зависит от фреймворков.
 */
class MainInteractor(
    private val remoteRepository: com.example.repository.Repository<List<DataModel>>,
    private val localRepository: com.example.repository.RepositoryLocal<List<DataModel>>
) : com.example.core.viewmodel.Interactor<com.example.model.AppState> {

    /** Интерактор лишь запрашивает у репозитория данные,
     * детали имплементации интерактору неизвестны
     */
    override suspend fun getData(word: String, fromRemoteSource: Boolean): com.example.model.AppState {

        val appState: com.example.model.AppState
        /**
         * Полученное слово мы сохраняем в БД. Сделать это нужно именно здесь,
         * в соответствии с принципами чистой архитектуры: интерактор обращается к репозиторию
         */
        if (fromRemoteSource) {
            appState = com.example.model.AppState.Success(remoteRepository.getData(word))
            localRepository.saveToDB(appState)
        } else {
            appState = com.example.model.AppState.Success(localRepository.getData(word))
        }
        return appState
    }
}