package com.lucky.rush

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import com.lucky.rush.ui.view.CustomProgressBar
import io.michaelrocks.paranoid.Obfuscate
import kotlinx.coroutines.*
import java.util.*

@Obfuscate
@SuppressLint("CustomSplashScreen")
class BravoActivity : AppCompatActivity() {
    private val uiScope = CoroutineScope(Dispatchers.Main + Job())
    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // TODO начиначем анимацию загрузки, если таковая имеется

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        toMainActivity()
    }

    override fun onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(true)
        }

        super.onDestroy()
    }

    private fun toMainActivity() {
        ioScope.launch {

            withContext(Dispatchers.Main) {
                val progressBar = findViewById<CustomProgressBar>(R.id.progress_bar)
                progressBar.clicksEnabled = false
                progressBar.progress = 0
                do {
                    delay(30)
                    progressBar.progress = progressBar.progress + 2
                } while (progressBar.progress < 100)
            }

            uiScope.launch {
                startActivity(Intent(this@BravoActivity, FoxtrotActivity::class.java))
                finish()
            }
        }
    }
}
