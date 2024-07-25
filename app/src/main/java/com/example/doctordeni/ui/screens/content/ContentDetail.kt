package com.example.doctordeni.ui.screens.content

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.doctordeni.models.states.ScreenStateEnum
import com.example.doctordeni.ui.components.StateScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode
import com.example.doctordeni.viewmodels.ContentViewModel
import com.github.javafaker.Faker
import kotlinx.coroutines.flow.collect
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent.inject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentDetail(){

    val contentViewModel = koinViewModel<ContentViewModel>()
    val faker:Faker by inject(Faker::class.java)
    contentViewModel.getSelectedArticle()
    val selected = contentViewModel.selected.collectAsState()
    val content = selected.value

    if(content== null){
        StateScreen(screenStateEnum = ScreenStateEnum.EMPTY)
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = content.title ?: faker.artist().name(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.padding(16.dp))
                AsyncImage(
                    model = content.urlToImage,
                    contentDescription = "article image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(
                            border = BorderStroke(
                                2.dp, MaterialTheme.colorScheme.onSurface
                            ), shape = RoundedCornerShape(8.dp)
                        ),
                    contentScale = ContentScale.FillWidth
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Published: ${MiscellaniousCode.calculateTimeAgo(content.publishedAt ?: "2024-03-27T11:00:00Z")}",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Author: ${content.author}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            item {
                Text(
                    text = content.content ?: faker.lorem().paragraph(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun ContentDetailPreview(){
    DoctorDeniTheme {
        ContentDetail()
    }
}