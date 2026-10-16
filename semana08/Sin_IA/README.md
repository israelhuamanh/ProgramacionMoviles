# CafeCampus ☕📱

**App de Pedidos para la Cafetería del Campus Tecsup**  
*Paquete principal:* `com.tecsup.cafecampus`

---

## 📌 Descripción del Proyecto

**CafeCampus** es una aplicación móvil desarrollada en Kotlin y Jetpack Compose basada en la arquitectura **MVVM + Repository Pattern**, con persistencia local mediante **Room Database** y **DataStore Preferences**.

La app maneja dos perfiles que comparten la misma base de datos local (`cafecampus.db`):
- **Estudiante (Cliente):** Consulta el catálogo, agrega productos al carrito con cantidad e indicaciones, confirma su pedido y realiza el seguimiento en tiempo real de su estado.
- **Administrador de Cafetería:** Monitorea las métricas en tiempo real (ventas del día y pedidos por estado), avanza el flujo de preparación de pedidos (`RECIBIDO` → `PREPARANDO` → `LISTO` → `ENTREGADO` o `CANCELADO`), gestiona los productos del menú (crear, editar, eliminar y marcar disponible/agotado) y configura preferencias del sistema.

---

## 🏗️ Arquitectura del Sistema (MVVM + Repository)

```text
       VIEW (Composables)
        │            ▲
 Eventos│            │ collectAsState()
 (clics)▼            │
    VIEWMODEL (StateFlow)
        │            ▲
   Llama│            │ Flow / Resultados
        ▼            │
   CafeRepository (Interface)
        │
   implementa
        ▼
 CafeRepositoryRoom ─────────► DAOs ─────────► SQLite (cafecampus.db)
 DataStore Preferences ──────► SesionViewModel / MainActivity
```

---

## 📁 Estructura de Paquetes y Archivos

```text
com.tecsup.cafecampus
├── MainActivity.kt
├── CafeApp.kt
│
├── data
│   ├── local
│   │   ├── dao
│   │   │   ├── PedidoDao.kt
│   │   │   ├── ProductoDao.kt
│   │   │   └── UsuarioDao.kt
│   │   ├── entity
│   │   │   ├── DetallePedido.kt
│   │   │   ├── Pedido.kt
│   │   │   ├── Producto.kt
│   │   │   └── Usuario.kt
│   │   ├── relation
│   │   │   └── PedidoConDetalle.kt
│   │   ├── CafeDatabase.kt
│   │   └── DatosIniciales.kt
│   ├── prefs
│   │   └── PreferenciasStore.kt
│   └── repository
│       ├── CafeRepository.kt
│       └── CafeRepositoryRoom.kt
│
├── model
│   ├── EstadoPedido.kt
│   ├── ItemCarrito.kt
│   └── Rol.kt
│
├── viewmodel
│   ├── CafeViewModelFactory.kt
│   ├── MenuAdminViewModel.kt
│   ├── MenuViewModel.kt
│   ├── PanelAdminViewModel.kt
│   ├── PedidosAdminViewModel.kt
│   ├── PedidosClienteViewModel.kt
│   └── SesionViewModel.kt
│
└── ui
    ├── admin
    │   ├── AjustesScreen.kt
    │   ├── DetallePedidoAdminScreen.kt
    │   ├── FormularioProductoScreen.kt
    │   ├── MenuAdminScreen.kt
    │   ├── PanelScreen.kt
    │   └── PedidosAdminScreen.kt
    ├── cliente
    │   ├── ConfirmarPedidoScreen.kt
    │   ├── DetalleProductoScreen.kt
    │   ├── LoginScreen.kt
    │   ├── MenuScreen.kt
    │   ├── MisPedidosScreen.kt
    │   ├── SeguimientoScreen.kt
    │   └── SplashScreen.kt
    ├── components
    │   ├── AppTopBar.kt
    │   ├── CafeButton.kt
    │   ├── CafeTextField.kt
    │   ├── EstadoChip.kt
    │   ├── PedidoCard.kt
    │   ├── ProductoCard.kt
    │   ├── ProductoIcono.kt
    │   └── StepperEstado.kt
    ├── navigation
    │   ├── NavCafe.kt
    │   └── Rutas.kt
    └── theme
        ├── Color.kt
        ├── Theme.kt
        └── Type.kt
```

---

## 📸 Capturas de Pantalla del Flujo

| 1. Login con Selector de Rol | 2. Menú de Productos | 3. Detalle y Carrito |
|:---:|:---:|:---:|
| *(Agregar captura Login)* | *(Agregar captura Menú)* | *(Agregar captura Detalle)* |

| 4. Confirmación y Seguimiento | 5. Panel Administrador | 6. Gestión de Menú y Ajustes |
|:---:|:---:|:---:|
| *(Agregar captura Seguimiento)* | *(Agregar captura Panel)* | *(Agregar captura Ajustes)* |

---

## ✅ Pruebas de Aceptación

| Prueba | Resultado Esperado | Estado |
| :--- | :--- | :---: |
| **Login con clave incorrecta** | Muestra mensaje de error y deniega el acceso | **OK** |
| **Estudiante: agregar 2 productos y confirmar** | Pedido creado con estado `RECIBIDO` y total correcto | **OK** |
| **Producto agotado** | Botón de agregar deshabilitado y chip `Agotado` | **OK** |
| **Admin: avanzar pedido a Preparando** | El seguimiento del estudiante cambia a `PREPARANDO` en tiempo real | **OK** |
| **Admin: cancelar pedido** | El estado cambia a `CANCELADO` de forma inmediata | **OK** |
| **Ajustes: cambiar a modo oscuro** | El tema cambia a oscuro en toda la app e incluye persistencia | **OK** |

---

## 💡 Preguntas Teóricas (Para Pensar)

### 1. ¿Por qué `CafeRepository` debe ser una `interface` en lugar de una clase concreta?
> **Respuesta:**  
> Aplicar el principio de **Inversión de Dependencias (DIP)** mediante una interfaz desacopla completamente los ViewModels y la interfaz de usuario de la implementación técnica subyacente. Esto permite cambiar en el futuro de un almacenamiento local en Room a una API REST en la nube o Firebase sin modificar una sola línea de código en las pantallas ni en los ViewModels. Además, facilita la creación de *Mocks* y pruebas unitarias rápidas.

### 2. ¿Por qué en `DetallePedido` guardamos `nombreProducto` y `precioUnitario` de forma redundante en lugar de solo guardar `productoId`?
> **Respuesta:**  
> Representa una **fotografía histórica e inmutable** de la transacción en el momento de la compra. Si posteriormente el administrador cambia el precio de un producto, lo renombras o lo elimina del menú, los pedidos históricos antiguos almacenados en la base de datos conservarán los valores originales con los que el estudiante compró, evitando inconsistencias contables o errores de claves foráneas.

### 3. ¿Por qué Room no permite realizar operaciones de lectura/escritura directamente en el hilo principal (Main Thread)?
> **Respuesta:**  
> Las operaciones de lectura y escritura en disco con SQLite son síncronas y bloqueantes. Si se ejecutaran en el hilo principal de la aplicación (UI Thread), congelarían el renderizado de la pantalla provocando lag o caídas de cuadros por segundo, e incluso errores de **ANR (Application Not Responding)**. Room fuerza el uso de corrutinas (`Dispatchers.IO`) o flujos asíncronos (`Flow`) para mantener la interfaz de usuario fluida a 60/120 fps.
