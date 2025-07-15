
# Hawaii Countdown Widget

Android App Widget that shows "X Days Until Hawaii!" with a Nā Pali Coast background.

## How to use
1. Replace `app/src/main/res/drawable/napali_bg.jpg` with your own high‑resolution image.
2. Open the project in Android Studio (Giraffe or newer). Let it download any missing components.
3. Build & run on your device or push the repo to GitHub and use GitHub Actions to build an APK.

The target trip date is set in `HawaiiWidgetProvider.kt`:
```kotlin
private val targetDate: LocalDate = LocalDate.of(2025, 10, 8)
```
Adjust as needed.
