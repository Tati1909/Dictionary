package com.example.dictionary.model.data

import com.google.gson.annotations.SerializedName

/**
 * Значение слова
 */
class Meanings(
    @field:SerializedName("translation") val translation: Translation?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)