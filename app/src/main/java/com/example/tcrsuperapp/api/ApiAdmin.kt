package com.example.tcrsuperapp.api

class ApiAdmin {
    companion object {
        private val SERVER = "http://10.0.2.2/fuboru_tcr_superapp/admin/"
        val ABSENSI = SERVER + "absensi.php"
        val ABSENSI_ADD = SERVER + "absensi_add.php"
        val ABSENSI_APPROVAL = SERVER + "absensi_approval.php"
        val ABSENSI_COUNT = SERVER + "absensi_count.php"
        val ABSENSI_DETAIL = SERVER + "absensi_detail.php"
        val OMZET = SERVER + "omzet.php"
        val PENGGUNA = SERVER + "pengguna.php"
    }
}