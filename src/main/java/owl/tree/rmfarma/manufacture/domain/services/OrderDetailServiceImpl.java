package owl.tree.rmfarma.manufacture.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.ports.api.OrderDetailServicePort;
import owl.tree.rmfarma.manufacture.domain.ports.spi.OrderDetailPersistencePort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailServicePort {

    private final OrderDetailPersistencePort orderDetailPersistencePort;


    public List<CustomReportDTO> getCustomReport() {
        return this.orderDetailPersistencePort.getCustomReport();
    }
}
