package com.sddamico.mvp

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.view.clicks
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource

import kotlinx.android.synthetic.main.activity_increment.*

typealias PresenterMvmvp = IncrementActivityMvmvpContract.PresenterMvmvp
typealias ViewModelMvmvp = IncrementActivityMvmvpContract.IncrementActivityStateViewModel
typealias StateMvmvp = IncrementActivityMvmvpContract.IncrementActivityMvmvpState

class IncrementActivityRxMvmvp : RxAppCompatActivity(), ScopeProvider {

    override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

    private val viewModel by lazy { ViewModelProviders.of(this).get(ViewModelMvmvp::class.java) }

    private lateinit var presenter: IncrementActivityMvmvpContract.PresenterMvmvp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val maybePresenter = lastCustomNonConfigurationInstance as PresenterMvmvp?

        if (maybePresenter != null) {
            presenter = maybePresenter
        }

        if (!::presenter.isInitialized) {
            presenter = PresenterMvmvpImpl(viewModel)
        }

        setContentView(R.layout.activity_increment)

        observeActions()
        observeState()
    }

    private fun observeState() {
        viewModel.state
                .`as`(autoDisposable(this))
                .subscribe { render(it) }
    }

    private fun render(state: StateMvmvp) {
        counter.text = state.count
    }

    private fun observeActions() {
        increment.clicks()
                .`as`(autoDisposable(this))
                .subscribe { presenter.onIncrementClicked() }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = presenter
}
