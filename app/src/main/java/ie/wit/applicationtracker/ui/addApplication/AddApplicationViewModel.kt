package ie.wit.applicationtracker.ui.addApplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.models.ApplicationModel

class AddApplicationViewModel : ViewModel() {
    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addApplication(firebaseUser: MutableLiveData<FirebaseUser>,
                    application: ApplicationModel
    ) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,application)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}