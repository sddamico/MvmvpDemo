package com.sddamico.mvp

interface IncrementActivityContract {
    interface IncrementActivityView : MvpView {
        fun setCountView(countString : String)
    }

    interface IncrementActivityPresenter : MvpPresenter<IncrementActivityView> {
        fun onIncrementClicked()
    }
}

class IncrementActivityPresenterImpl : MvpPresenterBase<View>(), Presenter {

    private var count = 0

    override fun attach(view: View) {
        super.attach(view)

        view.setCountView(getCountString())
    }

    override fun onIncrementClicked() {
        count++

        view?.setCountView(getCountString())
    }

    private fun getCountString() = count.toString()
}

