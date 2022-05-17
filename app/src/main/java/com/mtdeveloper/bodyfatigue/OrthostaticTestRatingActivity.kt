package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OrthostaticTestRatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test_rating)

        val actionBar = supportActionBar
        actionBar!!.title = "Ocena zmęczenia"
        actionBar!!.subtitle = "testu ortostatycznego"
    }
}