package owl.tree.rmfarma.product.domain.data.commercialproduct;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialProductResourceDto {
    private String id;
    private String code;
    private String description;
    private String laboratory;
    private Double concentration;
    private String concentrationUnit;
    private String defaultConcentration;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;
    private List<CommercialProductFactorResourceDto> factors;
    private ProductResourceDto product;
}
