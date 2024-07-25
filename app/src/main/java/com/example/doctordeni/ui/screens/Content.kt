package com.example.doctordeni.ui.screens

import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.doctordeni.models.states.ScreenStateEnum
import com.example.doctordeni.navigation.BaseNav
import com.example.doctordeni.ui.components.StateScreen
import com.example.doctordeni.ui.theme.DoctorDeniTheme
import com.example.doctordeni.utils.MiscellaniousCode.calculateTimeAgo
import com.example.doctordeni.viewmodels.ContentViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(
    navController: NavController? = null
) {

    val context = LocalContext.current
    val contentViewModel = koinViewModel<ContentViewModel>()

    val errorState = contentViewModel.dbError.collectAsState()
    val contentState = contentViewModel.content.collectAsState()

    if (errorState.value != null) {
        StateScreen(screenStateEnum = ScreenStateEnum.ERROR)
    }


    if (contentState.value != null) {
        if (contentState.value!!.isEmpty()) {
            StateScreen(screenStateEnum = ScreenStateEnum.EMPTY)
        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(contentState.value!!) {
                    ContentItem(
                        urlToImage = it?.urlToImage,
                        title = it?.title,
                        description = it?.description,
                        publishedAt = it?.publishedAt,
                        author = it?.author,
                        clickArticle = {
                            Log.d("ContentViewModel", "selectedArticle: ${it}")
                            contentViewModel.selectArticle(it!!)
                            navController?.navigate(BaseNav.ContentDetail.route)
                        }
                    )
                }
            }
        }
    }

}

@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun ContentPreview() {
    DoctorDeniTheme {
        Content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentItem(
    urlToImage: String? = null,
    title: String? = null,
    description: String? = null,
    publishedAt: String? = null,
    author: String? = null,
    clickArticle: (() -> Unit)
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = clickArticle, enabled = true),
        shape = RoundedCornerShape(8.dp)
    ) {
        AsyncImage(
            model = urlToImage,
            contentDescription = "article image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(
                    border = BorderStroke(
                        2.dp, MaterialTheme.colorScheme.onSurface
                    ), shape = RoundedCornerShape(8.dp)
                )
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth
        )

        Text(
            text = title ?: "title",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
            maxLines = 2,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = description ?: "description",
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 4.dp),
            maxLines = 5,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (publishedAt != null) {
                Text(
                    text = "Published: ${calculateTimeAgo(publishedAt)}",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Published: Unknown",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
            if (author != null) {
                Text(
                    text = "Author: ${author}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Author: Unknown",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )
            }
        }

    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Preview(name = "light", uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
fun PreviewContentItem() {
    DoctorDeniTheme {
        ContentItem(){
            Log.e("CntnClick","click")
        }
    }
}