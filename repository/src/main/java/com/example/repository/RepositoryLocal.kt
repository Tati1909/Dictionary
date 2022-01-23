package com.example.repository

/**
 * Локальный репозитоиий для базы данных
 */
interface RepositoryLocal<T> : Repository<T> {

    suspend fun saveToDB(appState: com.example.model.AppState)
}