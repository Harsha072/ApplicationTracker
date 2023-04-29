package ie.wit.applicationtracker.main

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.wit.applicationtracker.R
import timber.log.Timber

class ApplicationTracker : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        //  donationsStore = DonationManager()
        Timber.i("ApplicationTracker::::  Started")
    }
}