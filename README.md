# Xplore App

A modern Android application that connects travelers with local tour guides, enabling seamless tour booking and management.

## Overview

Xplore is a dual-interface mobile platform designed to bridge the gap between travelers and tour guides. The app provides two distinct user experiences:
- **Travelers** can discover, book, and review tours
- **Guides** can create profiles, manage tours, and handle bookings

## Features

### For Travelers
- Browse and search available tours
- Filter tours by location and type
- View tour schedules and availability
- See pricing and tour details
- Read and write reviews
- Book tours with preferred guides
- View guide profiles and ratings
- Track booking history

### For Guides
- Create and manage tour guide profile
- Create and publish tours
- Define tour details (location, duration, price, schedule)
- Upload tour and profile images
- Manage tour schedules
- View and manage bookings
- Track tour performance and ratings

## Tech Stack

### Core Technologies
- **Language**: Kotlin
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 34
- **Build System**: Gradle with Kotlin DSL

### Architecture & Libraries
- **Architecture Pattern**: MVVM (Model-View-ViewModel)
- **UI**:
  - ViewBinding
  - Material Design Components
  - ConstraintLayout
  - SwipeRefreshLayout
- **Navigation**: Android Navigation Component
- **Lifecycle**:
  - LiveData
  - ViewModel
  - Lifecycle-aware components

### Backend & Cloud
- **Firebase Services**:
  - Firebase Authentication (User management)
  - Cloud Firestore (Database)
  - Firebase Storage (Image storage)
  - Firebase Analytics

### Additional Libraries
- **Image Loading**: Coil (v2.5.0)
- **JSON Parsing**: Gson (v2.10.1)
- **Coroutines**: Kotlin Coroutines for async operations

## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK (API 26+)
- Firebase account

### Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Xplore
   ```

2. **Firebase Configuration**
   - Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add an Android app to your Firebase project
   - Download `google-services.json` and place it in the `app/` directory
   - Enable the following Firebase services:
     - Authentication (Email/Password)
     - Cloud Firestore
     - Storage
     - Analytics

3. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the Xplore directory
   - Wait for Gradle sync to complete

4. **Run the app**
   - Connect an Android device or start an emulator (API 26+)
   - Click the "Run" button or press `Shift + F10`

### Configuration Files

- `build.gradle.kts` - Project-level build configuration
- `app/build.gradle.kts` - App-level dependencies and configuration
- `app/google-services.json` - Firebase configuration (not included in repo)

## Database Structure

### Firestore Collections

- **users** - User profiles (travelers and guides)
- **tours** - Tour listings with details
- **bookings** - Tour bookings
- **reviews** - Tour reviews and ratings
- **guides** - Guide profiles with additional information

## User Flow

### Traveler Flow
1. Register/Login
2. Browse available tours
3. View tour details and guide information
4. Book a tour
5. Leave review after tour completion

### Guide Flow
1. Register/Login
2. Create guide profile
3. Create tour listings
4. Manage tour schedules
5. Handle booking requests
6. View ratings and reviews

## Permissions

The app requires the following permissions:
- `INTERNET` - Network access for Firebase operations
- `ACCESS_NETWORK_STATE` - Check network connectivity

## Build

To build the app:

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease
```

## Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests
./gradlew connectedAndroidTest
```

## Team

**BugDroiders** - Development Team

## Version

**Version**: 1.0
**Version Code**: 1

## Support

For issues and questions, please contact the development team.

---

**Note**: This application requires an active internet connection to function properly as it relies on Firebase services for authentication and data storage.
