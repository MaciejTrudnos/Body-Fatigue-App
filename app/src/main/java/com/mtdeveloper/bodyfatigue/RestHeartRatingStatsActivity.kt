package com.mtdeveloper.bodyfatigue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.mtdeveloper.bodyfatigue.database.AppDatabase
import java.time.format.DateTimeFormatter

class RestHeartRatingStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rest_heart_rating_stats)

        val actionBar = supportActionBar
        actionBar!!.title = "Wyniki pomiarów"
        actionBar!!.subtitle = "tętna spoczynkowego"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val restHeartRatingDao = db.restHeartRatingDao()
        val restHeartRatingList = restHeartRatingDao
            .getAll()
            .toList()

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        val dateRating = restHeartRatingList
            .map{it.createDate.format(dateFormat)}
            .toTypedArray()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Średnie tętno spoczynkowe")
            .categories(dateRating)
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
                    .name("Czas snu w minutach")
                    .data(restHeartRatingList.map{it.sleepTime}.toTypedArray()),
                AASeriesElement()
                    .name("Ocena")
                    .data(restHeartRatingList.map{it.rating}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val listViewRating = findViewById<ListView>(R.id.listViewRating)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dateRating)
        listViewRating.adapter = adapter

        listViewRating.setOnItemClickListener { parent, view, position, id ->
            val stats = restHeartRatingList[position]

            val heartRateStats = HeartRateStats()
            val sleepTime = heartRateStats
                .changeMinutesToTextTime(stats.sleepTime.toLong())

            val text =  "BPM podczas oceny: ${stats.currentBpm}" + "\n" +
                        "IBI podczas oceny: ${stats.currentIbi}" + "\n" +
                        "Średnie BPM: ${stats.averageBpm}" + "\n" +
                        "Średnie IBI: ${stats.averageIbi}" + "\n" +
                        "Czas snu: $sleepTime" + "\n" +
                        "Ocena: ${stats.rating}"

            val alertDialog = AlertDialog.Builder(this)
                .setTitle(stats.createDate.format(dateFormat))
                .setMessage(text)

            alertDialog.show()
        }
    }
}