package owl.tree.rmfarma.manufacture.application.orderdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReportOrderDetailUseCase {
    private final OrderDetailServicePort orderDetailServicePort;

    public List<CustomReportDTO> customReport() {
        return orderDetailServicePort.getCustomReport();
    }
}
