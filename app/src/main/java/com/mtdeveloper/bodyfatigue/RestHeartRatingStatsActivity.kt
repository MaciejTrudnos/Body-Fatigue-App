package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase

class RestHeartRatingStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart_rating_stats)

        val actionBar = supportActionBar
        actionBar!!.title = "Oceny zmęczenia"
        actionBar!!.subtitle = "tętna spoczynkowego"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val restHeartRatingDao = db.restHeartRatingDao()
        val restHeartRatingList = restHeartRatingDao
            .getAll()
            .toList()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Średnie tętno spoczynkowe")
            .categories(restHeartRatingList.map{it.createDate.toString()}.toTypedArray())
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("BPM podczas oceny")
                    .data(restHeartRatingList.map{it.currentBpm}.toTypedArray()),
                AASeriesElement()
                    .name("IBI podczas oceny")
                    .data(restHeartRatingList.map{it.currentIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie BPM")
                    .data(restHeartRatingList.map{it.averageBpm}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie IBI")
                    .data(restHeartRatingList.map{it.averageIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Czas snu")
                    .data(restHeartRatingList.map{it.sleepTime}.toTypedArray()),
                AASeriesElement()
                    .name("Ocena")
                    .data(restHeartRatingList.map{it.rating}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)


    }
}