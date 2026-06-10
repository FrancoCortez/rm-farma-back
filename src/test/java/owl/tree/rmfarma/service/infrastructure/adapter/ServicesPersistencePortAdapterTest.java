package owl.tree.rmfarma.service.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
import owl.tree.rmfarma.service.infrastructure.entities.Services;
import owl.tree.rmfarma.service.infrastructure.mappers.ServicesMapper;
import owl.tree.rmfarma.service.infrastructure.repository.ServicesRepository;

@ExtendWith(MockitoExtension.class)
class ServicesPersistencePortAdapterTest {

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";
    private static final String ID_OTHER = "550e8400-e29b-41d4-a716-446655440001";

    @Mock
    private ServicesRepository servicesRepository;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private ServicesPersistencePortAdapter adapter;

    @Test
    void findByIdReturnsEmptyOptionalWhenIdIsNull() {
        Optional<Services> result = adapter.findById(null);
        assertThat(result).isEmpty();
        verify(servicesRepository, never()).findById(any());
    }

    @Test
    void findByIdReturnsEmptyOptionalWhenIdIsBlank() {
        Optional<Services> result = adapter.findById("   ");
        assertThat(result).isEmpty();
        verify(servicesRepository, never()).findById(any());
    }

    @Test
    void findByIdReturnsEmptyOptionalWhenRepositoryMisses() {
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.empty());

        Optional<Services> result = adapter.findById(ID_UUID);

        assertThat(result).isEmpty();
        verify(servicesRepository, times(1)).findById(ID_UUID);
    }

    @Test
    void findByIdReturnsOptionalOfEntityWhenRepositoryHits() {
        Services entity = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.of(entity));

        Optional<Services> result = adapter.findById(ID_UUID);

        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(entity);
    }

    @Test
    void findResourceByIdReturnsEmptyOptionalWhenIdIsBlank() {
        assertThat(adapter.findResourceById("   ")).isEmpty();
        verify(servicesRepository, never()).findById(any());
    }

    @Test
    void findResourceByIdReturnsEmptyWhenRepositoryMisses() {
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.empty());

        assertThat(adapter.findResourceById(ID_UUID)).isEmpty();
    }

    @Test
    void findResourceByIdReturnsMappedDtoWhenRepositoryHits() {
        Services entity = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto dto =
                owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto.builder()
                        .id(ID_UUID)
                        .code("SRV-001")
                        .description("Checkup")
                        .enabled(true)
                        .build();
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        Optional<owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto> result =
                adapter.findResourceById(ID_UUID);

        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(dto);
    }

    @Test
    void saveDelegatesToRepository() {
        Services input = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        Services saved = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.save(input)).thenReturn(saved);

        Services result = adapter.save(input);

        assertThat(result).isSameAs(saved);
        verify(servicesRepository, times(1)).save(input);
    }

    @Test
    void findEnabledByIdReturnsEmptyWhenIdIsNull() {
        assertThat(adapter.findEnabledById(null)).isEmpty();
        verify(servicesRepository, never()).findById(any());
    }

    @Test
    void findEnabledByIdReturnsEmptyWhenRepositoryMisses() {
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.empty());

        Optional<Services> result = adapter.findEnabledById(ID_UUID);

        assertThat(result).isEmpty();
    }

    @Test
    void findEnabledByIdReturnsEmptyWhenEntityIsDisabled() {
        Services entity = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(false)
                .build();
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.of(entity));

        Optional<Services> result = adapter.findEnabledById(ID_UUID);

        assertThat(result).isEmpty();
    }

    @Test
    void findEnabledByIdReturnsEntityWhenRepositoryHitsAndEnabled() {
        Services entity = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.of(entity));

        Optional<Services> result = adapter.findEnabledById(ID_UUID);

        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(entity);
    }

    @Test
    void disableByIdFlipsEnabledToFalseAndSaves() {
        Services entity = Services.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.of(entity));
        when(servicesRepository.save(any(Services.class))).thenAnswer(inv -> inv.getArgument(0));

        adapter.disableById(ID_UUID);

        assertThat(entity.getEnabled()).isFalse();
        verify(servicesRepository, times(1)).save(entity);
    }

    @Test
    void disableByIdIsNoOpWhenServiceDoesNotExist() {
        when(servicesRepository.findById(ID_UUID))
                .thenReturn(Optional.empty());

        adapter.disableById(ID_UUID);

        verify(servicesRepository, never()).save(any());
    }

    @Test
    void findAllReturnsMappedDtosOfEnabledServices() {
        Services s1 = Services.builder().id("1").code("A").description("a").enabled(true).build();
        Services s2 = Services.builder().id("2").code("B").description("b").enabled(true).build();
        when(servicesRepository.findAllByEnabledTrue()).thenReturn(java.util.List.of(s1, s2));
        when(servicesMapper.toServiceResourceDto(s1))
                .thenReturn(owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto.builder()
                        .id("1").code("A").description("a").enabled(true).build());
        when(servicesMapper.toServiceResourceDto(s2))
                .thenReturn(owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto.builder()
                        .id("2").code("B").description("b").enabled(true).build());

        var result = adapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getCode()).isEqualTo("A");
        assertThat(result.get(1).getCode()).isEqualTo("B");
    }

    @Test
    void existsByCodeAndIdNotDelegatesToRepository() {
        when(servicesRepository.existsByCodeAndIdNot("SRV-001", ID_OTHER)).thenReturn(true);
        assertThat(adapter.existsByCodeAndIdNot("SRV-001", ID_OTHER)).isTrue();
        verify(servicesRepository, times(1)).existsByCodeAndIdNot("SRV-001", ID_OTHER);
    }

    @Test
    void existsByDescriptionAndIdNotDelegatesToRepository() {
        when(servicesRepository.existsByDescriptionAndIdNot("Checkup", ID_OTHER)).thenReturn(false);
        assertThat(adapter.existsByDescriptionAndIdNot("Checkup", ID_OTHER)).isFalse();
        verify(servicesRepository, times(1)).existsByDescriptionAndIdNot("Checkup", ID_OTHER);
    }
}
