package com.example.a23_hf069

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class PublicityBoardSearchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_publicity_board_search, container, false)

        val closeButton = rootView.findViewById<ImageButton>(R.id.backButton_search)
        closeButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return rootView
    }
}