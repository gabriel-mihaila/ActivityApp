package com.example.activityapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.activityapp.adapter.ActivityAdapter
import com.example.activityapp.databinding.ActivityMainBinding
import com.example.activityapp.model.ActivityItem
import com.example.activityapp.service.ActivityService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import android.graphics.Color


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lastMenuItem: MenuItem
    private lateinit var service: ActivityService
    private lateinit var activityView: RecyclerView

    private var activities: ArrayList<ActivityItem> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Random"

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.boredapi.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        service = retrofit.create(ActivityService::class.java)
        getActivities("Random")

        activityView = binding.activityList
        activityView.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        registerForContextMenu(activityView)
    }

    private fun getActivities(category: String) {
        activities = arrayListOf()

        for (i in 1..10) {
            service.getActivity(if (category.lowercase(Locale.getDefault()) != "random") category.lowercase(Locale.getDefault()) else null)
                .enqueue(object : Callback<ActivityItem> {
                    override fun onResponse(call: Call<ActivityItem>, response: Response<ActivityItem>) {
                        if (response.isSuccessful) {
                            response.body()?.let { activities.add(it) }
                            activityView.adapter = ActivityAdapter(activities)
                        }
                    }

                    override fun onFailure(call: Call<ActivityItem>, t: Throwable) { }
                })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        lastMenuItem = menu.findItem(R.id.random)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        lastMenuItem.isVisible = true
        supportActionBar?.title = item.title
        item.isVisible = false
        lastMenuItem = item
        getActivities(item.title.toString())

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.send_to_friend -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Send to Friend")
                builder.setMessage("Are you sure you want to send this to a friend?")
                builder.setPositiveButton("Yes") { _, _ ->
                    val position = (item.menuInfo as AdapterView.AdapterContextMenuInfo).position
                    activities[position].shared = true
                    activityView.setBackgroundColor(Color.CYAN)
                    (activityView.adapter as ActivityAdapter).notifyDataSetChanged()
                }
                builder.setNegativeButton("No") { _, _ -> }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
}
