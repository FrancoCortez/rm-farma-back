package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
}
