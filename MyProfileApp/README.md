# My Profile App

**Nama:** Ivan Nandira Mangunang
**NIM:** 123140094
**Kelas:** RB

A **Compose Multiplatform** profile application that displays user information with a clean, modern UI. Built with Kotlin Multiplatform targeting **Android** and **Desktop**.

---

## ✨ Features

- 🎨 **Custom Material 3 Theme** — Light & dark mode with curated color palette
- 👤 **Profile Header** — Circular avatar, name, and bio
- 📇 **Contact Details** — Email, phone, and location with Material icons
- 🔘 **Interactive Toggle** — Show/hide contact info with `AnimatedVisibility`
- 🧩 **Reusable Composables** — `ProfileHeader`, `InfoItem`, `ProfileCard`
- 📱 **Cross-Platform** — Runs on Android and Desktop from shared code

---

## 📁 Project Structure

```
MyProfileApp/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew.bat
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       └── gradle-wrapper.properties
├── composeApp/
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/kotlin/com/example/myprofileapp/
│       │   ├── App.kt                 ← Root composable
│       │   ├── Profile.kt             ← Data model
│       │   ├── ProfileScreen.kt       ← Main screen
│       │   ├── ProfileHeader.kt       ← Avatar + name + bio
│       │   ├── InfoItem.kt            ← Icon + label + value row
│       │   ├── ProfileCard.kt         ← Styled Card container
│       │   └── Theme.kt               ← Material 3 theme
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   ├── kotlin/.../MainActivity.kt
│       │   └── res/...
│       └── desktopMain/
│           └── kotlin/.../main.kt
└── README.md
```

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

| Profile Screen | Contact Details Expanded |
|:-:|:-:|
| ![Android Page](screenshot/android%20page.png) | ![Android Page 2](screenshot/android%20page%202.png) |

### Desktop

| Profile Screen | Contact Details Expanded |
|:-:|:-:|
| ![Desktop Page](screenshot/desktop%20page.png) | ![Desktop Page 2](screenshot/desktop%20page%202.png) |

---

## 🛠️ Tech Stack

| Component | Version |
|-----------|---------|
| Kotlin | 2.0.21 |
| Compose Multiplatform | 1.7.0 |
| Android Gradle Plugin | 8.5.2 |
| Gradle | 8.13 |
| Material 3 | Latest (bundled) |

---

## 📝 License

This project is for educational purposes.
