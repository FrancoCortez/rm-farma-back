package owl.tree.rmfarma.doctor.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.doctor.application.doctor.CreateDoctorUseCase;
import owl.tree.rmfarma.doctor.application.doctor.FindDoctorUseCase;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final FindDoctorUseCase findDoctorUserCase;
    private final CreateDoctorUseCase createDoctorUseCase;

    @GetMapping
    public ResponseEntity<List<DoctorResourceDto>> findAll() {
        return ResponseEntity.ok(this.findDoctorUserCase.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<DoctorResourceDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(this.findDoctorUserCase.findById(id));
    }

    @PostMapping
    public ResponseEntity<DoctorResourceDto> create(@RequestBody DoctorCreateResourceDto create) {
        return ResponseEntity.ok(this.createDoctorUseCase.create(create));
    }
}
