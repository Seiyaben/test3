package com.foretmagique.content.catalog

import com.foretmagique.content.model.Syllable

// Only the syllables actually used by WordCatalog are curated here (not the
// full consonant x vowel cross product) so every entry is pedagogically
// deliberate rather than auto-generated.
object SyllableCatalog {

    val syllables: List<Syllable> = listOf(
        Syllable(id = "a", consonantLetterId = null, vowelLetterId = "a", text = "a"),
        Syllable(id = "pa", consonantLetterId = "p", vowelLetterId = "a", text = "pa"),
        Syllable(id = "la", consonantLetterId = "l", vowelLetterId = "a", text = "la"),
        Syllable(id = "ma", consonantLetterId = "m", vowelLetterId = "a", text = "ma"),
        Syllable(id = "ta", consonantLetterId = "t", vowelLetterId = "a", text = "ta"),
        Syllable(id = "sa", consonantLetterId = "s", vowelLetterId = "a", text = "sa"),
        Syllable(id = "mi", consonantLetterId = "m", vowelLetterId = "i", text = "mi"),
        Syllable(id = "pi", consonantLetterId = "p", vowelLetterId = "i", text = "pi"),
        Syllable(id = "li", consonantLetterId = "l", vowelLetterId = "i", text = "li"),
        Syllable(id = "mo", consonantLetterId = "m", vowelLetterId = "o", text = "mo"),
        Syllable(id = "to", consonantLetterId = "t", vowelLetterId = "o", text = "to"),
        Syllable(id = "ro", consonantLetterId = "r", vowelLetterId = "o", text = "ro"),
        Syllable(id = "no", consonantLetterId = "n", vowelLetterId = "o", text = "no"),
        Syllable(id = "lu", consonantLetterId = "l", vowelLetterId = "u", text = "lu"),
        Syllable(id = "pu", consonantLetterId = "p", vowelLetterId = "u", text = "pu"),
        Syllable(id = "ne", consonantLetterId = "n", vowelLetterId = "e", text = "ne"),
        Syllable(id = "le", consonantLetterId = "l", vowelLetterId = "e", text = "le"),
        Syllable(id = "se", consonantLetterId = "s", vowelLetterId = "e", text = "se"),
        Syllable(id = "te", consonantLetterId = "t", vowelLetterId = "e", text = "te"),
        Syllable(id = "re", consonantLetterId = "r", vowelLetterId = "e", text = "re"),
        Syllable(id = "me", consonantLetterId = "m", vowelLetterId = "e", text = "me"),
    )

    fun syllableById(id: String): Syllable =
        syllables.first { it.id == id }
}
