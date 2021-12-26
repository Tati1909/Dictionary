package com.example.dictionary.presenter

import com.example.dictionary.model.data.AppState
import com.example.dictionary.view.base.View

/**
 * На уровень выше находится презентер, который уже ничего не знает ни о контексте, ни о фреймворке
 */
interface Presenter<T : AppState, V : View> {

    fun attachView(view: V)

    fun detachView(view: V)

    /**
     *  Когда активити будет готова запросить данные, она их запросит
     *  с флагом isOnline(из Интернета или нет)
     */
    fun getData(word: String, isOnline: Boolean)
}