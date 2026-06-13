package owl.tree.rmfarma.doctor.application.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.api.DoctorServicePort;

@Component
@RequiredArgsConstructor
public class DeleteDoctorUseCase {
    private final DoctorServicePort doctorServicePort;

    public DoctorResourceDto delete(String id) {
        return this.doctorServicePort.deleteDoctor(id);
    }
}
