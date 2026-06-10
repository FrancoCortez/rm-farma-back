package owl.tree.rmfarma.service.userinterfaces;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.service.application.service.CreateServiceUseCase;
import owl.tree.rmfarma.service.application.service.DeleteServiceUseCase;
import owl.tree.rmfarma.service.application.service.FindServiceUseCase;
import owl.tree.rmfarma.service.application.service.GetServiceByCodeUseCase;
import owl.tree.rmfarma.service.application.service.UpdateServiceUseCase;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;

import java.util.List;

@RestController
@RequestMapping("api/v1/services")
@RequiredArgsConstructor
public class ServiceController {
    private final FindServiceUseCase findServiceUseCase;
    private final CreateServiceUseCase createServiceUseCase;
    private final UpdateServiceUseCase updateServiceUseCase;
    private final DeleteServiceUseCase deleteServiceUseCase;
    private final GetServiceByCodeUseCase getServiceByCodeUseCase;

    @GetMapping
    public ResponseEntity<List<ServiceResourceDto>> findAll() {
        return ResponseEntity.ok(this.findServiceUseCase.findAll());
    }

    @PostMapping
    public ResponseEntity<ServiceResourceDto> create(@Valid @RequestBody CreateServiceRequest request) {
        ServiceResourceDto created = this.createServiceUseCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{code}")
    public ResponseEntity<ServiceResourceDto> update(@PathVariable String code,
                                                     @Valid @RequestBody UpdateServiceRequest request) {
        ServiceResourceDto updated = this.updateServiceUseCase.update(code, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> delete(@PathVariable String code) {
        this.deleteServiceUseCase.deleteByCode(code);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<ServiceResourceDto> findByCode(@PathVariable String code) {
        return ResponseEntity.ok(this.getServiceByCodeUseCase.findByCode(code));
    }
}
