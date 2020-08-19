package com.ort.lastwolf.infrastructure.datasource.firebase

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.springframework.stereotype.Repository

@Repository
class MessageLatestDatetimeDataSource {

    fun register(villageId: Int, uid: String?, epocMilli: Long) {
        val ref: DatabaseReference = getDatabaseReference(villageId, uid)
        ref.setValueAsync(epocMilli)
    }

    // ===================================================================================
    //                                                                        Assist Logic
    //                                                                        ============
    private fun getDatabaseReference(villageId: Int, uid: String?): DatabaseReference {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val vid = getVillageIdString(villageId)
        return database.getReference("message_datetime/${vid}/${uid ?: "not_login"}")
    }

    private fun getVillageIdString(villageId: Int): String {
        return String.format("village%05d", villageId)
    }

}