package owl.tree.rmfarma.report.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.report.domain.data.ChemotherapyPreparationFormDto;
import owl.tree.rmfarma.report.domain.data.RecipeBookDto;
import owl.tree.rmfarma.report.domain.ports.api.GeneralReportServicePort;
import owl.tree.rmfarma.report.domain.ports.spi.GeneralReportPersistencePort;
import owl.tree.rmfarma.shared.comparer.FractionComparator;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GeneralReportServiceImpl implements GeneralReportServicePort {
    private final GeneralReportPersistencePort generalReportPersistencePort;

    public List<RecipeBookDto> recipeBookReport (OffsetDateTime startDate, OffsetDateTime endDate) {
        return generalReportPersistencePort.recipeBook(startDate, endDate)
                .stream()
                .sorted(Comparator.comparing(RecipeBookDto::getMasterRecord, new FractionComparator()).reversed())
                .toList();
    }

    public List<ChemotherapyPreparationFormDto> chemotherapyPreparationFormReport (OffsetDateTime startDate, OffsetDateTime endDate) {
        return generalReportPersistencePort.chemotherapyPreparationForm(startDate, endDate)
                .stream()
                .sorted(Comparator.comparing(ChemotherapyPreparationFormDto::getMasterRecord, new FractionComparator()))
                .toList();
    }
}
