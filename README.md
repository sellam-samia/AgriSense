# 🌱 AgriSense – Smart Farming Management System

AgriSense is a desktop application developed in **Java** with **JavaFX**, designed to provide intelligent management of agricultural farms.

The system allows farmers to manage different agricultural zones, monitor crops and livestock, track production, manage sensors and measurements, and handle alerts through a centralized application.

This project was developed as part of the **P00 – Practical Work** at the École Nationale Supérieure d'Informatique (ESI-Alger), 2CP, Academic Year 2025–2026.

## 🎯 Project Objectives

The main objective is to design and implement an intelligent farm management system using Object-Oriented Programming (OOP).

The application manages three types of agricultural zones:

- 🌾 **Cultivation Zones:** Management of crops, planting dates, harvesting dates, growth stages, and soil requirements.
- 🐄 **Livestock Zones:** Management of animals, biometric measurements, health status, GPS tracking, and feeding programs.
- 🐟 **Aquaculture Zones:** Management of aquatic species, water quality, and dedicated sensors.

## ✨ Main Features

### 1. Farm and Zone Management
- Add, modify, and deactivate agricultural zones.
- Manage cultivation, livestock, and aquaculture zones.
- Activate and suspend zones.
- Manage the entities associated with each zone.
- Display zone information and status.

### 2. Crop Management
- Register and manage different crop types.
- Organize crops into cereals, vegetables, and fruits.
- Record planting and expected harvesting dates.
- Track crop growth stages.
- Define soil requirements, including pH and humidity.
- Monitor agricultural production.

### 3. Livestock Management
- Register animals with their species, age, weight, and health status.
- Manage ruminants and poultry.
- Monitor biometric measurements.
- Track animal activity and geographical location.
- Manage feeding programs.
- Record sanitary events and weight evolution.

### 4. Aquaculture Management
- Manage aquatic species.
- Monitor water-related parameters.
- Associate dedicated sensors with aquaculture zones.
- Manage production information.

### 5. Sensor Management
- Manage different sensor types:
  - Environmental sensors.
  - Soil sensors.
  - Water sensors.
  - Biometric sensors.
  - GPS sensors.
- Associate sensors with agricultural zones.
- Configure measurement thresholds.
- Manage sensor statuses: Active, Failed, and Suspended.
- Record and consult sensor measurements.

### 6. Alert Management
- Detect abnormal measurements based on configured thresholds.
- Generate alerts when acceptable limits are exceeded.
- Assign severity levels: Normal, Warning, and Critical.
- Consult alert history.
- Filter alerts by zone, sensor type, severity, and period.
- Acknowledge or delete alerts.

### 7. Monitoring and Reports
- Monitor agricultural zones.
- Consult historical sensor measurements.
- Visualize measurement evolution through graphs.
- Generate reports about crops and zones.
- Display the overall status of the farm.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Main programming language |
| JavaFX | Desktop graphical interface |
| OOP | Application design and architecture |
| CSS (if used) | Interface styling |

**Database:** Not implemented.

## 🏗️ Object-Oriented Design

The project uses Object-Oriented Programming concepts to organize the different components of the farming system.

Some of the main classes include:

**Zone Management**
- `Zone`
- `ZoneCulture`
- `ZoneElevage`
- `ZoneAquacole`

**Crop Management**
- `Culture`
- `FamilleCulture`
- `TypeProduction`
- `Production`

**Livestock Management**
- `Animal`
- `TypeElevage`
- `EtatDeSante`
- `EvenementSanitaire`

**Sensor Management**
- `Capteur`
- `CapteurSol`
- `CapteurEau`
- `CapteurGPS`
- `CapteurBiometrique`
- `CapteurEnvironnemental`

**Monitoring and Alerts**
- `Releve`
- `ReleveNumerique`
- `ReleveGPS`
- `Alerte`
- `StatutAlerte`
- `NiveauGravite`

**Application Management**
- `Gestionnaire`
- `SmartFarmingApp`

## 📂 Project Structure

The source files are organized around the application's main entities and functionalities.

```text
AgriSense/
│
├── src/
│   ├── SmartFarmingApp.java
│   ├── Gestionnaire.java
│   │
│   ├── Zone.java
│   ├── ZoneCulture.java
│   ├── ZoneElevage.java
│   ├── ZoneAquacole.java
│   │
│   ├── Culture.java
│   ├── FamilleCulture.java
│   ├── Production.java
│   ├── Animal.java
│   │
│   ├── Capteur.java
│   ├── CapteurSol.java
│   ├── CapteurEau.java
│   ├── CapteurGPS.java
│   ├── CapteurBiometrique.java
│   ├── CapteurEnvironnemental.java
│   │
│   ├── Releve.java
│   ├── ReleveGPS.java
│   ├── ReleveNumerique.java
│   ├── Alerte.java
│   │
│   └── [Other Java classes]
│
├── run.bat
└── README.md
```



## 🚀 Getting Started

### Prerequisites

- Java Development Kit (JDK).
- JavaFX SDK.
- An IDE such as IntelliJ IDEA, Eclipse, or VS Code.

### Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/YOUR-USERNAME/AgriSense.git
   ```

2. Navigate to the project directory:

   ```bash
   cd AgriSense
   ```

3. Configure JavaFX and the required libraries.

4. Run the application using your IDE or the provided `run.bat` script if it is configured correctly.

## 💾 Data Management

The current implementation does not use a database management system. Data management is handled within the Java application.

## 🎓 Academic Context

- **Institution:** École Nationale Supérieure d'Informatique (ESI-Alger)
- **Level:** 2CP
- **Academic Year:** 2025–2026
- **Module:** P00 – Practical Work

---

<p align="center">
  🌱 <b>AgriSense – Making Farming Smarter.</b>
</p>
