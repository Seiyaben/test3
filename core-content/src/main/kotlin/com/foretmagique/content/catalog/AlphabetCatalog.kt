package com.foretmagique.content.catalog

import com.foretmagique.content.model.Letter
import com.foretmagique.content.model.Phoneme

// MVP teaching order: vowels first, then the most frequent, easiest-to-blend
// consonants (continuants l/m/r/n/s before the occlusives p/t). The
// remaining 14 letters follow the same pattern and can be appended once this
// set is validated by a real child.
object AlphabetCatalog {

    val phonemes: List<Phoneme> = listOf(
        Phoneme(id = "a", label = "voyelle a", ttsText = "a"),
        Phoneme(id = "e", label = "voyelle e", ttsText = "e"),
        Phoneme(id = "i", label = "voyelle i", ttsText = "i"),
        Phoneme(id = "o", label = "voyelle o", ttsText = "o"),
        Phoneme(id = "u", label = "voyelle u", ttsText = "u"),
        Phoneme(id = "l", label = "consonne l", ttsText = "lll"),
        Phoneme(id = "m", label = "consonne m", ttsText = "mmm"),
        Phoneme(id = "p", label = "consonne p", ttsText = "pe"),
        Phoneme(id = "t", label = "consonne t", ttsText = "te"),
        Phoneme(id = "r", label = "consonne r", ttsText = "rrr"),
        Phoneme(id = "n", label = "consonne n", ttsText = "nnn"),
        Phoneme(id = "s", label = "consonne s", ttsText = "sss"),
    )

    val letters: List<Letter> = listOf(
        Letter(id = "a", upper = 'A', lower = 'a', soundId = "a", order = 1),
        Letter(id = "e", upper = 'E', lower = 'e', soundId = "e", order = 2),
        Letter(id = "i", upper = 'I', lower = 'i', soundId = "i", order = 3),
        Letter(id = "o", upper = 'O', lower = 'o', soundId = "o", order = 4),
        Letter(id = "u", upper = 'U', lower = 'u', soundId = "u", order = 5),
        Letter(id = "l", upper = 'L', lower = 'l', soundId = "l", order = 6),
        Letter(id = "m", upper = 'M', lower = 'm', soundId = "m", order = 7),
        Letter(id = "p", upper = 'P', lower = 'p', soundId = "p", order = 8),
        Letter(id = "t", upper = 'T', lower = 't', soundId = "t", order = 9),
        Letter(id = "r", upper = 'R', lower = 'r', soundId = "r", order = 10),
        Letter(id = "n", upper = 'N', lower = 'n', soundId = "n", order = 11),
        Letter(id = "s", upper = 'S', lower = 's', soundId = "s", order = 12),
    )

    val lettersInTeachingOrder: List<Letter> = letters.sortedBy { it.order }

    fun letterById(id: String): Letter =
        letters.first { it.id == id }

    fun phonemeForLetter(letterId: String): Phoneme {
        val letter = letterById(letterId)
        return phonemes.first { it.id == letter.soundId }
    }
}
