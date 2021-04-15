package com.example.ratitoveccompose.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room

class AppRepository(context: Context) {
    private var db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).allowMainThreadQueries().build();

    fun GetAll(): LiveData<List<Pohod>>
    {
        return db.PohodDao().GetAll();
    }

    fun Insert(pohod: Pohod)
    {
        db.PohodDao().Insert(pohod)
    }
    fun Remove(pohod: Pohod)
    {
        db.PohodDao().Remove(pohod)
    }
}