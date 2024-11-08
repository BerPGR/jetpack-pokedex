package br.com.newpokeapi.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.viewmodel.PokemonViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import java.sql.Types

@Composable
fun PokemonScreen(
    pokemon: Pokemon,
    navController: NavHostController,
    viewModel: PokemonViewModel,
    types: List<Type>
) {
    
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppBar(navController = navController, pokemon = pokemon)

        PokemonImage(pokemon = pokemon)
        
        PokemonInfo(pokemon = pokemon, types = types)
    }
}

@Composable
fun AppBar(navController: NavHostController, pokemon: Pokemon) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = {
                navController.popBackStack()
            }) {
            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Text(text = pokemon.name.capitalize(), fontSize = 23.sp)

        IconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Favorite")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonImage(pokemon: Pokemon) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(Color.DarkGray)
    ) {
        GlideImage(
            model = pokemon.sprites.front_default,
            contentDescription = "Pokemon Imagem",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonInfo(pokemon: Pokemon, types: List<Type>) {
    val pokeHeight = pokemon.height / 10.0
    val formattedHeight = String.format("%.1f", pokeHeight)
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp)
            .fillMaxSize()
    ) {
        Text(text = pokemon.name.capitalize(), fontSize = 28.sp, fontWeight = FontWeight.Bold)

        LazyRow(
        ) {
            items(types) { type ->
                GlideImage(
                    model = type.sprites.generation_iii.colosseum.name_icon,
                    contentDescription = "Type",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(80.dp).padding(end = 10.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Height", fontSize = 16.sp)
                Text(text = "$formattedHeight m", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }

            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = "Weight", fontSize = 16.sp)
                Text(text = "${pokemon.weight} kg", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Text(text = "Abilities", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 36.dp))
        LazyColumn {
            items(pokemon.abilities) { ability ->
                val names = ability.ability.name.split("-").joinToString(" ")
                Text(text = names.capitalize(), modifier = Modifier.padding(top = 10.dp))
            }
        }
    }
}