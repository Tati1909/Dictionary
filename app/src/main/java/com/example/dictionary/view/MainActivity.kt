package com.example.dictionary.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.BaseActivity
import com.example.description.DescriptionActivity
import com.example.dictionary.R
import com.example.dictionary.convertMeaningsToString
import com.example.dictionary.databinding.ActivityMainBinding
import com.example.historyscreen.HistoryActivity
import com.example.model.data.AppState
import com.example.model.data.userdata.DataModel
import com.example.utils.viewById
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.scope.Scope

/**
 * Экран со списком слов.
 */
class MainActivity : BaseActivity<AppState, MainInteractor>(), AndroidScopeComponent {

    private lateinit var binding: ActivityMainBinding

    /** Теперь ViewModel инициализируется через функцию by viewModel()
     *  Это функция, предоставляемая Koin из коробки через зависимость
     *  import org.koin.androidx.viewmodel.ext.android.viewModel
     */
    override lateinit var model: MainViewModel

    override val scope: Scope by activityScope()

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener) }

    /**
     * Применяем наш собственный делегат
     */
    private val mainActivityRecyclerview by viewById<RecyclerView>(R.id.main_activity_recyclerview)

    /**
     * При клике на кнопку поска
     */
    private val fabClickListener: View.OnClickListener =
        View.OnClickListener {
            val searchDialogFragment = SearchDialogFragment.newInstance()
            searchDialogFragment.setOnSearchClickListener(onSearchClickListener)
            searchDialogFragment.show(supportFragmentManager, BOTTOM_SHEET_FRAGMENT_DIALOG_TAG)
        }

    /**
     * При клике на элемент списка слушатель получает от адаптера необходимые данные и
     * запускает новый экран DescriptionActivity.
     * OnListItemClickListener - наш интерфейс в алаптере.
     */
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                startActivity(
                    DescriptionActivity.getIntent(
                        context = this@MainActivity,
                        word = data.text,
                        description = convertMeaningsToString(data.meanings),
                        url = data.meanings[0].imageUrl
                    )
                )
            }
        }

    private val onSearchClickListener: SearchDialogFragment.OnSearchClickListener =
        object : SearchDialogFragment.OnSearchClickListener {
            override fun onClick(searchWord: String) {
                if (isNetworkAvailable) {
                    /**
                     *  У ViewModel мы получаем LiveData через метод loadData
                     */
                    model.loadData(searchWord, isNetworkAvailable)
                } else {
                    showNoInternetConnectionDialog()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        iniViewModel()
        initViews()
    }

    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.history_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun iniViewModel() {
        /**
         * Убедимся, что модель инициализируется раньше View.
         * Recyclerview без binding, т к написали свой делегат.
         */
        if (mainActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }

        val viewModel: MainViewModel by inject()
        model = viewModel

        model.subscribe().observe(this@MainActivity) { renderData(it) }
    }

    private fun initViews() {
        binding.searchFab.setOnClickListener(fabClickListener)
        binding.mainActivityRecyclerview.layoutManager = LinearLayoutManager(applicationContext)
        mainActivityRecyclerview.adapter = adapter
    }

    companion object {

        private const val BOTTOM_SHEET_FRAGMENT_DIALOG_TAG =
            "74a54328-5d62-46bf-ab6b-cbf5fgt0-092395"
    }
}