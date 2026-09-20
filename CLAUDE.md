# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 🛠️ Development Commands

This is a Maven-based Java project using Hibernate/JPA for ORM and PostgreSQL as the database.

### Build
```bash
mvn clean compile
```

### Run Tests
```bash
mvn test
```
To run a single test class:
```bash
mvn -Dtest=TestClienteDAO test
```

### Package
```bash
mvn package
```
Produces a JAR in `target/` (though the project is designed to be run from the IDE or via `java` command).

### Run the Application
The application has a text‑based menu entry point in `org.gagandeepsuman.Main`. After building, you can run it with:

```bash
# Copy dependencies (if not already done by package)
mvn dependency:copy-dependencies
java -cp "target/classes:target/dependency/*" org.gagandeepsuman.Main
```

Alternatively, run directly from an IDE (IntelliJ IDEA, Eclipse) by executing the `Main` class.

### Database Setup
The project expects a PostgreSQL database named `gagandeepsuman` (as per `hibernate.cfg.xml`). Ensure PostgreSQL is running and the database/user/password match the configuration:
- URL: `jdbc:postgresql://localhost:5432/gagandeepsuman`
- Username: `gagandeepsuman`
- Password: `Gaggu123`

The `hibernate.hbm2ddl.auto` property is set to `validate`, so the schema must already exist. You can create tables by setting it to `update` or `create-drop` temporarily, or by running the SQL scripts (if provided) manually.

### Configuration
- `sistema_config.properties` (created at runtime) toggles whether inscriptions are open (`iscrizioni_aperte=true`). The controller methods `aprireIscrizioni()` and `chiusuraIscrizioni()` modify this file.
- Email service (`EmailService.java`) uses Gmail SMTP; you must insert a valid App Password and sender email for real email sending.

## 🏗️ High‑Level Architecture

The code follows a layered architecture reminiscent of MVC, adapted for a console‑based Java application:

1. **Entry Point** (`Main.java`)  
   - Presents a main menu to select a user role: Cliente, Gestore, or Segreteria.  
   - Instantiates the corresponding *Boundary* class and delegates menu handling.

2. **Boundary Layer** (`src/main/java/org/gagandeepsuman/boundary/*`)  
   - Console UI / input collection classes (`BoundaryCliente`, `BoundaryGestore`, `BoundaryTempo`, `ImpiegatoSegreteria`).  
   - Each boundary gathers user input via `Scanner` and forwards requests to the `GestioneScuolaController`.  
   - They also contain simple validation helpers (e.g., `isOnlyLetters`, date parsing).

3. **Controller** (`GestioneScuolaController.java`)  
   - Central business logic orchestrator.  
   - Uses DAO objects to perform CRUD operations and enforce workflows (e.g., course inscription, payment, registration).  
   - Contains utility methods for username generation, temporary password creation, and email/scheduling logic (promemoria).  
   - Directly calls DAO methods; does not contain UI code.

4. **Data Access Layer (DAO)** (`src/main/java/org/gagandeepsuman/dao/*`)  
   - Each entity has a corresponding DAO extending `GenericDAO<T, ID>`.  
   - `GenericDAO` provides Hibernate‑based `save`, `findById`, `update`, `delete`, `findAll` methods.  
   - DAOs are instantiated per‑use (e.g., `new ClienteDAO()`) and share a static `SessionFactory` built from `hibernate.cfg.xml`.  
   - Some DAOs may add custom query methods (e.g., `trovaPerLinguaELivello` in `CorsoDAO`).

5. **Entity Layer** (`src/main/java/org/gagandeepsuman/entity/*`)  
   - JPA/Hibernate annotated classes representing tables (`EntityCliente`, `EntityCorso`, `EntityIscrizione`, etc.).  
   - Fields map to columns; relationships (e.g., `FKidDocente`) are represented as integer foreign keys.  
   - Entities include helper methods (e.g., `verificaDisponbilitaPosto` in `EntityCorso`).

6. **Utilities** (`src/main/java/org/gagandeepsuman/util/*`)  
   - `PasswordSecurity`: PBKDF2‑based password hashing and verification.  
   - `EmailService`: Wrapper around Jakarta Mail to send real SMTP emails (Gmail by default). Includes domain validation to restrict sending to known email providers.

### Interaction Flow (Example: Cliente inscrizione a corso)
1. `Main` → `BoundaryCliente.mostraMenu()` → user selects "Iscriviti a un Corso".  
2. `BoundaryCliente.iscriversiAlCorso()` collects input (lingua, livello, idCliente) and calls `controlCliente.iscriversiAlCorso(...)`.  
3. `GestioneScuolaController.iscriversiAlCorso`:  
   - Validates cliente existence via `ClienteDAO`.  
   - Finds matching course via `CorsoDAO`.  
   - Checks docente via `DocenteDAO`.  
   - Prompts user for confirmation (Y/N).  
   - If Y and inscriptions are open (`BoundaryTempo.isIscrizioneAperta()`), verifies seat availability.  
   - Creates `EntityIscrizione` and `EntityPagamento`.  
   - Calls `IscrizioneDAO.creaIscrizioneTransazionale` to persist both in a single transaction.  
4. DAOs use Hibernate sessions (opened per operation) to interact with PostgreSQL.  
5. Upon success, controller outputs success message; boundary shows it to the user.

### Extending the System
- To add a new entity: create a JPA class in `entity`, a DAO extending `GenericDAO`, and register it in `hibernate.cfg.xml` `<mapping>`.  
- To add a new controller method: place it in `GestioneScuolaController` and call it from the appropriate boundary.  
- To change UI: modify the relevant `Boundary*` class or add a new one (e.g., for a different role) and update `Main` menu.

### Notes
- The project uses Java 26 (as per `maven.compiler.source/target`). Ensure JDK 26 is installed.  
- Hibernate version 6.4.4.Final; Jakarta Mail 2.0.3.  
- Tests use JUnit Jupiter (`org.junit.jupiter`).  
- The `.gitignore` file excludes `target/`, IDE files, and `.DS_Store`.