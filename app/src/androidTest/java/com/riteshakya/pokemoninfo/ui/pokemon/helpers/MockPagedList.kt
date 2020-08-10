package com.riteshakya.pokemoninfo.ui.pokemon.helpers

import androidx.paging.PagedList
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

fun <T> List<T>.mockPagedList(): PagedList<T> {
    val pagedList = Mockito.mock(PagedList::class.java) as PagedList<T>
    Mockito.`when`(pagedList.get(ArgumentMatchers.anyInt())).then { invocation ->
        val index = invocation.arguments.first() as Int
        this[index]
    }
    Mockito.`when`(pagedList.size).thenReturn(size)
    return pagedList
}