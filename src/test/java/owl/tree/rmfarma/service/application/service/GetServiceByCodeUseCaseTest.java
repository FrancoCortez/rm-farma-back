package owl.tree.rmfarma.service.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class GetServiceByCodeUseCaseTest {

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private GetServiceByCodeUseCase getServiceByCodeUseCase;

    @Test
    void findByCodeReturnsMappedDtoWhenPresent() {
        Services entity = Services.builder()
                .id("uuid-1").code("SRV-001").description("Checkup").enabled(true).build();
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id("uuid-1").code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesPersistencePort.findByCode("SRV-001")).thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        ServiceResourceDto result = getServiceByCodeUseCase.findByCode("SRV-001");

        assertThat(result).isSameAs(dto);
    }

    @Test
    void findByCodeThrowsNotFoundWhenMissing() {
        when(servicesPersistencePort.findByCode("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getServiceByCodeUseCase.findByCode("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(servicesMapper, never()).toServiceResourceDto((Services) org.mockito.ArgumentMatchers.any());
    }
}
