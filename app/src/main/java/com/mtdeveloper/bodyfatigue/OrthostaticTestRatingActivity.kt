package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import com.mtdeveloper.bodyfatigue.database.PositionTest
import com.mtdeveloper.bodyfatigue.database.model.OrthostaticTestRating
import kotlinx.android.synthetic.main.activity_orthostatic_test_rating.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OrthostaticTestRatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test_rating)

        val actionBar = supportActionBar
        actionBar!!.title = "Ocena zmęczenia"
        actionBar!!.subtitle = "testu ortostatycznego"

        val np = numberPicker
        np.minValue = 1
        np.maxValue = 4
        np.displayedValues = arrayOf("Wypoczęty","Przeciętnie wypoczęty","Zmęczony","Przemęczony")

        val receivedOrthostaticTestIntent = getIntent()

        val receivedCreateDate = receivedOrthostaticTestIntent
            .getStringExtra("CreateDate")

        val receivedHideRating = receivedOrthostaticTestIntent
            .getBooleanExtra("HideRating", false)

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val createDate = LocalDateTime.parse(receivedCreateDate)

        if (receivedHideRating == true) {
            actionBar!!.title = "Szczegóły z ${createDate.format(dateFormat)}"
            np.setVisibility(View.GONE)
            buttonSaveRatingOrthostaticTest.setVisibility(View.GONE)
            textView41.setVisibility(View.GONE)
        }

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val orthostaticTestRatingDao = db.orthostaticTestRatingDao()

        val orthostaticTestResults = db
            .orthostaticTestDao()
            .getByCreateDate(createDate)
            .toList()

        val bpmLying = orthostaticTestResults
            .filter { it.position == PositionTest.Lying}
            .map{it.bpm}
            .toList()

        val bpmStanding= orthostaticTestResults
            .filter { it.position == PositionTest.Standing}
            .map{it.bpm}
            .toList()

        val ibiLying = orthostaticTestResults
            .filter { it.position == PositionTest.Lying}
            .map{it.ibi}
            .toList()

        val ibiStanding = orthostaticTestResults
            .filter { it.position == PositionTest.Standing}
            .map{it.ibi}
            .toList()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Wyniki testu ortostatycznego")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM leżąc")
                    .data(bpmLying.toTypedArray()),
                AASeriesElement()
                    .name("BPM stojąc")
                    .data(bpmStanding.toTypedArray()),
                AASeriesElement()
                    .name("IBI leżąc")
                    .data(ibiLying.toTypedArray()),
                AASeriesElement()
                    .name("IBI stojąc")
                    .data(ibiStanding.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val heartRateStats = HeartRateStats()

        val averageLyingBPM = heartRateStats
            .calculateAverage(bpmLying)

        val maxLyingBPM = heartRateStats
            .getMax(bpmLying)

        val minLyingBPM = heartRateStats
            .getMin(bpmLying)

        val averageStandingBPM = heartRateStats
            .calculateAverage(bpmStanding)

        val maxStandingBPM = heartRateStats
            .getMax(bpmStanding)

        val minStandingBPM = heartRateStats
            .getMin(bpmStanding)

        val averageLyingIBI = heartRateStats
            .calculateAverage(ibiLying)

        val maxLyingIBI = heartRateStats
            .getMax(ibiLying)

        val minLyingIBI = heartRateStats
            .getMin(ibiLying)

        val averageStandingIBI = heartRateStats
            .calculateAverage(ibiStanding)

        val maxStandingIBI = heartRateStats
            .getMax(ibiStanding)

        val minStandingIBI = heartRateStats
            .getMin(ibiStanding)

        val diffPositionBpm = averageLyingBPM - averageStandingBPM
        val diffPositionIbi = averageLyingIBI - averageStandingIBI

        textViewAvgBpmLying.setText("${averageLyingBPM}")
        textViewMaxBpmLying.setText("${maxLyingBPM}")
        textViewMinBpmLying.setText("${minLyingBPM}")

        textViewAvgBpmStanding.setText("${averageStandingBPM}")
        textViewMaxBpmStanding.setText("${maxStandingBPM}")
        textViewMinBpmStanding.setText("${minStandingBPM}")

        textViewAvgIbiLying.setText("${averageLyingIBI}")
        textViewMaxIbiLying.setText("${maxLyingIBI}")
        textViewMinIbiLying.setText("${minLyingIBI}")

        textViewAvgIbiStanding.setText("${averageStandingIBI}")
        textViewMaxIbiStanding.setText("${maxStandingIBI}")
        textViewMinIbiStanding.setText("${minStandingIBI}")

        textViewDiffBpm.setText("${Math.abs(diffPositionBpm)}")
        textViewDiffIbi.setText("${Math.abs(diffPositionIbi)}")

        buttonSaveRatingOrthostaticTest.setOnClickListener {
            val command = OrthostaticTestRating(averageLyingBPM, maxLyingBPM, minLyingBPM, averageLyingIBI, maxLyingIBI, minLyingIBI, averageStandingBPM, maxStandingBPM, minStandingBPM, averageStandingIBI, maxStandingIBI, minStandingIBI, Math.abs(diffPositionBpm), Math.abs(diffPositionIbi), createDate, np.value)

            orthostaticTestRatingDao.insert(command)

            Toast.makeText(this, "Dodano ocenę", Toast.LENGTH_SHORT)
                .show()

            buttonSaveRatingOrthostaticTest.setEnabled(false)
        }
    }
}