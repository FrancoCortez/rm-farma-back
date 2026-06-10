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

### Work Units

| # | Goal | PR |
|--|------|----|
| 1 | Port-contract gate + repo + adapter (Optional entity, new methods) + caller fix | PR 1 |
| 2 | CreateServiceUseCase + CreateServiceRequest + `POST` | PR 1 |
| 3 | UpdateServiceUseCase + UpdateServiceRequest + mapper update + `PATCH` | PR 2 |
| 4 | DeleteServiceUseCase + `DELETE` + GetServiceByCodeUseCase + `GET /{code}` + port wiring | PR 2 |
| 5 | Extend `GlobalExceptionHandler` (ExistsException→409, MethodArgumentNotValidException→400) | PR 3 |

> Orchestrator must ask user which chain strategy (stacked-to-main / feature-branch-chain / size:exception) before apply.

## Phase 1: Foundation (PR 1)

- [ ] **1.1 Gate the `findByCode` contract change** (read-only). `git grep -nE "ServicesPersistencePort.*findByCode|servicesPersistencePort\.findByCode"`; confirm single call site `DiagnosisPatientServiceImpl#createDiagnosisPatient` (line 52). Commit: `docs(services): gate findByCode return-type change`.

- [ ] **1.2 Repository: 5 derived methods + `@DataJpaTest`** (RED→GREEN). RED: `service/infrastructure/repository/ServicesRepositoryTest.java` (`@DataJpaTest` + `@AutoConfigureTestDatabase`); one test per derived method, assert `existsByCodeAndIdNot` self-exclusion. Add `com.h2database:h2` test-scoped to `pom.xml`. GREEN: extend `ServicesRepository` with `existsByCode`, `existsByDescription`, `findByDescriptionAndEnabledTrue`, `existsByDescriptionAndIdNot`, `existsByCodeAndIdNot`. Verify: `mvn -B test -Dtest=ServicesRepositoryTest`. Commit: `feat(service): uniqueness and lookup derived queries`.

- [ ] **1.3 Out port: extend `ServicesPersistencePort`** (RED→GREEN). RED: stub `ServicesPersistencePortTest` compiling against new signatures. GREEN: replace `ServiceResourceDto findByCode(String)` with `Optional<Services> findByCode(String)`; add `Services save(Services)`, `Optional<Services> findEnabledByCode(String)`, `void disableByCode(String)`. Verify: `mvn -B compile`. Commit: `refactor(service): persistence port returns Optional<entity>`.

- [ ] **1.4 Adapter: implement new port methods** (RED→GREEN). RED: `service/infrastructure/adapter/ServicesPersistencePortAdapterTest.java` (`@Mock ServicesRepository` + `@Mock ServicesMapper`); assert `findByCode` returns `Optional` (empty on miss, mapped on hit), `disableByCode` saves with `enabled=false`, `save` delegates. GREEN: rewrite `ServicesPersistencePortAdapter`. Verify: `mvn -B test -Dtest=ServicesPersistencePortAdapterTest`. Commit: `feat(service): persistence adapter for create/update/disable/findByCode`.

- [ ] **1.5 Fix caller `DiagnosisPatientServiceImpl`** (no new test). Update line 52 of `patient/domain/services/DiagnosisPatientServiceImpl.java` to consume `Optional<Services>` and map locally. Verify: `mvn -B compile`. Commit: `fix(patient): adapt DiagnosisPatientServiceImpl to Optional<entity>`.

## Phase 2: Create + Update slices (PR 1 end → PR 2)

- [ ] **2.1 CreateServiceRequest DTO** (RED→GREEN). RED: `service/domain/data/service/CreateServiceRequestValidationTest.java` using Jakarta `Validator`; assert `@NotBlank` + `@Size` violations. GREEN: create `CreateServiceRequest.java` (record, `@NotBlank @Size(max=30) String code`, `@NotBlank @Size(max=100) String description`). Verify: `mvn -B test -Dtest=CreateServiceRequestValidationTest`. Commit: `feat(service): CreateServiceRequest DTO`.

- [ ] **2.2 CreateServiceUseCase (unit)** (RED→GREEN). RED: `service/application/service/CreateServiceUseCaseTest.java` (`@ExtendWith(MockitoExtension.class)`, `@Mock ServiceServicePort` + `@Mock ServicesMapper`); cover trim before `existsByCode`/`existsByDescription`, `ExistsException("code"|"description",…)`, happy path. GREEN: create `CreateServiceUseCase.java` (`@Component`). Verify: `mvn -B test -Dtest=CreateServiceUseCaseTest`. Commit: `feat(service): CreateServiceUseCase with trim and uniqueness`.

- [ ] **2.3 POST endpoint + controller slice test** (RED→GREEN). RED: `service/userinterfaces/ServiceControllerTest.java` (`@WebMvcTest(ServiceController.class)`, `@MockBean` on 5 use cases); POST cases: happy → 201, blank code → 400, oversize desc → 400, duplicate code → 409. GREEN: add `@PostMapping` with `@Valid @RequestBody CreateServiceRequest` to `ServiceController.java`. Verify: `mvn -B test -Dtest=ServiceControllerTest`. Commit: `feat(service): POST /api/v1/services`.

- [ ] **2.4 UpdateServiceRequest DTO + mapper update** (RED→GREEN). RED: `UpdateServiceRequestValidationTest` + `service/infrastructure/mappers/ServicesMapperTest.java` (`Mappers.getMapper(...)`); assert IGNORE strategy overwrites only non-null, leaves `enabled` untouched. GREEN: create `UpdateServiceRequest`; extend `ServicesMapper` with `toServices(CreateServiceRequest)` + `@BeanMapping(IGNORE) void updateEntityFromRequest(UpdateServiceRequest, @MappingTarget Services)`. Verify: `mvn -B test -Dtest=UpdateServiceRequestValidationTest,ServicesMapperTest`. Commit: `feat(service): UpdateServiceRequest DTO and mapper update`.

- [ ] **2.5 UpdateServiceUseCase + PATCH endpoint** (RED→GREEN). RED: (a) `UpdateServiceUseCaseTest` — same-row self-exclusion, cross-row `ExistsException`, `NotFoundException` on empty `findByCode`, `port.save(current)` once with trimmed values. (b) Extend `ServiceControllerTest`: PATCH happy → 200, not found → 404, cross-row duplicate → 409, blank code → 400. GREEN: create `UpdateServiceUseCase`; add `@PatchMapping("/{code}")`. Verify: `mvn -B test -Dtest=UpdateServiceUseCaseTest,ServiceControllerTest`. Commit: `feat(service): PATCH /api/v1/services/{code}`.

## Phase 3: Delete + Get-by-code slice (PR 2)

- [ ] **3.1 DeleteServiceUseCase + DELETE endpoint** (RED→GREEN). RED: (a) `DeleteServiceUseCaseTest` — empty `findEnabledByCode` → `NotFoundException`; present → `port.disableByCode` once. (b) Extend `ServiceControllerTest`: DELETE happy → 204, not found → 404. GREEN: create `DeleteServiceUseCase`; add `@DeleteMapping("/{code}")`. Verify: `mvn -B test -Dtest=DeleteServiceUseCaseTest,ServiceControllerTest`. Commit: `feat(service): soft DELETE /api/v1/services/{code}`.

- [ ] **3.2 GetServiceByCodeUseCase + GET by code** (RED→GREEN). RED: (a) `GetServiceByCodeUseCaseTest` — present → mapped DTO; empty → `NotFoundException`. (b) Extend `ServiceControllerTest`: GET happy → 200, not found → 404. GREEN: create `GetServiceByCodeUseCase`; add `@GetMapping("/{code}")`. Verify: `mvn -B test -Dtest=GetServiceByCodeUseCaseTest,ServiceControllerTest`. Commit: `feat(service): GET /api/v1/services/{code}`.

- [ ] **3.3 In-port `ServiceServicePort` + impl wiring** (RED→GREEN). RED: optional `ServiceServicePortImplTest` with `@Mock ServicesPersistencePort` asserting delegation. GREEN: extend `ServiceServicePort` with `create`, `update`, `deleteByCode`, `findByCode`; extend `ServiceServicePortImpl` to delegate. Verify: `mvn -B test -Dtest=ServiceServicePortImplTest`. Commit: `feat(service): in-port and impl expose create/update/delete/findByCode`.

## Phase 4: Cross-cutting error handler (PR 3)

- [ ] **4.1 GlobalExceptionHandler: `ExistsException` → 409** (RED→GREEN). RED: `shared/config/GlobalExceptionHandlerExistsTest.java` (MockMvc on tiny throwing controller); assert 409, `error == "Conflict"`, `message == "Resource already exists"`, `errors` non-null with `[ex.getMessage()]`, `path` contains URI. GREEN: add `@ExceptionHandler(ExistsException.class)` to `shared/config/GlobalExceptionHandler.java`. Do **not** modify existing handlers. Verify: `mvn -B test -Dtest=GlobalExceptionHandlerExistsTest`. Commit: `feat(shared): map ExistsException to 409`.

- [ ] **4.2 GlobalExceptionHandler: `MethodArgumentNotValidException` → 400** (RED→GREEN). RED: `GlobalExceptionHandlerValidationTest` (MockMvc on `@PostMapping @Valid` test controller with `@NotBlank` + `@Size(max=10)`); send blank + oversize; assert 400, `error == "Bad Request"`, `message == "Validation failed"`, `errors` has one `"<field>: <msg>"` per violation. GREEN: add `@ExceptionHandler(MethodArgumentNotValidException.class)`; copy `getFieldErrors()` into `errors`. Verify: `mvn -B test -Dtest=GlobalExceptionHandlerValidationTest`. Commit: `feat(shared): map MethodArgumentNotValidException to 400`.

## Phase 5: Integration verification

- [ ] **5.1 Full `mvn -B verify`**. Run `mvn -B verify -DskipITs=false`; assert zero failures; leave `RmFarmaBackApplicationTests` uncommented (preferred — proves full context loads) per `openspec/config.yaml` notes. Verify: `mvn -B verify`. Commit: `chore(services): green mvn verify with all CRUD tests` (or `test: enable SpringBootTest context-load` if uncommented).

## Apply notes

- Strict TDD: never land GREEN before its RED compiles and fails for the right reason.
- `findByCode` is consumed only by `DiagnosisPatientServiceImpl#createDiagnosisPatient`; 1.1 verifies before any production change.
- Handler precedence: `ExistsException extends BusinessException`; the new handler wins. Do not delete existing handlers.
- H2 dep added in 1.2 is test-scoped. If the team prefers Testcontainers MySQL, flag to orchestrator.
