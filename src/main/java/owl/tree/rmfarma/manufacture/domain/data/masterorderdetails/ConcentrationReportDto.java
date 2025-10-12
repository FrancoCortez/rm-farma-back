package owl.tree.rmfarma.manufacture.domain.data.masterorderdetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConcentrationReportDto {
    private String name;
    private String hospitalName;
    private String productName;
    private Double dose;
    private String unitMetric;
    private String volumeTotalProduct;
    private String laboratory;
    private Double volumeTotal;
    private String vehicle;
    private String viaName;
    private String down;
}
