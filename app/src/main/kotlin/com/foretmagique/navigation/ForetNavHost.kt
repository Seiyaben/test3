package com.foretmagique.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.foretmagique.audio.SpeechService
import com.foretmagique.data.progress.ProgressRepository
import com.foretmagique.ui.forestmap.ForestMapScreen
import com.foretmagique.ui.letter.LetterLearningScreen
import com.foretmagique.ui.matching.SoundMatchScreen
import com.foretmagique.ui.progress.ProgressScreen
import com.foretmagique.ui.syllable.SyllableBlendScreen
import com.foretmagique.ui.tracing.TracingScreen
import com.foretmagique.ui.word.WordBuildScreen

@Composable
fun ForetNavHost(
    speechService: SpeechService,
    progressRepository: ProgressRepository,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Destinations.FOREST_MAP) {
        composable(Destinations.FOREST_MAP) {
            ForestMapScreen(
                progressRepository = progressRepository,
                onLetterSelected = { letterId ->
                    navController.navigate(Destinations.letterLearning(letterId))
                },
                onOpenProgress = { navController.navigate(Destinations.PROGRESS_OVERVIEW) },
            )
        }

        composable(Destinations.PROGRESS_OVERVIEW) {
            ProgressScreen(
                progressRepository = progressRepository,
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Destinations.LETTER_LEARNING,
            arguments = listOf(navArgument(Destinations.ARG_LETTER_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString(Destinations.ARG_LETTER_ID).orEmpty()
            LetterLearningScreen(
                letterId = letterId,
                speechService = speechService,
                onPlaySoundMatch = { navController.navigate(Destinations.soundMatch(letterId)) },
                onPlaySyllableBlend = { navController.navigate(Destinations.syllableBlend(letterId)) },
                onPlayWordBuild = { navController.navigate(Destinations.wordBuild(letterId)) },
                onPlayTracing = { navController.navigate(Destinations.tracing(letterId)) },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Destinations.SOUND_MATCH,
            arguments = listOf(navArgument(Destinations.ARG_LETTER_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString(Destinations.ARG_LETTER_ID).orEmpty()
            SoundMatchScreen(
                letterId = letterId,
                speechService = speechService,
                progressRepository = progressRepository,
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Destinations.SYLLABLE_BLEND,
            arguments = listOf(navArgument(Destinations.ARG_LETTER_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString(Destinations.ARG_LETTER_ID).orEmpty()
            SyllableBlendScreen(
                letterId = letterId,
                speechService = speechService,
                progressRepository = progressRepository,
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Destinations.WORD_BUILD,
            arguments = listOf(navArgument(Destinations.ARG_LETTER_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString(Destinations.ARG_LETTER_ID).orEmpty()
            WordBuildScreen(
                letterId = letterId,
                speechService = speechService,
                progressRepository = progressRepository,
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Destinations.TRACING,
            arguments = listOf(navArgument(Destinations.ARG_LETTER_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val letterId = backStackEntry.arguments?.getString(Destinations.ARG_LETTER_ID).orEmpty()
            TracingScreen(
                letterId = letterId,
                progressRepository = progressRepository,
                onComplete = { navController.popBackStack() },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
