package com.example.ratitoveccompose.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Pohod::class), version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PohodDao(): PohodDAO
}
