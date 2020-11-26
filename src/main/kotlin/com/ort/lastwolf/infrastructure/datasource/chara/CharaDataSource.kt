package com.ort.lastwolf.infrastructure.datasource.chara

import com.ort.dbflute.exbhv.CharaBhv
import com.ort.dbflute.exentity.Chara
import com.ort.lastwolf.domain.model.charachip.*
import org.springframework.stereotype.Repository

@Repository
class CharaDataSource(
    val charaBhv: CharaBhv
) {
    fun findCharas(charachipId: Int): Charas {
        val charaList = charaBhv.selectList {
            it.query().setCharaGroupId_Equal(charachipId)
        }
        return Charas(charaList.map { convertCharaToChara(it) })
    }

    fun findCharas(charachips: Charachips): Charas {
        val charaList = charaBhv.selectList {
            it.query().setCharaGroupId_InScope(charachips.list.map { charachip -> charachip.id })
        }
        return Charas(charaList.map { convertCharaToChara(it) })
    }

    fun findCharas(charachipIdList: List<Int>): Charas {
        if (charachipIdList.isEmpty()) return Charas(listOf())
        val charaList = charaBhv.selectList {
            it.query().setCharaGroupId_InScope(charachipIdList)
        }
        return Charas(charaList.map { convertCharaToChara(it) })
    }

    fun findChara(charaId: Int): com.ort.lastwolf.domain.model.charachip.Chara {
        val chara = charaBhv.selectEntityWithDeletedCheck {
            it.query().setCharaId_Equal(charaId)
        }
        return convertCharaToChara(chara)
    }

    fun findDummyChara(charaChipId: Int): com.ort.lastwolf.domain.model.charachip.Chara {
        val chara = charaBhv.selectEntityWithDeletedCheck {
            it.query().setCharaGroupId_Equal(charaChipId)
            it.query().addOrderBy_CharaId_Asc()
            it.fetchFirst(1)
        }
        return convertCharaToChara(chara)
    }

    // ===================================================================================
    //                                                                             Mapping
    //                                                                             =======
    private fun convertCharaToChara(chara: Chara): com.ort.lastwolf.domain.model.charachip.Chara {
        return Chara(
            id = chara.charaId,
            name = CharaName(
                name = chara.charaName,
                shortName = chara.charaShortName
            ),
            charachipId = chara.charaGroupId,
            image = CharaImage(
                width = chara.displayWidth,
                height = chara.displayHeight,
                imageUrl = chara.charaImgUrl
            )
        )
    }
}