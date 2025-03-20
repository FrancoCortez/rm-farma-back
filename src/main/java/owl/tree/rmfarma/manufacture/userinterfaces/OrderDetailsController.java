package owl.tree.rmfarma.manufacture.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.manufacture.application.orderdetails.ReportOrderDetailUseCase;
import owl.tree.rmfarma.manufacture.application.orderdetails.UpdateOrderDetailUseCase;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.CustomReportDTO;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.OrderDetailUpdateFormResourceDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorderdetails.ResumeReportDto;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/order-details")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final ReportOrderDetailUseCase reportOrderDetailUseCase;
    private final UpdateOrderDetailUseCase updateOrderDetailUseCase;

    @GetMapping("/generate/custom-report")
    public ResponseEntity<List<CustomReportDTO>> getCustomReport(@RequestParam(required = false) OffsetDateTime startDate,
                                                                 @RequestParam(required = false) OffsetDateTime endDate) {
        return ResponseEntity.ok(reportOrderDetailUseCase.customReport(startDate, endDate));
    }

    @GetMapping("/generate/resume-report")
    public ResponseEntity<List<ResumeReportDto>> getResumeReport(@RequestParam(required = false) OffsetDateTime startDate,
                                                                 @RequestParam(required = false) OffsetDateTime endDate) {
        return ResponseEntity.ok(reportOrderDetailUseCase.resumeReport(startDate, endDate));
    }

    @PutMapping("/update")
    public ResponseEntity<OrderDetailResourceDto> updateOrderDetail(@RequestBody OrderDetailUpdateFormResourceDto body) {
        return ResponseEntity.ok(this.updateOrderDetailUseCase.updateOrderDetail(body));
    }
}
