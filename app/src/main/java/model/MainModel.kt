package model

import com.google.gson.annotations.SerializedName

 data class MainModel (


            @SerializedName("id") val id : Int,
            @SerializedName("description") val description : String,
            @SerializedName("member") val member : Member,
            @SerializedName("created_at") val created_at : String,
            @SerializedName ("post_medias") val post_medias : List<Post_medias>
)


