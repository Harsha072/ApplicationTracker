package ie.wit.applicationtracker.ui.applicationlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.models.ApplicationModel
import timber.log.Timber
import java.lang.Exception

class ApplicationListViewModel : ViewModel() {

    private val applicationsList =
        MutableLiveData<List<ApplicationModel>>()

    val observableDonationsList: LiveData<List<ApplicationModel>>
        get() = applicationsList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init { load() }

    fun load() {
        try {
            FirebaseDBManager.findAll(liveFirebaseUser.value?.uid!!, applicationsList)
            Timber.i("Report Load Success : ${applicationsList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}