package com.example.emotiontracking.mood

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emotiontracking.database.MoodDatabaseDao
import java.lang.IllegalArgumentException

class MoodViewModelFactory(
    private val emotionId: Long,
    private val dataSource: MoodDatabaseDao
):ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MoodViewModel::class.java)){
            return MoodViewModel(emotionId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class !")
    }
}