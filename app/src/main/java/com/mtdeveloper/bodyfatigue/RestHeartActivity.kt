package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.dao.HeartRateDao
import com.mtdeveloper.bodyfatigue.database.dao.SleepTimeDao
import kotlinx.android.synthetic.main.activity_rest_heart.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RestHeartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val sleepTimeId = db
            .sleepTimeDao()
            .getLastSleepTime()
            .id

        Log.i("dart","sleepTimeId: ${sleepTimeId}")

        val heartRateDao = db
            .heartRateDao()
            .getLastSleepHeartRate(sleepTimeId)
            .toList()

        Log.i("dart","heartRateDao: ${heartRateDao.count()}")

        val heartRateStats = HeartRateStats()

        val bpmStats = heartRateStats.CalculateHourlyAverageBpm(heartRateDao)
        val ibiStats = heartRateStats.CalculateHourlyAverageIbi(heartRateDao)

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Pomiar tętna spoczynkowego")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM")
                    .data(bpmStats.map{it}.toTypedArray()),
                AASeriesElement()
                    .name("IBI")
                    .data(ibiStats.map{it}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)
    }
}