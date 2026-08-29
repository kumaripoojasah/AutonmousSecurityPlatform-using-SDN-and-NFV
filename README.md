# 🛡️ Real-Time Network Threat Detection & Mitigation Engine

An automated, event-driven network security and mitigation platform built with **Spring Boot** and **Apache Kafka**. The system ingests live telemetry data from OpenDaylight (ODL) SDN controllers, transforms raw network flows into machine-learning-ready feature vectors, evaluates threats via an AI/ML Decision Engine, and dynamically triggers threshold-based mitigation policies (such as `BLOCK_IP` and `RATE_LIMIT`).

---

## 🏗️ System Architecture

```
+------------------+      +------------------+      +------------------+
|   OpenDaylight   | ---> |  Traffic Event   | ---> |  Apache Kafka    |
|  (SDN Controller)|      |     Producer     |      | (traffic-events) |
+------------------+      +------------------+      +------------------+
                                                             |
                                                             v
+------------------+      +------------------+      +------------------+
|  Policy Engine   | <--- |   Feature Vector | <--- |  Traffic Event   |
|   & Mitigation   |      |   Transformer    |      |     Consumer     |
+------------------+      +------------------+      +------------------+
         |                          |
         v                          v
+------------------+      +------------------+
| SDN Flow Control |      | ML Decision Engine|
|  (REST / ODL)    |      | (Python Server)  |
+------------------+      +------------------+
```

---

## 🚀 Key Features

- **Real-time Telemetry Ingestion** — Stream network flow metrics (`packetRate`, `byteCount`, `tcpFlagSynCount`, etc.) seamlessly via Kafka.
- **Feature Engineering & Transformation** — Automatically extracts raw metrics into normalized vectors (`ModelInputFeatures`) including derived metrics like SYN/ACK Ratio and volume flags.
- **ML Decision Engine Integration** — Asynchronously communicates with a Python Flask/FastAPI REST service for real-time attack classification (`SYN_FLOOD`, `UDP_FLOOD`, `NORMAL`).
- **Rule-Based Policy Engine** — Evaluates ML classifications alongside hard rule thresholds to determine mitigation strategies:
  - `BLOCK_IP` — Pushes OpenFlow drop rules to isolate malicious targets.
  - `RATE_LIMIT` — Throttles bandwidth on overloaded ports.
  - `ALERT_AND_MONITOR` — Logs suspicious activity without disrupting legitimate traffic.
- **REST API & Live Dashboard Integration** — Serves historical mitigation data and live SOC telemetry endpoints (`/api/traffic/live`) for frontend monitoring interfaces.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend Framework | Java 17, Spring Boot 3.x |
| Event Streaming | Apache Kafka |
| Database | PostgreSQL / MySQL (Spring Data JPA, Hibernate) |
| ML Decision Engine | Python (Flask / FastAPI) |
| SDN Controller | OpenDaylight (ODL) RESTCONF API |
| Build Tool & Utilities | Maven, Project Lombok, SLF4J Loggers |

---

## 📁 Repository Structure

```
src/main/java/com/example/demo/
├── controller/
│   ├── TrafficController.java          # Live telemetry & device traffic APIs
│   └── MitigationController.java       # Mitigation action management APIs
├── dto/
│   ├── traffic/                        # ModelInputFeatures, TrafficResponse
│   └── mitigation/                     # MitigationActionResponse
├── entity/
│   └── MitigationAction.java           # JPA Database Entity for audit logs
├── kafka/
│   ├── consumer/
│   │   └── TrafficEventConsumer.java   # Core Kafka listener & pipeline trigger
│   └── event/
│       └── TrafficEvent.java           # Kafka payload DTO
├── repository/
│   └── MitigationActionRepository.java # JPA Repository for historical logs
└── service/
    ├── DecisionModelService.java              # HTTP Client to ML model server (port 5000)
    ├── TrafficFeatureTransformerService.java   # Raw flow to ML vector transformer
    ├── PolicyEngineService.java                # Threshold & rule evaluation engine
    └── MitigationService.java                  # SDN enforcement & DB logging service
```

---

## ⚙️ Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Kafka running on `localhost:9092`
- Python 3.9+ (for the decision engine ML service running on `localhost:5000`)
- Maven 3.8+

---

## 🚦 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/network-threat-mitigation.cmd
cd network-threat-mitigation
```

### 2. Configure `application.yml` / `application.properties`

Ensure Kafka and Database configurations match your environment:

```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=decision-engine
spring.datasource.url=jdbc:postgresql://localhost:5432/threat_db
spring.datasource.username=postgres
spring.datasource.password=yourpassword
```

### 3. Build & Run the Application

```bash
mvn clean install
mvn spring-boot:run
```

---

## 📊 Live Pipeline Output Sample

When a traffic event is ingested from OpenDaylight through Kafka, the application produces real-time terminal logs:

```
INFO  c.e.d.k.c.TrafficEventConsumer : Traffic event received: device=device-1, packetRate=120.5, uniquePorts=48
INFO  c.e.d.k.c.TrafficEventConsumer : ------------------------------------------------------------------
INFO  c.e.d.k.c.TrafficEventConsumer : >>> INGESTED RAW EVENT [Device: device-1 | Src: 10.0.0.5 -> Dst: 10.0.0.5]
INFO  c.e.d.k.c.TrafficEventConsumer : >>> RAW METRICS        : Packet Count = 500, Byte Count = 64000, Timestamp = 2026-08-28T05:52:36
INFO  c.e.d.k.c.TrafficEventConsumer : >>> TRANSFORMED ML VECTOR: [500.0, 64000.0, 120.5, 0.0, 0.0, 48.0, 0.0, 0.0, 48.0]
INFO  c.e.d.k.c.TrafficEventConsumer : >>> DERIVED METRICS      : SYN/ACK Ratio = 48.0, HighVolume = true
INFO  c.e.d.k.c.TrafficEventConsumer : >>> DECISION ENGINE RESULT: SYN_FLOOD
WARN  c.e.d.s.PolicyEngineService     : Threat detected by ML model: SYN_FLOOD for device device-1
INFO  c.e.d.k.c.TrafficEventConsumer : >>> POLICY ENGINE ACTION  : BLOCK_IP
WARN  c.e.d.s.MitigationService       : >>> ENFORCING SDN RULE: Blocking IP 10.0.0.5 on Device device-1
INFO  c.e.d.k.c.TrafficEventConsumer : ------------------------------------------------------------------
```

---

## 📡 Key API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/traffic/live` | Polling endpoint for real-time telemetry dashboard |
| GET | `/api/traffic/device/{deviceId}` | Query traffic logs for a specific device in a time window |
| GET | `/api/mitigation/recent` | Fetch top 10 recently executed SDN mitigation actions |
| GET | `/api/mitigation/{id}` | Fetch detailed mitigation report by action ID |
