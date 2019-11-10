package org.aa2019.test

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import java.lang.Exception
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter

@Target(AnnotationTarget.FIELD)
annotation class JsonDate

@Target(AnnotationTarget.FIELD)
annotation class JsonDecimal

val dateConverter = object : Converter {
    override fun canConvert(cls: Class<*>) =
        cls == LocalDateTime::class.java

    override fun fromJson(jv: JsonValue) =
        if (jv.string != null) {
            LocalDateTime.parse(jv.string, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } else {
            throw Exception("Couldn't parse date: ${jv.string}")
        }

    override fun toJson(o: Any) =
        """ { "creationDate": $o } """
}

val decimalConverter = object : Converter {
    override fun canConvert(cls: Class<*>) =
        cls == BigDecimal::class.java

    override fun fromJson(jv: JsonValue) =
        if (jv.double != null) {
            BigDecimal(jv.double!!)
        } else {
            throw Exception("Couldn't parse decimal: ${jv.string}")
        }

    override fun toJson(o: Any) =
        """ { "unitPrice": $o } """
}

class OrdersAnalyzer {

    data class Order @JvmOverloads constructor(
        val orderId: Int,
        @JsonDate val creationDate: LocalDateTime,
        val orderLines: List<OrderLine>
    )

    data class OrderLine @JvmOverloads constructor(
        val productId: Int,
        val name: String,
        val quantity: Int,
        @JsonDecimal val unitPrice: BigDecimal
    )

    fun totalDailySales(orders: List<Order>): Map<DayOfWeek, Int> {
        val dailySales = HashMap<DayOfWeek, Int>().withDefault { 0 } // Map.getValue's default value will be 0
        for (order in orders) {
            for (orderLine in order.orderLines) {
                order.creationDate.dayOfWeek.let { key ->
                    dailySales[key] =
                        dailySales.getValue(key) + orderLine.quantity // Using Map.getValue for the 0 default value
                }
            }
        }
        return dailySales
    }
}