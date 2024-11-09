package br.com.newpokeapi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonAll>
)

data class PokemonAll(
    val name: String,
    val apiUrl: String
)

data class Pokemon(
    val id: Int,
    val height: Int,
    val name: String,
    val sprites: Sprites,
    val weight: Int,
    val types: List<PokemonType>,
    val abilities: List<PokemonAbility>,
    val species: Species
)

data class Sprites(
    val back_default: String,
    val back_female: String,
    val back_shiny: String,
    val back_shiny_female: String,
    val front_default: String,
    val front_female: String,
    val front_shiny: String,
    val front_shiny_female: String,
)

data class Ability(
    val name: String,
    val url: String
)


data class PokemonAbility(
    val ability: Ability,
    val is_hidden: Boolean,
    val slot: Int
)

data class TypeInfo(
    val name: String,
    val url: String
)

data class PokemonType(
    val slot: Int,
    val type: TypeInfo
)

data class Species(
    val name: String,
    val url: String
)