package com.davidhajas.moviebrowser.plugin.threading

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultDispatcherProvider : DispatcherProvider {

    // region DispatcherProvider

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val background: CoroutineDispatcher = Dispatchers.IO

    // endregion
}