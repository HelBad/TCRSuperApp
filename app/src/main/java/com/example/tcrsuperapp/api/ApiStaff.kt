package com.example.tcrsuperapp.api

class ApiStaff {
    companion object {
        private val SERVER = "http://10.0.2.2/fuboru_tcr_superapp/staff/"
        val ABSENSI = SERVER + "absensi.php"
        val ABSENSI_ADD = SERVER + "absensi_add.php"
        val ABSENSI_COUNT = SERVER + "absensi_count.php"
        val ABSENSI_DETAIL = SERVER + "absensi_detail.php"
        val CUSTOMER = SERVER + "customer.php"
        val FBL = SERVER + "fbl.php"
        val GALERY = SERVER + "galery.php"
        val MASUKAN_ADD = SERVER + "masukan_add.php"
        val MASUKAN_COUNT = SERVER + "masukan_count.php"
        val PENGGUNA = SERVER + "pengguna.php"
        val SP = SERVER + "sp.php"
        val STOK = SERVER + "stok.php"
        val SURVEY = SERVER + "survey.php"
        val SURVEY_ADD = SERVER + "survey_add.php"
        val SURVEY_COUNT = SERVER + "survey_count.php"
        val SURVEY_DETAIL = SERVER + "survey_detail.php"
    }
}