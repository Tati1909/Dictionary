package com.example.dictionary.model.repository

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.datasource.DataSourceLocal

/**
 * Реализация локального репозитория, который может и получить, и сохранить данные.
 */
class RepositoryImplementationLocal(
    private val dataSource: DataSourceLocal<List<DataModel>>
) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: AppState) {
        dataSource.saveToDB(appState)
    }
}