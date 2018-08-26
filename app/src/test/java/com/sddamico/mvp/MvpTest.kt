package com.sddamico.mvp

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Test

class MvpTest {

    @Test
    fun `test initial state`() {
        val presenterImpl = IncrementActivityPresenterImpl()

        val view = mock<View> {}

        presenterImpl.attach(view)

        verify(view).setCountView("0")
    }


    @Test
    fun `test increment`() {
        val presenterImpl = IncrementActivityPresenterImpl()

        val view = mock<View> {}

        presenterImpl.attach(view)

        presenterImpl.onIncrementClicked()

        verify(view).setCountView("0")
        verify(view).setCountView("1")
    }
}
