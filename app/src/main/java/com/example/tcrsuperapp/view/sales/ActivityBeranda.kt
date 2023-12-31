package com.example.tcrsuperapp.view.sales

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiSales
import com.example.tcrsuperapp.view.ActivityLogin
import com.example.tcrsuperapp.view.sales.retur.ActivityReturList
import com.example.tcrsuperapp.view.sales.sales.ActivitySalesList
import com.example.tcrsuperapp.view.sales.sp.ActivitySpList
import com.example.tcrsuperapp.view.sales.absensi.ActivityAbsensiList
import com.example.tcrsuperapp.view.sales.customer.ActivityCustomer
import com.example.tcrsuperapp.view.sales.fbl.ActivityFbl
import com.example.tcrsuperapp.view.sales.galery.ActivityGalery
import com.example.tcrsuperapp.view.sales.masukan.ActivityMasukan
import com.example.tcrsuperapp.view.sales.omzet.ActivityOmzet
import com.example.tcrsuperapp.view.sales.stok.ActivityStok
import com.example.tcrsuperapp.view.sales.survey.ActivitySurveyList
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_beranda.*
import org.json.JSONObject

class ActivityBeranda : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    val PERM_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_beranda)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        checkAndRequestPermissions()
        getData()

        absenBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivityAbsensiList::class.java)
            intent.putExtra("approval", "Menunggu")
            startActivity(intent)
            finish()
        }
        stokBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityStok::class.java))
            finish()
        }
        custBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityCustomer::class.java))
            finish()
        }
        spBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivitySpList::class.java))
            finish()
        }
        fblBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityFbl::class.java))
            finish()
        }
        returBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityReturList::class.java))
            finish()
        }
        omzetBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityOmzet::class.java))
            finish()
        }
        produkBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivitySalesList::class.java)
            intent.putExtra("nama", namaBeranda.text.toString())
            startActivity(intent)
            finish()
        }
        surveyBeranda.setOnClickListener {
            val intent = Intent(this@ActivityBeranda, ActivitySurveyList::class.java)
            intent.putExtra("nama", namaBeranda.text.toString())
            startActivity(intent)
            finish()
        }
        galeryBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityGalery::class.java))
            finish()
        }
        masukanBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityMasukan::class.java))
            finish()
        }
        logoutBeranda.setOnClickListener {
            alertDialog.setTitle("Keluar Akun")
            alertDialog.setMessage("Apakah anda ingin keluar dari akun ini ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val editor = SP.edit()
                        editor.putString("id_pengguna", "")
                        editor.putString("username", "")
                        editor.putString("password", "")
                        editor.putString("level", "")
                        editor.apply()

                        startActivity(Intent(this@ActivityBeranda, ActivityLogin::class.java))
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

    private fun getData() {
        val fetchData = FetchData(ApiSales.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaBeranda.text = dataObject.getString("nama")
                    idBeranda.text = SP.getString("username", "").toString()
                } catch (t: Throwable) { }
            }
        }
    }

    private fun checkAndRequestPermissions(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded: MutableList<String> = ArrayList()

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)
        }
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listPermissionsNeeded.isNotEmpty()) {
            requestPermissions(listPermissionsNeeded.toTypedArray(), PERM_CODE)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERM_CODE) {
            if (grantResults.isNotEmpty()) {
                for (i in permissions.indices) {
                    if (permissions[i] == Manifest.permission.CAMERA) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    } else if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        } else {
                            Toast.makeText(this, "Koneksi Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?").setCancelable(false)
            .setNeutralButton("", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {}
            })
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}