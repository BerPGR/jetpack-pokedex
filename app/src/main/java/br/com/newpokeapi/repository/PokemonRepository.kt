package br.com.newpokeapi.repository

import android.util.Log
import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.PokemonAll
import br.com.newpokeapi.model.Type
import br.com.newpokeapi.service.PokemonAPI

class PokemonRepository(
    val pokeApi: PokemonAPI
) {

    suspend fun getAllPokemonFromAPi(): List<PokemonAll> {
        val response = pokeApi.getAllPokemons()
        return response.results
    }

    suspend fun getPokemonByName(pokemonName: String) : Pokemon {
        return pokeApi.getPokemonByName(pokemonName)
    }

    suspend fun getTypeByNameFromApi(typeName: String) : Type {
        return pokeApi.getTypeByName(typeName)
    }
}