package com.mw.beam.beamwallet.utxo

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.baseScreen.BaseFragment
import com.mw.beam.beamwallet.baseScreen.BasePresenter
import com.mw.beam.beamwallet.baseScreen.MvpView
import com.mw.beam.beamwallet.core.entities.TxDescription
import com.mw.beam.beamwallet.core.entities.Utxo
import com.mw.beam.beamwallet.core.helpers.UtxoStatus
import com.mw.beam.beamwallet.core.watchers.TabSelectedListener
import kotlinx.android.synthetic.main.fragment_utxo.*

/**
 * Created by vain onnellinen on 10/2/18.
 */
class UtxoFragment : BaseFragment<UtxoPresenter>(), UtxoContract.View {
    private lateinit var presenter: UtxoPresenter
    private lateinit var allUtxosAdapter: UtxosAdapter
    private lateinit var activeUtxosAdapter: UtxosAdapter

    companion object {
        fun newInstance() = UtxoFragment().apply { arguments = Bundle() }
        fun getFragmentTag(): String = UtxoFragment::class.java.simpleName
    }

    override fun onControllerGetContentLayoutId() = R.layout.fragment_utxo
    override fun getToolbarTitle(): String? = getString(R.string.utxo_title)

    override fun init() {
        val context = context ?: return

        allUtxosAdapter = UtxosAdapter(context, mutableListOf(), object : UtxosAdapter.OnItemClickListener {
            override fun onItemClick(item: Utxo) {
                presenter.onUtxoPressed(item)
            }
        })
        activeUtxosAdapter = UtxosAdapter(context, mutableListOf(), object : UtxosAdapter.OnItemClickListener {
            override fun onItemClick(item: Utxo) {
                presenter.onUtxoPressed(item)
            }
        })

        allUtxos.layoutManager = LinearLayoutManager(context)
        activeUtxos.layoutManager = LinearLayoutManager(context)

        allUtxos.adapter = allUtxosAdapter
        activeUtxos.adapter = activeUtxosAdapter

        activeUtxos.visibility = View.VISIBLE
        allUtxos.visibility = View.GONE
    }

    override fun addListeners() {
        tabLayout.addOnTabSelectedListener(object : TabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                allUtxos.visibility = if (tab?.position == Tab.ALL.ordinal) View.VISIBLE else View.GONE
                activeUtxos.visibility = if (tab?.position == Tab.ACTIVE.ordinal) View.VISIBLE else View.GONE
            }
        })
    }

    override fun showUtxoDetails(utxo: Utxo, relatedTransactions: ArrayList<TxDescription>) = (activity as UtxoDetailsHandler).onShowUtxoDetails(utxo, relatedTransactions)

    override fun updateUtxos(utxos: List<Utxo>) {
        activeUtxosAdapter.setData(utxos.filter { it.status == UtxoStatus.Available || it.status == UtxoStatus.Maturing })
        allUtxosAdapter.setData(utxos)
    }

    override fun clearListeners() {
        tabLayout.clearOnTabSelectedListeners()
    }

    override fun initPresenter(): BasePresenter<out MvpView> {
        presenter = UtxoPresenter(this, UtxoRepository(), UtxoState())
        return presenter
    }

    interface UtxoDetailsHandler {
        fun onShowUtxoDetails(item: Utxo, relatedTransactions: ArrayList<TxDescription>)
    }

    private enum class Tab {
        ACTIVE, ALL
    }
}
