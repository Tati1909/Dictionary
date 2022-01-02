package com.example.dictionary.model.datasource

/**
 * Источник данных для репозитория (Интернет, БД и т. п.)
 */
interface DataSource<T> {

    suspend fun getData(word: String): T
}