package com.mtdeveloper.bodyfatigue

import com.mtdeveloper.bodyfatigue.database.model.HeartRate

class HeartRateStats {

    fun CalculateHourlyAverageBpm(heartRate: List<HeartRate>) : List<Int> {
        val bpmStats : MutableList<Int> = ArrayList()
        var lastHour : Int = heartRate.first().createDate.hour
        var heartBeat = 0
        var heartBeatCount = 0

        heartRate.forEach {
            if(lastHour == it.createDate.hour){
                heartBeat += it.bpm
                heartBeatCount++
            }else {
                val avgBpm = heartBeat / heartBeatCount

                bpmStats.add(avgBpm)

                lastHour = it.createDate.hour
                heartBeat = it.bpm
                heartBeatCount = 1
            }
        }

        return bpmStats
    }

    fun CalculateHourlyAverageIbi(heartRate: List<HeartRate>) : List<Int> {
        val ibiStats : MutableList<Int> = ArrayList()
        var lastHour : Int = heartRate.first().createDate.hour
        var heartBeat = 0
        var heartBeatCount = 0

        heartRate.forEach {
            if(lastHour == it.createDate.hour){
                heartBeat += it.ibi
                heartBeatCount++
            }else {
                val avgBpm = heartBeat / heartBeatCount

                ibiStats.add(avgBpm)

                lastHour = it.createDate.hour
                heartBeat = it.ibi
                heartBeatCount = 1
            }
        }

        return ibiStats
    }
}