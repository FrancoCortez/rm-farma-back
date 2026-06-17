package owl.tree.rmfarma.report.userinterfaces;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.report.domain.data.ChemotherapyPreparationFormDto;
import owl.tree.rmfarma.report.domain.data.RecipeBookDto;
import owl.tree.rmfarma.report.domain.ports.api.GeneralReportServicePort;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/general-reports")
@RequiredArgsConstructor
public class GeneralReportController {
    private final GeneralReportServicePort generalReportServicePort;

    @GetMapping("/recipe-book")
    public ResponseEntity<List<RecipeBookDto>> getRecipeBookReport(@RequestParam(required = false) OffsetDateTime startDate,
                                                                   @RequestParam(required = false) OffsetDateTime endDate) {
        return ResponseEntity.ok(generalReportServicePort.recipeBookReport(startDate, endDate));
    }

    @GetMapping("/chemotherapy-preparation-form")
    public ResponseEntity<List<ChemotherapyPreparationFormDto>> getChemotherapyPreparationFormReport(@RequestParam(required = false) OffsetDateTime startDate,
                                                                                                      @RequestParam(required = false) OffsetDateTime endDate) {
        return ResponseEntity.ok(generalReportServicePort.chemotherapyPreparationFormReport(startDate, endDate));
    }
}
