package com.android.newsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.newsapp.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(article: Article):Long

    @Query("SELECT * FROM articles")
    fun getAllArticles():LiveData<List<Article>>

    @Delete()
    fun deleteArticles(article: Article)

}