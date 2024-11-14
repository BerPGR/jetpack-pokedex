package br.com.newpokeapi.room

import androidx.room.Dao
import androidx.room.Insert
import br.com.newpokeapi.model.Pokemon

@Dao
interface PokemonDAO {

    @Insert
    suspend fun addPokemon(pokemon: Pokemon)
}