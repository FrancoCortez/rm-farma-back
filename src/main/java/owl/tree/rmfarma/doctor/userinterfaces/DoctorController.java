package owl.tree.rmfarma.doctor.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.doctor.application.doctor.FindDoctorUseCase;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final FindDoctorUseCase findDoctorUserCase;

    @GetMapping("find-by-rut/{rut}")
    public ResponseEntity<DoctorResourceDto> findByRut(@PathVariable String rut) {
        return ResponseEntity.ok(findDoctorUserCase.findByRut(rut));
    }

    @GetMapping
    public ResponseEntity<List<DoctorResourceDto>> findAll() {
        return ResponseEntity.ok(this.findDoctorUserCase.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<DoctorResourceDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(this.findDoctorUserCase.findById(id));
    }
}
