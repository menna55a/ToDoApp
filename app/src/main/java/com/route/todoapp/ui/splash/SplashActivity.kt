package com.route.todoapp.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.route.todoapp.R
import com.route.todoapp.ui.home.HomeActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        Handler(mainLooper).postDelayed({
            navigateToHomeActivity()
        },2000)
        settingsMode()
    }
    private fun navigateToHomeActivity() {
        val intent = Intent(this,HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun settingsMode(){
        val sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val savedMode = sharedPreferences.getString("App_Mode", "Light")

        when (savedMode) {
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}