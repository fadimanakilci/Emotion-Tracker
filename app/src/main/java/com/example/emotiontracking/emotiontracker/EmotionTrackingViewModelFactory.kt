package com.example.emotiontracking.emotiontracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emotiontracking.database.MoodDatabaseDao
import java.lang.IllegalArgumentException

class EmotionTrackingViewModelFactory(
        private val dataSource: MoodDatabaseDao,
        private val application: Application
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?>create(modelClass: Class<T>):T{
        if (modelClass.isAssignableFrom(EmotionTrackingViewModel::class.java)){
            return EmotionTrackingViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class !")
    }
}