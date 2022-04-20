package com.mtdeveloper.bodyfatigue

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

class RestHeartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val heartRateDao = db.heartRateDao()

        var result = heartRateDao
            .getAll()
            .take(24)
            .filterNotNull()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Pomiar tętna spoczynkowego")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM")
                    .data(result.map{it.bpm}.toTypedArray()),
                AASeriesElement()
                    .name("IBI")
                    .data(result.map{it.ibi}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
}