package ie.wit.applicationtracker.ui.about

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ie.wit.applicationtracker.R
import mehdi.sakout.aboutpage.AboutPage

class aboutFragment : Fragment() {

    companion object {
        fun newInstance() = aboutFragment()
    }

    private lateinit var viewModel: AboutViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return AboutPage(context)
            .isRTL(false)
            // or Typeface
            .setImage(R.drawable.baseline_newspaper_24)
            .setDescription(getString(R.string.description))
            .addGroup("Connect with us")
            .addEmail("elmehdi.sakout@gmail.com")
            .addWebsite("https://mehdisakout.com/")
            .addFacebook("the.medy")
            .addTwitter("medyo80")
            .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
            .addPlayStore("com.ideashower.readitlater.pro")
            .addGitHub("medyo")
            .addInstagram("medyo80")
            .create();
    }








    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        // TODO: Use the ViewModel
    }

}