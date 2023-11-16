package com.example.tcrsuperapp.view.admin.retur

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiAdmin
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_retur_detail.*
import org.json.JSONObject

class ActivityReturDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var dataRetur: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_retur_detail)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        dataRetur = arrayListOf("", "")
        getData()
        getLayout()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
            finish()
        }
    }

    private fun getLayout() {
        if(intent.getStringExtra("status").toString() == "DIPROSES") {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[0])
                intent.putExtra("nota", dataRetur[1])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            layFotonota.visibility = View.GONE
            layTglsetuju.visibility = View.GONE
            layKolektor.visibility = View.GONE
        } else if(intent.getStringExtra("status").toString() == "SELESAI") {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[0])
                intent.putExtra("nota", dataRetur[1])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            fotonotaDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[0])
                intent.putExtra("nota", dataRetur[1])
                intent.putExtra("status", "NOTA")
                startActivity(intent)
            }
        } else {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[0])
                intent.putExtra("nota", dataRetur[1])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            ketDetail.isClickable = false
            ketDetail.inputType = InputType.TYPE_NULL

            layNoretur.visibility = View.GONE
            layKoderetur.visibility = View.GONE
            layPiutang.visibility = View.GONE
            layFotonota.visibility = View.GONE
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiAdmin.RETUR_DETAIL +
                "?id_retur=" + intent.getStringExtra("kode").toString())
        if (fetchData.startFetch()) {
            if (fetchData.onComplete()) {
                val result: String = fetchData.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaDetail.setText(dataObject.getString("perusahaan"))
                    alamatDetail.setText(dataObject.getString("alamat") + ", " + dataObject.getString("kota"))

                    createbyDetail.setText(dataObject.getString("nama_a"))
                    dataRetur[0] = dataObject.getString("img_brg")
                    createdateDetail.setText(dataObject.getString("create_date"))
                    noreturDetail.setText(dataObject.getString("no_retur"))
                    kodereturDetail.setText(dataObject.getString("kode_retur"))
                    piutangDetail.setText(dataObject.getString("kode_piutang"))
                    dataRetur[1] = dataObject.getString("img_nota")
                    tglsetujuDetail.setText(dataObject.getString("tgl_nota"))
                    ketDetail.setText(dataObject.getString("keterangan"))
                    statusDetail.setText(dataObject.getString("status"))
                    kolektorDetail.setText(dataObject.getString("nama_b"))
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
        finish()
    }
}