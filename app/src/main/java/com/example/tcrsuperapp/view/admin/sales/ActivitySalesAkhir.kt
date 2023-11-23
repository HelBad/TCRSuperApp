package com.example.tcrsuperapp.view.admin.sales

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcrsuperapp.R
import kotlinx.android.synthetic.main.admin_activity_sales_akhir.*

class ActivitySalesAkhir : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_sales_akhir)

        btnAkhir.setOnClickListener {
            startActivity(Intent(this@ActivitySalesAkhir, ActivitySalesList::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySalesAkhir, ActivitySalesList::class.java))
        finish()
    }
}