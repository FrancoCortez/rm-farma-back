# Change: crud-services

## Why
The `service` bounded context (`owl.tree.rmfarma.service`) currently exposes only `GET /api/v1/services`. Operators cannot create, update, or deactivate a service through the API, and there is no global error handler — so any future validation failure would surface as a raw 500. This change closes that gap by adding the missing CRUD endpoints (POST, PATCH, soft DELETE, GET by code) and introducing a reusable `GlobalExceptionHandler` in the `shared` module so that uniqueness violations, not-found misses, and bean-validation errors translate into proper 409/404/400 responses with a uniform shape.

## What Changes
- **[NEW]** `CreateServiceUseCase` + input DTO `CreateServiceRequest` (with `@NotBlank` + `@Size` bean validation, trimmed) + output `ServiceResourceDto` (reuse existing).
- **[NEW]** `UpdateServiceUseCase` + input DTO `UpdateServiceRequest` (with `@NotBlank` + `@Size` bean validation, trimmed) + output `ServiceResourceDto`.
- **[NEW]** `DeleteServiceUseCase` (soft) — loads by code, sets `enabled = false`, persists.
- **[NEW]** `GetServiceByCodeUseCase` — finds by code, throws `NotFoundException` on miss.
- **[NEW]** Repository methods on `ServicesRepository`: `existsByCode(String)`, `existsByDescription(String)`, `findByDescriptionAndEnabledTrue(String)`, `existsByDescriptionAndIdNot(String, String)`, `existsByCodeAndIdNot(String, String)`.
- **[MODIFY]** `ServiceServicePort` (in port): add `create(CreateServiceRequest)`, `update(String code, UpdateServiceRequest)`, `deleteByCode(String code)`, `findByCode(String code)`.
- **[MODIFY]** `ServicesPersistencePort` (out port): add `save(Services)`, `update(Services)`, `deleteByCode(String code)`, change `findByCode(String)` to return `Optional<Services>` instead of nullable entity.
- **[MODIFY]** `ServicesPersistencePortAdapter`: implement new port methods; change `findByCode` to return `Optional<Services>` (empty on miss — the use case layer is the one that translates to `NotFoundException`, keeping the persistence adapter pure).
- **[MODIFY]** `ServicesMapper` (MapStruct): add `toServices(CreateServiceRequest)` and `updateEntityFromRequest(UpdateServiceRequest, @MappingTarget Services)`.
- **[MODIFY]** `ServiceController`: add `POST /api/v1/services`, `PATCH /api/v1/services/{code}`, `DELETE /api/v1/services/{code}` (soft), `GET /api/v1/services/{code}`. Existing `GET /api/v1/services` stays untouched.
- **[NEW]** `shared/exception/GlobalExceptionHandler` (`@RestControllerAdvice`) — translates `ExistsException`→409, `NotFoundException`→404, `MethodArgumentNotValidException`→400 with a uniform `ErrorResponse {timestamp, status, error, message, path}` shape.
- **[NEW]** Bean validation on request DTOs: `@NotBlank` + `@Size` on `code` and `description` of `CreateServiceRequest` and `UpdateServiceRequest`. Whitespace trimmed in the use case before persistence.
- **[NEW]** Tests (RED→GREEN under strict TDD) — unit tests for each new use case with mocked ports + `@WebMvcTest` slice tests for the controller covering happy path and the three error-handler branches (400/404/409).

## Capabilities

### Modified Capabilities
- `services` — the bounded context's public contract expands with create, update, soft delete, and find-by-code.

### New Capabilities
- `shared-error-handling` — global exception → HTTP translation usable by every bounded context.

## Impact
- **Affected code**: all packages under `owl.tree.rmfarma.service.*` (entity, repository, ports, use cases, mapper, controller) plus new code under `owl.tree.rmfarma.shared.exception.*`.
- **Affected APIs**: 4 new endpoints under `/api/v1/services/*` (`POST`, `PATCH`, `DELETE`, `GET /{code}`).
- **Data migration**: none.
- **Backward compatibility**: `GET /api/v1/services` is unchanged. Existing `Services` entity is not modified in shape (only new repository methods). The `findByCode` port contract changes from nullable to `Optional`, which is safe today because no production caller consumes it.
- **Out of scope** (flagged for future changes): Envers config typo fix, hardcoded DB password in `application.yml`, Flyway introduction, pagination, UUID-based GET by id, auth/authorization.

## Risks
- [ ] Application-level uniqueness on `description` allows race-condition duplicates under concurrent requests. Document and accept for v1; revisit in a hardening change.
- [ ] Strict TDD mode + 0 existing tests in this context means this change lands with its first RED→GREEN specs. Expect a heavier first iteration.
- [ ] No global error handler exists today — POST/PATCH/DELETE will 500 without the `shared` module. Mitigation: include `GlobalExceptionHandler` in the same change.
- [ ] `ServicesPersistencePortAdapter.findByCode` returning null today: changing it to `Optional` is a contract change. Today only the unused port is affected; verify no production caller exists before merging.
- [ ] No migrations exist in the project, so no DDL scripts are produced. The `enabled` flag and audit columns already exist on `Services`.

## Open Questions
- (None — user decisions locked: uniqueness app-only, error handler in same change, GET by id = by code).
