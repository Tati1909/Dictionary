package com.example.dictionary.view.base

import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.model.data.AppState
import com.example.dictionary.viewmodel.BaseViewModel
import com.example.dictionary.viewmodel.Interactor

abstract class BaseActivity<T : AppState, I : Interactor<T>> : AppCompatActivity() {

    /**
     * В каждой Активити будет своя ViewModel, которая наследуется от BaseViewModel
     */
    abstract val viewModel: BaseViewModel<T>

    /**
     * Каждая Активити будет отображать какие-то данные в соответствующем состоянии
     */
    abstract fun renderData(appState: T)
}