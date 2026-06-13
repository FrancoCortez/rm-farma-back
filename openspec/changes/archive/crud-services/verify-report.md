# Verification Report

**Change**: `crud-services`
**Version**: spec v1 (proposal + 2 deltas, 12 commits landed on `master`)
**Mode**: Strict TDD
**Verifier role**: sdd-verify sub-agent (fresh context, adversarial posture)

---

## Verdict: ❌ **FAIL**

**Reason**: HEAD does not compile. The 12 implementation commits depend on an `enabled` field on `Services` and a matching `enabled` field on `ServiceResourceDto` — both of which exist **only in the uncommitted working tree**. Stashing the WIP and running `mvn compile` produces 3 `cannot find symbol setEnabled` errors. `mvn verify` only passes because the dirty working tree supplies the missing fields. The change as committed is broken.

---

## Executive summary

1. **Headline finding** (CRITICAL): The 12 commits land an implementation that compiles and passes 72/72 tests **only with the unrelated WIP in the working tree**. Two WIP-modified files in the `service` package — `Services.java` (entity) and `ServiceResourceDto.java` — silently supply the `enabled` field that the new use cases, adapter, and tests require. The proposal explicitly said "**No entity change**" and the design carried that forward. The implementation chose to depend on an `enabled` field that it never committed. With WIP stashed, `mvn compile` fails; the CRUD slice is unbuildable from HEAD alone.
2. **Test execution** otherwise green: `mvn -B clean test` → 72/72 pass; `mvn -B verify` → BUILD SUCCESS, 72/72 pass. Coverage is not configured (`jacoco: false`) per `openspec/config.yaml`, so per the sdd-verify rules I skip coverage reporting and do not flag it.
3. **Spec ↔ test mapping** is otherwise strong: 11 of 12 spec scenarios have covering tests, and every test was asserted against the code. The one gap is `Repository methods are available and return expected results` (partially covered — see table). The `getByCode→404` on GET is covered; the `PATCH→409 on duplicate other` is covered.
4. **Documented deviations** in `tasks.md` are real and the most material is real: the port contract change broke `DiagnosisPatientServiceImpl`, and the team added a sibling `findResourceByCode` to keep that caller working. That deviation is documented, justified, and the spec still holds. The other deviations (Phase 4 handlers landing with Phase 2, standalone `MockMvc` instead of `@WebMvcTest`, `ServiceServicePortImpl` having no production caller) are also documented and benign.
5. **Undocumented deviation** (CRITICAL): the implementation depends on an `enabled` column on the `services` entity and an `enabled` field on `ServiceResourceDto` that were never committed and were never called out in the "Implementation deviations" section. This is the blocker.
6. **Working tree**: 68 modified + 12 untracked files, all unrelated to `crud-services`. Confirmed by intersection with the 36 files touched in the 12 commits = 0. The user already chose to leave this WIP alone — I do not touch it.

---

## Static evidence (Step 1)

### Component inventory

| Component | Spec/Design | File path | Status |
|---|---|---|---|
| `Services` entity | "No entity change" | `src/main/java/owl/tree/rmfarma/service/infrastructure/entities/Services.java` | ❌ Missing `enabled` field at HEAD. Working tree supplies it. |
| `ServicesRepository` derived methods | spec REQ §"Repository contract" | `src/main/java/owl/tree/rmfarma/service/infrastructure/repository/ServicesRepository.java` | ✅ All 5 methods present (`existsByCode`, `existsByDescription`, `findByDescriptionAndEnabledTrue`, `existsByCodeAndIdNot`, `existsByDescriptionAndIdNot`) plus `findByCode` and `findByCodeAndEnabledTrue` for the soft-delete semantics. |
| `ServicesPersistencePort` | spec §"Repository contract" | `src/main/java/owl/tree/rmfarma/service/domain/ports/spi/ServicesPersistencePort.java` | ✅ `findByCode → Optional<Services>`, `findResourceByCode → Optional<DTO>` (deviation: sibling added), `save`, `findEnabledByCode`, `disableByCode`, plus 4 `exists*` methods. |
| `ServiceServicePort` (in port) | design §3.3 | `src/main/java/owl/tree/rmfarma/service/domain/ports/api/ServiceServicePort.java` | ✅ 5 methods (`findAll`, `create`, `update`, `deleteByCode`, `findByCode`). |
| `ServiceServicePortImpl` | design §3.3 | `src/main/java/owl/tree/rmfarma/service/domain/services/ServiceServicePortImpl.java` | ✅ All 5 methods implemented. **Note: deviation — impl delegates straight to SPI port + mapper, not via domain service layer. No production caller exists for this impl.** |
| `CreateServiceUseCase` | design §3.4 | `src/main/java/owl/tree/rmfarma/service/application/service/CreateServiceUseCase.java` | ✅ Trims in use case (line 20–21), runs `existsByCode` then `existsByDescription` (lines 23–28), throws `ExistsException` with the documented field name, forces `entity.setEnabled(Boolean.TRUE)` (line 32), saves, maps to DTO. **Deviation: use case depends on `ServicesPersistencePort` + `ServicesMapper` rather than `ServiceServicePort`.** |
| `UpdateServiceUseCase` | design §3.4 | `src/main/java/owl/tree/rmfarma/service/application/service/UpdateServiceUseCase.java` | ✅ Trims, loads via `findByCode` (line 24), self-exclusion via `!trimmed.equals(current.getX()) && exists*AndIdNot` (lines 27–34), applies trimmed values, saves, maps. |
| `DeleteServiceUseCase` | design §3.4 | `src/main/java/owl/tree/rmfarma/service/application/service/DeleteServiceUseCase.java` | ✅ `findEnabledByCode` empty → `NotFoundException`; otherwise `disableByCode`. |
| `GetServiceByCodeUseCase` | design §3.4 | `src/main/java/owl/tree/rmfarma/service/application/service/GetServiceByCodeUseCase.java` | ✅ `findByCode` empty → `NotFoundException`; otherwise maps to DTO. |
| `ServicesMapper` | design §3.5 | `src/main/java/owl/tree/rmfarma/service/infrastructure/mappers/ServicesMapper.java` | ✅ `toServiceResourceDto`, `toServices(CreateServiceRequest)`, `updateEntityFromRequest(@BeanMapping IGNORE)`. |
| `ServicesPersistencePortAdapter` | design §3.2 / §3.4 | `src/main/java/owl/tree/rmfarma/service/infrastructure/adapter/ServicesPersistencePortAdapter.java` | ✅ `findByCode` returns empty on null/blank, calls `findByCodeAndEnabledTrue`; `findResourceByCode` same; `save` delegates; `disableByCode` flips `enabled` to false and saves; all 4 `exists*` delegate. |
| `ServiceController` | design §3.6 | `src/main/java/owl/tree/rmfarma/service/userinterfaces/ServiceController.java` | ✅ `GET /api/v1/services` (200), `POST /api/v1/services` (201), `PATCH /{code}` (200), `DELETE /{code}` (204), `GET /{code}` (200). All `@Valid` annotations on bodies. No try/catch. |
| `CreateServiceRequest` DTO | design §3.7 | `src/main/java/owl/tree/rmfarma/service/domain/data/service/CreateServiceRequest.java` | ✅ Java record, `@NotBlank @Size(max=30) String code`, `@NotBlank @Size(max=100) String description`. No trim logic (correct). |
| `UpdateServiceRequest` DTO | design §3.7 | `src/main/java/owl/tree/rmfarma/service/domain/data/service/UpdateServiceRequest.java` | ✅ Same constraints. |
| `GlobalExceptionHandler` (existing) | design §3.8 / spec §"Centralized handler" | `src/main/java/owl/tree/rmfarma/shared/config/GlobalExceptionHandler.java` | ✅ **EXTENDS, does not recreate.** `BusinessException` handler (line 20), `InfrastructureException` handler (line 27), `NotFoundException` handler (line 41) all preserved untouched. New `ExistsException` handler added (line 48). New `MethodArgumentNotValidException` handler added (line 55). |
| `ErrorResponse` | spec §"Uniform ErrorResponse shape" | `src/main/java/owl/tree/rmfarma/shared/exception/data/ErrorResponse.java` | ✅ Six fields (`timestamp`, `status`, `error`, `message`, `path`, `errors`). Constructor initializes `timestamp = LocalDateTime.now()`, `errors = new ArrayList<>()` — never null. `addValidationError` is the only mutator. |
| `DiagnosisPatientServiceImpl` fix | design deviation | `src/main/java/owl/tree/rmfarma/patient/domain/services/DiagnosisPatientServiceImpl.java` | ✅ Line 52 now calls `servicesPersistencePort.findResourceByCode(entry.getServices()).orElse(null)` — preserves the original null-on-miss behavior. |

### Trimming rule check (Step 1)

The design requires trim to live in the use case, not the DTO. The DTOs are bare records with bean-validation only — no trim there. The 4 use cases that touch the strings (`Create`, `Update`) trim with `request.code() == null ? null : request.code().trim()` — confirmed in lines 20–21 of both `CreateServiceUseCase` and `UpdateServiceUseCase`. ✅

### Controller status codes (Step 1)

- `POST` → `ResponseEntity.status(HttpStatus.CREATED).body(created)` (line 44 of `ServiceController`)
- `PATCH` → `ResponseEntity.ok(updated)` (line 51)
- `DELETE` → `ResponseEntity.noContent().build()` (line 57)
- `GET /{code}` → `ResponseEntity.ok(...)` (line 62)

All match the design. ✅

### DB-level unique constraint check (Step 1)

The proposal says "**out of scope**: ... hardening of the description uniqueness race". I confirmed by reading the entity: only `code` has `unique = true` (line 27 of `Services.java`). No migration files were added (`src/main/resources/**/*.sql` is empty). ✅

---

## Spec ↔ test mapping (Step 2)

Status legend: ✅ COMPLIANT (covering test exists and passed at runtime) · ⚠️ PARTIAL · ❌ UNTESTED

### Capability: `services` (7 requirements, 13 scenarios)

| Spec requirement | Scenario | Test class > method | Status |
|---|---|---|---|
| Service catalog CRUD endpoints | Create a new service with valid data | `ServiceControllerTest#createReturns201OnValidBody` + `CreateServiceUseCaseTest#createTrimsValuesBeforeUniquenessCheck` | ✅ |
| Service catalog CRUD endpoints | Reject create when code already exists | `ServiceControllerTest#createReturns409OnDuplicateCode` + `CreateServiceUseCaseTest#createThrowsExistsExceptionOnCodeCollision` | ✅ |
| Service catalog CRUD endpoints | Reject create when description already exists | `CreateServiceUseCaseTest#createThrowsExistsExceptionOnDescriptionCollision` | ✅ (use-case level — controller-level only has the `code` variant; the description variant of the 409 in the controller is covered transitively by the shared `ExistsException→409` test) |
| Service catalog CRUD endpoints | Reject create when request body fails bean validation | `ServiceControllerTest#createReturns400OnBlankCode` + `#createReturns400OnOversizeDescription` + `CreateServiceRequestValidationTest` | ✅ |
| Partial update of a service by code | Update description on an existing service | `UpdateServiceUseCaseTest#updatePersistsTrimmedValuesAndReturnsMappedDto` + `ServiceControllerTest#updateReturns200OnValidBody` | ✅ |
| Partial update of a service by code | Update keeps the same code (self-exclusion) | `UpdateServiceUseCaseTest#updateAllowsSelfExclusionForSameCodeAndDescription` | ✅ |
| Partial update of a service by code | Reject update when code is already used by another service | `UpdateServiceUseCaseTest#updateThrowsExistsExceptionWhenCodeIsTakenByAnotherService` + `ServiceControllerTest#updateReturns409OnCrossRowDuplicate` | ✅ |
| Partial update of a service by code | Reject update when service code does not exist | `UpdateServiceUseCaseTest#updateThrowsNotFoundExceptionWhenServiceDoesNotExist` | ✅ (use-case level — controller slice asserts 404 status only when use case throws NotFoundException, which is the same code path) |
| Partial update of a service by code | Reject update when request body fails bean validation | `ServiceControllerTest#updateReturns400OnBlankCode` + `UpdateServiceRequestValidationTest` | ✅ |
| Soft delete of a service by code | Soft delete an existing enabled service | `DeleteServiceUseCaseTest#deleteDisablesServiceWhenPresent` + `ServiceControllerTest#deleteReturns204OnExisting` | ✅ |
| Soft delete of a service by code | Reject delete when service code does not exist | `DeleteServiceUseCaseTest#deleteThrowsNotFoundWhenServiceMissing` + `ServiceControllerTest#deleteReturns404WhenMissing` | ✅ |
| Get a service by code | Return an existing service by code | `GetServiceByCodeUseCaseTest#findByCodeReturnsMappedDtoWhenPresent` + `ServiceControllerTest#getByCodeReturns200WhenFound` | ✅ |
| Get a service by code | Return 404 when the code does not exist | `GetServiceByCodeUseCaseTest#findByCodeThrowsNotFoundWhenMissing` + `ServiceControllerTest#getByCodeReturns404WhenMissing` | ✅ |
| Whitespace trimming in use cases | Trimmed values are persisted and uniqueness is checked against trimmed values | `CreateServiceUseCaseTest#createTrimsValuesBeforeUniquenessCheck` + `UpdateServiceUseCaseTest#updatePersistsTrimmedValuesAndReturnsMappedDto` | ✅ |
| Repository contract for uniqueness and lookup | Repository methods are available and return expected results | `ServicesRepositoryTest` (9 test methods) | ⚠️ PARTIAL — `existsByCode` true/false, `existsByDescription`, `findByDescriptionAndEnabledTrue` present/empty, `existsByCodeAndIdNot` self-exclusion + other, `existsByDescriptionAndIdNot` self-exclusion + other. The spec scenario mentions `findByDescriptionAndEnabledTrue` and is covered. The variant that "another service's id finds the row" via `existsByCodeAndIdNot` is also covered. **Gap: `existsByCode` is asserted true/false but no test for `existsByCodeAndIdNot` returns false for non-existent code** — the spec scenario only demands the self-exclusion behavior, so this is fine. |
| Repository contract for uniqueness and lookup | findByCode returns Optional.empty on miss | `ServicesPersistencePortAdapterTest#findByCodeReturnsEmptyOptionalWhenRepositoryMisses` + `#findByCodeReturnsEmptyOptionalWhenCodeIsNull` + `#findByCodeReturnsEmptyOptionalWhenCodeIsBlank` | ✅ |

### Capability: `shared-error-handling` (5 requirements, 6 scenarios)

| Spec requirement | Scenario | Test class > method | Status |
|---|---|---|---|
| Uniform `ErrorResponse` shape | ErrorResponse fields are present and well-formed | Implicit in `GlobalExceptionHandlerExistsTest#existsExceptionMapsTo409Conflict` (asserts `status`, `error`, `message`, `path`, `errors` are all present and well-typed) and `GlobalExceptionHandlerValidationTest#blankCodeProduces400WithFieldError` (asserts the same shape). `timestamp` is not explicitly asserted. | ⚠️ PARTIAL — the 5 scalar fields + `errors` array are all asserted. The spec scenario demands `timestamp (ISO-8601 local date-time)`. The Jackson `JavaTimeModule` is registered in both tests' setUp, so the value will serialize; but **no test asserts the actual format or presence of `timestamp`**. This is a minor gap. |
| Map `ExistsException` to `409 Conflict` | ExistsException surfaces as 409 | `GlobalExceptionHandlerExistsTest#existsExceptionMapsTo409Conflict` + `ServiceControllerTest#createReturns409OnDuplicateCode` | ✅ |
| Map `NotFoundException` to `404 Not Found` | NotFoundException surfaces as 404 | `ServiceControllerTest#deleteReturns404WhenMissing` + `#getByCodeReturns404WhenMissing` | ✅ (no dedicated test file, but the controller slice exercises the handler) |
| Map `MethodArgumentNotValidException` to `400 Bad Request` | Bean validation failure surfaces as 400 with field errors | `GlobalExceptionHandlerValidationTest#blankCodeProduces400WithFieldError` + `#oversizeDescriptionProduces400WithFieldError` + `ServiceControllerTest#createReturns400OnBlankCode` + `#createReturns400OnOversizeDescription` + `#updateReturns400OnBlankCode` | ✅ |
| Map `MethodArgumentNotValidException` to `400 Bad Request` | Bean validation on PATCH request also returns 400 | `ServiceControllerTest#updateReturns400OnBlankCode` + `UpdateServiceRequestValidationTest` | ✅ |
| Centralized, cross-cutting handler | Handler covers controllers from any bounded context | `GlobalExceptionHandlerExistsTest#existsExceptionMapsTo409Conflict` (uses an isolated throwing controller) + `GlobalExceptionHandlerValidationTest` (uses an isolated `@Valid` controller) | ✅ — the handler is `@RestControllerAdvice` with no `basePackages`, so it covers everything. The dedicated tests in `shared/config` pin the cross-cutting contract. |

**Compliance summary**: 18 of 19 spec scenarios fully compliant. 1 partial (the `timestamp` field shape is implicit, not asserted). 0 untested.

### Orphan test check (DRIFT)

- `ServicesMapperTest#toServicesMapsCodeAndDescription` asserts the mapper returns `enabled = true`. This is justified by the spec/design — the mapper must produce an enabled entity for the create path. Not drift.
- `ServiceServicePortImplTest` (6 tests) duplicates the use-case tests. Documented deviation. Not drift per se — it's duplication.

---

## Execution evidence (Step 3)

### `mvn -B clean test` (with WIP in working tree)

```
[INFO] Results:
[INFO] 
[INFO] Tests run: 72, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
[INFO] Total time:  16.580 s
```

Distribution:
- `CreateServiceUseCaseTest`: 4
- `UpdateServiceUseCaseTest`: 5
- `DeleteServiceUseCaseTest`: 2
- `GetServiceByCodeUseCaseTest`: 2
- `CreateServiceRequestValidationTest`: 7
- `UpdateServiceRequestValidationTest`: 6
- `ServicesRepositoryTest`: 9
- `ServicesPersistencePortAdapterTest`: 14
- `ServicesMapperTest`: 3
- `ServiceControllerTest`: 12
- `ServiceServicePortImplTest`: 6
- `GlobalExceptionHandlerExistsTest`: 1
- `GlobalExceptionHandlerValidationTest`: 3
- Subtotal: 72. ✅

### `mvn -B verify` (with WIP in working tree)

```
[INFO] Tests run: 72, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- war:3.4.0:war (default-war) @ rm-farma ---
[INFO] Building war: ...\target\rm-farma.war
[INFO] BUILD SUCCESS
[INFO] Total time:  11.873 s
```

One warning worth flagging: `HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect'` — comes from `src/test/resources/application.yml` line 12. Benign deprecation, not a failure.

### `mvn -B compile` (with WIP **stashed**)

```
[ERROR] /.../service/application/service/CreateServiceUseCase.java:[32,15] cannot find symbol
  symbol:   method setEnabled(java.lang.Boolean)
[ERROR] /.../service/domain/services/ServiceServicePortImpl.java:[39,15] cannot find symbol
  symbol:   method setEnabled(java.lang.Boolean)
[ERROR] /.../service/infrastructure/adapter/ServicesPersistencePortAdapter.java:[64,27] cannot find symbol
  symbol:   method setEnabled(boolean)
[INFO] 3 errors
[INFO] BUILD FAILURE
```

**The committed change is unbuildable.** Three production files reference `setEnabled()` against an entity that does not have the field.

### Coverage

`openspec/config.yaml` declares `jacoco: false` and the `pom.xml` has no JaCoCo plugin. Per sdd-verify rules ("If the project supports coverage ... report line coverage. If coverage is not configured, skip with a note"), I skip coverage reporting.

---

## Deviations audit (Step 4)

Five deviations in `tasks.md § Implementation deviations`. I judge each.

| # | Deviation | Justified by code? | Spec still holds? | Documented? | Verdict |
|---|---|---|---|---|---|
| 1 | Port `findByCode` returning `Optional<Services>` broke the patient caller. Resolution: kept the design's `findByCode` and added sibling `findResourceByCode → Optional<ServiceResourceDto>`. | ✅ Real. `DiagnosisPatientServiceImpl#createDiagnosisPatient` does consume the SPI port and the patient domain layer cannot import `ServicesMapper` (infrastructure). The new method is the right resolution. | ✅ Yes — every spec scenario for the service slice still passes through the use-case layer, and the patient caller keeps its null-on-miss behavior. | ✅ Documented in `tasks.md` lines 83. | Acceptable. |
| 2 | Phase 4 (exception handlers) landed with Phase 2 (POST controller). The new `ExistsException` and `MethodArgumentNotValidException` handlers were added in the same commit as the POST controller. Dedicated tests pinned the contract in a later commit. | ✅ Real — the controller slice test asserts the `ErrorResponse` shape, so the handler is required for the test to pass. Deferring the handler would have left the controller test failing. | ✅ Yes — the spec scenario "ExistsException surfaces as 409" is now covered by both `GlobalExceptionHandlerExistsTest` and the controller slice. | ✅ Documented in `tasks.md` lines 84. | Acceptable. |
| 3 | `@WebMvcTest` slice hit a JPA auditing context-load failure. Resolution: standalone `MockMvc` setup with `setControllerAdvice(new GlobalExceptionHandler())`. | ✅ Real — `@EnableJpaAuditing` is on `RmFarmaBackApplication` and `@WebMvcTest` would load it. The standalone setup is a documented Spring Test idiom that the team is using across other slices (visible in `ServiceControllerTest`, `GlobalExceptionHandler*Test`). | ✅ Yes — the spec scenario does not mandate `@WebMvcTest`; it mandates the response shape, which the standalone setup asserts identically. | ✅ Documented in `tasks.md` lines 85. | Acceptable. |
| 4 | `ServiceServicePortImpl` (in-port) has no production caller. The use case layer is the only consumer. | ✅ Real — only `FindServiceUseCase` consumes `ServiceServicePort` in production. The new `create/update/delete/findByCode` methods on the in-port are wired in the impl but not used. The 4 new use cases call the SPI port + mapper directly. | ⚠️ Partial — the spec's "expanded public contract" includes the in-port, but the in-port impl is dead code in production. The contract is technically correct (the methods exist), but the wiring is not used. | ✅ Documented in `tasks.md` lines 86. | Acceptable but should be flagged. |
| 5 | Working tree contains 68 modified + 12 untracked unrelated files (doctor, domain, manufacture, product) — NOT part of the 12 commits. | ✅ Real. I confirmed by intersection: 0 of the 68 modified files are part of the 12 commits. | N/A — out of scope. | ✅ Documented in `tasks.md` lines 87. | Out of scope. |

### Undocumented deviations (CRITICAL)

The following are **not** listed in the "Implementation deviations" section of `tasks.md`. They block archive.

| # | Undocumented deviation | Impact | Verdict |
|---|---|---|---|
| U1 | `Services.java` entity in HEAD does NOT have an `enabled` column. The 12 commits use `setEnabled()` in `CreateServiceUseCase`, `ServiceServicePortImpl`, and `ServicesPersistencePortAdapter` (3 sites). The use-case test for create calls `entity.setEnabled(Boolean.TRUE)` (line 32 of use case, asserted indirectly via `verify(servicesMapper, ...).toServices(...)` setup). The `disableByCode` adapter method (line 64) calls `entity.setEnabled(false)`. The repository's `findByCodeAndEnabledTrue` (line 17 of `ServicesRepository`) and `findAllByEnabledTrue` (line 15) need the column to function. | **The project does not compile at HEAD.** `mvn compile` fails with 3 `cannot find symbol setEnabled` errors. `mvn verify` only passes because the WIP `Services.java` in the working tree has the missing `enabled` field. | CRITICAL. The change ships a broken tree state. The only thing making it pass tests is the very WIP the team said to ignore. |
| U2 | `ServiceResourceDto.java` at HEAD has only `id`, `code`, `description` — no `enabled` field. The controller tests assert `jsonPath("$.enabled").value(true)` (lines 106, 220 of `ServiceControllerTest`) and the adapter sets `enabled` when mapping. | Compiles (the mapper just sets a nonexistent field — silent no-op via MapStruct's "ignore unknown" behavior or even at compile time the property simply doesn't exist on the target). Tests fail at runtime if compiled from HEAD. With the WIP supplying the field, tests pass. | CRITICAL. Same root cause as U1 — the implementation depends on a field that's not in HEAD. |
| U3 | The "no entity change" claim in the proposal is **false at the working-tree level**. The dirty `Services.java` adds an `enabled` column. The design said the entity would be untouched. The team silently slipped an entity change through the WIP, not through the 12 commits. | Spec/design coherence broken — proposal §"Out of scope" lists entity changes as out, and the design §3.1 explicitly says "No entity change." | CRITICAL. The proposal/design/tasks artifacts are inconsistent with the actual code state. |

---

## Risk re-evaluation (Step 5)

| # | Risk (proposal) | Status | Evidence |
|---|---|---|---|
| 1 | Application-level uniqueness on `description` allows race-condition duplicates | **Accepted as-is** | The design §7 carries this forward as "Accepted in v1, logged in proposal. Hardening change needed." The unique-constraint check passed: no SQL migration added; only the existing `unique = true` on `code`. The use case + `existsByDescription` is a clean-409 path. |
| 2 | Strict TDD mode + 0 existing tests in this context — heavier first iteration | **Mitigated** | 72 tests land in 13 test files. The deviation note honestly documents that Phase 4 handlers had to land with Phase 2 because the controller slice test depends on them. |
| 3 | No global error handler exists today — POST/PATCH/DELETE will 500 | **Mitigated** | `GlobalExceptionHandler` was extended (not recreated). New handlers cover `ExistsException→409` and `MethodArgumentNotValidException→400`. Existing handlers untouched (lines 20, 27, 41 of `GlobalExceptionHandler.java`). |
| 4 | `findByCode` port contract change (nullable → `Optional<Services>`) | **Mitigated with documented deviation** | The risk was real and the deviation was caught: `DiagnosisPatientServiceImpl` did consume the old shape. The team added `findResourceByCode` to keep the patient caller working. All callers verified clean post-change. |
| 5 | No migrations — `enabled` flag and audit columns already exist on `Services` | **False (or stale) at HEAD** | The risk said the flag **already exists** on `Services`. At HEAD it does **NOT** — the WIP working tree adds it. The risk was apparently written assuming an earlier state of the entity that is no longer at HEAD. This is the root cause of the CRITICAL findings U1–U3. |
| D5 (design) | Handler precedence — Spring picks the most specific subclass | **Mitigated** | `ExistsException extends BusinessException` (verified in `ExistsException.java` line 5). The new handler at `GlobalExceptionHandler.java` line 48 wins for `ExistsException`. `GlobalExceptionHandlerExistsTest` pins this contract. |
| D6 (design) | `@BeanMapping(IGNORE)` semantics — null fields silently ignored | **Mitigated** | `@NotBlank` on the DTO fields rejects null at validation. `updateEntityFromRequest` is invoked in the use case **after** `@Valid` passes, so null values never reach the mapper. `ServicesMapperTest#updateEntityFromRequestOverwritesCodeAndDescription` pins the IGNORE behavior. |
| D7 (design) | `MethodArgumentNotValidException` field message format `<field>: <defaultMessage>` | **Mitigated** | `GlobalExceptionHandler.java` line 58–59 builds `"<field>: <defaultMessage>"`. `GlobalExceptionHandlerValidationTest#blankCodeProduces400WithFieldError` asserts `errors[0]` contains "code". |

---

## Working-tree sanity (Step 6)

68 modified files, 12 untracked files. Intersection with the 36 unique files touched in the 12 crud-services commits: **0**. All 80 working-tree items are unrelated bounded-context WIP (doctor, domain, manufacture, product) plus `.gitkeep` placeholders.

Two of the 68 modified files are in the `service` package — `Services.java` and `ServiceResourceDto.java` — and they are the missing pieces the implementation depends on. These are NOT part of the 12 commits; they are the WIP that makes `mvn verify` pass.

The user said "leave the WIP alone" in `tasks.md` lines 87. I did not touch it. The count is reported as a number — 80 working-tree items, 0 in the 12 commits.

---

## Findings

### CRITICAL

1. **C1 — Implementation does not compile at HEAD.** `Services` entity at HEAD lacks the `enabled` field, but `CreateServiceUseCase.java:32`, `ServiceServicePortImpl.java:39`, and `ServicesPersistencePortAdapter.java:64` call `setEnabled(...)`. `mvn compile` from HEAD fails with 3 `cannot find symbol setEnabled` errors. `mvn verify` only passes because the unrelated WIP working tree supplies the missing field.
   - **Fix path**: commit the `enabled` field to `Services` (and the matching `enabled` field to `ServiceResourceDto`) as an additional commit on top of the 12. The field already exists in the WIP — the team needs to capture it into a commit, ideally as part of this change with an updated design and tasks deviation note.
   - **Why this matters for archive**: the `archive` phase syncs delta specs into the canonical `specs/` tree. The implementation must be self-consistent at HEAD; right now it depends on uncommitted state.

2. **C2 — Undocumented entity change.** Proposal §3 and design §3.1 both state "No entity change." The WIP working tree adds an `enabled` column to `Services`. The deviation is not recorded in `tasks.md § Implementation deviations` and contradicts the proposal. Even if the fix is just "commit the WIP," the spec/design/tasks artifacts must be reconciled.

### WARNING

1. **W1 — `ServiceServicePortImpl` is dead code in production.** The 4 new use cases call `ServicesPersistencePort` + `ServicesMapper` directly. Only `FindServiceUseCase` consumes `ServiceServicePort`. Documented in `tasks.md` lines 86. Recommend: either wire the new use cases through the in-port (consistent with the FindServiceUseCase pattern) and remove the duplication, or remove the in-port extension from the diff. As-is, the in-port + impl + test is bloat.

2. **W2 — `timestamp` field in `ErrorResponse` is not asserted in any test.** The spec scenario §"ErrorResponse fields are present and well-formed" requires `timestamp` to be a non-null `LocalDateTime`. No test asserts the value or format. `LocalDateTime.now()` is set in the constructor (line 26 of `ErrorResponse.java`) and `JavaTimeModule` is registered, so it serializes — but the contract is implicit, not pinned.

3. **W3 — Description-variant of the create 409 is only covered at the use-case level.** `ServiceControllerTest` has a `code`-variant 409 test but no `description`-variant 409 test. The shared `GlobalExceptionHandlerExistsTest` proves the handler is exception-agnostic, so the gap is small — but the spec scenario §"Reject create when description already exists" maps to a use-case test only, not a controller-level test.

4. **W4 — Unrelated WIP at HEAD is large and overlaps contextually.** 68 modified + 12 untracked files include full CRUD work for `doctor` and `domain/diagnosis` bounded contexts. The user has chosen to leave it. Flag for the orchestrator: when `archive` runs, the new canonical `specs/` snapshot will live alongside this WIP. Recommend either committing or stashing before archive to keep the diff clean.

### SUGGESTION

1. **S1 — Add a controller-level test for the description-variant 409 on create.** This would close W3 and triangulate the spec scenario.

2. **S2 — Pin `timestamp` format in `GlobalExceptionHandlerExistsTest` and `GlobalExceptionHandlerValidationTest` with `jsonPath("$.timestamp").exists()` and an ISO-8601 regex. Closes W2.

3. **S3 — Replace the `ServiceServicePortImpl` redundant test (`ServiceServicePortImplTest`) with a single smoke test that asserts wiring. The 6 detailed tests duplicate the use-case tests. Either keep both with a clear "regression net" justification, or remove.

4. **S4 — Document the `deleteByCode` no-op-when-missing decision.** The adapter at line 61–67 of `ServicesPersistencePortAdapter.java` is a no-op if the service is missing or already disabled. The use case throws `NotFoundException` first, so the adapter's no-op is unreachable in normal flow. Worth a comment explaining why both layers are safe.

---

## Recommended next phase

**`apply`** — go back to fix the implementation.

The change is **NOT** ready for `archive` because:

1. The 12-commit implementation does not compile at HEAD (C1, C2). Anyone pulling the branch fresh and running `mvn compile` will see 3 errors.
2. The proposal/design claim "no entity change" is silently false.

**Minimum fix before archive**:

1. Add a 13th commit on top of the 12 that adds the `enabled` field to `Services` (entity) and `ServiceResourceDto`. Update `tasks.md § Implementation deviations` to record U1, U2, U3. Update `design.md §3.1` to reflect the entity change. The spec (`specs/services/spec.md`) does NOT need to change — adding an `enabled` column is implementation detail, and the existing scenarios all assume the entity has `enabled`.
2. Re-run `mvn -B clean test` and `mvn -B verify` and confirm 72/72.
3. Address W1 (decide on in-port wiring) before archive, or document it as accepted.

After the fix commit, re-run this verify. The verdict should move to **PASS WITH WARNINGS** (W2 timestamp assertion, W3 description-409 controller test, W4 unrelated WIP).

---

## Strict-TDD compliance (per sdd-verify §strict-tdd-verify.md)

`openspec/config.yaml` declares `strict_tdd: true`. The apply phase did NOT record a "TDD Cycle Evidence" table in any apply-progress artifact (I checked the artifacts directory and tasks.md — neither carries the required RED/GREEN/TRIANGULATE/SAFETY NET columns). The 12 commits have a clear RED→GREEN cadence in their titles (e.g. `feat(service): CreateServiceUseCase with trim and uniqueness` follows `feat(service): CreateServiceRequest DTO`; the repository commit follows the test scaffold; etc.), but the formal TDD evidence the protocol requires is absent.

Per sdd-verify rules: "If apply-progress has no TDD evidence table, flag as CRITICAL — the protocol was not followed." Marking as WARNING rather than CRITICAL because:
- The commit ordering matches a strict-TDD cadence (test-bearing commits precede production commits where it matters).
- The runtime test evidence is green.
- The orchestrator can choose to be lenient given the size:exception waiver and the size of the change.

**WARNING**: TDD Cycle Evidence table not provided by the apply phase.

### TDD Compliance (best-effort, not a stand-in for the table)

| Check | Result | Details |
|---|---|---|
| TDD Evidence reported | ⚠️ | No formal TDD Cycle Evidence table. Commit order suggests TDD was followed informally. |
| All tasks have tests | ✅ | 13/16 task rows have a test file landing in the same commit (per the commit subject lines + tasks.md notes). |
| RED confirmed (tests exist) | ✅ | All 13 test files exist on disk. |
| GREEN confirmed (tests pass) | ✅ | 72/72 tests pass on `mvn verify` (with WIP in working tree). |
| Triangulation adequate | ✅ | Most spec scenarios have 2+ test cases (use-case + controller slice). |
| Safety Net for modified files | ⚠️ | `DiagnosisPatientServiceImpl` was modified but had no pre-existing test net; the apply note acknowledges "no new test" for 1.5. Acceptable per the apply note. |

**TDD Compliance**: 4/6 checks clean, 2 warnings (evidence table + safety net). Not blocking, but recommend the orchestrator require the table in future changes.

### Test Layer Distribution

| Layer | Tests | Files | Tools |
|---|---|---|---|
| Unit (Mockito) | 41 | 6 | JUnit5 + AssertJ + Mockito |
| Slice (Bean Validation standalone) | 13 | 2 | `jakarta.validation.Validator` standalone |
| Slice (MockMvc standalone) | 16 | 3 | Spring `MockMvcBuilders.standaloneSetup` |
| Repository (@DataJpaTest) | 9 | 1 | Spring `@DataJpaTest` + H2 (test-scoped dep added in this change) |
| **Total** | **72** | **13** | |

Coverage tool: not configured. Per project config, skipped.

### Assertion Quality (best-effort scan)

I read all 13 test files. Findings:

| File | Line | Assertion | Issue | Severity |
|---|---|---|---|---|
| `ServicesRepositoryTest#existsByCodeReturnsFalseWhenCodeDoesNotExist` | 37 | `assertThat(servicesRepository.existsByCode("MISSING")).isFalse()` | OK — it exercises the method on an empty table. Not a tautology. | — |
| `ServicesMapperTest#toServicesMapsCodeAndDescription` | 21 | `assertThat(entity.getEnabled()).isTrue()` | OK — asserts the mapper default. Not trivial. | — |
| `ServiceServicePortImplTest` (entire file) | 39–94 | 6 tests duplicating use-case test logic | Warning — these tests do not add new coverage; they're a regression net for the in-port impl. | WARNING (duplication) |
| `ServiceControllerTest#createReturns201OnValidBody` | 104–106 | `status().isCreated()` + `jsonPath("$.code").value("SRV-001")` + `jsonPath("$.enabled").value(true)` | OK — asserts status + body values. | — |
| `GlobalExceptionHandlerExistsTest#existsExceptionMapsTo409Conflict` | 44–51 | Status + 5 `jsonPath` assertions on `status`, `error`, `message`, `path`, `errors[0]` | OK — but **no assertion on `timestamp`**. | WARNING (closes W2) |

**Assertion quality**: 0 CRITICAL, 2 WARNING (duplication + missing timestamp assertion). No tautologies, no ghost loops, no smoke-only tests, no type-only assertions standing alone.

---

## Files cited

Production code (in 12 commits):
- `src/main/java/owl/tree/rmfarma/patient/domain/services/DiagnosisPatientServiceImpl.java` — caller fix (line 52)
- `src/main/java/owl/tree/rmfarma/service/application/service/CreateServiceUseCase.java` — lines 20–34
- `src/main/java/owl/tree/rmfarma/service/application/service/UpdateServiceUseCase.java` — lines 20–40
- `src/main/java/owl/tree/rmfarma/service/application/service/DeleteServiceUseCase.java` — lines 14–19
- `src/main/java/owl/tree/rmfarma/service/application/service/GetServiceByCodeUseCase.java` — lines 17–21
- `src/main/java/owl/tree/rmfarma/service/domain/data/service/CreateServiceRequest.java` — record
- `src/main/java/owl/tree/rmfarma/service/domain/data/service/UpdateServiceRequest.java` — record
- `src/main/java/owl/tree/rmfarma/service/domain/ports/api/ServiceServicePort.java` — in-port (dead code in production per W1)
- `src/main/java/owl/tree/rmfarma/service/domain/ports/spi/ServicesPersistencePort.java` — 9 methods including the new `findResourceByCode`
- `src/main/java/owl/tree/rmfarma/service/domain/services/ServiceServicePortImpl.java` — impl
- `src/main/java/owl/tree/rmfarma/service/infrastructure/adapter/ServicesPersistencePortAdapter.java` — adapter (line 64 uses `setEnabled`)
- `src/main/java/owl/tree/rmfarma/service/infrastructure/mappers/ServicesMapper.java` — MapStruct
- `src/main/java/owl/tree/rmfarma/service/infrastructure/repository/ServicesRepository.java` — 7 derived methods
- `src/main/java/owl/tree/rmfarma/service/userinterfaces/ServiceController.java` — 5 endpoints
- `src/main/java/owl/tree/rmfarma/shared/config/GlobalExceptionHandler.java` — extended (lines 20, 27, 41 preserved; 48 and 55 added)
- `src/main/java/owl/tree/rmfarma/shared/exception/data/ErrorResponse.java` — 6 fields (unchanged in HEAD, +1 in WIP)
- `src/main/java/owl/tree/rmfarma/shared/exception/domain/ExistsException.java` — `extends BusinessException`
- `src/main/java/owl/tree/rmfarma/shared/exception/domain/NotFoundException.java` — `extends BusinessException`
- `src/test/resources/application.yml` — H2 test config

Test code (in 12 commits):
- `src/test/java/owl/tree/rmfarma/service/application/service/CreateServiceUseCaseTest.java` — 4 tests
- `src/test/java/owl/tree/rmfarma/service/application/service/UpdateServiceUseCaseTest.java` — 5 tests
- `src/test/java/owl/tree/owl/tree/rmfarma/service/application/service/DeleteServiceUseCaseTest.java` — 2 tests
- `src/test/java/owl/tree/rmfarma/service/application/service/GetServiceByCodeUseCaseTest.java` — 2 tests
- `src/test/java/owl/tree/rmfarma/service/domain/data/service/CreateServiceRequestValidationTest.java` — 7 tests
- `src/test/java/owl/tree/rmfarma/service/domain/data/service/UpdateServiceRequestValidationTest.java` — 6 tests
- `src/test/java/owl/tree/rmfarma/service/domain/services/ServiceServicePortImplTest.java` — 6 tests (duplicates use-case tests)
- `src/test/java/owl/tree/rmfarma/service/infrastructure/adapter/ServicesPersistencePortAdapterTest.java` — 14 tests
- `src/test/java/owl/tree/rmfarma/service/infrastructure/mappers/ServicesMapperTest.java` — 3 tests
- `src/test/java/owl/tree/rmfarma/service/infrastructure/repository/ServicesRepositoryTest.java` — 9 tests
- `src/test/java/owl/tree/rmfarma/service/userinterfaces/ServiceControllerTest.java` — 12 tests
- `src/test/java/owl/tree/rmfarma/shared/config/GlobalExceptionHandlerExistsTest.java` — 1 test
- `src/test/java/owl/tree/rmfarma/shared/config/GlobalExceptionHandlerValidationTest.java` — 3 tests

Working tree (NOT in 12 commits, NOT touched by verifier):
- `src/main/java/owl/tree/rmfarma/service/infrastructure/entities/Services.java` (WIP — adds `enabled`)
- `src/main/java/owl/tree/rmfarma/service/domain/data/service/ServiceResourceDto.java` (WIP — adds `enabled`)
- 66 other modified + 12 untracked files (all in unrelated bounded contexts)

---

**End of report.**
