package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.domain.application.diagnosis.CreateDiagnosisUseCase;
import owl.tree.rmfarma.domain.application.diagnosis.DeleteDiagnosisUseCase;
import owl.tree.rmfarma.domain.application.diagnosis.FindDiagnosisUseCase;
import owl.tree.rmfarma.domain.application.diagnosis.UpdateDiagnosisUseCase;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisCreateDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisResourceDto;
import owl.tree.rmfarma.domain.domain.data.diagnosis.DiagnosisUpdateResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/diagnosis")
@RequiredArgsConstructor
public class DiagnosisController {

    private final FindDiagnosisUseCase findDiagnosisUseCase;
    private final CreateDiagnosisUseCase createDiagnosisUseCase;
    private final UpdateDiagnosisUseCase updateDiagnosisUseCase;
    private final DeleteDiagnosisUseCase deleteDiagnosisUseCase;

    @GetMapping
    public ResponseEntity<List<DiagnosisResourceDto>> findAll() {
        return ResponseEntity.ok(this.findDiagnosisUseCase.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiagnosisResourceDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(this.findDiagnosisUseCase.findById(id));
    }

    @PostMapping
    public ResponseEntity<DiagnosisResourceDto> create(@RequestBody DiagnosisCreateDto dto) {
        return ResponseEntity.ok(this.createDiagnosisUseCase.create(dto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DiagnosisResourceDto> update(@PathVariable String id, @RequestBody DiagnosisUpdateResourceDto dto) {
        return ResponseEntity.ok(this.updateDiagnosisUseCase.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> softDelete(@PathVariable String id) {
        this.deleteDiagnosisUseCase.softDelete(id);
        return ResponseEntity.noContent().build();
    }
}