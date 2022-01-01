package com.example.dictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dictionary.model.data.AppState
import com.example.dictionary.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

/**
 * Передаем SchedulerProvider, т. к. он нам пригодится для тестирования приложения
 */
abstract class BaseViewModel<T : AppState>(
    protected val liveDataForViewToObserve: MutableLiveData<T> = MutableLiveData(),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : ViewModel() {

    val viewState: LiveData<T> = liveDataForViewToObserve

    /**
     * Метод, благодаря которому Activity подписывается на изменение данных,
     * возвращает LiveData, через которую и передаются данные
     *  с флагом isOnline(из Интернета или нет)
     */
    abstract fun loadData(word: String, isOnline: Boolean)

    /**
     * Единственный метод класса ViewModel, который вызывается перед уничтожением Activity.
     * ViewModel может быть уничтожена и очистить свои ресурсы.
     * Clear завершает все задачи, которые положили в compositeDisposable(напр. getData)
     */
    override fun onCleared() {
        compositeDisposable.clear()
    }
}