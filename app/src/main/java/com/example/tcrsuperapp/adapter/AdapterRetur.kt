package com.example.tcrsuperapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tcrsuperapp.R
import com.example.tcrsuperapp.model.Retur

class AdapterRetur(dataList: ArrayList<Retur>?): RecyclerView.Adapter<AdapterRetur.ReturViewHolder>() {
    private val dataList: ArrayList<Retur>?
    lateinit var SP: SharedPreferences

    init {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.list_customer, parent, false)
        return ReturViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReturViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.custRetur.text = dataList!![position].perusahaan
        if(dataList[position].no_retur == "") {
            holder.noRetur.visibility = View.GONE
        } else {
            holder.noRetur.text = "NO. RETUR : " + dataList[position].no_retur
        }
        holder.dateRetur.text = dataList[position].create_date

        holder.viewHolder.setOnClickListener { v ->
            SP = v.context.getSharedPreferences("Pengguna", Context.MODE_PRIVATE)
            if(SP.getString("level", "").toString() == "Admin") {

            } else if(SP.getString("level", "").toString() == "Staff") {
//                val intent = Intent(v.context,
//                    com.example.tcrsuperapp.view.staff.retur.ActivityReturDetail::class.java)
//                intent.putExtra("kode", dataList[position].kode_nota)
//                intent.putExtra("status", dataList[position].status)
//                v.context.startActivity(intent)
//                (v.context as AppCompatActivity).finish()
            } else {

            }
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    inner class ReturViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val custRetur: TextView
        val noRetur: TextView
        val dateRetur: TextView
        val viewHolder: View

        init {
            custRetur = itemView.findViewById<View>(R.id.namaCust) as TextView
            noRetur = itemView.findViewById<View>(R.id.alamatCust) as TextView
            dateRetur = itemView.findViewById<View>(R.id.telpCust) as TextView
            viewHolder = itemView
        }
    }
}