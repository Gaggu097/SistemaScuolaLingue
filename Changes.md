# Changes Log

## 2026-09-19
- Created CLAUDE.md with development commands and high‑level architecture overview.
- Created Changes.md to track changes made during this Claude session.

## 2026-09-20
- Implemented missing functions in BoundaryGestore.java:
  - eliminareDocente(), aggiornareDocente(), eliminareCorso()
  - gestireCatalogoCorsi(), gestireImpiegato(), organizzareClassi()
- Implemented missing functions in ImpiegatoSegreteria.java:
  - annullareIscrizione(), registrareRimborso(), annullareLezione()
  - consultareStatisticheCorsi(), fixed consultareIscritti() comment
- Added missing controller methods to support above:
  - eliminaDocente, aggiornaDocente, eliminaCorso
  - organizzareClasse, visualizzareClassi, consultareStatisticheCorsi
  - consultareStatisticheIscritti, annullareLezione
- Added missing method declarations to IGestioneScuolaService interface
- Added missing EntityCorso import to BoundaryGestore.java
- Fixed UI glitch in mostraMenuGestCorsi by adding missing "0. Torna al menu principale" option

## 2026-09-21
- Improved domain model by moving business logic into EntityCorso entity:
  * Enhanced verificaDisponbilitaPosto() method to prevent negative return values and add proper validation
  * Added isCorsoPieno() method to check if course is full
  * Added isStatoValido() method to validate course state consistency
- Removed unused javax.print.Doc import from BoundaryGestore.java

## Not Implemented Functions Analysis

### High Priority
- **EntityCorso.verificaDisponbilitaPosto** (`src/main/java/org/gagandeepsuman/entity/EntityCorso.java:126-130`) – TODO: Implement Method (currently returns a simple subtraction; may need proper validation).
- **BoundaryGestore.organizzareClassi** (`src/main/java/org/gagandeepsuman/boundary/BoundaryGestore.java:221-224`) – throws `UnsupportedOperationException()`; TODO comment.
- **ImpiegatoSegreteria.annullareIscrizione** (`src/main/java/org/gagandeepsuman/boundary/ImpiegatoSegreteria.java:68-71`) – throws `UnsupportedOperationException()`; TODO comment.
- **ImpiegatoSegreteria.registrareRimborso** (`src/main/java/org/gagandeepsuman/boundary/ImpiegatoSegreteria.java:73-76`) – throws `UnsupportedOperationException()`; TODO comment.
- **ImpiegatoSegreteria.annullareLezione** (`src/main/java/org/gagandeepsuman/boundary/ImpiegatoSegreteria.java:78-81`) – throws `UnsupportedOperationException()`; TODO comment.
- **ImpiegatoSegreteria.consultareStatisticheCorsi** (`src/main/java/org/gagandeepsuman/boundary/ImpiegatoSegreteria.java:88-91`) – throws `UnsupportedOperationException()`; TODO comment.
- **GestioneScuolaController.registrareRimborso** (`src/main/java/org/gagandeepsuman/controller/GestioneScuolaController.java:416-418`) – throws `UnsupportedOperationException()`; TODO comment.
- **GestioneScuolaController.consultareStatisticheIscritti** (`src/main/java/org/gagandeepsuman/controller/GestioneScuolaController.java:463-465`) – throws `UnsupportedOperationException()`; TODO comment.

### Medium Priority
- **BoundaryGestore.gestireCatalogoCorsi** (`src/main/java/org/gagandeepsuman/boundary/BoundaryGestore.java:210-214`) – TODO comment; calls `mostraMenuGestCorsi()` but marked for implementation.
- **BoundaryGestore.gestireImpiegato** (`src/main/java/org/gagandeepsuman/boundary/BoundaryGestore.java:216-219`) – TODO comment; calls `mostraMenuGestioneImpiegati()`.
- **ImpiegatoSegreteria.consultareIscritti** (`src/main/java/org/gagandeepsuman/boundary/ImpiegatoSegreteria.java:83-86`) – TODO comment; delegates to controller method (which also has TODO).
- **GestioneScuolaController.visulizzareCatalogo** (`src/main/java/org/gagandeepsuman/controller/GestioneScuolaController.java:59-65`) – TODO comment; method name misspelled.
- **GestioneScuolaController.iscriversiAlCorso** (two occurrences) – TODO comments; actual implementation present but may need refinement.
- **DBManager.closeConnection** (`src/main/java/org/gagandeepsuman/dao/DBManager.java:25-37`) – TODO comment; method returns false; should properly close connection.

### Low Priority
- **EntityCorso** (additional methods) – none significant.
- **Other TODO comments** in entity getters/setters (likely from generated code) – low.