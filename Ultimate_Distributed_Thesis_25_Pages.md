# THESIS: DISTRIBUTED ENTERPRISE ARCHITECTURE FOR UNIVERSITY MEAL MANAGEMENT

---

## TABLE OF CONTENTS
1.  **Chapter 1: Introduction** .............................................. 1
2.  **Chapter 2: Literature Review & Problem Analysis** ...................... 4
3.  **Chapter 3: Requirements Specification** ................................ 7
4.  **Chapter 4: System Design & Architecture** ............................. 10
5.  **Chapter 5: Implementation Methodology** ............................... 14
6.  **Chapter 6: Security and Policy Enforcement** .......................... 17
7.  **Chapter 7: Quality Assurance and Testing** ............................ 20
8.  **Chapter 8: Distributed Systems Theory Applied** ........................ 23
9.  **Chapter 9: Project Evaluation & Performance** ......................... 26
10. **Chapter 10: Conclusion & Future Scope** .............................. 29
11. **Appendix: Source Code & Setup Guide** ................................. 31

---

## CHAPTER 1: INTRODUCTION

### 1.1 Project Overview
The "Distributed Cafeteria Management System" is a mission-critical enterprise application designed to streamline student identity verification and meal entitlement at Aksum University. The system is built upon a decentralized architecture that separates administrative identity management from real-time operational execution.

### 1.2 Problem Definition
In a traditional university environment, cafeteria management is often manual or semi-automated with a centralized bottleneck. This results in:
*   **Dependency Risks:** If the central database is offline, the cafeteria cannot function.
*   **Performance Bottlenecks:** Real-time verification against a master database with 20,000+ records can be slow.
*   **Fraud Vulnerability:** Lack of automated "Anti-Double-Dipping" checks across different meal sessions.

### 1.3 Objectives
*   To implement a **Distributed Database Model** ensuring high availability.
*   To develop an **Intelligent Scanning Engine** (The Five-Point Gatekeeper).
*   To create a **Mobile Digital Identity Portal** for students.

---

## CHAPTER 2: LITERATURE REVIEW & PROBLEM ANALYSIS

### 2.1 The Evolution of Campus Management
Historically, campus systems moved from paper to centralized digital systems. However, the modern "Smart Campus" requires a move towards **Edge Computing**—where the logic is executed close to the user (the cafeteria) rather than in a distant central server.

### 2.2 Distributed vs. Centralized Systems
This chapter analyzes the **CAP Theorem** (Consistency, Availability, Partition Tolerance). For a cafeteria system, **Availability** and **Partition Tolerance** are prioritized over immediate Consistency, allowing for a robust "Offline Mode."

---

## CHAPTER 3: REQUIREMENTS SPECIFICATION

### 3.1 Functional Requirements (FR)
| ID | Requirement | Description |
| :--- | :--- | :--- |
| FR1 | Identity Sync | Automatically replicate new student records from Registrar to Cafeteria. |
| FR2 | Real-time Scan | Verify student QRs against local 5-point policy. |
| FR3 | Menu Scheduling | Allow admins to dynamically update meal windows. |
| FR4 | Emergency QR | Generate single-use tokens for lost IDs. |

### 3.2 Non-Functional Requirements (NFR)
*   **Availability:** System must be operational 99.9% of meal hours.
*   **Security:** Passwords must be hashed; APIs must require credentials.
*   **Latency:** Scan verification must happen in < 250ms.

---

## CHAPTER 4: SYSTEM DESIGN & ARCHITECTURE

### 4.1 Physical Architecture Diagram
```text
[ REGISTRAR MASTER ] <---(WAN/LAN)---> [ CAFETERIA EDGE SERVER ]
        |                                       |
    [MySQL MASTER]                       [MySQL OPERATIONAL CACHE]
        |                                       |
    (Identity MGMT)                      (Scanner & API Logic)
                                                |
                                        [ ANDROID CLIENTS ]
```

### 4.2 Component Descriptions
1.  **SyncService (The Bridge):** A multithreaded Java service that manages delta-replication.
2.  **Scanner Logic Engine:** The core algorithm that processes QR codes.
3.  **API Gateway:** Serves JSON data to the Mobile application.

---

## CHAPTER 5: IMPLEMENTATION METHODOLOGY

### 5.1 The "Three Pillars" of Logic
*   **Pillar 1: Scanner Logic:** Implementing a server-side gatekeeper that cross-references 5 variables in a single database transaction.
*   **Pillar 2: Mobile Integration:** Developing an Android client that consumes RESTful APIs.
*   **Pillar 3: Fail-Safe QRs:** Creating a manager-level override module for hardware failures.

### 5.2 Technology Stack
*   Java EE (Jakarta)
*   MySQL 8.0
*   Android (Native)
*   ZXing Library

---

## CHAPTER 6: SECURITY AND POLICY ENFORCEMENT

### 6.1 Threat Modeling
We analyzed potential threats such as **QR Replay Attacks** (where a student uses a photo of a friend's ID). The system mitigates this by logging timestamps and enforcing the "One-Meal-Per-Session" policy.

### 6.2 Data Privacy
Strict separation of concerns ensures that the Cafeteria system only has access to necessary student data (ID, Name, Dept), not sensitive registrar data like grades or family history.

---

## CHAPTER 7: QUALITY ASSURANCE AND TESTING

### 7.1 Unit Testing
Individual test cases were written for the `isWithinTime()` and `hasAlreadyEaten()` functions to ensure 100% logic accuracy.

### 7.2 Integration Testing
Verifying that a change in the `Registrar_DB` is correctly reflected in the `Cafeteria_DB` within the defined sync window.

---

## CHAPTER 8: DISTRIBUTED SYSTEMS THEORY APPLIED

### 8.1 Replication Strategy
We utilize **Lazy Master Replication**. The master handles writes, and the follower (Cafeteria) handles reads and local operational writes.

### 8.2 Fault Tolerance (The Retry Pattern)
The `SyncService` implements a **Wait-and-Retry** loop. If the database connection is lost, it doesn't crash; it waits 3 seconds and tries again, ensuring high reliability.

---

## CHAPTER 9: PROJECT EVALUATION & PERFORMANCE

### 9.1 Scalability Analysis
The system is horizontally scalable. If the university opens a second cafeteria, we simply deploy a new Cafeteria Node. Each node remains independent, preventing a bottleneck.

### 9.2 Economic Impact
By automating the verification process, the university can reduce staff costs and eliminate food waste caused by unauthorized meal claims.

---

## CHAPTER 10: CONCLUSION & FUTURE SCOPE
The Distributed Cafeteria Management System provides a blueprint for modern campus infrastructure. Future work will include **Biometric Face-ID integration** and **Blockchain-based meal vouchers**.

---

## APPENDIX: SOURCE CODE & SETUP

### 1. Database Initialization
Scripts are provided for `registrar_db` and `cafeteria`. Ensure `foreign_key_checks` are enabled during setup.

### 2. Environment Variables
*   `DB_URL`: JDBC connection string.
*   `BASE_URL`: For Mobile App connectivity.

### 3. Usage Guide
1.  Register Student in Registrar.
2.  Wait for Sync.
3.  Login via Mobile App.
4.  Scan QR at the Cafeteria Station.

