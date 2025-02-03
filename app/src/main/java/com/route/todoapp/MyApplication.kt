package com.route.todoapp

import android.app.Application
import com.route.todoapp.database.MyDatabase

class MyApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        MyDatabase.init(this)
    }
}