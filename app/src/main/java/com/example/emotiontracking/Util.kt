package com.example.emotiontracking

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.emotiontracking.database.Mood
import java.lang.StringBuilder
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLISECONDS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLISECONDS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

fun convertTimeToText(startTimeMilli: Long, endTimeMilli: Long, resources: Resources): String{
    val millisecondTime = endTimeMilli - startTimeMilli
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(startTimeMilli)

    return when{
        millisecondTime < ONE_MINUTE_MILLISECONDS -> {
            val second = TimeUnit.SECONDS.convert(millisecondTime, TimeUnit.MILLISECONDS)
            resources.getString(R.string.second_length, second, dayOfWeek)
        }
        millisecondTime < ONE_HOUR_MILLISECONDS -> {
            val minute = TimeUnit.MINUTES.convert(millisecondTime, TimeUnit.SECONDS)
            resources.getString(R.string.minute_length, minute, dayOfWeek)
        }
        else -> {
            val hour = TimeUnit.HOURS.convert(millisecondTime, TimeUnit.MINUTES)
            resources.getString(R.string.hour_length, hour, dayOfWeek)
        }
    }
}

fun convertMoodToText(mood: Int, resources: Resources): String{
    var qualityString = resources.getString(R.string.how_do_you_feel)
    when(mood){
        -1 -> qualityString = "___"
        0  -> qualityString = resources.getString(R.string.angry)
        1  -> qualityString = resources.getString(R.string.tearful)
        2  -> qualityString = resources.getString(R.string.flushed)
        3  -> qualityString = resources.getString(R.string.full)
        4  -> qualityString = resources.getString(R.string.screaming_scared)
        5  -> qualityString = resources.getString(R.string.sleepy)
        6  -> qualityString = resources.getString(R.string.happy)
        7  -> qualityString = resources.getString(R.string.pensive)
    }
    return qualityString
}


@SuppressLint("SimpleDataFormat")
fun convertMillisecondsToDate(millisecond: Long): String{
    return SimpleDateFormat("EEEE dd-MM-yyyy' Time: 'HH:mm").format(millisecond).toString()
}

/*
fun convertNumericQualityToString(emotion: Int, resources: Resources): String{
    var qualityString = resources.getString(R.string.how_do_you_feel)
    when(emotion){
        -1 -> qualityString = "___"
        0  -> qualityString = resources.getString(R.string.angry)
        1  -> qualityString = resources.getString(R.string.tearful)
        2  -> qualityString = resources.getString(R.string.flushed)
        3  -> qualityString = resources.getString(R.string.full)
        4  -> qualityString = resources.getString(R.string.screaming_scared)
        5  -> qualityString = resources.getString(R.string.sleepy)
        6  -> qualityString = resources.getString(R.string.happy)
        7  -> qualityString = resources.getString(R.string.pensive)
    }
    return qualityString
}

// ScrollView ve HTML e çevirme durumu çok sağlıklı omadığı için RecyclerView kullandık ve artık bu
// fonksiyona gerek yok. Bunun yerine başa eklediğimiz convertTimeToText ve convertMoodToText
// fonksiyonlarını kullanıyoruz


fun convertEmotionToHtml(emotions: List<Mood>, resources: Resources): Spanned{
    val stringBuilder = StringBuilder()
    stringBuilder.apply {
        append(resources.getString(R.string.title))
        emotions.forEach {
            append("<br>")
            append(resources.getString(R.string.start_time))
            // it: foreach döngüsü içerisinde o an bulunan/işlenen duygu
            append("\t${convertMillisecondsToDate(it.startTimeMilli)}<br>")
            if (it.endTimeMilli != it.startTimeMilli){
                val  ms = it.endTimeMilli.minus(it.startTimeMilli)
                val sc = ms/1000
                val mn = sc/60
                val hr = mn/60

                append(resources.getString(R.string.end_time))
                append("\t${ convertMillisecondsToDate(it.endTimeMilli)}<br>")
                append(resources.getString(R.string.emotion))
                append("\t${convertNumericQualityToString(it.mood, resources)}<br>")
                append(resources.getString(R.string.emotion_time))
                // Hour
                append("\t${hr}:")
                // Minutes
                append("${mn % 60}:")
                // Seconds
                append("${sc % 60}<br><br>")
            }
        }
    }
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
        Html.fromHtml(stringBuilder.toString(), Html.FROM_HTML_MODE_LEGACY)
    }else{
        HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

}
*/

