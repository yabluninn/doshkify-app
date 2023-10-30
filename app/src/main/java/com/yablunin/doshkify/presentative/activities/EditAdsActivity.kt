package com.yablunin.doshkify.presentative.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yablunin.doshkify.R
import com.yablunin.doshkify.databinding.ActivityEditAdsBinding

class EditAdsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditAdsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}