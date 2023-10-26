package com.example.tcrsuperapp.view.staff.masukan

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiStaff
import com.example.tcrsuperapp.view.staff.ActivityBeranda
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_masukan.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date

class ActivityMasukan : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var formatY = SimpleDateFormat("YY")
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_masukan)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        kode = arrayListOf("", "")
        getData()

        backMasukan.setOnClickListener {
            startActivity(Intent(this@ActivityMasukan, ActivityBeranda::class.java))
            finish()
        }
        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin mengupload kritik atau saran ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        simpanMasukan()
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
        val fetchData1 = FetchData(ApiStaff.MASUKAN_COUNT)
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    kode[0] = String.format("%03d", dataObject.getString("COUNT(*)").toInt() + 1)
                } catch (t: Throwable) { }
            }
        }

        val fetchData2 = FetchData(ApiStaff.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaMasukan.text = dataObject.getString("nama")
                    idMasukan.text = SP.getString("username", "").toString()
                } catch (t: Throwable) { }
            }
        }

        kode[1] = SP.getString("username", "").toString() + "/MSK/" +
                formatY.format(Date()).toString() + "/" + kode[0]
    }

    private fun simpanMasukan() {
        AndroidNetworking.post(ApiStaff.MASUKAN_ADD)
            .addBodyParameter("kode", kode[1])
            .addBodyParameter("keterangan", ketMasukan.text.toString())
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityMasukan, ActivityBeranda::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityMasukan, ActivityBeranda::class.java))
        finish()
    }
}