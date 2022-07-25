package com.android.newsapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.android.newsapp.R
import com.android.newsapp.data.db.ArticlesDatabase
import com.android.newsapp.databinding.ActivityMainBinding
import com.android.newsapp.databinding.FragmentBreakingNewsBinding
import com.android.newsapp.repo.NewsRepo
import com.android.newsapp.ui.viewmodel.NewsViewModel
import com.android.newsapp.ui.viewmodel.NewsViewModelProvider
import com.android.newsapp.util.Resource
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var newsViewModel: NewsViewModel
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frag_host_pro) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
        var repo = NewsRepo(ArticlesDatabase(this))
        val newsViewModelProvider = NewsViewModelProvider(repo)
        newsViewModel =
            ViewModelProvider(this, newsViewModelProvider).get(NewsViewModel::class.java)

    }
}