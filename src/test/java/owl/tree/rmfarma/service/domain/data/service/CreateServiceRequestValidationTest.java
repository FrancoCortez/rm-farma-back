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

class CreateServiceRequestValidationTest {

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
        CreateServiceRequest request = new CreateServiceRequest("", "Checkup");
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("code");
    }

    @Test
    void blankDescriptionProducesNotBlankViolation() {
        CreateServiceRequest request = new CreateServiceRequest("SRV-001", "");
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("description");
    }

    @Test
    void codeExceeding30CharsProducesSizeViolation() {
        String longCode = "X".repeat(31);
        CreateServiceRequest request = new CreateServiceRequest(longCode, "Checkup");
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("code");
    }

    @Test
    void descriptionExceeding100CharsProducesSizeViolation() {
        String longDescription = "X".repeat(101);
        CreateServiceRequest request = new CreateServiceRequest("SRV-001", longDescription);
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).extracting(v -> v.getPropertyPath().toString())
                .contains("description");
    }

    @Test
    void validRequestProducesNoViolations() {
        CreateServiceRequest request = new CreateServiceRequest("SRV-001", "Checkup");
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void codeAtMaxSizeProducesNoViolation() {
        String maxCode = "X".repeat(30);
        CreateServiceRequest request = new CreateServiceRequest(maxCode, "Checkup");
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @Test
    void descriptionAtMaxSizeProducesNoViolation() {
        String maxDescription = "X".repeat(100);
        CreateServiceRequest request = new CreateServiceRequest("SRV-001", maxDescription);
        Set<ConstraintViolation<CreateServiceRequest>> violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }
}
