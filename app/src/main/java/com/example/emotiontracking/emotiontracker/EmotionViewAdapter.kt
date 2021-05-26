package com.example.emotiontracking.emotiontracker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.emotiontracking.R
import com.example.emotiontracking.convertMoodToText
import com.example.emotiontracking.convertTimeToText
import com.example.emotiontracking.database.Mood
import com.example.emotiontracking.databinding.EmotionViewLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
class EmotionViewAdapter: RecyclerView.Adapter<EmotionViewAdapter.ViewGenerator>(){
    var data = listOf<Mood>()
        set(value) {
            field = value
            // eleman eklendiğinde datasette değişiklik olduğu için ekranı yeniler
            notifyDataSetChanged()
        }

    // Her bir görüntü için bir görüntü tutucu oluşturulacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewGenerator {
        return ViewGenerator.from(parent)
    }

    // Görüntü tutucuya tutması gereken veri kaçıncı eleman olacağı ile beraber verilir
    override fun onBindViewHolder(holder: ViewGenerator, position: Int) {
        val element = data[position]
        holder.connect(element)
    }

    override fun getItemCount(): Int = data.size

    class ViewGenerator private constructor(elementView: View): RecyclerView.ViewHolder(elementView){
        val emotionTime: TextView = itemView.findViewById(R.id.emotion_time)
        val feeling: TextView = itemView.findViewById(R.id.feeling)
        val emotionImage: ImageView = itemView.findViewById(R.id.emotion_image)

        fun connect(element: Mood){
            val resource = itemView.context.resources
            emotionTime.text = convertTimeToText(element.startTimeMilli, element.endTimeMilli, resource)
            feeling.text = convertMoodToText(element.mood, resource)

            emotionImage.setImageResource(
                    when (element.mood){
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

        companion object{
            fun from(parent: ViewGroup): ViewGenerator{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.emotion_view_layout, parent, false)
                return  ViewGenerator(view)
            }
        }
    }
}

Daha düzenli bir hale getirip aşağıdaki kodları yazdık. Kodlarımız daha basit ve daha verimli bir hal aldı.
Yeni kodlar için connectionUtil dosyasını açtık
*/


// Başlık ekleme durumu için 2 değişken oluşturduk
private val HEADER_ITEM = 0
private val EMOTION_ITEM = 1
private val adapterScope = CoroutineScope(Dispatchers.Default)
/*
      Başlık ve emoji olma durumu olduğu için buradaki Mood
      olan değişkenleri DataElement ile değiştiriyoruz

class EmotionViewDifferenceFeedback: DiffUtil.ItemCallback<Mood>(){
    override fun areItemsTheSame(oldItem: Mood, newItem: Mood): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Mood, newItem: Mood): Boolean {
        return oldItem == newItem
    }
} */
class EmotionViewDiffCallback: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class ClickTracker(val clickTracker: (emotionId: Long) ->  Unit){
    fun clicked(emotion: Mood) = clickTracker(emotion.id)
}

// Başlık ve emojiyi ayırmak için gerekli class
sealed class DataItem {
    abstract val id: Long

    data class MoodItem(val mood: Mood) : DataItem() {
        override val id = mood.id
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}
// Başlığı oluşturacak sınıf
class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): HeaderViewHolder {

            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.text_element_view, parent, false)

            return HeaderViewHolder(view)
        }
    }
}



class EmotionViewAdapter(val clickTracker: ClickTracker):
    ListAdapter<DataItem, RecyclerView.ViewHolder>(EmotionViewDiffCallback())
{
    // Her bir görüntü için bir görüntü tutucu oluşturulacak
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        //return ViewBuilder.from(parent)

        // Başlık ve emoji olma durumu olduğu için return u ve yukarıdaki ": RecyclerView.ViewHolder {"
        // kısmını değiştirdik. Öceki hali ": ViewBuilder {" dı
        return when (viewType) {
            HEADER_ITEM -> HeaderViewHolder.from(parent)
            EMOTION_ITEM -> ViewBuilder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    // Görüntü tutucuya tutması gereken veri kaçıncı eleman olacağı ile beraber verilir
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // "holder : ViewBuilder" olan kısmı "holder: RecyclerView.ViewHolder" ile değiştirdik
        /*val element = getItem(position)
        holder.connect(element, clickTracker)*/

        // Başlık ve emoji olma durumu olduğu için yukarıdaki kodu aşağıdaki ile değiştirdik
        when (holder) {
            is ViewBuilder -> {
                val item = getItem(position) as DataItem.MoodItem
                holder.connect(item.mood, clickTracker)
            }
        }
    }


    // Aşağıdaki 2 fonksiyon da başlık ve emoji olma durumu için eklendi
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> HEADER_ITEM
            is DataItem.MoodItem -> EMOTION_ITEM
            //else -> EMOTION_ITEM
        }
    }
    fun addHeaderAndSubmitList(list: List<Mood>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.MoodItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }



    class ViewBuilder private constructor(private val connection: EmotionViewLayoutBinding) :
        RecyclerView.ViewHolder(connection.root){
        // emotion_view_layout dosyasında ki data ile bağladık
        fun connect(element: Mood, clickTracker: ClickTracker){
            connection.emotionV = element
            connection.executePendingBindings()
            connection.clickTracking = clickTracker
        }

        companion object {
            fun from(parent: ViewGroup): ViewBuilder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val connection = EmotionViewLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewBuilder(connection)
            }
        }

    }
}



