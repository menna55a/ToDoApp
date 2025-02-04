package com.route.todoapp.ui.home

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.route.todoapp.databinding.ActivityEditTaskBinding
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.entity.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var dao: TasksDao
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = MyDatabase.getInstance().tasksDao()

        taskId = intent.getIntExtra("TASK_ID", -1) // Get task ID from intent
        if (taskId == -1) {
            Toast.makeText(this, "Invalid task", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        loadTaskDetails()

        binding.btnSaveTask.setOnClickListener {
            updateTask()
        }
    }

    private fun loadTaskDetails() {
        val task = dao.getTaskById(taskId)
        if (task != null) {
            binding.tvEditTaskTitle.setText(task.title)
            binding.etTaskDetails.setText(task.description)
            binding.tvTaskTime.text = getFormattedDate(task.time)
        } else {
            Toast.makeText(this, "Task not found!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun updateTask() {
        val title = binding.tvEditTaskTitle.text.toString()
        val details = binding.etTaskDetails.text.toString()
        val time = System.currentTimeMillis()

        if (title.isEmpty()) {
            Toast.makeText(this, "Title cannot be empty!", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedTask = Task(taskId, title, details, time, time, false)
        dao.updateTask(updatedTask)

        Toast.makeText(this, "Task updated!", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun getFormattedDate(time: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date(time))
    }
}

