package owl.tree.rmfarma.domain.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.domain.application.isapre.FindIsapreUseCase;
import owl.tree.rmfarma.domain.domain.data.isapre.IsapreResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/isapres")
@RequiredArgsConstructor
public class IsapreController {
    private final FindIsapreUseCase findIsapreUseCase;

    @GetMapping
    public ResponseEntity<List<IsapreResourceDto>> findAll() {
        return ResponseEntity.ok(this.findIsapreUseCase.findAll());
    }
}
