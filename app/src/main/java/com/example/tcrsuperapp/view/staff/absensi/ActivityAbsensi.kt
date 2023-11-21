package com.example.tcrsuperapp.view.staff.absensi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiStaff
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_absensi.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import java.util.HashMap
import java.util.Locale

class ActivityAbsensi : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var bitmap: Bitmap
    lateinit var encodedimage: String

    var formatDate = SimpleDateFormat("YYYY-MM-dd")
    var formatY = SimpleDateFormat("YY")
    var formatTime = SimpleDateFormat("HH:mm:ss")
    var formatH = SimpleDateFormat("HH")
    var formatM = SimpleDateFormat("mm")
    val formatDay = SimpleDateFormat("EEEE")
    var kode: ArrayList<String> = arrayListOf()
    var countTime = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_absensi)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        kode = arrayListOf("", "", "", "")
        getData()
        currentTime()
        lokasiSekarang()

        backAbsensi.setOnClickListener {
            val intent = Intent(this@ActivityAbsensi, ActivityAbsensiList::class.java)
            intent.putExtra("approval", "MENUNGGU")
            startActivity(intent)
            finish()
        }
        btnMasuk.setOnClickListener {
            if(ketAbsensi.text.toString() != "") {
                Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                            kode[2] = "Clock In"
                            if(countTime > 36000) {
                                kode[3] = "Terlambat"
                            } else {
                                kode[3] = "Ontime"
                            }
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, 111)
                        }
                        override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}
                        override fun onPermissionRationaleShouldBeShown(
                            p0: com.karumi.dexter.listener.PermissionRequest?, permissionToken: PermissionToken?) {
                            permissionToken!!.continuePermissionRequest()
                        }
                    }).check()
            } else {
                Toast.makeText(this@ActivityAbsensi, "Lengkapi data dahulu", Toast.LENGTH_SHORT).show()
            }
        }
        btnKeluar.setOnClickListener {
            if(ketAbsensi.text.toString() != "") {
                Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
                    .withListener(object : PermissionListener {
                        override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                            kode[2] = "Clock Out"
                            val day = formatDay.format(Date())
                            if(day.toString() == "Sabtu" || day.toString() == "Saturday") {
                                if(countTime < 50400) {
                                    kode[3] = "Pulang Awal"
                                } else {
                                    kode[3] = "Ontime"
                                }
                            } else {
                                if(countTime <= 61200) {
                                    kode[3] = "Pulang Awal"
                                } else {
                                    kode[3] = "Ontime"
                                }
                            }
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, 111)
                        }
                        override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}
                        override fun onPermissionRationaleShouldBeShown(
                            p0: com.karumi.dexter.listener.PermissionRequest?, permissionToken: PermissionToken?) {
                            permissionToken!!.continuePermissionRequest()
                        }
                    }).check()
            } else {
                Toast.makeText(this@ActivityAbsensi, "Lengkapi data dahulu", Toast.LENGTH_SHORT).show()
            }
        }

        refreshAbsensi.setOnRefreshListener {
            Handler().postDelayed({
                refreshAbsensi.isRefreshing = false
                this.recreate()
            }, 1000)
        }
    }

    private fun getData() {
        val fetchData1 = FetchData(ApiStaff.ABSENSI_COUNT)
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
                    namaAbsensi.text = dataObject.getString("nama")
                    idAbsensi.text = SP.getString("username", "").toString()
                } catch (t: Throwable) { }
            }
        }

        kode[1] = SP.getString("username", "").toString() + "/ABS/" +
                formatY.format(Date()).toString() + "/" + kode[0]
    }

    private fun currentTime() {
        tglAbsensi.text = formatDate.format(Date()).toString()
        waktuAbsensi.text = formatTime.format(Date()).toString()

        countTime = (formatH.format(Date()).toInt() * 3600) + (formatM.format(Date()).toInt() * 60)
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
                val geocoder = Geocoder(this@ActivityAbsensi, Locale.getDefault())
                val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)!!
                val address: String = addresses[0].getAddressLine(0)
                lokasiAbsensi.text = address
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap
            encodebitmap(bitmap)
            uploadFoto()
            startActivity(Intent(this@ActivityAbsensi, ActivityAbsensiList::class.java))
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun encodebitmap(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteofimages = byteArrayOutputStream.toByteArray()
        encodedimage = Base64.encodeToString(byteofimages, Base64.DEFAULT)
    }

    private fun uploadFoto() {
        val request: StringRequest = object : StringRequest(Method.POST, ApiStaff.ABSENSI_ADD, Response.Listener {
            Toast.makeText(applicationContext, "Berhasil diupload", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["kode"] = kode[1]
                map["kode_pengguna"] = idAbsensi.text.toString()
                map["tanggal"] = tglAbsensi.text.toString()
                map["waktu"] = waktuAbsensi.text.toString()
                map["keterangan"] = kode[2]
                map["lokasi"] = lokasiAbsensi.text.toString()
                map["catatan"] = ketAbsensi.text.toString()
                map["status"] = kode[3]
                map["foto"] = encodedimage
                map["approval"] = "MENUNGGU"
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    override fun onBackPressed() {
        val intent = Intent(this@ActivityAbsensi, ActivityAbsensiList::class.java)
        intent.putExtra("approval", "MENUNGGU")
        startActivity(intent)
        finish()
    }
}