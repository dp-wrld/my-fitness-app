package com.fitlife.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitlife.app.ui.screens.challenge.ChallengeScreen
import com.fitlife.app.ui.screens.dashboard.DashboardScreen
import com.fitlife.app.ui.screens.diet.DietScreen
import com.fitlife.app.ui.screens.plans.WorkoutPlansScreen
import com.fitlife.app.ui.screens.profile.ProfileScreen
import com.fitlife.app.ui.screens.steps.StepCounterScreen
import com.fitlife.app.ui.screens.workout.WorkoutScreen

sealed class Screen(val route: String) {
    object Dashboard : Screen("dashboard")
    object Workout : Screen("workout")
    object Steps : Screen("steps")
    object Diet : Screen("diet")
    object Challenge : Screen("challenge")
    object Plans : Screen("plans")
    object Profile : Screen("profile")
}

@Composable
fun FitLifeNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) { DashboardScreen(navController) }
        composable(Screen.Workout.route) { WorkoutScreen(navController) }
        composable(Screen.Steps.route) { StepCounterScreen(navController) }
        composable(Screen.Diet.route) { DietScreen(navController) }
        composable(Screen.Challenge.route) { ChallengeScreen(navController) }
        composable(Screen.Plans.route) { WorkoutPlansScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
    }
}
