package owl.tree.rmfarma.service.domain.data.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateServiceRequestValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void initValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void blankCodeProducesNotBlankViolation() {
        UpdateServiceRequest request = new UpdateServiceRequest("", "Checkup");
        Set<ConstraintViolation<UpdateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("code");
    }

    @Test
    void blankDescriptionProducesNotBlankViolation() {
        UpdateServiceRequest request = new UpdateServiceRequest("SRV-001", "");
        Set<ConstraintViolation<UpdateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("description");
    }

    @Test
    void codeExceeding30CharsProducesSizeViolation() {
        String longCode = "X".repeat(31);
        UpdateServiceRequest request = new UpdateServiceRequest(longCode, "Checkup");
        Set<ConstraintViolation<UpdateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("code");
    }

    @Test
    void descriptionExceeding100CharsProducesSizeViolation() {
        String longDescription = "X".repeat(101);
        UpdateServiceRequest request = new UpdateServiceRequest("SRV-001", longDescription);
        Set<ConstraintViolation<UpdateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("description");
    }

    @Test
    void validRequestProducesNoViolations() {
        UpdateServiceRequest request = new UpdateServiceRequest("SRV-001", "Checkup");
        Set<ConstraintViolation<UpdateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
