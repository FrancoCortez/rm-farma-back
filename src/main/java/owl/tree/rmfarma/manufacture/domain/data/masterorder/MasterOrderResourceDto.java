package owl.tree.rmfarma.manufacture.domain.data.masterorder;

import lombok.*;
import owl.tree.rmfarma.domain.domain.data.documentype.DocumentTypeResourceDto;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;
import owl.tree.rmfarma.shared.enumes.StateMachineEnum;

import java.time.OffsetDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MasterOrderResourceDto {
    private String id;
    private String masterRecord;
    private OffsetDateTime productionDate;
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
    private String viaCode;
    private String viaDescription;
    private String documentTypeCode;
    private String documentTypeName;
    private Set<OrderDetailResourceDto> orderDetails;
    private DiagnosisOrderStageResourceDto diagnosisOrderStage;
    private PatientResourceDto patient;
    private DocumentTypeResourceDto documentType;
    private StateMachineEnum state;
}
