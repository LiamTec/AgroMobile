package com.agromobile

import android.app.Application
import com.agromobile.data.SessionManager

class AgroMobileApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar SessionManager
        SessionManager.init(this)
    }
} 