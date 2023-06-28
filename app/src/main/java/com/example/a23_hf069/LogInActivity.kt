package com.example.a23_hf069

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_hf069.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2

class LogInActivity : AppCompatActivity(){
    //
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabTextList = listOf("개인회원 로그인", "기업회원 로그인")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Set up the adapter
        binding.viewpager01.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tablayout01, binding.viewpager01) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }
}