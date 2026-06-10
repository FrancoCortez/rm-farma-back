# Gate: findByCode contract change for ServicesPersistencePort

This note gates the `ServicesPersistencePort.findByCode(String)` return-type
change required by the `crud-services` change.

## Current state (pre-apply)

- `ServicesPersistencePort.findByCode(String)` returns `ServiceResourceDto`
  (nullable, null on miss).
- `ServicesPersistencePortAdapter.findByCode` delegates to
  `ServicesRepository.findByCodeAndEnabledTrue(...)` and maps to the DTO.
- `ServicesRepository` exposes `Optional<Services> findByCode(String)` and
  `Optional<Services> findByCodeAndEnabledTrue(String)`.

## Caller search (production code)

`git grep -nE "ServicesPersistencePort.*findByCode|servicesPersistencePort\.findByCode"`
returns **one** hit:

- `src/main/java/owl/tree/rmfarma/patient/domain/services/DiagnosisPatientServiceImpl.java:52`
  — `ServiceResourceDto serviceResource = servicesPersistencePort.findByCode(entry.getServices());`

No tests, no other production callers reference
`servicesPersistencePort.findByCode`.

## Risk assessment

- One production caller. It treats a `null` return as a soft miss (it
  skips setting the resource). Migration to `Optional<Services>` is safe
  if the caller is updated to `findByCode(...).map(ServiceResourceDto::fromEntity).orElse(null)`
  semantics — concrete fix lands in Task 1.5.
- No `@WebMvcTest` or unit test currently exercises this path; no test
  surface to update.

## Decision

- **Proceed** with the port contract change.
- Task 1.5 will update `DiagnosisPatientServiceImpl#createDiagnosisPatient`
  to consume `Optional<Services>` and map locally to `ServiceResourceDto`
  (preserving the soft-miss-on-null behaviour).
- The use case layer is the only layer that translates `Optional.empty()`
  into `NotFoundException`.
