package com.example.tcrsuperapp.view.admin

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
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.view.ActivityLogin
import com.example.tcrsuperapp.view.admin.absensi.ActivityAbsensiList
import com.example.tcrsuperapp.view.admin.monitorsales.ActivityMonitorsales
import com.example.tcrsuperapp.view.admin.bukuharian.ActivityBukuharian
import com.example.tcrsuperapp.view.admin.customer.ActivityCustomer
import com.example.tcrsuperapp.view.admin.fbl.ActivityFbl
import com.example.tcrsuperapp.view.admin.galery.ActivityGalery
import com.example.tcrsuperapp.view.admin.masukan.ActivityMasukan
import com.example.tcrsuperapp.view.admin.omzet.ActivityOmzet
import com.example.tcrsuperapp.view.admin.stok.ActivityStok
import com.example.tcrsuperapp.view.admin.survey.ActivitySurveyList
import com.example.tcrsuperapp.view.admin.sp.ActivitySpList
import com.example.tcrsuperapp.view.admin.retur.ActivityReturList
import com.example.tcrsuperapp.view.admin.izin.ActivityIzinList
import com.example.tcrsuperapp.view.admin.sales.ActivitySalesList
import com.example.tcrsuperapp.view.admin.sales.ActivitySalesTarget
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_beranda.*
import org.json.JSONObject

class ActivityBeranda : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    val PERM_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_beranda)

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
        izinBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityIzinList::class.java))
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
        harianBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityBukuharian::class.java))
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
        salesBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityMonitorsales::class.java))
            finish()
        }
        omzetBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivityOmzet::class.java))
            finish()
        }
        produkBeranda.setOnClickListener {
            alertDialog.setMessage("Masuk ke menu daftar kunjungan atau menu target kunjungan ?")
                .setCancelable(false)
                .setPositiveButton("SURVEY", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        startActivity(Intent(this@ActivityBeranda, ActivitySalesList::class.java))
                        finish()
                    }
                })
                .setNeutralButton("TARGET", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        startActivity(Intent(this@ActivityBeranda, ActivitySalesTarget::class.java))
                        finish()
                    }
                })
                .setNegativeButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                }).create().show()
        }
        surveyBeranda.setOnClickListener {
            startActivity(Intent(this@ActivityBeranda, ActivitySurveyList::class.java))
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
        val fetchData = FetchData(ApiAdmin.PENGGUNA +
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