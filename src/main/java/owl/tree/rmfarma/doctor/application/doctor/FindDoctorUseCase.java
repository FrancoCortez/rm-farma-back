package owl.tree.rmfarma.doctor.application.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.api.DoctorServicePort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindDoctorUseCase {

    private final DoctorServicePort doctorServicePort;

//    public DoctorResourceDto findByRut(String rut) {
//        return this.doctorServicePort.findByRut(rut);
//    }

    public List<DoctorResourceDto> findAll() {
        return this.doctorServicePort.findAll();
    }

    public DoctorResourceDto findById(String id) {
        return this.doctorServicePort.findById(id);
    }
}
