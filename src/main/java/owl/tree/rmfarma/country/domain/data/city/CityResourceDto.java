package owl.tree.rmfarma.country.domain.data.city;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CityResourceDto {
    private String id;
    private String name;
    private String code;
}
