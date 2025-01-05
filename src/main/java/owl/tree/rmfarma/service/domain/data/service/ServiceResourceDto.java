package owl.tree.rmfarma.service.domain.data.service;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceResourceDto {
    private String id;
    private String code;
    private String description;
}
