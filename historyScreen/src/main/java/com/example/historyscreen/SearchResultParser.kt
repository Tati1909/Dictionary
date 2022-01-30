package com.example.historyscreen

import com.example.model.data.AppState
import com.example.model.data.dto.SearchResultDto
import com.example.model.data.userdata.DataModel
import com.example.model.data.userdata.Meaning
import com.example.model.data.userdata.TranslatedMeaning

/**
 * Все методы говорят сами за себя, универсальны и парсят данные в зависимости от источника данных (интернет или БД),
 * возвращая их в понятном для наших Activity форматах
 */

fun parseLocalSearchResults(appState: AppState): AppState {
    return AppState.Success(mapResult(appState, true))
}

private fun mapResult(
    appState: AppState,
    isOnline: Boolean
): List<DataModel> {
    val newSearchResults = arrayListOf<DataModel>()
    when (appState) {
        is AppState.Success -> {
            getSuccessResultData(appState, isOnline, newSearchResults)
        }
        else -> {}
    }
    return newSearchResults
}

private fun getSuccessResultData(
    data: AppState.Success,
    isOnline: Boolean,
    newSearchDataModels: ArrayList<DataModel>
) {
    val searchDataModels: List<DataModel> = data.data as List<DataModel>
    if (searchDataModels.isNotEmpty()) {
        if (isOnline) {
            for (searchResult in searchDataModels) {
                parseOnlineResult(searchResult, newSearchDataModels)
            }
        } else {
            for (searchResult in searchDataModels) {
                newSearchDataModels.add(
                    DataModel(
                        searchResult.text,
                        arrayListOf()
                    )
                )
            }
        }
    }
}

private fun parseOnlineResult(
    dataModel: DataModel,
    newDataModels: ArrayList<DataModel>
) {
    if (dataModel.text.isNotBlank() && dataModel.meanings.isNotEmpty()) {
        val newMeanings = arrayListOf<Meaning>()
        for (meaning in dataModel.meanings) {
            if (meaning.translatedMeaning.translatedMeaning.isBlank()) {
                newMeanings.add(Meaning(meaning.translatedMeaning, meaning.imageUrl))
            }
        }
        if (newMeanings.isNotEmpty()) {
            newDataModels.add(DataModel(dataModel.text, newMeanings))
        }
    }
}

/**
 * перенесём данные, полученные из интернета, в модель, подготовленную для отображения данных
 */
fun mapSearchResultToResult(searchResults: List<SearchResultDto>):
    List<DataModel> {
    return searchResults.map { searchResult ->
        var meanings: List<Meaning> = listOf()
        searchResult.meanings?.let { // Дополнительная проверка для
// HistoryScreen, так как там сейчас не отображаются значения
            meanings = it.map { meaningsDto ->
                Meaning(
                    TranslatedMeaning(
                        meaningsDto.translationDto?.translation ?: ""
                    ),
                    meaningsDto.imageUrl ?: ""
                )
            }
        }
        DataModel(searchResult.text ?: "", meanings)
    }
}