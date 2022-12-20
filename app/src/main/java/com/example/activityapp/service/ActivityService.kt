package com.example.activityapp.service

import com.example.activityapp.model.ActivityItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ActivityService {
    @GET("activity")
    fun getActivity(@Query("type") type: String?): Call<ActivityItem>
}
