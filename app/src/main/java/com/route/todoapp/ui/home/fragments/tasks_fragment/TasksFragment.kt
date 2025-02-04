package com.route.todoapp.ui.home.fragments.tasks_fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.route.todoapp.databinding.FragmentTasksBinding
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.entity.Task
import com.route.todoapp.ui.home.EditTaskActivity
import com.route.todoapp.ui.util.clearTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

//**************************************
// It's Work Without Activity
//****************************************
class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var dao: TasksDao
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = MyDatabase.getInstance().tasksDao()
        adapter = TasksAdapter(dao, requireContext())
        initRecyclerView()
        initCalendarView()
    }
    private fun initRecyclerView() {
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        // Setup Swipe to Delete using SwipeToDeleteCallback
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onItemSwiped(position: Int) {
                // Delete the task from the database and adapter
                val task = adapter.tasksList[position]
                dao.deleteTask(task) // Delete from database
                adapter.removeTask(position) // Remove from adapter
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvTasks)
    }

    private fun initCalendarView() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, date.year)
                calendar.set(Calendar.MONTH, date.month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, date.day)
                calendar.clearTime()
                loadAllTasksOfDate(calendar.timeInMillis)
            }
        }
    }

    private fun loadAllTasksOfDate(date: Long) {
        val tasks = dao.getAllTasksByDate(date).toMutableList()
        adapter.updateTasks(tasks)
    }

}

/*class TasksFragment : Fragment() {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var dao: TasksDao
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }
    companion object {
        const val REQUEST_EDIT_TASK = 1001
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dao = MyDatabase.getInstance().tasksDao()

        adapter = TasksAdapter(
            requireContext(),
            dao.getAllTasks().toMutableList(), // Pass the task list
            onDeleteClick = { task -> deleteTask(task) }
        )

        initRecyclerView()
        initCalendarView()
    }

    private fun initRecyclerView() {
        binding.rvTasks.adapter = adapter
        binding.rvTasks.layoutManager = LinearLayoutManager(requireContext())

        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onItemSwiped(position: Int) {
                adapter.removeTask(position) // Remove from UI
            }
        }
        ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(binding.rvTasks)
    }

    private fun initCalendarView() {
        binding.calendarView.selectedDate = CalendarDay.today()
        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, date.year)
                calendar.set(Calendar.MONTH, date.month - 1)
                calendar.set(Calendar.DAY_OF_MONTH, date.day)
                calendar.clearTime()
                loadAllTasksOfDate(calendar.timeInMillis)
            }
        }
    }

    private fun loadAllTasksOfDate(date: Long) {
        val tasks = dao.getAllTasksByDate(date).toMutableList()
        adapter.updateTasks(tasks)
    }

    private fun openEditTaskActivity(task: Task) {
        val intent = Intent(requireContext(), EditTaskActivity::class.java).apply {
            putExtra("TASK_ID", task.id)
            putExtra("TASK_TITLE", task.title)
            putExtra("TASK_DETAILS", task.description)
            putExtra("TASK_TIME", task.time)
        }
        startActivityForResult(intent, REQUEST_EDIT_TASK)
    }

    private fun deleteTask(task: Task) {
        dao.deleteTask(task)
        loadAllTasksOfDate(task.date) // Refresh the list
    }*/

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDIT_TASK && resultCode == Activity.RESULT_OK) {
            val taskId = data?.getIntExtra("TASK_ID", -1) ?: return
            val updatedTitle = data.getStringExtra("UPDATED_TITLE") ?: return
            val updatedDetails = data.getStringExtra("UPDATED_DETAILS") ?: return
            //val updatedTime = data.getStringExtra("UPDATED_TIME") ?: return
            val updatedTime = "14:30"
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(updatedTime)  // Parse the string into Date
            val updatedTimeMillis = calendar.timeInMillis  // Get the time in milliseconds
            // Create an updated task
            val updatedTask = Task(taskId, updatedTitle, updatedDetails, updatedTimeMillis, System.currentTimeMillis())

            // Update in the database
            dao.updateTask(updatedTask)

            // Update in the adapter (UI)
            adapter.updateTask(updatedTask) // This will refresh the specific item in the list
        }
    }

}*/
