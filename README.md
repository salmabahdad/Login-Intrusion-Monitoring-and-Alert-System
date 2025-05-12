# 🔐 Intrusion Detection System (IDS)

  This project is a lightweight **Intrusion Detection System** built in Java, designed to monitor log files in real time, detect potential intrusions, and       alert administrators through multiple channels.
---

## 📌 Features

- ✅ Detects brute-force attacks based on failed login attempts
- 📧 Sends real-time email alerts using Gmail SMTP
- 🌍 GeoIP location lookup with MaxMind database
- 📤 Exports:
  - CSV with IP and failure count
  - JSON with geolocation data
- 🗺️ Visualizes IP origins on an interactive Leaflet.js map

---

## 🏗️ Project Structure
<pre> <code>
Intrusion-Detection-System/
├── src/
│ ├── Main.java
| |–– map.html
│ └── services/
│ ├── LogAnalyzer.java
│ ├── EmailAlertService.java
│ ├── GeoIPService.java
│ ├── CSVExporter.java
│ └── JsonExporter.java
|–– sample.log
├── ressources/
├── libs/
└── README.md
</code> </pre>
---

## 📄 Log File Structure (sample.log)

  Each line in the sample.log file follows the structure:

```bash
[date] [IP address] [status]
```
  Example:

```bash
2025-05-12 02:13:45 192.168.1.15 failed  
2025-05-12 02:15:12 192.168.1.25 success
```
⚠️ Important: Only public IP addresses should be included in the log file.

 Private IPs (like 192.168.x.x, 10.x.x.x, or 127.0.0.1) cannot be geolocated using the GeoIP database because they are not routable on the public internet. 

---

## 🛠️ Setup

### 1. Dependencies

Download these JARs and put them in the `libs/` folder:

- [Gson](https://github.com/google/gson) (`gson-2.8.x.jar`)
- [JavaMail](https://eclipse-ee4j.github.io/mail/) (`javax.mail.jar`)
- [GeoIP2](https://github.com/maxmind/GeoIP2-java) (`geoip2.jar` and dependencies)

### 2. GeoIP Database

- [Sign up at MaxMind](https://www.maxmind.com/en/geolite2/signup)
- Download **GeoLite2-City.mmdb**
- Place it in: `ressources/GeoLite2-City.mmdb`

---

## 🧪 Run the IDS

### 1. Compile

```bash
javac -cp "libs/*" -d out src/services/*.java src/Main.java
```
### 2. Execute
```bash
java -cp "bin:libs/*" Main sample.log
```

---

## ✉️ Email Setup
-Edit EmailAlertService.java:
```bash
final String fromEmail = "your@email.com";       // Gmail address
final String password = "your_app_password";     // App Password (not your Gmail password)
```
-Edit LogAnalyzer.java:
```bash
EmailAlertService.send("receiveremail@gmail.com", "Brute-force Alert", alert);
```
🔐 Get the password from Google App Passwords (you must enable 2-Step Verification first)

---

## 🌍 Visualize Attacks

1- Open web/map.html in a browser

2- It loads outputs/attacks.json and displays IP origins with markers

---

## 👥 Team Members

- **Salma Bahdad**   
- **Omar Khallouki**
  
---
  

## 📜 License

  This project is intended solely for educational and demonstrative use.  
  
---
