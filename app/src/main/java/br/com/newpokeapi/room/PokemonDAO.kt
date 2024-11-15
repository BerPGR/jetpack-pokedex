package br.com.newpokeapi.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.newpokeapi.model.Pokemon
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPokemon(pokemon: Pokemon)

    @Query("SELECT * FROM Pokemon")
    fun getAllPokemons() : Flow<List<Pokemon>>
}