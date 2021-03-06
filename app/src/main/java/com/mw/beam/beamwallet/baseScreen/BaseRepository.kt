package com.mw.beam.beamwallet.baseScreen

import com.mw.beam.beamwallet.core.App
import com.mw.beam.beamwallet.core.entities.Wallet
import com.mw.beam.beamwallet.core.utils.LogUtils
import io.reactivex.subjects.Subject

/**
 * Created by vain onnellinen on 10/1/18.
 */
open class BaseRepository : MvpRepository {

    override var wallet: Wallet? = null
        get() = App.wallet

    fun <T> getResult(block: () -> Unit, subject: Subject<T>, requestName: String): Subject<T> {
        LogUtils.log(StringBuilder()
                .append(LogUtils.LOG_REQUEST)
                .append(" ")
                .append(requestName)
                .append("\n")
                .append("--------------------------")
                .append("\n").toString())
        block.invoke()
        return subject
    }

    fun getResult(block: () -> Unit, requestName: String) {
        LogUtils.log(StringBuilder()
                .append(LogUtils.LOG_REQUEST)
                .append(" ")
                .append(requestName)
                .append("\n")
                .append("--------------------------")
                .append("\n").toString())
        block.invoke()
    }
}
