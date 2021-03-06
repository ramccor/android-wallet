package com.mw.beam.beamwallet.core

import java.io.File
import java.util.*

/**
 * Created by vain onnellinen on 10/1/18.
 */
object AppConfig {
    var DB_PATH = ""
    var DB_FILE_NAME = "wallet.db"
    var DB_KEYS_NAME = "keys.bbs"
    var NODE_ADDRESS = "176.58.98.195:8501"
    var LOCALE: Locale = Locale.US

    enum class Status(val value: Int) {
        STATUS_OK(0), STATUS_ERROR(-1);

        companion object {
            private val map: MutableMap<Int, Status> = HashMap()

            init {
                Status.values().forEach {
                    map[it.value] = it
                }
            }

            fun fromValue(type: Int?): Status {
                return map[type] ?: STATUS_ERROR
            }
        }
    }

    fun removeDatabase() {
        val db = File(DB_PATH, DB_FILE_NAME)
        val keys = File(DB_PATH, DB_KEYS_NAME)

        db.delete()
        keys.delete()
    }
}
