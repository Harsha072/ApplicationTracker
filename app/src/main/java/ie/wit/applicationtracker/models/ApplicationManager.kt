package ie.wit.applicationtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.api.ApplicationClient
import ie.wit.applicationtracker.api.ApplicationWrapper




import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

object ApplicationManager : ApplicationStore {

    private var donations = ArrayList<ApplicationModel>()
    override fun findAll(donationsList: MutableLiveData<List<ApplicationModel>>) {

    }

    override fun findAll(email: String, donationsList: MutableLiveData<List<ApplicationModel>>) {

        val call = ApplicationClient.getApi().findall(email)

        call.enqueue(object : Callback<List<ApplicationModel>> {
            override fun onResponse(call: Call<List<ApplicationModel>>,
                                    response: Response<List<ApplicationModel>>
            ) {
                donationsList.value = response.body() as ArrayList<ApplicationModel>
                Timber.i("Retrofit JSON finall = ${response.body()}")
            }

            override fun onFailure(call: Call<List<ApplicationModel>>, t: Throwable) {
                Timber.i("Retrofit Error : $t.message")
            }
        })
    }

    override fun update(email: String,id: String, application: ApplicationModel) {

        val call = ApplicationClient.getApi().put(email,id,application)

        call.enqueue(object : Callback<ApplicationWrapper> {
            override fun onResponse(call: Call<ApplicationWrapper>,
                                    response: Response<ApplicationWrapper>
            ) {
                val applicationWrapper = response.body()
                if (applicationWrapper != null) {
                    Timber.i("Retrofit Update ${applicationWrapper.message}")
                    Timber.i("Retrofit Update ${applicationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<ApplicationWrapper>, t: Throwable) {
                Timber.i("Retrofit Update Error : $t.message")
            }
        })
    }

    override fun findById(email: String, id: String, application: MutableLiveData<ApplicationModel>)   {

        val call = ApplicationClient.getApi().get(email,id)

        call.enqueue(object : Callback<ApplicationModel> {
            override fun onResponse(call: Call<ApplicationModel>, response: Response<ApplicationModel>) {
                application.value = response.body() as ApplicationModel
                Timber.i("Retrofit findById() = ${response.body()}")
                Timber.i("application valuefind by id::: "+application.value)
            }

            override fun onFailure(call: Call<ApplicationModel>, t: Throwable) {
                Timber.i("Retrofit findById() Error : $t.message")
            }
        })
    }


    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, application: ApplicationModel) {

        val call = ApplicationClient.getApi().post(application.email,application)


        call.enqueue(object : Callback<ApplicationWrapper> {
            override fun onResponse(call: Call<ApplicationWrapper>,
                                    response: Response<ApplicationWrapper>
            ) {
                val applicationWrapper = response.body()
                if (applicationWrapper != null) {
                    Timber.i("Retrofit ${applicationWrapper.message}")
                    Timber.i("Retrofit ${applicationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<ApplicationWrapper>, t: Throwable) {
                        Timber.i("Retrofit Error : $t.message")
            }
        })
    }

    override fun delete(email:String,id: String) {
        val call = ApplicationClient.getApi().delete(email,id)

        call.enqueue(object : Callback<ApplicationWrapper> {
            override fun onResponse(call: Call<ApplicationWrapper>,
                                    response: Response<ApplicationWrapper>
            ) {
                val applicationWrapper = response.body()
                if (applicationWrapper != null) {
                    Timber.i("Retrofit Delete ${applicationWrapper.message}")
                    Timber.i("Retrofit Delete ${applicationWrapper.data.toString()}")
                }
            }

            override fun onFailure(call: Call<ApplicationWrapper>, t: Throwable) {
                Timber.i("Retrofit Delete Error : $t.message")
            }
        })
    }
}