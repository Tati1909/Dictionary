package com.example.dictionary.view.history

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.repository.Interactor
import com.example.dictionary.model.repository.Repository
import com.example.dictionary.model.repository.RepositoryLocal

/**
 * Класс мало чем отличается от интерактора, который мы уже описывали
 */
class HistoryInteractor(
    private val remoteRepository: Repository<List<DataModel>>,
    private val localRepository: RepositoryLocal<List<DataModel>>
) : Interactor<AppState> {

    override suspend fun getData(word: String, fromRemoteSource: Boolean): AppState {

        return AppState.Success(
            if (fromRemoteSource) {
                remoteRepository
            } else {
                localRepository
            }.getData(word)
        )
    }
}