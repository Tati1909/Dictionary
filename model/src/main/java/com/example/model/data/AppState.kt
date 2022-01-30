package com.example.model.data

import com.example.model.data.userdata.DataModel

/**
 * Добавление модификатора sealed к суперклассу ограничивает возможность создания подклассов.
 * Все прямые подклассы должны быть вложены в суперкласс.
 * Запечатанный класс не может иметь наследников, объявленных вне класса.
 */
sealed class AppState {

    data class Success(val data: List<DataModel>?) : AppState()
    data class Error(val error: Throwable) : AppState()
    data class Loading(val progress: Int?) : AppState()
}