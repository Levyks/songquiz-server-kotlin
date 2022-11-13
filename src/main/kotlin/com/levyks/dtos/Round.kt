package com.levyks.dtos

import com.levyks.enums.RoundStatus
import com.levyks.enums.RoundType

data class RoundDTO (
    val number: Int,
    val type: RoundType,
    val status: RoundStatus,
    val remainingSeconds: Int,
    val songUrl: String,
    val options: List<String>,
)

data class RoundResultDTO (
    val number: Int,
    val correctAnswer: Int,
    val scores: Map<String, Int>,
)

data class AddSongToHistoryDTO(
    var name: String,
    var artists: List<String>,
    var cover: String,
    var url: String,
)