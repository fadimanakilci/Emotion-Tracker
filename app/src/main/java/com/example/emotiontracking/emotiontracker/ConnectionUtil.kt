package com.example.emotiontracking.emotiontracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.emotiontracking.R
import com.example.emotiontracking.convertMoodToText
import com.example.emotiontracking.convertTimeToText
import com.example.emotiontracking.database.Mood

// Burada ekran ile veri bağlantısı öncekine(EmotionViewAdapter ilk kod) göre
// çok daha hızlı yapıyoruz. emotion_view_layout ekranındaki veri atamaları
// için duyguyu alıp bulunulan view e direkt atıyor. Bu da resim gibi büyük
// veriler için hız kazandırıyor.


@BindingAdapter("emotionTime")
fun TextView.emotionTimeAssignment(moods: Mood?) {
    moods?.let {
        text = convertTimeToText(
                moods.startTimeMilli,
                moods.endTimeMilli,
                context.resources
            )
    }
}

@BindingAdapter("mood")
fun TextView.moodAssignment(moods: Mood?) {
    moods?.let {
        text = convertMoodToText(moods.mood, context.resources)
    }

}

@BindingAdapter("emotionIcon")
fun ImageView.setSleepImage(moods: Mood?) {
    moods?.let {
        setImageResource(
            when (moods.mood) {
                0 -> R.drawable.angry
                1 -> R.drawable.crying
                2 -> R.drawable.flushed
                3 -> R.drawable.full
                4 -> R.drawable.screaming
                5 -> R.drawable.sleeping
                6 -> R.drawable.smiling
                7 -> R.drawable.thinking
                else -> R.drawable.smiling
            }
        )
    }
}