package br.com.newpokeapi.model

import com.google.gson.annotations.SerializedName

data class Type(
    val id: Int,
    val name: String,
    val sprites: TypeSprite
)

data class GenerationIII(
    val colosseum: GenerationInfo,
    val emerald: GenerationInfo,
    val firered_leafgreen: GenerationInfo,
    val ruby_sapphire: GenerationInfo,
    val xd: GenerationInfo
)

data class TypeSprite(
    @SerializedName("generation-iii")
    val generation_iii: GenerationIII,
)

data class GenerationInfo(
    val name_icon: String
)