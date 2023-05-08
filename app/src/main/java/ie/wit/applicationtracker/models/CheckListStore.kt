package ie.wit.applicationtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface CheckListStore {
    fun findAllCheckList(userList:
                    MutableLiveData<List<ItemList>>
    )
    fun findAllCheckList(userid:String,
                    userList:
                    MutableLiveData<List<ItemList>>
    )
    fun findByCheckListId(userid:String,
                     donation: MutableLiveData<ItemList>
    )
    fun createCheckList(firebaseUser: MutableLiveData<FirebaseUser>, list: ItemList, application:String)
    fun deleteCheckList(userid:String)
    fun updateCheckList(userid:String, checklistid: String, list: ItemList)
}