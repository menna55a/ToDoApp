package com.route.todoapp.ui.home.fragments.tasks_fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.route.todoapp.databinding.FragmentTasksBinding
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.ui.home.EditTaskActivity
import com.route.todoapp.ui.util.Constants
import com.route.todoapp.ui.util.clearTime
import java.util.Calendar

class TasksFragment : Fragment() {
    private var _binding: FragmentTasksBinding?= null
    private val binding get() = _binding!!
    private lateinit var dao: TasksDao
    private lateinit var adapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
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

        adapter.onItemClickListener = TasksAdapter.OnTaskClickListener{position,task->
            val intent = Intent(requireContext(), EditTaskActivity::class.java)
            intent.putExtra(Constants.TASK_KEY,task)
            startActivity(intent)
        }

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

    override fun onStart() {
        super.onStart()
        loadAllTasksOfDate(getSeletedDate().timeInMillis)
    }

   private fun getSeletedDate(): Calendar {

        val calender = Calendar.getInstance()
        if(binding.calendarView.selectedDate != null ){
            calender.set(Calendar.YEAR, binding.calendarView.selectedDate!!.year)
        }
        binding.calendarView.selectedDate?.let { date ->
            calender.set(Calendar.YEAR, date.year)
            calender.set(Calendar.MONTH, date.month-1)
            calender.set(Calendar.DAY_OF_MONTH, date.day)
        }
       calender.clearTime()
        return calender
    }

    private fun loadAllTasksOfDate(date: Long) {
        val tasks = dao.getAllTasksByDate(date).toMutableList()
        adapter.updateTasks(tasks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter.onItemClickListener = null
    }
}
