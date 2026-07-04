package com.foretmagique.content.progression

import kotlinx.serialization.Serializable

@Serializable
data class UserProgressSnapshot(
    val starsByActivityKey: Map<String, Int> = emptyMap(),
    val unlockedLetterIds: Set<String> = emptySet(),
)
