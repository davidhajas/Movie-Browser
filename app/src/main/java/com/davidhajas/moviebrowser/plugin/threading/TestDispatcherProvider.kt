package com.davidhajas.moviebrowser.plugin.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestDispatcherProvider : DispatcherProvider {

    // region DispatcherProvider

    override val main: CoroutineDispatcher = Dispatchers.Unconfined

    override val background: CoroutineDispatcher = Dispatchers.Unconfined

    // endregion
}