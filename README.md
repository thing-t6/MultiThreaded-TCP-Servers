# PortPulse

A lightweight Java TCP socket client-server application featuring multithreading, timeout handling, and real-time message streaming.

## Features
- **Multithreaded Server:** Handles client connections in background execution threads.
- **Timeout Management:** Configured socket and connection timeouts to drop inactive sessions gracefully.
- **Structured Communication:** Utilizes Java `DataInputStream` and `DataOutputStream` for reliable data transmission over TCP.

## Project Structure
```text
PortPulse/
├── src/
│   ├── Server.java
│   ├── ServerRun.java
│   └── Client.java
└── README.md
