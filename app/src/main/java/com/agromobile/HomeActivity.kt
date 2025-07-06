package com.agromobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import android.content.Intent
import android.view.MenuItem
import androidx.core.view.GravityCompat
import android.widget.TextView

class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var toolbar: Toolbar
    private var authToken: String = ""

    fun setAuthToken(token: String) {
        authToken = token
    }

    fun getAuthToken(): String = authToken

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu)

        // Recibir el token y datos del usuario desde el intent
        val token = intent.getStringExtra("AUTH_TOKEN") ?: ""
        setAuthToken(token)
        val userName = intent.getStringExtra("USER_NAME") ?: ""
        val userEmail = intent.getStringExtra("USER_EMAIL") ?: ""

        // Accede al header del NavigationView
        val headerView = navigationView.getHeaderView(0)
        val tvHeaderTitle = headerView.findViewById<TextView>(R.id.nav_header_title)
        val tvHeaderEmail = headerView.findViewById<TextView>(R.id.nav_header_email)
        tvHeaderTitle.text = userName
        tvHeaderEmail.text = userEmail

        // Fragmento inicial
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, CultivoFragment())
            .commit()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_cultivos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, CultivoFragment())
                        .commit()
                }
                R.id.nav_recomendaciones -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, RecomendacionFragment())
                        .commit()
                }
                R.id.nav_seguimiento -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, SeguimientoFragment())
                        .commit()
                }
                R.id.nav_precios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, PreciosFragment())
                        .commit()
                }
                R.id.nav_logout -> {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 