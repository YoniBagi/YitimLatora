# עיתים לתורה - Yitim Latora App

An Android application for accessing Torah, Halacha, and Jewish thought content from itim-latora.org, **fully compliant with Google Play policies**.

## ✅ Google Play Policy Compliance

This app is **NOT just a WebView wrapper**. It includes substantial native functionality and value beyond simply displaying a website:

### Native Features

1. **Native Home Screen**
   - Beautiful native UI with Material Design 3
   - Quick access cards to different sections
   - Native navigation and interactions
   - Entirely built with Jetpack Compose

2. **Bookmark Management**
   - Save favorite pages with native bookmark system
   - Stored locally using Room database
   - Native bookmark list with search and management
   - Delete and organize bookmarks natively

3. **Browsing History**
   - Automatic history tracking
   - View and manage browsing history
   - Clear history functionality
   - Stored locally for privacy

4. **Settings & Preferences**
   - Text size adjustment (75%-150%)
   - Dark mode toggle
   - JavaScript enable/disable
   - Customizable home page
   - All settings saved using DataStore

5. **Enhanced WebView Experience**
   - Native navigation controls (back/forward/home)
   - Progress indicator
   - Native share functionality
   - Open in external browser option
   - Custom toolbar with bookmark integration

6. **Native Navigation**
   - Bottom navigation bar
   - Smooth transitions between screens
   - State preservation

## 🏗️ Architecture

### Technology Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Modern Android with Repository pattern
- **Database**: Room for local storage
- **Preferences**: DataStore for settings
- **Navigation**: Navigation Compose
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 36

### Project Structure

```
app/src/main/java/com/yitimlatora/
├── data/
│   ├── dao/                    # Data Access Objects
│   │   ├── BookmarkDao.kt      # Bookmark database operations
│   │   └── HistoryDao.kt       # History database operations
│   ├── database/
│   │   └── AppDatabase.kt      # Room database configuration
│   ├── model/                  # Data models
│   │   ├── Bookmark.kt         # Bookmark entity
│   │   ├── HistoryItem.kt      # History entity
│   │   └── QuickLink.kt        # Quick link model
│   ├── preferences/
│   │   └── UserPreferences.kt  # DataStore preferences manager
│   └── repository/
│       └── YitimLatoraRepository.kt  # Data repository
├── ui/
│   ├── navigation/             # Navigation configuration
│   │   ├── BottomNavItem.kt    # Bottom nav items
│   │   ├── Navigation.kt       # Navigation graph
│   │   └── Screen.kt           # Screen routes
│   ├── screen/                 # UI screens
│   │   ├── HomeScreen.kt       # Native home screen
│   │   ├── BrowseScreen.kt     # Browse screen
│   │   ├── WebViewScreen.kt    # Enhanced WebView
│   │   ├── BookmarksScreen.kt  # Bookmarks management
│   │   └── SettingsScreen.kt   # Settings screen
│   └── theme/                  # App theme
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── MainActivity.kt             # Main activity
└── YitimLatoraApplication.kt   # Application class
```

## 🎨 Features Detail

### Home Screen
- **Native UI**: Built entirely with Compose, not a WebView
- **Quick Links**: Native cards for rapid navigation to key sections
- **Categories**: Articles, Lessons, Parsha, Halacha, Thought, About, Contact
- **Material Design**: Modern, beautiful UI following Material Design 3 guidelines

### Browse/WebView Screen
- **Enhanced Controls**: Native bottom bar with back/forward/home buttons
- **Bookmarking**: One-tap bookmark with custom title dialog
- **Progress Indicator**: Native loading indicator
- **Sharing**: Native Android share sheet integration
- **Settings Integration**: Respects user preferences (text size, JavaScript)
- **History**: Automatically saves visited pages

### Bookmarks Screen
- **Native List**: Fully native RecyclerView-like experience with Compose
- **Management**: Delete bookmarks with confirmation dialog
- **Timestamp Display**: Shows when each bookmark was created
- **Empty State**: Beautiful empty state with guidance

### Settings Screen
- **Text Size Control**: Slider for adjusting WebView text size (75%-150%)
- **Dark Mode**: Toggle for dark theme (currently affects app, can extend to WebView)
- **JavaScript Toggle**: Enable/disable JavaScript in WebView
- **Home Page**: Customize starting URL
- **History Management**: View history in bottom sheet, clear all history
- **About Section**: App version and information

## 📱 User Experience

The app provides significant value through native features:

1. **Faster Access**: Quick links on home screen provide instant access to key sections
2. **Better Organization**: Native bookmarks are easier to manage than browser bookmarks
3. **Customization**: Text size, dark mode, and other settings enhance reading experience
4. **History Tracking**: Automatic history makes it easy to return to previously viewed content
5. **Offline Indicators**: Native error handling when network is unavailable
6. **Native Sharing**: Seamless integration with Android's share system

## 🚀 Building the App

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK 36

### Build Commands

```bash
# Debug build
./gradlew assembleDebug

# Release build
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

### Configuration

The app is pre-configured for `itim-latora.org`. To use with a different website:

1. Update the home page URL in `UserPreferences.kt`
2. Update quick links in `HomeScreen.kt`
3. Update app name in `strings.xml`
4. Update package name and application ID if needed

## 📋 Google Play Submission Checklist

- ✅ App has native home screen with original content
- ✅ Substantial native functionality (bookmarks, history, settings)
- ✅ Not just a wrapper around a website
- ✅ Provides value beyond visiting the website directly
- ✅ Uses WebView as one component, not the entire app
- ✅ Native UI/UX elements throughout
- ✅ Local data storage (Room + DataStore)
- ✅ Proper error handling and offline states
- ✅ Material Design guidelines followed
- ✅ Responsive to user preferences
- ✅ Native Android integration (sharing, etc.)

## 🔒 Privacy & Security

- All data stored locally on device
- No analytics or tracking (can be added if needed)
- Respects user privacy settings
- No data sent to external servers (except website content in WebView)
- Users can clear history anytime

## 📄 License

This app is designed for itim-latora.org. Ensure you have proper permissions to create an app for this content.

## 🛠️ Future Enhancements (Optional)

- Push notifications for new content
- Offline reading mode
- Advanced search within bookmarks
- Categories for bookmarks
- Export/import bookmarks
- Reading progress tracking
- Notes and highlights
- Font customization
- Reading statistics

---

**Note**: This app demonstrates how to properly integrate web content into a native Android app while maintaining Google Play compliance by providing substantial native functionality and value.

