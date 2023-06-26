package com.example.myapplication.webservice.pojo.info

data class Name(
    val common: String,
    val nativeName: Pair<String?, String?>?,
    val official: String
)