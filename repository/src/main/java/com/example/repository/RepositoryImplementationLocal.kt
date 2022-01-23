package com.example.repository

import com.example.model.DataModel

/**
 * Реализация локального репозитория, который может и получить, и сохранить данные.
 */
class RepositoryImplementationLocal(
    private val dataSource: DataSourceLocal<List<DataModel>>
) : RepositoryLocal<List<DataModel>> {

    override suspend fun getData(word: String): List<DataModel> {
        return dataSource.getData(word)
    }

    override suspend fun saveToDB(appState: com.example.model.AppState) {
        dataSource.saveToDB(appState)
    }
}