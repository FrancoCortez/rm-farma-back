package owl.tree.rmfarma.doctor.application.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorCreateResourceDto;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.api.DoctorServicePort;

@Component
@RequiredArgsConstructor
public class CreateDoctorUseCase {
    private final DoctorServicePort doctorServicePort;

    public DoctorResourceDto create(DoctorCreateResourceDto create) {
        return this.doctorServicePort.createDoctor(create);
    }
}
