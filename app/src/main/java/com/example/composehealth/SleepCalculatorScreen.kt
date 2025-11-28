package com.example.composehealth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composehealth.ui.theme.AppTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SleepCalculatorScreen() {
    var selectedIdx by remember { mutableIntStateOf(0) }
    var hourText by remember {  mutableStateOf("8") }
    var minuteText by remember {  mutableStateOf("00") }
    var cycleCountText by remember {  mutableStateOf("5") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text("Я хочу")

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selectedIdx == 0, onClick = { selectedIdx = 0 })
                Text("лечь спать в")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = selectedIdx == 1, onClick = { selectedIdx = 1 })
                Text("проснуться в")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                value = hourText,
                onValueChange = { hourText = it },
                label = { Text("Часы") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(.5f)
            )
            OutlinedTextField(
                value = minuteText,
                onValueChange = { minuteText = it },
                label = { Text("Минуты") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(.5f)
            )

        }
        TextButton(onClick = {
            hourText = LocalTime.now().hour.toString()
            minuteText = LocalTime.now().minute.toString()
        }) {
            Text("Сейчас")
        }
        TextButton(onClick = {
        }) {
            Text("Выбрать пару как время")
        }

        OutlinedTextField(
            value = cycleCountText,
            onValueChange = { cycleCountText = it },
            label = { Text("Количество циклов") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )

        var result by remember { mutableStateOf("") }

        Button(
            onClick = {
                val time = LocalTime.of(hourText.toInt(), minuteText.toInt())
                when (selectedIdx) {
                    0 -> {
                        result = calculateWakeUpTime(time, cycleCountText.toInt())
                            .format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                    1 -> {
                        result = calculateBedtime(time, cycleCountText.toInt())
                            .format(DateTimeFormatter.ofPattern("HH:mm"))
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Посчитать")
        }

        if (!result.isBlank()) {
            Text(result)
        }

        Button(onClick = {}) {
            Text("Поставить будильник на это время")
        }
    }
}

/**
 * Calculates the optimal bedtime based on desired wake-up time and sleep cycles
 * @param wakeUpTime The time you want to wake up
 * @param sleepCycles Number of 90-minute sleep cycles you want (typically 4-6 cycles)
 * @return The time you should go to bed
 */
fun calculateBedtime(wakeUpTime: LocalTime, sleepCycles: Int): LocalTime {
    // Each sleep cycle is approximately 90 minutes
    val cycleDurationMinutes = 90L
    val totalMinutes = sleepCycles * cycleDurationMinutes

    // Subtract total sleep time from wake-up time
    return wakeUpTime.minusMinutes(totalMinutes)
}

/**
 * Calculates when you should wake up based on bedtime and sleep cycles
 * @param bedtime The time you go to sleep
 * @param sleepCycles Number of 90-minute sleep cycles you want (typically 4-6 cycles)
 * @return The time you should wake up
 */
fun calculateWakeUpTime(bedtime: LocalTime, sleepCycles: Int): LocalTime {
    // Each sleep cycle is approximately 90 minutes
    val cycleDurationMinutes = 90L
    val totalMinutes = sleepCycles * cycleDurationMinutes

    // Add total sleep time to bedtime
    return bedtime.plusMinutes(totalMinutes)
}

@Preview(showSystemUi = true)
@Composable
fun SleepCalculatorScreenPreview() {
    AppTheme {
        SleepCalculatorScreen()
    }
}