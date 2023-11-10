package com.example.tcrsuperapp.view.staff.sp

import android.app.DatePickerDialog
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
import kotlinx.android.synthetic.main.staff_activity_sp.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class ActivitySp : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var SP1: SharedPreferences
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var formatDate2 = SimpleDateFormat("YYYY-MM-dd")
    val kalender = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_sp)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        SP1 = getSharedPreferences("SP", Context.MODE_PRIVATE)
        getData()

        custSp.setOnClickListener {
            val editor = SP1.edit()
            editor.putString("faktur", fakturSp.text.toString())
            editor.putString("no_order", orderSp.text.toString())
            editor.apply()

            startActivity(Intent(this@ActivitySp, ActivitySpCust::class.java))
        }
        tglinvoiceSp.setOnClickListener {
            setWaktu()
        }
        backSp.setOnClickListener {
            alertDialog.setMessage("Progress belum tersimpan, Apakah anda tetap ingin keluar halaman ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        val editor = SP1.edit()
                        editor.putString("kode", "")
                        editor.putString("perusahaan", "")
                        editor.putString("alamat", "")
                        editor.putString("faktur", "")
                        editor.putString("no_order", "")
                        editor.apply()

                        startActivity(Intent(this@ActivitySp, ActivitySpList::class.java))
                        finish()
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

    private fun setWaktu() {
        val tanggal = DatePickerDialog(this, {
                view, year, month, dayOfMonth -> val selectedDate = Calendar.getInstance()
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            tglinvoiceSp.setText(formatDate2.format(selectedDate.time))
        }, kalender.get(Calendar.YEAR), kalender.get(Calendar.MONTH), kalender.get(Calendar.DAY_OF_MONTH))
        tanggal.show()
    }

    private fun getData() {
        fakturSp.setText(SP1.getString("faktur", "").toString())
        orderSp.setText(SP1.getString("no_order", "").toString())

        if(SP1.getString("perusahaan", "").toString() == "null") {
            custSp.setText("")
            alamatSp.setText("")
        } else {
            custSp.setText(SP1.getString("perusahaan", "").toString())
            alamatSp.setText(SP1.getString("alamat", "").toString())
        }
    }

    private fun simpanSp() {
        AndroidNetworking.post(ApiStaff.SP_ADD)
            .addBodyParameter("kode_nota", fakturSp.text.toString())
            .addBodyParameter("no_order", orderSp.text.toString())
            .addBodyParameter("cust", SP1.getString("kode", "").toString())
            .addBodyParameter("tgl_invoice", tglinvoiceSp.text.toString())
            .addBodyParameter("nominal", nominalSp.text.toString())
            .addBodyParameter("create_by", SP.getString("username", "").toString())
            .addBodyParameter("create_date", formatDate.format(Date()).toString())
            .addBodyParameter("keterangan", keteranganSp.text.toString())
            .addBodyParameter("status", "DIPROSES")
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        val editor = SP1.edit()
                        editor.putString("kode", "")
                        editor.putString("perusahaan", "")
                        editor.putString("alamat", "")
                        editor.putString("faktur", "")
                        editor.putString("no_order", "")
                        editor.apply()

                        startActivity(Intent(this@ActivitySp, ActivitySpList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        alertDialog.setMessage("Progress belum tersimpan, Apakah anda tetap ingin keluar halaman ?").setCancelable(false)
            .setNeutralButton("", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {}
            })
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    val editor = SP1.edit()
                    editor.putString("kode", "")
                    editor.putString("perusahaan", "")
                    editor.putString("alamat", "")
                    editor.putString("faktur", "")
                    editor.putString("no_order", "")
                    editor.apply()

                    startActivity(Intent(this@ActivitySp, ActivitySpList::class.java))
                    finish()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}