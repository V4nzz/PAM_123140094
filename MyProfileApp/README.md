# My Profile App

A **Compose Multiplatform** profile application that displays user information with a clean, modern UI. Built with Kotlin Multiplatform targeting **Android** and **Desktop**.

Now powered by **MVVM architecture** with proper state management, an **Edit Profile** form, and a **Dark Mode** toggle.

---

## ✨ Features

- 🏗️ **MVVM Architecture** — Clean separation: `data/`, `viewmodel/`, `ui/` layers
- 📊 **StateFlow** — `ProfileViewModel` exposes immutable `StateFlow<ProfileUiState>`
- 🎨 **Custom Material 3 Theme** — Light & dark mode with curated color palette
- 🌙 **Dark Mode Toggle** — Switch between light/dark via ViewModel
- ✏️ **Edit Profile** — Inline form to update name & bio with animated visibility
- 👤 **Profile Header** — Circular avatar, name, and bio
- 📇 **Contact Details** — Email, phone, and location with Material icons
- 🔘 **Interactive Toggle** — Show/hide contact info with `AnimatedVisibility`
- 🧩 **Reusable Composables** — `ProfileHeader`, `InfoItem`, `ProfileCard`, `LabeledTextField`
- 📱 **Cross-Platform** — Runs on Android and Desktop from shared code

---

## 🏛️ Architecture (MVVM)

```
┌─────────────────────────────────────┐
│          UI (Composables)           │
│  ProfileScreen · ProfileHeader      │
│  InfoItem · ProfileCard             │
│  LabeledTextField                   │
└──────────┬──────────────────────────┘
           │ State ↓  Events ↑
┌──────────▼──────────────────────────┐
│        ProfileViewModel             │
│  MutableStateFlow<ProfileUiState>   │
│  updateName() · updateBio()         │
│  toggleDarkMode() · toggleEditMode()│
│  saveProfile()                      │
└──────────┬──────────────────────────┘
           │
┌──────────▼──────────────────────────┐
│        ProfileUiState (data)        │
│  name · bio · email · phone         │
│  location · isDarkMode · isEditing  │
└─────────────────────────────────────┘
```

**State flows down** from ViewModel → UI via `collectAsState()`.
**Events flow up** from UI → ViewModel via function calls.

---

## 📁 Project Structure

```
MyProfileApp/
├── composeApp/src/
│   ├── commonMain/kotlin/com/example/myprofileapp/
│   │   ├── App.kt                    ← Root composable (wires ViewModel)
│   │   ├── Profile.kt                ← Data model + sample data
│   │   ├── ProfileScreen.kt          ← Main screen (collects StateFlow)
│   │   ├── ProfileHeader.kt          ← Avatar + name + bio (stateless)
│   │   ├── InfoItem.kt               ← Icon + label + value row (stateless)
│   │   ├── ProfileCard.kt            ← Styled Card container (stateless)
│   │   ├── Theme.kt                  ← Material 3 theme (light/dark)
│   │   ├── data/
│   │   │   └── ProfileUiState.kt     ← Immutable UI state data class
│   │   ├── viewmodel/
│   │   │   └── ProfileViewModel.kt   ← ViewModel with StateFlow
│   │   └── ui/
│   │       └── LabeledTextField.kt   ← Reusable stateless text field
│   ├── androidMain/
│   │   └── kotlin/.../MainActivity.kt
│   └── desktopMain/
│       └── kotlin/.../main.kt
├── gradle/libs.versions.toml
├── README.md
└── ...
```

---

## 🔄 BEFORE vs AFTER

| Aspect | Before | After |
|--------|--------|-------|
| **Architecture** | No separation — state inside composables | MVVM — ViewModel holds all state |
| **State** | `remember { mutableStateOf() }` | `StateFlow<ProfileUiState>` + `collectAsState()` |
| **Data** | `Profile` passed directly to screen | `ProfileUiState` managed by `ProfileViewModel` |
| **Edit feature** | ❌ Not available | ✅ Edit name & bio with `LabeledTextField` |
| **Dark mode** | System-only (no user control) | ✅ Manual toggle via Switch in UI |
| **Data flow** | Mixed (state inside composables) | Unidirectional: State ↓ / Events ↑ |

### Key Refactoring Points

1. **State Hoisting** — All business state moved from composables to `ProfileViewModel`
2. **Immutable Updates** — `ProfileUiState.copy()` ensures predictable state changes
3. **Edit Buffers** — Edits go to a temporary buffer; only committed on Save
4. **Stateless Composables** — `ProfileHeader`, `InfoItem`, `ProfileCard`, `LabeledTextField` accept data via parameters

---

## 🚀 How to Run

### Android
1. Open the project in **Android Studio**
2. Select the `composeApp` run configuration
3. Choose an emulator or connected device
4. Click **Run ▶**

### Desktop
Run from terminal:
```bash
./gradlew composeApp:run
```

---

## 🖼️ Screenshots

### Android

| Profile Home | Edit Mode | Contact Info |
|:-:|:-:|:-:|
| ![Android 1](screenshot/android%20page%202.1.png) | ![Android 2](screenshot/android%20page%202.2.png) | ![Android 3](screenshot/android%20page%202.3.png) |

### Desktop

| Profile Home | Edit Mode | Contact Info |
|:-:|:-:|:-:|
| ![Desktop 1](screenshot/desktop%20page%202.1.png) | ![Desktop 2](screenshot/desktop%20page%202.2.png) | ![Desktop 3](screenshot/desktop%20page%202.3.png) |


---

## 🛠️ Tech Stack

| Component | Version |
|-----------|---------|
| Kotlin | 2.0.21 |
| Compose Multiplatform | 1.7.0 |
| Lifecycle ViewModel (KMP) | 2.8.3 |
| Android Gradle Plugin | 8.5.2 |
| Gradle | 8.13 |
| Material 3 | Latest (bundled) |

---

## 📝 License

This project is for educational purposes.
