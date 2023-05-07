package ie.wit.applicationtracker.ui.details

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.models.ApplicationModel
import timber.log.Timber

 public enum class ApplicationStatus {
    PENDING,
    ACCEPTED,
    REJECTED
}
class DetailsViewModel : ViewModel() {
    private val application = MutableLiveData<ApplicationModel>()

    var observableApplication: LiveData<ApplicationModel>
        get() = application
        set(value) {
            application.value = value.value
        }

    fun getApplication(userid: String, id: String) {
        try {
            FirebaseDBManager.findById(userid, id, application)
            Timber.i("Detail getApplication() Success : ${application.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Detail getApplication() Error : $e.message")
        }
    }
//    @BindingAdapter("app:checkedButton")
//    fun setCheckedButton(view: RadioGroup, status: ApplicationStatus?) {
//        when (status) {
//            ApplicationStatus.ACCEPTED -> view.check(R.id.statusAccepted)
//            ApplicationStatus.REJECTED -> view.check(R.id.statusRejected)
//            else -> view.check(R.id.statusPending)
//        }
//    }
    fun updateDonation(userid: String, id: String, application: ApplicationModel) {
        try {
            FirebaseDBManager.update(userid, id, application)
            Timber.i("Detail update() Success : $application")
        } catch (e: Exception) {
            Timber.i("Detail update() Error : $e.message")
        }
    }
}