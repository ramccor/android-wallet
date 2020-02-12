package com.mw.beam.beamwallet.screens.timer_overlay_dialog

import com.mw.beam.beamwallet.base_screen.MvpPresenter
import com.mw.beam.beamwallet.base_screen.MvpRepository
import com.mw.beam.beamwallet.base_screen.MvpView

interface TimerOverlayContract {
    interface View: MvpView {
        fun init()
    }

    interface Presenter: MvpPresenter<View> {
    }

    interface Repository: MvpRepository {

    }
}