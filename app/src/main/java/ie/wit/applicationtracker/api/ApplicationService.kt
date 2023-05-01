package ie.wit.applicationtracker.api


import ie.wit.applicationtracker.models.ApplicationModel
import retrofit2.Call
import retrofit2.http.*

interface ApplicationService {
    @GET("/applications")
    fun findall(): Call<List<ApplicationModel>>

    @GET("/applications/{email}")
    fun findall(@Path("email") email: String?)
            : Call<List<ApplicationModel>>

    @GET("/applications/{email}/{id}")
    fun get(@Path("email") email: String?,
            @Path("id") id: String): Call<ApplicationModel>

    @DELETE("/applications/{email}/{id}")
    fun delete(@Path("email") email: String?,
               @Path("id") id: String): Call<ApplicationWrapper>

    @POST("/applications/{email}")
    fun post(@Path("email") email: String?,
             @Body application: ApplicationModel)
            : Call<ApplicationWrapper>

    @PUT("/applications/{email}/{id}")
    fun put(@Path("email") email: String?,
            @Path("id") id: String,
            @Body application: ApplicationModel
    ): Call<ApplicationWrapper>
}
