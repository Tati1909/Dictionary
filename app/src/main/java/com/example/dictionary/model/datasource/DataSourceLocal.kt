package com.example.dictionary.model.datasource

import com.example.dictionary.model.data.DataModel
import io.reactivex.Observable

/**
 * Для локальных данных используется Room
 */
class DataSourceLocal(
    private val remoteProvider: RoomDataBaseImplementation =
        RoomDataBaseImplementation()
) : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> =
        remoteProvider.getData(word)
}