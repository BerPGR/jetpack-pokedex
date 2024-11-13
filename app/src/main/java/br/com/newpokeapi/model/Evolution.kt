package br.com.newpokeapi.model

import com.google.gson.annotations.SerializedName

data class EvolutionChain(
    val chain: EvolutionChainDetails,
    val id: Int
)

data class EvolutionChainDetails(
    @SerializedName("evolves_to")
    val evolvesTo: List<EvolutionChainDetails>,

    val species: Species
)

data class Specie(
    val id: Int,

    @SerializedName("evolution_chain")
    val evolutionChain: SpecieEvolutionChain
)

data class SpecieEvolutionChain(
    val name: String,
    val url: String
)
