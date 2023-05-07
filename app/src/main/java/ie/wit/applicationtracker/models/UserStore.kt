package ie.wit.applicationtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface UserStore {
    fun findAllUser(userList:
                MutableLiveData<List<UserModel>>
    )
    fun findAllUser(userid:String,
                userList:
                MutableLiveData<List<UserModel>>
    )
    fun findByUserId(userid:String,
                 donation: MutableLiveData<UserModel>
    )
    fun createUser(firebaseUser: MutableLiveData<FirebaseUser>, user: UserModel)
    fun deleteUser(userid:String)
    fun updateUser(userid:String, donationid: String, user: UserModel)
}