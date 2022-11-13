package com.levyks.dtos

data class PlaylistDTO(
    val name: String,
    val url: String,
    val cover: String,
    val numberOfTracks: Int,
    val numberOfTracksNotPlayed: Int,
)
