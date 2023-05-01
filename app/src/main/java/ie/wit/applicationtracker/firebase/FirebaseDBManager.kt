

//package ie.wit.applicationtracker.firebase
//
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.models.ApplicationStore

import timber.log.Timber


object FirebaseDBManager : ApplicationStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(applicationList: MutableLiveData<List<ApplicationModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, applicationList: MutableLiveData<List<ApplicationModel>>) {

        database.child("user-applications").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Donation error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ApplicationModel>()
                    val children = snapshot.children
                    children.forEach {
                        val application = it.getValue(ApplicationModel::class.java)
                        localList.add(application!!)
                    }
                    database.child("user-applications").child(userid)
                        .removeEventListener(this)

                    applicationList.value = localList
                }
            })
    }

    override fun findById(userid: String, applicationid: String, application: MutableLiveData<ApplicationModel>) {

        database.child("user-application").child(userid)
            .child(applicationid).get().addOnSuccessListener {
                application.value = it.getValue(ApplicationModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, application: ApplicationModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("application").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        application.uid = key
        val applicationValues = application.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/application/$key"] = applicationValues
        childAdd["/user-application/$uid/$key"] = applicationValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, applicationid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/application/$applicationid"] = null
        childDelete["/user-application/$userid/$applicationid"] = null

        database.updateChildren(childDelete)
    }




    override fun update(userid: String, applicationid: String, application: ApplicationModel) {

        val applicationValues = application.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["application/$applicationid"] = applicationValues
        childUpdate["user-donations/$userid/$applicationid"] = applicationValues

        database.updateChildren(childUpdate)
    }

}
