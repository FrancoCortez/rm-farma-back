package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomReportDTO {
    private String masterRecord;
    private Timestamp productionDate;
    private String fullPatientName;
    private String patientRut;
    private String doctorName;
    private String doctorRut;
    private String unitName; // Corregido a unitName
    private String code;
    private String description; // Corregido a description
    private Long quantity;
    private String quantityReal;
    private String laboratory;
    private String batch;
    private Integer isapreCode;
    private String isapreName;
    private String diagnosisCode;
    private String diagnosisName;
    private Integer cycleDay;
    private Integer cycleNumber;
    private String schemaCode;
    private String schemaName;
    private Double volumeTotal;
    private String viaCode;
    private String viaDescription;
    private String qf; // Corregido a qf (minúscula)
    private Long guia;
}
