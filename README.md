<div align="center">

# ⚡ Gemini Switcher AI — Multi-Key Hub & Live Preview Engine

**Zero-Server Google Gemini Web Client with Intelligent Multi-Key Auto-Rotation, Live Sandbox Code Execution, Model Continuity, and Android Companion App.**

<p align="center">
  <strong>Developed with ❤️ by <a href="https://github.com/mirmalot">Tonmoy Mir Malot</a></strong>
</p>

[![Developer: Tonmoy Mir Malot](https://img.shields.io/badge/Developer-Tonmoy%20Mir%20Malot-blue?style=flat&logo=github)](https://github.com/mirmalot)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![Platform](https://img.shields.io/badge/Platform-Web%20%7C%20Android-emerald.svg)](https://github.com/mirmalot)
[![Gemini Models](https://img.shields.io/badge/Gemini%20API-2.5%20%7C%202.0%20%7C%201.5-blueviolet.svg)](https://ai.google.dev/)
[![Zero Server](https://img.shields.io/badge/Architecture-100%25%20Client--Side-success.svg)](#security--privacy)

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Live Preview Engine & View Modes](#-live-preview-engine--view-modes)
- [API Key Vault & 429 Auto-Rotation](#-api-key-vault--429-auto-rotation)
- [Developer Credits & Links](#-developer-credits--links)
- [Live Web App Deployment](#-live-web-app-deployment)
  - [Deploy to GitHub Pages](#1-deploy-to-github-pages)
  - [Deploy to Vercel](#2-deploy-to-vercel)
  - [Deploy to Netlify](#3-deploy-to-netlify)
- [Android App Integration & APK Download](#-android-app-integration--apk-download)
  - [Downloading & Installing from GitHub Releases](#download-and-install-from-github-releases)
  - [Automated GitHub Actions CI/CD](#automated-github-actions-cicd)
  - [Building Locally with Android Studio / Gradle](#building-locally-with-android-studio--gradle)
- [Security & Privacy Guarantee](#-security--privacy-guarantee)
- [License](#-license)

---

## 🌟 Overview

**Gemini Switcher AI** (also known as *Gemini Multi-Key Hub*) is an open-source, zero-server developer client built for Google Gemini.

It eliminates the single biggest obstacle when building or experimenting with Gemini models: **`HTTP 429 Resource Exhausted` rate limits and quota caps**.

- Store an unlimited pool of Gemini API keys securely in browser `localStorage`.
- When an API key encounters a 429 or quota exhaustion error, Gemini Switcher AI **instantly rotates to the next available healthy key in the vault**.
- **Model continuity and multi-turn context are preserved seamlessly** without dropping user progress or interrupting the generation stream.
- An integrated **Live Preview Engine** immediately renders generated HTML, CSS, and JavaScript inside a responsive sandboxed iframe with device testing frames (Desktop, Tablet, Mobile).

---

## 👨‍💻 Developer Credits & Links

**Gemini Switcher AI** was created and engineered with ❤️ by:

### **Tonmoy Mir Malot**
- 🌐 **GitHub Profile**: [@mirmalot](https://github.com/mirmalot)
- 🔗 **Direct URL**: [https://github.com/mirmalot](https://github.com/mirmalot)
- 💼 **Project Repository**: [https://github.com/malot-apps/Gemini-Switcher-AI](https://github.com/malot-apps/Gemini-Switcher-AI)

If this project helps you bypass rate limits and build apps faster, please consider starring the repository on GitHub!

---

## ✨ Key Features

### 👁️ 1. Live Preview Engine (AI Studio Interface)
- **Instant Code Execution**: Automatically detects and extracts full web applications from Gemini responses and executes them live inside a secure sandboxed `<iframe>`.
- **4 Flexible View Modes**:
  - 💬 **Chat Mode**: Conversational view for rapid ideation.
  - ⚡ **Split View Mode**: Side-by-side prompt pane and live preview engine (responsive on desktop/tablet).
  - 👁️ **Full Preview Mode**: Expands the live running application to full viewport.
  - 💻 **Code View Mode**: Syntax-highlighted code editor with line counts and file statistics.
- **Responsive Device Switcher**: Test apps in **Desktop (100%)**, **Tablet (768px)**, and **Mobile (375px)** frames.
- **Interactive Controls**: Refresh frame, open in new tab/window, and "Run in Preview" buttons on individual chat code blocks.

### 📥 2. Code Export & Download Options
- **Download HTML**: One-click download of the generated application as a standalone `.html` file.
- **Download ZIP Bundle**: Leverages JSZip to package the project (`index.html` + `README.md`) into a `.zip` archive.
- **Copy Code**: One-click clipboard copy for code blocks and full files with visual feedback.

### 🔑 3. API Key Vault & Unlimited Auto-Rotation
- **Local Vault Storage**: Store multiple Gemini API keys in browser `localStorage`.
- **Real-Time Key Telemetry**: Track request counts, 429 rate-limit counts, cooldown timers (60s), and active key status.
- **Key Health Check**: One-click ping to test key validity before running prompts.
- **Zero-Drop Failover**: Upon hitting 429 or `RESOURCE_EXHAUSTED`, the active key is placed on cooldown and the exact prompt is retried seamlessly with the next valid key.

### 🎯 4. Dynamic Model Discovery & Capabilities Registry
- **Live `models.list` Endpoint Integration**: Fetches real-time available models directly from the official Gemini API for the active API key—never rely on stale hardcoded lists.
- **Intelligent Capability Filtering**: Automatically detects `supportedGenerationMethods` to filter models into Chat-ready models (`generateContent`) while classifying specialized embedding, audio, and prediction models.
- **Model Diagnostics & Verification Suite**: One-click connectivity testing for any model or full automated testing suite across all chat models with latency telemetry (ms) and clear error explanations for quota, permission, and deprecated models.
- **Strict Model Continuity**: Target models and conversation context remain strictly preserved across auto-rotation events on 429 errors.
- **Automatic Auto-Recovery**: Models that do not support system personas automatically retry without them instead of failing requests.

### ⚡ 5. Real-Time Token Estimator
- **Live Token Estimation**: Real-time calculation based on the industry standard ~4 characters per token heuristic.
- **Itemized Breakdown Tooltip**: Hover over the token badge in the input bar to inspect input draft tokens, chat history tokens, and system instruction tokens.

### 📱 6. Native Android App & CI/CD
- **Native Android Wrapper**: Jetpack Compose and in-memory software-rendered WebView wrapper for smooth execution on mobile and cloud emulators.
- **Automated GitHub Actions**: `.github/workflows/build-apk.yml` automatically compiles and publishes APK releases to GitHub Releases.

---

## 🖥️ UI Layout Overview

```
+---------------------------------------------------------------------------------------------------------+
| [=] Gemini Switcher AI            [Chat] [Split View] [Code] [Preview]    [Key 1 Active]  [Export v]    |
+------------------------------------+--------------------------------------------------------------------+
| MODEL & VAULT CONFIGURATION        | LEFT PANE: CHAT & PROMPTING      | RIGHT PANE: LIVE PREVIEW ENGINE  |
|                                    |                                  |                                  |
| Target Model:                      | [User]                           | [Desktop] [Tablet] [Mobile] [O]  |
| [ gemini-2.0-flash             v ] |  Create a Cyberpunk Pomodoro...  | +------------------------------+ |
|                                    |                                  | |   00:25:00                   | |
| System Persona:                    | [Gemini]                         | |   [START] [PAUSE] [RESET]    | |
| [ Web App Builder              v ] |  Here is the complete app:       | |   (Neon Glowing Canvas Ring) | |
|                                    |  ```html                         | |                              | |
| API Key Vault:                     |  <!DOCTYPE html>...              | |                              | |
| +--------------------------------+ |  ```                             | +------------------------------+ |
| | Key 1 (Primary)  [ACTIVE] req:4 |  [Run in Preview] [Copy Code]    |                                  |
| | Key 2 (Backup A) [READY]  req:0 |                                  |                                  |
| +--------------------------------+ +----------------------------------+----------------------------------+
|                                    | (~340 ctx tokens | ~18 prompt)                   [Draft saved]      |
|                                    | [ Describe an app or game to generate... (Ctrl+Enter) ]   [Send ->] |
+------------------------------------+--------------------------------------------------------------------+
| Developed with ❤️ by Tonmoy Mir Malot (https://github.com/mirmalot)     | 100% Client-Side Privacy       |
+---------------------------------------------------------------------------------------------------------+
```

---

## 🔒 Security & Privacy Guarantee

- **Zero-Server Architecture**: All API requests are dispatched directly from your browser to Google's official endpoint: `https://generativelanguage.googleapis.com`.
- **No Middlemen**: Zero proxy servers, zero external databases, zero tracking analytics.
- **Local-Only Storage**: Your API keys never leave your machine; they are stored exclusively inside `window.localStorage`.

---

## 🚀 Live Web App Deployment

The entire web application is standalone in `index.html`:

### 1. Deploy to GitHub Pages
1. Push this repository to GitHub.
2. Navigate to **Settings** &rarr; **Pages**.
3. Under **Branch**, select `main` and `/ (root)`.
4. Click **Save**. Live at `https://<username>.github.io/<repository-name>/`.

### 2. Deploy to Vercel
```bash
npm install -g vercel
vercel --prod
```

### 3. Deploy to Netlify
Drag and drop the folder containing `index.html` onto [Netlify Drop](https://app.netlify.com/drop).

---

## 📱 Android App Integration & APK Download

### Download from GitHub Releases
1. Navigate to the **Releases** tab: [https://github.com/malot-apps/Gemini-Switcher-AI/releases](https://github.com/malot-apps/Gemini-Switcher-AI/releases).
2. Download `Gemini-Switcher-AI-debug.apk` onto your Android device.
3. Tap the file to install (grant "Install unknown apps" if prompted).

### Download from GitHub Actions Artifacts
1. Go to the **Actions** tab on GitHub: [https://github.com/malot-apps/Gemini-Switcher-AI/actions](https://github.com/malot-apps/Gemini-Switcher-AI/actions).
2. Select the latest completed run from the `Build & Release Android APK` workflow.
3. Scroll down to **Artifacts** and download the `Gemini-Switcher-AI-APK` zip containing the debug APK.

### Local Compilation with Gradle Wrapper
```bash
./gradlew assembleDebug
```
Locate the generated APK at:
`app/build/outputs/apk/debug/app-debug.apk`

---

## 📄 License

This project is open-source under the [MIT License](LICENSE).

Developed with ❤️ by **[Tonmoy Mir Malot](https://github.com/mirmalot)**.
