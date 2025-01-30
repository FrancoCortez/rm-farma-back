package owl.tree.rmfarma.manufacture.domain.ports.api;

import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;

import java.util.List;

public interface OrderDetailServicePort {
    List<CustomReportDTO> getCustomReport();
}
