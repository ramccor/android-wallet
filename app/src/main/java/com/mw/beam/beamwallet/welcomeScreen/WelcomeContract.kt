package com.mw.beam.beamwallet.welcomeScreen

import com.mw.beam.beamwallet.baseScreen.MvpPresenter
import com.mw.beam.beamwallet.baseScreen.MvpRepository
import com.mw.beam.beamwallet.baseScreen.MvpView

/**
 * Created by vain onnellinen on 10/19/18.
 */
interface WelcomeContract {
    interface View : MvpView {
        fun showOpenFragment()
        fun showDescriptionFragment()
        fun showPasswordsFragment(phrases : Array<String>)
        fun showPhrasesFragment()
        fun showRestoreFragment()
        fun showCreateFragment()
        fun showValidationFragment(phrases: Array<String>)
        fun showMainActivity()
    }

    interface Presenter : MvpPresenter<View> {
        fun onCreateWallet()
        fun onRestoreWallet()
        fun onGeneratePhrase()
        fun onOpenWallet()
        fun onChangeWallet()
        fun onProceedToPasswords(phrases : Array<String>)
        fun onProceedToValidation(phrases: Array<String>)
    }

    interface Repository : MvpRepository {
        fun isWalletInitialized(): Boolean
    }
}
