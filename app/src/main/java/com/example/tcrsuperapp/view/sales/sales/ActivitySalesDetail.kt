package com.example.tcrsuperapp.view.sales.sales

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiSales
import com.squareup.picasso.Picasso
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.sales_activity_sales_detail.*
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat

class ActivitySalesDetail : AppCompatActivity() {
    var formatNumber: NumberFormat = DecimalFormat("#,###")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_activity_sales_detail)

        getData()
    }

    private fun getData() {
        val fetchData = FetchData(ApiSales.SALES_DETAIL +
                "?kode_survey=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    idDetail1.text = dataObject.getString("nama")
                    idDetail2.text = dataObject.getString("perusahaan")
                    idDetail3.text = dataObject.getString("pic")
                    idDetail4.text = dataObject.getString("lokasi")
                    idDetail5.text = dataObject.getString("waktu")
                    produk1Detail.text = dataObject.getString("produk_1")
                    kompetitor1Detail.text = dataObject.getString("kompetitor_1")
                    harga1Detail.text = dataObject.getString("stok_1") + " x " + "Rp. " + formatNumber.format(dataObject.getString("harga_1").toInt()) + ",00"
                    produk2Detail.text = dataObject.getString("produk_2")
                    kompetitor2Detail.text = dataObject.getString("kompetitor_2")
                    harga2Detail.text = dataObject.getString("stok_2") + " x " + "Rp. " + formatNumber.format(dataObject.getString("harga_2").toInt()) + ",00"
                    catatanDetail.text = dataObject.getString("keterangan")

                    Picasso.get().load(dataObject.getString("img_1")).into(img1Detail)
                    Picasso.get().load(dataObject.getString("img_2")).into(img2Detail)
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}