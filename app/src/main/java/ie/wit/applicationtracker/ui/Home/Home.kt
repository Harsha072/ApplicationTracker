package ie.wit.applicationtracker.ui.Home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.HomeBinding
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.ui.auth.Login
import timber.log.Timber

class Home : AppCompatActivity() {
    private lateinit var homeBinding: HomeBinding
    private lateinit var loggedInViewModel: LoggedInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        homeBinding.button.setOnClickListener{
             signOut()
        }

    }
    fun signOut() {
        Timber.i("calling sign out ")
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
//            if (firebaseUser != null)
//                updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)
  }
       )

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })

    }
}