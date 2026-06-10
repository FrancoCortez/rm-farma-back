package owl.tree.rmfarma.service.infrastructure.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.service.infrastructure.entities.Services;

class ServicesMapperTest {

    private final ServicesMapper mapper = Mappers.getMapper(ServicesMapper.class);

    @Test
    void toServicesMapsCodeAndDescription() {
        CreateServiceRequest request = new CreateServiceRequest("SRV-001", "Checkup");
        Services entity = mapper.toServices(request);
        assertThat(entity.getCode()).isEqualTo("SRV-001");
        assertThat(entity.getDescription()).isEqualTo("Checkup");
        assertThat(entity.getEnabled()).isTrue();
    }

    @Test
    void toServiceResourceDtoPreservesEnabledFlag() {
        Services entity = Services.builder()
                .id("uuid-1")
                .code("SRV-001")
                .description("Checkup")
                .enabled(false)
                .build();

        ServiceResourceDto dto = mapper.toServiceResourceDto(entity);

        assertThat(dto.getEnabled()).isFalse();
    }

    @Test
    void updateEntityFromRequestOverwritesCodeAndDescription() {
        Services entity = Services.builder()
                .id("uuid-1")
                .code("OLD-CODE")
                .description("Old desc")
                .enabled(true)
                .build();
        UpdateServiceRequest request = new UpdateServiceRequest("NEW-CODE", "New desc");

        mapper.updateEntityFromRequest(request, entity);

        assertThat(entity.getCode()).isEqualTo("NEW-CODE");
        assertThat(entity.getDescription()).isEqualTo("New desc");
    }

    @Test
    void updateEntityFromRequestLeavesIdAndEnabledUntouched() {
        Services entity = Services.builder()
                .id("uuid-1")
                .code("OLD-CODE")
                .description("Old desc")
                .enabled(true)
                .build();
        UpdateServiceRequest request = new UpdateServiceRequest("NEW-CODE", "New desc");

        mapper.updateEntityFromRequest(request, entity);

        assertThat(entity.getId()).isEqualTo("uuid-1");
        assertThat(entity.getEnabled()).isTrue();
    }
}
