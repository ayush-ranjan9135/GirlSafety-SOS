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
In critical situations, victims of harassment or danger often face:
*   **Time Constraints**: No time to dial a number or type a message.
*   **Panic**: Difficulty in explaining their exact location.
*   **Connectivity Issues**: Reliance on data for messaging apps (WhatsApp/Telegram) which might be slow.
*   **Device Loss**: Losing contact lists when moving to a new phone.

## 💡 The Solution
Our app addresses these gaps by providing:
*   **One-Tap Activation**: A prominent SOS button for instant use.
*   **Automated Geolocation**: Sends a direct **Google Maps link** of the user's live location.
*   **SMS-Based Alerting**: Works even without a stable data connection for the recipient.
*   **Cloud Synchronization**: Securely stores emergency contacts in **Firebase Firestore**, ensuring data persists across devices.

---

## 🏗️ Project Architecture
The application follows a **Modular Layered Architecture** to ensure reliability and scalability:

1.  **Presentation Layer (UI)**: Built with XML and Material Design. Includes activities for Authentication, Contact Management, and the main SOS dashboard.
2.  **Service Layer (Logic)**:
    *   **Location Service**: Uses Google's `FusedLocationProviderClient` for battery-efficient, high-accuracy GPS tracking.
    *   **SMS Gateway**: Utilizes `SmsManager` to dispatch background messages to multiple recipients.
3.  **Data Layer (Backend)**: 
    *   **Firebase Auth**: Secure login/registration.
    *   **Cloud Firestore**: Real-time NoSQL database to manage user-specific emergency contact lists.

---

## 🔄 App Flow
The workflow is designed to be frictionless:

1.  **Onboarding**: User creates an account or logs in via Firebase.
2.  **Configuration**: User adds up to 5 trusted emergency contacts (stored in Firestore).
3.  **Emergency Trigger**: 
    *   User presses the **SOS Button** on the `MainActivity`.
    *   App checks for `SEND_SMS` and `ACCESS_FINE_LOCATION` permissions.
4.  **Data Processing**: App captures the **Latitude & Longitude** and constructs a Google Maps URL.
5.  **Alert Dispatch**: The system fetches the contact list from the cloud and sends the SOS SMS to every contact simultaneously.

---

## ✨ Key Features
*   🔐 **Secure Cloud Auth**: Personalized accounts so your safety circle is always yours.
*   📍 **High-Accuracy Tracking**: Real-time GPS integration with Google Play Services.
*   📇 **Dynamic Contact Manager**: Easily Add, Edit, or Delete contacts with immediate cloud sync.
*   🚀 **Zero-Delay SOS**: Optimized to send alerts within seconds of the trigger.
*   🎨 **Material UI**: Modern, clean, and intuitive interface for ease of use under stress.

---

## 🛠️ Tech Stack
| Category | Technology |
| :--- | :--- |
| **Language** | Java (JDK 8+) |
| **Mobile Platform** | Android (Min SDK 26) |
| **Database** | Firebase Cloud Firestore |
| **Authentication** | Firebase Auth |
| **Location API** | Google Fused Location Provider |
| **UI Design** | XML, Material Design Components |

---

## 🚀 Getting Started

1.  **Clone the Repository**:
    
