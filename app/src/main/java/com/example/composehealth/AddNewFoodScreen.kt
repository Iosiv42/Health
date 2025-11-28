package com.example.composehealth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddNewFoodScreen(
    mainViewModel: MainViewModel,
    onComplete: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        var foodName by remember { mutableStateOf("") }
        var proteinText by remember { mutableStateOf("") }
        var fatText by remember { mutableStateOf("") }
        var carbsText by remember { mutableStateOf("") }
        var caloriesText by remember { mutableStateOf("") }

        Icon(
            ImageVector.vectorResource(R.drawable.nutrition_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
            "",
            modifier = Modifier
                .fillMaxWidth(.4f)
                .aspectRatio(1f)
        )

        OutlinedTextField(
            value = foodName,
            onValueChange = { foodName = it },
            label = { Text("Название продукта") },
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = proteinText,
            onValueChange = { proteinText = it },
            label = { Text("Белки / 100г") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            suffix = { Text("г") }
        )

        OutlinedTextField(
            value = fatText,
            onValueChange = { fatText = it },
            label = { Text("Жиры / 100г") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            suffix = { Text("г") }
        )

        OutlinedTextField(
            value = carbsText,
            onValueChange = { carbsText = it },
            label = { Text("Углеводы / 100г") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            suffix = { Text("г") }
        )

        OutlinedTextField(
            value = caloriesText,
            onValueChange = { caloriesText = it },
            label = { Text("ккал / 100г") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            suffix = { Text("ккал") }
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    try {
                        mainViewModel.addFoodNutrition(FoodNutrition(
                            foodName,
                            proteinText.toFloat(),
                            fatText.toFloat(),
                            carbsText.toFloat()
                        ))

                        onComplete()
                    } catch (e: Exception) { }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Add, "")
                    Text("Добавить")
                }
            }
        }
    }
}