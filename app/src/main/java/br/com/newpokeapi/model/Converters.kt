package br.com.newpokeapi.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    // Converters for Sprites
    @TypeConverter
    fun fromSprites(sprites: Sprites): String {
        return gson.toJson(sprites)
    }

    @TypeConverter
    fun toSprites(spritesString: String): Sprites {
        val type = object : TypeToken<Sprites>() {}.type
        return gson.fromJson(spritesString, type)
    }

    // Converters for List<PokemonType>
    @TypeConverter
    fun fromPokemonTypeList(types: List<PokemonType>): String {
        return gson.toJson(types)
    }

    @TypeConverter
    fun toPokemonTypeList(typesString: String): List<PokemonType> {
        val type = object : TypeToken<List<PokemonType>>() {}.type
        return gson.fromJson(typesString, type)
    }

    // Converters for List<PokemonAbility>
    @TypeConverter
    fun fromPokemonAbilityList(abilities: List<PokemonAbility>): String {
        return gson.toJson(abilities)
    }

    @TypeConverter
    fun toPokemonAbilityList(abilitiesString: String): List<PokemonAbility> {
        val type = object : TypeToken<List<PokemonAbility>>() {}.type
        return gson.fromJson(abilitiesString, type)
    }

    // Converter for Species
    @TypeConverter
    fun fromSpecies(species: Species): String {
        return gson.toJson(species)
    }

    @TypeConverter
    fun toSpecies(speciesString: String): Species {
        val type = object : TypeToken<Species>() {}.type
        return gson.fromJson(speciesString, type)
    }
}