package com.example.tcrsuperapp.view.staff.izin

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_izin.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

class ActivityIzin : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var formatTanggal = SimpleDateFormat("YYYY-MM-dd")
    var formatWaktu = SimpleDateFormat("HH:mm:ss")
    var formatY = SimpleDateFormat("YY")
    val kalender = Calendar.getInstance()
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_izin)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        kode = arrayListOf("", "", "", "")
        getData()

        backIzin.setOnClickListener {
            startActivity(Intent(this@ActivityIzin, ActivityIzinList::class.java))
            finish()
        }
        cardWaktu.setOnClickListener {
            setWaktu()
        }
        cardBatas.setOnClickListener {
            setWaktu()
        }
        btnPengajuan.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin mengajukan izin keluar kantor ?").setCancelable(false)
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
        val fetchData1 = FetchData(ApiStaff.IZIN_COUNT)
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    kode[0] = String.format("%04d", dataObject.getString("COUNT(*)").toInt() + 1)
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
                    namaIzin.text = dataObject.getString("nama")
                    idIzin.text = SP.getString("username", "").toString()
                } catch (t: Throwable) { }
            }
        }

        kode[1] = SP.getString("username", "").toString() + "/IKK/" +
                formatY.format(Date()).toString() + "/" + kode[0]
    }

    private fun setWaktu() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            kode[2] = formatTanggal.format(selectedDate.time)

            val waktu = TimePickerDialog(this, {
                    view, hourOfDay, minute -> val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                kode[3] = formatWaktu.format(selectedTime.time)

                if(cardWaktu.isClickable) {
                    waktuIzin.text = kode[2] + " " + kode[3]
                    cardWaktu.isClickable = false
                } else {
                    batasIzin.text = kode[2] + " " + kode[3]
                    cardBatas.isClickable = false
                }
            }, kalender.get(Calendar.HOUR_OF_DAY), kalender.get(Calendar.MINUTE), false)
            waktu.show()
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun simpanIzin() {
        AndroidNetworking.post(ApiStaff.IZIN_ADD)
            .addBodyParameter("kode_izin", kode[0])
            .addBodyParameter("kode_pengguna", idIzin.text.toString())
            .addBodyParameter("waktu_awal", waktuIzin.text.toString())
            .addBodyParameter("waktu_akhir", batasIzin.text.toString())
            .addBodyParameter("keperluan", alasanIzin.text.toString())
            .addBodyParameter("approval", "")
            .addBodyParameter("lokasi", "")
            .addBodyParameter("status", "MENUNGGU")
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityIzin, ActivityIzinList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityIzin, ActivityIzinList::class.java))
        finish()
    }
}