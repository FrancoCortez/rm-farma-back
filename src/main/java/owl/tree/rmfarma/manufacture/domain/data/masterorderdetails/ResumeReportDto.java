package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeReportDto {
    private String rut;
    private String schemaName;
    private String unitHospitalName;
    private String doctorName;
    private String masterRecord;
    private String lastName;
    private String name;
    private Timestamp productionDate;
    private String observation;
    private String genericProduct;
    private Integer cycleNumber;
    private Integer cycleDay;
    private Long ov;
    private String trainer;

    @JsonGetter("productionDate")
    public OffsetDateTime getProductionDateAsOffset() {
        return productionDate != null
                ? productionDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }
}
