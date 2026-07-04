package com.foretmagique.content.catalog

import com.foretmagique.content.model.Word

object WordCatalog {

    val words: List<Word> = listOf(
        Word(id = "papa", text = "papa", syllableIds = listOf("pa", "pa"), emoji = "👨", difficultyTier = 1),
        Word(id = "lama", text = "lama", syllableIds = listOf("la", "ma"), emoji = "🦙", difficultyTier = 1),
        Word(id = "ami", text = "ami", syllableIds = listOf("a", "mi"), emoji = "🤝", difficultyTier = 1),
        Word(id = "moto", text = "moto", syllableIds = listOf("mo", "to"), emoji = "🏍️", difficultyTier = 1),
        Word(id = "lune", text = "lune", syllableIds = listOf("lu", "ne"), emoji = "🌙", difficultyTier = 1),
        Word(id = "pile", text = "pile", syllableIds = listOf("pi", "le"), emoji = "🔋", difficultyTier = 1),
        Word(id = "rose", text = "rose", syllableIds = listOf("ro", "se"), emoji = "🌹", difficultyTier = 1),
        Word(id = "note", text = "note", syllableIds = listOf("no", "te"), emoji = "🎵", difficultyTier = 1),
        Word(id = "puma", text = "puma", syllableIds = listOf("pu", "ma"), emoji = "🐆", difficultyTier = 1),
        Word(id = "mare", text = "mare", syllableIds = listOf("ma", "re"), emoji = "🐸", difficultyTier = 1),
        Word(id = "lime", text = "lime", syllableIds = listOf("li", "me"), emoji = "🍋", difficultyTier = 1),
        Word(id = "tapis", text = "tapis", syllableIds = listOf("ta", "pi"), emoji = "🟫", difficultyTier = 1),
        Word(id = "tomate", text = "tomate", syllableIds = listOf("to", "ma", "te"), emoji = "🍅", difficultyTier = 2),
        Word(id = "salami", text = "salami", syllableIds = listOf("sa", "la", "mi"), emoji = "🍕", difficultyTier = 2),
        Word(id = "patate", text = "patate", syllableIds = listOf("pa", "ta", "te"), emoji = "🥔", difficultyTier = 2),
    )

    fun wordById(id: String): Word =
        words.first { it.id == id }

    fun wordsForLetter(letterId: String): List<Word> =
        words.filter { word ->
            word.syllableIds.any { syllableId ->
                SyllableCatalog.syllableById(syllableId).let {
                    it.consonantLetterId == letterId || it.vowelLetterId == letterId
                }
            }
        }
}
