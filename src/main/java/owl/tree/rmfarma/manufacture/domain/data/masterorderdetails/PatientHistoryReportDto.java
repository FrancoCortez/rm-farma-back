package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistoryReportDto {
    private Timestamp productionDate;
    private String masterRecord;
    private String fullPatientName;
    private String patientRut;
    private String service;
    private String bed;
    private String patientIdentification;
    private String diagnosis;
    private String schema_de;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String doctorName;
    private String doctorRut;
    private String activeIngredient;
    private String dose;
    private String batch;
    private String laboratory;
    private Timestamp expirationDate;
    private String volumeTotal;
    private String complement;
    private String via;
    private String administrationTime;
    private Timestamp administrationDate;
    private Timestamp expirationAdministrationDate;
    private String conditionValue;
    private String observation;
    private String qfValidate;
    private String qfPrepare;
    private String technicalPrepare;
    private String qfConditioningTechnician;
    private String episode;
    private String observationNurse;

    @JsonGetter("productionDate")
    public OffsetDateTime getProductionDateAsOffset() {
        return productionDate != null
                ? productionDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }

    @JsonGetter("expirationDate")
    public OffsetDateTime getExpirationDateAsOffset() {
        return expirationDate != null
                ? expirationDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }

    @JsonGetter("administrationDate")
    public OffsetDateTime getAdministrationDateAsOffset() {
        return administrationDate != null
                ? administrationDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }

    @JsonGetter("expirationAdministrationDate")
    public OffsetDateTime getExpirationAdministrationDateAsOffset() {
        return expirationAdministrationDate != null
                ? expirationAdministrationDate.toLocalDateTime().atOffset(ZoneOffset.UTC)
                : null;
    }

}
