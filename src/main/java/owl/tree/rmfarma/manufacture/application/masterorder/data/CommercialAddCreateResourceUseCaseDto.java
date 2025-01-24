package owl.tree.rmfarma.manufacture.application.masterorder.data;


import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CommercialAddCreateResourceUseCaseDto {
    private String commercial;
    private Integer part;
    private String batch;
}
