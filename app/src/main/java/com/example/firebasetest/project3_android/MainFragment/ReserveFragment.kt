package com.example.firebasetest.project3_android.MainFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.firebasetest.project3_android.R
import com.example.firebasetest.project3_android.databinding.FragmentReserveBinding

class ReserveFragment : Fragment() {
    private lateinit var fragmentReserveBinding: FragmentReserveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentReserveBinding = FragmentReserveBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reserve, container, false)
    }


}