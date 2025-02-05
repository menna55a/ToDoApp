package com.route.todoapp.ui.home

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import com.route.todoapp.databinding.ActivityEditTaskBinding
import com.route.todoapp.database.MyDatabase
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.entity.Task
import com.route.todoapp.ui.util.Constants
import com.route.todoapp.ui.util.clearDate
import com.route.todoapp.ui.util.clearSeconds
import com.route.todoapp.ui.util.clearTime
import com.route.todoapp.ui.util.getFormattedTime
import com.route.todoapp.ui.util.showDatePickerDialog
import com.route.todoapp.ui.util.showTimePickerDialog
import java.util.Calendar

class EditTaskActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditTaskBinding
    private lateinit var intentTask: Task
    private lateinit var editTask: Task
    private lateinit var dao: TasksDao
    private var dateCalendar = Calendar.getInstance()
    private var timeCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentTask = IntentCompat.getParcelableExtra(intent,Constants.TASK_KEY,Task::class.java) as Task
        editTask = intentTask.copy()
        dao = MyDatabase.getInstance().tasksDao()
        setupToolBar()
        initViews()
        onSelectDateClick()
        onSelectTimeClick()
        onSaveDada()

    }

    private fun onSaveDada() {
        binding.btnSaveTask.setOnClickListener {
            if (!validateInput()) {
                return@setOnClickListener
            }
            updateTask()
        }
    }
    private fun updateTask() {
        editTask.apply {
            title = binding.title.text.toString()
            description = binding.description.text.toString()
        }
        dao.updateTask(editTask)
        finish()

    }

    fun validateInput():Boolean{
        var isValid = true
        if(binding.title.text.isNullOrBlank()){
            isValid = false
            binding.titleTil.error ="Required Field"
        }else{
            binding.titleTil.error = null
        }
        return isValid
    }

    private fun onSelectTimeClick() {
        binding.selectTimeTv.setOnClickListener {
            val calendar = Calendar.getInstance()
            showTimePickerDialog(calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE),"Select Time:",supportFragmentManager){ hour, minute->
                binding.selectTimeTv.text = getFormattedTime(hour,minute)
                timeCalendar.set(Calendar.HOUR,hour)
                timeCalendar.set(Calendar.MINUTE,minute)
                timeCalendar.clearDate()
                timeCalendar.clearSeconds()
                editTask.time = timeCalendar.timeInMillis

            }
        }
    }

    private fun onSelectDateClick() {
      binding.selectDateTv.setOnClickListener{
          showDatePickerDialog(this){ date, calender ->
              binding.selectDateTv.text = date
              dateCalendar.set(Calendar.YEAR, calender.get(Calendar.YEAR))
              dateCalendar.set(Calendar.MONTH, calender.get(Calendar.MONTH))
              dateCalendar.set(Calendar.DAY_OF_MONTH, calender.get(Calendar.DAY_OF_MONTH))
              dateCalendar.clearTime()
              editTask.time =timeCalendar.timeInMillis
          }
      }

    }
    private fun initViews() {
        binding.title.setText(intentTask.title)
        binding.description.setText(intentTask.description)

        val calender = Calendar.getInstance()
        calender.timeInMillis = intentTask.time
        val year = calender.get(Calendar.YEAR)
        val month = calender.get(Calendar.MONTH)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        binding.selectDateTv.text = "$day/${month-1}/$year"

        calender.timeInMillis = intentTask.date
        val hour = calender.get(Calendar.HOUR)
        val minutes = calender.get(Calendar.MINUTE)
        binding.selectTimeTv.text = getFormattedTime(hour , minutes)

    }

    private fun setupToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeButtonEnabled(true)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

    }
}

