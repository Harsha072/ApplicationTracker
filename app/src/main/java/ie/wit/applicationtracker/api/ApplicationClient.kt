package ie.wit.applicationtracker.api

import com.google.gson.GsonBuilder
import ie.wit.applicationtracker.api.ApplicationService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApplicationClient {

    val serviceURL = "https://donationx-multi-web-server.onrender.com"



    fun getApi() : ApplicationService {

            val gson = GsonBuilder().create()

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()

            val apiInterface = Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return apiInterface.create(ApplicationService::class.java)
        }
    }
