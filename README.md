# Bluetooth Sensor App

Esta aplicación Android permite la conexión, monitorización y gestión de sensores BLE (Bluetooth Low Energy), mostrando información de temperatura en tiempo real y proporcionando una experiencia de usuario robusta y moderna.

## Características principales

- **Escaneo de dispositivos BLE**: Busca y lista dispositivos BLE cercanos.
- **Conexión y desconexión**: Permite conectar/desconectar a un sensor BLE específico.
- **Lectura de temperatura**: Muestra la temperatura leída del sensor conectado.
- **Gestión de permisos**: Solicita y gestiona permisos Bluetooth y localización solo cuando es necesario.
- **Feedback visual**:
  - Snackbar para mostrar errores y acciones rápidas (reintentar, cerrar).
  - Spinner de carga durante la conexión.
  - Botones deshabilitados mientras se conecta.
- **Navegación moderna**: Navegación entre pantalla principal y pantalla de sensor de temperatura usando Jetpack Navigation Compose.
- **Compatibilidad con Hilt**: Inyección de dependencias para ViewModels y componentes.

## Estructura del proyecto

- `app/` - Código fuente principal de la app Android.
- `bluetoothcore/` - Módulo con lógica de bajo nivel para BLE y acceso a sensores.
- `src/main/java/com/itram/bluetooth/ui/` - Componentes de UI, navegación y pantallas.
- `src/main/java/com/itram/bluetooth/permissions/` - Lógica de permisos.

## Cómo ejecutar

1. Clona el repositorio.
2. Abre el proyecto en Android Studio.
3. Conecta un dispositivo físico (BLE necesario).
4. Compila y ejecuta la app.

## Notas técnicas
- Requiere permisos de Bluetooth y localización en tiempo de ejecución.
- El UUID del servicio y característica de temperatura debe coincidir con el de tu sensor BLE.
- El proyecto usa Jetpack Compose, Hilt, y la API moderna de Bluetooth de Android.

## Licencia
MIT

