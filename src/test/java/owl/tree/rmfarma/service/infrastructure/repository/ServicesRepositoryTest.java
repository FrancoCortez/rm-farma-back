package owl.tree.rmfarma.service.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

@DataJpaTest
@AutoConfigureTestDatabase
class ServicesRepositoryTest {

    @Autowired
    private ServicesRepository servicesRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void existsByCodeReturnsTrueWhenCodeExists() {
        Services row = Services.builder()
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByCode("SRV-001")).isTrue();
    }

    @Test
    void existsByCodeReturnsFalseWhenCodeDoesNotExist() {
        assertThat(servicesRepository.existsByCode("MISSING")).isFalse();
    }

    @Test
    void existsByDescriptionReturnsTrueWhenDescriptionExists() {
        Services row = Services.builder()
                .code("SRV-002")
                .description("Checkup")
                .enabled(true)
                .build();
        entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByDescription("Checkup")).isTrue();
    }

    @Test
    void findByDescriptionAndEnabledTrueReturnsRowWhenEnabled() {
        Services row = Services.builder()
                .code("SRV-003")
                .description("Blood pressure")
                .enabled(true)
                .build();
        entityManager.persistAndFlush(row);

        Optional<Services> found = servicesRepository.findByDescriptionAndEnabledTrue("Blood pressure");
        assertThat(found).isPresent();
        assertThat(found.get().getCode()).isEqualTo("SRV-003");
    }

    @Test
    void findByDescriptionAndEnabledTrueReturnsEmptyWhenDisabled() {
        Services row = Services.builder()
                .code("SRV-004")
                .description("Disabled service")
                .enabled(false)
                .build();
        entityManager.persistAndFlush(row);

        assertThat(servicesRepository.findByDescriptionAndEnabledTrue("Disabled service")).isEmpty();
    }

    @Test
    void existsByCodeAndIdNotReturnsFalseForSameRow() {
        Services row = Services.builder()
                .code("SRV-005")
                .description("Self exclusion")
                .enabled(true)
                .build();
        Services persisted = entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByCodeAndIdNot("SRV-005", persisted.getId())).isFalse();
    }

    @Test
    void existsByCodeAndIdNotReturnsTrueForDifferentRow() {
        Services row = Services.builder()
                .code("SRV-006")
                .description("Other service")
                .enabled(true)
                .build();
        entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByCodeAndIdNot("SRV-006", "some-other-id")).isTrue();
    }

    @Test
    void existsByDescriptionAndIdNotReturnsFalseForSameRow() {
        Services row = Services.builder()
                .code("SRV-007")
                .description("Self desc")
                .enabled(true)
                .build();
        Services persisted = entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByDescriptionAndIdNot("Self desc", persisted.getId())).isFalse();
    }

    @Test
    void existsByDescriptionAndIdNotReturnsTrueForDifferentRow() {
        Services row = Services.builder()
                .code("SRV-008")
                .description("Taken description")
                .enabled(true)
                .build();
        entityManager.persistAndFlush(row);

        assertThat(servicesRepository.existsByDescriptionAndIdNot("Taken description", "other-id")).isTrue();
    }
}
