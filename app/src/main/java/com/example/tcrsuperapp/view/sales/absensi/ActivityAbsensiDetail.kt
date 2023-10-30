package com.example.tcrsuperapp.view.sales.absensi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiSales
import com.squareup.picasso.Picasso
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_absensi_detail.*
import org.json.JSONObject
import java.util.ArrayList

class ActivityAbsensiDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    var kode: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_absensi_detail)

        alertDialog = AlertDialog.Builder(this)
        kode = arrayListOf("", "", "")
        kode[0] = intent.getStringExtra("kode").toString()
        kode[1] = intent.getStringExtra("approval").toString()
        getData()

        backDetail.setOnClickListener {
            val intent = Intent(this@ActivityAbsensiDetail, ActivityAbsensiList::class.java)
            intent.putExtra("approval", kode[1])
            startActivity(intent)
            finish()
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiSales.ABSENSI_DETAIL + "?kode=" + kode[0])
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

    override fun onBackPressed() {
        val intent = Intent(this@ActivityAbsensiDetail, ActivityAbsensiList::class.java)
        intent.putExtra("approval", kode[1])
        startActivity(intent)
        finish()
    }
}