package com.example.repository

import com.example.repository.room.HistoryDao

/**
 * Передаём в конструктор HistoryDao (вспоминаем в модуле Koin - RoomDataBaseImplementation(get())).
 */
class RoomImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<com.example.model.DataModel>> {

    // Возвращаем список всех слов в виде понятного для Activity List<DataModel>
    override suspend fun getData(word: String): List<com.example.model.DataModel> {

        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: com.example.model.AppState) {

        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}