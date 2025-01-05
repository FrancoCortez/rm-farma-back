package owl.tree.rmfarma.doctor.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import owl.tree.rmfarma.doctor.domain.data.doctor.DoctorResourceDto;
import owl.tree.rmfarma.doctor.domain.ports.api.DoctorServicePort;
import owl.tree.rmfarma.doctor.domain.ports.spi.DoctorPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.IsEmptyException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorServicePort {

    private final DoctorPersistencePort doctorPersistencePort;

    public DoctorResourceDto findByRut(String rut) {
        if(rut == null || rut.isEmpty()) throw new IsEmptyException("rut", "Doctor");
        DoctorResourceDto result = this.doctorPersistencePort.findByRut(rut);
        if(result == null) throw new NotFoundException("rut", "Doctor");
        return result;
    }

    @Override
    public List<DoctorResourceDto> findAll() {
        return this.doctorPersistencePort.findAll();
    }

    public DoctorResourceDto findById(String id) {
        if(id == null || id.isEmpty()) throw new IsEmptyException("id", "Doctor");
        DoctorResourceDto result =  this.doctorPersistencePort.findById(id);
        if(result == null) throw new NotFoundException("id", "Doctor");
        return result;
    }
}
