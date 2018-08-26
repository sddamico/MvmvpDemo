package com.sddamico.mvp

interface IncrementActivityMvmvpContract {
    interface PresenterMvmvp : MvmvpPresenter<IncrementActivityMvmvpState, IncrementActivityStateViewModel> {
        fun onIncrementClicked()
    }

    data class IncrementActivityMvmvpState(
            var count: String = 0.toString()
    )

    class IncrementActivityStateViewModel : MvmvpViewModel<IncrementActivityMvmvpState>(IncrementActivityMvmvpState())
}

class PresenterMvmvpImpl(override val viewModel: ViewModelMvmvp)
    : MvmvpPresenterBase<StateMvmvp, ViewModelMvmvp>(viewModel), PresenterMvmvp {

    override fun onIncrementClicked() {
        sendToViewModel { it.copy(
                count = it.count.toInt().plus(1).toString()
        )}
    }
}
