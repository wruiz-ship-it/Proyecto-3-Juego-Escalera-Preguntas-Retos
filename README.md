# Proyecto-3-Juego-Escalera-Preguntas-Retos
# 🎲 Escaleras, Serpientes y Retos

Juego de mesa multijugador desarrollado en **Java 17 con Swing**, implementado como proyecto universitario de Estructuras de Datos. Combina la mecánica clásica de Escaleras y Serpientes con preguntas de cultura general y programación.

> **Restricción académica cumplida:** No se utiliza ninguna colección de `java.util` (ArrayList, LinkedList, Stack, Queue, HashMap, Hashtable, TreeMap, etc.). Todas las estructuras de datos fueron implementadas desde cero.

---

## 👥 Integrantes

| Nombre |
|---|
| George Estiven Campo |
| Wilson Arley Ruiz |
| Juan José Rosero González |
| Cristian Iván Dorado |
| Julián Santiago  Gómez |

---

## 📋 Tabla de contenidos

1. [Requisitos del sistema](#requisitos-del-sistema)
2. [Instalación y ejecución](#instalación-y-ejecución)
3. [Manual de uso](#manual-de-uso)
4. [Reglas del juego](#reglas-del-juego)
5. [Estructuras de datos implementadas](#estructuras-de-datos-implementadas)
6. [Arquitectura del proyecto](#arquitectura-del-proyecto)
7. [Banco de preguntas](#banco-de-preguntas)
8. [Tablero — escaleras y serpientes](#tablero--escaleras-y-serpientes)

---

## 💻 Requisitos del sistema

| Herramienta | Versión mínima |
|---|---|
| Java JDK | 17 o superior |
| Maven | 3.8 o superior |
| Sistema operativo | Windows 10 / macOS 12 / Ubuntu 20.04 |

### Verificar que tienes Java y Maven instalados

```bash
java -version
mvn -version
```

Si no tienes Java, descárgalo desde: https://adoptium.net  
Si no tienes Maven, descárgalo desde: https://maven.apache.org/download.cgi

---

## 🚀 Instalación y ejecución

### Opción A — Maven (recomendada)

```bash
# 1. Descomprimir el proyecto y entrar a la carpeta
cd escaleras-serpientes

# 2. Compilar y empaquetar
mvn package -q

# 3. Ejecutar
java -jar target/escaleras-serpientes.jar
```

### Opción B — Script de compilación manual

```bash
# En Linux / macOS
chmod +x compile.sh
./compile.sh

# En Windows (PowerShell)
.\compile.sh
```

### Opción C — VS Code

1. Abrir la carpeta `escaleras-serpientes` en VS Code
2. Instalar la extensión **Extension Pack for Java**
3. Abrir `src/main/java/com/juego/ui/MainApp.java`
4. Hacer clic en el botón ▶ **Run** que aparece sobre el método `main`

> El archivo `.vscode/launch.json` ya está configurado para ejecutar `MainApp` directamente.

---

## 🕹️ Manual de uso

### Pantalla 1 — Menú Principal

Al iniciar la aplicación aparece el menú principal con tres opciones:

| Botón | Acción |
|---|---|
| **▶ Nuevo Juego** | Inicia el flujo de registro de jugadores |
| **🏆 Ranking** | Muestra el ranking de la última partida |
| **✖ Salir** | Cierra la aplicación |

---

### Pantalla 2 — Registro de Jugadores

1. Seleccionar el **número de jugadores** (2, 3 o 4) con el control numérico
2. Escribir el **nombre de cada jugador** en los campos de texto — cada jugador tiene su color asignado automáticamente
3. Hacer clic en **▶ Iniciar Juego**

> Los campos tienen texto predeterminado ("Jugador 1", "Jugador 2"...). Si no se editan, se usan esos nombres.

---

### Pantalla 3 — Tablero de Juego

La ventana principal tiene tres secciones:

**Centro — Tablero (50 casillas en serpentina)**

| Color de casilla | Significado |
|---|---|
| 🟦 Azul/morado oscuro | Casilla normal |
| 🟩 Verde | Casilla con escalera (sube) |
| 🟥 Rojo oscuro | Casilla con serpiente (baja) |
| 🟪 Morado | Casilla de reto (pregunta) |
| ⭐ Dorado (casilla 50) | Meta — casilla ganadora |

Las flechas verdes indican escaleras (hacia arriba) y las flechas rojas discontinuas indican serpientes (hacia abajo).

**Derecha — Panel de información**
- Lista de jugadores con posición actual, aciertos y errores
- El jugador con el turno activo aparece destacado con borde grueso
- Historial de los últimos movimientos (pila — más reciente arriba)

**Abajo — Panel de turno**
- Nombre del jugador activo
- Dado con animación
- Botón **🎲 Lanzar Dado**

---

### Flujo de un turno

```
1. Presionar "Lanzar Dado"
      ↓
2. El dado anima y muestra el resultado (1-6)
      ↓
3. La ficha se desplaza con animación hasta la nueva casilla
      ↓
4a. Casilla NORMAL      → turno pasa al siguiente jugador
4b. Casilla ESCALERA    → ficha sube automáticamente + mensaje
4c. Casilla SERPIENTE   → ficha baja automáticamente + mensaje
4d. Casilla de RETO     → aparece ventana con pregunta
4e. Casilla 50 (META)   → ¡Ganador! → pantalla de ranking
```

---

### Pantalla 4 — Reto (Pregunta)

Cuando un jugador cae en una casilla morada aparece una ventana de pregunta:

- Se muestra el **enunciado** y **4 opciones** (A, B, C, D)
- El jugador tiene tiempo ilimitado para responder
- Hacer clic en la opción deseada

| Resultado | Consecuencia |
|---|---|
| ✅ Respuesta correcta | +10 puntos + **turno extra** (vuelve a lanzar) |
| ❌ Respuesta incorrecta | -5 puntos + **pierde el siguiente turno** |

> La dificultad de la pregunta depende de la casilla: casillas 1-15 → Fácil, 16-35 → Media, 36-50 → Difícil.

---

### Pantalla 5 — Ranking Final

Cuando un jugador llega a la casilla 50 el juego termina y se muestra el ranking:

- Posición final de todos los jugadores
- Puntaje: `(aciertos × 10) - (errores × 5)`
- Botón **Nueva Partida** para volver al menú

---

## 📏 Reglas del juego

1. El orden de turnos es el orden en que se registraron los jugadores
2. Para ganar hay que caer **exactamente** en la casilla 50 — si el dado da más, el jugador no avanza ese turno
3. Las escaleras suben y las serpientes bajan sin importar si hay pregunta en el destino
4. Al responder correctamente un reto se obtiene **un turno extra** inmediato
5. Al responder incorrectamente se **pierde el siguiente turno** (no el actual)
6. Si un jugador pierde el turno y le toca, simplemente se le salta y pasa al siguiente

---

## 🏗️ Estructuras de datos implementadas

Todas ubicadas en `src/main/java/com/juego/model/estructuras/`

### 1. `ListaEnlazada.java` — Tablero
```
Nodo[Casilla 1] → Nodo[Casilla 2] → ... → Nodo[Casilla 50] → null
```
- **Uso:** almacena las 50 casillas del tablero en orden
- **Operaciones:** `agregar(casilla)`, `obtener(numero)`, `toArray()`
- **Complejidad de búsqueda:** O(n) — recorrido lineal por número de casilla

---

### 2. `ColaJugadores.java` — Turnos (FIFO)
```
frente → [Jugador A] → [Jugador B] → [Jugador C] → null ← fin
```
- **Uso:** maneja el orden de turnos rotando al jugador activo al final
- **Operaciones:** `encolar()`, `desencolar()`, `rotar()`, `verFrente()`
- **Complejidad:** O(1) para todas las operaciones

---

### 3. `PilaHistorial.java` — Historial (LIFO)
```
cima → [Movimiento N] → [Movimiento N-1] → ... → null
```
- **Uso:** almacena cada movimiento — el más reciente aparece primero
- **Operaciones:** `apilar(movimiento)`, `desapilar()`, `verCima()`
- **Complejidad:** O(1) para todas las operaciones

---

### 4. `TablaHash.java` — Jugadores
```
Índice 0: null
Índice 1: ["Ana" → Jugador] → null
Índice 2: ["Carlos" → Jugador] → ["Carla" → Jugador] → null  ← colisión resuelta
Índice 3: null
```
- **Uso:** búsqueda rápida de jugador por nombre
- **Función hash:** `h = (h * 31 + char) % capacidad` para cada carácter
- **Colisiones:** resueltas con encadenamiento separado (listas enlazadas por índice)
- **Complejidad promedio:** O(1) para insertar y buscar

---

### 5. `ArbolBST.java` — Preguntas
```
             [Pregunta id=30]
            /                \
    [Pregunta id=15]    [Pregunta id=45]
       /      \               /       \
  [id=7]   [id=20]       [id=37]   [id=55]
```
- **Uso:** almacena las preguntas ordenadas por ID; filtrado por dificultad con recorrido inorder
- **Operaciones:** `insertar()`, `buscar(id)`, `obtenerPorDificultad()`
- **Complejidad:** O(log n) promedio para inserción y búsqueda

---

### 6. `Grafo.java` — Escaleras y Serpientes
```
Vértice 3  ──ESCALERA──▶  Vértice 22
Vértice 17 ──SERPIENTE──▶ Vértice 6
...
```
- **Uso:** representa las conexiones dirigidas del tablero
- **Implementación:** lista de adyacencia con nodos enlazados propios
- **Operaciones:** `agregarArista()`, `obtenerArista(casilla)`, `todasLasAristas()`

---

## 📁 Arquitectura del proyecto

```
escaleras-serpientes/
├── pom.xml                          # Configuración Maven
├── compile.sh                       # Script de compilación alternativa
├── README.md                        # Este archivo
└── src/
    └── main/
        ├── java/
        │   └── com/juego/
        │       ├── model/
        │       │   ├── Jugador.java          # Entidad jugador (nombre, posición, puntaje)
        │       │   ├── Casilla.java          # Entidad casilla (número, tipo, destino)
        │       │   ├── Pregunta.java         # Entidad pregunta (enunciado, opciones, respuesta)
        │       │   ├── Movimiento.java       # DTO de cada movimiento para el historial
        │       │   └── estructuras/
        │       │       ├── ListaEnlazada.java  # Lista enlazada simple — tablero
        │       │       ├── ColaJugadores.java  # Cola FIFO — turnos
        │       │       ├── PilaHistorial.java  # Pila LIFO — historial
        │       │       ├── TablaHash.java      # Tabla hash con encadenamiento
        │       │       ├── ArbolBST.java       # Árbol binario de búsqueda — preguntas
        │       │       └── Grafo.java          # Grafo dirigido — escaleras/serpientes
        │       ├── service/
        │       │   └── TableroService.java   # Lógica central del juego
        │       ├── ui/
        │       │   ├── MainApp.java          # Punto de entrada
        │       │   ├── MenuPrincipalUI.java  # Pantalla menú
        │       │   ├── RegistroJugadoresUI.java # Pantalla registro de jugadores
        │       │   ├── JuegoUI.java          # Pantalla de juego principal
        │       │   ├── TableroPanel.java     # Canvas del tablero con animaciones
        │       │   ├── RetoUI.java           # Diálogo de preguntas
        │       │   ├── RankingUI.java        # Pantalla ranking final
        │       │   └── UITheme.java          # Constantes de diseño (colores, fuentes)
        │       └── util/
        │           └── PreguntaLoader.java   # Carga preguntas desde archivo .txt
        └── resources/
            └── preguntas.txt                # Banco de 59 preguntas (editable)
```

---

## ❓ Banco de preguntas

El archivo `src/main/resources/preguntas.txt` puede editarse sin necesidad de recompilar.

### Formato de cada línea

```
DIFICULTAD|enunciado|opción0|opción1|opción2|opción3|índice_correcto
```

- `DIFICULTAD`: `FÁCIL`, `MEDIA` o `DIFÍCIL`
- `índice_correcto`: número del 0 al 3 indicando cuál opción es correcta

### Ejemplo

```
FÁCIL|¿Cuánto es 5 + 3?|6|8|7|9|1
MEDIA|¿Cuál es la complejidad de buscar en array?|O(log n)|O(n²)|O(n)|O(1)|2
DIFÍCIL|¿Qué patrón de diseño usa una sola instancia?|Factory|Observer|Singleton|Decorator|2
```

Las líneas que comienzan con `#` son comentarios y se ignoran.

### Distribución actual (59 preguntas)

| Dificultad | Cantidad | Casillas que la activan |
|---|---|---|
| FÁCIL | 20 | 1 – 15 |
| MEDIA | 20 | 16 – 35 |
| DIFÍCIL | 19 | 36 – 50 |

---

## 🗺️ Tablero — escaleras y serpientes

### Escaleras (suben)

| Casilla origen | Casilla destino | Avance |
|---|---|---|
| 3 | 22 | +19 |
| 8 | 30 | +22 |
| 16 | 26 | +10 |
| 31 | 44 | +13 |
| 36 | 48 | +12 |

### Serpientes (bajan)

| Casilla origen | Casilla destino | Retroceso |
|---|---|---|
| 17 | 6 | -11 |
| 25 | 11 | -14 |
| 33 | 14 | -19 |
| 40 | 20 | -20 |
| 46 | 28 | -18 |

### Casillas de reto (16 en total)

`5, 7, 10, 12, 15, 18, 21, 23, 27, 29, 34, 37, 41, 43, 45, 49`

---

## 👨‍💻 Tecnologías y decisiones de diseño

| Aspecto | Decisión |
|---|---|
| Lenguaje | Java 17 |
| UI | Java Swing — sin dependencias externas |
| Build | Maven 3.8 |
| Estructuras de datos | 100% implementadas a mano — sin colecciones de `java.util` |
| Animaciones | `javax.swing.Timer` para el dado y desplazamiento de fichas |
| Patrón de diseño | Separación MVC: `model` + `service` (lógica) + `ui` (vista) |
| Comunicación servicio↔UI | DTO `ResultadoMovimiento` — evita acoplamiento directo |
| Preguntas | Archivo externo `.txt` — modificable sin recompilar |

---

*Proyecto académico — Estructuras de Datos — 2026*
