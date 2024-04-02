package com.example.to_dolistapp.mainActivity

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.to_dolistapp.R
import com.example.to_dolistapp.database.model.TodoModel
import com.zerobranch.layout.SwipeLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodosListAdapter(var todosList: List<TodoModel>? = null) :
    Adapter<TodosListAdapter.TodosListViewHolder>() {
    var onItemClicked: OnItemClicked? = null
    var onItemDeleteClick: OnItemDeleteClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodosListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        return TodosListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return todosList?.size ?: 0
    }

    fun updateData(todosList: List<TodoModel>?) {
        this.todosList = todosList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TodosListViewHolder, position: Int) {
        val item = todosList?.get(position)
        holder.title.text = item?.title
        holder.description.text = item?.description
        holder.dateTextView.text = formatDate(item?.time)

        if (item?.isDone == true) {
            holder.btnIsDone.setBackgroundColor(Color.GREEN)
            holder.title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
            holder.btnIsDone.setBackgroundResource(R.drawable.make_done)
            holder.line.setBackgroundResource(R.drawable.make_done)
        } else {
            holder.btnIsDone.setBackgroundColor(Color.WHITE)
            holder.title.setTextColor(Color.BLACK)
            holder.btnIsDone.setBackgroundResource(R.drawable.make_un_done)
            holder.line.setBackgroundResource(R.drawable.make_un_done)

        }

        holder.card.setOnClickListener {
            onItemClicked?.onItemClick(item!!)
        }

        holder.swipe.setOnActionsListener(object : SwipeLayout.SwipeActionsListener {
            override fun onOpen(direction: Int, isContinuous: Boolean) {}
            override fun onClose() {}
        })

        holder.delete.setOnClickListener {
            onItemDeleteClick?.onItemDeleteClick(position, item!!)
        }
    }


    class TodosListViewHolder(val view: View) : ViewHolder(view) {
        val btnIsDone: ImageView = view.findViewById(R.id.todo_check)
        val title: TextView = view.findViewById(R.id.todo_title_text)
        val dateTextView: TextView = view.findViewById(R.id.todo_time)
        val card: CardView = view.findViewById(R.id.card)
        val swipe: SwipeLayout = view.findViewById(R.id.swipe)
        val delete: LinearLayout = view.findViewById(R.id.delete)
        val description: TextView = view.findViewById(R.id.todo_disc_text)
        val line : View = view.findViewById(R.id.vertical_line)

    }


    private fun formatDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd, HH:mm", Locale.getDefault())
        return dateFormat.format(date)
    }
}

interface OnItemClicked {
    fun onItemClick(todoModel: TodoModel)
}

interface OnItemDeleteClick {
    fun onItemDeleteClick(position: Int, task: TodoModel)
}