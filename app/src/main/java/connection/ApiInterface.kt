package connection

import model.MainModel
import retrofit2.Call
import retrofit2.http.GET


interface ApiInterface {


    @GET("detail/")
    fun getData(): Call<MainModel>
}