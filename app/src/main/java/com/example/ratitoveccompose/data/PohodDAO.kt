package com.example.ratitoveccompose.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PohodDAO {
    @Query("SELECT * FROM Pohod WHERE :type = 2 OR Type = :type ORDER BY Datum DESC")
    fun GetAll(type: Int): LiveData<List<Pohod>>

    @Insert
    fun Insert(pohod: Pohod)

    @Delete
    fun Remove(pohod: Pohod)

    @Query("SELECT COUNT(*) FROM Pohod")
    fun Count(): Int
}