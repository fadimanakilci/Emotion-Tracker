package com.example.emotiontracking.emotiondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.emotiontracking.database.MoodDatabaseDao

class EmotionDetailViewModelFactory(
    private val emotionId: Long,
    private val dataSource: MoodDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmotionDetailViewModel::class.java)) {
            return EmotionDetailViewModel(emotionId, dataSource) as T
        }
        throw IllegalArgumentException("Unknown view model class")
    }
}