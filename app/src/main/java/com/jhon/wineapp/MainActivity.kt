package com.jhon.wineapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.jhon.wineapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var currentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)

        //setupBottomNav()
        val navController = findNavController(R.id.nav_host)
        binding.navView.setupWithNavController(navController)

    }

    private fun setupBottomNav() {

        val homeFragment = HomeFragment()
        val favouriteFragment = FavouriteFragment()

        currentFragment = homeFragment

        with(supportFragmentManager) {
            beginTransaction().add(R.id.nav_host, favouriteFragment).hide(favouriteFragment)
                .commit()

            beginTransaction().add(R.id.nav_host, homeFragment)
                .commit()

            binding.navView.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_home -> {
                        beginTransaction().hide(currentFragment).show(homeFragment).commit()
                        currentFragment = homeFragment
                    }

                    R.id.navigation_favourite -> {
                        beginTransaction().hide(currentFragment).show(favouriteFragment).commit()
                        currentFragment = favouriteFragment
                    }
                }
                true

            }
        }
    }

}