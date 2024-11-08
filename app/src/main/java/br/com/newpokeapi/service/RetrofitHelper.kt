package br.com.newpokeapi.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    fun provideApi(builder: Retrofit.Builder): PokemonAPI {
        return builder.build().create(PokemonAPI::class.java)
    }

    fun provideRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
    }
}