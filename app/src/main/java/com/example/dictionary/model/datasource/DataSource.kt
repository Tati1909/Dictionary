package com.example.dictionary.model.datasource

import com.example.dictionary.model.data.AppState

/**
 * Источник данных для репозитория (Интернет, БД и т. п.)
 */
interface DataSource<T> {

    suspend fun getData(word: String): T
}

/**
 * Интерфейс для сохранения данных в локальную базу данных
 */
interface DataSourceLocal<T> : DataSource<T> {

    suspend fun saveToDB(appState: AppState)
}