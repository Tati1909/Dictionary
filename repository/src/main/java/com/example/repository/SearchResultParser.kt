package com.example.repository

import com.example.model.data.AppState
import com.example.model.data.dto.SearchResultDto
import com.example.repository.room.HistoryEntity

/**
 * Принимаем на вход список слов в виде таблицы из БД и переводим его в List<DataModel>
 */
fun mapHistoryEntityToSearchResult(list: List<HistoryEntity>): List<SearchResultDto> {
    val searchResult = ArrayList<SearchResultDto>()

    if (!list.isNullOrEmpty()) {
        for (entity in list) {
            searchResult.add(SearchResultDto(entity.word, null))
        }
    }
    return searchResult
}

/**
 * convertDataModelSuccessToEntity конвертирует полученный от сервера результат в
данные, доступные для сохранения в БД
 */
fun convertDataModelSuccessToEntity(appState: AppState): HistoryEntity? {
    return when (appState) {
        is AppState.Success -> {
            val searchResult = appState.data
            if (searchResult.isNullOrEmpty() || searchResult[0].text.isEmpty()) {
                null
            } else {
                HistoryEntity(searchResult[0].text, null)
            }
        }
        else -> null
    }
}
