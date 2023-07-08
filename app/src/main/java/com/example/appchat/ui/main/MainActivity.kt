package com.example.appchat.ui.main

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appchat.R
import com.example.appchat.forceHideKeyboard
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    private lateinit var mainProgressBar: ProgressBar
    private lateinit var mainToolBar: Toolbar
    private lateinit var notification: BadgeDrawable
    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainToolBar = findViewById(R.id.main_toolbar)
        navView = findViewById(R.id.nav_view)
        mainProgressBar = findViewById(R.id.main_progress_bar)
        setSupportActionBar(mainToolBar)


        val navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.startFragment -> {
                    navView.visibility = View.GONE
                    mainToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.loginFragment -> {
                    navView.visibility = View.GONE
                    mainToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                }
                R.id.registerFragment -> {
                    navView.visibility = View.GONE
                    mainToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    mainToolBar.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200))
                }
            }
            showGlobalProgressBar(false)
            currentFocus?.rootView?.forceHideKeyboard()
        }

        //ẩn nút back toolbar
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.registerFragment,
                R.id.startFragment,

                )
        )
        //dong bo hóa navigationUI voi actionbar
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    fun showGlobalProgressBar(show: Boolean) {
        if (show) mainProgressBar.visibility = View.VISIBLE
        else mainProgressBar.visibility = View.GONE
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }


}