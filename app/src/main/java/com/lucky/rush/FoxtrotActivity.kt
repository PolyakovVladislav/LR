package com.lucky.rush

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lucky.rush.ui.utils.Data
import com.lucky.rush.databinding.ActivityMainBinding
import com.lucky.rush.ui.extensions.safeNavigate
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class FoxtrotActivity : AppCompatActivity() {

    private val navigationController: NavController
        get() = vb.fragmentContainer.getFragment<NavHostFragment>().navController
    private lateinit var vb: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        if (Data.getInstance(this).isUserSigned) {
            navigationController.popBackStack()
            navigationController.safeNavigate(R.id.mainFragment)
        }
    }
}