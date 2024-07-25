package com.example.doctordeni.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.doctordeni.ui.components.InformationCard
import com.example.doctordeni.ui.theme.DoctorDeniTheme


@Composable
fun ProfileScreen(
    firstName: String? = "First Name",
    lastName: String? = "Last Name",
    userName: String? = "User Name",
    email: String? = "Email",
    phone: String? = "Phone",
    postCode: String? = "Post Code",
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {


            Card {

//            AsyncImage(
//                model = "http://38.242.219.131:8000${imgUrl}",
//                contentDescription = "profile picture",
//                modifier = Modifier
//                    .padding(16.dp)
//                    .height(250.dp)
//                    .width(250.dp)
//                    .border(
//                        border = BorderStroke(
//                            2.dp,
//                            MaterialTheme.colorScheme.onSurface
//                        ), shape = RoundedCornerShape(8.dp)
//                    )
//                    .clickable(enabled = true, onClick = dialogClicker)
//                    .align(Alignment.CenterHorizontally),
//                contentScale = ContentScale.FillWidth,
//
//                )
                Image(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .padding(16.dp)
                        .height(250.dp)
                        .width(250.dp)
                        .border(
                            border = BorderStroke(
                                2.dp, MaterialTheme.colorScheme.onSurface
                            ), shape = RoundedCornerShape(8.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                )

                Column {
                    InformationCard(
                        "First Name", firstName, Icons.Filled.Person
                    )
                    InformationCard(
                        "Last Name", lastName, Icons.Filled.Person2
                    )
                    InformationCard(
                        "User Name", userName, Icons.Filled.Person3
                    )
                    InformationCard(
                        "Email", email, Icons.Filled.Email
                    )
                    InformationCard(
                        "Phone", phone, Icons.Filled.Phone
                    )
                    InformationCard(
                        "Post Code", postCode, Icons.Filled.ContactMail
                    )
                }
            }
        }
    }
}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun ProfileScreenPreview() {
    DoctorDeniTheme {
        ProfileScreen()
    }
}