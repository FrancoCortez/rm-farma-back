package owl.tree.rmfarma.report.domain.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecipeBookDto {
    private String masterRecord;
    private Timestamp productionDate;
    private String patientName;
    private String patientRut;
    private String doctorName;
    private String doctorRut;
    private String productName;
    private String dose;
    private String laboratory;
    private String lote;
    private Timestamp expirationDate;
    private String complementName;
    private String volumeTotal;

    @JsonGetter("productionDate")
    public OffsetDateTime getProductionDateAsOffset() {
        return productionDate != null
                ? productionDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }

    @JsonGetter("expirationDate")
    public OffsetDateTime getExpirationAsOffset() {
        return expirationDate != null
                ? expirationDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }
}
