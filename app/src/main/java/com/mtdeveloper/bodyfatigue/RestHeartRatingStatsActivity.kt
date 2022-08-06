package com.mtdeveloper.bodyfatigue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        val restHeartRatingResults = restHeartRatingDao
            .getAll()
            .toList()

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        val dateRating = restHeartRatingResults
            .map{it.createDate.format(dateFormat)}
            .toTypedArray()

        val aaChartView = findViewById<AAChartView>(R.id.aa_chart_view)
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("Wyniki pomiarów")
            .categories(dateRating)
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Średnie BPM")
                    .data(restHeartRatingResults.map{it.averageBpm}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne BPM")
                    .data(restHeartRatingResults.map{it.maxBpm}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne BPM")
                    .data(restHeartRatingResults.map{it.minBpm}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie IBI")
                    .data(restHeartRatingResults.map{it.averageIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne IBI")
                    .data(restHeartRatingResults.map{it.maxIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne IBI")
                    .data(restHeartRatingResults.map{it.minIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Ocena")
                    .data(restHeartRatingResults.map{it.rating}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val listViewRating = findViewById<ListView>(R.id.listViewRating)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dateRating)
        listViewRating.adapter = adapter

        listViewRating.setOnItemClickListener { parent, view, position, id ->
            val stats = restHeartRatingResults[position]

            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            val text =  "Średnie BMP: ${stats.averageBpm}" + "\n" +
                        "Max BPM: ${stats.maxBpm}" + "\n" +
                        "Min BPM: ${stats.minBpm}" + "\n" +
                        "Średnie IBI: ${stats.averageIbi}" + "\n" +
                        "Max IBI: ${stats.maxIbi}" + "\n" +
                        "Min IBI: ${stats.minIbi}" + "\n" +
                        "Ocena: ${stats.rating}"

            val alertDialog = AlertDialog.Builder(this)
                .setTitle(stats.createDate.format(dateFormat))
                .setPositiveButton("Szczegóły"){dialogInterface, which ->
                    val restHeartRatingActivityIntent = Intent(this, RestHeartRatingActivity::class.java)
                    restHeartRatingActivityIntent.putExtra("CreateDate", stats.createDate.toString())
                    restHeartRatingActivityIntent.putExtra("HideRating", true)
                    startActivity(restHeartRatingActivityIntent)
                }
                .setMessage(text)

            alertDialog.show()
        }
    }
}