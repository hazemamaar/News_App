package com.android.newsapp.data.db

import androidx.room.TypeConverter
import com.android.newsapp.model.Source

class Converts {

    @TypeConverter
    fun forSource(source: Source):String{
        return source.name
    }
    @TypeConverter
    fun toSource(name:String):Source{
        return Source(name,name)
    }
}