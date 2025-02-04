package com.route.todoapp.ui.home.fragments.tasks_fragment

import android.app.AlertDialog
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.route.todoapp.R
import com.route.todoapp.databinding.DialogEditTaskBinding
import com.route.todoapp.databinding.ItemTaskBinding
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.entity.Task
import com.route.todoapp.ui.home.EditTaskActivity
import com.route.todoapp.ui.util.getFormattedTime
import java.util.Calendar
import java.util.Locale
//**************************************
// It's Work Without Activity
//****************************************

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
            //  Open Edit Task Dialog
            binding.root.setOnClickListener {
                showEditDialog(task)
            }
        }

       private fun updateTaskStatus(task: Task) {
            dao.updateTask(task)
        }

        private fun showEditDialog(task: Task) {
            val dialogViewBinding = DialogEditTaskBinding.inflate(LayoutInflater.from(context))
            dialogViewBinding.etTaskTitle.setText(task.title)
            dialogViewBinding.etTaskDescription.setText(task.description)

            AlertDialog.Builder(context)
                .setTitle("Edit Task")
                .setView(dialogViewBinding.root)
                .setPositiveButton("Save") { _, _ ->
                    task.title = dialogViewBinding.etTaskTitle.text.toString()
                    dao.updateTask(task)
                    notifyItemChanged(adapterPosition)
                }

                .setNegativeButton("Cancel", null)
                .show()
        }

    }override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task)
    }
}


/*class TasksAdapter(
    private val context: Context,
    private var tasks: MutableList<Task>,
    private val onDeleteClick: (Task) -> Unit
) : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

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

            // Edit Task
            binding.root.setOnClickListener {
                val intent = Intent(context, EditTaskActivity::class.java).apply {
                    putExtra("TASK_ID", task.id)
                    putExtra("TASK_TITLE", task.title)
                    putExtra("TASK_DETAILS", task.description)
                    putExtra("TASK_TIME", task.time)
                }
                (context as Activity).startActivityForResult(intent, TasksFragment.REQUEST_EDIT_TASK)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(newTasks: List<Task>) {
        tasks.clear()
        tasks.addAll(newTasks)
        notifyDataSetChanged()
    }
    fun updateTask(updatedTask: Task) {
        val index = tasks.indexOfFirst { it.id == updatedTask.id }
        if (index != -1) {
            tasks[index] = updatedTask
            notifyItemChanged(index)
        }
    }
    fun removeTask(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }
}*/



/*class TasksAdapter(private val dao: TasksDao, private val context: Context, private val fragment: TasksFragment)
    : RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {

    var tasksList = mutableListOf<Task>()

    fun updateTasks(tasks: MutableList<Task>) {
        tasksList = tasks
        notifyDataSetChanged()
    }

    fun removeTask(position: Int) {
        tasksList.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
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
                dao.updateTask(task)
                notifyItemChanged(adapterPosition)
            }

            // Open EditTaskActivity when clicked
            binding.root.setOnClickListener {
                fragment.openEditTaskActivity(task.id)
            }
        }

        private fun getFormattedTime(hour: Int, minutes: Int): String {
            return String.format(Locale.getDefault(), "%02d:%02d", hour, minutes)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
        TaskViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.bind(task)
    }
}
*/

