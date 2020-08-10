package com.riteshakya.pokemoninfo.ui.pokemon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.riteshakya.pokemoninfo.core.DataResult
import com.riteshakya.pokemoninfo.core.base.BaseViewModel
import com.riteshakya.pokemoninfo.interactor.pokemon.GetPokemonDetail
import com.riteshakya.pokemoninfo.repository.pokemon.models.PokemonDetail
import com.riteshakya.pokemoninfo.testing.OpenForTesting
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@OpenForTesting
class PokemonDetailViewModel
@Inject constructor(
    private val getPokemonDetail: GetPokemonDetail
) : BaseViewModel() {
    private val urlSubject = PublishSubject.create<String>()

    private val _pokemonDetail = MutableLiveData<DataResult<PokemonDetail>>()
    val pokemonDetail: LiveData<DataResult<PokemonDetail>> by lazy { _pokemonDetail }

    init {
        urlSubject
            .switchMapSingle {
                getDetailSingle(it)
            }
            .subscribe()
            .untilCleared()
    }

    private fun getDetailSingle(url: String): Single<DataResult<PokemonDetail>> {
        return getPokemonDetail(url)
            .doOnSubscribe {
                _pokemonDetail.postValue(DataResult.loading())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                DataResult.success(it)
            }.onErrorReturn { DataResult.error(it.message) }
            .doOnSuccess {
                _pokemonDetail.postValue(it)
            }
    }

    fun getDetail(url: String) {
        urlSubject.onNext(url)
    }
}