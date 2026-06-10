package owl.tree.rmfarma.service.domain.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ServiceServicePortImplTest {

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private ServiceServicePortImpl impl;

    @Test
    void findByCodeReturnsMappedDto() {
        Services entity = Services.builder().id("1").code("SRV-001").description("Checkup").enabled(true).build();
        ServiceResourceDto dto = ServiceResourceDto.builder().id("1").code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesPersistencePort.findByCode("SRV-001")).thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        assertThat(impl.findByCode("SRV-001")).isSameAs(dto);
    }

    @Test
    void findByCodeThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findByCode("MISSING")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> impl.findByCode("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");
    }

    @Test
    void createTrimsAndDelegates() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(false);
        when(servicesPersistencePort.existsByDescription("Checkup")).thenReturn(false);
        Services toPersist = Services.builder().code("SRV-001").description("Checkup").enabled(true).build();
        Services persisted = Services.builder().id("1").code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServices(any())).thenReturn(toPersist);
        when(servicesPersistencePort.save(toPersist)).thenReturn(persisted);
        ServiceResourceDto dto = ServiceResourceDto.builder().id("1").code("SRV-001").description("Checkup").enabled(true).build();
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
    void deleteByCodeThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findEnabledByCode("MISSING")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> impl.deleteByCode("MISSING"))
                .isInstanceOf(NotFoundException.class);
        verify(servicesPersistencePort, never()).disableByCode(any());
    }

    @Test
    void deleteByCodeDisablesWhenPresent() {
        when(servicesPersistencePort.findEnabledByCode("SRV-001")).thenReturn(Optional.of(
                Services.builder().code("SRV-001").build()));
        impl.deleteByCode("SRV-001");
        verify(servicesPersistencePort, times(1)).disableByCode("SRV-001");
    }
}
