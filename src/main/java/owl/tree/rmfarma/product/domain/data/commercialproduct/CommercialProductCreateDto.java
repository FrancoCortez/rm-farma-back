package owl.tree.rmfarma.product.domain.data.commercialproduct;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialProductCreateDto {
    private String code;
    private String description;
    private String laboratory;
    private Double concentration;
    private String activeIngredientCode;
    private String concentrationUnit;
}
