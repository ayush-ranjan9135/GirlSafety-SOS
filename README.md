<h1 align="center">🚨 GirlSafety-SOS</h1>

<p align="center">
  A robust personal safety application designed to provide 
  <strong>instant emergency assistance</strong> to women in distress with one tap.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Android-12.0%2B-brightgreen.svg" />
  <img src="https://img.shields.io/badge/Backend-Firebase-orange.svg" />
  <img src="https://img.shields.io/badge/Language-Java-blue.svg" />
  <img src="https://img.shields.io/badge/Status-Active-success.svg" />
</p>

---

## 📌 Problem Statement
In today's world, personal safety is a paramount concern. Many existing solutions are either too complex to use during an actual emergency or fail to provide precise location data when it's needed most. Often, a victim doesn't have the time to make a call or type a detailed message.

## 💡 The Solution
**GirlSafety-SOS** simplifies emergency communication into a **one-click action**. 
- **Instant Alerts**: Sends SMS to multiple contacts simultaneously.
- **Real-time Tracking**: Includes a direct Google Maps link to the user's current coordinates.
- **Cloud-Synced Contacts**: Emergency contacts are stored securely in the cloud (Firestore), ensuring they are accessible even if the phone is changed.

---

## 🏗️ Project Architecture
The project follows a modular Android architecture, ensuring a clean separation of concerns:

- **UI Layer**: Activities (`MainActivity`, `AddNumbersActivity`, `LoginActivity`, `RegisterActivity`) and XML Layouts using Material Design.
- **Logic Layer**: Handles runtime permission checks, real-time location fetching, and SMS dispatching.
- **Data Layer**: 
    - **Firebase Auth**: Manages user identity and secure session handling.
    - **Cloud Firestore**: Stores and retrieves emergency contact lists per user.
    - **Fused Location API**: Provides high-accuracy GPS coordinates.

---

## 🔄 App Flow
1.  **Launch**: User is greeted with a sleek Splash Screen.
2.  **Authentication**: New users register; returning users log in securely via Firebase.
3.  **Setup**: Users navigate to "Manage Contacts" to add phone numbers of trusted individuals.
4.  **Emergency**: In danger, the user presses the **SOS Button**.
5.  **Action**:
    *   App fetches the **Latitude & Longitude**.
    *   Generates a **Google Maps link**.
    *   Retrieves the **Contact List** from Firestore.
    *   Sends an **Automated SMS** to all contacts.

---

## ✨ Key Features
- 🚨 **One-Tap SOS Alert**: Instant help trigger for high-stress situations.
- 📍 **Precise Location**: Integration with Google Fused Location Provider for better accuracy.
- 📲 **Dynamic SMS**: Custom SOS messages with live location links.
- 📇 **Contact Management**: Add, Edit, and Delete emergency contacts with cloud sync.
- 🔐 **Secure Auth**: Personalized data protection using Firebase.
- 🎨 **Intuitive UI**: Clean, Material Design interface for stress-free navigation.
- 🔔 **Alert Notifications**: Immediate feedback on message delivery status.

---

## 🛠️ Tech Stack
| Component | Technology |
| :--- | :--- |
| **Language** | Java |
| **IDE** | Android Studio |
| **Database** | Firebase Cloud Firestore |
| **Auth** | Firebase Authentication |
| **Location** | Google Play Services (Location / Maps API) |
| **Build Tool** | Gradle |
| **UI** | Material Design Components |

---

## 📂 Project Structure
```text
📁 GirlSafety-SOS
 ├── 📁 app
 │    ├── 📁 src/main/java (Java Source Code)
 │    └── 📁 src/main/res (Layouts & Resources)
 ├── 📁 gradle
 ├── 📄 build.gradle
 ├── 📄 settings.gradle
 └── 📄 README.md
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Ladybug or later.
- A Firebase Project (add your `google-services.json` to the `app/` directory).
- A physical device with a SIM card (for SMS testing).

### Installation
1.  **Clone the Repo**:
    ```bash
    git clone https://github.com/ayush-ranjan9135/GirlSafety-SOS.git
    ```
2.  **Open in Android Studio**: Import the project and wait for Gradle sync.
3.  **Firebase Setup**: 
    - Enable Email/Password Auth in Firebase Console.
    - Create a Firestore Database.
4.  **Run**: Click the "Run" button in Android Studio.

---

## 🔐 Permissions Used
The app requires the following permissions to function effectively:
- 📩 `SEND_SMS`: To send emergency alerts.
- 📍 `ACCESS_FINE_LOCATION`: To get accurate GPS data.
- 📞 `CALL_PHONE`: For quick emergency calling features.
- 🌐 `INTERNET`: To sync contacts with the cloud.

---

## 📌 Future Enhancements
- [ ] ✔ Voice-Activated SOS (Trigger by keywords)
- [ ] ✔ Background Tracking Mode
- [ ] ✔ AI-based Danger Detection (using sensors)
- [ ] ✔ Nearby Safe Places (Police stations, Hospitals)

---

## 👨‍💻 Author
**Ayush Ranjan**  
GitHub: [@ayush-ranjan9135](https://github.com/ayush-ranjan9135)

---

<p align="center">
  ⭐ If you find this project useful, please consider giving it a star!
</p>
