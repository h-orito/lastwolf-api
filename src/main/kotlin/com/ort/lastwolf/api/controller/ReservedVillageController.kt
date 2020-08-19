package com.ort.lastwolf.api.controller

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.body.ReservedVillageRegisterBody
import com.ort.lastwolf.api.view.reserved.ReservedVillagesView
import com.ort.lastwolf.application.service.ReservedVillageService
import com.ort.lastwolf.domain.model.reserved.ReservedVillage
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import com.ort.lastwolf.fw.security.LastwolfUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ReservedVillageController(
    val reservedVillageService: ReservedVillageService
) {

    /**
     * 予約村一覧取得
     */
    @GetMapping("/reserved-village/list")
    fun list(): ReservedVillagesView = ReservedVillagesView(reservedVillageService.findReservedVillages())

    /**
     * 予約村登録
     *
     * @param user user
     * @param body body
     */
    @PostMapping("/reserved-village")
    fun register(
        @AuthenticationPrincipal user: LastwolfUser,
        @RequestBody @Validated body: ReservedVillageRegisterBody
    ) {
        if (user.authority != CDef.Authority.管理者) throw LastwolfBusinessException("管理者のみ可能な操作です")
        reservedVillageService.registerReservedVillage(
            ReservedVillage(
                id = 1,
                villageCreateDatetime = body.createDatetime!!,
                villageStartDatetime = body.startDatetime!!,
                organization = body.organization!!,
                silentHours = body.silentHours!!,
                availableDummySkill = body.availableDummySkill!!
            )
        )
    }

    /**
     * 予約村削除
     *
     * @param reservedVillageId 予約村ID
     * @param user user
     */
    @DeleteMapping("/reserved-village/{reservedVillageId}")
    fun register(
        @PathVariable("reservedVillageId") reservedVillageId: Int,
        @AuthenticationPrincipal user: LastwolfUser
    ) {
        if (user.authority != CDef.Authority.管理者) throw LastwolfBusinessException("管理者のみ可能な操作です")
        reservedVillageService.deleteReservedVillage(reservedVillageId)
    }
}