package com.ort.lastwolf.domain.model.charachip

data class Charas(
    val list: List<Chara>
) {
    fun chara(id: Int): Chara = list.first { it.id == id }
}

