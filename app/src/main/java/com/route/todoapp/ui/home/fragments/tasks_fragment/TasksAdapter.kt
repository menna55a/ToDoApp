package com.route.todoapp.ui.home.fragments.tasks_fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapp.R
import com.route.todoapp.databinding.ItemTaskBinding
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.entity.Task
import com.route.todoapp.ui.util.getFormattedTime
import java.util.Calendar

class TasksAdapter(private val dao: TasksDao, private val context: Context) :
    RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var tasksList = mutableListOf<Task>()

    fun updateTasks(tasks: MutableList<Task>) {
        tasksList = tasks
        notifyDataSetChanged()
    }

    fun removeTask(position: Int) {
        tasksList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.title.text = task.title
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = task.time
            val hr = calendar.get(Calendar.HOUR)
            val minutes = calendar.get(Calendar.MINUTE)
            binding.time.text = getFormattedTime(hr, minutes)

            if (task.isDone) {
                binding.doneIcon.visibility = View.VISIBLE
                binding.btnTaskIsDone.visibility = View.INVISIBLE
                binding.title.setTextColor(context.getColor(R.color.green))
                binding.draggingBar.setColorFilter(context.getColor(R.color.green))
            } else {
                binding.doneIcon.visibility = View.INVISIBLE
                binding.btnTaskIsDone.visibility = View.VISIBLE
                binding.title.setTextColor(context.getColor(R.color.black))
                binding.draggingBar.setColorFilter(context.getColor(R.color.grey))
            }
            // Mark task as done
            binding.btnTaskIsDone.setOnClickListener {
                task.isDone = true
                notifyItemChanged(adapterPosition)
            }

        }




    }override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task)

        onItemClickListener?.let {
            holder.binding.dragItem.setOnClickListener {
                onItemClickListener?.onClick(position, task)
            }
        }
    }

    var onItemClickListener: OnTaskClickListener?=null

    fun interface OnTaskClickListener {
        fun onClick(position: Int, task: Task)
    }
}

