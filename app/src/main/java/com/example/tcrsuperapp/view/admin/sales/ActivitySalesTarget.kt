package com.example.tcrsuperapp.view.admin.sales

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.api.ApiAdmin
import com.example.tcrsuperapp.view.admin.ActivityBeranda
import com.squareup.picasso.Picasso
import com.vishnusivadas.advanced_httpurlconnection.FetchData
import kotlinx.android.synthetic.main.admin_activity_sales_target.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException

class ActivitySalesTarget : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var bitmap: Bitmap
    lateinit var encodedimage: String
    private var filePath: Uri? = null
    var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_activity_sales_target)

        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        getData()

        backTarget.setOnClickListener {
            startActivity(Intent(this@ActivitySalesTarget, ActivityBeranda::class.java))
            finish()
        }
        foto1Detail.setOnClickListener {
            if(target1Target.text.toString() == "") {
                Toast.makeText(this@ActivitySalesTarget, "Lengkapi data produk", Toast.LENGTH_SHORT).show()
            } else {
                status = "foto 1"
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111)
            }
        }
        foto2Detail.setOnClickListener {
            if(target2Target.text.toString() == "") {
                Toast.makeText(this@ActivitySalesTarget, "Lengkapi data produk", Toast.LENGTH_SHORT).show()
            } else {
                status = "foto 2"
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 111)
            }
        }
    }

    private fun getData() {
        val fetchData1 = FetchData(ApiAdmin.PENGGUNA +
                "?kode=" + SP.getString("username", "").toString())
        if (fetchData1.startFetch()) {
            if (fetchData1.onComplete()) {
                val result: String = fetchData1.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    namaTarget.text = dataObject.getString("nama")
                } catch (t: Throwable) { }
            }
        }

        val fetchData2 = FetchData(ApiAdmin.SALES_TARGET_DETAIL + "?id_target=T0001")
        if (fetchData2.startFetch()) {
            if (fetchData2.onComplete()) {
                val result: String = fetchData2.result
                try {
                    val data = JSONObject(result)
                    val dataObject = data.getJSONObject("data")
                    target1Target.setText(dataObject.getString("produk_1"))
                    target2Target.setText(dataObject.getString("produk_2"))
                    Picasso.get().load(dataObject.getString("img_1")).into(foto1Detail)
                    Picasso.get().load(dataObject.getString("img_2")).into(foto2Detail)
                } catch (t: Throwable) { }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                encodebitmap(bitmap)
                if(status == "foto 1") {
                    uploadTarget1()
                } else {
                    uploadTarget2()
                }
                startActivity(Intent(this@ActivitySalesTarget, ActivitySalesTarget::class.java))
                finish()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun encodebitmap(bitmap: Bitmap) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteofimages = byteArrayOutputStream.toByteArray()
        encodedimage = Base64.encodeToString(byteofimages, Base64.DEFAULT)
    }

    private fun uploadTarget1() {
        val request: StringRequest = object : StringRequest(Method.POST, ApiAdmin.SALES_TARGET_1, Response.Listener {
            Toast.makeText(applicationContext, "FileUploaded Successfully", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["id_target"] = "T0001"
                map["kode_pengguna"] = SP.getString("username", "").toString()
                map["produk_1"] = target1Target.text.toString()
                map["img_1"] = encodedimage
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    private fun uploadTarget2() {
        val request: StringRequest = object : StringRequest(Method.POST, ApiAdmin.SALES_TARGET_2, Response.Listener {
            Toast.makeText(applicationContext, "FileUploaded Successfully", Toast.LENGTH_SHORT).show()
        }, Response.ErrorListener { error -> Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show() }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String?> {
                val map: HashMap<String, String> = HashMap()
                map["id_target"] = "T0001"
                map["kode_pengguna"] = SP.getString("username", "").toString()
                map["produk_2"] = target2Target.text.toString()
                map["img_2"] = encodedimage
                return map
            }
        }
        val queue = Volley.newRequestQueue(applicationContext)
        queue.add(request)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ActivitySalesTarget, ActivityBeranda::class.java))
        finish()
    }
}