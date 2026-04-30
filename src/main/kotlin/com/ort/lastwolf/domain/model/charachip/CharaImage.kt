package com.ort.lastwolf.domain.model.charachip

import com.google.firebase.database.PropertyName

data class CharaImage(
    val width: Int,
    val height: Int,
    @get:PropertyName("image_url") // firebase用
    val imageUrl: String,
)
