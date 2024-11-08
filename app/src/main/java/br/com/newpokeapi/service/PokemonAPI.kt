package br.com.newpokeapi.service

import br.com.newpokeapi.model.Pokemon
import br.com.newpokeapi.model.PokemonAll
import br.com.newpokeapi.model.PokemonResponse
import br.com.newpokeapi.model.Type
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonAPI {

    @GET("pokemon?limit=10&offset=0")
    suspend fun getAllPokemons(): PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonByName(@Path("name") name: String): Pokemon

    @GET("type/{typeName}")
    suspend fun getTypeByName(@Path("typeName") typeName: String): Type
}