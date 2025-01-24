package owl.tree.rmfarma.domain.domain.data.isapre;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class IsapreResourceDto {
    private String id;
    private Integer code;
    private String description;
}
