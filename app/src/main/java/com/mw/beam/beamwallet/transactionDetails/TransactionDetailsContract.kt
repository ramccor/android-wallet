package com.mw.beam.beamwallet.transactionDetails

import com.mw.beam.beamwallet.baseScreen.MvpPresenter
import com.mw.beam.beamwallet.baseScreen.MvpRepository
import com.mw.beam.beamwallet.baseScreen.MvpView
import com.mw.beam.beamwallet.core.entities.TxDescription

/**
 * Created by vain onnellinen on 10/18/18.
 */
interface TransactionDetailsContract {
    interface View : MvpView {
        fun getTransactionDetails(): TxDescription
        fun init(txDescription: TxDescription)
    }

    interface Presenter : MvpPresenter<View>

    interface Repository : MvpRepository {
        var txDescription: TxDescription?
    }
}
