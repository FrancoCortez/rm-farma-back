package owl.tree.rmfarma.service.application.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
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
import owl.tree.rmfarma.service.domain.ports.spi.ServicesPersistencePort;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@ExtendWith(MockitoExtension.class)
class DeleteServiceUseCaseTest {

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @InjectMocks
    private DeleteServiceUseCase deleteServiceUseCase;

    @Test
    void deleteThrowsNotFoundWhenServiceMissing() {
        when(servicesPersistencePort.findEnabledByCode("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteServiceUseCase.deleteByCode("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(servicesPersistencePort, never()).disableByCode("MISSING");
    }

    @Test
    void deleteDisablesServiceWhenPresent() {
        when(servicesPersistencePort.findEnabledByCode("SRV-001")).thenReturn(Optional.of(
                owl.tree.rmfarma.service.infrastructure.entities.Services.builder().code("SRV-001").build()));
        doNothing().when(servicesPersistencePort).disableByCode("SRV-001");

        deleteServiceUseCase.deleteByCode("SRV-001");

        verify(servicesPersistencePort, times(1)).disableByCode("SRV-001");
    }
}
