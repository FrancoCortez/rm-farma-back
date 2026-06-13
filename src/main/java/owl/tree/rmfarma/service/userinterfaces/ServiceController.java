package owl.tree.rmfarma.service.userinterfaces;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.service.application.service.*;
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
    private final GetServiceByIdUseCase getServiceByIdUseCase;

    @GetMapping
    public ResponseEntity<List<ServiceResourceDto>> findAll() {
        return ResponseEntity.ok(this.findServiceUseCase.findAll());
    }

    @PostMapping
    public ResponseEntity<ServiceResourceDto> create(@Valid @RequestBody CreateServiceRequest request) {
        ServiceResourceDto created = this.createServiceUseCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ServiceResourceDto> update(@PathVariable String id,
                                                     @Valid @RequestBody UpdateServiceRequest request) {
        ServiceResourceDto updated = this.updateServiceUseCase.update(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        this.deleteServiceUseCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResourceDto> findById(@PathVariable String id) {
        return ResponseEntity.ok(this.getServiceByIdUseCase.findById(id));
    }
}
