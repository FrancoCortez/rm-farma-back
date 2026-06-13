package owl.tree.rmfarma.doctor.application.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorUpdateResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.api.DoctorServicePort;

@Component
@RequiredArgsConstructor
public class UpdateDoctorUseCase {
    private final DoctorServicePort doctorServicePort;

    public DoctorResourceDto update(String id, DoctorUpdateResourceDto update) {
        return this.doctorServicePort.updateDoctor(id, update);
    }
}
