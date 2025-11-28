package com.example.composehealth

import android.app.Application

class App: Application() {
    val database by lazy { AppDatabase.createDatabase(this) }
}