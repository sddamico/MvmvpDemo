package com.sddamico.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_increment.*

typealias View = IncrementActivityContract.IncrementActivityView
typealias Presenter = IncrementActivityContract.IncrementActivityPresenter

class IncrementActivityMvp : AppCompatActivity(), View {

    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPresenter()

        setContentView(R.layout.activity_increment)

        increment.setOnClickListener { presenter.onIncrementClicked() }
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

    fun initPresenter() {
        val maybePresenter = lastCustomNonConfigurationInstance as Presenter?

        if (maybePresenter != null) {
            presenter = maybePresenter
        }

        if (!::presenter.isInitialized) {
            presenter = IncrementActivityPresenterImpl()
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = presenter
}
