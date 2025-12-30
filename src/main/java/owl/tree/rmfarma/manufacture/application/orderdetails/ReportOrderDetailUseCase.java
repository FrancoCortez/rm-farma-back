package owl.tree.rmfarma.manufacture.application.orderdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ConcentrationReportDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.PatientHistoryReportDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ResumeReportDto;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportOrderDetailUseCase {
    private final OrderDetailServicePort orderDetailServicePort;

    public List<CustomReportDTO> customReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return orderDetailServicePort.getCustomReport(startDate, endDate);
    }

    public List<ResumeReportDto> resumeReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return orderDetailServicePort.getResumeReport(startDate, endDate);
    }

    public List<ConcentrationReportDto> concentrationReport(OffsetDateTime startDate, OffsetDateTime endDate) {
        return orderDetailServicePort.getConcentrationReport(startDate, endDate);
    }

    public List<PatientHistoryReportDto> patientHistoryReport() {
        return orderDetailServicePort.patientHistoryReport();
    }
}
