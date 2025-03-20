package owl.tree.rmfarma.manufacture.domain.ports.api;

import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailUpdateFormResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ResumeReportDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderDetailServicePort {
    List<CustomReportDTO> getCustomReport(OffsetDateTime startDate, OffsetDateTime endDate);

    OrderDetailResourceDto updateOrderDetail(OrderDetailUpdateFormResourceDto body);

    OrderDetailResourceDto createOrderDetail (MasterOrderCreateResourceUseCaseDto masterOrderCreateResourceUseCaseDto, String masterOrderId);

    List<ResumeReportDto> getResumeReport(OffsetDateTime startDate, OffsetDateTime endDate);
}
