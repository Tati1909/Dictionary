package com.example.dictionary.view.main

import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.datasource.DataSourceLocal
import com.example.dictionary.model.datasource.DataSourceRemote
import com.example.dictionary.model.repository.RepositoryImplementation
import com.example.dictionary.presenter.Presenter
import com.example.dictionary.rx.SchedulerProvider
import com.example.dictionary.view.base.View
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver

/**
 * Интерактор мы создаём сразу в конструкторе.
 * Передаем SchedulerProvider, т. к. он нам пригодится для тестирования приложения
 */
open class MainPresenterImpl<T : AppState, V : View>(
    private val interactor: MainInteractor = MainInteractor(
        RepositoryImplementation(DataSourceRemote()),
        RepositoryImplementation(DataSourceLocal())
    ),
    protected val compositeDisposable: CompositeDisposable = CompositeDisposable(),
    protected val schedulerProvider: SchedulerProvider = SchedulerProvider()
) : Presenter<T, V> {

    // Ссылка на View, никакого контекста
    private var currentView: V? = null

    // Как только появилась View, сохраняем ссылку на неё для дальнейшей работы
    override fun attachView(view: V) {
        if (view != currentView) {
            currentView = view
        }
    }

    /**
     * View скоро будет уничтожена: прерываем все загрузки и обнуляем ссылку,
     * чтобы предотвратить утечки памяти и NPE
     */
    override fun detachView(view: V) {
        compositeDisposable.clear()
        if (view == currentView) {
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
                .doOnSubscribe { currentView?.renderData(AppState.Loading(null)) }
                .subscribeWith(getObserver())
        )
    }

    private fun getObserver(): DisposableObserver<AppState> {
        return object : DisposableObserver<AppState>() {

            override fun onNext(appState: AppState) {
// Если загрузка окончилась успешно, передаем модель с данными
// для отображения
                currentView?.renderData(appState)
            }

            override fun onError(e: Throwable) {
// Если произошла ошибка, передаем модель с ошибкой
                currentView?.renderData(AppState.Error(e))
            }

            override fun onComplete() {
            }
        }
    }
}