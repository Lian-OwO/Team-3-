package com.example.firebasetest.project3_android.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasetest.project3_android.R
import com.example.firebasetest.project3_android.databinding.FragmentReserveResultBinding

class ReserveResultFragment : Fragment() {
    private lateinit var fragmentReserveResultBinding: FragmentReserveResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentReserveResultBinding = FragmentReserveResultBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve_result, container, false)
    }


}