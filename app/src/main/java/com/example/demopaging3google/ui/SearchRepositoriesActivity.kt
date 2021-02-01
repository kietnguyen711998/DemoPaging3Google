package com.example.demopaging3google.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demopaging3google.R
import com.example.demopaging3google.databinding.ActivitySearchRepositoriesBinding
import kotlinx.coroutines.Job

class SearchRepositoriesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchRepositoriesBinding
    private lateinit var viewModel: SearchRepositoriesViewModel
    private val adapter = ReposAdapter()

    private var searchJob: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_repositories)
    }


}