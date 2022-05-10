package com.mtdeveloper.bodyfatigue

import android.util.Log
import com.mtdeveloper.bodyfatigue.database.model.HeartRate
import com.mtdeveloper.bodyfatigue.database.model.SleepTime
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

class HeartRateStats {

    fun calculateHourlyAverageBpm(heartRate: List<HeartRate>) : List<Int> {
        val bpmStats : MutableList<Int> = ArrayList()
        var lastHour : Int = heartRate.first().createDate.hour
        var heartBeat = 0
        var heartBeatCount = 0

        heartRate.forEach {
            if (lastHour == it.createDate.hour){
                heartBeat += it.bpm
                heartBeatCount++
            } else {
                val avgBpm = heartBeat / heartBeatCount

                bpmStats.add(avgBpm)

                lastHour = it.createDate.hour
                heartBeat = it.bpm
                heartBeatCount = 1
            }
        }

        return bpmStats
    }

    fun calculateHourlyAverageIbi(heartRate: List<HeartRate>) : List<Int> {
        val ibiStats : MutableList<Int> = ArrayList()
        var lastHour : Int = heartRate.first().createDate.hour
        var heartBeat = 0
        var heartBeatCount = 0

        heartRate.forEach {
            if (lastHour == it.createDate.hour) {
                heartBeat += it.ibi
                heartBeatCount++
            } else {
                val avgBpm = heartBeat / heartBeatCount

                ibiStats.add(avgBpm)

                lastHour = it.createDate.hour
                heartBeat = it.ibi
                heartBeatCount = 1
            }
        }

        return ibiStats
    }

    fun calculateAverage(heartRateStats: List<Int>) : Int {
        val avgBpm = heartRateStats
            .average()
            .roundToInt()

        return avgBpm
    }

    fun getMax(heartRateStats: List<Int>) : Int {
        val max = heartRateStats
            .maxOrNull() ?: 0

        return max;
    }

    fun getMin(heartRateStats: List<Int>) : Int {
        val min = heartRateStats
            .minOrNull() ?: 0

        return min;
    }

    fun CalculateSleepTime(sleepTime : SleepTime) : Long {
        val startSleep = sleepTime.startSleep
        val stopSleep = sleepTime.stopSleep

        val minutesSleep = startSleep.until(stopSleep, ChronoUnit.MINUTES)

        return minutesSleep
    }

    fun calculateSleepTimeRelativePreviousNights(sleepTimeList: List<SleepTime>) : Long {
        val lastSleepTime = sleepTimeList
            .last()

        val minutesLastSleepTime = lastSleepTime.startSleep.until(lastSleepTime.stopSleep, ChronoUnit.MINUTES)

        var allSleepTimeMinutes : Long = 0
        var count = 0

        sleepTimeList.forEach {
            if (it.id == lastSleepTime.id) {
                return@forEach
            }

            val minutesSleep = it.startSleep.until(it.stopSleep, ChronoUnit.MINUTES)
            allSleepTimeMinutes += minutesSleep
            count++
        }

        val avgSleepTime = allSleepTimeMinutes / count

        val result = minutesLastSleepTime - avgSleepTime

        return result
    }

    fun calculateBpmRelativePreviousNights(heartRateList: List<HeartRate>, lastNightAvgBpm : Int, lastSleepTimeId : Int) : Int {
        val avgBpm = heartRateList
            .filter{x -> x.sleepTimeId != lastSleepTimeId}
            .map{it.bpm}
            .average()
            .roundToInt()

        val result = lastNightAvgBpm - avgBpm

        return result
    }

    fun calculateIbiRelativePreviousNights(heartRateList: List<HeartRate>, lastNightIbiBpm : Int, lastSleepTimeId : Int) : Int {
        val avgIbi = heartRateList
            .filter{x -> x.sleepTimeId != lastSleepTimeId}
            .map{it.ibi}
            .average()
            .roundToInt()

        val result = lastNightIbiBpm - avgIbi

        return result
    }
}