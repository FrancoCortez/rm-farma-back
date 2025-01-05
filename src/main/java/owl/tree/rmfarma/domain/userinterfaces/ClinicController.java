package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.clinic.FindClinicUseCase;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/clinics")
@RequiredArgsConstructor
public class ClinicController {
    private final FindClinicUseCase findClinicUseCase;

    @GetMapping
    public ResponseEntity<List<ClinicResourceDto>> findAll() {
        return ResponseEntity.ok(this.findClinicUseCase.findAll());
    }
}
