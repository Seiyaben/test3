package com.foretmagique.content.progression

import com.foretmagique.content.catalog.AlphabetCatalog

// Only SOUND_MATCH and SYLLABLE_BLEND gate progression to the next clearing -
// these are the two MVP pillars ("Lettres & sons" + "Syllabes & mots").
// TRACING and WORD_BUILD are supplementary and always rewarding, never
// blocking, per the "never a hard fail" design for a 5-6 year old.
object ProgressionEngine {

    val activitiesRequiredToUnlockNext: List<ActivityType> =
        listOf(ActivityType.SOUND_MATCH, ActivityType.SYLLABLE_BLEND)

    fun activityKey(letterId: String, activity: ActivityType): String =
        "$letterId:${activity.name}"

    fun computeStars(correctFirstTry: Boolean, hintsUsed: Int): Int = when {
        correctFirstTry && hintsUsed == 0 -> 3
        hintsUsed <= 1 -> 2
        else -> 1
    }

    fun initialSnapshot(
        orderedLetterIds: List<String> = AlphabetCatalog.lettersInTeachingOrder.map { it.id },
    ): UserProgressSnapshot =
        UserProgressSnapshot(unlockedLetterIds = setOf(orderedLetterIds.first()))

    fun withStars(
        snapshot: UserProgressSnapshot,
        letterId: String,
        activity: ActivityType,
        stars: Int,
        orderedLetterIds: List<String> = AlphabetCatalog.lettersInTeachingOrder.map { it.id },
    ): UserProgressSnapshot {
        val key = activityKey(letterId, activity)
        val existing = snapshot.starsByActivityKey[key] ?: 0
        val updatedStars = snapshot.starsByActivityKey + (key to maxOf(existing, stars))
        val updated = snapshot.copy(starsByActivityKey = updatedStars)
        return updated.copy(unlockedLetterIds = nextUnlockedLetterIds(updated, orderedLetterIds))
    }

    fun isLetterMastered(snapshot: UserProgressSnapshot, letterId: String): Boolean =
        activitiesRequiredToUnlockNext.all { activity ->
            (snapshot.starsByActivityKey[activityKey(letterId, activity)] ?: 0) >= 1
        }

    fun nextUnlockedLetterIds(
        snapshot: UserProgressSnapshot,
        orderedLetterIds: List<String> = AlphabetCatalog.lettersInTeachingOrder.map { it.id },
    ): Set<String> {
        if (orderedLetterIds.isEmpty()) return emptySet()
        val unlocked = mutableSetOf(orderedLetterIds.first())
        for (i in 0 until orderedLetterIds.size - 1) {
            val current = orderedLetterIds[i]
            if (isLetterMastered(snapshot, current)) {
                unlocked.add(orderedLetterIds[i + 1])
            } else {
                break
            }
        }
        return unlocked
    }
}
