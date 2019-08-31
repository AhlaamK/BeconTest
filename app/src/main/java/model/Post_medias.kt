package model

import com.google.gson.annotations.SerializedName

data class Post_medias (

        @SerializedName("id") val id : Int,
        @SerializedName("media") val media : String,
        @SerializedName("is_cover") val is_cover : Boolean,
        @SerializedName("is_video") val is_video : Boolean,
        @SerializedName("cover_image") val cover_image : String,
        @SerializedName("video_length") val video_length : Double,
        @SerializedName("filter_name") val filter_name : String,
        @SerializedName("width") val width : Int,
        @SerializedName("height") val height : Int,
        @SerializedName("cover_width") val cover_width : Int,
        @SerializedName("cover_height") val cover_height : Int
)