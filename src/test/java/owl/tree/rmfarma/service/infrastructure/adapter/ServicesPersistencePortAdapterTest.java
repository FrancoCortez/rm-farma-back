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

    @Mock
    private ServicesRepository servicesRepository;

    @Mock
    private ServicesMapper servicesMapper;

    @InjectMocks
    private ServicesPersistencePortAdapter adapter;

    @Test
    void findByCodeReturnsEmptyOptionalWhenCodeIsNull() {
        Optional<Services> result = adapter.findByCode(null);
        assertThat(result).isEmpty();
        verify(servicesRepository, never()).findByCodeAndEnabledTrue(any());
    }

    @Test
    void findByCodeReturnsEmptyOptionalWhenCodeIsBlank() {
        Optional<Services> result = adapter.findByCode("   ");
        assertThat(result).isEmpty();
        verify(servicesRepository, never()).findByCodeAndEnabledTrue(any());
    }

    @Test
    void findByCodeReturnsEmptyOptionalWhenRepositoryMisses() {
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-MISSING"))
                .thenReturn(Optional.empty());

        Optional<Services> result = adapter.findByCode("SRV-MISSING");

        assertThat(result).isEmpty();
        verify(servicesRepository, times(1)).findByCodeAndEnabledTrue("SRV-MISSING");
    }

    @Test
    void findByCodeReturnsOptionalOfEntityWhenRepositoryHits() {
        Services entity = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-001"))
                .thenReturn(Optional.of(entity));

        Optional<Services> result = adapter.findByCode("SRV-001");

        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(entity);
    }

    @Test
    void findResourceByCodeReturnsEmptyOptionalWhenCodeIsBlank() {
        assertThat(adapter.findResourceByCode("   ")).isEmpty();
        verify(servicesRepository, never()).findByCodeAndEnabledTrue(any());
    }

    @Test
    void findResourceByCodeReturnsEmptyWhenRepositoryMisses() {
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-MISSING"))
                .thenReturn(Optional.empty());

        assertThat(adapter.findResourceByCode("SRV-MISSING")).isEmpty();
    }

    @Test
    void findResourceByCodeReturnsMappedDtoWhenRepositoryHits() {
        Services entity = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto dto =
                owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto.builder()
                        .id("uuid-1")
                        .code("SRV-001")
                        .description("Checkup")
                        .enabled(true)
                        .build();
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-001"))
                .thenReturn(Optional.of(entity));
        when(servicesMapper.toServiceResourceDto(entity)).thenReturn(dto);

        Optional<owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto> result =
                adapter.findResourceByCode("SRV-001");

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
    void findEnabledByCodeReturnsEmptyWhenRepositoryMisses() {
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-MISSING"))
                .thenReturn(Optional.empty());

        Optional<Services> result = adapter.findEnabledByCode("SRV-MISSING");

        assertThat(result).isEmpty();
    }

    @Test
    void findEnabledByCodeReturnsEntityWhenRepositoryHits() {
        Services entity = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-001"))
                .thenReturn(Optional.of(entity));

        Optional<Services> result = adapter.findEnabledByCode("SRV-001");

        assertThat(result).isPresent();
        assertThat(result.get()).isSameAs(entity);
    }

    @Test
    void disableByCodeFlipsEnabledToFalseAndSaves() {
        Services entity = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-001"))
                .thenReturn(Optional.of(entity));
        when(servicesRepository.save(any(Services.class))).thenAnswer(inv -> inv.getArgument(0));

        adapter.disableByCode("SRV-001");

        assertThat(entity.getEnabled()).isFalse();
        verify(servicesRepository, times(1)).save(entity);
    }

    @Test
    void disableByCodeIsNoOpWhenServiceDoesNotExist() {
        when(servicesRepository.findByCodeAndEnabledTrue("SRV-MISSING"))
                .thenReturn(Optional.empty());

        adapter.disableByCode("SRV-MISSING");

        verify(servicesRepository, never()).save(any());
    }

    @Test
    void findAllReturnsMappedDtosOfEnabledServices() {
        Services s1 = Services.builder().code("A").description("a").enabled(true).build();
        Services s2 = Services.builder().code("B").description("b").enabled(true).build();
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
}
