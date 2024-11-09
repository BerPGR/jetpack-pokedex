package br.com.newpokeapi.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.newpokeapi.model.EvolutionChain
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.viewmodel.PokemonViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun PokemonScreen(
    pokemon: Pokemon,
    evolution: Pokemon,
    navController: NavHostController,
    viewModel: PokemonViewModel,
    types: List<Type>
) {
    
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        AppBar(navController = navController, pokemon = pokemon)

        PokemonImage(pokemon = pokemon, types = types)
        
        PokemonInfo(pokemon = pokemon, types = types, evolution = evolution)
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
fun PokemonImage(pokemon: Pokemon, types: List<Type>) {
    val colors: List<Color> = getAllColors(types)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(
                brush = Brush.linearGradient(
                    colors = colors,
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                )
            )
    ) {
        GlideImage(
            model = pokemon.sprites.front_default,
            contentDescription = "Pokemon Imagem",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
        )
    }
}

private fun getAllColors(types: List<Type>) : List<Color> {
    val typeColors = mutableListOf<Color>()

    val typeToColorMap = mapOf(
        "ice" to Color(0xff73CEC0),
        "ground" to Color(0xffE0A98C),
        "poison" to Color(0xFFC89AD7),
        "steel" to Color(0xFF7198A7),
        "fire" to Color(0xffFFC8A0),
        "water" to Color(0xff7FABDC),
        "grass" to Color(0xff82C67B),
        "rock" to Color(0xFFC5B78C),
        "electric" to Color(0xffF2E194),
        "bug" to Color(0xFFA9C66F),
        "psychic" to Color(0xFFFFA3A8),
        "fighting" to Color(0xFFD6889F),
        "ghost" to Color(0xFF7A8AB8),
        "dragon" to Color(0xFF749CBE),
        "dark" to Color(0xFFB1A7C3),
        "fairy" to Color(0xFFF1B1ED),
        "normal" to Color(0xFFA4A4A4),
        "flying" to Color(0xFF89AAE3),
        // Adicione outros tipos conforme necessário
    )

    if (types.size == 2) {
        for (type in types) {
            typeToColorMap[type.name]?.let { typeColors.add(it) }
        }
    }
    else if (types.size > 2) {
        typeToColorMap[types[0].name]?.let { typeColors.add(it) }
        typeToColorMap[types[1].name]?.let { typeColors.add(it) }
    }
    else {
        typeToColorMap[types[0].name]?.let { typeColors.add(it) }
        typeToColorMap[types[0].name]?.let { typeColors.add(it) }
    }

    return typeColors
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun PokemonInfo(pokemon: Pokemon, types: List<Type>, evolution: Pokemon) {
    val pokeHeight = pokemon.height / 10.0
    val formattedHeight = String.format("%.1f", pokeHeight)
    Column(
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp)
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
                    modifier = Modifier
                        .height(40.dp)
                        .width(70.dp)
                        .padding(end = 10.dp)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color(0xffE8EBF5),
                        shape = RoundedCornerShape(percent = 20)
                    )
                    .padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Height", fontSize = 16.sp)
                    Text(text = "$formattedHeight m", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color(0xffE8EBF5),
                        shape = RoundedCornerShape(percent = 20)
                    )
                    .padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Weight", fontSize = 16.sp)
                    Text(text = "${pokemon.weight} kg", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }


        Text(text = "Evolution", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 36.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = evolution.sprites.front_default,
                contentDescription = "Evolution Picture",
                modifier = Modifier.size(60.dp)
            )
            Text(text = evolution.name.capitalize(), modifier = Modifier.padding(start = 10.dp))
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