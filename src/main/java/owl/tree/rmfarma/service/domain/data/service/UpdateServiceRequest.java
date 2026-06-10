package owl.tree.rmfarma.service.domain.data.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateServiceRequest(
        @NotBlank @Size(max = 30) String code,
        @NotBlank @Size(max = 100) String description
) {
}
