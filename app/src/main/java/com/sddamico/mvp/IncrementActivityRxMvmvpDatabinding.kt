package com.sddamico.mvp

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.sddamico.mvp.databinding.ActivityIncrementBindingBinding
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.uber.autodispose.AutoDispose.autoDisposable
import com.uber.autodispose.ScopeProvider
import com.ubercab.autodispose.rxlifecycle.RxLifecycleInterop
import io.reactivex.CompletableSource

class IncrementActivityRxMvmvpDatabinding : RxAppCompatActivity(), ScopeProvider {

    override fun requestScope(): CompletableSource = RxLifecycleInterop.from(this).requestScope()

    private val viewModel by lazy { ViewModelProviders.of(this).get(ViewModelMvmvp::class.java) }

    private lateinit var presenter: IncrementActivityMvmvpContract.PresenterMvmvp

    private lateinit var binding: ActivityIncrementBindingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initPresenter()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_increment_binding)
        binding.setLifecycleOwner(this)
        binding.presenter = presenter

        observeState()
    }

    private fun observeState() {
        viewModel.state
                .`as`(autoDisposable(this))
                .subscribe { binding.model = it }
    }

    fun initPresenter() {
        val maybePresenter = lastCustomNonConfigurationInstance as PresenterMvmvp?

        if (maybePresenter != null) {
            presenter = maybePresenter
        }

        if (!::presenter.isInitialized) {
            presenter = PresenterMvmvpImpl(viewModel)
        }
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = presenter
}
