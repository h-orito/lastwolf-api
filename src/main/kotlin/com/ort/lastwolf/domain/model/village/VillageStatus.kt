package com.ort.lastwolf.domain.model.village

import com.ort.dbflute.allcommon.CDef

data class VillageStatus(
    val code: String,
    val name: String
) {

    constructor(
        cdefStatus: CDef.VillageStatus
    ) : this(
        code = cdefStatus.code(),
        name = cdefStatus.name
    )

    // ===================================================================================
    //                                                                              status
    //                                                                           =========
    fun isSolved(): Boolean = this.toCdef().isSolvedVillage

    fun isRecruiting(): Boolean = this.toCdef() == CDef.VillageStatus.募集中

    fun isRollCalling(): Boolean = this.toCdef() == CDef.VillageStatus.点呼中

    fun isProgress(): Boolean = this.toCdef() == CDef.VillageStatus.進行中

    fun isSettled(): Boolean = this.toCdef() == CDef.VillageStatus.決着

    fun isFinished(): Boolean = this.toCdef().isFinishedVillage

    fun toCdef(): CDef.VillageStatus = CDef.VillageStatus.codeOf(code)
}