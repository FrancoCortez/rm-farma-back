package owl.tree.rmfarma.domain.application.hospitalunit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.hospitalunit.HospitalUnitResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.HospitalUnitServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindHospitalUnitUseCase {

    private final HospitalUnitServicePort hospitalUnitServicePort;

    public List<HospitalUnitResourceDto> findAll() {
        return this.hospitalUnitServicePort.findAll();
    }
}
