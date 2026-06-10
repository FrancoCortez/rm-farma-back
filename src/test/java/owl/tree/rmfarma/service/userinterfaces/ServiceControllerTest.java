package owl.tree.rmfarma.service.userinterfaces;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import owl.tree.rmfarma.service.application.service.CreateServiceUseCase;
import owl.tree.rmfarma.service.application.service.DeleteServiceUseCase;
import owl.tree.rmfarma.service.application.service.FindServiceUseCase;
import owl.tree.rmfarma.service.application.service.GetServiceByIdUseCase;
import owl.tree.rmfarma.service.application.service.UpdateServiceUseCase;
import owl.tree.rmfarma.service.domain.data.service.CreateServiceRequest;
import owl.tree.rmfarma.service.domain.data.service.ServiceResourceDto;
import owl.tree.rmfarma.service.domain.data.service.UpdateServiceRequest;
import owl.tree.rmfarma.shared.config.GlobalExceptionHandler;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;
import owl.tree.rmfarma.shared.exception.domain.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ServiceControllerTest {

    private static final String ID_UUID = "550e8400-e29b-41d4-a716-446655440000";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private FindServiceUseCase findServiceUseCase;

    @Mock
    private CreateServiceUseCase createServiceUseCase;

    @Mock
    private UpdateServiceUseCase updateServiceUseCase;

    @Mock
    private DeleteServiceUseCase deleteServiceUseCase;

    @Mock
    private GetServiceByIdUseCase getServiceByIdUseCase;

    @InjectMocks
    private ServiceController serviceController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(serviceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(converter)
                .build();
    }

    @Test
    void findAllReturnsList() throws Exception {
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(findServiceUseCase.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].code").value("SRV-001"));
    }

    @Test
    void createReturns201OnValidBody() throws Exception {
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Checkup")
                .enabled(true)
                .build();
        when(createServiceUseCase.create(any())).thenReturn(dto);

        String body = objectMapper.writeValueAsString(new CreateServiceRequest("SRV-001", "Checkup"));

        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("SRV-001"))
                .andExpect(jsonPath("$.enabled").value(true));
    }

    @Test
    void createReturns400OnBlankCode() throws Exception {
        String body = objectMapper.writeValueAsString(new CreateServiceRequest("", "Checkup"));

        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("code")));
    }

    @Test
    void createReturns400OnOversizeDescription() throws Exception {
        String longDescription = "X".repeat(101);
        String body = objectMapper.writeValueAsString(new CreateServiceRequest("SRV-001", longDescription));

        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("description")));
    }

    @Test
    void createReturns409OnDuplicateCode() throws Exception {
        when(createServiceUseCase.create(any()))
                .thenThrow(new ExistsException("code", "Service", "SRV-001"));

        String body = objectMapper.writeValueAsString(new CreateServiceRequest("SRV-001", "Checkup"));

        mockMvc.perform(post("/api/v1/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Resource already exists"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void updateReturns200OnValidBody() throws Exception {
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID)
                .code("SRV-001")
                .description("Updated")
                .enabled(true)
                .build();
        when(updateServiceUseCase.update(any(), any())).thenReturn(dto);

        String body = objectMapper.writeValueAsString(new UpdateServiceRequest("SRV-001", "Updated"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/services/" + ID_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SRV-001"))
                .andExpect(jsonPath("$.description").value("Updated"));
    }

    @Test
    void updateReturns409OnCrossRowDuplicate() throws Exception {
        when(updateServiceUseCase.update(any(), any()))
                .thenThrow(new ExistsException("description", "Service", "Taken"));

        String body = objectMapper.writeValueAsString(new UpdateServiceRequest("SRV-001", "Taken"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/services/" + ID_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void updateReturns400OnBlankCode() throws Exception {
        String body = objectMapper.writeValueAsString(new UpdateServiceRequest("", "Updated"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/api/v1/services/" + ID_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void deleteReturns204OnExisting() throws Exception {
        org.mockito.Mockito.doNothing().when(deleteServiceUseCase).deleteById(ID_UUID);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/services/" + ID_UUID))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns404WhenMissing() throws Exception {
        org.mockito.Mockito.doThrow(new NotFoundException("Service", "MISSING"))
                .when(deleteServiceUseCase).deleteById("MISSING");

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/api/v1/services/MISSING"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void getByIdReturns200WhenFound() throws Exception {
        ServiceResourceDto dto = ServiceResourceDto.builder()
                .id(ID_UUID).code("SRV-001").description("Checkup").enabled(true).build();
        when(getServiceByIdUseCase.findById(ID_UUID)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/services/" + ID_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("SRV-001"));
    }

    @Test
    void getByIdReturns404WhenMissing() throws Exception {
        when(getServiceByIdUseCase.findById("MISSING"))
                .thenThrow(new NotFoundException("Service", "MISSING"));

        mockMvc.perform(get("/api/v1/services/MISSING"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }
}
