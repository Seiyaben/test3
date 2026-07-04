package com.foretmagique.data.progress

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.foretmagique.content.progression.ActivityType
import com.foretmagique.content.progression.ProgressionEngine
import com.foretmagique.content.progression.UserProgressSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val Context.progressDataStore by preferencesDataStore(name = "progress_prefs")

private val progressJsonKey = stringPreferencesKey("progress_snapshot_json")

private val json = Json { ignoreUnknownKeys = true }

// Single-profile app: one JSON blob for the whole progress snapshot is
// simpler to reason about than a scalar Preferences key per letter/activity,
// and there's no relational querying need that would justify Room here.
class ProgressRepository(private val context: Context) {

    val progress: Flow<UserProgressSnapshot> = context.progressDataStore.data.map { prefs ->
        prefs[progressJsonKey]?.let { decodeOrNull(it) } ?: ProgressionEngine.initialSnapshot()
    }

    suspend fun recordStars(letterId: String, activity: ActivityType, stars: Int) {
        context.progressDataStore.edit { prefs ->
            val current = prefs[progressJsonKey]?.let { decodeOrNull(it) } ?: ProgressionEngine.initialSnapshot()
            val updated = ProgressionEngine.withStars(current, letterId, activity, stars)
            prefs[progressJsonKey] = json.encodeToString(UserProgressSnapshot.serializer(), updated)
        }
    }

    private fun decodeOrNull(rawJson: String): UserProgressSnapshot? =
        runCatching { json.decodeFromString(UserProgressSnapshot.serializer(), rawJson) }.getOrNull()
}
