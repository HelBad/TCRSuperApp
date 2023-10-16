package com.example.tcrsuperapp.view.admin.absensi

import android.content.*
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
import com.example.tcrsuperapp.api.ApiAdmin
import com.squareup.picasso.Picasso
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_absensi_detail.*
import org.json.JSONObject
import java.util.ArrayList

class ActivityAbsensiDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_absensi_detail)

        alertDialog = AlertDialog.Builder(this)
        kode = arrayListOf("", "", "")
        kode[0] = intent.getStringExtra("kode").toString()
        kode[1] = intent.getStringExtra("approval").toString()
        getApproval()

        backDetail.setOnClickListener {
            val intent = Intent(this@ActivityAbsensiDetail, ActivityAbsensiList::class.java)
            intent.putExtra("approval", kode[1])
            startActivity(intent)
            finish()
        }
        btnSetuju.setOnClickListener {
            alertDialog.setMessage("Apakah anda yakin menyetujui absensi ini ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        kode[2] = "Disetujui"
                        approvalAbsensi()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }

        btnTolak.setOnClickListener {
            alertDialog.setMessage("Apakah anda yakin menolak absensi ini ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        kode[2] = "Ditolak"
                        approvalAbsensi()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun getApproval() {
        if(kode[1] == "Menunggu") {
            layDetail.visibility = View.VISIBLE
            getData()
        } else {
            layDetail.visibility = View.GONE
            getData()
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiAdmin.ABSENSI_DETAIL + "?kode=" + kode[0])
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaDetail.text = dataObject.getString("nama")
                    idDetail.text = dataObject.getString("kode_pengguna")
                    waktuDetail.text = dataObject.getString("tanggal") + ", " + dataObject.getString("waktu")
                    lokasiDetail.text = dataObject.getString("lokasi")
                    ketDetail.text = dataObject.getString("catatan")
                    statusDetail.text = dataObject.getString("keterangan") + " - " + dataObject.getString("approval")
                    Picasso.get().load(dataObject.getString("foto")).into(fotoDetail)
                } catch (t: Throwable) { }
            }
        }
    }

    private fun approvalAbsensi() {
        AndroidNetworking.post(ApiAdmin.ABSENSI_APPROVAL)
            .addBodyParameter("kode", kode[0])
            .addBodyParameter("approval", kode[2])
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        val intent = Intent(this@ActivityAbsensiDetail, ActivityAbsensiList::class.java)
                        intent.putExtra("approval", kode[1])
                        startActivity(intent)
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        val intent = Intent(this@ActivityAbsensiDetail, ActivityAbsensiList::class.java)
        intent.putExtra("approval", kode[1])
        startActivity(intent)
        finish()
    }
}