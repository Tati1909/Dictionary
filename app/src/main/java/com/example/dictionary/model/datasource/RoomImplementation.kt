package com.example.dictionary.model.datasource

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.room.HistoryDao
import com.example.dictionary.utils.convertDataModelSuccessToEntity
import com.example.dictionary.utils.mapHistoryEntityToSearchResult

/**
 * Передаём в конструктор HistoryDao (вспоминаем в модуле Koin - RoomDataBaseImplementation(get())).
 */
class RoomImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<DataModel>> {

    // Возвращаем список всех слов в виде понятного для Activity List<DataModel>
    override suspend fun getData(word: String): List<DataModel> {

        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {

        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}