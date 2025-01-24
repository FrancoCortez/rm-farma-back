package owl.tree.rmfarma.manufacture.domain.data.generateid;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GeneratorIdCreateResourceDto {
    private Integer correlative;
    private Integer year;
}
