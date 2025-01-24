package owl.tree.rmfarma.service.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.service.application.service.FindServiceUseCase;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final FindServiceUseCase findServiceUseCase;

    @GetMapping
    public ResponseEntity<List<ServiceResourceDto>> findAll() {
        return ResponseEntity.ok(this.findServiceUseCase.findAll());
    }
}
