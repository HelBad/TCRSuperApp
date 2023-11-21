package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Izin

class AdapterIzin(dataList: ArrayList<Izin>?): RecyclerView.Adapter<AdapterIzin.IzinViewHolder>() {
    private val dataList: ArrayList<Izin>?
    lateinit var SP: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IzinViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_izin, parent, false)
        return IzinViewHolder(view)
    }

    override fun onBindViewHolder(holder: IzinViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.namaIzin.text = dataList!![position].nama
        holder.waktuIzin.text = "Dari : " + dataList[position].waktu_awal
        holder.batasIzin.text = "Sampai : " + dataList[position].waktu_akhir
        holder.statusIzin.text = "Status : " + dataList[position].status

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.admin.izin.ActivityIzinDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_izin)
                intent.putExtra("status", dataList[position].status)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            } else {
                val intent = Intent(v.context,
                    com.example.tcrsuperapp.view.staff.izin.ActivityIzinDetail::class.java)
                intent.putExtra("kode", dataList[position].kode_izin)
                intent.putExtra("status", dataList[position].status)
                v.context.startActivity(intent)
                (v.context as AppCompatActivity).finish()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class IzinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaIzin: TextView
        val waktuIzin: TextView
        val batasIzin: TextView
        val statusIzin: TextView
        val viewHolder: View

        init {
            namaIzin = itemView.findViewById<View>(R.id.namaIzin) as TextView
            waktuIzin = itemView.findViewById<View>(R.id.waktuIzin) as TextView
            batasIzin = itemView.findViewById<View>(R.id.batasIzin) as TextView
            statusIzin = itemView.findViewById<View>(R.id.statusIzin) as TextView
            viewHolder = itemView
        }
    }
}