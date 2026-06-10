# Design: crud-services

## 1. Context & Scope

Extend the `service` bounded context (`owl.tree.rmfarma.service`) from a read-only
listing (`GET /api/v1/services`) to a full CRUD contract — `POST`, `PATCH`,
`DELETE` (soft), and `GET /{code}` — and extend the existing
`shared/config/GlobalExceptionHandler` so uniqueness, not-found, and bean-validation
failures translate into uniform `409 / 404 / 400` responses backed by the existing
6-field `ErrorResponse` shape. Anchored in
`openspec/changes/crud-services/proposal.md` and the two spec deltas
(`specs/services`, `specs/shared-error-handling`). Strict TDD, hexagonal, no DB
constraints in v1, no migrations, no auth.

## 2. Architecture Alignment

The codebase is already hexagonal: `domain/ports/api` (in port),
`domain/ports/spi` (out port), `infrastructure/adapter` + `infrastructure/repository`
+ `infrastructure/mappers` (out), `application/*` (use cases), `userinterfaces`
(controller), `domain/data` (DTOs). This change adds use cases and ports but does
**not** change the layering rules.

```
ServiceController  ──@RestController──▶  CreateServiceUseCase / UpdateServiceUseCase
   (userinterfaces)                       DeleteServiceUseCase / GetServiceByCodeUseCase
                                              │
                                              ▼
                                       ServiceServicePort  (domain/ports/api IN)
                                              │
                                              ▼
                                       ServiceServicePortImpl  (domain/services)
                                              │
                                              ▼
                                       ServicesPersistencePort (domain/ports/spi OUT)
                                              │
                                              ▼
                                       ServicesPersistencePortAdapter (infrastructure)
                                              │
                                              ▼
                                       ServicesRepository  ──▶  Services (JPA)

On any uncaught domain exception
  ──▶ GlobalExceptionHandler (@RestControllerAdvice)  ──▶ ErrorResponse (6 fields)
```

Trim happens in the **use case** (the only place that knows the trim rule); the
request DTO is never mutated, the entity never sees padded values.

## 3. Component Changes

### 3.1 Entity & Repository

**No entity change.** `Services` already has `id (UUID)`, `code` (unique, 30),
`description` (100), `enabled` (default `true`), and Envers `@Audited`.

**`src/main/java/owl/tree/rmfarma/service/infrastructure/repository/ServicesRepository.java`** — add derived methods:

```java
boolean existsByCode(String code);
boolean existsByDescription(String description);
Optional<Services> findByDescriptionAndEnabledTrue(String description);
boolean existsByDescriptionAndIdNot(String description, String id);
boolean existsByCodeAndIdNot(String code, String id);
```

`existsByCode` is already covered by the column `unique = true`, but we still call
it at the use-case layer for a clean 409 (better message than letting the DB
throw) — accepted race risk per proposal.

### 3.2 Out port — `ServicesPersistencePort`

`src/main/java/owl/tree/rmfarma/service/domain/ports/spi/ServicesPersistencePort.java`
— replace current 2 methods with:

```java
Optional<Services> findByCode(String code);          // CHANGED: was ServiceResourceDto, now entity+Optional
List<ServiceResourceDto> findAll();
Services save(Services entity);                       // NEW: covers create + update (JPA upsert)
Optional<Services> findEnabledByCode(String code);   // NEW: explicit "not soft-deleted" lookup
void disableByCode(String code);                      // NEW: soft delete at persistence layer
```

**Contract change**: `findByCode` returns the **entity** (not a DTO) wrapped in
`Optional`. Rationale: use cases need the entity to mutate it (update, soft-delete)
and to check `id` for the `existsBy…AndIdNot` self-exclusion. The adapter is the
single mapper-to-DTO boundary on reads via `findAll`.

### 3.3 In port — `ServiceServicePort`

`src/main/java/owl/tree/rmfarma/service/domain/ports/api/ServiceServicePort.java`:

```java
List<ServiceResourceDto> findAll();
ServiceResourceDto create(CreateServiceRequest request);
ServiceResourceDto update(String code, UpdateServiceRequest request);
void deleteByCode(String code);
ServiceResourceDto findByCode(String code);
```

### 3.4 Use cases (`application/service/`)

All four new use cases are `@Component` classes depending on `ServiceServicePort`
(mirroring `FindServiceUseCase`). Trim rule lives in create + update only.

**`CreateServiceUseCase`**
- Input: `CreateServiceRequest`
- Flow: `String code = request.code().trim(); String description = request.description().trim();`
- `if (port.existsByCode(code)) throw new ExistsException("code", "Service", code);`
- `if (port.existsByDescription(description)) throw new ExistsException("description", "Service", description);`
- `Services entity = mapper.toEntity(trimmedCode, trimmedDescription);`
- `return mapper.toResource(port.save(entity));`

**`UpdateServiceUseCase`**
- Input: `String code` (path), `UpdateServiceRequest`
- Flow: `String trimmedCode = request.code().trim(); String trimmedDescription = request.description().trim();`
- `Services current = port.findByCode(code).orElseThrow(() -> new NotFoundException("Service", code));`
- `if (!trimmedCode.equals(current.getCode()) && port.existsByCodeAndIdNot(trimmedCode, current.getId())) throw new ExistsException("code", "Service", trimmedCode);`
- `if (!trimmedDescription.equals(current.getDescription()) && port.existsByDescriptionAndIdNot(trimmedDescription, current.getId())) throw new ExistsException("description", "Service", trimmedDescription);`
- `current.setCode(trimmedCode); current.setDescription(trimmedDescription);`
- `return mapper.toResource(port.save(current));`

**`DeleteServiceUseCase`**
- Input: `String code`
- Flow: `if (port.findEnabledByCode(code).isEmpty()) throw new NotFoundException("Service", code);`
- `port.disableByCode(code);` — adapter loads the entity, sets `enabled=false`, persists.
- No return value (`void`); controller responds `204 No Content`.

**`GetServiceByCodeUseCase`**
- Input: `String code`
- Flow: `return port.findByCode(code).map(mapper::toResource).orElseThrow(() -> new NotFoundException("Service", code));`

### 3.5 Mapper

**`src/main/java/owl/tree/rmfarma/service/infrastructure/mappers/ServicesMapper.java`**:

```java
@Mapper(componentModel = "spring")
public interface ServicesMapper {
    ServiceResourceDto toServiceResourceDto(Services services);
    Services toServices(CreateServiceRequest request);   // NEW: id=null, enabled=true
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateServiceRequest request, @MappingTarget Services services);  // NEW
}
```

`@BeanMapping(IGNORE)` keeps PATCH semantics — only non-null fields overwrite. For
create, the use case forces the two fields via setters after mapping so the
mapper stays free of `Boolean.TRUE` defaults.

### 3.6 Controller

**`src/main/java/owl/tree/rmfarma/service/userinterfaces/ServiceController.java`** —
add to existing class:

| Method | Path | Status | Body in | Body out |
|---|---|---|---|---|
| `POST` | `/api/v1/services` | `201 Created` | `CreateServiceRequest` (`@Valid`) | `ServiceResourceDto` |
| `PATCH` | `/api/v1/services/{code}` | `200 OK` | `UpdateServiceRequest` (`@Valid`) | `ServiceResourceDto` |
| `DELETE` | `/api/v1/services/{code}` | `204 No Content` | — | — |
| `GET` | `/api/v1/services/{code}` | `200 OK` | — | `ServiceResourceDto` |

No local `try/catch` — exceptions bubble to `GlobalExceptionHandler`. Existing
`GET /api/v1/services` untouched.

### 3.7 Request DTOs (`service/domain/data/service/`)

**`CreateServiceRequest`** (Java record, mirrors Lombok DTO style elsewhere):

```java
public record CreateServiceRequest(
    @NotBlank @Size(max = 30) String code,
    @NotBlank @Size(max = 100) String description
) {}
```

**`UpdateServiceRequest`** (record, PATCH — same constraints, both fields are
required by the spec's examples):

```java
public record UpdateServiceRequest(
    @NotBlank @Size(max = 30) String code,
    @NotBlank @Size(max = 100) String description
) {}
```

Trim does **not** happen in the DTO — it happens in the use case, on the values
**after** validation. Reusing `ServiceResourceDto` for responses (no change).

### 3.8 `GlobalExceptionHandler` (extension, not recreation)

**`src/main/java/owl/tree/rmfarma/shared/config/GlobalExceptionHandler.java`** —
keep existing `BusinessException`, `InfrastructureException`, and `NotFoundException`
mappings exactly as they are. Add two new handlers:

```java
@ExceptionHandler(ExistsException.class)
public ResponseEntity<ErrorResponse> handleExistsException(ExistsException ex, WebRequest request) {
    ErrorResponse body = new ErrorResponse(
        HttpStatus.CONFLICT,
        "Resource already exists",
        request.getDescription(false));
    body.addValidationError(ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex, WebRequest request) {
    ErrorResponse body = new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        "Validation failed",
        request.getDescription(false));
    ex.getBindingResult().getFieldErrors().forEach(fe ->
        body.addValidationError(fe.getField() + ": " + fe.getDefaultMessage()));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
}
```

**Conflict with existing handler**: the current `BusinessException` handler is
too coarse — it maps the parent of `ExistsException` to 400. Once the new
`ExistsException` handler is added, Spring picks the most specific match per
exception, so the new mapping wins for `ExistsException` while other
`BusinessException` subclasses still hit the existing 400 handler. **Do not
delete or alter** the existing handlers.

## 4. Error Mapping

| Trigger | Exception | HTTP | `ErrorResponse.status` | `ErrorResponse.error` | `ErrorResponse.message` | `ErrorResponse.errors` |
|---|---|---|---|---|---|---|
| `code` already exists (create) | `ExistsException("code","Service",code)` | 409 | 409 | `Conflict` | `Resource already exists` | `[ex.getMessage()]` |
| `description` already exists (create/update) | `ExistsException("description","Service",desc)` | 409 | 409 | `Conflict` | `Resource already exists` | `[ex.getMessage()]` |
| code used by another service (update) | `ExistsException("code","Service",code)` | 409 | 409 | `Conflict` | `Resource already exists` | `[ex.getMessage()]` |
| `GET/PATCH/DELETE /{code}` miss | `NotFoundException("Service",code)` | 404 | 404 | `Not Found` | `Resource not found` | `[ex.getMessage()]` |
| empty `code` or oversized field | `MethodArgumentNotValidException` | 400 | 400 | `Bad Request` | `Validation failed` | `["code: must not be blank", "description: size must be between 0 and 100", …]` |
| any other `BusinessException` (unchanged) | `BusinessException` | 400 | 400 | (existing) | `Validation failed` | `[ex.getMessage()]` |
| any other `InfrastructureException` (unchanged) | `InfrastructureException` | 400 | 400 | (existing) | `Validation Failed` | `[ex.getMessage()]` |

`timestamp` is set in the `ErrorResponse` constructor via `LocalDateTime.now()`.
`path` comes from `request.getDescription(false)`. `errors` is never `null`
(constructor initializes an empty list).

## 5. Data Flow Examples

### 5.1 Happy path — `POST /api/v1/services`

```
Client
  │  POST { "code":"  SRV-001  ", "description":"  Blood pressure  " }
  ▼
ServiceController.create(req)
  │  @Valid → CreateServiceRequest(code="  SRV-001  ", description="  Blood pressure  ")
  ▼
CreateServiceUseCase.create(req)
  │  code = req.code().trim()           → "SRV-001"
  │  desc = req.description().trim()    → "Blood pressure"
  │  port.existsByCode("SRV-001")       → false
  │  port.existsByDescription("Blood pressure") → false
  │  mapper.toServices(req)             → Services(id=null, code="SRV-001", description="Blood pressure", enabled=true)
  │  port.save(entity)                  → Services(id="uuid-…", code="SRV-001", description="Blood pressure", enabled=true)
  │  mapper.toResource(saved)           → ServiceResourceDto(id, code, description, enabled)
  ▼
ServiceController
  │  ResponseEntity.status(201).body(dto)
  ▼
Client  ←  201 Created  +  JSON body
```

### 5.2 Error path — `PATCH /api/v1/services/SRV-001` with duplicate description

```
Client
  │  PATCH { "code":"SRV-001", "description":"  Checkup  " }     // "Checkup" already used by SRV-002
  ▼
ServiceController.update("SRV-001", req)
  │  @Valid passes (both non-blank, within size)
  ▼
UpdateServiceUseCase.update("SRV-001", req)
  │  trimmedCode = "SRV-001", trimmedDesc = "Checkup"
  │  port.findByCode("SRV-001")           → Optional[Services(id="A", code="SRV-001", description="Old", enabled=true)]
  │  code unchanged  → skip existsByCodeAndIdNot
  │  desc changed    → port.existsByDescriptionAndIdNot("Checkup","A") → true
  │  throw new ExistsException("description","Service","Checkup")
  ▼
GlobalExceptionHandler.handleExistsException(...)
  │  body = ErrorResponse(CONFLICT, "Resource already exists", "uri=/api/v1/services/SRV-001")
  │  body.addValidationError("El Service ya existe en el sistema con el description con clave Checkup.")
  │  return 409 with body
  ▼
Client  ←  409 Conflict  +  ErrorResponse JSON  (no DB write)
```

## 6. Testing Strategy (Strict TDD, RED→GREEN)

`openspec/config.yaml` mandates `strict_tdd: true` and the only existing test is
a commented-out context-loads test — every test below is new. JUnit5 + AssertJ +
Mockito + `@WebMvcTest` + `@DataJpaTest` available per config.

### 6.1 Unit tests (no Spring context)

| Test class | What it covers |
|---|---|
| `service/application/service/CreateServiceUseCaseTest` | trimmed values pass to `existsByCode`/`existsByDescription`; `ExistsException("code",…)` thrown on code hit; `ExistsException("description",…)` on description hit; happy path returns `ServiceResourceDto` from `port.save(...)`. Pure Mockito on `ServiceServicePort` and `ServicesMapper` (mapper is also injected into the use case to convert the entity the port returns into the DTO). |
| `service/application/service/UpdateServiceUseCaseTest` | same-row self-exclusion (sending its own `code`/`description` is a no-op for uniqueness); `ExistsException` on cross-row conflict; `NotFoundException` on `findByCode` empty; `setCode`/`setDescription` invoked with trimmed values; `port.save(current)` called once. |
| `service/application/service/DeleteServiceUseCaseTest` | `port.findEnabledByCode(code)` empty → `NotFoundException`; present → `port.disableByCode(code)` invoked once. |
| `service/application/service/GetServiceByCodeUseCaseTest` | present → returns mapped DTO; empty → `NotFoundException`. |

Pattern: `@ExtendWith(MockitoExtension.class)`, `@Mock ServiceServicePort`,
`@Mock ServicesMapper`, `InjectMocks` the use case, AssertJ assertions.

### 6.2 `@WebMvcTest` slice — controller

`service/userinterfaces/ServiceControllerTest` (`@WebMvcTest(ServiceController.class)`,
`@MockBean` on the four use cases + `ServicesMapper` is not needed at the
controller layer because the response DTO is built inside the use case in the
design). Cover with `MockMvc`:

| Case | Method | Path | Expected |
|---|---|---|---|
| list happy | `GET` | `/api/v1/services` | 200 + JSON array |
| create happy | `POST` | `/api/v1/services` (valid body) | 201 + `ServiceResourceDto` |
| create blank code | `POST` | `/api/v1/services` (empty `code`) | 400 + `ErrorResponse` with `code: must not be blank` in `errors` |
| create oversize description | `POST` | `/api/v1/services` (101-char `description`) | 400 + `description: size must be…` |
| create duplicate code | `POST` | (use case throws `ExistsException("code",…)`) | 409 + uniform body |
| create duplicate description | `POST` | (use case throws `ExistsException("description",…)`) | 409 |
| update happy | `PATCH` | `/api/v1/services/SRV-001` | 200 |
| update not found | `PATCH` | `/api/v1/services/SRV-X` | 404 |
| update duplicate other | `PATCH` | (cross-row) | 409 |
| update blank code | `PATCH` | empty `code` | 400 + `errors` |
| delete happy | `DELETE` | `/api/v1/services/SRV-001` | 204 (empty body) |
| delete not found | `DELETE` | `/api/v1/services/SRV-X` | 404 |
| get happy | `GET` | `/api/v1/services/SRV-001` | 200 |
| get not found | `GET` | `/api/v1/services/SRV-X` | 404 |

JSON assertions verify the 6 `ErrorResponse` fields and that `errors` is a
non-null array (AssertJ `extracting`).

### 6.3 `@DataJpaTest`

`service/infrastructure/repository/ServicesRepositoryTest` (`@DataJpaTest`,
in-memory MySQL not configured — uses H2 if available, otherwise
`@AutoConfigureTestDatabase`). Justified because the spec's "Repository methods
are available and return expected results" scenario is a real contract
guarantee. Verifies the five derived methods (`existsByCode`,
`existsByDescription`, `findByDescriptionAndEnabledTrue`,
`existsByDescriptionAndIdNot`, `existsByCodeAndIdNot`) including the
self-exclusion behavior.

## 7. Open Risks & Mitigations

Carried from the proposal:

1. **Application-level uniqueness race** — two concurrent POSTs with the same
   `description` could both pass the pre-check and let the DB reject the second.
   Mitigated only by the `unique = true` on `Services.code` (no DB-level unique
   on `description`). **Accepted in v1**, logged in proposal. Hardening change
   needed: either DB constraint + retry/translation, or a database-level
   lock.
2. **Strict TDD on a zero-tests context** — first RED→GREEN cycle for service.
   Mitigated by writing all use-case + controller tests before any production
   code in the apply phase.
3. **No prior global handler** — every controller in every context was 500-ing
   on domain exceptions. Now mitigated by extending the existing handler in the
   same change.
4. **`findByCode` port contract change** (nullable → `Optional<Services>`) —
   only the unused port method is affected, but `FindServiceUseCase` and
   `ServiceController` already exist; verify with the apply phase that no
   other code path reads the old shape. Mitigated by searching for
   `servicesPersistencePort.findByCode` and `ServiceServicePort.findByCode`
   during the change.

New from this design:

5. **Handler precedence** — Spring's `@ExceptionHandler` resolution picks the
   most specific subclass, so the new `ExistsException` handler wins over the
   existing `BusinessException` handler. Verified by reading the inheritance:
   `ExistsException extends BusinessException`. The unit test for
   `ExistsException → 409` in the controller slice locks this in.
6. **`@BeanMapping(IGNORE)` semantics** — chosen so that future partial-PATCH
   expansions (e.g. `description` only) don't require touching the mapper.
   Trade-off: a `null` `code` in the PATCH body is silently ignored today,
   which could surprise clients. Mitigated by `@NotBlank` on the DTO field —
   any `null` is rejected at validation time, so the `IGNORE` path is
   unreachable for the current spec. If a future spec wants "code is
   immutable", remove it from `UpdateServiceRequest` entirely.
7. **`MethodArgumentNotValidException` field message format** — proposal/spec
   don't pin the exact `errors[i]` shape. This design chooses
   `"<field>: <defaultMessage>"` (e.g. `"code: must not be blank"`) to match
   the spec's example verbatim. If clients need a different shape later, it's
   a one-line change in the handler.

## 8. Out of Scope

- Envers configuration typo fix (proposal flag).
- Hardcoded DB password in `application.yml` (proposal flag).
- Flyway / database migrations (proposal flag — no DDL produced).
- Pagination on `GET /api/v1/services` (proposal flag).
- `GET /api/v1/services/{id}` by UUID (proposal flag — code is the public
  identifier in this domain).
- Auth/authorization (proposal flag).
- Hardening of the description uniqueness race (Risk #1 — separate change).
- Hard delete endpoint (only soft delete is in scope; spec is explicit).
- Any other bounded context's controllers — they benefit transparently from
  `GlobalExceptionHandler` once the handler is updated, but no controller
  outside `service` is modified in this change.
