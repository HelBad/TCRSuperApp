package com.example.tcrsuperapp.model

class Pengguna {
    lateinit var id_pengguna: String
    lateinit var username: String
    lateinit var password: String
    lateinit var level: String

    constructor() {}
    constructor(id_pengguna: String, username: String, password: String, level: String) {
        this.id_pengguna = id_pengguna
        this.username = username
        this.password = password
        this.level = level
    }
}