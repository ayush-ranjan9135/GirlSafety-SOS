# 🚨 GirlSafety-SOS — Empowering Women's Safety 🛡️

<p align="center">
  <img src="https://img.shields.io/badge/Android-33D47E?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Status-Active-brightgreen?style=for-the-badge" />
</p>

**GirlSafety-SOS** is a real-time emergency response application designed to provide an immediate safety net for women. With a single "SOS" trigger, the app automatically fetches precise GPS coordinates and broadcasts emergency alerts to trusted contacts via SMS.

---

## 📌 Problem Statement
In critical situations, victims of harassment or danger often face several challenges:
*   **Time Constraints**: There is rarely enough time to dial a phone number or type a manual message.
*   **Panic & Disorientation**: Under stress, explaining an exact location to responders is difficult.
*   **Connectivity Gaps**: Relying on data-heavy apps (like WhatsApp) can be risky in low-signal areas.
*   **Data Portability**: Losing emergency contact info when switching devices.

## 💡 The Solution
Our app simplifies emergency communication into a **one-click action**:
*   **One-Tap Activation**: A high-visibility SOS button for instant help.
*   **Automated Geolocation**: Real-time GPS tracking that sends a direct **Google Maps link**.
*   **SMS-Based Alerting**: Reliable communication that works even without a stable data connection.
*   **Cloud Persistence**: Securely stores trusted contacts in **Firebase Firestore**, ensuring your safety circle is accessible from any device.

---

## 🏗️ Project Architecture
The application follows a modular architecture to ensure reliability and clean code:

### 📱 Presentation Layer
*   **Activities**: `MainActivity` (SOS dashboard), `AddNumbersActivity` (Contact manager), `LoginActivity`, and `RegisterActivity`.
*   **UI/UX**: Clean XML layouts using **Material Design Components** for high visibility.

### ⚙️ Logic Layer
*   **Location Engine**: Uses Google's `FusedLocationProviderClient` for battery-efficient and high-accuracy GPS coordinates.
*   **SMS Dispatcher**: Leverages the `SmsManager` API to broadcast background messages to multiple recipients.
*   **Permission Handler**: Dynamic runtime checks for Location and SMS permissions.

### ☁️ Data Layer
*   **Authentication**: Managed by **Firebase Auth** (Email/Password).
*   **Storage**: **Cloud Firestore** stores user-specific emergency contacts in a scalable NoSQL format.

---

## 🔄 App Flow
The workflow is designed to minimize the time between "danger" and "alert":

1.  **Onboarding**: User creates a secure account.
2.  **Configuration**: User adds up to 5 trusted contacts in the "Manage Contacts" section.
3.  **Emergency Trigger**:
    *   User taps the SOS button.
    *   App fetches **Latitude & Longitude** via GPS.
    *   App retrieves the contact list from **Firestore** in real-time.
4.  **Broadcast**: System generates a Google Maps link and sends a custom SOS SMS to all saved contacts simultaneously.
5.  **Feedback**: The user receives visual confirmation (Toast) once the alerts are dispatched.

---

## ✨ Key Features
*   🚨 **Instant SOS**: Trigger emergency alerts in under 2 seconds.
*   📍 **Live Tracking**: Inclusion of a live Google Maps link for immediate navigation.
*   📇 **Smart Contact Management**: Add, Edit, and Delete contacts with cloud synchronization.
*   🔐 **Secure User Profiles**: Your data is protected by Firebase security rules.
*   🎨 **Stress-Free UI**: Large buttons and clear icons designed for emergency use.

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
 │    │         └── ...
 │    └── 📁 src/main/res (Resources)
 │         ├── 📁 layout (UI XMLs)
 │         └── 📁 drawable (Icons/Images)
 └── 📄 README.md
```

---

## 🚀 Getting Started

### Prerequisites
*   Android Studio Ladybug or later.
*   A Firebase project with `google-services.json` in the `/app` folder.
*   A physical Android device (recommended for testing SMS and GPS).

### Installation
1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/ayush-ranjan9135/GirlSafety-SOS.git
    ```
2.  **Open in Android Studio**: Import the project and let Gradle sync.
3.  **Run**: Click the "Run" button to install the app.

---

## 👨‍💻 Author
**Ayush Ranjan**  
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/ayush-ranjan9135)

---

<p align="center">
  ⭐ If you find this project useful, please consider giving it a star on GitHub!
</p>
