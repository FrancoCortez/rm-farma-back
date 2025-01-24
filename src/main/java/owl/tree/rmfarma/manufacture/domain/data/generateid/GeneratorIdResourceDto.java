package owl.tree.rmfarma.manufacture.domain.data.generateid;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GeneratorIdResourceDto {
    private String id;
    private Integer correlative;
    private Integer year;
}
