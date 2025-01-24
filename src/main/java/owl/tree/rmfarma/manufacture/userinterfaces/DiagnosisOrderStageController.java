package owl.tree.rmfarma.manufacture.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.manufacture.application.diagnosisorder.CreateDiagnosisOrderStageUseCase;
import owl.tree.rmfarma.manufacture.application.diagnosisorder.data.DiagnosisOrderStageCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.diagnosisorder.DiagnosisOrderStageResourceDto;

@RestController
@RequestMapping("api/v1/diagnosis-order-stages")
@RequiredArgsConstructor
public class DiagnosisOrderStageController {
    private final CreateDiagnosisOrderStageUseCase createDiagnosisOrderStageUseCase;

    @PostMapping
    public ResponseEntity<DiagnosisOrderStageResourceDto> createDiagnosisOrder(@RequestBody DiagnosisOrderStageCreateResourceUseCaseDto body) {
        return ResponseEntity.ok(this.createDiagnosisOrderStageUseCase.createDiagnosis(body));
    }
}
