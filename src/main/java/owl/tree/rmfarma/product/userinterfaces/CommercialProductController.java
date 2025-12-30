package owl.tree.rmfarma.product.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.product.application.commercialproduct.CreateCommercialProductUseCase;
import owl.tree.rmfarma.product.application.commercialproduct.FindCommercialProductUseCase;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductCreateDto;
import owl.tree.rmfarma.product.domain.data.commercialproduct.CommercialProductResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/commercial-product")
@RequiredArgsConstructor
public class CommercialProductController {
    private final FindCommercialProductUseCase findCommercialProductUseCase;
    private final CreateCommercialProductUseCase createCommercialProductUseCase;

    @GetMapping
    public ResponseEntity<List<CommercialProductResourceDto>> findAll () {
        return ResponseEntity.ok(this.findCommercialProductUseCase.findAll());
    }

    @PostMapping
    public ResponseEntity<CommercialProductResourceDto> createCommercialProduct (@RequestBody CommercialProductCreateDto dto) {
        return ResponseEntity.ok(this.createCommercialProductUseCase.createCommercialProduct(dto));
    }
}
