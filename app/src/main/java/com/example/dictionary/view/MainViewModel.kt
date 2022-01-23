package com.example.dictionary.view

import androidx.lifecycle.LiveData
import com.example.dictionary.parseOnlineSearchResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val interactor: MainInteractor,
) : com.example.core.viewmodel.BaseViewModel<com.example.model.AppState>() {

    private val liveDataForViewToObserve: LiveData<com.example.model.AppState> = _mutableLiveData

    fun subscribe(): LiveData<com.example.model.AppState> {
        return liveDataForViewToObserve
    }

    /**
     * Кладем новое состояние - загрузка,
     * cancelJob() - завершаем все предыдущие состояния и
     * запускаем корутину для загрузки данных.
     */
    override fun loadData(word: String, isOnline: Boolean) {

        _mutableLiveData.value = com.example.model.AppState.Loading(null)
        cancelJob()
        /**
         * Запускаем корутину для асинхронного доступа к серверу с помощью launch.
         * Можно запустить сопрограмму, используя viewModelScope.launch(более новый вариант),
         * только в нем нет CoroutineExceptionHandler. Для этого нужна библиотека -
         * implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:x.x.x"
         * Библиотека добавляет viewModelScope как функцию расширения ViewModel класса.
         * Эта область связана с Dispatchers.Main и будет автоматически отменена,
         * когда ViewModel будет очищена.
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
            val mapped = parseOnlineSearchResults(result)
            /**
             * меняем поток на main, т к взаимодействуем с UI
             */
            _mutableLiveData.postValue(mapped)
        }

    /**
     * Обрабатываем ошибки
     */
    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(com.example.model.AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = com.example.model.AppState.Success(null)
        super.onCleared()
    }
}