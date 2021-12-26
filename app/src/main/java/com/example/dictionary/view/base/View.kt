package com.example.dictionary.view.base

import com.example.dictionary.model.data.AppState

/**
 * Нижний уровень. View знает о контексте и фреймворке
 * View имеет только один метод, в который приходит некое состояние приложения
 */
interface View {

    fun renderData(appState: AppState)
}
