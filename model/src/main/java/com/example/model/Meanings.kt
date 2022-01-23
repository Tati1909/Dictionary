package com.example.model

import com.google.gson.annotations.SerializedName

/**
 * Значение слова
 */
class Meanings(
    @field:SerializedName("translation") val translation: Translation?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)