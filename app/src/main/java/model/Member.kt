package model

import com.google.gson.annotations.SerializedName

data class Member (

        @SerializedName("id") val id : Int,
        @SerializedName("username") val username : String,
        @SerializedName("resized_avatar") val resized_avatar : String,
        @SerializedName("is_verified") val is_verified : Boolean,
        @SerializedName("is_business") val is_business : Boolean,
        @SerializedName("is_private") val is_private : Boolean
)