package owl.tree.rmfarma.manufacture.userinterfaces;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import owl.tree.rmfarma.manufacture.application.masterorder.CreateMasterOrderUseCase;
import owl.tree.rmfarma.manufacture.application.masterorder.FindMasterOrderUseCase;
import owl.tree.rmfarma.manufacture.application.masterorder.data.MasterOrderCreateResourceUseCaseDto;
import owl.tree.rmfarma.manufacture.domain.data.masterorder.MasterOrderResourceDto;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/master-order")
@RequiredArgsConstructor
@Slf4j
public class MasterOrderController {

    private final FindMasterOrderUseCase findMasterOrderUseCase;
    private final CreateMasterOrderUseCase createMasterOrderUseCase;

    @GetMapping()
    public ResponseEntity<List<MasterOrderResourceDto>> findAll(
            @RequestParam(required = false) OffsetDateTime searchDay,
            @RequestParam(required = false) String searchIdentification
    ) {
        return ResponseEntity.ok(this.findMasterOrderUseCase.findAll(searchDay, searchIdentification));
    }

    @PostMapping
    public ResponseEntity<MasterOrderResourceDto> create(@RequestBody List<MasterOrderCreateResourceUseCaseDto> masterOrderCreateResourceUseCaseDto) {
        return ResponseEntity.ok(this.createMasterOrderUseCase.create(masterOrderCreateResourceUseCaseDto));
    }
}
