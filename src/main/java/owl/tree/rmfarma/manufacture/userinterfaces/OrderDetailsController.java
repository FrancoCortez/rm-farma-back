package owl.tree.rmfarma.manufacture.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.manufacture.application.orderdetails.ReportOrderDetailUseCase;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/order-details")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final ReportOrderDetailUseCase reportOrderDetailUseCase;

    @GetMapping("/generate/custom-report")
    public ResponseEntity<List<CustomReportDTO>> getCustomReport(@RequestParam(required = false) OffsetDateTime startDate,
                                                                 @RequestParam(required = false) OffsetDateTime endDate) {
        return ResponseEntity.ok(reportOrderDetailUseCase.customReport(startDate, endDate));
    }
}
