package br.com.newpokeapi

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.PokemonAll
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.repository.PokemonRepository
import br.com.newpokeapi.screens.PokemonScreen
import br.com.newpokeapi.service.RetrofitHelper
import br.com.newpokeapi.ui.theme.NewPokeApiTheme
import br.com.newpokeapi.viewmodel.PokemonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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
                    val pokeApi = RetrofitHelper.provideApi(RetrofitHelper.provideRetrofit())
                    val pokeRepo = PokemonRepository(pokeApi)
                    val pokemonViewModel = PokemonViewModel(pokeRepo)
                    val scope = rememberCoroutineScope()

                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable(route = "main_screen") {

                            MainScreen(viewModel = pokemonViewModel, navController = navController)
                        }

                        composable(route = "pokemon_screen/{pokemonName}") { backStackEntry ->
                            val pokemonName = backStackEntry.arguments?.getString("pokemonName") ?: ""

                            var pokemon by remember { mutableStateOf<Pokemon?>(null) }
                            var type by remember { mutableStateOf(mutableListOf<Type>()) }

                            LaunchedEffect(pokemonName) {
                                pokemon = pokemonViewModel.getPokemon(pokemonName)

                                pokemon?.let {
                                    val loadedTypes = mutableListOf<Type>()
                                    for (types in it.types) {
                                        val selectedType = pokemonViewModel.getTypeById(types.type.name)
                                        loadedTypes.add(selectedType)
                                    }
                                    type = loadedTypes
                                }
                            }

                            Log.i("POKEMON_INFO", "$pokemon")
                            for (types in type) {
                                Log.i("POKEMON_TYPES", "$types")
                            }

                            pokemon?.let { PokemonScreen(pokemon = it, types = type, navController = navController, viewModel = pokemonViewModel) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: PokemonViewModel, navController: NavHostController) {
    val state by viewModel.state.collectAsState()
    AllPokemons(pokemons = state, navController = navController, viewModel = viewModel)
}

@Composable
fun AllPokemons(pokemons: List<PokemonAll>, navController: NavHostController, viewModel: PokemonViewModel) {
    LazyVerticalGrid(columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp)
        ) {
        items(pokemons) {item ->
            PokeInfo(pokemon = item, navController = navController, viewModel = viewModel)
        }
    }
}

@SuppressLint("MutableCollectionMutableState")
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