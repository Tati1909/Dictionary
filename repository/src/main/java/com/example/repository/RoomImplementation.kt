package com.example.repository

import com.example.model.data.AppState
import com.example.model.data.dto.SearchResultDto
import com.example.repository.room.HistoryDao

/**
 * Передаём в конструктор HistoryDao (вспоминаем в модуле Koin - RoomDataBaseImplementation(get())).
 */
class RoomImplementation(
    private val historyDao: HistoryDao
) : DataSourceLocal<List<SearchResultDto>> {

    // Возвращаем список всех слов в виде понятного для Activity List<DataModel>
    override suspend fun getData(word: String): List<SearchResultDto> {

        return mapHistoryEntityToSearchResult(historyDao.all())
    }

    override suspend fun saveToDB(appState: AppState) {

        convertDataModelSuccessToEntity(appState)?.let {
            historyDao.insert(it)
        }
    }
}