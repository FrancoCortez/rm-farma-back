package owl.tree.rmfarma.product.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.product.application.product.CreateProductUseCase;
import owl.tree.rmfarma.product.application.product.FindProductUseCase;
import owl.tree.rmfarma.product.domain.data.product.ProductCreateDto;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final FindProductUseCase findProductUseCase;
    private final CreateProductUseCase createProductUseCase;

    @GetMapping
    public ResponseEntity<List<ProductResourceDto>> findAll() {
        return ResponseEntity.ok(this.findProductUseCase.findAll());
    }

    @PostMapping
    public ResponseEntity<ProductResourceDto> createProduct(@RequestBody ProductCreateDto dto) {
        return ResponseEntity.ok(this.createProductUseCase.createProduct(dto));
    }
}
