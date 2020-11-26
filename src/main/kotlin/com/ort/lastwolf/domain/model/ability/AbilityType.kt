package com.ort.lastwolf.domain.model.ability

import com.ort.dbflute.allcommon.CDef

data class AbilityType(
    val code: String,
    val name: String
) {

    constructor(
        cdef: CDef.AbilityType
    ) : this(
        code = cdef.code(),
        name = cdef.alias()
    )

    constructor(
        code: String
    ) : this(
        code = CDef.AbilityType.codeOf(code).code(),
        name = CDef.AbilityType.codeOf(code).alias()
    )

    fun toCdef(): CDef.AbilityType = CDef.AbilityType.codeOf(code)
}