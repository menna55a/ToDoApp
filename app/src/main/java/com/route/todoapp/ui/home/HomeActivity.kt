package com.route.todoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.route.todoapp.R
import com.route.todoapp.databinding.ActivityHomeBinding
import com.route.todoapp.database.entity.Task
import com.route.todoapp.ui.home.fragments.AddTaskFragment
import com.route.todoapp.ui.home.fragments.settings_fragment.SettingsFragment
import com.route.todoapp.ui.home.fragments.tasks_fragment.TasksFragment

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setNavigation()
        setOnFabClick()
    }

    private fun setNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem->
            if (menuItem.itemId == R.id.tasks){
                showFragment(TasksFragment())
                binding.title.text = getString(R.string.to_do_list)
            }else if (menuItem.itemId ==R.id.settings){
                showFragment(SettingsFragment())
                binding.title.text = getString(R.string.settings)
            }
            true
        }
        binding.bottomNavigationView.selectedItemId = R.id.tasks
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
            .commit()
    }


    private fun setOnFabClick() {
        binding.fabAddTask.setOnClickListener {
            val bottomSheet = AddTaskFragment()
            bottomSheet.show(supportFragmentManager,"")
            bottomSheet.onTaskAdded = AddTaskFragment.OnTaskAdded { task: Task ->
                //reload data in recyclerview in TasksFragment
            }
        }

    }
}