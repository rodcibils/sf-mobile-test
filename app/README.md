# Seed App (Android)

A demo Android application developed in Kotlin using Jetpack Compose. The app communicates with a local Node.js API to display and validate expiring QR code seeds.

---

## 🛠 Installation

1. Clone the repository:

```bash
git clone https://github.com/rodcibils/sf-mobile-test.git
cd sf-mobile-test
```

2. Open the project in **Android Studio (Giraffe or newer - I've used Android Studio Meerkat Feature Drop | 2024.3.2)**.

3. Let Gradle sync and download dependencies.

---

## ▶️ Build & Run the App

### From Android Studio:

- Open the `app/` module
- Select a device or emulator
- Click **Run ▶️**

### From CLI:

```bash
./gradlew installDebug
```

---

## 🌐 Start the Local API

This app requires the local API to be running.

Refer to [Local API README](https://github.com/rodcibils/sf-mobile-test/blob/master/api/README.md) for detailed instructions to:

- Install dependencies
- Start the local API server
- Validate seeds

---

## 📡 Getting Your Local IP Address

To access the API from a physical Android device, all devices must be on the same Wi-Fi network.

Use the following to get your local IP:

### macOS

```bash
ipconfig getifaddr en0
```

### Windows (Command Prompt)

```cmd
ipconfig
```

Look for `IPv4 Address`.

### Linux

```bash
hostname -I
```

---

## 🔧 Configure Android App to Use Local API

1. Edit this file:

```
app/src/main/res/xml/network_security_config.xml
```

Replace the existing IP with your local IP (e.g. `192.168.1.123`).

2. Edit this Kotlin file:

```
app/src/main/java/com/rodcibils/sfmobiletest/api/HttpClientProvider.kt
```

Update this line:

```kotlin
private const val BASE_URL = "http://192.168.1.123:3000"
```

Then rebuild the app.

---

## 🧱 App Architecture

This project follows a layered architecture using MVVM and modern Android practices:

```
app/
├── ui/                  // Jetpack Compose screens & UI logic
│   └── screen/
│       ├── scan/        // ScanScreen + ScanViewModel
│       └── qrcode/      // QRCodeScreen + QRCodeViewModel
├── model/               // Data models (e.g. QRCodeSeed)
├── repo/                // Repository layer to abstract data sources
├── api/                 // Remote API logic (Ktor)
├── local/               // Local storage (EncryptedSharedPreferences)
├── util/                // Reusable helpers (e.g. DateUtils, QRCode generator)
└── di/                  // Koin DI module (AppModule.kt)
```

---

## 🔄 ViewModel + StateFlow Pattern

All screens use `ViewModel` + `StateFlow` for state management.

### State Flow Structure:

```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: T) : UiState()
    data class Error(val message: String) : UiState()
}
```

### Usage in UI:

```kotlin
val uiState by viewModel.uiState.collectAsState()

when (val state = uiState) {
    is UiState.Loading -> ...
    is UiState.Success -> ...
    is UiState.Error -> ...
}
```

This ensures:

- Reactive, lifecycle-aware UI
- Testable ViewModel logic
- Separation of concerns

---

## 📚 Libraries

- **Jetpack Compose** – Declarative UI toolkit
- **Jetpack Navigation** - Navigation library for Jetpack Compose
- **Koin** – Lightweight dependency injection
- **Ktor** – HTTP client for networking
- **ZXing** – QR Code generation
- **ML Kit Barcode Scanning** – Camera-powered QR Code scanning
- **CameraX** – Camera lifecycle and preview integration
- **kotlinx.serialization** – JSON parsing
- **EncryptedSharedPreferences** – Secure local storage (to be deprecated)

---

### 🪝 Git Hooks Setup & Behavior

This project uses **Git hooks** to enforce code quality standards **before committing or pushing code**.

#### ✅ What do the hooks do?

- `pre-commit`: runs `ktlint` to check for code formatting and style issues
- `pre-push`: runs both `ktlint` and **unit tests**, ensuring your code is linted and tested before being pushed

These hooks help catch errors early and promote a clean development workflow.

---

#### 🔧 How are the hooks installed?

Hooks are automatically installed when the Android project is built for the first time (`preBuild` Gradle task).

This is configured in `app/build.gradle.kts`:

```kotlin
val installGitHooks by tasks.registering(Copy::class) {
    val sourceDir = rootProject.file("scripts/git-hooks")
    val gitDir = rootProject.file("../.git/hooks")

    from(sourceDir)
    into(gitDir)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    rename("pre-commit", "pre-commit")
    rename("pre-push", "pre-push")

    doLast {
        gitDir.resolve("pre-commit").setExecutable(true)
        gitDir.resolve("pre-push").setExecutable(true)
        println("✅ Git hooks installed")
    }
}

afterEvaluate {
    tasks.named("preBuild").configure {
        dependsOn(installGitHooks)
    }
}
```

---

#### 📂 Location of Hook Scripts

Git hook scripts are located in:

```
scripts/git-hooks/
├── pre-commit   # Runs ktlint
└── pre-push     # Runs ktlint + unit tests
```

You can customize these scripts if needed.

---

#### 💡 Note

If the hook scripts are not being triggered:

- Ensure they are **installed** (check `.git/hooks/` folder)
- Ensure the hook files are **executable** (`chmod +x` if needed)
- Rebuild the project to re-trigger hook installation

---

## ⚙️ Considerations

- Git hooks are auto-installed at build time (see `app/build.gradle.kts`)
- Koin is used instead of Dagger2 for DI
- This implementation assumes API runs locally and all devices are on the same Wi-Fi network
- Local IP must be updated in:
  - `network_security_config.xml`
  - `HttpClientProvider.kt`

---

## 🧩 Known TODOs

1. No authentication/security implemented in API
2. API is local-only; would benefit from deployment to a remote environment
3. `ktlint` is the linter; `.editorconfig` used to resolve Compose casing issues  
   - `detekt` may offer better support for Compose
4. Git hooks provide local CI; a GitHub Actions pipeline should be added for PR validation
5. Modularization is not yet applied; separating `ui`, `api`, `storage` could improve build times
6. `EncryptedSharedPreferences` is deprecated → should migrate to **Proto DataStore**
7. API stores only the **latest** seed in memory. Multiple devices would invalidate previous seeds. This is acceptable for local testing but not suitable for production

---

## 🚀 Roadmap & Improvements

| Area          | Suggested Improvements                                                                 |
|---------------|------------------------------------------------------------------------------------------|
| 🧠 Architecture | Modularize code into `:ui`, `:data`, `:domain`, etc.                                    |
| 📦 CI/CD        | Add GitHub Actions to run lint + unit tests on each PR                                 |
| 🔐 Security     | Add basic auth/token-based validation to API                                            |
| 📖 Docs         | Extract long docs to `docs/architecture.md`, `testing.md`, `networking.md`, etc.       |
| 🧪 Testing      | Add integration tests for ViewModels, API, local data, and QR parsing logic             |

---

## 📄 License

MIT
