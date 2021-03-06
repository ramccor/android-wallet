package com.mw.beam.beamwallet.utxoDetails

import android.annotation.SuppressLint
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.baseScreen.BaseActivity
import com.mw.beam.beamwallet.baseScreen.BasePresenter
import com.mw.beam.beamwallet.baseScreen.MvpView
import com.mw.beam.beamwallet.core.entities.TxDescription
import com.mw.beam.beamwallet.core.entities.Utxo
import com.mw.beam.beamwallet.core.helpers.UtxoKeyType
import com.mw.beam.beamwallet.core.helpers.UtxoStatus
import com.mw.beam.beamwallet.core.helpers.convertToBeam
import com.mw.beam.beamwallet.core.utils.CalendarUtils
import kotlinx.android.synthetic.main.activity_utxo_details.*
import kotlinx.android.synthetic.main.item_utxo.*

/**
 * Created by vain onnellinen on 12/20/18.
 */
class UtxoDetailsActivity : BaseActivity<UtxoDetailsPresenter>(), UtxoDetailsContract.View {
    private lateinit var presenter: UtxoDetailsPresenter

    companion object {
        const val EXTRA_UTXO_DETAILS = "EXTRA_UTXO_DETAILS"
        const val EXTRA_RELATED_TRANSACTIONS = "EXTRA_RELATED_TRANSACTIONS"
    }

    override fun onControllerGetContentLayoutId() = R.layout.activity_utxo_details
    override fun getToolbarTitle(): String? = getString(R.string.utxo_details_title)
    override fun getUtxoDetails(): Utxo = intent.getParcelableExtra(EXTRA_UTXO_DETAILS)
    override fun getRelatedTransactions(): ArrayList<TxDescription> = intent.getParcelableArrayListExtra<TxDescription>(EXTRA_RELATED_TRANSACTIONS)

    override fun init(utxo: Utxo, relatedTransactions: ArrayList<TxDescription>) {
        configUtxoInfo(utxo)
        configUtxoDetails(utxo)
        configUtxoHistory(utxo, relatedTransactions)
    }

    private fun configUtxoInfo(utxo: Utxo) {
        when (utxo.status) {
            UtxoStatus.Available, UtxoStatus.Maturing, UtxoStatus.Incoming -> {
                ContextCompat.getColor(this, R.color.received_color).apply {
                    amount.setTextColor(this)
                    status.setTextColor(this)
                }
            }
            UtxoStatus.Outgoing, UtxoStatus.Change, UtxoStatus.Spent, UtxoStatus.Unavailable -> {
                ContextCompat.getColor(this, R.color.sent_color).apply {
                    amount.setTextColor(this)
                    status.setTextColor(this)
                }
            }
        }

        status.text = when (utxo.status) {
            UtxoStatus.Incoming, UtxoStatus.Change, UtxoStatus.Outgoing -> getString(R.string.utxo_status_in_progress)
            UtxoStatus.Maturing -> getString(R.string.utxo_status_maturing)
            UtxoStatus.Spent -> getString(R.string.utxo_status_spent)
            UtxoStatus.Available -> getString(R.string.utxo_status_available)
            UtxoStatus.Unavailable -> getString(R.string.utxo_status_unavailable)
        }

        detailedStatus.visibility = View.VISIBLE
        detailedStatus.text = when (utxo.status) {
            UtxoStatus.Incoming -> getString(R.string.utxo_status_incoming)
            UtxoStatus.Change -> getString(R.string.utxo_status_change)
            UtxoStatus.Outgoing -> getString(R.string.utxo_status_outgoing)
            UtxoStatus.Unavailable -> getString(R.string.utxo_status_result_rollback)
            UtxoStatus.Maturing, UtxoStatus.Spent, UtxoStatus.Available -> {
                detailedStatus.visibility = View.GONE
                null //TODO add correct description for maturing
            }
        }

        amount.text = utxo.amount.convertToBeam()
        id.text = utxo.id.toString() //TODO implement correct id from API
    }

    private fun configUtxoDetails(utxo: Utxo) {
        transactionId.text = utxo.createTxId

        utxoType.text = when (utxo.keyType) {
            UtxoKeyType.Commission -> getString(R.string.utxo_type_commission)
            UtxoKeyType.Coinbase -> getString(R.string.utxo_type_coinbase)
            UtxoKeyType.Regular -> getString(R.string.utxo_type_regular)
            UtxoKeyType.Change -> getString(R.string.utxo_type_change)
            UtxoKeyType.Kernel -> getString(R.string.utxo_type_kernel)
            UtxoKeyType.Kernel2 -> getString(R.string.utxo_type_kernel2)
            UtxoKeyType.Identity -> getString(R.string.utxo_type_identity)
            UtxoKeyType.ChildKey -> getString(R.string.utxo_type_childKey)
            UtxoKeyType.Bbs -> getString(R.string.utxo_type_bbs)
            UtxoKeyType.Decoy -> getString(R.string.utxo_type_decoy)
        }
    }

    private fun configUtxoHistory(utxo: Utxo, relatedTransactions: ArrayList<TxDescription>) {
        val offset: Int = resources.getDimensionPixelSize(R.dimen.utxo_history_offset)

        relatedTransactions.forEach {
            transactionHistoryList.addView(configTransaction(
                    isReceived = it.id == utxo.createTxId,
                    time = CalendarUtils.fromTimestamp(it.modifyTime * 1000),
                    id = it.id,
                    comment = it.message,
                    offset = offset))
        }
    }

    @SuppressLint("InflateParams")
    private fun configTransaction(isReceived: Boolean, time: String, id: String, comment: String, offset: Int): View? {
        val view = LayoutInflater.from(this).inflate(R.layout.item_history, null)
        view.findViewById<TextView>(R.id.time).text = time
        view.findViewById<TextView>(R.id.id).text = id
        view.findViewById<ImageView>(R.id.icon).setImageResource(if (isReceived) R.drawable.ic_history_received else R.drawable.ic_history_sent)
        view.findViewById<TextView>(R.id.comment).apply {
            if (comment.isEmpty()) {
                text = comment
                visibility = View.VISIBLE
            }
        }

        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        params.topMargin = offset
        params.bottomMargin = offset

        view.layoutParams = params

        return view
    }

    override fun initPresenter(): BasePresenter<out MvpView> {
        presenter = UtxoDetailsPresenter(this, UtxoDetailsRepository())
        return presenter
    }
}
