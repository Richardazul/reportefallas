package com.lab.failurereport.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import com.lab.failurereport.R
import com.lab.failurereport.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            showFragment(HomeFragment())
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    binding.topBar.title = "Centro de Soporte"
                    showFragment(HomeFragment())
                    true
                }
                R.id.nav_tickets -> {
                    binding.topBar.title = "Tickets"
                    showFragment(TicketsFragment())
                    true
                }
                R.id.nav_history -> {
                    binding.topBar.title = "Historial de Tickets"
                    showFragment(HistoryFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun goToHistorySection() {
        binding.bottomNav.selectedItemId = R.id.nav_history
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
