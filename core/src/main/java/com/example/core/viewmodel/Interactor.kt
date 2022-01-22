package com.example.core.viewmodel

/**
 * Здесь уже чистая бизнес-логика
 * Use Сase: получение данных для вывода на экран
 * fromRemoteSource - либо true(из облака),
 * либо false - из локального источника.
 * T - чтобы тип данных был один.
 */
interface Interactor<T> {

    suspend fun getData(word: String, fromRemoteSource: Boolean): T
}