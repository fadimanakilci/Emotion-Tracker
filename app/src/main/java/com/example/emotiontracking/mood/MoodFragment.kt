package com.example.emotiontracking.mood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.emotiontracking.R
import com.example.emotiontracking.database.MoodDatabase
import com.example.emotiontracking.databinding.FragmentMoodBinding
//import android.databinding.tool.util.GenerationalClassUtil.ExtensionFilter.BR



class MoodFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataLink: FragmentMoodBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_mood,
            container,
            false
        )

        val arguments = MoodFragmentArgs.fromBundle(requireArguments())
        val app = requireNotNull(this.activity).application

        val dataSource = MoodDatabase.getSample(app).databaseAccessObject
        val moodViewModelFactory = MoodViewModelFactory(arguments.emotionKey, dataSource)
        
        val moodViewModel = ViewModelProvider(this, moodViewModelFactory).get(MoodViewModel::class.java)
        dataLink.moodViewModel = moodViewModel

        moodViewModel.orientationEmotionTracking.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(MoodFragmentDirections.actionMoodFragmentToEmotionTracking())
                moodViewModel.orientationCompleted()
            }
        })


        return dataLink.root
    }
}

