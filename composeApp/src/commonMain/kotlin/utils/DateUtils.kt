package utils

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class DateUtils {
    fun convertMillisToLocalDate(millis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(millis)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    }

    fun dateToString(date: LocalDate): String {
        val formatter = kotlinx.datetime.LocalDate
        val dayOfWeek = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
        return "$dayOfWeek, ${date.dayOfMonth} $month, ${date.year}"
    }
}