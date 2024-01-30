package com.example.firebasetest.project3_android.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasetest.project3_android.R
import com.example.firebasetest.project3_android.databinding.FragmentMudAiBinding

class MudAiFragment : Fragment() {
    private lateinit var fragmentMudAiBinding: FragmentMudAiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMudAiBinding = FragmentMudAiBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mud_ai, container, false)
    }


}