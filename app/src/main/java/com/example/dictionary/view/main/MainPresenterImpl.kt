package com.example.dictionary.view.main

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.datasource.RetrofitImplementation
import com.example.dictionary.model.datasource.RoomDataBaseImplementation
import com.example.dictionary.model.repository.RepositoryImplementation
import com.example.dictionary.presenter.Interactor
import com.example.dictionary.presenter.Presenter
import com.example.dictionary.rx.ISchedulerProvider
import com.example.dictionary.rx.SchedulerProvider
import com.example.dictionary.view.base.View
import io.reactivex.disposables.CompositeDisposable

/**
 * Интерактор мы создаём сразу в конструкторе.
 * Передаем SchedulerProvider, т. к. он нам пригодится для тестирования приложения
 */
class MainPresenterImpl(
    private val interactor: Interactor<AppState> = MainInteractor(
        RepositoryImplementation(RetrofitImplementation()),
        RepositoryImplementation(RoomDataBaseImplementation())
    ),
    private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    private val schedulerProvider: ISchedulerProvider = SchedulerProvider()
) : Presenter<View> {

    /**
     *  Ссылка на MvpView, никакого контекста
     */
    private var currentView: View? = null

    /**
     * Как только появилась View, сохраняем ссылку на неё для дальнейшей работы.
     */
    override fun attachView(view: View) {
        currentView = view
    }

    /**
     * View скоро будет уничтожена: прерываем все загрузки и
     * обнуляем ссылку, чтобы предотвратить утечки памяти и NPE
     */
    override fun detachView(view: View) {

        if (view == currentView) {
            compositeDisposable.clear()
            currentView = null
        }
    }

    override fun getData(word: String, isOnline: Boolean) {

        compositeDisposable.add(
            interactor
                .getData(word, isOnline)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                /**
                 * Как только начинается загрузка, передаём во View модель данных для
                 * отображения экрана загрузки
                 */
                .doOnSubscribe {
                    currentView?.renderData(AppState.Loading(null))
                }
                /**
                 * Параметры subscribe :
                 * onNext - Если загрузка окончилась успешно, передаем модель с данными для отображения
                 * onError - Если произошла ошибка, передаем модель с ошибкой
                 */
                .subscribe(
                    { appState -> currentView?.renderData(appState) },
                    { throwable -> currentView?.renderData(AppState.Error(throwable)) }
                )
        )
    }
}