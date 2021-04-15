package com.example.ratitoveccompose.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class PohodiViewModel(context: Context) : ViewModel() {
    val repo = AppRepository(context)

    val pohodi = repo.GetAll()

    fun GetAll()
    {
       // pohodi = repo.GetAll()
    }

    fun Insert(pohod: Pohod)
    {
        repo.Insert(pohod)
    }

    fun Remove(pohod: Pohod)
    {
        repo.Remove(pohod)
    }
}