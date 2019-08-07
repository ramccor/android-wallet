package com.mw.beam.beamwallet.screens.add_contact

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.zxing.integration.android.IntentIntegrator
import com.mw.beam.beamwallet.R
import com.mw.beam.beamwallet.base_screen.BaseFragment
import com.mw.beam.beamwallet.base_screen.BasePresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView
import com.mw.beam.beamwallet.core.helpers.Tag
import com.mw.beam.beamwallet.screens.qr.ScanQrActivity
import kotlinx.android.synthetic.main.fragment_add_contact.*

class AddContactFragment : BaseFragment<AddContactPresenter>(), AddContactContract.View {
    private val tokenWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            presenter?.onTokenChanged()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    }

    override fun getAddress(): String {
        return address.text?.toString() ?: ""
    }

    override fun getName(): String {
        return name.text?.toString() ?: ""
    }

    override fun onControllerGetContentLayoutId() = R.layout.fragment_add_contact

    override fun getToolbarTitle(): String? = getString(R.string.add_contact)

    override fun getStatusBarColor(): Int = ContextCompat.getColor(context!!, R.color.addresses_status_bar_color)

    override fun addListeners() {
        btnCancel.setOnClickListener {
            presenter?.onCancelPressed()
        }

        btnSave.setOnClickListener {
            presenter?.onSavePressed()
        }

        scanQR.setOnClickListener {
            presenter?.onScanPressed()
        }

        address.addTextChangedListener(tokenWatcher)
    }

    override fun setAddress(address: String) {
        this.address.setText(address)
    }

    override fun navigateToScanQr() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.captureActivity = ScanQrActivity::class.java
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter?.onScannedQR(IntentIntegrator.parseActivityResult(resultCode, data).contents)
    }

    override fun navigateToAddNewCategory() {
        findNavController().navigate(AddContactFragmentDirections.actionAddContactFragmentToEditCategoryFragment())
    }

    override fun setupTagAction(isEmptyTags: Boolean) {
        val resId = if (isEmptyTags) R.drawable.ic_add_tag else R.drawable.ic_edit_tag
        val drawable = ContextCompat.getDrawable(context!!, resId)
        tagAction.setImageDrawable(drawable)
    }

    override fun showCreateTagDialog() {
        showAlert(
                getString(R.string.dialog_empty_tags_message),
                getString(R.string.create_tag),
                { presenter?.onCreateNewTagPressed() },
                getString(R.string.tag_list_is_empty),
                getString(R.string.cancel)
        )
    }

    override fun showTagsDialog(selectedTags: List<Tag>) {
        
    }

    override fun showTokenError() {
        tokenError.visibility = View.VISIBLE
    }

    override fun hideTokenError() {
        tokenError.visibility = View.INVISIBLE
    }

    override fun clearListeners() {
        btnCancel.setOnClickListener(null)
        btnSave.setOnClickListener(null)
        scanQR.setOnClickListener(null)
        address.removeTextChangedListener(tokenWatcher)
    }

    override fun close() {
        findNavController().popBackStack()
    }


    override fun initPresenter(): BasePresenter<out MvpView, out MvpRepository> {
        return AddContactPresenter(this, AddContactRepository(), AddContactState())
    }
}