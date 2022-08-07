package com.mtdeveloper.bodyfatigue

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.model.RestHeartRating
import kotlinx.android.synthetic.main.activity_orthostatic_test_rating.*
import kotlinx.android.synthetic.main.activity_rest_heart_rating.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class RestHeartRatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart_rating)

        val actionBar = supportActionBar
        actionBar!!.title = "Ocena zmęczenia"
        actionBar!!.subtitle = "tętna spoczynkowego"

        val np = numberPickerRH
        np.minValue = 1
        np.maxValue = 4
        np.displayedValues = arrayOf("Wypoczęty","Przeciętnie wypoczęty","Zmęczony","Przemęczony")

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val heartRateDao = db.heartRateDao()
        val restHeartRatingDao = db.restHeartRatingDao()

        val receivedRestHeartRatingIntent = getIntent()

        val receivedCreateDate = receivedRestHeartRatingIntent
            .getStringExtra("CreateDate")

        val receivedHideRating = receivedRestHeartRatingIntent
            .getBooleanExtra("HideRating", false)

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val createDate = LocalDateTime.parse(receivedCreateDate)

        if (receivedHideRating == true) {
            actionBar!!.title = "Szczegóły z ${createDate.format(dateFormat)}"
            np.setVisibility(View.GONE)
            buttonRestHeartRatingStats.setVisibility(View.GONE)
            textView15.setVisibility(View.GONE)
        }

        val heartRateResults = heartRateDao
            .getByCreateDate(createDate)
            .toList()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Wyniki tętna spoczynkowego")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM")
                    .data(heartRateResults.map{it.bpm}.toTypedArray()),
                AASeriesElement()
                    .name("IBI")
                    .data(heartRateResults.map{it.ibi}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val heartRateStats = HeartRateStats()

        val bpm = heartRateResults
            .map{it.bpm}
            .toList()

        val averageBPM = heartRateStats
            .calculateAverage(bpm)

        val maxBPM = heartRateStats
            .getMax(bpm)

        val minBPM = heartRateStats
            .getMin(bpm)

        val ibi = heartRateResults
            .map{it.ibi}
            .toList()

        val averageIBI = heartRateStats
            .calculateAverage(ibi)

        val maxIBI = heartRateStats
            .getMax(ibi)

        val minIBI = heartRateStats
            .getMin(ibi)

        textViewAvgBpm.setText("${averageBPM}")
        textViewMaxBpm.setText("${maxBPM}")
        textViewMinBpm.setText("${minBPM}")
        textViewAvgIbi.setText("${averageIBI}")
        textViewMaxIbi.setText("${maxIBI}")
        textViewMinIbi.setText("${minIBI}")

        buttonRestHeartRatingStats.setOnClickListener {
            val command = RestHeartRating(averageBPM, maxBPM, minBPM, averageIBI, maxIBI, minIBI, createDate, np.value )

            restHeartRatingDao.insert(command)

            Toast.makeText(this, "Dodano ocenę", Toast.LENGTH_SHORT)
                .show()

            buttonRestHeartRatingStats.setEnabled(false)
        }
    }
}