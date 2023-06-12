package com.ctacek.yandexschool.doitnow.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ctacek.yandexschool.doitnow.R
import com.ctacek.yandexschool.doitnow.data.model.Priority
import com.ctacek.yandexschool.doitnow.data.model.Todoitem
import com.ctacek.yandexschool.doitnow.databinding.ItemTaskBinding
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.SimpleDateFormat

class ItemDiffUtil(
    private val oldList: List<Todoitem>,
    private val newList: List<Todoitem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem == newItem
    }
}


class ToDoItemAdapter(private val toDoItemActionListener: ToDoItemActionListener) :
    RecyclerView.Adapter<ToDoItemAdapter.ItemViewHolder>(), View.OnClickListener {

    var items: List<Todoitem> = emptyList()
        set(newValue) {
            val personDiffUtil = ItemDiffUtil(field, newValue)
            val personDiffUtilResult = DiffUtil.calculateDiff(personDiffUtil)
            field = newValue
            personDiffUtilResult.dispatchUpdatesTo(this)
        }

    @SuppressLint("SimpleDateFormat")
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkbox: MaterialCheckBox
        private val title: TextView
        private val priority: ImageView
        private val data: TextView
        private val dataFormat: SimpleDateFormat


        init {
            checkbox = itemView.findViewById(R.id.isCompleted)
            title = itemView.findViewById(R.id.title)
            priority = itemView.findViewById(R.id.priority)
            data = itemView.findViewById(R.id.data)
            dataFormat = SimpleDateFormat("d MMMM")
        }


        fun bind(item: Todoitem) {
            title.text = item.description
            checkbox.isChecked = item.status

            if (item.endDate != null) {
                data.visibility = View.VISIBLE
                data.text =
                    itemView.context.getString(R.string.infodata, dataFormat.format(item.endDate))
            } else {
                data.visibility = View.GONE
            }

            if (item.status) {
                title.paintFlags = title.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                checkbox.buttonTintList = AppCompatResources.getColorStateList(
                    itemView.context,
                    R.color.color_light_green
                )
                priority.visibility = View.GONE
            } else {
                title.paintFlags = title.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                when (item.priority) {
                    Priority.LOW -> {
                        priority.visibility = View.VISIBLE
                        priority.setImageDrawable(
                            AppCompatResources.getDrawable(
                                itemView.context,
                                R.drawable.priority1
                            )
                        )
                        checkbox.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context, R.color.grey)
                    }

                    Priority.STANDARD -> {
                        priority.visibility = View.GONE
                        checkbox.buttonTintList =
                            AppCompatResources.getColorStateList(itemView.context, R.color.grey)

                    }

                    Priority.HIGH -> {
                        priority.visibility = View.VISIBLE
                        priority.setImageDrawable(
                            AppCompatResources.getDrawable(
                                itemView.context,
                                R.drawable.priority3
                            )
                        )
                        checkbox.buttonTintList = AppCompatResources.getColorStateList(
                            itemView.context,
                            R.color.color_light_red
                        )
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.isCompleted.setOnClickListener(this)

        return ItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    interface ToDoItemActionListener {
        fun onItemCheck(item: Todoitem)
        fun onItemRemove(item: Todoitem)
        fun onItemGetId(item: Todoitem)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onClick(view: View) {
        val item: Todoitem = view.tag as Todoitem

        when (view.id) {
            R.id.isCompleted -> toDoItemActionListener.onItemCheck(item)
            else -> toDoItemActionListener.onItemGetId(item)
        }
    }
}