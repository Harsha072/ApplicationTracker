package ie.wit.applicationtracker.ui.checklist

import FirebaseDBManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.models.ApplicationModel
import ie.wit.applicationtracker.models.ItemList
import timber.log.Timber

class ChecklistViewModel : ViewModel() {
    private val checkList = MutableLiveData<ItemList>()

    var observableList: LiveData<ItemList>
        get() = checkList
        set(value) {
            checkList.value = value.value
        }
    fun getCheckList(application: String) {
        try {
            FirebaseDBManager.findByCheckListId(application,checkList)
            Timber.i("Detail getCheckList() Success : ${checkList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Detail getCheckList() Error : $e.message")
        }
    }
    fun saveChecklist(firebaseUser: MutableLiveData<FirebaseUser>,itemList: ItemList,applicationid:String) {
         Timber.i("got list "+itemList)
        FirebaseDBManager.createCheckList(firebaseUser,itemList,applicationid)
    }
}