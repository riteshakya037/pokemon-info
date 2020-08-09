package com.riteshakya.pokemoninfo.di


import com.riteshakya.pokemoninfo.PokemonApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AppModule::class,
        AndroidInjectionModule::class,
        ActivityBuilderModule::class,
        NetworkModule::class
    ]
)
interface AppComponent : AndroidInjector<PokemonApp> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: PokemonApp): Builder

        fun build(): AppComponent
    }

    fun create(app: PokemonApp)
}