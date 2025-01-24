package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.hospitalunit.FindHospitalUnitUseCase;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/hospital-units")
@RequiredArgsConstructor
public class HospitalUnitController {
    private final FindHospitalUnitUseCase findHospitalUnitUseCase;

    @GetMapping
    public ResponseEntity<List<HospitalUnitResourceDto>> findAll() {
        return ResponseEntity.ok(this.findHospitalUnitUseCase.findAll());
    }

}
