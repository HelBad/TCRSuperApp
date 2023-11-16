package com.example.tcrsuperapp.view.staff.retur

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_retur.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

class ActivityRetur : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var SP1: SharedPreferences
    lateinit var bitmap: Bitmap
    lateinit var encodedimage: String
    var formatDate = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
    var kode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_retur)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        SP1 = getSharedPreferences("Retur", Context.MODE_PRIVATE)
        getData()

        custRetur.setOnClickListener {
            startActivity(Intent(this@ActivityRetur, ActivityReturCust::class.java))
        }
        backRetur.setOnClickListener {
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
                        editor.apply()

                        startActivity(Intent(this@ActivityRetur, ActivityReturList::class.java))
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
            alertDialog.setMessage("Apakah anda ingin menyimpan Retur Penjualan ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(custRetur.text.toString() != "") {
                            Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
                                .withListener(object : PermissionListener {
                                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
                                        val editor = SP1.edit()
                                        editor.putString("perusahaan", "")
                                        editor.putString("alamat", "")
                                        editor.apply()

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
                            Toast.makeText(this@ActivityRetur, "Lengkapi data dahulu", Toast.LENGTH_SHORT).show()
                        }
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
        if(SP1.getString("perusahaan", "").toString() == "null") {
            custRetur.setText("")
            alamatRetur.setText("")
        } else {
            custRetur.setText(SP1.getString("perusahaan", "").toString())
            alamatRetur.setText(SP1.getString("alamat", "").toString())
        }

        val fetchData1 = FetchData(ApiStaff.RETUR_COUNT)
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    kode = "R" + String.format("%04d", dataObject.getString("COUNT(*)").toInt() + 1)
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap
            encodebitmap(bitmap)
            uploadFoto()
            startActivity(Intent(this@ActivityRetur, ActivityReturList::class.java))
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
        val request: StringRequest = object : StringRequest(Method.POST, ApiStaff.RETUR_ADD, Response.Listener {
            Toast.makeText(applicationContext, "Berhasil diupload", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["id_retur"] = kode
                map["cust"] = SP1.getString("kode", "").toString()
                map["img_brg"] = encodedimage
                map["create_by"] = SP.getString("username", "").toString()
                map["create_date"] = formatDate.format(Date()).toString()
                map["keterangan"] = ketRetur.text.toString()
                map["status"] = "DIPROSES"
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
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
                    editor.apply()

                    startActivity(Intent(this@ActivityRetur, ActivityReturList::class.java))
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