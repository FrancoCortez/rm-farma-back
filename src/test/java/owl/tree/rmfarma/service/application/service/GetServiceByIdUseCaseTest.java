package owl.tree.rmfarma.service.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetServiceByIdUseCaseTest {

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private GetServiceByIdUseCase getServiceByIdUseCase;

    @Test
    void findByIdReturnsMappedDtoWhenPresent() {
        Services entity = Services.builder()
                .id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        ServiceResourceDto result = getServiceByIdUseCase.findById(ID_UUID);

        assertThat(result).isSameAs(dto);
    }

    @Test
    void findByIdThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findById("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getServiceByIdUseCase.findById("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(servicesMapper, never()).toServiceResourceDto((Services) org.mockito.ArgumentMatchers.any());
    }
}
