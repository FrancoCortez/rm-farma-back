package owl.tree.rmfarma.service.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
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
class UpdateServiceUseCaseTest {

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private UpdateServiceUseCase updateServiceUseCase;

    @Test
    void updateThrowsNotFoundExceptionWhenServiceDoesNotExist() {
        when(servicesPersistencePort.findById("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updateServiceUseCase.update("MISSING",
                new UpdateServiceRequest("MISSING", "Anything")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void updateAllowsSelfExclusionForSameCodeAndDescription() {
        Services existing = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(existing));
        when(servicesPersistencePort.save(existing)).thenReturn(existing);
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(servicesMapper.toServiceResourceDto(existing)).thenReturn(dto);

        ServiceResourceDto result = updateServiceUseCase.update(ID_UUID,
                new UpdateServiceRequest("SRV-001", "Checkup"));

        assertThat(result).isSameAs(dto);
        verify(servicesPersistencePort, never()).existsByCodeAndIdNot(any(), any());
        verify(servicesPersistencePort, never()).existsByDescriptionAndIdNot(any(), any());
        verify(servicesPersistencePort, times(1)).save(existing);
    }

    @Test
    void updateThrowsExistsExceptionWhenCodeIsTakenByAnotherService() {
        Services existing = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(existing));
        when(servicesPersistencePort.existsByCodeAndIdNot("SRV-002", ID_UUID)).thenReturn(true);

        assertThatThrownBy(() -> updateServiceUseCase.update(ID_UUID,
                new UpdateServiceRequest("SRV-002", "Checkup")))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("code");

        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void updateThrowsExistsExceptionWhenDescriptionIsTakenByAnotherService() {
        Services existing = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(existing));
        when(servicesPersistencePort.existsByDescriptionAndIdNot("Other", ID_UUID)).thenReturn(true);

        assertThatThrownBy(() -> updateServiceUseCase.update(ID_UUID,
                new UpdateServiceRequest("SRV-001", "Other")))
                .isInstanceOf(ExistsException.class)
                .hasMessageContaining("description");

        verify(servicesPersistencePort, never()).save(any());
    }

    @Test
    void updatePersistsTrimmedValuesAndReturnsMappedDto() {
        Services existing = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Old")
                .enabled(true)
                .build();
        when(servicesPersistencePort.findById(ID_UUID)).thenReturn(Optional.of(existing));
        when(servicesPersistencePort.save(existing)).thenReturn(existing);
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID).code("SRV-001").description("Updated").enabled(true).build();
        when(servicesMapper.toServiceResourceDto(existing)).thenReturn(dto);

        ServiceResourceDto result = updateServiceUseCase.update(ID_UUID,
                new UpdateServiceRequest("  SRV-001  ", "  Updated  "));

        assertThat(result).isSameAs(dto);
        assertThat(existing.getCode()).isEqualTo("SRV-001");
        assertThat(existing.getDescription()).isEqualTo("Updated");
    }
}
