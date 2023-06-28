package com.example.a23_hf069

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_hf069.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class LogInActivity : AppCompatActivity(){
    //
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val tabTextList = listOf("Profile", "Search")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Set up the adapter
        binding.viewPager01.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout01, binding.viewPager01) { tab, pos ->
            tab.text = tabTextList[pos]
        }.attach()
    }
}