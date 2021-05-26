package com.example.emotiontracking.emotiontracker

import android.app.Application
import androidx.lifecycle.*
//import com.example.emotiontracking.convertEmotionToHtml
import com.example.emotiontracking.database.Mood
import com.example.emotiontracking.database.MoodDatabaseDao
import kotlinx.coroutines.launch

class EmotionTrackingViewModel(
        val database: MoodDatabaseDao,
        application: Application
) : AndroidViewModel(application) {
    private var latestEmotion = MutableLiveData<Mood?>()
    val emotions = database.getAllMood()

    /* RecyleView kullandığımız için buraya gerekyok
    ScrollView tüm verileri tek bir string olarak tutuyor. Bu yüzden de kullanımı zor oluyor.
    RecyleView ise verileri ayrı ve sıralı olarak tutuyor. Tıpkı listView gibi. Fakat diğer ikisine göre
    düzen ve kullanış olarak avantajları çok daha fazla

    val emotionsString = Transformations.map(emotions){emotions ->
        convertEmotionToHtml(emotions, application.resources)
    }*/

    // MoodFragment e geçiş
    private val _orientationMood = MutableLiveData<Mood?>()
    val orientationMood: LiveData<Mood?>
        get() = _orientationMood

    fun orientationCompleted(){
        _orientationMood.value = null
    }

    private val _orientationEmotionDetail = MutableLiveData<Long?>()
    val orientationEmotionDetail
        get() = _orientationEmotionDetail

    fun orientationCompletedDetail(){
        _orientationEmotionDetail.value = null
    }

    // Snackbar gösterilmesi
    private var _showSnackbar = MutableLiveData<Boolean>()
    val showSnackbar: LiveData<Boolean>
        get() = _showSnackbar

    fun snackbarShown(){
        _showSnackbar.value = false
    }

    private suspend fun getTheLatestMoodFromTheDatabase(): Mood? {
        var emotion = database.getLatestMood()

        if (emotion?.startTimeMilli != emotion?.endTimeMilli)
            emotion = null
        return emotion
    }

    private fun LatestEmotion(){
        viewModelScope.launch {
            latestEmotion.value = getTheLatestMoodFromTheDatabase()
        }
    }

    init {
        LatestEmotion()
    }

    fun  startEmotionTracking(){
        viewModelScope.launch {
            val newEmotion = Mood()
            latestEmotion.value = newEmotion
            database.insert(newEmotion)
        }
    }

    fun stopEmotionTracking(){
        viewModelScope.launch {
            val lateEmotion = database.getLatestMood() ?: return@launch
            lateEmotion.endTimeMilli = System.currentTimeMillis()
            database.update(lateEmotion)
            _orientationMood.value = lateEmotion
        }
    }

    fun deleteAllData(){
        viewModelScope.launch {
            database.clear()
            latestEmotion.value = null
            _showSnackbar.value = true
        }
    }

    val displayTheStartButton = Transformations.map(latestEmotion){
        it == null
    }
    val displayTheStopButton = Transformations.map(latestEmotion){
        it != null
    }
    val displayTheClearButton = Transformations.map(emotions){
        it?.isNotEmpty()
    }
    fun moodClicked(id: Long) {
        _orientationEmotionDetail.value = id
    }

}

