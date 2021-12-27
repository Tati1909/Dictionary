package com.example.dictionary.model.repository

import com.example.dictionary.model.data.DataModel
import com.example.dictionary.model.datasource.DataSource
import io.reactivex.Observable

class RepositoryImplementation(
    private val dataSource: DataSource<List<DataModel>>
) : Repository<List<DataModel>> {

    private val cache = mutableMapOf<String, List<DataModel>>()

    /**
     * Репозиторий возвращает данные, используя dataSource (локальный или внешний)
     * Если это слово есть в кэше, то верни нам его значения из кэша,
     * если его нет в кэше, то верни нам его значения из сети и добавь в кэш.
     */
    override fun getData(word: String): Observable<List<DataModel>> {

        if (cache.containsKey(word))
            return Observable.just(cache[word])

        return dataSource.getData(word)
            .doOnNext { cache[word] = it }
    }
}