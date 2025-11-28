package com.example.composehealth

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: LocalDate?): Long? {
        return date
            ?.atStartOfDay(ZoneOffset.systemDefault())
            ?.toEpochSecond()
    }

    @TypeConverter
    fun toDate(timestamp: Long?): LocalDate? {
        return timestamp?.let {
            Instant.ofEpochSecond(it)
                .atZone(ZoneOffset.systemDefault())
                .toLocalDate()
        }
    }
}