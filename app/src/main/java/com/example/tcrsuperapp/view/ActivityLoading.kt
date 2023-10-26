package com.example.tcrsuperapp.view

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.tcrsuperapp.R

class ActivityLoading : AppCompatActivity() {
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        loading()
    }

    private fun loading() {
        val backgrond = object : Thread() {
            override fun run() {
                try {
                    sleep(2500)
                    if(SP.getString("level", "") == "Admin") {
                        startActivity(Intent(applicationContext,
                            com.example.tcrsuperapp.view.admin.ActivityBeranda::class.java))
                        finish()
                    } else if(SP.getString("level", "") == "Staff") {
                        startActivity(Intent(applicationContext,
                            com.example.tcrsuperapp.view.staff.ActivityBeranda::class.java))
                        finish()
                    } else if(SP.getString("level", "") == "Salesman") {
//                        startActivity(Intent(applicationContext, ActivityBeranda::class.java))
//                        finish()
                    } else {
                        startActivity(Intent(applicationContext, ActivityLogin::class.java))
                        finish()
                    }
                } catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        backgrond.start()
    }

    override fun onBackPressed() {}
}