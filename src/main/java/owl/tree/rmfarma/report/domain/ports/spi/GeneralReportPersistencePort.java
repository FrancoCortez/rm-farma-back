package owl.tree.rmfarma.report.domain.ports.spi;

import owl.tree.rmfarma.report.domain.data.ChemotherapyPreparationFormDto;
import owl.tree.rmfarma.report.domain.data.RecipeBookDto;

import java.time.OffsetDateTime;
import java.util.List;

public interface GeneralReportPersistencePort {
    List<RecipeBookDto> recipeBook (OffsetDateTime startDate, OffsetDateTime endDate);
    List<ChemotherapyPreparationFormDto> chemotherapyPreparationForm(OffsetDateTime startDate, OffsetDateTime endDate);
}
