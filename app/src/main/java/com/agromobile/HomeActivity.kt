package com.agromobile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.agromobile.data.SessionManager
import com.agromobile.databinding.ActivityHomeBinding
import com.agromobile.ui.fragments.CultivoFragment
import com.agromobile.ui.fragments.PreciosFragment
import com.agromobile.ui.fragments.RecomendacionFragment
import com.agromobile.ui.fragments.SeguimientoFragment
import com.agromobile.ui.viewmodels.AuthViewModel
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityHomeBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupUserInfo()
        
        // Cargar el fragmento por defecto
        loadFragment(CultivoFragment(), "Cultivos")
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "AgroMobile"
    }

    private fun setupNavigationDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupUserInfo() {
        val user = SessionManager.getUserInfo()
        user?.let { userInfo ->
            // Actualizar el header del navigation drawer con la información del usuario
            val headerView = binding.navView.getHeaderView(0)
            // Aquí podrías actualizar los TextView del header si los tienes
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_cultivos -> {
                loadFragment(CultivoFragment(), "Cultivos")
            }
            R.id.nav_precios -> {
                loadFragment(PreciosFragment(), "Precios")
            }
            R.id.nav_recomendaciones -> {
                loadFragment(RecomendacionFragment(), "Recomendaciones")
            }
            R.id.nav_seguimiento -> {
                loadFragment(SeguimientoFragment(), "Seguimiento")
            }
            R.id.nav_logout -> {
                logout()
            }
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        
        supportActionBar?.title = title
    }

    private fun logout() {
        authViewModel.logout()
        
        // Regresar a MainActivity
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Limpiar recursos si es necesario
    }
} 