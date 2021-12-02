package day1

fun numIncreases(depths: List<Int>, windowSize: Int): Int =
    depths.windowed(windowSize).zipWithNext().count { it.second.sum() > it.first.sum() }