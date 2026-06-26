package org.fandom.project

import kotlinx.serialization.Serializable

@Serializable
data class Article (
    val id : String,
    val title : String,
    val url : String,
    val imageUrl : String,
    val communityId : String,
    val communityName : String
)