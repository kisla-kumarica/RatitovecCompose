package com.example.ratitoveccompose.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class PohodiViewModel(context: Context) : ViewModel() {
    val repo = AppRepository(context)

    var pohodi: LiveData<List<Pohod>>

    var filter = MutableLiveData(0)

    init {
        pohodi = Transformations.switchMap(filter) { filter ->
            repo.GetAll(filter)
        }
    }

    fun setFilter(newFilter: Int) {
        filter.postValue(newFilter)
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