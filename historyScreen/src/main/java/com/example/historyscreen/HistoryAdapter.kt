package com.example.historyscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.historyscreen.databinding.HistoryRecyclerviewItemBinding
import com.example.model.DataModel

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = arrayListOf()

    // Метод передачи данных в адаптер
    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            HistoryRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(
        private val binding: HistoryRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.headerHistoryTextviewRecyclerItem.text = data.text
                itemView.setOnClickListener {
                    Toast.makeText(
                        itemView.context, "on click: ${data.text}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}