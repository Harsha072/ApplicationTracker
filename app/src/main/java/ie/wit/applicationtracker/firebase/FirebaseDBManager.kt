

//package ie.wit.applicationtracker.firebase
//
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.applicationtracker.models.*

import timber.log.Timber


object FirebaseDBManager : ApplicationStore,UserStore,CheckListStore {
    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(applicationList: MutableLiveData<List<ApplicationModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, applicationList: MutableLiveData<List<ApplicationModel>>) {

        database.child("user-application").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase application error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<ApplicationModel>()
                    val children = snapshot.children
                    children.forEach {
                        val application = it.getValue(ApplicationModel::class.java)
                        localList.add(application!!)
                    }
                    database.child("user-application").child(userid)
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

    override fun findAllUser(userList: MutableLiveData<List<UserModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAllUser(userid: String, userList: MutableLiveData<List<UserModel>>) {
        TODO("Not yet implemented")
    }

    override fun findByUserId(
        userid: String,
        User: MutableLiveData<UserModel>
    ) {
        database.child("user-details").child(userid)
           .get().addOnSuccessListener {
                Timber.i("firebase check user"+" "+userid)
                User.value = it.getValue(UserModel::class.java)
                Timber.i("firebase Got value user ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun createUser(firebaseUser: MutableLiveData<FirebaseUser>, user: UserModel) {
        val uid = firebaseUser.value!!.uid
        val key = database.child("user-details").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        user.uid = key
        val userValues = user.toMap()

        val childAdd = HashMap<String, Any>()

        childAdd["/user-details/$uid"] = userValues

        database.updateChildren(childAdd)
    }
    override fun createCheckList(firebaseUser: MutableLiveData<FirebaseUser>, list:ItemList, application: String) {
        Timber.i("calling creat:::::::")
        val uid = firebaseUser.value!!.uid
        val key = database.child("user-checklist").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }

        val listValues = list.toMap()
        Timber.i("calling "+listValues)
        Timber.i("calling creat:::::::"+application)
        val childAdd = HashMap<String, Any>()

        childAdd["/user-checklist/$application"] = listValues

        database.updateChildren(childAdd)
    }


    override fun deleteUser(userid: String) {
        TODO("Not yet implemented")
    }

    override fun updateUser(userid: String, donationid: String, user: UserModel) {
        TODO("Not yet implemented")
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
        childUpdate["user-application/$userid/$applicationid"] = applicationValues

        database.updateChildren(childUpdate)
    }

    override fun findAllCheckList(userList: MutableLiveData<List<ItemList>>) {
        TODO("Not yet implemented")
    }

    override fun findAllCheckList(userid: String, userList: MutableLiveData<List<ItemList>>) {
        TODO("Not yet implemented")
    }

    override fun findByCheckListId(application: String, checklist: MutableLiveData<ItemList>) {
        database.child("user-checklist").child(application)
           .get().addOnSuccessListener {
                checklist.value = it.getValue(ItemList::class.java)
                Timber.i("firebase Got item value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error item getting data $it")
            }
    }


    override fun deleteCheckList(userid: String) {
        TODO("Not yet implemented")
    }

    override fun updateCheckList(userid: String, checklistid: String, list: ItemList) {
        TODO("Not yet implemented")
    }

}
