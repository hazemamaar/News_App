package com.android.newsapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.newsapp.model.Article

@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(Converts::class)
abstract class ArticlesDatabase :RoomDatabase(){

    abstract fun getArticleDao(): ArticleDao

    companion object{
        @Volatile
        private var instance:ArticlesDatabase ?=null
        private val LOCK =Any()
        operator fun invoke(context:Context)= instance?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance =it}
        }
        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext
            ,ArticlesDatabase::class.java
            ,"article_db.db").build()
    }
}