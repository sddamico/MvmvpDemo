package com.sddamico.mvp

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.zipWith

interface MvmvpPresenter<State, out VM: MvmvpViewModel<State>> {
    val viewModel: VM
}

abstract class MvmvpViewModel<State>(initialState: State) : ViewModel() {
    val state = BehaviorRelay.createDefault(initialState)!!
}

abstract class MvmvpPresenterBase<State, out VM: MvmvpViewModel<State>>(override val viewModel: VM) : MvmvpPresenter<State, VM> {

    @SuppressLint("CheckResult")
    fun sendToViewModel(reducer: (State) -> State) {
        Observable.just(reducer)
                .observeOn(AndroidSchedulers.mainThread()) // ensures mutations happen serially on main thread
                .zipWith(viewModel.state)
                .map { (reducer, state) -> reducer.invoke(state) }
                .subscribe(viewModel.state)
    }
}
