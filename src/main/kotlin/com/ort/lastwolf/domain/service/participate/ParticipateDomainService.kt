package com.ort.lastwolf.domain.service.participate

import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.domain.model.charachip.Chara
import com.ort.lastwolf.domain.model.charachip.Charas
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.myself.participant.VillageParticipateSituation
import com.ort.lastwolf.domain.model.player.Player
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.participant.VillageParticipant
import com.ort.lastwolf.fw.exception.LastwolfBusinessException
import org.springframework.stereotype.Service

@Service
class ParticipateDomainService {

    fun convertToSituation(
        village: Village,
        participant: VillageParticipant?,
        player: Player?,
        charas: Charas
    ): VillageParticipateSituation {
        return VillageParticipateSituation(
            isParticipating = participant != null,
            isAvailableParticipate = isAvailableParticipate(player, village),
            selectableCharaList = getSelectableCharaList(village, charas),
            isAvailableLeave = isAvailableLeave(village, participant),
            myself = participant
        )
    }

    /**
     * 参加チェック
     * @param player player
     * @param village village
     * @param charaId charaId
     * @param firstRequestSkill 第1役職希望
     * @param secondRequestSkill 第2役職希望
     * @param password 入村パスワード
     */
    fun assertParticipate(
        player: Player?,
        village: Village,
        charaId: Int,
        firstRequestSkill: CDef.Skill,
        secondRequestSkill: CDef.Skill,
        password: String?
    ) {
        // 参加できない状況ならNG
        if (!isAvailableParticipate(player, village)) throw LastwolfBusinessException("参加できません")
        // 既にそのキャラが参加していたりパスワードを間違えていたらNG
        village.assertParticipate(charaId, password)
        // 役職希望無効の場合はおまかせのみ
        if (!village.setting.rules.isValidSkillRequest(
                firstRequestSkill,
                secondRequestSkill
            )
        ) throw LastwolfBusinessException("希望役職が不正です")
    }

    /**
     * 参加メッセージ
     * @param village village
     * @param chara chara
     * @return 参加時のメッセージ
     */
    fun createParticipateMessage(village: Village, chara: Chara): Message {
        // 何人目か
        val text = "${chara.name.name}が参加しました。（${village.participants.count}人目）"
        return Message.createPublicSystemMessage(text, village.days.prologueDay().id)
    }

    /**
     * @param village village
     * @param charas charas
     * @return 参加/見学できるキャラ
     */
    fun getSelectableCharaList(village: Village, charas: Charas): List<Chara> {
        return charas.list.filterNot { chara ->
            village.participants.list.any { it.chara.id == chara.id }
        }
    }

    /**
     * @param village village
     * @param participant 参加者
     * @return 退村可能か
     */
    fun isAvailableLeave(
        village: Village,
        participant: VillageParticipant?
    ): Boolean {
        // 村として退村可能か
        if (!village.isAvailableLeave()) return false
        // 参加していない
        participant ?: return false

        return true
    }

    /**
     * 退村チェック
     * @param village village
     * @param participant 参加者
     */
    fun assertLeave(
        village: Village,
        participant: VillageParticipant?
    ) {
        if (!isAvailableLeave(village, participant)) throw LastwolfBusinessException("退村できません")
    }


    /**
     * 退村メッセージ
     * @param village village
     * @param chara chara
     * @return 退村時のメッセージ e.g. {キャラ名}は村を去った。
     */
    fun createLeaveMessage(village: Village, chara: Chara): Message =
        Message.createPublicSystemMessage(createLeaveMessageString(chara), village.days.latestDay().id)


    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    /**
     * @param player player
     * @return 参加可能な状況か
     */
    private fun isAvailableParticipate(
        player: Player?,
        village: Village
    ): Boolean {
        // プレイヤーとして参加可能か
        player ?: return false
        if (!player.isAvailableParticipate()) return false
        // 村として参加可能か
        return village.isAvailableParticipate(player)
    }

    private fun createLeaveMessageString(chara: Chara): String =
        "${chara.name.name}は村を去った。"
}