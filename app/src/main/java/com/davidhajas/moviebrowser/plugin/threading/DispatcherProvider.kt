package com.davidhajas.moviebrowser.plugin.threading

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {
    val main: CoroutineDispatcher
    val background: CoroutineDispatcher
}