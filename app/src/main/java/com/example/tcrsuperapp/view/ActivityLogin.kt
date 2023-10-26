package com.example.tcrsuperapp.view

import android.content.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Pengguna
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {
    lateinit var SP: SharedPreferences
    lateinit var alertDialog: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        SP = getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
        alertDialog = AlertDialog.Builder(this)
        btnLogin.setOnClickListener {
            if(validate()) {
                login()
            }
        }
        resetLogin.setOnClickListener {
            startActivity(Intent(this@ActivityLogin, ActivityPassword::class.java))
            finish()
        }
    }

    private fun validate(): Boolean {
        if(userLogin.text.toString() == "") {
            Toast.makeText(this, "Username kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        if(passLogin.text.toString() == "") {
            Toast.makeText(this, "Password kosong", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun login() {
        Toast.makeText(this@ActivityLogin, "Mohon Tunggu...", Toast.LENGTH_SHORT).show()
        FirebaseDatabase.getInstance().getReference("pengguna").orderByChild("username")
            .equalTo(userLogin.text.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        for (h in p0.children) {
                            val us = h.getValue(Pengguna::class.java)
                            if(us!!.password == passLogin.text.toString()) {
                                val editor = SP.edit()
                                editor.putString("id_pengguna", us.id_pengguna)
                                editor.putString("username", us.username)
                                editor.putString("password", us.password)
                                editor.putString("level", us.level)
                                editor.apply()

                                if(us.level == "Admin") {
                                    startActivity(Intent(this@ActivityLogin,
                                        com.example.tcrsuperapp.view.admin.ActivityBeranda::class.java))
                                    finish()
                                } else if(us.level == "Staff") {
                                    startActivity(Intent(this@ActivityLogin,
                                        com.example.tcrsuperapp.view.staff.ActivityBeranda::class.java))
                                    finish()
                                } else {
//                                    startActivity(Intent(this@ActivityLogin, ActivityArea::class.java))
//                                    finish()
                                }
                            } else {
                                Toast.makeText(this@ActivityLogin, "Password salah", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this@ActivityLogin, "Username salah", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    override fun onBackPressed() {
        alertDialog.setTitle("Keluar Aplikasi")
        alertDialog.setMessage("Apakah anda ingin keluar aplikasi ?").setCancelable(false)
            .setNeutralButton("", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {}
            })
            .setPositiveButton("YA", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    finishAffinity()
                }
            })
            .setNegativeButton("TIDAK", object: DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id:Int) {
                    dialog.cancel()
                }
            }).create().show()
    }
}