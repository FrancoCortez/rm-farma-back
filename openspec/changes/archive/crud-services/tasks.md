# Tasks: crud-services

## Review Workload Forecast

| Field | Value |
|-------|-------|
| Estimated changed lines | ~1000 (250 prod + 750 test) |
| 400-line budget risk | High |
| Chained PRs recommended | Yes |
| Split | PR 1 (foundation + create) → PR 2 (update + delete + get-by-code) → PR 3 (error handler) |
| Delivery strategy | ask-on-risk |
| Chain strategy | pending |

Decision needed before apply: Yes
Chained PRs recommended: Yes
Chain strategy: pending
400-line budget risk: High

> **Resolved by orchestrator**: `size:exception` (single PR). This change lands in
> one commit chain on `master` because the maintainer approved the size exception.

### Work Units

| # | Goal | PR |
|--|------|----|
| 1 | Port-contract gate + repo + adapter (Optional entity, new methods) + caller fix | PR 1 |
| 2 | CreateServiceUseCase + CreateServiceRequest + `POST` | PR 1 |
| 3 | UpdateServiceUseCase + UpdateServiceRequest + mapper update + `PATCH` | PR 2 |
| 4 | DeleteServiceUseCase + `DELETE` + GetServiceByCodeUseCase + `GET /{code}` + port wiring | PR 2 |
| 5 | Extend `GlobalExceptionHandler` (ExistsException→409, MethodArgumentNotValidException→400) | PR 3 |

## Phase 1: Foundation (PR 1)

- [x] **1.1 Gate the `findByCode` contract change** (read-only). `git grep -nE "ServicesPersistencePort.*findByCode|servicesPersistencePort\.findByCode"`; confirm single call site `DiagnosisPatientServiceImpl#createDiagnosisPatient` (line 52). Commit: `docs(services): gate findByCode return-type change`.

- [x] **1.2 Repository: 5 derived methods + `@DataJpaTest`** (RED→GREEN). RED: `service/infrastructure/repository/ServicesRepositoryTest.java` (`@DataJpaTest` + `@AutoConfigureTestDatabase`); one test per derived method, assert `existsByCodeAndIdNot` self-exclusion. Add `com.h2database:h2` test-scoped to `pom.xml`. GREEN: extend `ServicesRepository` with `existsByCode`, `existsByDescription`, `findByDescriptionAndEnabledTrue`, `existsByDescriptionAndIdNot`, `existsByCodeAndIdNot`. Verify: `mvn -B test -Dtest=ServicesRepositoryTest`. Commit: `feat(service): uniqueness and lookup derived queries`.

- [x] **1.3 Out port: extend `ServicesPersistencePort`** (RED→GREEN). RED: stub `ServicesPersistencePortTest` compiling against new signatures. GREEN: replace `ServiceResourceDto findByCode(String)` with `Optional<Services> findByCode(String)`; add `Services save(Services)`, `Optional<Services> findEnabledByCode(String)`, `void disableByCode(String)`. Verify: `mvn -B compile`. Commit: `refactor(service): persistence port returns Optional<entity>`.

- [x] **1.4 Adapter: implement new port methods** (RED→GREEN). RED: `service/infrastructure/adapter/ServicesPersistencePortAdapterTest.java` (`@Mock ServicesRepository` + `@Mock ServicesMapper`); assert `findByCode` returns `Optional` (empty on miss, mapped on hit), `disableByCode` saves with `enabled=false`, `save` delegates. GREEN: rewrite `ServicesPersistencePortAdapter`. Verify: `mvn -B test -Dtest=ServicesPersistencePortAdapterTest`. Commit: `feat(service): persistence adapter for create/update/disable/findByCode`.

- [x] **1.5 Fix caller `DiagnosisPatientServiceImpl`** (no new test). Update line 52 of `patient/domain/services/DiagnosisPatientServiceImpl.java` to consume `Optional<Services>` and map locally. Verify: `mvn -B compile`. Commit: `fix(patient): adapt DiagnosisPatientServiceImpl to Optional<entity>`. (Note: implementation also required adding `findResourceByCode` to the port for the patient caller to consume a DTO, since the patient service cannot import the infrastructure mapper; the port contract change is therefore `findByCode` returns `Optional<entity>` and `findResourceByCode` returns `Optional<DTO>`.)

## Phase 2: Create + Update slices (PR 1 end → PR 2)

- [x] **2.1 CreateServiceRequest DTO** (RED→GREEN). RED: `service/domain/data/service/CreateServiceRequestValidationTest.java` using Jakarta `Validator`; assert `@NotBlank` + `@Size` violations. GREEN: create `CreateServiceRequest.java` (record, `@NotBlank @Size(max=30) String code`, `@NotBlank @Size(max=100) String description`). Verify: `mvn -B test -Dtest=CreateServiceRequestValidationTest`. Commit: `feat(service): CreateServiceRequest DTO`.

- [x] **2.2 CreateServiceUseCase (unit)** (RED→GREEN). RED: `service/application/service/CreateServiceUseCaseTest.java` (`@ExtendWith(MockitoExtension.class)`, `@Mock ServiceServicePort` + `@Mock ServicesMapper`); cover trim before `existsByCode`/`existsByDescription`, `ExistsException("code"|"description",…)`, happy path. GREEN: create `CreateServiceUseCase.java` (`@Component`). Verify: `mvn -B test -Dtest=CreateServiceUseCaseTest`. Commit: `feat(service): CreateServiceUseCase with trim and uniqueness`. (Note: use case depends on `ServicesPersistencePort` + `ServicesMapper` per design 3.4; the test mocks the SPI port and the mapper.)

- [x] **2.3 POST endpoint + controller slice test** (RED→GREEN). RED: `service/userinterfaces/ServiceControllerTest.java` (standalone `MockMvc` with `GlobalExceptionHandler` as `@ControllerAdvice`); POST cases: happy → 201, blank code → 400, oversize desc → 400, duplicate code → 409. GREEN: add `@PostMapping` with `@Valid @RequestBody CreateServiceRequest` to `ServiceController.java`. Verify: `mvn -B test -Dtest=ServiceControllerTest`. Commit: `feat(service): POST /api/v1/services`. (Note: the `ExistsException→409` and `MethodArgumentNotValidException→400` handlers in `GlobalExceptionHandler` were added in the same commit because the controller slice test depends on them to assert the documented `ErrorResponse` shape. They are formally scoped to PR 3 in the work units table, but they cannot be deferred past this commit.)

- [x] **2.4 UpdateServiceRequest DTO + mapper update** (RED→GREEN). RED: `UpdateServiceRequestValidationTest` + `service/infrastructure/mappers/ServicesMapperTest.java` (`Mappers.getMapper(...)`); assert IGNORE strategy overwrites only non-null, leaves `enabled` untouched. GREEN: create `UpdateServiceRequest`; extend `ServicesMapper` with `toServices(CreateServiceRequest)` + `@BeanMapping(IGNORE) void updateEntityFromRequest(UpdateServiceRequest, @MappingTarget Services)`. Verify: `mvn -B test '-Dtest=UpdateServiceRequestValidationTest,ServicesMapperTest'`. Commit: `feat(service): UpdateServiceRequest DTO and mapper update`.

- [x] **2.5 UpdateServiceUseCase + PATCH endpoint** (RED→GREEN). RED: (a) `UpdateServiceUseCaseTest` — same-row self-exclusion, cross-row `ExistsException`, `NotFoundException` on empty `findByCode`, `port.save(current)` once with trimmed values. (b) Extend `ServiceControllerTest`: PATCH happy → 200, cross-row duplicate → 409, blank code → 400. GREEN: create `UpdateServiceUseCase`; add `@PatchMapping("/{code}")`. Verify: `mvn -B test '-Dtest=UpdateServiceUseCaseTest,ServiceControllerTest'`. Commit: `feat(service): PATCH /api/v1/services/{code}`.

## Phase 3: Delete + Get-by-code slice (PR 2)

- [x] **3.1 DeleteServiceUseCase + DELETE endpoint** (RED→GREEN). RED: (a) `DeleteServiceUseCaseTest` — empty `findEnabledByCode` → `NotFoundException`; present → `port.disableByCode` once. (b) Extend `ServiceControllerTest`: DELETE happy → 204, not found → 404. GREEN: create `DeleteServiceUseCase`; add `@DeleteMapping("/{code}")`. Verify: `mvn -B test '-Dtest=DeleteServiceUseCaseTest,ServiceControllerTest'`. Commit: `feat(service): soft DELETE /api/v1/services/{code}` (combined with 3.2 in the same commit `feat(service): DELETE/GET endpoints and in-port delegation`).

- [x] **3.2 GetServiceByCodeUseCase + GET by code** (RED→GREEN). RED: (a) `GetServiceByCodeUseCaseTest` — present → mapped DTO; empty → `NotFoundException`. (b) Extend `ServiceControllerTest`: GET happy → 200, not found → 404. GREEN: create `GetServiceByCodeUseCase`; add `@GetMapping("/{code}")`. Verify: `mvn -B test '-Dtest=GetServiceByCodeUseCaseTest,ServiceControllerTest'`. Commit: `feat(service): GET /api/v1/services/{code}` (combined with 3.1 and 3.3 in the same commit).

- [x] **3.3 In-port `ServiceServicePort` + impl wiring** (RED→GREEN). RED: `ServiceServicePortImplTest` with `@Mock ServicesPersistencePort` asserting delegation. GREEN: extend `ServiceServicePort` with `create`, `update`, `deleteByCode`, `findByCode`; extend `ServiceServicePortImpl` to delegate. Verify: `mvn -B test -Dtest=ServiceServicePortImplTest`. Commit: `feat(service): in-port and impl expose create/update/delete/findByCode` (combined with 3.1 and 3.2).

## Phase 4: Cross-cutting error handler (PR 3)

- [x] **4.1 GlobalExceptionHandler: `ExistsException` → 409** (RED→GREEN). RED: `shared/config/GlobalExceptionHandlerExistsTest.java` (standalone `MockMvc` on a tiny throwing controller); assert 409, `error == "Conflict"`, `message == "Resource already exists"`, `errors` non-null with `[ex.getMessage()]`, `path` contains URI. GREEN: add `@ExceptionHandler(ExistsException.class)` to `shared/config/GlobalExceptionHandler.java`. Do **not** modify existing handlers. Verify: `mvn -B test -Dtest=GlobalExceptionHandlerExistsTest`. Commit: `feat(shared): map ExistsException to 409` (the handler was added in the same commit as the POST controller; this test pins the contract).

- [x] **4.2 GlobalExceptionHandler: `MethodArgumentNotValidException` → 400** (RED→GREEN). RED: `GlobalExceptionHandlerValidationTest` (standalone `MockMvc` on `@PostMapping @Valid` test controller with `@NotBlank` + `@Size(max=10)`); send blank + oversize; assert 400, `error == "Bad Request"`, `message == "Validation failed"`, `errors` has one `"<field>: <msg>"` per violation. GREEN: add `@ExceptionHandler(MethodArgumentNotValidException.class)`; copy `getFieldErrors()` into `errors`. Verify: `mvn -B test -Dtest=GlobalExceptionHandlerValidationTest`. Commit: `feat(shared): map MethodArgumentNotValidException to 400` (the handler was added in the same commit as the POST controller; this test pins the contract).

## Phase 5: Integration verification

- [x] **5.1 Full `mvn -B verify`**. Run `mvn -B verify`; assert zero failures; leave `RmFarmaBackApplicationTests` uncommented (no — left commented per `openspec/config.yaml` notes, the new code is compatible with the commented state). Verify: `mvn -B verify` (72 tests run, 0 failures, 0 errors, 0 skipped, BUILD SUCCESS). Commit: chore commit `chore(services): green mvn verify with all CRUD tests`.

## Apply notes

- Strict TDD: never land GREEN before its RED compiles and fails for the right reason.
- `findByCode` is consumed only by `DiagnosisPatientServiceImpl#createDiagnosisPatient`; 1.1 verifies before any production change.
- Handler precedence: `ExistsException extends BusinessException`; the new handler wins. Do not delete existing handlers.
- H2 dep added in 1.2 is test-scoped. If the team prefers Testcontainers MySQL, flag to orchestrator.

## Implementation deviations

- **Port `findByCode` returning `Optional<Services>` broke the patient caller.** The design claim "only the unused port is affected" was wrong — `DiagnosisPatientServiceImpl#createDiagnosisPatient` consumes `servicesPersistencePort.findByCode(...)` and expects a `ServiceResourceDto` DTO (cannot import the infrastructure mapper from the domain service layer). Resolution: kept the design's `findByCode` returning `Optional<Services>` and added a sibling `findResourceByCode(String) → Optional<ServiceResourceDto>` for the patient caller. The patient call site uses `findResourceByCode` and preserves the original `null`-on-miss behavior.
- **Phase 4 handlers landed with Phase 2.** The `ExistsException→409` and `MethodArgumentNotValidException→400` handlers in `GlobalExceptionHandler` were added in the same commit as the POST controller (Task 2.3) because the controller slice test depends on them. They are scoped to PR 3 in the work-units table, but cannot be deferred past the controller tests that assert the documented `ErrorResponse` shape. Dedicated tests for the handlers were added in a later commit (`test(shared): pin … contracts`) to keep the spec scenarios in `shared-error-handling` locked in.
- **`@WebMvcTest` slice hit the JPA auditing context-load failure** because `@EnableJpaAuditing` is on `RmFarmaBackApplication`. Resolution: switched the controller test to a standalone `MockMvc` setup (`MockMvcBuilders.standaloneSetup(...).setControllerAdvice(new GlobalExceptionHandler())`). The design 6.2's `@WebMvcTest` recipe would have failed in this codebase.
- **No `ServiceServicePortImpl` (in-port) callers exist** in production. The in-port + impl were added per the design 3.3, but only the use case layer is consumed. The test for the impl duplicates the use case tests; both pass.
- **74 lines of unrelated WIP files in the working tree** (doctor, domain, manufacture, product) were NOT committed — they are out of scope for `crud-services` and should be reviewed separately.
- **Identifier corrected from `code` to `id` after apply (2026-06-09).** The proposal and design initially specified paths and use-case inputs as `code` (the business key). The front-end team confirmed they send UUIDs in CRUD operations, so write paths and the `GET /{id}` endpoint now use the entity's UUID `id` field. Renamed in this delta:
  - `ServicesPersistencePort`: `findByCode` → `findById`, `findEnabledByCode` → `findEnabledById`, `disableByCode` → `disableById`, `findResourceByCode` → `findResourceById`.
  - `ServiceServicePort`: `findByCode` → `findById`, `update(String code, …)` → `update(String id, …)`, `deleteByCode` → `deleteById`.
  - Use cases: `GetServiceByCodeUseCase` → `GetServiceByIdUseCase`.
  - Controller paths: `/{code}` → `/{id}` (PATCH, DELETE, GET).
  - All affected tests renamed and updated to pass `id` values.
  - The patient caller `DiagnosisPatientServiceImpl#createDiagnosisPatient` is updated to use `findResourceById` with the same null-on-miss behavior.
  - `JpaRepository<Services, String>#findById(String)` is the primary lookup; no new repository method needed for the id-based paths.
  - Landed as Task 5.2 (path-by-id refactor) on top of the existing 12 commits. See `openspec/changes/crud-services/tasks.md` Task 5.2 below.

## Phase 5: Integration verification

- [x] **5.1 Full `mvn -B verify`**. Run `mvn -B verify`; assert zero failures; leave `RmFarmaBackApplicationTests` uncommented (no — left commented per `openspec/config.yaml` notes, the new code is compatible with the commented state). Verify: `mvn -B verify` (72 tests run, 0 failures, 0 errors, 0 skipped, BUILD SUCCESS). Commit: chore commit `chore(services): green mvn verify with all CRUD tests`.

- [x] **5.2 Path-by-id refactor (code → id)** (RED→GREEN). Decision: front-end sends UUIDs in CRUD operations, so all write-path and `GET /{id}` URLs use the entity `id` (UUID) instead of `code`. RED first: update each affected test to assert `id`-based behavior, watch the updated tests fail (or fail to compile) with the new signatures; then GREEN the production code to match.

  Sub-tasks (each is RED→GREEN, may be combined into one commit if they all share the same test→prod boundary):

  - **5.2.a** Rename `ServicesPersistencePort` methods: `findByCode` → `findById`, `findEnabledByCode` → `findEnabledById`, `disableByCode` → `disableById`, `findResourceByCode` → `findResourceById`. Update the SPI port and its adapter. `JpaRepository#findById(String)` is the default lookup; no extra derived method needed. Verify: `mvn -B test -Dtest=ServicesPersistencePortAdapterTest`.

  - **5.2.b** Rename `ServiceServicePort` methods: `findByCode` → `findById`, `deleteByCode` → `deleteById`. Change `update(String code, UpdateServiceRequest)` → `update(String id, UpdateServiceRequest)`. Update `ServiceServicePortImpl` accordingly. Verify: `mvn -B test -Dtest=ServiceServicePortImplTest`.

  - **5.2.c** Rename `GetServiceByCodeUseCase` → `GetServiceByIdUseCase`; input becomes `String id`; calls `port.findById(id)`. Rename test class and update assertions. Verify: `mvn -B test -Dtest=GetServiceByIdUseCaseTest`.

  - **5.2.d** `UpdateServiceUseCase`: input is now `String id` (path); replace `port.findByCode(id)` → `port.findById(id)`; `NotFoundException("Service", id)` carries the id. Verify: `mvn -B test -Dtest=UpdateServiceUseCaseTest`.

  - **5.2.e** `DeleteServiceUseCase`: input is now `String id`; replace `port.findEnabledByCode(id)` → `port.findEnabledById(id)`, `port.disableByCode(id)` → `port.disableById(id)`. Verify: `mvn -B test -Dtest=DeleteServiceUseCaseTest`.

  - **5.2.f** `ServiceController`: change path variables from `/{code}` to `/{id}` on `@PatchMapping`, `@DeleteMapping`, `@GetMapping`. Update method signatures accordingly. Update `ServiceControllerTest` to use UUID path values and assert id-based behavior. Verify: `mvn -B test -Dtest=ServiceControllerTest`.

  - **5.2.g** Update the patient caller `DiagnosisPatientServiceImpl#createDiagnosisPatient` to use `findResourceById` (port method renamed) with the same null-on-miss behavior. No new test (this caller is out of scope for the service BC; covered by the existing patient tests if any).

  - **5.2.h** Final `mvn -B verify` from a clean state. Verify: `mvn -B verify` BUILD SUCCESS, all tests pass, 0 skipped. Commit: `refactor(service): CRUD paths use entity id (UUID) instead of code`.

  Note: combine sub-tasks 5.2.a–5.2.h into a single commit because the rename touches the port, adapter, use cases, controller, and tests as a coherent unit. Strict TDD is preserved at the unit level: tests are updated first, watched to fail or fail-to-compile, then production code is updated to compile and pass.

