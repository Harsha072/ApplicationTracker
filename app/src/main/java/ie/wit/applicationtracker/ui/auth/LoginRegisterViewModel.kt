package ie.wit.applicationtracker.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import ie.wit.applicationtracker.firebase.FirebaseAuthManager


class LoginRegisterViewModel (app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser

    fun login(email: String?, password: String?) {
        firebaseAuthManager.login(email, password)
    }
    fun authWithGoogle(acct: GoogleSignInAccount) {
        firebaseAuthManager.firebaseAuthWithGoogle(acct)
    }

    fun register(email: String?, password: String?) {
        firebaseAuthManager.register(email, password)
    }
}