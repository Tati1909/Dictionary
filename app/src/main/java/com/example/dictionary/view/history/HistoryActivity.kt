package com.example.dictionary.view.history

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.dictionary.databinding.ActivityHistoryBinding
import com.example.dictionary.model.data.AppState
import com.example.dictionary.model.data.DataModel
import com.example.dictionary.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : BaseActivity<AppState, HistoryInteractor>() {

    private lateinit var binding: ActivityHistoryBinding

    override lateinit var model: HistoryViewModel

    private val adapter: HistoryAdapter by lazy { HistoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        iniViewModel()
        initViews()
    }

    // Сразу запрашиваем данные из локального репозитория
    override fun onResume() {
        super.onResume()
        model.loadData("", false)
    }

    // Вызовется из базовой Activity, когда данные будут готовы
    override fun setDataToAdapter(data: List<DataModel>) {
        adapter.setData(data)
    }

    private fun iniViewModel() {
        if (binding.historyActivityRecyclerview.adapter != null) {
            throw IllegalStateException("The ViewModel should be initialised first")
        }
        //import org.koin.android.viewmodel.ext.android.viewModel
        val viewModel: HistoryViewModel by viewModel()
        model = viewModel
        model.subscribe().observe(this@HistoryActivity, Observer<AppState> {
            renderData(it)
        })
    }

    private fun initViews() {
        binding.historyActivityRecyclerview.adapter = adapter
    }
}