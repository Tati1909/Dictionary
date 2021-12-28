package com.example.dictionary.view.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dictionary.model.data.AppState
import com.example.dictionary.presenter.Presenter

/**
 * Базовая View. Часть функционала каждого экрана будет общей (например, создание презентера),
поэтому имеет смысл вывести его в родительский класс:
 */
abstract class BaseActivity : AppCompatActivity(), View {

    // Храним ссылку на презентер
    protected lateinit var presenter: Presenter<View>

    protected abstract fun createPresenter(): Presenter<View>

    abstract override fun renderData(appState: AppState)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = createPresenter()
    }

    // Когда View готова отображать данные, передаём ссылку на View в презентер
    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    // При пересоздании или уничтожении View удаляем ссылку, иначе в презентере
// будет ссылка на несуществующую View
    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }
}