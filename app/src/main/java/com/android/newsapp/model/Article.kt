package com.android.newsapp.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Entity(
    tableName = "articles"
)

data class Article(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String
):Parcelable {
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {

    }
}