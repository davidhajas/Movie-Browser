package com.davidhajas.moviebrowser.module

import android.content.Context
import com.nhaarman.mockitokotlin2.whenever

class ContextCoach(private val context: Context) {

    fun trainGetString(resId: Int, string: String) {
        whenever(context.getString(resId)).thenReturn(string)
    }
}