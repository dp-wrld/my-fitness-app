# 🏋️ FitLife — Android Fitness App

A comprehensive Android fitness app built with **Kotlin + Jetpack Compose**, following **MVVM + Clean Architecture**.

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🏠 **Dashboard** | Daily stats overview — steps, calories, BMI, streaks & quick actions |
| 💪 **Workout Tracker** | Log workouts by category (Strength, Cardio, HIIT, Flexibility), track sets/reps/duration |
| 🚶 **Step Counter** | Real-time step counting via device sensors, weekly bar chart, manual entry |
| 🥗 **Diet Planner** | Log meals (Breakfast/Lunch/Dinner/Snack), calorie & macro tracking with visual bars |
| 🏆 **Fitness Challenges** | 30-day challenges, popular templates (Push-Up, Plank, Squat, Steps), daily log |
| 🗓️ **Workout Plans** | Built-in plans (PPL, StrongLifts, HIIT, Marathon), custom plan creator |
| 👤 **Profile** | BMI calculator, body stats, daily goals, activity level & primary goal |

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture (3 layers) |
| DI | Hilt |
| Database | Room (SQLite) |
| Navigation | Navigation Compose |
| Reactive | Kotlin Flows + StateFlow |
| Background | Android Foreground Service (Step Counter) |
| Charts | Custom Canvas drawing + Vico library |

## 📁 Project Structure

```
app/src/main/java/com/fitlife/app/
├── data/
│   ├── local/
│   │   ├── entity/        # Room entities
│   │   ├── dao/           # Data Access Objects
│   │   └── FitLifeDatabase.kt
│   └── repository/        # Repository implementations
├── domain/
│   └── repository/        # Repository interfaces
├── di/                    # Hilt DI modules
├── service/               # StepCounterService (Foreground)
├── ui/
│   ├── theme/             # Colors, Typography, Theme
│   ├── navigation/        # NavGraph + BottomNavBar
│   ├── components/        # Shared composables
│   └── screens/
│       ├── dashboard/     # Home dashboard
│       ├── workout/       # Workout tracker
│       ├── steps/         # Step counter
│       ├── diet/          # Diet planner
│       ├── challenge/     # Fitness challenges
│       ├── plans/         # Workout plans
│       └── profile/       # User profile
├── FitLifeApplication.kt
└── MainActivity.kt
```

## 🚀 Setup & Build

### Prerequisites
- Android Studio Ladybug (2024.2.1) or newer
- JDK 11+
- Android SDK 35 (target), SDK 26 (minimum)
- Physical device or emulator with **API 26+**

### Steps

1. **Open in Android Studio**
   ```
   File → Open → select the `my-fitness-app` folder
   ```

2. **Sync Gradle**
   Android Studio will auto-detect the Gradle project. Click **Sync Now** if prompted.

3. **Run the app**
   - Connect a device or start an emulator
   - Click ▶️ **Run** (Shift+F10)

> [!NOTE]
> Step counting uses the device's `TYPE_STEP_COUNTER` hardware sensor. This requires a **physical device** or an emulator with sensor simulation enabled.

> [!IMPORTANT]
> The app requires the `ACTIVITY_RECOGNITION` permission (Android 10+). It will be requested at runtime.

## 📱 App Screens

### 🏠 Dashboard
- Personalized greeting + motivational quote
- Today's stats cards: Steps, Calories Burned, Water, Calories In
- Weekly streak tracker with fire emoji
- Active challenge progress bar
- Quick action buttons
- BMI overview card

### 💪 Workout Tracker
- Filter by category (All, Strength, Cardio, HIIT, Flexibility)
- Weekly summary (workouts count, total calories, total minutes)
- Log workout with name, category, duration, calories, notes
- Mark as completed / delete

### 🚶 Step Counter
- Circular progress ring (animated)
- Distance (km), Calories, Active minutes
- Weekly bar chart
- Editable daily step goal
- Manual step entry (for when sensor isn't available)

### 🥗 Diet Planner
- Calorie balance bar (consumed vs goal vs burned)
- Macro breakdown bars (Protein, Carbs, Fat)
- Meals grouped by type (Breakfast, Lunch, Dinner, Snack)
- Add meal with full nutrition info

### 🏆 Fitness Challenges
- Active challenges with progress bars
- Popular challenge templates:
  - 30-Day Push-Up Challenge
  - 10K Steps Daily
  - 7-Day Plank Challenge
  - 21-Day Clean Eating
  - 30-Day Squat Challenge
  - Couch to 5K (8 weeks)
- Daily log button, custom challenge creator

### 🗓️ Workout Plans
- Built-in plans: Beginner Full Body, PPL Strength, HIIT Shred, 5x5 StrongLifts, Marathon Prep, Yoga Flow
- Active plan display with pause option
- Custom plan creator (name, duration, days/week, difficulty, goal)

### 👤 Profile
- Avatar with initials
- BMI calculation with color-coded category
- Editable stats: name, age, weight, height, gender, activity level, goal
- Daily step & calorie goals

## 🗄️ Database Schema

| Table | Purpose |
|-------|---------|
| `workouts` | Workout sessions |
| `exercises` | Individual exercises per workout |
| `steps` | Daily step counts |
| `meals` | Meal logs with macros |
| `challenges` | Fitness challenges |
| `challenge_logs` | Daily challenge check-ins |
| `workout_plans` | Training plans |
| `plan_days` | Individual plan days/sessions |
| `user_profile` | Single-row user profile |

## 🎨 Design

- **Dark theme** by default with navy/red-pink accent palette
- Color-coded categories (Orange=Strength, Blue=Cardio, Red=HIIT, Purple=Flexibility)
- Rounded cards with subtle icon backgrounds
- Material Design 3 components throughout

## 📦 Dependencies

```toml
Compose BOM: 2024.12.01
Navigation: 2.8.5
Hilt: 2.52
Room: 2.6.1
Vico Charts: 2.0.0-beta.4
DataStore: 1.1.1
WorkManager: 2.10.0
Splash Screen: 1.0.1
```

---

*Built with ❤️ using Kotlin + Jetpack Compose*
