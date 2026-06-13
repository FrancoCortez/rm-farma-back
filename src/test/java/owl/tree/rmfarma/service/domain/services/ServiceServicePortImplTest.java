package owl.tree.rmfarma.service.domain.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceServicePortImplTest {

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private ServiceServicePortImpl impl;

    @Test
    void findByIdReturnsMappedDto() {
        Services entity = Services.builder().id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        ServiceResourceDto dto = ServiceResourceDto.builder().id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        assertThat(impl.findById(ID_UUID)).isSameAs(dto);
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findById("MISSING")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> impl.findById("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");
    }

    @Test
    void createTrimsAndDelegates() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(false);
        when(servicesPersistencePort.existsByDescription("Checkup")).thenReturn(false);
        Services toPersist = Services.builder().code("SRV-001").description("Checkup").enabled(true).build();
        Services persisted = Services.builder().id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServices(any())).thenReturn(toPersist);
        when(servicesPersistencePort.save(toPersist)).thenReturn(persisted);
        ServiceResourceDto dto = ServiceResourceDto.builder().id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServiceResourceDto(persisted)).thenReturn(dto);

        assertThat(impl.create(new CreateServiceRequest("  SRV-001  ", "  Checkup  "))).isSameAs(dto);
        verify(servicesPersistencePort, times(1)).existsByCode("SRV-001");
        verify(servicesPersistencePort, times(1)).existsByDescription("Checkup");
    }

    @Test
    void createThrowsExistsOnCodeConflict() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(true);
        assertThatThrownBy(() -> impl.create(new CreateServiceRequest("SRV-001", "X")))
                .isInstanceOf(ExistsException.class);
        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void deleteByIdThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findEnabledById("MISSING")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> impl.deleteById("MISSING"))
                .isInstanceOf(NotFoundException.class);
        verify(servicesPersistencePort, never()).disableById(any());
    }

    @Test
    void deleteByIdDisablesWhenPresent() {
        when(servicesPersistencePort.findEnabledById(ID_UUID)).thenReturn(Optional.of(
                Services.builder().id(ID_UUID).code("SRV-001").build()));
        impl.deleteById(ID_UUID);
        verify(servicesPersistencePort, times(1)).disableById(ID_UUID);
    }
}
