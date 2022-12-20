package com.example.activityapp.model

data class ActivityItem(
    val activity: String,
    val accessibility: Double,
    val type: String,
    val participants: Int,
    val price: Double,
    val link: String,
    val key: String,
    var star: Boolean = false,
    var shared: Boolean = false
)
