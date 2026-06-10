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

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    @Mock
    private ServicesPersistencePort servicesPersistencePort;

    @InjectMocks
    private DeleteServiceUseCase deleteServiceUseCase;

    @Test
    void deleteThrowsNotFoundWhenServiceMissing() {
        when(servicesPersistencePort.findEnabledById("MISSING")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteServiceUseCase.deleteById("MISSING"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("MISSING");

        verify(servicesPersistencePort, never()).disableById("MISSING");
    }

    @Test
    void deleteDisablesServiceWhenPresent() {
        when(servicesPersistencePort.findEnabledById(ID_UUID)).thenReturn(Optional.of(
                owl.tree.rmfarma.service.infrastructure.entities.Services.builder().id(ID_UUID).code("SRV-001").build()));
        doNothing().when(servicesPersistencePort).disableById(ID_UUID);

        deleteServiceUseCase.deleteById(ID_UUID);

        verify(servicesPersistencePort, times(1)).disableById(ID_UUID);
    }
}
