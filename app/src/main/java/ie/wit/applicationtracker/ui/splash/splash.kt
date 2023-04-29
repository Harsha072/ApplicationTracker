package ie.wit.applicationtracker.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.main.ApplicationTracker
import ie.wit.applicationtracker.ui.auth.Login

class splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            // Start the main activity after 2 seconds
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        // remove this activity from the stack
        finish()
        }, 2000) // Delay for 2 seconds (2000 milliseconds)

    }
}