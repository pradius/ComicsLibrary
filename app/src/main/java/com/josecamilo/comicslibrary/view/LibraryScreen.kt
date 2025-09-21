package com.josecamilo.comicslibrary.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.josecamilo.comicslibrary.Destination
import com.josecamilo.comicslibrary.model.CharactersApiResponse
import com.josecamilo.comicslibrary.model.api.NetworkResult
import com.josecamilo.comicslibrary.viewmodel.LibraryApiViewModel

@Composable
fun LibraryScreen(
    navController: NavHostController,
    libraryVM: LibraryApiViewModel,
    paddingValues: PaddingValues
) {
    val result by libraryVM.result.collectAsState()
    val text by libraryVM.queryText.collectAsState()

    Column(
        modifier = Modifier
            .padding(bottom = paddingValues.calculateBottomPadding())
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = libraryVM::onQueryUpdate,
            label = { Text("Character search") },
            placeholder = { Text("Character") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (result) {
                is NetworkResult.Initial -> {
                    Text("Search for a character")
                }

                is NetworkResult.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResult.Success -> {
                    ShowCharactersList(result, navController)
                }

                is NetworkResult.Error -> {
                    Text("An error occurred: ${(result as NetworkResult.Error).message}")
                }
            }
        }
    }

}

@Composable
fun ShowCharactersList(
    result: NetworkResult<CharactersApiResponse>,
    navController: NavHostController
) {
    result.data?.data?.results?.let { characters ->

        LazyColumn(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Color.LightGray),
            verticalArrangement = Arrangement.Top
        ) {
            result.data.attributionText?.let {
                item {
                    Text(
                        text = it,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp),
                        fontSize = 12.sp
                    )
                }
            }

            items(characters) { character ->
                val imageUrl = character.thumbnail?.path + "." + character.thumbnail?.extension
                val title = character.name
                val description = character.description
                val context = LocalContext.current
                val id = character.id

                Column(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color.White)
                        .fillMaxSize()
                        .wrapContentHeight()
                        .clickable {
                            if (id != null) {
                                navController.navigate(Destination.CharacterDetail.createRoute(character.id))
                            }
                            else {
                                Toast.makeText(context, "Character ID is null", Toast.LENGTH_SHORT).show()
                            }
                        }
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .width(100.dp),
                            contentScale = ContentScale.FillWidth
                        )
                        Column(modifier = Modifier.padding(4.dp)) {
                            Text(text = title ?: "", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }

                Text(text = description ?: "", maxLines = 4, fontSize = 14.sp)

            }
        }
    }
}