package com.example.dictionary.model.datasource

import com.example.dictionary.model.data.DataModel
import io.reactivex.Observable

/**
 *  Для получения внешних данных мы будем использовать Retrofit
 */
class DataSourceRemote(
    private val remoteProvider: RetrofitImplementation =
        RetrofitImplementation()
) : DataSource<List<DataModel>> {

    override fun getData(word: String): Observable<List<DataModel>> =
        remoteProvider.getData(word)
}