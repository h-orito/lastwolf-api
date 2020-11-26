package com.ort.lastwolf.infrastructure.datasource.firebase

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.ort.dbflute.allcommon.CDef
import com.ort.lastwolf.api.view.message.MessageView
import com.ort.lastwolf.domain.model.message.Message
import com.ort.lastwolf.domain.model.village.Village
import com.ort.lastwolf.domain.model.village.ability.VillageAbility
import com.ort.lastwolf.domain.model.village.vote.VillageVote
import com.ort.lastwolf.fw.LastwolfDateUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.time.ZoneOffset

@Repository
class FirebaseDataSource {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    // 夜メッセージの最新
    fun registerMessageLatest(villageId: Int, uid: String?, epocMilli: Long) {
        val ref: DatabaseReference = getMessageLatestDatabaseReference(villageId, uid)
        ref.setValueAsync(epocMilli)
    }

    // 昼と投票はfirebaseに登録する
    fun registerMessage(village: Village, message: Message) {
        val ref: DatabaseReference = getMessagesDatabaseReference(
            villageId = village.id
        )
        val now = LastwolfDateUtil.currentLocalDateTime()
        val epocTimeMilli = now.toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
        var messageView = MessageView(message, village, false)
        messageView = messageView.copy(
            time = messageView.time.copy(
                datetime = now,
                unixTimeMilli = epocTimeMilli
            )
        )
        ref.push().setValueAsync(messageView)
    }

    fun registerVillageLatest(villageId: Int) {
        val ref: DatabaseReference = getVillageLatestDatabaseReference(villageId)
        val epocMilli = LastwolfDateUtil.currentLocalDateTime().toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
        ref.setValueAsync(epocMilli)
    }

    fun registerSituationLatest(village: Village, ability: VillageAbility) {
        val epocTimeMilli = LastwolfDateUtil.currentLocalDateTime().toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
        if (ability.abilityType.toCdef() == CDef.AbilityType.襲撃) {
            village.participants.list.filter { it.isAlive() && it.skill!!.toCdef().isHasAttackAbility }.forEach {
                val ref: DatabaseReference = getSituationLatestDatabaseReference(village.id, it.player.uid)
                ref.setValueAsync(epocTimeMilli)
            }
        } else {
            village.participants.first(ability.myselfId).let {
                val ref: DatabaseReference = getSituationLatestDatabaseReference(village.id, it.player.uid)
                ref.setValueAsync(epocTimeMilli)
            }
        }
    }

    fun registerSituationLatest(village: Village, vote: VillageVote) {
        val epocTimeMilli = LastwolfDateUtil.currentLocalDateTime().toInstant(ZoneOffset.ofHours(+9)).toEpochMilli()
        village.participants.first(vote.myselfId).let {
            val ref: DatabaseReference = getSituationLatestDatabaseReference(village.id, it.player.uid)
            ref.setValueAsync(epocTimeMilli)
        }
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun getVillageLatestDatabaseReference(villageId: Int): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val vid = getVillageIdString(villageId)
        return database.getReference("${vid}/village_latest")
    }

    private fun getMessageLatestDatabaseReference(villageId: Int, uid: String?): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val vid = getVillageIdString(villageId)
        return database.getReference("${vid}/message_latest/${uid ?: "not_login"}")
    }

    private fun getSituationLatestDatabaseReference(villageId: Int, uid: String?): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val vid = getVillageIdString(villageId)
        return database.getReference("${vid}/situation_latest/${uid ?: "not_login"}")
    }

    private fun getMessagesDatabaseReference(villageId: Int): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val vid = getVillageIdString(villageId)
        return database.getReference("${vid}/messages/")
    }

    private fun getVillageIdString(villageId: Int): String {
        return String.format("v%05d", villageId)
    }
}