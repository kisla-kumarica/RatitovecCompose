package com.example.ratitoveccompose.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room

class AppRepository(context: Context) {
    private var db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "database-name"
    ).fallbackToDestructiveMigration().build();

    fun GetAll(type: Int): LiveData<List<Pohod>>
    {
        return db.PohodDao().GetAll(type);
    }

    fun Insert(pohod: Pohod)
    {
        db.queryExecutor.execute {
            db.PohodDao().Insert(pohod)
        }
    }
    fun Remove(pohod: Pohod)
    {
        db.queryExecutor.execute {
            db.PohodDao().Remove(pohod)
        }
    }
}