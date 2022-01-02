package com.example.dictionary.view.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionary.model.data.AppState
import kotlinx.coroutines.*

abstract class BaseViewModel<T : AppState>(
    protected val _mutableLiveData: MutableLiveData<T> = MutableLiveData()
) : ViewModel() {

    /**
     * Объявляем свой собственный скоуп. В качестве аргумента передается CoroutineContext,
     * который мы составляем через "+" из трех частей:
    // - Dispatchers.Main говорит, что результат работы предназначен для основного потока;
    // - SupervisorJob() позволяет всем дочерним корутинам выполняться независимо, то есть,
    если какая-то корутина упадёт с ошибкой, остальные будут выполнены нормально;
    // - CoroutineExceptionHandler позволяет перехватывать и отрабатывать ошибки и краши
     */
    protected val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.Main
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )

    /**
     * Единственный метод класса ViewModel, который вызывается перед уничтожением Activity.
     * ViewModel может быть уничтожена и очистить свои ресурсы.
     * Завершаем все незавершённые корутины, потому что пользователь закрыл экран
     */
    override fun onCleared() {
        cancelJob()
    }

    protected fun cancelJob() {
        viewModelCoroutineScope.coroutineContext.cancelChildren()
    }

    /**
     * Метод, благодаря которому Activity подписывается на изменение данных,
     * возвращает LiveData, через которую и передаются данные
     *  с флагом isOnline(из Интернета или нет)
     */
    abstract fun loadData(word: String, isOnline: Boolean)

    /**
     * Обрабатываем ошибки в конкретной имплементации базовой ВьюМодели
     */
    abstract fun handleError(error: Throwable)
}