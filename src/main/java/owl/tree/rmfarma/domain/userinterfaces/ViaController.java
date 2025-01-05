package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.via.FindViaUseCase;
import owl.tree.rmfarma.domain.domain.data.via.ViaResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/via")
@RequiredArgsConstructor
public class ViaController {
    private final FindViaUseCase findViaUseCase;

    @GetMapping
    public ResponseEntity<List<ViaResourceDto>> findAll() {
        return ResponseEntity.ok(this.findViaUseCase.findAll());
    }
}
