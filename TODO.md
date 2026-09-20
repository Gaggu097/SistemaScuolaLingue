# TODO List

## High Priority
- [DONE] Apply Dependency Injection (simple factory) to decouple DAO instantiation in controllers and boundaries.
- [DONE] Define DAO interfaces (e.g., IClienteDAO, ICorsoDAO) and have DAOs implement them. (via IDAO interface)
- [DONE] Create a factory (DAOFactory) to provide DAO instances.
- [DONE] Apply DI to GestioneScuolaController instantiation in boundaries.
- [DONE] Define service interface for GestioneScuolaController (IGestioneScuolaService) and have controller implement it.
- [DONE] Create a service factory for controller instances. (DAOFactory provides GestioneScuolaController)

## Medium Priority
- [DONE] Refactor validation logic into a validation service or use annotations (Bean Validation).
- [DONE] Externalize email SMTP configuration (SMTP_USER, SMTP_PASS) to configuration file or environment variables.
- [ ] Replace direct Scanner usage with an abstract UI layer to allow different UIs (GUI, web).
- [DONE] Implement missing functions marked with TODO or UnsupportedOperationException (from Changes.md analysis).
- [DONE] Improve domain model: move business logic into entities (e.g., verificaDisponbilitaPosto, validation).

## Low Priority
- [ ] Add logging framework (e.g., SLF4J) instead of System.out/System.err.
- [ ] Add unit tests for service layer and edge cases.
- [ ] Consider using Spring or CDI for DI instead of manual factory.
- [ ] Refactor static SessionFactory initialization to be more configurable (e.g., via XML or properties).
- [ ] Add DTOs to transfer data between layers to avoid exposing entities.