package owl.tree.rmfarma.product.userinterfaces;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.product.application.product.FindProductUseCase;
import owl.tree.rmfarma.product.domain.data.product.ProductResourceDto;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final FindProductUseCase findProductUseCase;

    @GetMapping
    public ResponseEntity<List<ProductResourceDto>> findAll() {
        return ResponseEntity.ok(this.findProductUseCase.findAll());
    }
}
