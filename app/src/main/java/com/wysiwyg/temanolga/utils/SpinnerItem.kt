package com.wysiwyg.temanolga.utils

import android.content.Context
import com.wysiwyg.temanolga.R

object SpinnerItem {

    fun sportPref(ctx: Context, sport: String?): String? {
        var sportName: String? = null
        when (sport) {
            "0" -> sportName = ctx.getString(R.string.running)
            "1" -> sportName = ctx.getString(R.string.futsal)
            "2" -> sportName = ctx.getString(R.string.football)
        }
        return sportName
    }

    fun slotType(ctx: Context, slotType: String?): String? {
        var slotNm: String? = null
        when (slotType) {
            "0" -> slotNm = ctx.getString(R.string.person)
            "1" -> slotNm = ctx.getString(R.string.team)
        }
        return slotNm
    }
}