package com.example.tcrsuperapp.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Absensi

class AdapterAbsensi(dataList: ArrayList<Absensi>?): RecyclerView.Adapter<AdapterAbsensi.AbsensiViewHolder>() {
    private val dataList: ArrayList<Absensi>?
    lateinit var SP: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbsensiViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_absensi, parent, false)
        return AbsensiViewHolder(view)
    }

    override fun onBindViewHolder(holder: AbsensiViewHolder, position: Int) {
        holder.namaAbsensi.text = dataList!![position].nama + " (" + dataList!![position].kode_pengguna + ")"
        holder.waktuAbsensi.text = dataList[position].tanggal + ", " + dataList[position].waktu +
                " (" + dataList!![position].status + ")"
        holder.statusAbsensi.text = "Status : " + dataList[position].keterangan + " - " + dataList[position].approval

        when (dataList[position].status) {
            "Pulang Awal" -> { holder.waktuAbsensi.setTextColor(Color.parseColor("#FFFF6333")) }
            "Terlambat" -> { holder.waktuAbsensi.setTextColor(Color.parseColor("#FFFF6333")) }
            else -> { holder.waktuAbsensi.setTextColor(Color.parseColor("#FF239D58")) }
        }

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.admin.absensi.ActivityAbsensiDetail::class.java)
                intent.putExtra("kode", dataList[position].kode)
                intent.putExtra("approval", dataList[position].approval)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else if(SP.getString("level", "").toString() == "Staff") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.staff.absensi.ActivityAbsensiDetail::class.java)
                intent.putExtra("kode", dataList[position].kode)
                intent.putExtra("approval", dataList[position].approval)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else {
//                val intent = Intent(v.context,
//                    com.example.tcrsuperapp.view.sales.absensi.ActivityAbsensiDetail::class.java)
//                intent.putExtra("kode", dataList[position].kode)
//                intent.putExtra("approval", dataList[position].approval)
//                v.context.startActivity(intent)
//                (v.context as AppCompatActivity).finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class AbsensiViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaAbsensi: TextView
        val waktuAbsensi: TextView
        val statusAbsensi: TextView
        val viewHolder: View

        init {
            namaAbsensi = itemView.findViewById<View>(R.id.namaAbsensi) as TextView
            waktuAbsensi = itemView.findViewById<View>(R.id.waktuAbsensi) as TextView
            statusAbsensi = itemView.findViewById<View>(R.id.statusAbsensi) as TextView
            viewHolder = itemView
        }
    }
}