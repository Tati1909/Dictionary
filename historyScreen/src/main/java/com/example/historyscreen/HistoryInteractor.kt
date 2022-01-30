package com.example.historyscreen

import com.example.core.viewmodel.Interactor
import com.example.model.data.AppState
import com.example.model.data.dto.SearchResultDto
import com.example.repository.Repository
import com.example.repository.RepositoryLocal

/**
 * Класс мало чем отличается от интерактора, который мы уже описывали
 */
class HistoryInteractor(
    private val remoteRepository: Repository<List<SearchResultDto>>,
    private val localRepository: RepositoryLocal<List<SearchResultDto>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {

        return AppState.Success(
            mapSearchResultToResult(
                if (fromRemoteSource) {
                    remoteRepository
                } else {
                    localRepository
                }.getData(word)
            )
        )
    }
}