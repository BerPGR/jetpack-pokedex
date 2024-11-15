package br.com.newpokeapi.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import br.com.newpokeapi.viewmodel.PokemonViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavoriteScreen(viewModel: PokemonViewModel, navController: NavHostController) {
    val favorites by viewModel.favoritePokemons.collectAsState(initial = emptyList())
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    navController.popBackStack()
                }
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "back")
            }

            Text(text = "Your favorite Pokemons")
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(favorites) { pokemon ->
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 54.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .align(Alignment.Center),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {}

                        GlideImage(model = pokemon.sprites.front_default, contentDescription = "Image",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(100.dp).align(Alignment.TopCenter).zIndex(1f).offset(y = (-48).dp)
                            )

                        Text(pokemon.name.capitalize(), fontSize = 22.sp, modifier = Modifier.align(Alignment.Center).padding(top = 32.dp))
                    }
                }
            }
        }
    }
}