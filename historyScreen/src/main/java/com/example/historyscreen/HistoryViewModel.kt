package com.example.historyscreen

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch

class HistoryViewModel(private val interactor: HistoryInteractor) :
    com.example.core.viewmodel.BaseViewModel<com.example.model.AppState>() {

    private val liveDataForViewToObserve: LiveData<com.example.model.AppState> = _mutableLiveData

    fun subscribe(): LiveData<com.example.model.AppState> {
        return liveDataForViewToObserve
    }

    override fun loadData(word: String, isOnline: Boolean) {
        _mutableLiveData.value = com.example.model.AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch { startInteractor(word, isOnline) }
    }

    private suspend fun startInteractor(word: String, isOnline: Boolean) {

        _mutableLiveData.postValue(parseLocalSearchResults(interactor.getData(word, isOnline)))
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(com.example.model.AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = com.example.model.AppState.Success(null)
        // Set View to original state in onStop
        super.onCleared()
    }
}