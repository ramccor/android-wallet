package com.mw.beam.beamwallet.core.entities

import android.os.Parcelable
import com.mw.beam.beamwallet.core.entities.dto.TxDescriptionDTO
import com.mw.beam.beamwallet.core.helpers.TxSender
import com.mw.beam.beamwallet.core.helpers.TxStatus
import com.mw.beam.beamwallet.core.helpers.toHex
import kotlinx.android.parcel.Parcelize

/**
 * Created by vain onnellinen on 10/2/18.
 */
@Parcelize
class TxDescription(private val source: TxDescriptionDTO) : Parcelable {
    val id: String = source.id.toHex()
    val amount: Long = source.amount
    val fee: Long = source.fee
    val change: Long = source.change
    val minHeight: Long = source.minHeight
    val peerId: String = source.peerId.replaceFirst(Regex("^0+"), "")
    val myId: String = source.myId.replaceFirst(Regex("^0+"), "")
    val message: String = String(source.message ?: ByteArray(0))
    val createTime: Long = source.createTime
    val modifyTime: Long = source.modifyTime
    val sender: TxSender = TxSender.fromValue(source.sender)
    val status: TxStatus = TxStatus.fromValue(source.status)

    override fun toString(): String {
        return "TxDescription(id=$id amount=$amount fee=$fee change=$change minHeight=$minHeight " +
                "peerId=$peerId myId=$myId message=$message createTime=$createTime modifyTime=$modifyTime sender= ${sender.name} status=${status.name})"
    }
}
