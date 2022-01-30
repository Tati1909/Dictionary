package com.example.repository

import com.example.model.data.AppState

/**
 * Локальный репозитоиий для базы данных
 */
interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: AppState)
}