package com.example.emotiontracking.emotiontracker

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.emotiontracking.R
import com.example.emotiontracking.database.MoodDatabase
import com.example.emotiontracking.databinding.EmotionTrackingFragmentBinding
import com.google.android.material.snackbar.Snackbar
import java.text.FieldPosition

class EmotionTrackingFragment : Fragment() {

    //private lateinit var viewModel: EmotionTrackingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding: EmotionTrackingFragmentBinding = DataBindingUtil.inflate(inflater,
            R.layout.emotion_tracking_fragment, container, false)

        val app = requireNotNull(this.activity).application
        val dataSource = MoodDatabase.getSample(app).databaseAccessObject
        val viewModelFactory = EmotionTrackingViewModelFactory(dataSource, app)

        val emotionTrackingViewModel = ViewModelProvider(this, viewModelFactory).get(EmotionTrackingViewModel::class.java)

        dataBinding.setLifecycleOwner(this)
        // emotion_tracking_fragment dosyasında ki data tagının içerisindeki değişkene atıyoruz
        dataBinding.emotionTrackingViewModel = emotionTrackingViewModel

        emotionTrackingViewModel.orientationMood.observe(viewLifecycleOwner, {emotion -> emotion?.let {
            this.findNavController().navigate(
                EmotionTrackingFragmentDirections.actionEmotionTrackingToMoodFragment2(emotion.id)
            )
            emotionTrackingViewModel.orientationCompleted()
        }})

        emotionTrackingViewModel.showSnackbar.observe(viewLifecycleOwner, {
            if (it == true){
                Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.deleted), Snackbar.LENGTH_SHORT).show()
                emotionTrackingViewModel.snackbarShown()
                // Alternatif olarak toast mesajıda gösterilebilir.
                //Toast.makeText(context, getString(R.string.toast), Toast.LENGTH_LONG).show()
            }
        })

        emotionTrackingViewModel.orientationEmotionDetail.observe(viewLifecycleOwner, { emotionId -> emotionId?.let {
            this.findNavController().navigate(
                EmotionTrackingFragmentDirections.actionEmotionTrackingToEmotionDetailFragment(emotionId)
            )
            emotionTrackingViewModel.orientationCompletedDetail()
        }})


        // emotion_tracking_fragment içerisindeki RecyleView ile EmotionViewAdapter arasında bağlantı kurar
        val adapter = EmotionViewAdapter(ClickTracker { id ->
            //Toast.makeText(context, "$id", Toast.LENGTH_LONG).show()
            emotionTrackingViewModel.moodClicked(id)
        })
        dataBinding.emotionList.adapter = adapter

        emotionTrackingViewModel.emotions.observe(viewLifecycleOwner, {
            it?.let {
                adapter.addHeaderAndSubmitList(it)
            }
        })

        // Eğer buradaki manager ı değiştirmemiş olsaydık yan yana 1den fazla eleman göstermek
        // istediğimizde başlığı da bir eleman olarak alıp diğerleri ile birlikte gösterecekti.
        // Fakat biz başlığı her durumda tek satırda görmek istiyoruz
        // Tabi sadece başlık değil birden fazla farklı tipte şeyler de ekleyebiliriz

        //val manager = GridLayoutManager(activity, 3, GridLayoutManager.VERTICAL, false)
        val manager = GridLayoutManager(activity, 3)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            // when (position % 4) dersek eğer bir satıra 3 eleman atarken diğer
            // satır tek eleman olacak şekilde sıralar.
            override fun getSpanSize(position: Int) = when (position) {
                // Başlık için 3 sütunu birleştirdik
                0 -> 3
                else -> 1
            }
        }
        dataBinding.emotionList.layoutManager = manager


        return dataBinding.root
    }

}