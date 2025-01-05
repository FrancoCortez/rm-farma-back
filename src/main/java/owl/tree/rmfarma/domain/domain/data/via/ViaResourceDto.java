package owl.tree.rmfarma.domain.domain.data.via;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ViaResourceDto {
    private String id;
    private String code;
    private String description;
}
