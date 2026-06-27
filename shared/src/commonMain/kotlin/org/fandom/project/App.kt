package org.fandom.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme {
        // UI State management for tabs
        var selectedTabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Titles", "Images")

        var articles by remember{ mutableStateOf<List<Article>>(emptyList())}
        val apiClient = remember { ApiClient() }

        //Trigger the API call only once when the composable enters the composition
        LaunchedEffect(Unit){
            articles = apiClient.fetchArticles()
        }

        Scaffold(
            topBar = {
                TopAppBar(
                title={Text(text=tabs[selectedTabIndex])},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff2d4c7a7),
                    titleContentColor = Color(0xff000000),
                )
                )
            }
        ){ paddingValues ->
            Column ( modifier = Modifier.padding(paddingValues)){
                PrimaryTabRow(selectedTabIndex = selectedTabIndex){
                    tabs.forEachIndexed{ index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = {selectedTabIndex= index},
                            text = {Text(title)}
                        )
                    }
                }

                // Show a loading indicator while data is being fetched
                if (articles.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }  else {
                        // Route to the appropriate screen based on the selected tab
                        when(selectedTabIndex){
                            0 -> TitlesScreen(articles=articles)
                            1 -> ImagesScreen(articles=articles)
                            }
                        }
                }
                }
            }
        }


@Composable
fun TitlesScreen(articles : List<Article>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        items(articles){
            article ->
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp)){
                Text(
                    text= article.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.TopStart).padding(end=60.dp))
                Text(
                    text = article.communityName,
                    color= Color.Gray,
                    style= MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.BottomEnd).padding(top=32.dp))
            }
                HorizontalDivider(color= Color.LightGray.copy(alpha=0.5f))
        }
    }
}

@Composable
fun ImagesScreen(articles:List<Article>){
    //Keep only the items where the imageUrl is NOT null
    val filteredArticles = articles.filter { article -> article.imageUrl != null }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ){
        items(filteredArticles){ article ->

                KamelImage(
                    resource = asyncPainterResource(data= article.imageUrl!!),
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.padding(8.dp).aspectRatio(1f)
                )

            }
    }
}