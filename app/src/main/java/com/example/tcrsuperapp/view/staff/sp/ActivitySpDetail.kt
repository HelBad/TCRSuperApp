package com.example.tcrsuperapp.view.staff.sp

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiStaff
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_sp_detail.*
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ActivitySpDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    var formatNumber: NumberFormat = DecimalFormat("#,###")
    var dataSp: ArrayList<String> = arrayListOf()
    var nota: ArrayList<String> = arrayListOf()
    var formatDate = SimpleDateFormat("YYYY-MM-dd")
    var formatDate2 = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    val kalender = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_sp_detail)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        dataSp = arrayListOf("", "", "")
        getLayout()
        getData()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
            finish()
        }
        btnBatal.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin membatalkan monitoring SP ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        Toast.makeText(this@ActivitySpDetail, "kode_nota : " + fpDetail.text.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@ActivitySpDetail, "tgl_kembali : " + tglnotaDetail.text.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@ActivitySpDetail, "keterangan : " + keteranganDetail.text.toString(), Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@ActivitySpDetail, "kolektor : " + dataSp[0], Toast.LENGTH_SHORT).show()
                        Toast.makeText(this@ActivitySpDetail, "status : DIBATALKAN", Toast.LENGTH_SHORT).show()

                        batalSp()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menyimpan monitoring SP ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        simpanSp()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
    }

    private fun getLayout() {
        if(intent.getStringExtra("status").toString() == "DIPROSES") {
            tglkirimDetail.setOnClickListener {
                setWaktu()
            }
            notaDetail.visibility = View.GONE
            layTglnota.visibility = View.GONE
            layKolektor.visibility = View.GONE
            layAdmin.visibility = View.GONE
        } else if(intent.getStringExtra("status").toString() == "SUDAH DIKIRIM") {
            ekspedisiDetail.isClickable = false
            resiDetail.isClickable = false
            ekspedisiDetail.inputType = InputType.TYPE_NULL
            resiDetail.inputType = InputType.TYPE_NULL

            layRadionota.visibility = View.GONE
            layAdmin.visibility = View.GONE
            btnBatal.visibility = View.GONE
        } else if(intent.getStringExtra("status").toString() == "SELESAI") {
            ekspedisiDetail.isClickable = false
            resiDetail.isClickable = false
            keteranganDetail.isClickable = false
            ekspedisiDetail.inputType = InputType.TYPE_NULL
            resiDetail.inputType = InputType.TYPE_NULL
            keteranganDetail.inputType = InputType.TYPE_NULL

            layRadionota.visibility = View.GONE
            layBtn.visibility = View.GONE
        } else {
            keteranganDetail.isClickable = false
            keteranganDetail.inputType = InputType.TYPE_NULL

            layTglkirim.visibility = View.GONE
            layEkspedisi.visibility = View.GONE
            layResi.visibility = View.GONE
            layNota.visibility = View.GONE
            layTglnota.visibility = View.GONE
            layAdmin.visibility = View.GONE
            layBtn.visibility = View.GONE
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiStaff.SP_DETAIL + "?kode_nota=" + intent.getStringExtra("kode").toString())
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
                    tglkirimDetail.setText(dataObject.getString("tgl_kirim"))
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
                    notaDetail.setText(dataObject.getString("nota_kembali"))
                    tglnotaDetail.setText(dataObject.getString("tgl_kembali"))
                    if(dataObject.getString("keterangan") == "") {
                        keteranganDetail.setText("-")
                    } else {
                        keteranganDetail.setText(dataObject.getString("keterangan"))
                    }
                    dataSp[0] = dataObject.getString("kolektor")
                    statusDetail.setText(dataObject.getString("status"))
                    kolektorDetail.setText(dataObject.getString("nama_b"))
                    adminDetail.setText(dataObject.getString("nama_c"))
                } catch (t: Throwable) { }
            }
        }

        if(intent.getStringExtra("status").toString() == "DIPROSES") {
            tglkirimDetail.setText("")
            layEkspedisi.visibility = View.VISIBLE
            layResi.visibility = View.VISIBLE
            textNota()
            tglnotaDetail.setText(formatDate2.format(Date()).toString())
            dataSp[0] = SP.getString("username", "").toString()
            dataSp[2] = "SUDAH DIKIRIM"
        } else {
            dataSp[1] = SP.getString("username", "").toString()
            dataSp[2] = "SELESAI"
        }
    }

    private fun setWaktu() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tglkirimDetail.setText(formatDate.format(selectedDate.time))
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun textNota() {
        nota = arrayListOf("", "", "", "")
        notaPutih1.setOnClickListener {
            notaPutih1.visibility = View.GONE
            notaPutih2.visibility = View.VISIBLE
            nota[0] = "Putih "
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaPutih2.setOnClickListener {
            notaPutih1.visibility = View.VISIBLE
            notaPutih2.visibility = View.GONE
            nota[0] = ""
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaPink1.setOnClickListener {
            notaPink1.visibility = View.GONE
            notaPink2.visibility = View.VISIBLE
            nota[1] = "Pink "
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaPink2.setOnClickListener {
            notaPink1.visibility = View.VISIBLE
            notaPink2.visibility = View.GONE
            nota[1] = ""
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaKuning1.setOnClickListener {
            notaKuning1.visibility = View.GONE
            notaKuning2.visibility = View.VISIBLE
            nota[2] = "Kuning "
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaKuning2.setOnClickListener {
            notaKuning1.visibility = View.VISIBLE
            notaKuning2.visibility = View.GONE
            nota[2] = ""
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaHijau1.setOnClickListener {
            notaHijau1.visibility = View.GONE
            notaHijau2.visibility = View.VISIBLE
            nota[3] = "Hijau"
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
        notaHijau2.setOnClickListener {
            notaHijau1.visibility = View.VISIBLE
            notaHijau2.visibility = View.GONE
            nota[3] = ""
            notaDetail.setText(nota[0] + nota[1] + nota[2] + nota[3])
        }
    }

    private fun simpanSp() {
        AndroidNetworking.post(ApiStaff.SP_DETAIL_UPDATE)
            .addBodyParameter("kode_nota", fpDetail.text.toString())
            .addBodyParameter("tgl_kirim", tglkirimDetail.text.toString())
            .addBodyParameter("ekspedisi", ekspedisiDetail.text.toString())
            .addBodyParameter("no_resi", resiDetail.text.toString())
            .addBodyParameter("nota_kembali", notaDetail.text.toString())
            .addBodyParameter("tgl_kembali", tglnotaDetail.text.toString())
            .addBodyParameter("keterangan", keteranganDetail.text.toString())
            .addBodyParameter("kolektor", dataSp[0])
            .addBodyParameter("admin", dataSp[1])
            .addBodyParameter("status", dataSp[2])
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun batalSp() {
        AndroidNetworking.post(ApiStaff.SP_DETAIL_BATAL)
            .addBodyParameter("kode_nota", fpDetail.text.toString())
            .addBodyParameter("tgl_kembali", tglnotaDetail.text.toString())
            .addBodyParameter("keterangan", keteranganDetail.text.toString())
            .addBodyParameter("kolektor", dataSp[0])
            .addBodyParameter("status", "DIBATALKAN")
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySpDetail, ActivitySpList::class.java))
        finish()
    }
}