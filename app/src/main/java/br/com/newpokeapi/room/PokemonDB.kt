package br.com.newpokeapi.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.newpokeapi.model.Converters
import br.com.newpokeapi.model.Pokemon

@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDB : RoomDatabase() {
    abstract fun pokemonDao() : PokemonDAO

    companion object {
        @Volatile
        private var INSTANCE: PokemonDB? = null

        fun getInstance(context: Context): PokemonDB {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDB::class.java,
                        "pokemon_db"
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}