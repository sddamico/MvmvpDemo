package com.sddamico.mvp

import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable

interface MvpView

interface MvpPresenter<View: MvpView> {
    fun attach(view : View)

    fun detach()
}

abstract class MvpPresenterBase<View: MvpView> : MvpPresenter<View> {

    var view: View? = null

    override fun attach(view: View) {
        this.view = view
    }

    override fun detach() {
        this.view = null
    }
}

abstract class MvpPresenterBaseRx<View: MvpView> : MvpPresenter<View>, LifecycleScopeProvider<PresenterScope> {

    private val lifecycle = BehaviorRelay.createDefault<PresenterScope>(PresenterScope.DETACHED)!!

    var view: View? = null

    override fun attach(view: View) {
        this.view = view
        lifecycle.accept(PresenterScope.ATTACHED)
    }

    override fun detach() {
        this.view = null
        lifecycle.accept(PresenterScope.DETACHED)
    }

    override fun lifecycle(): Observable<PresenterScope> = lifecycle

    override fun correspondingEvents(): CorrespondingEventsFunction<PresenterScope> {
        return CorrespondingEventsFunction { when(it)  {
            PresenterScope.DETACHED -> PresenterScope.ATTACHED
            PresenterScope.ATTACHED -> PresenterScope.DETACHED
        }}
    }

    override fun peekLifecycle(): PresenterScope? = lifecycle.value

    override fun requestScope(): CompletableSource = LifecycleScopes.resolveScopeFromLifecycle(this)

    private inline fun <T> autoDisposeable(it: Observable<T>) = AutoDispose.autoDisposable<T>(this).apply(it)

    internal inline fun <T> Observable<T>.autoDispose() = this.`as` { autoDisposeable(this) }
}

enum class PresenterScope {
    ATTACHED, DETACHED
}
