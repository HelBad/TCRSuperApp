package com.example.tcrsuperapp.view.sales.sales

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.squareup.picasso.Picasso
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_sales.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.Locale

class ActivitySales : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var SP1: SharedPreferences
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var formatY = SimpleDateFormat("YY")
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_sales)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        SP1 = getSharedPreferences("Sales", Context.MODE_PRIVATE)
        kode = arrayListOf("", "", "", "", "", "", "", "")
        getData()
        getLoc()

        btnSales.setOnClickListener {
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
        val fetchData1 = FetchData(ApiSales.SALES_COUNT)
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

        val fetchData2 = FetchData(ApiSales.SALES_TARGET_DETAIL + "?id_target=T0001")
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    tokoSales.text = SP1.getString("perusahaan", "").toString()
                    kode[3] = dataObject.getString("kode_pengguna")
                    kode[4] = dataObject.getString("produk_1")
                    kode[5] = dataObject.getString("img_1")
                    kode[6] = dataObject.getString("produk_2")
                    kode[7] = dataObject.getString("img_2")
                    target1Sales.text = kode[4]
                    target2Sales.text = kode[6]
                    Picasso.get().load(kode[5]).into(img1Sales)
                    Picasso.get().load(kode[7]).into(img2Sales)
                } catch (t: Throwable) { }
            }
        }

        kode[1] = SP.getString("username", "").toString() + "/SKS/" +
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
                val geocoder = Geocoder(this@ActivitySales, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                val address: String = addresses[0].getAddressLine(0)
                kode[2] = address
            }
        }
    }

    private fun simpanSurvey() {
        AndroidNetworking.post(ApiSales.SALES_ADD)
            .addBodyParameter("kode_survey", kode[1])
            .addBodyParameter("kode_pengguna", SP.getString("username", "").toString())
            .addBodyParameter("cust", SP1.getString("kode", "").toString())
            .addBodyParameter("pic", SP1.getString("pic", "").toString())
            .addBodyParameter("waktu", formatDate.format(Date()).toString())
            .addBodyParameter("lokasi", kode[2])
            .addBodyParameter("spv", kode[3])
            .addBodyParameter("produk_1", kode[4])
            .addBodyParameter("img_1", kode[5])
            .addBodyParameter("kompetitor_1", produk1Sales.text.toString())
            .addBodyParameter("stok_1", stok1Sales.text.toString())
            .addBodyParameter("harga_1", harga1Sales.text.toString())
            .addBodyParameter("produk_2", kode[6])
            .addBodyParameter("img_2", kode[7])
            .addBodyParameter("kompetitor_2", produk2Sales.text.toString())
            .addBodyParameter("stok_2", stok2Sales.text.toString())
            .addBodyParameter("harga_2", harga2Sales.text.toString())
            .addBodyParameter("keterangan", catatanSales.text.toString())
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

                        startActivity(Intent(this@ActivitySales, ActivitySalesAkhir::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySales, ActivitySalesAwal::class.java))
        finish()
    }
}