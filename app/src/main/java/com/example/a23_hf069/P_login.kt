package com.example.a23_hf069

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_hf069.databinding.FragmentPLoginBinding


class P_login : Fragment() {
    lateinit var binding: FragmentPLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPLoginBinding.inflate(inflater, container, false)

        return binding.root
    }
}