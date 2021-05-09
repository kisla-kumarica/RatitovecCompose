package com.example.ratitoveccompose.data

import android.app.Application

class MyApplication: Application() {
    lateinit var ThemePreferences: ThemePreferences

    fun init()
    {
        ThemePreferences = ThemePreferences(this)
    }
}