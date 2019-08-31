package connection

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    var BASE_URL:String="https://dev.beconapp.com/api/posts/task-posts/"
    var DYNAMIC_TOKEN:String="Token 275fd2fb454cf36ea39622aad2878a8fd0c550c4"
    val getClient: ApiInterface
        get() {

            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", DYNAMIC_TOKEN)
                .build()
                chain.proceed(newRequest)
            }
                  .build()



         val retrofit = Retrofit.Builder()
                 .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                 .baseUrl(BASE_URL)
                    .build()

            return retrofit.create(ApiInterface::class.java)

        }

}