package com.example.dictionary.view

import com.example.core.viewmodel.Interactor
import com.example.dictionary.mapSearchResultToResult
import com.example.model.data.AppState
import com.example.model.data.dto.SearchResultDto
import com.example.repository.Repository
import com.example.repository.RepositoryLocal

/**
 * Наш Interactor  c бизнес-правилами(которые пишет системный аналитик).
 * Снабжаем интерактор репозиторием для получения внешних или локальных данных
 * (у репозиториев разная реализация, но один тип Repository<List<DataModel>>).
 * <AppState> можем применять в бизнес-логике(интеракторе) приложения,
 * т к состояние приложения не зависит от фреймворков.
 */
class MainInteractor(
    private val remoteRepository: Repository<List<SearchResultDto>>,
    private val localRepository: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    /** Интерактор лишь запрашивает у репозитория данные,
     * детали имплементации интерактору неизвестны
     */
    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {

        val appState: AppState
        /**
         * Полученное слово мы сохраняем в БД. Сделать это нужно именно здесь,
         * в соответствии с принципами чистой архитектуры: интерактор обращается к репозиторию
         */
        if (fromRemoteSource) {
            appState = AppState.Success(mapSearchResultToResult(remoteRepository.getData(word)))
            localRepository.saveToDB(appState)
        } else {
            appState = AppState.Success(mapSearchResultToResult(localRepository.getData(word)))
        }
        return appState
    }
}