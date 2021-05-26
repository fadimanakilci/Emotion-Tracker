package com.example.emotiontracking.database

// Room ile veritabanında tablo oluşturduk
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Bu classın veri tabanında bir varlık olduğunu söyler. Yani tablonun bir satırını oluşturur.
@Entity(tableName = "mood_tracking_table")
data class Mood(
        @PrimaryKey(autoGenerate = true)
        var id: Long = 0L,

        @ColumnInfo(name = "start_time_milli")
        val startTimeMilli: Long = System.currentTimeMillis(),

        @ColumnInfo(name = "end_time_milli")
        var endTimeMilli: Long = startTimeMilli,

        @ColumnInfo(name = "quality_rating")
        var mood: Int = -1
)
