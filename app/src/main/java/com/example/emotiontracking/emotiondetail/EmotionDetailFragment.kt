package com.example.emotiontracking.emotiondetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.emotiontracking.R
import com.example.emotiontracking.database.MoodDatabase
import com.example.emotiontracking.databinding.EmotionDetailFragmentBinding

class EmotionDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBind: EmotionDetailFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.emotion_detail_fragment, container, false
        )

        val arguments = EmotionDetailFragmentArgs.fromBundle(requireArguments())
        val app = requireNotNull(this.activity).application

        val dataSource = MoodDatabase.getSample(app).databaseAccessObject

        val emotionDetailViewModelFactory = EmotionDetailViewModelFactory(arguments.emotionKey, dataSource)

        val emotionDetailViewModel = ViewModelProvider(this, emotionDetailViewModelFactory).get(EmotionDetailViewModel::class.java)

        dataBind.emotionDetailViewModel = emotionDetailViewModel
        dataBind.setLifecycleOwner(this)

        emotionDetailViewModel.orientationEmotionTracking.observe(viewLifecycleOwner, {
            if (it == true) {
                this.findNavController().navigate(EmotionDetailFragmentDirections.actionEmotionDetailFragmentToEmotionTracking())

                emotionDetailViewModel.orientationEmotionTrackingCompleted()
            }
        })

        return dataBind.root
    }

}