package com.example.dictionary.model.datasource.api

import com.example.dictionary.model.data.DataModel
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * Корутина возвращает Deferred.
     * Запускается такая корутина при помощи метода await(в RetrofitImplementation).
     */
    @GET("words/search")
    fun searchAsync(@Query("search") wordToSearch: String): Deferred<List<DataModel>>
}
