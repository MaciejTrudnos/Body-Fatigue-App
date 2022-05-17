package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OrthostaticTestRatingStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test_rating_stats)

        val actionBar = supportActionBar
        actionBar!!.title = "Wyniki pomiarów"
        actionBar!!.subtitle = "testu ortostatycznego"
    }
}