package owl.tree.rmfarma.domain.application.clinic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.domain.domain.data.clinic.ClinicResourceDto;
import owl.tree.rmfarma.domain.domain.ports.api.ClinicServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindClinicUseCase {
    private final ClinicServicePort clinicServicePort;

    public List<ClinicResourceDto> findAll() {
        return this.clinicServicePort.findAll();
    }
}
