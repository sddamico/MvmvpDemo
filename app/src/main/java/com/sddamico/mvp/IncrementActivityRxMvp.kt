package com.sddamico.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks

import kotlinx.android.synthetic.main.activity_increment.*

typealias ViewRx = IncrementActivityRxContract.IncrementActivityRxView
typealias PresenterRx = IncrementActivityRxContract.IncrementActivityRxPresenter

class IncrementActivityRxMvp : AppCompatActivity(), ViewRx {

    private lateinit var presenter: PresenterRx

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val maybePresenter = lastCustomNonConfigurationInstance as PresenterRx?

        if (maybePresenter != null) {
            presenter = maybePresenter
        }

        if (!::presenter.isInitialized) {
            presenter = IncrementActivityRxPresenterImpl()
        }

        setContentView(R.layout.activity_increment)

        increment.clicks()
                .subscribe { presenter.onIncrementClicked() }
    }

    override fun onStart() {
        super.onStart()

        presenter.attach(this)
    }

    override fun onStop() {
        presenter.detach()

        super.onStop()
    }

    override fun setCountView(countString: String) {
        counter.text = countString
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = presenter
}
