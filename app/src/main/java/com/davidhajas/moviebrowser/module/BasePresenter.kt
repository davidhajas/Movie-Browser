package com.davidhajas.moviebrowser.module

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.davidhajas.moviebrowser.plugin.threading.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

abstract class BasePresenter(
    private var view: View?,
    private var context: Context?,
    private var lifecycle: Lifecycle?,
    private val dispatcherProvider: DispatcherProvider
) : LifecycleObserver {

    private var coroutineSupervisor: Job
    protected var mainScope: CoroutineScope

    init {
        lifecycle?.addObserver(this)
        coroutineSupervisor = SupervisorJob()
        mainScope = CoroutineScope(dispatcherProvider.main + coroutineSupervisor)
    }

    open fun onViewReady() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        coroutineSupervisor.cancel()
    }
}