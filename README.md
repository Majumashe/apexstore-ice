# ApexStore ICE

Sistema de pagos distribuido con ZeroC Ice 3.7 y Gradle. Tres procesos:

- **backend** (puerto 10000): recibe las compras (`checkout`) y los callbacks de confirmación (`receptor`).
- **pasarelas** (puerto 10001): simula las pasarelas de pago (STRIPE, PSE, CRIPTO) con demora y tasa de fallo.
- **cliente**: lanza 4 compras de prueba y hace polling del estado hasta que se resuelven.

## Requisitos

- Java 11 o superior (JDK) en el PATH.
- No hace falta instalar Gradle: el proyecto incluye el wrapper (`gradlew` / `gradlew.bat`).

## Ejecución

Abrir **tres terminales** en la raíz del proyecto y lanzar en este orden:

**Terminal 1 — backend**

```
.\gradlew :backend:run
```

Esperar a que imprima `ServidorBackend escuchando en 10000`.

**Terminal 2 — pasarelas**

```
.\gradlew :pasarelas:run
```

**Terminal 3 — cliente**

```
.\gradlew :cliente:run
```

En Linux, macOS o una terminal bash (Cygwin/Git Bash) usar `./gradlew` en lugar de `.\gradlew`.

## Qué esperar

- El backend y las pasarelas quedan corriendo; Gradle muestra el build detenido en `> :backend:run` (es normal, es el servidor vivo). Se detienen con `Ctrl+C`.
- El cliente ejecuta las compras ORD-001 a ORD-004 y termina solo. Cada compra arranca en `PENDIENTE` y pasa a `APROBADO` o `RECHAZADO` cuando la pasarela confirma por callback.
- ORD-004 usa el medio `NEQUI`, que no está registrado: termina en `ERROR` a propósito (fallo controlado).

## Configuración

Los puertos y proxies están en los `.cfg` de cada módulo (`backend/backend.cfg`, `pasarelas/pasarelas.cfg`, `cliente/cliente.cfg`). Todo corre en `localhost`; para ejecutar en máquinas distintas basta cambiar el `-h localhost` por el host correspondiente en esos archivos.
