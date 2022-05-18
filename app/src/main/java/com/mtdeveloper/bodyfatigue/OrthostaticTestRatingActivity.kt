package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import kotlinx.android.synthetic.main.activity_rest_heart_rating.*
import java.time.LocalDateTime

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
        np.displayedValues = arrayOf("Wypoczęty","Przeciętnie wypoczęty","Duże zmęczenie","Przemęczenie")

        val receivedOrthostaticTestIntent = getIntent()

        val receivedCreateDate = receivedOrthostaticTestIntent
            .getStringExtra("CreateDate")

        val createDate = LocalDateTime.parse(receivedCreateDate)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val orthostaticTestAll = db
            .orthostaticTestDao()
            .getByCreateDate(createDate)
            .toList()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Wyniki testu ortostatycznego")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM")
                    .data(orthostaticTestAll.map{it.bpm}.toTypedArray()),
                AASeriesElement()
                    .name("IBI")
                    .data(orthostaticTestAll.map{it.ibi}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

    }
}