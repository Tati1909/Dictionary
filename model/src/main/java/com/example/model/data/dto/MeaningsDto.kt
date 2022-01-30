package com.example.model.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Значение слова
 */
class MeaningsDto(
    @field:SerializedName("translation") val translationDto: TranslationDto?,
    @field:SerializedName("imageUrl") val imageUrl: String?
)