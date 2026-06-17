package owl.tree.rmfarma.report.domain.ports.api;

import owl.tree.rmfarma.report.domain.data.ChemotherapyPreparationFormDto;
import owl.tree.rmfarma.report.domain.data.RecipeBookDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface GeneralReportServicePort {
    List<RecipeBookDto> recipeBookReport (OffsetDateTime startDate, OffsetDateTime endDate);
    List<ChemotherapyPreparationFormDto> chemotherapyPreparationFormReport (OffsetDateTime startDate, OffsetDateTime endDate);
}
