package com.example.dictionary.presenter

import io.reactivex.Observable

/**
 * Здесь уже чистая бизнес-логика
 * Use Сase: получение данных для вывода на экран
 * Используем RxJava.
 * fromRemoteSource - либо true(из облака),
 * либо false - из локального источника.
 * <T> - наш дженерик, чтобы тип данных был один.
 */
interface Interactor<T> {

    fun getData(word: String, fromRemoteSource: Boolean): Observable<T>
}