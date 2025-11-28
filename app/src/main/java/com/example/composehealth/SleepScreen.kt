package com.example.composehealth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.format.DateTimeFormatter

@Composable
fun SleepScreen(
    mainViewModel: MainViewModel,
    onNavigateToSleepCalculator: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(.8f)
                .aspectRatio(1f)
                .wrapContentWidth()
                .wrapContentHeight()
        ) {
            Column {
                Text(
                    "10 ч\n12 мин\nсна",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    mainViewModel.currentDate.value = mainViewModel.currentDate.value.minusDays(1)
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    "prev day"
                )
            }

            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
            val formattedDate = mainViewModel.currentDate.collectAsState().value.format(formatter)
            Text(formattedDate, style = MaterialTheme.typography.titleLarge)

            IconButton(
                onClick = {
                    mainViewModel.currentDate.value = mainViewModel.currentDate.value.plusDays(1)
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    "next day"
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {}) {
                Text("Лечь спать")
            }

            Spacer(Modifier.padding(5.dp))

            Button(onClick = {}) {
                Text("Проснуться")
            }
        }

        Button(onClick = onNavigateToSleepCalculator) {
            Text("Калькулятор сна")
        }
    }
}