# 🚨 GirlSafety-SOS — Empowering Women's Safety 🛡️

<p align="center">
  <img src="https://img.shields.io/badge/Android-33D47E?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge" />
</p>

**GirlSafety-SOS** is a real-time emergency response application designed to provide an immediate safety net for women. With a single "SOS" trigger—either via a button or hardware volume keys—the app automatically broadcasts emergency alerts with live GPS coordinates to trusted contacts.

---

## 📌 Problem Statement
In high-stress situations, victims of danger often face:
*   **Time Constraints**: No time to dial numbers or type messages.
*   **Panic**: Difficulty explaining their exact location.
*   **Device Accessibility**: Inability to unlock the phone or open an app.
*   **Data Portability**: Losing emergency contact info when switching phones.

## 💡 The Solution
Our app simplifies emergency response into an **instant action**:
*   **Dual Trigger System**: SOS button in-app + **Volume Up Button (4x press)** trigger that works even when the app is closed.
*   **Automated Geolocation**: Sends a direct **Google Maps link** of the user's live coordinates.
*   **SMS-Based Alerting**: Reliable communication that works without a data connection.
*   **Cloud Persistence**: Securely stores contacts in **Firebase Firestore**.

---

## 🏗️ Project Architecture
The application follows a modular architecture:

### 📱 Presentation Layer (UI)
*   **MainActivity**: The primary SOS dashboard.
*   **AddNumbersActivity**: Cloud-synced emergency contact manager.
*   **LoginActivity / RegisterActivity**: Secure user authentication.

### ⚙️ Logic Layer
*   **SafetyAccessibilityService**: Monitors hardware buttons (Volume Up) to trigger alerts while the app is in the background or the screen is off.
*   **Location Engine**: Uses `FusedLocationProviderClient` for fresh, high-accuracy GPS tracking.
*   **SMS Dispatcher**: Uses `SmsManager` with Multipart support to ensure long messages with links are delivered.

### ☁️ Data Layer
*   **Authentication**: Secure login/registration via Firebase Auth.
*   **Storage**: Cloud Firestore real-time NoSQL database for contact management.

---

## 🔄 App Flow
1.  **Onboarding**: User creates a secure account.
2.  **Configuration**: User adds trusted contacts (synced to cloud).
3.  **Emergency Trigger**:
    *   **Manual**: Tap the SOS button in the app.
    *   **Panic Mode**: Press the **Volume Up button 4 times** quickly (works even if the app is closed).
4.  **Broadcast**: 
    *   App fetches fresh GPS coordinates.
    *   App pulls contacts from Firestore.
    *   Sends automated SMS with a Google Maps link to all contacts.

---

## ✨ Key Features
*   🚨 **Volume Button Trigger**: Activate SOS without even looking at or unlocking your phone.
*   📍 **Live Tracking**: Direct navigation links for responders.
*   📇 **Smart Contact Manager**: Add, Edit, and Delete contacts with cloud sync.
*   🔐 **Secure Profiles**: Data protected by Firebase security.
*   🎨 **Material UI**: High-contrast, easy-to-use interface.

---

## 🛠️ Tech Stack
| Category | Technology |
| :--- | :--- |
| **Language** | Java (JDK 8) |
| **Mobile Platform** | Android (Min SDK 26) |
| **Backend** | Firebase (Auth & Firestore) |
| **Location API** | Google Fused Location Provider |
| **UI Framework** | Material Design Components |

---

## 📂 Project Structure
```text
📁 GirlSafety-SOS
 ├── 📁 app
 │    ├── 📁 src/main/java (Source Code)
 │    │    └── 📁 com.example.girlssafety
 │    │         ├── MainActivity.java
 │    │         ├── AddNumbersActivity.java
 │    │         ├── SafetyAccessibilityService.java
 │    │         └── ...
 │    └── 📁 src/main/res (Resources)
 └── 📄 README.md
```

---

## 🚀 Getting Started
1. Clone the repo: `git clone https://github.com/ayush-ranjan9135/GirlSafety-SOS.git`
2. Add your `google-services.json` to the `/app` directory.
3. Enable **Accessibility Service** in your phone settings for "Girls Safety".
4. Run on a physical device for SMS/GPS testing.

---

## 👨‍💻 Author
**Ayush Ranjan**  
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/ayush-ranjan9135)

---

<p align="center">
  ⭐ If you find this project useful, please consider giving it a star on GitHub!
</p>
