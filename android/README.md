# SmartReader AI 📖✨

> **Understand Every Word While You Read.**
> A Kindle + AI Tutor + Dictionary for Android. Open any PDF, tap a difficult word,
> and get a simple, **context-aware** explanation instantly.

This folder contains the native Android application. It is built with Kotlin,
Jetpack Compose, MVVM, Hilt, Room, and a swappable AI provider layer (Gemini first).

---

## ⚠️ Project status — read this first

This is a **working architectural foundation**, not a finished store-ready app.
A full build of everything in the original spec (Firebase Auth, OCR, EPUB, TTS,
quizzes, multi-language books, polished store assets) is months of work. What is
implemented here is a coherent, buildable skeleton centered on the product's
unique differentiator — **context-aware word explanations** — with clean seams
so the rest can be filled in incrementally.

| Area | Status |
|------|--------|
| Project structure, Gradle, version catalog | ✅ Complete |
| MVVM + Hilt DI + Navigation | ✅ Complete |
| Room database schema (books, bookmarks, highlights, vocabulary, sessions) | ✅ Complete |
| AI abstraction layer + **Gemini provider** | ✅ Complete & functional |
| Context extraction logic (word → sentence → paragraph) + unit tests | ✅ Complete |
| PDF rendering (Android `PdfRenderer`) | ✅ Complete |
| Library / Home, Reader, Vocabulary, Analytics, Settings screens | ✅ Functional |
| Freemium quota (20/day) + Play Billing wrapper | ✅ Wired |
| **PDF text-layer extraction** (needed for true tap-on-word) | 🟡 Pluggable stub — wire PDFBox-Android |
| Firebase Auth / Storage / Analytics | 🟡 Local session + rules included; needs `google-services.json` |
| Google Sign-In (Credential Manager) | 🟡 Stubbed entry point |
| OCR / EPUB / TTS / quizzes (optional upgrades) | ⬜ Not started — extension points documented |
| Launcher icon | ✅ Placeholder adaptive icon (replace with branded art) |
| Gradle wrapper JAR | ⬜ Run `gradle wrapper` or open in Android Studio to generate |

The one thing that turns "type a word" into "tap a word in the PDF" is a PDF
**text layer**. `PdfRenderer` can only draw pixels, so text extraction is a
separate dependency. See [Wiring real tap-on-word](#-wiring-real-tap-on-word).

---

## 🚀 Build instructions for beginners

1. **Install Android Studio** (Hedgehog or newer) from
   <https://developer.android.com/studio>. It installs the Android SDK for you.
2. **Open the project**: `File → Open` → select this `android/` folder.
   Android Studio will generate the Gradle wrapper and download dependencies.
3. **Add your secrets**: copy `local.properties.example` to `local.properties`
   and paste your free Gemini API key
   (<https://aistudio.google.com/app/apikey>):
   ```properties
   GEMINI_API_KEY=AIza...your_key...
   ```
4. **Run**: plug in a phone (USB debugging on) or start an emulator, then press
   the green ▶ **Run** button.
5. In the app: tap **Add book**, pick a PDF, open it, tap the ✨ button, type a
   word, and tap **Word** to see the AI explanation.

> No Android Studio? From a terminal with the Android SDK + JDK 17 installed:
> ```bash
> cd android
> gradle wrapper            # one-time, generates ./gradlew
> ./gradlew assembleDebug   # builds app/build/outputs/apk/debug/app-debug.apk
> ./gradlew test            # runs the ContextExtractor unit tests
> ```

---

## 🏗️ Folder architecture

```
android/
├── build.gradle.kts                 # root build config (plugins)
├── settings.gradle.kts              # modules + repositories
├── gradle/libs.versions.toml        # single source of dependency versions
├── firebase/storage.rules           # Storage security rules
└── app/
    ├── build.gradle.kts             # app module config, secrets injection
    ├── proguard-rules.pro           # R8 keep rules
    └── src/main/java/com/smartreader/ai/
        ├── app/                     # Application + MainActivity
        ├── di/                      # Hilt modules (DataModule, AiModule)
        ├── data/
        │   ├── local/               # Room: entities, DAOs, database
        │   ├── remote/ai/           # AiProvider, Prompts, Gemini API + provider
        │   └── repository/          # Book, Ai, AiUsage, Auth, Billing
        ├── domain/model/            # provider-agnostic models
        ├── pdf/                     # PdfDocument, ContextExtractor, TextExtractor
        └── ui/                      # theme, navigation, + one package per screen
            ├── splash · login · home · reader
            └── vocabulary · analytics · settings
```

### Architecture diagram (data flow for the main feature)

```
User taps word in Reader
        │
        ▼
ReaderViewModel ──► ContextExtractor (word + sentence + prev/next + paragraph)
        │
        ▼
   AiRepository  ──► AiUsageManager.tryConsume()  (enforce 20/day free quota)
        │           └─► auto-saves the word to VocabularyDao
        ▼
   AiProvider (interface)  ◄── swap Gemini / OpenAI / Claude here
        │
        ▼
   GeminiProvider ──► Gemini REST API ──► JSON ──► WordExplanation
        │
        ▼
   ModalBottomSheet shows: simple meaning · context meaning · example · synonyms · translation
```

---

## 🗄️ Database schema (Room)

| Table | Key columns | Purpose |
|-------|-------------|---------|
| `books` | id, title, filePath, totalPages, currentPage, lastOpenedAt | PDF library + progress |
| `bookmarks` | bookId, page, label | Saved pages |
| `highlights` | bookId, page, text, note, colorArgb | Highlights + notes |
| `vocabulary_words` | word (unique), meaning, example, dateLearned, mastered | Vocabulary Builder + flashcards |
| `reading_sessions` | bookId, startedAt, endedAt, pagesRead, dayEpoch | Feeds Reading Analytics + streak |

---

## 🔌 The AI abstraction layer (why models are swappable)

Everything talks to the `AiProvider` **interface** — never a concrete model.
Switching from Gemini to OpenAI or Claude = write one new class + change one Hilt
binding in `di/AiModule.kt`. Prompts live in one place (`data/remote/ai/Prompts.kt`)
and ask for strict JSON so responses parse deterministically.

```kotlin
interface AiProvider {
    suspend fun explainWord(context: WordContext, translateTo: TranslationLanguage): WordExplanation
    suspend fun simplifySentence(sentence: String): SentenceExplanation
    suspend fun explainParagraph(paragraph: String): ParagraphExplanation
    suspend fun chat(history: List<ChatMessage>, bookContext: String): String
}
```

---

## 🔍 Wiring real tap-on-word

`PdfRenderer` rasterizes pages but cannot read text, so the tap coordinate can't
yet be mapped to a word out of the box. Two-step path to enable it:

1. **Extract text** — add `com.tom-roush:pdfbox-android` and implement
   `PdfTextExtractor` (replace `StubPdfTextExtractor` binding in `DataModule`):
   ```kotlin
   PDFBoxResourceLoader.init(context)
   PDDocument.load(file).use { doc ->
       val stripper = PDFTextStripper().apply { startPage = page + 1; endPage = page + 1 }
       return stripper.getText(doc)
   }
   ```
2. **Map the tap** — overlay selectable text or use a text-positioning library to
   convert the tapped (x, y) into a character offset, then call
   `ReaderViewModel.explainWord(word, tapOffset)`. `ContextExtractor` already turns
   that offset into the full word/sentence/paragraph context.

For **scanned** PDFs (no text layer), run ML Kit Text Recognition over the page
bitmap from `PdfDocument.renderPage` — same `PdfTextExtractor` seam.

---

## 🔥 Firebase setup

1. Create a project at <https://console.firebase.google.com>.
2. Add an Android app with package `com.smartreader.ai`; download
   `google-services.json` into `android/app/`.
3. Uncomment the `google-services` plugin in `app/build.gradle.kts` and the root
   `build.gradle.kts`.
4. Enable **Authentication → Google**, **Storage**, and **Analytics**.
5. Deploy the included rules: `firebase deploy --only storage` (uses
   `firebase/storage.rules`).
6. Replace the local-session logic in `AuthManager`/`LoginScreen` with the real
   Credential Manager → `FirebaseAuth.signInWithCredential` flow (see the
   `TODO`s in `LoginScreen.kt`). Put your Web client ID in `local.properties`.

---

## 💳 Google Play Billing

`BillingManager` already connects, queries, purchases, acknowledges, and restores
the `premium_monthly` subscription, then flips the premium flag in `AiUsageManager`
(unlocking unlimited explanations). To go live:

1. Create the `premium_monthly` subscription in **Play Console → Monetize**.
2. Upload a signed build to the **Internal testing** track.
3. Add license testers, then test the purchase on a real device.

---

## 🚢 Deployment guide

1. Create a keystore: `keytool -genkey -v -keystore release.jks -alias smartreader -keyalg RSA -keysize 2048 -validity 10000`.
2. Add a `release` signing config in `app/build.gradle.kts` (load passwords from
   `local.properties`, never commit them).
3. Build the bundle: `./gradlew bundleRelease` → `app/build/outputs/bundle/release/app-release.aab`.
   R8/ProGuard shrinking is already enabled for release.
4. Upload the `.aab` to **Play Console**, complete the store listing, content
   rating, and data-safety form, then roll out to a testing track first.

---

## 🔐 Security checklist (implemented)

- API keys injected via `BuildConfig` from git-ignored `local.properties` — never hard-coded.
- Imported PDFs stored in app-private storage; `FileProvider` for controlled sharing.
- R8/ProGuard enabled with keep rules for DTOs/Retrofit/Room.
- Firebase Storage rules scope files per-user and cap size to 500 MB.
- DataStore for preferences; add `EncryptedSharedPreferences` (dependency included)
  for any future sensitive tokens.

---

## 🧭 Suggested next steps

1. Wire `PdfBoxTextExtractor` for true tap-on-word (highest impact).
2. Real Google Sign-In + Firebase Auth.
3. Record `reading_sessions` from the Reader to populate Analytics.
4. Highlight/bookmark UI in the Reader (DAOs already exist).
5. Optional upgrades: OCR, EPUB, TTS, AI quizzes.
```
