package com.example.tcrsuperapp.view.staff.retur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcrsuperapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.staff_activity_retur_preview.*

class ActivityReturPreview : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_retur_preview)

        if(intent.getStringExtra("status").toString() == "BARANG") {
            Picasso.get().load(intent.getStringExtra("barang").toString()).into(imgPreview)
        } else {
            Picasso.get().load(intent.getStringExtra("nota").toString()).into(imgPreview)
        }

        backPreview.setOnClickListener {
            finish()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}