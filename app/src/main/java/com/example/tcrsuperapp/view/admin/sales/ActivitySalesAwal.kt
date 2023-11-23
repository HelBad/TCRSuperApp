package com.example.tcrsuperapp.view.admin.sales

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tcrsuperapp.R
import kotlinx.android.synthetic.main.admin_activity_sales_awal.*

class ActivitySalesAwal : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_sales_awal)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Sales", Context.MODE_PRIVATE)
        getData()

        tokoAwal.setOnClickListener {
            startActivity(Intent(this@ActivitySalesAwal, ActivitySalesCust::class.java))
        }
        btnAwal.setOnClickListener {
            if(tokoAwal.text.toString() == "" && namaAwal.text.toString() == "") {
                Toast.makeText(this@ActivitySalesAwal, "Lengkapi data", Toast.LENGTH_SHORT).show()
            } else {
                val editor = SP.edit()
                editor.putString("pic", namaAwal.text.toString())
                editor.apply()

                startActivity(Intent(this@ActivitySalesAwal, ActivitySales::class.java))
                finish()
            }
        }
    }

    private fun getData() {
        if(SP.getString("perusahaan", "").toString() == "null") {
            tokoAwal.setText("")
        } else {
            tokoAwal.setText(SP.getString("perusahaan", "").toString())
        }
    }

    override fun onBackPressed() {
        alertDialog.setMessage("Progress belum tersimpan, Apakah anda tetap ingin keluar halaman ?").setCancelable(false)
            .setNeutralButton("", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {}
            })
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    val editor = SP.edit()
                    editor.putString("kode", "")
                    editor.putString("perusahaan", "")
                    editor.putString("pic", "")
                    editor.apply()

                    startActivity(Intent(this@ActivitySalesAwal, ActivitySalesList::class.java))
                    finish()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}