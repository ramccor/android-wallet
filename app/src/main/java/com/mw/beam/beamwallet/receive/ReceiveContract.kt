package com.mw.beam.beamwallet.receive

import com.mw.beam.beamwallet.baseScreen.MvpPresenter
import com.mw.beam.beamwallet.baseScreen.MvpRepository
import com.mw.beam.beamwallet.baseScreen.MvpView
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 11/13/18.
 */
interface ReceiveContract {
    interface View : MvpView {
        fun init()
        fun getComment() : String?
        fun showToken(token : String)
    }

    interface Presenter : MvpPresenter<View> {
        fun onNextPressed()
    }

    interface Repository : MvpRepository {
        fun generateWalletId() : Subject<ByteArray>
        fun createNewAddress(walletID: ByteArray, comment : String)
    }
}