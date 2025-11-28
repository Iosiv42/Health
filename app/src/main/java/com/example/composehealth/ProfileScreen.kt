package com.example.composehealth

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.composehealth.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

import com.example.composehealth.R

@Composable
fun ProfileScreen(
    userDataStoreManager: UserDataStoreManager
) {
    val weight = remember {
        mutableFloatStateOf(75f)
    }
    val height = remember {
        mutableIntStateOf(175)
    }
    val activity = remember {
        mutableStateOf(ActivityLevel.Moderate)
    }
    val age = remember {
        mutableIntStateOf(21)
    }
    val goal = remember {
        mutableStateOf(WeightGoal.Maintain)
    }
    val sex = remember {
        mutableStateOf(Sex.Male)
    }

    val weightDialogState = remember {
        mutableStateOf(false)
    }
    val heightDialogState = remember {
        mutableStateOf(false)
    }
    val activityDialogState = remember {
        mutableStateOf(false)
    }
    val ageDialogState = remember {
        mutableStateOf(false)
    }
    val goalDialogState = remember {
        mutableStateOf(false)
    }
    val sexDialogState = remember {
        mutableStateOf(false)
    }

    val entries = listOf(
        ProfileEntryData("Вес", "${weight.floatValue} кг", { weightDialogState.value = true }, R.drawable.weight_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
        ProfileEntryData("Рост", "${height.intValue} см", { heightDialogState.value = true }, R.drawable.straighten_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
        ProfileEntryData("Уровень активности", activity.value.desc, { activityDialogState.value = true }, R.drawable.directions_walk_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
        ProfileEntryData("Возраст", "${age.intValue} " + getAgeSuffix(age.intValue), { ageDialogState.value = true }, R.drawable.calendar_today_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
        ProfileEntryData("Цель", goal.value.desc, { goalDialogState.value = true }, R.drawable.flag_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
        ProfileEntryData("Пол", sex.value.desc, { sexDialogState.value = true }),
        ProfileEntryData("Унивеситет", "УУНиТ", {}, R.drawable.school_24dp_e3e3e3_fill0_wght400_grad0_opsz24)
    )

    LaunchedEffect(true) {
        userDataStoreManager.getPersonalData().collect { personalData ->
            weight.floatValue = personalData.weight
            height.intValue = personalData.height
            activity.value = personalData.activityLevel
            age.intValue = personalData.age
            goal.value = personalData.weightGoal
            sex.value = personalData.sex
        }
    }

    val coroutine = rememberCoroutineScope()


    if (weightDialogState.value) {
        DialogWeight(weightDialogState, userDataStoreManager, weight, coroutine = coroutine)
    }

    if (heightDialogState.value) {
        DialogHeight(heightDialogState, userDataStoreManager, height, coroutine = coroutine)
    }

    if (activityDialogState.value) {
        DialogActivityLevel(activityDialogState, userDataStoreManager, activity, coroutine = coroutine)
    }

    if (ageDialogState.value) {
        DialogAge(ageDialogState, userDataStoreManager, age, coroutine = coroutine)
    }

    if (goalDialogState.value) {
        DialogWeightGoal(goalDialogState, userDataStoreManager, goal, coroutine = coroutine)
    }

    if (sexDialogState.value) {
        DialogSex(sexDialogState, userDataStoreManager, sex, coroutine = coroutine)
    }

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Icon(
                    Icons.Rounded.AccountCircle,
                    "",
                    modifier = Modifier
                        .fillMaxWidth(.6f)
                        .aspectRatio(1f)
                )
                Text("Осипов Илья", style = MaterialTheme.typography.displaySmall)
                Spacer(modifier = Modifier.padding(20.dp))
            }

            items(entries) { dataEntry ->
                ProfileEntry(dataEntry)
            }
        }
    }
}

data class ProfileEntryData(
    val name: String,
    val desc: String,
    val da: () -> Unit,
    val iconResId: Int? = null,
    val icon: ImageVector? = null,
) {
    @Composable
    fun Icon() {
        return when {
            (icon != null) -> { Icon(icon, "") }
            (iconResId != null) -> { Icon(ImageVector.vectorResource(iconResId), "") }
            else -> { Icon(Icons.Default.Info, "") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogWeight(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldWeight: MutableState<Float>,
    coroutine: CoroutineScope
) {
    var fieldText by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                    oldWeight.value = fieldText.toFloat()
                    coroutine.launch {
                        Log.e("BBB", "weight $oldWeight")
                        userDataStoreManager.saveWeight(oldWeight.value)
                    }
                }
            ) {
                Text("Готово")
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Введите вес")
        },
        text = {
            OutlinedTextField(
                value = fieldText,
                onValueChange = {
                    fieldText = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogHeight(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldHeight: MutableState<Int>,
    coroutine: CoroutineScope
) {
    var fieldText by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                    oldHeight.value = fieldText.toInt()
                    coroutine.launch {
                        userDataStoreManager.saveHeight(oldHeight.value)
                    }
                }
            ) {
                Text("Готово")
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Введите рост")
        },
        text = {
            OutlinedTextField(
                value = fieldText,
                onValueChange = {
                    fieldText = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogActivityLevel(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldActivityLevel: MutableState<ActivityLevel>,
    coroutine: CoroutineScope
) {
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Выберите уровень активности")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ActivityLevel.entries) {
                    TextButton(
                        onClick = {
                            dialogState.value = false
                            oldActivityLevel.value = it
                            coroutine.launch {
                                userDataStoreManager.saveActivityLevel(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it.desc)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogAge(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldAge: MutableState<Int>,
    coroutine: CoroutineScope
) {
    var fieldText by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dialogState.value = false
                    oldAge.value = fieldText.toInt()
                    coroutine.launch {
                        userDataStoreManager.saveAge(oldAge.value)
                    }
                }
            ) {
                Text("Готово")
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Введите возраст")
        },
        text = {
            OutlinedTextField(
                value = fieldText,
                onValueChange = {
                    fieldText = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogWeightGoal(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldGoal: MutableState<WeightGoal>,
    coroutine: CoroutineScope
) {
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Выберите цель")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(WeightGoal.entries) {
                    TextButton(
                        onClick = {
                            dialogState.value = false
                            oldGoal.value = it
                            coroutine.launch {
                                userDataStoreManager.saveWeightGoal(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it.desc)
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogSex(
    dialogState: MutableState<Boolean>,
    userDataStoreManager: UserDataStoreManager,
    oldSex: MutableState<Sex>,
    coroutine: CoroutineScope
) {
    AlertDialog(
        onDismissRequest = {
            dialogState.value = false
        },
        confirmButton = {
        },
        dismissButton = {
            TextButton(onClick = { dialogState.value = false }) {
                Text("Отменить")
            }
        },
        title = {
            Text("Выберите уровень активности")
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(Sex.entries) {
                    TextButton(
                        onClick = {
                            dialogState.value = false
                            oldSex.value = it
                            coroutine.launch {
                                userDataStoreManager.saveSex(it)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(it.desc)
                    }
                }
            }
        }
    )
}

fun getAgeSuffix(age: Int): String {
    if (age.div(10) % 10 != 1) {
        if (age % 10 == 1) { return "год" }
        else if ((age % 10) in 2..4) { return "года" }
    }

    return "лет"
}

@Composable
fun ProfileEntry(
    entryData: ProfileEntryData,
) {
    Row(
        modifier = Modifier
            .clickable(onClick = entryData.da)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        entryData.Icon()

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start
        ) {
            Text(entryData.name, style = MaterialTheme.typography.titleMedium)
            Text(entryData.desc, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

//@Preview(showSystemUi = false)
//@Composable
//fun ProfileScreenPreview() {
//    AppTheme(darkTheme = true) {
//        ProfileScreen()
//    }
//}