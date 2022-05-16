package com.mtdeveloper.bodyfatigue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

        val actionBar = supportActionBar
        actionBar!!.title = "Analiza tętna spoczynkowego"

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

        val allSleepTime = db
            .sleepTimeDao()
            .getAll()
            .toList()

        val allHeartRate = db
            .heartRateDao()
            .getAll()
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
        val sleepTimeText = heartRateStats.changeMinutesToTextTime(sleepTime)
        textViewSleepTime.setText(sleepTimeText)

        val diffTime = heartRateStats.calculateSleepTimeRelativePreviousNights(allSleepTime)
        val relativePreviousNightsText = prepareRelativePreviousNightsText(diffTime)

        textViewRelativeSleepTime.setText(relativePreviousNightsText)

        val bpmRelativePreviousNights = heartRateStats.calculateBpmRelativePreviousNights(allHeartRate, avgBpm, lastSleepTime.id)
        val bpmRelativePreviousNightsText = prepareBpmRelativePreviousNightsText(bpmRelativePreviousNights)

        textViewRelativeBpm.setText(bpmRelativePreviousNightsText)

        val ibiRelativePreviousNights = heartRateStats.calculateIbiRelativePreviousNights(allHeartRate, avgIbi, lastSleepTime.id)
        val ibiRelativePreviousNightsText = prepareIbiRelativePreviousNightsText(ibiRelativePreviousNights)

        textViewRelativeIbi.setText(ibiRelativePreviousNightsText)

        buttonRestHeartRating.setOnClickListener {
            val restHeartRatingDao = db.restHeartRatingDao()
            val isRatingExists = restHeartRatingDao.isRatingExists(lastSleepTime.id)

            if (isRatingExists) {
                Toast.makeText(this, "Ocena została już dodana", Toast.LENGTH_SHORT)
                    .show()

                return@setOnClickListener
            }

            val restHeartRatingIntent = Intent(this, RestHeartRatingActivity::class.java)
            restHeartRatingIntent.putExtra("CurrentBPM", 0)
            restHeartRatingIntent.putExtra("CurrentIBI", 0)
            restHeartRatingIntent.putExtra("AverageBPM", avgBpm)
            restHeartRatingIntent.putExtra("AverageIBI", avgIbi)
            restHeartRatingIntent.putExtra("SleepTime", sleepTime)
            restHeartRatingIntent.putExtra("SleepTimeId", lastSleepTime.id)
            startActivity(restHeartRatingIntent)
        }

        buttonRestHeartRatingStats.setOnClickListener {
            val restHeartRatingStatsIntent = Intent(this, RestHeartRatingStatsActivity::class.java)
            startActivity(restHeartRatingStatsIntent)
        }
    }

    private fun prepareRelativePreviousNightsText(diffTime : Long) : String {
        if (diffTime > 0) {
            val hour = diffTime / 60
            val min = diffTime % 60

            return "dłuższy o " + String.format("%d g %02d min", hour, min)

        }else{
            val hour = Math.abs(diffTime) / 60
            val min = Math.abs(diffTime) % 60

            return "krótszy o " + String.format("%d g %02d min", hour, min)
        }
    }

    private fun prepareBpmRelativePreviousNightsText(bpm : Int) : String {
        if (bpm > 0) {
            return "wyższe o ${bpm} bpm"

        } else {
            return "niższe o ${Math.abs(bpm)} bpm"
        }
    }

    private fun prepareIbiRelativePreviousNightsText(ibi : Int) : String {
        if (ibi > 0) {
            return "wyższe o ${ibi} ms"

        } else {
            return "niższe o ${Math.abs(ibi)} ms"
        }
    }
}