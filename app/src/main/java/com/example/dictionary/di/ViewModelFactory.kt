package com.example.dictionary.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Мы наследуемся от стандартной фабрики.
 * MutableMap состоит из:
 * ключ - Class<out ViewModel> - класс обязательно должен быть унаследован от ViewModel.
 * значение - Provider<ViewModel> - предоставляет нам нашу ViewModel.
 */
@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    /**
     * Находим по ключу необходимый провайдер и создаем нашу ViewModel
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val creator = viewModels[modelClass]
            ?: viewModels.asIterable().firstOrNull {
                modelClass.isAssignableFrom(it.key)
            }?.value
            ?: throw IllegalArgumentException("unknown model class $modelClass")

        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}