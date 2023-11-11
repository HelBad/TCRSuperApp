package com.example.tcrsuperapp.view.sales.sp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiSales
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_sp_detail.*
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivitySpDetail : AppCompatActivity() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_sp_detail)

        getData()
        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
            finish()
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiSales.SP_DETAIL + "?kode_nota=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    fpDetail.setText(dataObject.getString("kode_nota"))
                    orderDetail.setText(dataObject.getString("no_order"))
                    custDetail.setText(dataObject.getString("perusahaan"))
                    alamatDetail.setText(dataObject.getString("alamat") + ", " + dataObject.getString("kota"))
                    tglinvoiceDetail.setText(dataObject.getString("tgl_invoice"))
                    nominalDetail.setText("Rp. " + formatNumber.format(dataObject.getString("nominal").toInt()) + ",00")
                    createbyDetail.setText(dataObject.getString("nama_a"))
                    createDateDetail.setText(dataObject.getString("create_date"))
                    if(dataObject.getString("tgl_kirim") == "0000-00-00") {
                        layTglkirim.visibility = View.GONE
                    } else {
                        tglkirimDetail.setText(dataObject.getString("tgl_kirim"))
                    }
                    if(dataObject.getString("ekspedisi") == "") {
                        layEkspedisi.visibility = View.GONE
                    } else {
                        ekspedisiDetail.setText(dataObject.getString("ekspedisi"))
                    }
                    if(dataObject.getString("no_resi") == "") {
                        layResi.visibility = View.GONE
                    } else {
                        resiDetail.setText(dataObject.getString("no_resi"))
                    }
                    if(dataObject.getString("nota_kembali") == "") {
                        layNota.visibility = View.GONE
                    } else {
                        notaDetail.setText(dataObject.getString("nota_kembali"))
                    }
                    if(dataObject.getString("tgl_kembali") == "0000-00-00 00:00:00") {
                        layTglnota.visibility = View.GONE
                    } else {
                        tglnotaDetail.setText(dataObject.getString("tgl_kembali"))
                    }
                    if(dataObject.getString("keterangan") == "") {
                        keteranganDetail.setText("-")
                    } else {
                        keteranganDetail.setText(dataObject.getString("keterangan"))
                    }
                    statusDetail.setText(dataObject.getString("status"))
                    if(dataObject.getString("kolektor") == "") {
                        layKolektor.visibility = View.GONE
                    } else {
                        kolektorDetail.setText(dataObject.getString("nama_b"))
                    }
                    if(dataObject.getString("admin") == "") {
                        layAdmin.visibility = View.GONE
                    } else {
                        adminDetail.setText(dataObject.getString("nama_c"))
                    }
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
        finish()
    }
}