# Project Plan

A multi-screen, responsive Android habit-tracking app using Jetpack Compose, state-driven navigation, and interactive progress visualization (circular indicators, GitHub-style calendar/heatmaps). Optimized with a premium, vibrant theme based on Midnight Blue/Neon Black from the provided design system. Includes database persistence (Room) and responsive tablet supporting layouts.

## Project Brief

# Build Habit

Build Habit is a responsive habit-tracking Android application designed to help users build long-term discipline. Built using modern Android architecture, the app features a vibrant, high-contrast visual identity optimized for Premium Midnight Blue or Neon Black themes.

---

## Features

1. **Interactive Home Dashboard**: Highlights the user's current streak with a circular progress indicator, highlights the top three ongoing habits, and displays a summary consistency heatmap.
2. **Flexible Habit Creation**: Allows users to quickly define new habits by entering a name, setting a specific daily reminder time, and choosing a personalized calendar chip color.
3. **Habit Tracking & Management**: Provides a centralized tracking panel with search functionality, habit cards with live streak counts, one-tap today status toggle, delete operations, and a weekly consistency tracker.
4. **In-Depth Habit Analytics**: Displays a full GitHub-style completion grid filled with the custom habit color, detailed completion statistics (best streak, overall rate), and a bottom sheet for quick edits.

---

## High-Level Tech Stack

* **Kotlin**: Main language for building the application.
* **Jetpack Compose**: For a fully responsive, modern declarative UI.
* **Jetpack Navigation 3**: For state-driven navigation handling flow between Home, Create, Track, and Detail screens.
* **Compose Material Adaptive Library**: To support multi-device and dynamic panel sizing.
* **Kotlin Coroutines**: For asynchronous flow and smooth UI thread handling.

---

## UI Design Image

![UI Design](file://C:/Users/anura/AndroidStudioProjects/BuildHabit/input_images/image_2.png)

## Implementation Steps

### Task_1_DatabaseAndRepository: Implement the database persistence layer using Room. Create entities for Habit and HabitCompletion to track user habits, streaks, colors, reminders, and daily completions. Write DAOs and repositories with Coroutines.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Room Database class and entities (Habit and HabitCompletion) created
  - DAOs support CRUD operations for habits and toggling daily completion status
  - Database and Repository layer successfully builds and runs within tests/code
- **StartTime:** 2026-06-03 15:27:44 IST

### Task_2_NavigationAndViewModel: Configure the state-driven Navigation 3 flow between Home, Create, Track, and Detail screens. Develop the ViewModels to handle screen states, database interactions, and stream data asynchronously using Kotlin Coroutines and StateFlow.
- **Status:** PENDING
- **Acceptance Criteria:**
  - State-driven navigation routes configured for Home, Create, Track, and Detail screens
  - ViewModel manages app UI state for daily tracking, analytics, and creations
  - Search and filter logic implemented in view model or repository

### Task_3_ResponsiveUIAndTheme: Build the user interface using Jetpack Compose with a vibrant, high-contrast Midnight Blue and Neon Black premium theme. Implement interactive components (circular progress indicator, weekly tracker, GitHub-style heatmap/calendar grid, customizable habit color chips). Support Edge-to-Edge display and responsive/adaptive layout on tablets.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Vibrant Premium Midnight Blue / Neon Black Material 3 theme is implemented
  - Interactive Home Dashboard, Habit Creation, Track Management, and Habit Analytics screens built
  - The implemented UI must match the design provided in C:/Users/anura/AndroidStudioProjects/BuildHabit/input_images/image_2.png.
  - Full Edge-to-Edge display support and responsive layout on tablets are functional
  - GitHub-style completion grid renders custom habit colors properly

### Task_4_AppIconAndVerification: Create and configure a custom adaptive app icon matching the Habit Tracking theme. Perform final end-to-end testing, verify application stability, ensure no crashes occur, and verify all existing tests build and pass successfully.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Adaptive app icon implemented and visible in AndroidManifest
  - App builds successfully without any errors
  - App does not crash and handles state configuration changes correctly
  - All existing tests pass

