package com.example.dictionary.model.repository

import com.example.dictionary.model.data.AppState

/**
 * Локальный репозитоиий для базы данных
 */
interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
}