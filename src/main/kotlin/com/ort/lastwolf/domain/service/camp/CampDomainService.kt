package com.ort.lastwolf.domain.service.camp

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.message.Message
import org.springframework.stereotype.Service

@Service
class CampDomainService {

    /**
     * 勝利陣営メッセージ
     * @param cdefWinCamp 勝利した陣営
     * @param villageDayId 村日付ID
     */
    fun createWinCampMessage(cdefWinCamp: CDef.Camp?, villageDayId: Int): Message =
        Message.createPublicSystemMessage(getWinCampMessage(cdefWinCamp), villageDayId, true)

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun getWinCampMessage(cdefWinCamp: CDef.Camp?): String {
        cdefWinCamp ?: return "村民たちの協議は永久に続いたのであった。\n引き分けです！"
        return when (cdefWinCamp) {
            CDef.Camp.村人陣営 -> "全ての人狼を退治した。人狼に怯える日々は去ったのだ。\n村人陣営の勝利です！"
            CDef.Camp.人狼陣営 -> "もう人狼に抵抗できるほど村人は残っていない。\n人狼は残った村人を全て食らい、別の獲物を求めて村を去っていった。\n" +
                "人狼陣営の勝利です！"
            CDef.Camp.妖狐陣営 -> "全ては終わったかのように見えた。だが、奴が生き残っていた。\n" +
                "妖狐陣営の勝利です！"
        }
    }
}