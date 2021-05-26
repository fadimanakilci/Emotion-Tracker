package com.example.emotiontracking.emotiondetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emotiontracking.database.Mood
import com.example.emotiontracking.database.MoodDatabaseDao
import kotlinx.coroutines.launch

class EmotionDetailViewModel(
    private val emotionId: Long = 0L,
    val database: MoodDatabaseDao
) : ViewModel() {

    private val mood: LiveData<Mood?>

    fun getMood() = mood

    init {
        mood = database.getId(emotionId)
    }

    private val _orientationEmotionTracking = MutableLiveData<Boolean?>()
    val orientationEmotionTracking: LiveData<Boolean?>
        get() = _orientationEmotionTracking

    fun orientationEmotionTrackingCompleted() {
        _orientationEmotionTracking.value = null
    }

    fun deleteMood() {
        viewModelScope.launch {
            val emotion = database.get(emotionId) ?: return@launch
            database.del(emotion)

            _orientationEmotionTracking.value = true
        }
    }

    fun orientationClicked() {
        _orientationEmotionTracking.value = true
    }
}