package com.example.dictionary.view.base

import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.model.data.AppState
import com.example.dictionary.viewmodel.BaseViewModel

abstract class BaseActivity<T : AppState> : AppCompatActivity() {

    /**
     * В каждой Активити будет своя ViewModel, которая наследуется от BaseViewModel
     */
    abstract val model: BaseViewModel<T>

    /**
     * Каждая Активити будет отображать какие-то данные в соответствующем состоянии
     */
    protected abstract fun renderData(appState: T)
}