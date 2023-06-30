package com.example.a23_hf069

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_hf069.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.example.a23_hf069.databinding.ActivityLoginBinding

class LogInActivity : AppCompatActivity(){
    private val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val tabTextList = listOf("개인회원", "기업회원")
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