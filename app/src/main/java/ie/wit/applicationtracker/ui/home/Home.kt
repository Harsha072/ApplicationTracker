package ie.wit.applicationtracker.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import ie.wit.applicationtracker.R
import ie.wit.applicationtracker.databinding.HomeBinding
import ie.wit.applicationtracker.databinding.NavHeaderBinding
import ie.wit.applicationtracker.ui.auth.LoggedInViewModel
import ie.wit.applicationtracker.ui.auth.Login
import timber.log.Timber

class Home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var headerView : View
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var homeBinding: HomeBinding
    private lateinit var loggedInViewModel: LoggedInViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.addApplicationFragment, R.id.applicationListFragment, R.id.aboutFragment,R.id.nav_sign_out,R.id.profileFragment),drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        val navView = homeBinding.navView
        navView.setupWithNavController(navController)
        initNavHeader()
        homeBinding.navView.setNavigationItemSelectedListener { menuItem ->
            Timber.i("clicked::::: ")
            when (menuItem.itemId) {
                R.id.nav_sign_out -> {
                    signOut()
                    true
                }
                else -> {
                    NavigationUI.onNavDestinationSelected(menuItem, navController)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
            }
        }
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)


    }
    fun signOut() {
        Timber.i("calling sign out ")
        loggedInViewModel.logOut()
        //Launch Login activity and clear the back stack to stop navigating back to the Home activity
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
//        navHeaderBinding.navHeaderImage.setOnClickListener {
//            showImagePicker(intentLauncher)
//            Toast.makeText(this,"Click To Change Image", Toast.LENGTH_SHORT).show()
//        }

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
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}