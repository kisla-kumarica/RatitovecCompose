package com.example.ratitoveccompose.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Pohod(
    @ColumnInfo
    val Datum: Long,
    @ColumnInfo
    val Type: Int
    )
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}