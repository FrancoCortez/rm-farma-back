package owl.tree.rmfarma.product.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.product.application.commercialproduct.FindCommercialProductUseCase;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/commercial-product")
@RequiredArgsConstructor
public class CommercialProductController {
    private final FindCommercialProductUseCase findCommercialProductUseCase;

    @GetMapping
    public ResponseEntity<List<CommercialProductResourceDto>> findAll () {
        return ResponseEntity.ok(this.findCommercialProductUseCase.findAll());
    }
}
