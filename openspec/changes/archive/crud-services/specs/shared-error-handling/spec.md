# Spec Delta: shared-error-handling

## Purpose

A reusable, cross-bounded-context translation layer from domain
exceptions to HTTP responses with a uniform JSON shape. This change
extends the existing `GlobalExceptionHandler` (`shared/config`) so that
the new service CRUD endpoints — and every future bounded context —
get correct `400 / 404 / 409` responses for bean validation, not-found
misses, and uniqueness violations.

## ADDED Requirements

### Requirement: Uniform `ErrorResponse` shape

Every error response produced by `GlobalExceptionHandler` SHALL conform
to the `ErrorResponse` DTO defined in
`owl.tree.rmfarma.shared.exception.data.ErrorResponse`, with the
following fields populated:

- `timestamp` — `LocalDateTime` of when the response was built
- `status` — the HTTP status code as an integer (e.g. `404`)
- `error` — the HTTP status reason phrase (e.g. `"Not Found"`)
- `message` — a short, human-readable summary of the failure
- `path` — the request URI as exposed by `WebRequest`
- `errors` — a `List<String>` of field-level or contextual violation
  messages; empty when the failure is a single domain error

The JSON contract is stable: clients can rely on these six fields
existing on every error response.

#### Scenario: ErrorResponse fields are present and well-formed

- **Given** any handler in `GlobalExceptionHandler` produces a
  response
- **When** the response body is serialized to JSON
- **Then** it contains `timestamp` (ISO-8601 local date-time), `status`
  (int), `error` (string), `message` (string), `path` (string), and
  `errors` (array of strings, possibly empty)
- **And** none of the five scalar fields are `null` (the `errors`
  array is never `null`).

### Requirement: Map `ExistsException` to `409 Conflict`

`GlobalExceptionHandler` SHALL handle
`owl.tree.rmfarma.shared.exception.domain.ExistsException` and return
`HTTP 409 Conflict`. The handler is the only place where this mapping
lives — use cases and the persistence adapter MUST remain free of HTTP
types.

#### Scenario: ExistsException surfaces as 409

- **Given** a use case throws `ExistsException("code", "Service",
  "SRV-001")`
- **When** the request reaches `GlobalExceptionHandler`
- **Then** the response status is `409`
- **And** the body is an `ErrorResponse` with `status = 409`,
  `error = "Conflict"`, `message` describing a uniqueness conflict, and
  `path` matching the request URI.

### Requirement: Map `NotFoundException` to `404 Not Found`

`GlobalExceptionHandler` SHALL continue to handle
`owl.tree.rmfarma.shared.exception.domain.NotFoundException` and return
`HTTP 404 Not Found`. (This requirement is added to this capability
because the new service endpoints rely on it; the behavior already
exists in code but is now documented as a contract of this
capability.)

#### Scenario: NotFoundException surfaces as 404

- **Given** a use case throws `NotFoundException("Service",
  "SRV-UNKNOWN")`
- **When** the request reaches `GlobalExceptionHandler`
- **Then** the response status is `404`
- **And** the body is an `ErrorResponse` with `status = 404`,
  `error = "Not Found"`, `message` indicating the resource was not
  found, and `path` matching the request URI.

### Requirement: Map `MethodArgumentNotValidException` to `400 Bad Request`

`GlobalExceptionHandler` SHALL handle Spring's
`MethodArgumentNotValidException` (raised by `@Valid` on
`@RequestBody`) and return `HTTP 400 Bad Request`. The
field-level violation messages produced by Bean Validation SHALL be
copied into the `errors` list of the `ErrorResponse`, so clients can
surface per-field errors.

#### Scenario: Bean validation failure surfaces as 400 with field errors

- **Given** a `POST /api/v1/services` request with an empty `code`
  (violates `@NotBlank`) and a 200-char `description` (violates
  `@Size(max = 100)`) on `CreateServiceRequest`
- **When** Spring's bean validation runs and raises
  `MethodArgumentNotValidException`
- **Then** the response status is `400`
- **And** the body is an `ErrorResponse` with `status = 400`,
  `error = "Bad Request"`, a generic validation `message`, and the
  `errors` list containing one entry per field violation
  (e.g. `"code: must not be blank"`, `"description: size must be
  between 0 and 100"`).

#### Scenario: Bean validation on PATCH request also returns 400

- **Given** a `PATCH /api/v1/services/SRV-001` request whose
  `UpdateServiceRequest` fails `@NotBlank` or `@Size`
- **When** Spring's bean validation runs
- **Then** the response is `400 Bad Request` with the same
  `ErrorResponse` shape and field-level entries in `errors`; the
  service row is unchanged.

### Requirement: Centralized, cross-cutting handler

`GlobalExceptionHandler` SHALL be a `@RestControllerAdvice` registered
globally (no explicit `basePackages` restriction required for v1) so
that every controller in the application benefits from the mappings
above. No controller method may catch domain exceptions locally; the
shared handler is the single source of truth for HTTP error shape.

#### Scenario: Handler covers controllers from any bounded context

- **Given** any controller in `owl.tree.rmfarma.*.userinterfaces`
  throws `ExistsException`, `NotFoundException`, or
  `MethodArgumentNotValidException`
- **When** the exception propagates
- **Then** it is handled by `GlobalExceptionHandler` and the response
  matches the shape defined above; the controller method does not
  catch the exception.

## MODIFIED Requirements

None — the capability is new in this change.

## REMOVED Requirements

None.
