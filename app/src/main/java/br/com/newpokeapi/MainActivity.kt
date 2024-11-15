package br.com.newpokeapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.newpokeapi.model.EvolutionChain
import br.com.newpokeapi.model.EvolutionChainDetails
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.PokemonAll
import br.com.newpokeapi.model.Specie
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.repository.PokemonRepository
import br.com.newpokeapi.room.PokemonDB
import br.com.newpokeapi.screens.FavoriteScreen
import br.com.newpokeapi.screens.PokemonScreen
import br.com.newpokeapi.service.RetrofitHelper
import br.com.newpokeapi.ui.theme.NewPokeApiTheme
import br.com.newpokeapi.viewmodel.PokemonViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewPokeApiTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->

                    val navController = rememberNavController()
                    val mContext = LocalContext.current
                    val pokeApi = RetrofitHelper.provideApi(RetrofitHelper.provideRetrofit())
                    val pokeDB = PokemonDB.getInstance(mContext)
                    val pokeRepo = PokemonRepository(pokeApi, pokeDB)
                    val pokemonViewModel = PokemonViewModel(pokeRepo)

                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable(route = "main_screen") {
                            MainScreen(viewModel = pokemonViewModel, navController = navController)
                        }

                        composable(route = "favorites_screen") {
                            FavoriteScreen(pokemonViewModel, navController)
                        }

                        composable(route = "pokemon_screen/{pokemonName}") { backStackEntry ->
                            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""

                            var pokemon by remember { mutableStateOf<Pokemon?>(null) }
                            var pokemonSpecie by remember { mutableStateOf<Specie?>(null) }
                            var pokemonEvolution by remember { mutableStateOf<Pokemon?>(null) }
                            var type by remember { mutableStateOf(mutableListOf<Type>()) }
                            var evolution by remember { mutableStateOf<EvolutionChain?>(null) }
                            var isLoading by remember { mutableStateOf(true) }

                            LaunchedEffect(pokemonName) {
                                isLoading = true

                                pokemon = pokemonViewModel.getPokemon(pokemonName)
                                pokemon?.let {
                                    val loadedTypes = mutableListOf<Type>()
                                    for (types in it.types) {
                                        val selectedType = pokemonViewModel.getTypeById(types.type.name)
                                        loadedTypes.add(selectedType)
                                    }
                                    type = loadedTypes
                                    pokemonSpecie = pokemonViewModel.getPokemonSpecieById(it.id)
                                }

                                pokemonSpecie?.let {
                                    val idEvolution = Regex("/(\\d+)/$").find(it.evolutionChain.url)?.groupValues?.get(1)
                                    if (idEvolution != null) {
                                        evolution = pokemonViewModel.getPokemonEvolutionById(idEvolution.toInt())
                                    }
                                }

                                evolution?.let {
                                    pokemonEvolution = getNextPokemonEvolution(pokemonViewModel, it.chain, pokemon!!.name)
                                }

                                isLoading = false
                            }

                            if (isLoading) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(56.dp))
                                }
                            } else if (pokemon != null) {
                                PokemonScreen(
                                    pokemon = pokemon!!,
                                    evolution = pokemonEvolution,
                                    types = type,
                                    navController = navController,
                                    viewModel = pokemonViewModel
                                )
                            } else {
                                Text(
                                    text = "Failed to load Pokémon data",
                                    modifier = Modifier.fillMaxSize().padding(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private suspend fun getNextPokemonEvolution(
    viewModel: PokemonViewModel,
    currentChainDetails: EvolutionChainDetails,
    selectedPokemonName: String
): Pokemon? {
    return if (currentChainDetails.species.name == selectedPokemonName) {
        if (currentChainDetails.evolvesTo.isEmpty()) {
            null
        } else {
            viewModel.getPokemon(currentChainDetails.evolvesTo[0].species.name)
        }
    } else {
        getNextPokemonEvolution(viewModel, currentChainDetails.evolvesTo[0], selectedPokemonName)
    }
}

@Composable
fun MainScreen(viewModel: PokemonViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        AllPokemons(pokemons = state, navController = navController, viewModel = viewModel)
        Button(onClick = {
            navController.navigate("favorites_screen")
        }) {
            Text(text = "Favorites")
        }
    }
}

@Composable
fun AllPokemons(pokemons: List<PokemonAll>, navController: NavHostController, viewModel: PokemonViewModel) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier
            .padding(top = 100.dp)
        ) {
        items(pokemons) {item ->
            PokeInfo(pokemon = item, navController = navController, viewModel = viewModel)
        }
    }
}

@Composable
fun PokeInfo(pokemon: PokemonAll, navController: NavHostController, viewModel: PokemonViewModel) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text(
            text = pokemon.name,
            fontSize = 24.sp,
            color = Color.Black,
            modifier = Modifier.padding(4.dp)
        )

        Button(onClick = {
            navController.navigate("pokemon_screen/${pokemon.name}")
        }) {
            Text("See Pokemon")
        }
    }

}