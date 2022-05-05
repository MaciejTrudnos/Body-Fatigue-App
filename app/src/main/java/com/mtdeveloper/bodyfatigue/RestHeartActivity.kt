package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import kotlinx.android.synthetic.main.activity_rest_heart.*

class RestHeartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val lastSleepTime = db
            .sleepTimeDao()
            .getLastSleepTime()

        val heartRateList = db
            .heartRateDao()
            .getLastSleepHeartRate(lastSleepTime.id)
            .toList()

        val heartRateStats = HeartRateStats()

        val bpmHourlyStats = heartRateStats
            .calculateHourlyAverageBpm(heartRateList)

        val ibiHourlyStats = heartRateStats
            .calculateHourlyAverageIbi(heartRateList)

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Średnie tętno spoczynkowe")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM")
                    .data(bpmHourlyStats.map{it}.toTypedArray()),
                AASeriesElement()
                    .name("IBI")
                    .data(ibiHourlyStats.map{it}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val avgBpm = heartRateStats
            .calculateAverage(bpmHourlyStats)

        textViewAvgBpm.setText("${avgBpm}")

        val maxBpm = heartRateStats
            .getMax(bpmHourlyStats)

        textViewMaxBpm.setText("${maxBpm}")

        val minBpm = heartRateStats
            .getMin(bpmHourlyStats)

        textViewMinBpm.setText("${minBpm}")

        val avgIbi = heartRateStats
            .calculateAverage(ibiHourlyStats)

        textViewAvgIbi.setText("${avgIbi}")

        val maxIbi = heartRateStats
            .getMax(ibiHourlyStats)

        textViewMaxIbi.setText("${maxIbi}")

        val minIbi = heartRateStats
            .getMin(ibiHourlyStats)

        textViewMinIbi.setText("${minIbi}")

        val sleepTime = heartRateStats.CalculateSleepTime(lastSleepTime)

        textViewSleepTime.setText(sleepTime)
    }
}