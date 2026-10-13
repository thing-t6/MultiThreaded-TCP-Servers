# MultiThreaded-TCP-Servers

A lightweight multi-threaded network utilities featuring a concurrent TCP server capable of handling multiple simultaneous clients and a robust multi-threaded TCP proxy server for traffic forwarding.

## Features

- **Multi-threaded Architecture for Server and Proxy Server:** Handles multiple client-to-target and target-to-client traffic streams concurrently using dedicated worker threads.
- **Structured Communication:** Utilizes Java InputStreamReader/OutputStreamWriter and BufferedInputStream/PrintWriter for reliable data transmission over TCP.
- **Concurrent TCP Server:** Accepts and manages multiple simultaneous client sessions, handling data input/output safely.
- **Connection Management:** Graceful handling of client disconnections, connection timeouts, and stream cleanup.
- **Clean Logging:** Detailed console output tracking client connections, disconnections, and data transfers in real time.

## Code Structure
```text
.
├── TcpProxy.java       # Main entry point, CLI parser, and ServerSocket listener, Nested Proxy Runnable worker handling bi-directional socket piping                     
├── Server.java         # Backend target server utilizing multi-threaded architecture
└── Client.java         # Client application to communicate through the proxy or communicate with server
```

## Prerequisites

- **Java Development Kit (JDK)** 8 or higher.

## Getting Started

### 1. Compile the Code

Save the code as `TcpProxy.java` and compile it using the Java compiler:

```bash
Javac Server.java
javac TcpProxy.java
javac Client.java
java Server
java CachingProxy --port <number> --origin <url>
java Client