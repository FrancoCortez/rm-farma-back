package owl.tree.rmfarma.product.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.product.application.complement.FindComplementUseCase;
import owl.tree.rmfarma.product.domain.data.complement.ComplementResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/complements")
@RequiredArgsConstructor
public class ComplementController {

    private final FindComplementUseCase findComplementUseCase;

    @GetMapping
    public ResponseEntity<List<ComplementResourceDto>> findAll() {
        return ResponseEntity.ok(this.findComplementUseCase.findAll());
    }
}
