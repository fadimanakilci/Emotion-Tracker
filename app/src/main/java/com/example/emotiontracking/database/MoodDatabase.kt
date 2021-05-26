package com.example.emotiontracking.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Mood :: class], version = 1, exportSchema = false)
abstract class MoodDatabase : RoomDatabase() {
    abstract val databaseAccessObject: MoodDatabaseDao

    companion object{
        // Volatile nesnenin diskte değil ramde tutulmasını sağlar
        // Normalde veritabanları diskte tutulur ancak şuan deneme yapıyoruz
        @Volatile
        private var SAMPLE_OBJECT: MoodDatabase? = null

        fun getSample(connect: Context): MoodDatabase {
            synchronized(this){
                // private olan değişkenimize dışarıdan erişebilmek için referans oluşturduk
                var sample = SAMPLE_OBJECT
                if (sample == null){
                    sample = Room.databaseBuilder(
                            connect.applicationContext,
                            MoodDatabase::class.java,
                            "mood_tracking_table"
                    ).fallbackToDestructiveMigration().build()
                    SAMPLE_OBJECT = sample
                }
                return sample
            }
        }
    }
}