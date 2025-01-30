package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailCreateResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;

import java.util.List;

public interface OrderDetailPersistencePort {

    OrderDetailResourceDto create(OrderDetailCreateResourceDto orderDetailCreateResourceDto);
    List<CustomReportDTO> getCustomReport();
}
