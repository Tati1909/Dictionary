package com.example.dictionary.viewmodel

import androidx.lifecycle.LiveData
import com.example.dictionary.model.data.AppState
import com.example.dictionary.view.main.MainInteractor
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val interactor: MainInteractor,
) : BaseViewModel<AppState>() {

    fun subscribe(): LiveData<AppState> {
        return liveDataForViewToObserve
    }

    override fun loadData(word: String, isOnline: Boolean) {

        liveDataForViewToObserve.value = AppState.Loading(null)

        compositeDisposable.add(
            interactor.getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ state ->
                    /**Данные успешно загружены;
                     * сохраняем их и передаем во View (через LiveData).*/
                    liveDataForViewToObserve.value = state
                }, { throwable ->
                    liveDataForViewToObserve.value = AppState.Error(throwable)
                })
        )
    }
}