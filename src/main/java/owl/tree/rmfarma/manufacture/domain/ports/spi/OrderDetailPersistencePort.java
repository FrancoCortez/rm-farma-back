package owl.tree.rmfarma.manufacture.domain.ports.spi;

import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.*;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderDetailPersistencePort {

    OrderDetailResourceDto create(OrderDetailCreateResourceDto orderDetailCreateResourceDto);
    List<CustomReportDTO> getCustomReport(OffsetDateTime startDate, OffsetDateTime endDate);

    OrderDetailResourceDto findByMasterRecord(String masterRecord);

    OrderDetailResourceDto updateOrderDetail(OrderDetailUpdateResourceDto orderUpdate);

    List<ResumeReportDto> getResumeReport(OffsetDateTime startDate, OffsetDateTime endDate);
}
