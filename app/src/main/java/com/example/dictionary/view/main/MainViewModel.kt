package com.example.dictionary.view.main

import androidx.lifecycle.LiveData
import com.example.dictionary.model.data.AppState
import com.example.dictionary.utils.parseSearchResults
import com.example.dictionary.view.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val interactor: MainInteractor,
) : BaseViewModel<AppState>() {

    private val liveDataForViewToObserve: LiveData<AppState> = _mutableLiveData

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    /**
     * Кладем новое состояние - загрузка,
     * cancelJob() - завершаем все предыдущие состояния и
     * запускаем корутину для загрузки данных.
     */
    override fun loadData(word: String, isOnline: Boolean) {

        _mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        /**
         * Запускаем корутину для асинхронного доступа к серверу с помощью launch.
         * Можно запустить сопрограмму, используя viewModelScope.launch(более новый вариант),
         * только в нем нет CoroutineExceptionHandler.
         */
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
    }

    /**
    // withContext(Dispatchers.IO) указывает, что доступ в сеть должен
    // осуществляться через диспетчер IO (который предназначен именно для таких
    // операций), хотя это и не обязательно указывать явно, потому что Retrofit
    // и так делает это благодаря CoroutineCallAdapterFactory(). Это же касается и Room.
     */
    private suspend fun startInteractor(word: String, isOnline: Boolean) =
        withContext(Dispatchers.IO) {
            val result = interactor.getData(word, isOnline)
            val mapped = parseSearchResults(result)
            /**
             * меняем поток на main, т к взаимодействуем с UI
             */
            _mutableLiveData.postValue(mapped)
        }

    /**
     * Обрабатываем ошибки
     */
    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.Success(null)
        super.onCleared()
    }
}