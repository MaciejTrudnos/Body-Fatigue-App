package com.mtdeveloper.bodyfatigue

import android.content.DialogInterface
import android.content.Intent
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

class OrthostaticTestRatingStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orthostatic_test_rating_stats)

        val actionBar = supportActionBar
        actionBar!!.title = "Wyniki pomiarów"
        actionBar!!.subtitle = "testu ortostatycznego"

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "AppDatabase"
        ).allowMainThreadQueries().build()

        val orthostaticTestRatingDao = db.orthostaticTestRatingDao()

        val orthostaticTestResults = orthostaticTestRatingDao
            .getAll()
            .toList()

        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

        val dateRating = orthostaticTestResults
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
                    .name("Średnie BPM leżąc")
                    .data(orthostaticTestResults.map{it.averageLyingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne BPM leżąc")
                    .data(orthostaticTestResults.map{it.maxLyingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne BPM leżąc")
                    .data(orthostaticTestResults.map{it.minLyingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie BPM stojąc")
                    .data(orthostaticTestResults.map{it.averageStandingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne BPM stojąc")
                    .data(orthostaticTestResults.map{it.maxStandingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne BPM stojąc")
                    .data(orthostaticTestResults.map{it.minStandingBPM}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie IBI leżąc")
                    .data(orthostaticTestResults.map{it.averageLyingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne IBI leżąc")
                    .data(orthostaticTestResults.map{it.maxLyingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne IBI leżąc")
                    .data(orthostaticTestResults.map{it.minLyingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Średnie IBI stojąc")
                    .data(orthostaticTestResults.map{it.averageStandingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Maksymalne IBI stojąc")
                    .data(orthostaticTestResults.map{it.maxStandingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Minimalne IBI stojąc")
                    .data(orthostaticTestResults.map{it.minStandingIBI}.toTypedArray()),
                AASeriesElement()
                    .name("Różnica BPM między pozycjami")
                    .data(orthostaticTestResults.map{it.diffPositionBpm}.toTypedArray()),
                AASeriesElement()
                    .name("Różnica IBI między pozycjami")
                    .data(orthostaticTestResults.map{it.diffPositionIbi}.toTypedArray()),
                AASeriesElement()
                    .name("Ocena")
                    .data(orthostaticTestResults.map{it.rating}.toTypedArray())
            ))

        aaChartView.aa_drawChartWithChartModel(aaChartModel)

        val listViewRating = findViewById<ListView>(R.id.listViewOrthostaticTestRating)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dateRating)
        listViewRating.adapter = adapter

        listViewRating.setOnItemClickListener { parent, view, position, id ->
            val stats = orthostaticTestResults[position]

            val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

            val text =  "Średnie BMP leżąc: ${stats.averageLyingBPM}" + "\n" +
                    "Max BPM leżąc: ${stats.maxLyingBPM}" + "\n" +
                    "Min BPM leżąc: ${stats.maxLyingBPM}" + "\n" +
                    "Średnie BPM stojąc: ${stats.averageStandingBPM}" + "\n" +
                    "Max BPM stojąc: ${stats.maxStandingBPM}" + "\n" +
                    "Min BPM stojąc: ${stats.minStandingBPM}" + "\n" +
                    "Średnie IBI leżąc: ${stats.averageLyingIBI}" + "\n" +
                    "Max IBI leżąc: ${stats.maxLyingIBI}" + "\n" +
                    "Min IBI leżąc: ${stats.minLyingIBI}" + "\n" +
                    "Średnie IBI stojąc: ${stats.averageStandingIBI}" + "\n" +
                    "Max IBI stojąc: ${stats.maxStandingIBI}" + "\n" +
                    "Min IBI stojąc: ${stats.minStandingIBI}" + "\n" +
                    "Różnica BPM między pozycjami: ${stats.diffPositionBpm}" + "\n" +
                    "Różnica IBI między pozycjami: ${stats.diffPositionIbi}" + "\n" +
                    "Ocena: ${stats.rating}"

            val alertDialog = AlertDialog.Builder(this)
                .setTitle(stats.createDate.format(dateFormat))
                .setPositiveButton("Szczegóły"){dialogInterface, which ->
                    val orthostaticTestRatingIntent = Intent(this, OrthostaticTestRatingActivity::class.java)
                    orthostaticTestRatingIntent.putExtra("CreateDate", stats.createDate.toString())
                    orthostaticTestRatingIntent.putExtra("HideRating", true)
                    startActivity(orthostaticTestRatingIntent)
                }
                .setMessage(text)

            alertDialog.show()
        }
    }
}