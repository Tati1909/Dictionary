package com.example.dictionary.view.main

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dictionary.*
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.utils.network.isOnline
import com.example.dictionary.view.base.BaseActivity
import com.example.dictionary.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Архитектура нашего приложения будет строиться по MVVM:
1. View
2. ViewModel
3. Репозиторий (Repository), с помощью которого мы будем получать данные из сети или БД
4. Источник данных для репозитория (DataSource) — конкретные имплементации Retrofit или БД
 */
class MainActivity : BaseActivity<AppState, MainInteractor>() {

    private lateinit var binding: ActivityMainBinding

    /**
     *     Внедряем фабрику для создания ViewModel
     */
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    /**override val viewModel: MainViewModel by lazy {
    ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }*/
    override lateinit var viewModel: MainViewModel

    private var adapter: MainAdapter? = null

    private val fabClickListener: View.OnClickListener =
        View.OnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    private val onSearchClickListener: SearchDialogFragment.OnSearchClickListener =

        object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                isNetworkAvailable = isOnline(applicationContext)
                if (isNetworkAvailable) {
                    /**
                     *  У ViewModel мы получаем LiveData через метод loadData и
                     *  подписываемся на изменения, передавая туда observer
                     */
                    viewModel.loadData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * Сообщаем Dagger’у, что тут понадобятся зависимости
         */
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Фабрика уже готова, можно создавать ViewModel
         */
        viewModel = viewModelFactory.create(MainViewModel::class.java)
        viewModel.subscribe().observe(this@MainActivity, Observer<AppState> {
            renderData(it)
        })

        binding.searchFab.setOnClickListener(fabClickListener)

    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                showViewWorking()
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showAlertDialog(
                        getString(R.string.dialog_tittle_sorry),
                        getString(R.string.empty_server_response_on_success)
                    )
                } else {
                    showViewWorking()
                    if (adapter == null) {
                        binding.mainActivityRecyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding.mainActivityRecyclerview.adapter =
                            MainAdapter(onListItemClickListener, dataModel)
                    } else {
                        adapter!!.setData(dataModel)
                    }
                }
            }
            is AppState.Loading -> {
                showViewLoading()
                if (appState.progress != null) {
                    binding.progressBarHorizontal.visibility = VISIBLE
                    binding.progressBarRound.visibility = GONE
                    binding.progressBarHorizontal.progress = appState.progress
                } else {
                    binding.progressBarHorizontal.visibility = GONE
                    binding.progressBarRound.visibility = VISIBLE
                }
            }
            is AppState.Error -> {
                showViewWorking()
                showAlertDialog(getString(R.string.error_stub), appState.error.message)
            }
        }
    }

    private fun showViewWorking() {
        binding.loadingFrameLayout.visibility = GONE
    }

    private fun showViewLoading() {
        binding.loadingFrameLayout.visibility = VISIBLE
    }

    companion object {
        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}