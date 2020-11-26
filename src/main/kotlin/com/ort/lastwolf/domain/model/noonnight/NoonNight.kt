package com.ort.lastwolf.domain.model.noonnight

import com.ort.dbflute.allcommon.CDef

data class NoonNight(
    val code: String,
    val name: String
) {
    constructor(
        cdef: CDef.Noonnight
    ) : this(
        code = cdef.code(),
        name = cdef.alias()
    )

    fun toCdef(): CDef.Noonnight = CDef.Noonnight.codeOf(code)

    fun isNoonTime(): Boolean = toCdef() == CDef.Noonnight.昼
    fun isVoteTime(): Boolean = listOf(CDef.Noonnight.投票1回目, CDef.Noonnight.投票2回目, CDef.Noonnight.投票3回目).contains(toCdef())
    fun isNightTime(): Boolean = toCdef() == CDef.Noonnight.夜

    fun next(toNextVote: Boolean = false): NoonNight {
        return when (toCdef()) {
            CDef.Noonnight.昼 -> NoonNight(CDef.Noonnight.投票1回目)
            CDef.Noonnight.投票1回目 -> {
                return if (toNextVote) NoonNight(CDef.Noonnight.投票2回目)
                else NoonNight(CDef.Noonnight.夜)
            }
            CDef.Noonnight.投票2回目 -> {
                return if (toNextVote) NoonNight(CDef.Noonnight.投票3回目)
                else NoonNight(CDef.Noonnight.夜)
            }
            CDef.Noonnight.投票3回目 -> NoonNight(CDef.Noonnight.夜)
            CDef.Noonnight.夜 -> NoonNight(CDef.Noonnight.昼)
        }
    }
}