package com.mtdeveloper.bodyfatigue

import kotlin.math.roundToInt

class HeartRateStats {

    fun calculateAverage(data: List<Int>) : Int {
        val result = data
            .average()
            .roundToInt()

        return result
    }

    fun getMax(data: List<Int>) : Int {
        val result = data
            .maxOrNull() ?: 0

        return result;
    }

    fun getMin(data: List<Int>) : Int {
        val result = data
            .minOrNull() ?: 0

        return result;
    }
}