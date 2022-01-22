package com.example.historyscreen

import com.example.core.viewmodel.Interactor
import com.example.model.DataModel

/**
 * Класс мало чем отличается от интерактора, который мы уже описывали
 */
class HistoryInteractor(
    private val remoteRepository: com.example.repository.Repository<List<DataModel>>,
    private val localRepository: com.example.repository.RepositoryLocal<List<DataModel>>
) : Interactor<com.example.model.AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): com.example.model.AppState {

        return com.example.model.AppState.Success(
            if (fromRemoteSource) {
                remoteRepository
            } else {
                localRepository
            }.getData(word)
        )
    }
}