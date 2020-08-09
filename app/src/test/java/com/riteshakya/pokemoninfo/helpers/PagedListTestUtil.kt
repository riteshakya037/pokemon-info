package com.riteshakya.pokemoninfo.helpers

import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList


fun <T> List<T>.asPagedList(config: PagedList.Config? = null): PagedList<T>? {
    val defaultConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(false)
        .setPageSize(size)
        .setMaxSize(size + 2)
        .setPrefetchDistance(1)
        .build()
    return LivePagedListBuilder<Int, T>(
        createMockDataSourceFactory(this),
        config ?: defaultConfig
    ).build().getOrAwaitValue()
}

fun <I, T> createMockDataSourceFactory(itemList: List<T>): DataSource.Factory<I, T> =
    object : DataSource.Factory<I, T>() {
        override fun create(): DataSource<I, T> = MockLimitDataSource(itemList)
    }

class MockLimitDataSource<I, T>(private val itemList: List<T>) : PageKeyedDataSource<I, T>() {
    override fun loadInitial(
        params: LoadInitialParams<I>,
        callback: LoadInitialCallback<I, T>
    ) {
        callback.onResult(itemList, null, null)
    }

    override fun loadAfter(params: LoadParams<I>, callback: LoadCallback<I, T>) {
        callback.onResult(itemList, null)
    }

    override fun loadBefore(params: LoadParams<I>, callback: LoadCallback<I, T>) {
        callback.onResult(itemList, null)
    }
}