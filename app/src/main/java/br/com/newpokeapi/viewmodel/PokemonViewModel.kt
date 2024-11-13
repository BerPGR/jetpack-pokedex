package br.com.newpokeapi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.newpokeapi.model.EvolutionChain
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.PokemonAll
import br.com.newpokeapi.model.Specie
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PokemonViewModel(val pokeRepository: PokemonRepository) : ViewModel() {

    private val _state = MutableStateFlow(emptyList<PokemonAll>())
    val state: StateFlow<List<PokemonAll>>
        get() = _state


    init {
        viewModelScope.launch {
            _state.value = pokeRepository.getAllPokemonFromAPi()
        }
    }

    suspend fun getPokemon(pokemonName: String): Pokemon {
        return withContext(Dispatchers.IO) {
            pokeRepository.getPokemonByName(pokemonName)
        }
    }

    suspend fun getTypeById(typeName: String) : Type {
        return withContext(Dispatchers.IO) {
            pokeRepository.getTypeByNameFromApi(typeName)
        }
    }

    suspend fun getPokemonEvolutionById(id: Int) : EvolutionChain {
        return withContext(Dispatchers.IO) {
            pokeRepository.getPokemonEvolutionById(id)
        }
    }

    suspend fun getPokemonSpecieById(id: Int) : Specie {
        return withContext(Dispatchers.IO) {
            pokeRepository.getPokemonSpecieById(id)
        }
    }
}