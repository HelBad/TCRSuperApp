package com.example.tcrsuperapp.view.admin.monitorsales

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import kotlinx.android.synthetic.main.admin_activity_monitorsales.*

class ActivityMonitorsales : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_monitorsales)

        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        backSales.setOnClickListener {
            startActivity(Intent(this@ActivityMonitorsales, ActivityBeranda::class.java))
            finish()
        }

        webSales.webViewClient = WebViewClient()
        webSales.loadUrl("https://tcrcorp.id/API/sfa/kunjungan.php")
        webSales.settings.blockNetworkLoads = false
        webSales.settings.javaScriptEnabled = true
        webSales.settings.setSupportZoom(true)
    }

    override fun onBackPressed() {}
}