package owl.tree.rmfarma.patient.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.patient.application.patient.CreatePatientUseCase;
import owl.tree.rmfarma.patient.application.patient.FindPatientUseCase;
import owl.tree.rmfarma.patient.application.patient.data.PatientCreateResourceUseCaseDto;
import owl.tree.rmfarma.patient.domain.data.patient.PatientResourceDto;

@RestController
@RequestMapping("api/v1/patients")
@RequiredArgsConstructor
public class PatientController {
    private final CreatePatientUseCase createPatientUseCase;
    private final FindPatientUseCase findPatientUseCase;

    @PostMapping
    public ResponseEntity<PatientResourceDto> createPatient(@RequestBody PatientCreateResourceUseCaseDto patientCreateResourceUseCaseDto) {
        return ResponseEntity.ok(this.createPatientUseCase.createPatient(patientCreateResourceUseCaseDto));
    }

    @GetMapping("identification/{identification}")
    public ResponseEntity<PatientResourceDto> getPatientByIdentification(@PathVariable String identification) {
        return ResponseEntity.ok(this.findPatientUseCase.getPatientByIdentification(identification));
    }
}
