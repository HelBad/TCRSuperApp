package com.example.tcrsuperapp.view.staff.retur

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiStaff
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.staff_activity_retur_detail.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashMap

class ActivityReturDetail : AppCompatActivity() {
    lateinit var alertDialog: AlertDialog.Builder
    lateinit var SP: SharedPreferences
    lateinit var bitmap: Bitmap
    lateinit var encodedimage: String
    var dataRetur: ArrayList<String> = arrayListOf()
    var formatDate = SimpleDateFormat("YYYY-MM-dd")
    var formatDate2 = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_activity_retur_detail)

        alertDialog = AlertDialog.Builder(this)
        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        dataRetur = arrayListOf("", "", "", "")
        getData()
        getLayout()

        backDetail.setOnClickListener {
            startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
            finish()
        }
        btnBatal.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin membatalkan retur penjualan ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        batalRetur()
                    }
                })
                .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        dialog.cancel()
                    }
                }).create().show()
        }
        btnSimpan.setOnClickListener {
            alertDialog.setMessage("Apakah anda ingin menyimpan retur penjualan ?").setCancelable(false)
                .setNeutralButton("", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {}
                })
                .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id:Int) {
                        if(noreturDetail.text.toString() != "" && kodereturDetail.text.toString() != ""
                            && piutangDetail.text.toString() != "") {
                            Dexter.withContext(applicationContext).withPermission(Manifest.permission.CAMERA)
                                .withListener(object : PermissionListener {
                                    override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {
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
                            Toast.makeText(this@ActivityReturDetail, "Lengkapi data dahulu", Toast.LENGTH_SHORT).show()
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

    private fun getLayout() {
        if(intent.getStringExtra("status").toString() == "DIPROSES") {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[2])
                intent.putExtra("nota", dataRetur[3])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            layFotonota.visibility = View.GONE
            layTglsetuju.visibility = View.GONE
            layKolektor.visibility = View.GONE
        } else if(intent.getStringExtra("status").toString() == "SELESAI") {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[2])
                intent.putExtra("nota", dataRetur[3])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            fotonotaDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[2])
                intent.putExtra("nota", dataRetur[3])
                intent.putExtra("status", "NOTA")
                startActivity(intent)
            }

            noreturDetail.isClickable = false
            kodereturDetail.isClickable = false
            piutangDetail.isClickable = false
            ketDetail.isClickable = false
            noreturDetail.inputType = InputType.TYPE_NULL
            kodereturDetail.inputType = InputType.TYPE_NULL
            piutangDetail.inputType = InputType.TYPE_NULL
            ketDetail.inputType = InputType.TYPE_NULL
            layBtn.visibility = View.GONE
        } else {
            fotobrgDetail.setOnClickListener {
                val intent = Intent(this@ActivityReturDetail, ActivityReturPreview::class.java)
                intent.putExtra("barang", dataRetur[2])
                intent.putExtra("nota", dataRetur[3])
                intent.putExtra("status", "BARANG")
                startActivity(intent)
            }
            ketDetail.isClickable = false
            ketDetail.inputType = InputType.TYPE_NULL

            layNoretur.visibility = View.GONE
            layKoderetur.visibility = View.GONE
            layPiutang.visibility = View.GONE
            layFotonota.visibility = View.GONE
            layBtn.visibility = View.GONE
        }
    }

    private fun getData() {
        val fetchData = FetchData(ApiStaff.RETUR_DETAIL +
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
                    dataRetur[2] = dataObject.getString("img_brg")
                    createdateDetail.setText(dataObject.getString("create_date"))
                    noreturDetail.setText(dataObject.getString("no_retur"))
                    kodereturDetail.setText(dataObject.getString("kode_retur"))
                    piutangDetail.setText(dataObject.getString("kode_piutang"))
                    dataRetur[3] = dataObject.getString("img_nota")
                    tglsetujuDetail.setText(dataObject.getString("tgl_nota"))
                    ketDetail.setText(dataObject.getString("keterangan"))
                    statusDetail.setText(dataObject.getString("status"))
                    kolektorDetail.setText(dataObject.getString("nama_b"))
                } catch (t: Throwable) { }
            }
        }

        if(intent.getStringExtra("status").toString() == "DIPROSES") {
            tglsetujuDetail.setText(formatDate2.format(Date()).toString())
            dataRetur[0] = SP.getString("username", "").toString()
            dataRetur[1] = "SELESAI"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 111 && resultCode == RESULT_OK) {
            bitmap = data!!.extras!!["data"] as Bitmap
            encodebitmap(bitmap)
            uploadFoto()
            startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
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
        val request: StringRequest = object : StringRequest(Method.POST, ApiStaff.RETUR_DETAIL_UPDATE, Response.Listener {
            Toast.makeText(applicationContext, "Berhasil diupload", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["id_retur"] = intent.getStringExtra("kode").toString()
                map["no_retur"] = noreturDetail.text.toString()
                map["kode_retur"] = kodereturDetail.text.toString()
                map["kode_piutang"] = piutangDetail.text.toString()
                map["img_nota"] = encodedimage
                map["tgl_nota"] = tglsetujuDetail.text.toString()
                map["keterangan"] = ketDetail.text.toString()
                map["kolektor"] = dataRetur[0]
                map["status"] = dataRetur[1]
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    private fun batalRetur() {
        AndroidNetworking.post(ApiStaff.RETUR_DETAIL_BATAL)
            .addBodyParameter("id_retur", intent.getStringExtra("kode").toString())
            .addBodyParameter("tgl_nota", tglsetujuDetail.text.toString())
            .addBodyParameter("keterangan", ketDetail.text.toString())
            .addBodyParameter("kolektor", dataRetur[0])
            .addBodyParameter("status", "DIBATALKAN")
            .setPriority(Priority.MEDIUM).build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Toast.makeText(applicationContext,response?.getString("status"), Toast.LENGTH_SHORT).show()
                    if(response?.getString("status")?.contains("Data berhasil ditambahkan")!!){
                        startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
                        finish()
                    }
                }
                override fun onError(anError: ANError?) {
                    Toast.makeText(applicationContext,"Connection Failure", Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivityReturDetail, ActivityReturList::class.java))
        finish()
    }
}