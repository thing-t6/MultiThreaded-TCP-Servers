# tcp-proxy-server

A lightweight, multi-threaded TCP proxy server written in Java. It listens on a specified local port, accepts client connections, and forwards traffic bi-directionally to a target origin host and port.

## Features

- **Multi-threaded Architecture:** Handles client-to-target and target-to-client traffic streams concurrently using dedicated threads.
- **CLI Argument Parsing:** Easily configure the listener port and target origin via command-line flags (`--port` and `--origin`).
- **Timeout Protection:** Automatically closes the server socket if no client connects within a 30-second window.
- **Structured Communication:** Utilizes Java DataInputStream/DataOutputStream and BufferedInputStream/BufferedOutputStream for reliable data transmission over TCP.

## Code Structure
```text
.
├── TcpProxy.java       # Main entry point, CLI parser, and ServerSocket listener, Nested Proxy Runnable worker handling bi-directional socket piping                     
├── Server.java         # Backend target server
└── Client.java         # Client application communicating through the proxy
```

## Prerequisites

- **Java Development Kit (JDK)** 8 or higher.

## Getting Started

### 1. Compile the Code

Save the code as `TcpProxy.java` and compile it using the Java compiler:

```bash
javac TcpProxy.java
java CachingProxy --port <number> --origin <url>