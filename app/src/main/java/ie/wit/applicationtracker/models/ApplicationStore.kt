package ie.wit.applicationtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface ApplicationStore {
    fun findAll(donationsList:
                MutableLiveData<List<ApplicationModel>>)
    fun findAll(userid:String,
                donationsList:
                MutableLiveData<List<ApplicationModel>>)
    fun findById(userid:String, donationid: String,
                 donation: MutableLiveData<ApplicationModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, donation: ApplicationModel)
    fun delete(userid:String, donationid: String)
    fun update(userid:String, donationid: String, donation: ApplicationModel)
}
