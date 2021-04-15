package com.example.ratitoveccompose.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PohodDAO {
    @Query("SELECT * FROM Pohod")
    fun GetAll(): LiveData<List<Pohod>>

    @Insert
    fun Insert(pohod: Pohod)

    @Delete
    fun Remove(pohod: Pohod)
}