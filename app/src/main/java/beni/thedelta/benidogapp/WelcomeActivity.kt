package beni.thedelta.benidogapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import beni.thedelta.benidogapp.databinding.ActivityWelcomeBinding
import beni.thedelta.benidogapp.welcome.WelcomeFragment

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(ActivityWelcomeBinding.inflate(layoutInflater).root)
        supportActionBar?.hide()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WelcomeFragment.newInstance())
                .commitNow()
        }
    }
}