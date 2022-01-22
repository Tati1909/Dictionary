package com.example.repository

import com.example.model.DataModel

class RepositoryImplementation(
    private val dataSource: DataSource<List<DataModel>>
) : Repository<List<DataModel>> {

    /**
     * Репозиторий возвращает данные, используя dataSource (локальный или внешний)
     */
    override suspend fun getData(word: String): List<com.example.model.DataModel> {

        return dataSource.getData(word)
    }
}