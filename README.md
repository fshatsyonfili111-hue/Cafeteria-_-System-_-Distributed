# Distributed Cafeteria Management System

A distributed system for managing campus cafeteria operations, featuring an Android mobile client and a Java-based distributed server.

## 🚀 Setup Steps

### 1. Backend Setup (Java Server)
1. Open **NetBeans IDE**.
2. Import the `Cafeterial_System_Distributed` project.
3. Ensure you have **MySQL** installed and running.
4. Run the SQL scripts (provided in `src/conf`) to create the `cafeteria` and `registrar_db` databases.
5. Update `src/java/com/campus/db/DBConnection.java` with your MySQL root password.
6. Run the project using **GlassFish** or **Tomcat** (configured on port `9070`).

### 2. Mobile Setup (Android App)
1. Open **Android Studio**.
2. Import the `Cafeteria_Mobile_App` project.
3. Open `app/src/main/java/com/cafeteria/mobile/api/RetrofitClient.java`.
4. Update the `BASE_URL` with your computer's IP address (e.g., `http://192.168.1.5:9070/Cafeterial_System_Distributed/`).
5. **Sync Gradle** and run the app on an emulator or a real device connected to the same network.

## 📦 Dependencies

### Backend
- **Jakarta Servlet API**: For handling distributed requests.
- **MySQL Connector/J**: For database connectivity.
- **Google GSON**: For JSON serialization.

### Mobile
- **Retrofit 2 & OkHttp**: For network communication.
- **Glide**: For image loading.
- **Room Persistence**: For local caching.
- **AndroidX Libraries**: Material Design, ConstraintLayout, etc.

## 💡 Example Usage

### 1. Student Login
- Enter Student ID and Password on the mobile app.
- The app sends an asynchronous request to `/api/login`.
- Upon success, the student's profile and photo are synchronized from the distributed server.

### 2. View Weekly Menu
- Navigate to the "Menu" tab.
- The app fetches real-time data from `/api/menu`.
- Even if the server is temporarily down, the app will attempt to show cached information to ensure availability.

### 3. Submit Feedback
- Students can rate their meals.
- Data is sent to `FeedbackServlet`, which ensures data consistency across the distributed database records.

---
*Developed as part of the Distributed Systems Final Project.*
