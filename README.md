# 🌐 Custom HTTP Server & Load Balancer (Práctica 4)

> ⚠️ **Navigation Note:** This module is part of a larger repository.  
> The source code for this specific project is located at:  
> `src/main/java/org/example/practica4/`

## 📖 Overview

Este proyecto implementa un **Servidor HTTP 1.1** desde cero utilizando Java Sockets puros, sin depender de frameworks web de alto nivel. El sistema está diseñado para demostrar conceptos avanzados de redes como el manejo manual de cabeceras HTTP, persistencia en memoria y estrategias de escalabilidad.

El servidor cuenta con un sistema de **Balanceo de Carga** nativo: administra un pool de conexiones limitado y, ante la saturación, despliega automáticamente una instancia secundaria redirigiendo el tráfico mediante códigos de estado `307 Temporary Redirect`.

## 🚀 Key Features

* **Protocolo HTTP Manual:** Implementación del parsing de peticiones (GET, POST, PUT, DELETE) y construcción de respuestas con patrón **Builder**.
* **Load Balancing & Failover:**
    * Pool de conexiones configurable (`MAX_POOL_SIZE`).
    * Detección automática de sobrecarga.
    * Redirección de clientes a puerto secundario (`8081`).
* **Soporte Multi-MIME:** Capacidad para servir JSON (`application/json`), Texto (`text/plain`) e Imágenes (`image/png`, `image/jpeg`).
* **In-Memory Persistence:** Uso de Singletons Thread-Safe para simular bases de datos.

## 📂 Project Location & Structure

Dado que este repositorio contiene múltiples prácticas, asegúrate de navegar a la ruta correcta:

```text
java-sockets-notes/
└── src/
    └── main/
        └── java/
            └── org/
                └── example/
                    └── practica4/  <-- PROJECT ROOT
                        ├── assets/              # Imágenes (png, jpg)
                        ├── builder/             # Lógica de respuesta HTTP
                        │   ├── HttpResponseBuilder.java
                        │   ├── ResponseStatus.java
                        │   └── ResponseTypes.java
                        ├── JSONDataSource.java  # Singleton DB para JSON
                        ├── ServerHttp.java      # ENTRY POINT (Main)
                        ├── ServerManager.java   # Hilo de atención al cliente
                        └── StringDataSource.java
