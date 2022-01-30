package com.example.repository

import com.example.model.data.dto.SearchResultDto
import com.example.repository.api.ApiService
import com.example.repository.api.BaseInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit. Для корректной работы Ретрофита мы создадим два класса: BaseInterceptor и ApiService.
 * Благодаря BaseInterceptor мы можем выводить в логи запросы на сервер и его ответы,
 * а также обрабатывать ошибки сервера.
 * ApiService — это имплементация запроса через Retrofit.
 */
class RetrofitImplementation : DataSource<List<SearchResultDto>> {

    /**
     * Добавляем  await, т к searchAsync возвращает Deferred(Отложенное значение).
     * Функция await говорит о том, что мы должны дождаться выполнения корутины, запущенной через async.
     * Соответственно, функция, которая ждёт корутину, должна быть с ключевым словом suspend.*/
    override suspend fun getData(word: String): List<SearchResultDto> {
        return getService(BaseInterceptor.interceptor).searchAsync(word).await()
    }

    private fun getService(interceptor: Interceptor): ApiService {
        return createRetrofit(interceptor).create(ApiService::class.java)
    }

    /**
     * В addCallAdapterFactory теперь передаётся CoroutineCallAdapterFactory(),
     * которая позволяет Retrofit работать с корутинами. Для ее использования нужно прописать
     * для Ретрофита зависимость вместо той, которая была для Rx:
     * implementation 'com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2'
     */
    private fun createRetrofit(interceptor: Interceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_LOCATIONS)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(createOkHttpClient(interceptor))
            .build()
    }

    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        return httpClient.build()
    }

    companion object {
        private const val BASE_URL_LOCATIONS =
            "https://dictionary.skyeng.ru/api/public/v1/"
    }
}