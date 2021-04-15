package com.example.ratitoveccompose.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Pohod::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun PohodDao(): PohodDAO
}
