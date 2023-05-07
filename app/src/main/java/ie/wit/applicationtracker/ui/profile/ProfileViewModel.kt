package ie.wit.applicationtracker.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.models.UserModel
import timber.log.Timber

class ProfileViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val status = MutableLiveData<Boolean>()
    val observableStatus: LiveData<Boolean>
        get() = status

    private val userValue = MutableLiveData<UserModel>()

    var observableUser: LiveData<UserModel>
        get() = userValue
        set(value) {
            userValue.value = value.value
        }

    fun addUser(firebaseUser: MutableLiveData<FirebaseUser>,
                       user: UserModel
    ) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.createUser(firebaseUser,user)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    fun getUser(userid: String) {
        try {
            FirebaseDBManager.findByUserId(userid, userValue)
            Timber.i("Detail getApplication() Success : ${userValue.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Detail getApplication() Error : $e.message")
        }
    }
}