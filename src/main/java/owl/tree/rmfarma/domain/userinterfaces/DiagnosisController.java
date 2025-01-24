package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.diagnosis.FindDiagnosisUseCase;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {
    private final FindDiagnosisUseCase findDiagnosisUseCase;

    @GetMapping
    public ResponseEntity<List<DiagnosisResourceDto>> findAll() {
        return ResponseEntity.ok(this.findDiagnosisUseCase.findAll());
    }
}
