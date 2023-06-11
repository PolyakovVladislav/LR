package com.lucky.rush

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lucky.rush.ui.utils.Data
import com.lucky.rush.databinding.ActivityMainBinding
import com.lucky.rush.ui.extensions.navigateSafe
import io.michaelrocks.paranoid.Obfuscate

@Obfuscate
class FoxtrotActivity : AppCompatActivity() {

    private val navController: NavController
        get() = binding.fragmentContainer.getFragment<NavHostFragment>().navController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Data.getInstance(this).isUserSigned) {
            navController.popBackStack()
            navController.navigateSafe(R.id.mainFragment)
        }
    }
}