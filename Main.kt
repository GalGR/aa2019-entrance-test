package org.aa2019.test

import com.beust.klaxon.*
import java.io.File
import java.time.DayOfWeek
import kotlin.system.exitProcess

private val errln: (String) -> Unit = System.err::println

fun outputMapToJsonString(outputMap: Map<DayOfWeek, Int>): String {
    var str = "{"
    if (outputMap.isNotEmpty()) {
        for (day in DayOfWeek.values()) {
            if (outputMap[day] != null) {
                str += "\n  \"DayOfWeek.$day\": ${outputMap[day]},"
            }
        }
        str = str.substring(startIndex = 0, endIndex = str.length - 1) // Remove the last ','
    }
    str += "\n}"
    return str
}

fun printHelp() {
    println(
        """
        |program-name  INPUT_JSON  <OUTPUT_JSON>
        |
        |If no output file argument is given, or '-' is given, the result will be printed to stdout instead.
        |""".trimMargin()
    )
}

fun main(args: Array<String>) {
    var inputName: String
    var outputName: String?

    for (arg in args) {
        if (arg.toLowerCase() in listOf("-help", "-h", "--help", "help", "/?")) {
            printHelp()
            return
        }
    }
    when (args.size) {
        0 -> {
            printHelp()
            return
        }
        1 -> {
            inputName = args[0]
            outputName = null
        }
        2 -> {
            inputName = args[0]
            if (args[1] == "-") outputName = null
            else outputName = args[1]
        }
        else -> {
            errln("Invalid number of arguments")
            exitProcess(1)
        }
    }

    var inputString: String
    try {
        val bufferedReader = File(inputName).bufferedReader()
        inputString = bufferedReader.use { it.readText() }
    } catch (e: Throwable) {
        errln("Couldn't read from file $inputName: ${e.message}")
        exitProcess(1)
    }
    var ordersList: List<OrdersAnalyzer.Order>?
    try {
        ordersList = Klaxon()
            .fieldConverter(JsonDate::class, dateConverter)
            .fieldConverter(JsonDecimal::class, decimalConverter)
            .parseArray<OrdersAnalyzer.Order>(inputString)
    }
    catch (e: Throwable) {
        errln("Couldn't parse the JSON file $inputName: ${e.message}")
        exitProcess(1)
    }
    if (ordersList == null) {
        errln("Couldn't parse the JSON file $inputName")
        exitProcess(1)
    }

    val outputMap = OrdersAnalyzer().totalDailySales(ordersList)

    val outputString = outputMapToJsonString(outputMap)
    if (outputName != null) {
        try { File(outputName).writeText(outputString) }
        catch (e: Throwable) { errln("Couldn't write to file $outputName: ${e.message}") }
    } else {
        println(outputString)
    }
}
