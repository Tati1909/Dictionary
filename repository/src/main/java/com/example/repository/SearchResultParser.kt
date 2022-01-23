package com.example.repository

import com.example.model.AppState

/**
 * Принимаем на вход список слов в виде таблицы из БД и переводим его в List<DataModel>
 */
fun mapHistoryEntityToSearchResult(list: List<com.example.repository.room.HistoryEntity>): List<com.example.model.DataModel> {
    val dataModel = ArrayList<com.example.model.DataModel>()

    if (!list.isNullOrEmpty()) {
        for (entity in list) {
            dataModel.add(com.example.model.DataModel(entity.word, null))
        }
    }
    return dataModel
}

/**
 * convertDataModelSuccessToEntity конвертирует полученный от сервера результат в
данные, доступные для сохранения в БД
 */
fun convertDataModelSuccessToEntity(appState: AppState): com.example.repository.room.HistoryEntity? {
    return when (appState) {
        is AppState.Success -> {
            val searchResult = appState.data
            if (searchResult.isNullOrEmpty() || searchResult[0].text.isNullOrEmpty()) {
                null
            } else {
                com.example.repository.room.HistoryEntity(searchResult[0].text!!, null)
            }
        }
        else -> null
    }
}
