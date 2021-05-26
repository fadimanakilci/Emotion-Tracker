package com.example.emotiontracking.mood

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emotiontracking.database.MoodDatabaseDao
import kotlinx.coroutines.launch

class MoodViewModel(
    private val emotionId: Long = 0L,
    val database: MoodDatabaseDao
): ViewModel() {
    // EmotionTrackingFragment e geçiş
    private val _orientationEmotionTracking = MutableLiveData<Boolean?>()

    val orientationEmotionTracking: LiveData<Boolean?>
        get() = _orientationEmotionTracking

    fun orientationCompleted(){
        _orientationEmotionTracking.value = null
    }

    fun makeAMoodSelection(mood: Int){
        viewModelScope.launch {
            val emotion = database.get(emotionId) ?: return@launch
            emotion.mood = mood
            database.update(emotion)
            _orientationEmotionTracking.value = true
        }
    }
}