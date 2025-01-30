package owl.tree.rmfarma.shared.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import owl.tree.rmfarma.shared.exception.base.BusinessException;
import owl.tree.rmfarma.shared.exception.base.InfrastructureException;
import owl.tree.rmfarma.shared.exception.data.ErrorResponse;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", request.getDescription(false));
        error.addValidationError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InfrastructureException.class)
    public ResponseEntity<Object> handleInfrastructureException(InfrastructureException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation Failed", request.getDescription(false));
        error.addValidationError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
//        ErrorResponse error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "We are sorry, an internal error has occurred", request.getDescription(false));
//        error.addValidationError(ex.getMessage());
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND, "Resource not found", request.getDescription(false));
        error.addValidationError(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
