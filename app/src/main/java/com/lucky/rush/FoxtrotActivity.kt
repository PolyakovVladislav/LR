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
    private val data = Data.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        if (data.isUserSigned) {
            navigationController.popBackStack()
            navigationController.safeNavigate(R.id.mainFragment)
        } else {
            data.total = 5000
            data.wingGameFirst = 0
            data.wingGameSecond = 0
            data.wingGameThird = 0
        }
    }
}
