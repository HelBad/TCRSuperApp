package com.example.tcrsuperapp.view.sales.survey

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiSales
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_survey.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class ActivitySurvey : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var SP1: SharedPreferences
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var formatY = SimpleDateFormat("YY")
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_survey)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        SP1 = getSharedPreferences("Survey", Context.MODE_PRIVATE)
        kode = arrayListOf("", "", "", "", "", "")
        getData()
        getLoc()
        kontenSurvey()

        btnSurvey.setOnClickListener {
            alertDialog.setMessage("Apakah data yang anda masukkan sudah benar ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        simpanSurvey()
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
        val fetchData1 = FetchData(ApiSales.SURVEY_COUNT)
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

        kode[1] = SP.getString("username", "").toString() + "/SUR/" +
                formatY.format(Date()).toString() + "/" + kode[0]
    }

    @SuppressLint("MissingPermission")
    private fun getLoc() {
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(this)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
        getLocations()
    }

    @SuppressLint("MissingPermission")
    private fun getLocations() {
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            if(it == null) {
                Toast.makeText(this, "Lokasi gagal ditampilkan", Toast.LENGTH_SHORT).show()
            } else it.apply {
                val geocoder = Geocoder(this@ActivitySurvey, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                val address: String = addresses[0].getAddressLine(0)
                kode[5] = address
            }
        }
    }

    private fun kontenSurvey() {
        nilaiSurvey11.visibility = View.VISIBLE
        nilaiSurvey12.visibility = View.GONE
        nilaiSurvey13.visibility = View.VISIBLE
        nilaiSurvey14.visibility = View.GONE
        nilaiSurvey15.visibility = View.VISIBLE
        nilaiSurvey16.visibility = View.GONE
        nilaiSurvey11.setOnClickListener {
            nilaiSurvey11.visibility = View.GONE
            nilaiSurvey12.visibility = View.VISIBLE
            nilaiSurvey13.visibility = View.VISIBLE
            nilaiSurvey14.visibility = View.GONE
            nilaiSurvey15.visibility = View.VISIBLE
            nilaiSurvey16.visibility = View.GONE
            kode[2] = "1"
        }
        nilaiSurvey13.setOnClickListener {
            nilaiSurvey11.visibility = View.VISIBLE
            nilaiSurvey12.visibility = View.GONE
            nilaiSurvey13.visibility = View.GONE
            nilaiSurvey14.visibility = View.VISIBLE
            nilaiSurvey15.visibility = View.VISIBLE
            nilaiSurvey16.visibility = View.GONE
            kode[2] = "3"
        }
        nilaiSurvey15.setOnClickListener {
            nilaiSurvey11.visibility = View.VISIBLE
            nilaiSurvey12.visibility = View.GONE
            nilaiSurvey13.visibility = View.VISIBLE
            nilaiSurvey14.visibility = View.GONE
            nilaiSurvey15.visibility = View.GONE
            nilaiSurvey16.visibility = View.VISIBLE
            kode[2] = "5"
        }

        nilaiSurvey21.visibility = View.VISIBLE
        nilaiSurvey22.visibility = View.GONE
        nilaiSurvey23.visibility = View.VISIBLE
        nilaiSurvey24.visibility = View.GONE
        nilaiSurvey25.visibility = View.VISIBLE
        nilaiSurvey26.visibility = View.GONE
        nilaiSurvey21.setOnClickListener {
            nilaiSurvey21.visibility = View.GONE
            nilaiSurvey22.visibility = View.VISIBLE
            nilaiSurvey23.visibility = View.VISIBLE
            nilaiSurvey24.visibility = View.GONE
            nilaiSurvey25.visibility = View.VISIBLE
            nilaiSurvey26.visibility = View.GONE
            kode[3] = "1"
        }
        nilaiSurvey23.setOnClickListener {
            nilaiSurvey21.visibility = View.VISIBLE
            nilaiSurvey22.visibility = View.GONE
            nilaiSurvey23.visibility = View.GONE
            nilaiSurvey24.visibility = View.VISIBLE
            nilaiSurvey25.visibility = View.VISIBLE
            nilaiSurvey26.visibility = View.GONE
            kode[3] = "3"
        }
        nilaiSurvey25.setOnClickListener {
            nilaiSurvey21.visibility = View.VISIBLE
            nilaiSurvey22.visibility = View.GONE
            nilaiSurvey23.visibility = View.VISIBLE
            nilaiSurvey24.visibility = View.GONE
            nilaiSurvey25.visibility = View.GONE
            nilaiSurvey26.visibility = View.VISIBLE
            kode[3] = "5"
        }

        nilaiSurvey31.visibility = View.VISIBLE
        nilaiSurvey32.visibility = View.GONE
        nilaiSurvey33.visibility = View.VISIBLE
        nilaiSurvey34.visibility = View.GONE
        nilaiSurvey35.visibility = View.VISIBLE
        nilaiSurvey36.visibility = View.GONE
        nilaiSurvey31.setOnClickListener {
            nilaiSurvey31.visibility = View.GONE
            nilaiSurvey32.visibility = View.VISIBLE
            nilaiSurvey33.visibility = View.VISIBLE
            nilaiSurvey34.visibility = View.GONE
            nilaiSurvey35.visibility = View.VISIBLE
            nilaiSurvey36.visibility = View.GONE
            kode[4] = "1"
        }
        nilaiSurvey33.setOnClickListener {
            nilaiSurvey31.visibility = View.VISIBLE
            nilaiSurvey32.visibility = View.GONE
            nilaiSurvey33.visibility = View.GONE
            nilaiSurvey34.visibility = View.VISIBLE
            nilaiSurvey35.visibility = View.VISIBLE
            nilaiSurvey36.visibility = View.GONE
            kode[4] = "3"
        }
        nilaiSurvey35.setOnClickListener {
            nilaiSurvey31.visibility = View.VISIBLE
            nilaiSurvey32.visibility = View.GONE
            nilaiSurvey33.visibility = View.VISIBLE
            nilaiSurvey34.visibility = View.GONE
            nilaiSurvey35.visibility = View.GONE
            nilaiSurvey36.visibility = View.VISIBLE
            kode[4] = "5"
        }
    }

    private fun simpanSurvey() {
        AndroidNetworking.post(ApiSales.SURVEY_ADD)
            .addBodyParameter("kode", kode[1])
            .addBodyParameter("kode_pengguna", SP.getString("username", "").toString())
            .addBodyParameter("cust", SP1.getString("kode", "").toString())
            .addBodyParameter("pic", SP1.getString("pic", "").toString())
            .addBodyParameter("waktu", formatDate.format(Date()).toString())
            .addBodyParameter("lokasi", kode[5])
            .addBodyParameter("survey_1", kode[2])
            .addBodyParameter("survey_2", kode[3])
            .addBodyParameter("survey_3", kode[4])
            .addBodyParameter("survey_4", nilaiSurvey4.text.toString())
            .addBodyParameter("survey_5", nilaiSurvey5.text.toString())
            .addBodyParameter("total_rate", (kode[2].toInt() + kode[3].toInt() + kode[4].toInt()).toString())
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        val editor = SP1.edit()
                        editor.putString("kode", "")
                        editor.putString("perusahaan", "")
                        editor.putString("pic", "")
                        editor.apply()

                        startActivity(Intent(this@ActivitySurvey, ActivitySurveyAkhir::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySurvey, ActivitySurveyAwal::class.java))
        finish()
    }
}