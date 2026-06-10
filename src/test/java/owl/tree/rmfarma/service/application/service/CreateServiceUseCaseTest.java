package owl.tree.rmfarma.service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class CreateServiceUseCaseTest {

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private CreateServiceUseCase createServiceUseCase;

    @Test
    void createTrimsValuesBeforeUniquenessCheck() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(false);
        when(servicesPersistencePort.existsByDescription("Blood pressure")).thenReturn(false);
        Services toPersist = Services.builder().code("SRV-001").description("Blood pressure").enabled(true).build();
        Services persisted = Services.builder().id("uuid-1").code("SRV-001").description("Blood pressure").enabled(true).build();
        when(servicesMapper.toServices(any(CreateServiceRequest.class))).thenReturn(toPersist);
        when(servicesPersistencePort.save(toPersist)).thenReturn(persisted);
        ServiceResourceDto resource = ServiceResourceDto.builder().id("uuid-1").code("SRV-001").description("Blood pressure").enabled(true).build();
        when(servicesMapper.toServiceResourceDto(persisted)).thenReturn(resource);

        ServiceResourceDto result = createServiceUseCase.create(
                new CreateServiceRequest("  SRV-001  ", "  Blood pressure  "));

        assertThat(result).isSameAs(resource);
        verify(servicesPersistencePort, times(1)).existsByCode("SRV-001");
        verify(servicesPersistencePort, times(1)).existsByDescription("Blood pressure");
    }

    @Test
    void createThrowsExistsExceptionOnCodeCollision() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(true);

        assertThatThrownBy(() -> createServiceUseCase.create(
                new CreateServiceRequest("SRV-001", "Anything")))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("code")
                .hasMessageContaining("SRV-001");

        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void createThrowsExistsExceptionOnDescriptionCollision() {
        when(servicesPersistencePort.existsByCode("SRV-NEW")).thenReturn(false);
        when(servicesPersistencePort.existsByDescription("Checkup")).thenReturn(true);

        assertThatThrownBy(() -> createServiceUseCase.create(
                new CreateServiceRequest("SRV-NEW", "Checkup")))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("description");

        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void createReturnsMappedDtoOnHappyPath() {
        when(servicesPersistencePort.existsByCode("SRV-001")).thenReturn(false);
        when(servicesPersistencePort.existsByDescription("Checkup")).thenReturn(false);
        Services toPersist = Services.builder().code("SRV-001").description("Checkup").enabled(true).build();
        Services persisted = Services.builder().id("uuid-1").code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServices(any(CreateServiceRequest.class))).thenReturn(toPersist);
        when(servicesPersistencePort.save(toPersist)).thenReturn(persisted);
        ServiceResourceDto resource = ServiceResourceDto.builder()
                .id("uuid-1").code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServiceResourceDto(persisted)).thenReturn(resource);

        ServiceResourceDto result = createServiceUseCase.create(
                new CreateServiceRequest("SRV-001", "Checkup"));

        assertThat(result).isSameAs(resource);
    }
}
