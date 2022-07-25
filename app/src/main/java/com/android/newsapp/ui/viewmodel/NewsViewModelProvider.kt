package com.android.newsapp.ui.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.newsapp.repo.NewsRepo

class NewsViewModelProvider(val repo: NewsRepo) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(repo) as T
    }

}
