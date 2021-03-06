package com.example.dictionary.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dictionary.convertMeaningsToString
import com.example.dictionary.databinding.MainRecyclerviewItemBinding
import com.example.model.data.userdata.DataModel

class MainAdapter(
    private var onListItemClickListener: OnListItemClickListener
) : RecyclerView.Adapter<MainAdapter.RecyclerItemViewHolder>() {

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
        RecyclerItemViewHolder {
        return RecyclerItemViewHolder(
            MainRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(
        private val binding: MainRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel) {

            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.headerTextviewRecyclerItem.text = data.text
                binding.descriptionTextviewRecyclerItem.text =
                    convertMeaningsToString(data.meanings!!)
                itemView.setOnClickListener { openInNewWindow(data) }
            }
        }
    }

    /**
     * Передаем событие в MainActivity
     */
    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener.onItemClick(listItemData)
    }

    /**
     * Определяем интерфейс обратного вызова.
     * Переопределяем onItemClick в MainActivity и переходим при клике на слово в списке -> на экран DescriptionActivity.
     */
    interface OnListItemClickListener {

        fun onItemClick(data: DataModel)
    }
}