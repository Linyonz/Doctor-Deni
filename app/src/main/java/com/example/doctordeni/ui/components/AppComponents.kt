package com.example.doctordeni.ui.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.doctordeni.R
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.vsnappy1.datepicker.DatePicker
import com.vsnappy1.datepicker.ui.model.DatePickerConfiguration

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value, modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp), style = TextStyle(
            fontSize = 24.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal
        ), color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value, modifier = Modifier
            .fillMaxWidth()
            .heightIn(), style = TextStyle(
            fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Normal
        ), color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTextFieldComponent(labelValue: String, icon: ImageVector, textValue: MutableState<String>) {

    OutlinedTextField(label = {
        Text(text = labelValue)
    }, value = textValue.value, onValueChange = {
        textValue.value = it
    }, colors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colorScheme.tertiary,
        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
        cursorColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.background,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface
    ), modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium, leadingIcon = {
        Icon(
            imageVector = icon, contentDescription = "profile"
        )
    }, keyboardOptions = KeyboardOptions.Default
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    icon: ImageVector,
    password: MutableState<String>
) {

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(label = {
        Text(text = labelValue)
    },
        value = password.value,
        onValueChange = {
            password.value = it
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.tertiary,
            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.background,
            focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon, contentDescription = "profile"
            )
        },
        trailingIcon = {
            val iconImage =
                if (isPasswordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
            val description = if (isPasswordVisible) "Show Password" else "Hide Password"
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun CheckboxComponent() {
    var isChecked by remember {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(56.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(checked = isChecked, onCheckedChange = {
            isChecked = it
        })
        ClickableTextComponent()
    }
}

@Composable
fun ClickableTextComponent() {
    val initialText = "By continuing you accept our "
    val privacyPolicyText = "Privacy Policy"
    val andText = " and "
    val termOfUseText = "Term of Use"

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append(initialText)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            pushStringAnnotation(tag = privacyPolicyText, annotation = privacyPolicyText)
            append(privacyPolicyText)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
            append(andText)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
            pushStringAnnotation(tag = termOfUseText, annotation = termOfUseText)
            append(termOfUseText)
        }
        append(".")
    }

    ClickableText(text = annotatedString, onClick = {
        annotatedString.getStringAnnotations(it, it).firstOrNull()?.also { annotation ->
                Log.d("ClickableTextComponent", "You have Clicked ${annotation.tag}")
            }
    })
}

@Composable
fun BottomComponent(
    textQuery: String,
    textClickable: String,
    action: String,
    navController: NavHostController,
    actionListener: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = actionListener,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            ), shape = RoundedCornerShape(50.dp)
                        )
                        .fillMaxWidth()
                        .heightIn(48.dp), contentAlignment = Alignment.Center
                ) {
                    Text(text = action, color = Color.White, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = "Or", modifier = Modifier.padding(10.dp), fontSize = 20.sp
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Transparent),
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color(android.graphics.Color.parseColor("#d2d2d2")),
//                            shape = RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.google_svg),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier
//                            .size(30.dp)
//                    )
//                }
//                Spacer(modifier = Modifier.width(10.dp))
//                Button(
//                    onClick = { /*TODO*/ },
//                    colors = ButtonDefaults.buttonColors(Color.Transparent),
//                    modifier = Modifier
//                        .padding(4.dp)
//                        .border(
//                            width = 2.dp,
//                            color = Color(android.graphics.Color.parseColor("#d2d2d2")),
//                            shape = RoundedCornerShape(20.dp)
//                        )
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.facebook_svg),
//                        contentDescription = "Google Logo",
//                        modifier = Modifier
//                            .size(30.dp)
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            AccountQueryComponent(textQuery, textClickable, navController)
        }
    }
}

@Composable
fun AccountQueryComponent(
    textQuery: String, textClickable: String, navController: NavHostController
) {
    val annonatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp
            )
        ) {
            append(textQuery)
        }
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.tertiary, fontSize = 15.sp)) {
            pushStringAnnotation(tag = textClickable, annotation = textClickable)
            append(textClickable)
        }
    }

    ClickableText(text = annonatedString, onClick = {
        annonatedString.getStringAnnotations(it, it).firstOrNull()?.also { annonation ->
                if (annonation.item == "Login") {
                    navController.navigate("Login")
                } else if (annonation.item == "Register") {
                    navController.navigate("Registration")
                }
            }
    })
}

@Composable
fun InformationCard(
    title: String? = "Name",
    undertone: String? = "Mia Johnson",
    imageVector: ImageVector? = Icons.Filled.Person
) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(2.dp)

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(8.dp),
                colors = IconButtonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.Black,
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Gray
                )
            ) {

                Icon(imageVector = imageVector!!, contentDescription = "silhouette")

            }

            Column(
                modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title!!, fontWeight = FontWeight.Bold
                )
                Text(
                    text = undertone ?: "null", fontSize = 12.sp
                )
            }

        }
    }
}

@Composable
@Preview(name = "mchana", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "usiku", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun InformationCardPreview() {
    DoctorDeniTheme {
        InformationCard()
    }
}

@Composable
fun DeniDate(
    dateTitle:String? = "LoanDate",
    id:Int = 1,
    onDateSelected: (Int, Int, Int) -> Unit,
) {

    Text(
        text = dateTitle ?: "Date",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
    DatePicker(
        onDateSelected = onDateSelected,
        configuration = DatePickerConfiguration.Builder().apply {
            dateTextStyle(textStyle = MaterialTheme.typography.bodySmall)
            selectedDateBackgroundColor(MaterialTheme.colorScheme.tertiary)
            headerTextStyle(textStyle = MaterialTheme.typography.headlineSmall)
            daysNameTextStyle(textStyle = MaterialTheme.typography.labelMedium)
            monthYearTextStyle(textStyle = MaterialTheme.typography.bodyMedium)
            selectedMonthYearAreaColor(color = MaterialTheme.colorScheme.tertiary)
            selectedDateTextStyle(TextStyle(color = MaterialTheme.colorScheme.onPrimary))
            headerArrowColor(MaterialTheme.colorScheme.tertiary)
            sundayTextColor(MaterialTheme.colorScheme.tertiary)

        }.build(),
        id = id

    )
}
@Composable
@Preview(name = "mchana", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "usiku", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PreviewDatePicker(){
    DoctorDeniTheme {
        DeniDate(
            "tarehe",
            1,
            { day, month, year -> Log.d("date", "$day-$month-$year") }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicSelectTextField(
    selectedValue: String,
    options: List<String>,
    label: String,
    onValueChangedEvent: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedValue,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = OutlinedTextFieldDefaults.colors(),
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option: String ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        expanded = false
                        onValueChangedEvent(option)
                    }
                )
            }
        }
    }
}

@Composable
@Preview(name = "mchana", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "usiku", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PreviewDynamicSelectTextField(){
    DoctorDeniTheme {
        DynamicSelectTextField(
            selectedValue = "Select",
            options = listOf("Option 1", "Option 2", "Option 3"),
            label = "Label",
            onValueChangedEvent = {}
        )
    }
}


@Composable
fun NoDataFoundCard() {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(500.dp),
        onClick = { /*TODO*/ }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(9f),
                resource = R.raw.nothing_found
            )
            Text(
                text = "No Data Found",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                fontWeight = FontWeight.ExtraBold,
            )
        }
    }
}

@Composable
@Preview(name = "mchana", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "usiku", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
private fun PreviewNoDataFound(){
    DoctorDeniTheme {
        NoDataFoundCard()
    }
}
