package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_hf069.databinding.FragmentCLoginBinding

class C_login : Fragment() {
    lateinit var binding: FragmentCLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCLoginBinding.inflate(inflater, container, false)

        return binding.root
    }
}