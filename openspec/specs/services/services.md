# Spec: services

> Canonical snapshot — archived from `openspec/changes/crud-services/specs/services/spec.md`
> Change: `crud-services` | Archived: 2026-06-10

## Requirement: Service catalog CRUD endpoints

The `service` bounded context SHALL expose the full CRUD contract for a
pharmaceutical service catalog at the base path `/api/v1/services`, in
addition to the already-shipped `GET /api/v1/services` listing. All write
operations on the catalog are routed through dedicated use cases and the
shared error handler so that validation, uniqueness, and not-found
conditions are translated into the appropriate HTTP responses.

### Scenario: Create a new service with valid data

- **Given** a `POST /api/v1/services` request with body
  `{ "code": "SRV-001 ", "description": "  Blood pressure check  " }`
  and no existing `Services` row with that `code` or `description`
- **When** the request is processed by `CreateServiceUseCase`
- **Then** the `code` and `description` are trimmed before any
  uniqueness check or persistence
- **And** a new `Services` row is persisted with `enabled = true`
- **And** the response is `201 Created` (or `200 OK`) with a body of
  `ServiceResourceDto` containing the trimmed values and the generated
  `id`.

### Scenario: Reject create when code already exists

- **Given** a `POST /api/v1/services` request whose trimmed `code`
  matches an existing `Services.code`
- **When** the use case invokes the uniqueness pre-check
- **Then** the use case throws `ExistsException("code", "Service", code)`
- **And** the global exception handler returns `409 Conflict` with an
  `ErrorResponse` body; **no** row is persisted.

### Scenario: Reject create when description already exists

- **Given** a `POST /api/v1/services` request whose trimmed
  `description` matches an existing enabled `Services.description`
- **When** the use case invokes the uniqueness pre-check
- **Then** the use case throws `ExistsException("description",
  "Service", description)`
- **And** the global exception handler returns `409 Conflict`; **no**
  row is persisted.

### Scenario: Reject create when request body fails bean validation

- **Given** a `POST /api/v1/services` request with an empty `code`
  (or `description` exceeding 100 chars / `code` exceeding 30 chars)
- **When** Spring deserializes the body and bean validation runs on
  `CreateServiceRequest` (`@NotBlank` + `@Size`)
- **Then** Spring raises `MethodArgumentNotValidException`
- **And** the global exception handler returns `400 Bad Request` with an
  `ErrorResponse` whose `errors` list contains the field-level
  violation messages; the use case is never invoked.

## Requirement: Partial update of a service by id

`PATCH /api/v1/services/{id}` SHALL perform a partial update of the
service identified by `{id}`, delegating to `UpdateServiceUseCase`.
The use case loads the existing entity by id (throwing
`NotFoundException` on miss), re-checks uniqueness of `code` and
`description` **excluding the current entity's id**, applies the
trimmed incoming values, and persists. Bean validation is enforced on
the request DTO before the use case runs.

### Scenario: Update description on an existing service

- **Given** a `PATCH /api/v1/services/00000000-0000-0000-0000-000000000001` request with body
  `{ "description": "  Updated description  " }`
- **And** no other enabled `Services` row has the trimmed description
  `Updated description`
- **When** the use case runs
- **Then** the existing entity is loaded, the description is updated
  with the trimmed value, and the entity is persisted
- **And** the response is `200 OK` with a `ServiceResourceDto` body.

### Scenario: Update keeps the same code (self-exclusion)

- **Given** a `PATCH /api/v1/services/00000000-0000-0000-0000-000000000001` request that re-sends
  the entity's own `code` and `description` unchanged (after trim)
- **When** the use case runs the uniqueness re-check
- **Then** the re-check uses `existsByCodeAndIdNot(code, id)` and
  `existsByDescriptionAndIdNot(description, id)`
- **And** the entity is persisted without raising `ExistsException`
  because the current row is excluded from the check.

### Scenario: Reject update when code is already used by another service

- **Given** a `PATCH /api/v1/services/00000000-0000-0000-0000-000000000001` request whose trimmed
  `code` matches the `code` of a **different** existing `Services` row
- **When** the use case runs the uniqueness re-check
- **Then** the use case throws `ExistsException("code", "Service",
  code)`
- **And** the global exception handler returns `409 Conflict`; the
  original row is unchanged.

### Scenario: Reject update when service id does not exist

- **Given** a `PATCH /api/v1/services/00000000-0000-0000-0000-000000000099` request
- **When** the use case tries to load the entity
- **Then** the persistence port returns `Optional.empty()` and the use
  case throws `NotFoundException("Service", "00000000-0000-0000-0000-000000000099")`
- **And** the global exception handler returns `404 Not Found`.

### Scenario: Reject update when request body fails bean validation

- **Given** a `PATCH /api/v1/services/00000000-0000-0000-0000-000000000001` request with a blank
  `code` or values that exceed the size limits
- **When** Spring validates `UpdateServiceRequest`
- **Then** `MethodArgumentNotValidException` is raised
- **And** the global exception handler returns `400 Bad Request` with
  field-level violation messages in `errors`; the entity is unchanged.

## Requirement: Soft delete of a service by id

`DELETE /api/v1/services/{id}` SHALL soft-delete the service by
flipping its `enabled` flag to `false` and persisting the change. The
endpoint SHALL NOT remove the row. A second delete on an already
disabled service SHALL be reported as `404 Not Found` to keep the
contract idempotent and to avoid resurrecting/auditing a no-op.

### Scenario: Soft delete an existing enabled service

- **Given** a service `00000000-0000-0000-0000-000000000001` exists with `enabled = true`
- **When** `DELETE /api/v1/services/00000000-0000-0000-0000-000000000001` is processed by
  `DeleteServiceUseCase`
- **Then** the entity is loaded, `enabled` is set to `false`, and the
  entity is persisted
- **And** the response is `204 No Content` (or `200 OK`); the row
  remains in the table.

### Scenario: Reject delete when service id does not exist

- **Given** no `Services` row has `id = "00000000-0000-0000-0000-000000000099"`
- **When** `DELETE /api/v1/services/00000000-0000-0000-0000-000000000099` is processed
- **Then** `DeleteServiceUseCase` throws
  `NotFoundException("Service", "00000000-0000-0000-0000-000000000099")`
- **And** the global exception handler returns `404 Not Found`.

## Requirement: Get a service by id

`GET /api/v1/services/{id}` SHALL return the service resource for the
given id. A miss MUST be translated into `404 Not Found` by the use
case, not by the controller or the persistence adapter.

### Scenario: Return an existing service by id

- **Given** a service `00000000-0000-0000-0000-000000000001` exists and is enabled
- **When** `GET /api/v1/services/00000000-0000-0000-0000-000000000001` is processed
- **Then** `GetServiceByIdUseCase` returns a `ServiceResourceDto`
- **And** the response is `200 OK` with that DTO in the body.

### Scenario: Return 404 when the id does not exist

- **Given** no `Services` row has `id = "00000000-0000-0000-0000-000000000099"`
- **When** `GET /api/v1/services/00000000-0000-0000-0000-000000000099` is processed
- **Then** `GetServiceByIdUseCase` throws
  `NotFoundException("Service", "00000000-0000-0000-0000-000000000099")`
- **And** the global exception handler returns `404 Not Found` with an
  `ErrorResponse` body.

## Requirement: Whitespace trimming in use cases

Both `CreateServiceUseCase` and `UpdateServiceUseCase` SHALL trim
leading and trailing whitespace from `code` and `description` **before**
running any uniqueness check and **before** persisting. The trimmed
values are the ones stored and exposed through `ServiceResourceDto`.
Request DTOs are NOT mutated; trimming happens inside the use case so
the persistence layer never sees padded values.

### Scenario: Trimmed values are persisted and uniqueness is checked against trimmed values

- **Given** a request with `"code": "  SRV-001  "` and no row matches
  the trimmed value `"SRV-001"`
- **When** `CreateServiceUseCase` (or `UpdateServiceUseCase`) runs
- **Then** the uniqueness checks
  (`existsByCode` / `existsByCodeAndIdNot` /
  `existsByDescription` / `existsByDescriptionAndIdNot`) are evaluated
  using the **trimmed** strings
- **And** the persisted entity stores the **trimmed** strings.

## Requirement: Repository contract for uniqueness and lookup

`ServicesRepository` (Spring Data JPA) SHALL expose the following
derived query methods used by the use cases:

- `boolean existsByCode(String code)`
- `boolean existsByDescription(String description)`
- `Optional<Services> findByDescriptionAndEnabledTrue(String
  description)`
- `boolean existsByDescriptionAndIdNot(String description, String id)`
- `boolean existsByCodeAndIdNot(String code, String id)`

The `ServicesPersistencePort.findById(String)` SPI contract SHALL return
`Optional<Services>` instead of a nullable entity, so the use case
layer (and only the use case layer) is responsible for translating
"not present" into `NotFoundException`. `findById` is the canonical
lookup for the write paths (PATCH, DELETE) and the new `GET` endpoint.

### Scenario: Repository methods are available and return expected results

- **Given** a `Services` row with `id = "00000000-0000-0000-0000-000000000001"`,
  `code = "SRV-001"`, `description = "Checkup"`, `enabled = true`
- **When** the new repository methods are invoked
- **Then** `existsByCode("SRV-001")` returns `true` and
  `existsByCode("OTHER")` returns `false`
- **And** `existsByDescription("Checkup")` returns `true` and
  `findByDescriptionAndEnabledTrue("Checkup")` returns `Optional`
  containing the row
- **And** `existsByCodeAndIdNot("SRV-001", "00000000-0000-0000-0000-000000000001")` returns `false`
  (self excluded) while `existsByCodeAndIdNot("SRV-001", "other-id")`
  returns `true`.

### Scenario: findById returns Optional.empty on miss

- **Given** no `Services` row with `id = "00000000-0000-0000-0000-0000000000aa"`
- **When** `ServicesPersistencePort.findById("00000000-0000-0000-0000-0000000000aa")` is invoked
- **Then** it returns `Optional.empty()` (not `null`).
