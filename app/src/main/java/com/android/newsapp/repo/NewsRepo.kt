package com.android.newsapp.repo

import com.android.newsapp.data.api.RetrofitInstance
import com.android.newsapp.data.db.ArticlesDatabase
import com.android.newsapp.model.Article

class NewsRepo(val db:ArticlesDatabase)
{

    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun getSearchingNews(countryCode:String,pageNumber:Int)=RetrofitInstance.api.searchForNews(countryCode,pageNumber)

    fun insert(article:Article)=db.getArticleDao().insert(article)
     fun delete(article:Article)=db.getArticleDao().deleteArticles(article)
    fun getAllArticles()=db.getArticleDao().getAllArticles()
}