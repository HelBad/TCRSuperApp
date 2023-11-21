package com.example.tcrsuperapp.view.staff.izin

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
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
import com.example.tcrsuperapp.api.ApiStaff
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin_detail.*
import org.json.JSONObject
import java.util.Locale

class ActivityIzinDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    var locIzin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin_detail)

        alertDialog = AlertDialog.Builder(this)
        getData()
        lokasiSekarang()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityIzinDetail, ActivityIzinList::class.java))
            finish()
        }
        btnAbsen.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin mengupload kritik atau saran ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        simpanIzin()
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
        val fetchData = FetchData(ApiStaff.IZIN_DETAIL +
                "?kode_izin=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    idDetail.text = dataObject.getString("kode_pengguna")
                    waktuDetail.text = dataObject.getString("waktu_awal")
                    batasDetail.text = dataObject.getString("waktu_akhir")
                    alasanDetail.text = dataObject.getString("keperluan")

                    if(dataObject.getString("lokasi") == "") {
                        layLokasi.visibility = View.GONE
                    } else {
                        lokasiDetail.text = dataObject.getString("lokasi")
                    }
                    if(dataObject.getString("status") == "") {
                        layStatus.visibility = View.GONE
                    } else {
                        statusDetail.text = dataObject.getString("status")
                    }
                    namaDetail.text = dataObject.getString("nama_a")
                    if(dataObject.getString("approval") == "") {
                        layApproval.visibility = View.GONE
                    } else {
                        approvalDetail.text = dataObject.getString("nama_b")
                        if(intent.getStringExtra("status").toString() == "AKTIF") {
                            layDetail.visibility = View.VISIBLE
                        } else {
                            layDetail.visibility = View.GONE
                        }
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun lokasiSekarang() {
        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) { }
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
                val geocoder = Geocoder(this@ActivityIzinDetail, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                val address: String = addresses[0].getAddressLine(0)
                locIzin = address
            }
        }
    }

    private fun simpanIzin() {
        AndroidNetworking.post(ApiStaff.IZIN_DETAIL_UPDATE)
            .addBodyParameter("kode_izin", intent.getStringExtra("kode").toString())
            .addBodyParameter("lokasi", locIzin)
            .addBodyParameter("status", "SELESAI")
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityIzinDetail, ActivityIzinList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzinDetail, ActivityIzinList::class.java))
        finish()
    }
}