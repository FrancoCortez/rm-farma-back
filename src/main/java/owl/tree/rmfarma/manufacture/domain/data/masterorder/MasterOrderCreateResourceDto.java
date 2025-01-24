package owl.tree.rmfarma.manufacture.domain.data.masterorder;

import lombok.*;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MasterOrderCreateResourceDto {
    private String masterRecord;
    private OffsetDateTime productionDate;
    private String patient;
    private String patientName;
    private String patientLastName;
    private String patientRut;
    private String patientIdentification;
    private String doctorName;
    private String doctorRut;
    private String clinicCode;
    private String clinicName;
    private Integer isapreCode;
    private String isapreName;
    private String diagnosisCode;
    private String diagnosisName;
    private Integer cycleNumber;
    private Integer cycleDay;
    private String schemaCode;
    private String schemaName;
    private String viaDescription;
    private String pharmaceuticalChemist;
    private String documentTypeCode;
    private String documentTypeName;
    private String documentType;
    private StateMachineEnum status;
    private String diagnosisOrderStage;
}
