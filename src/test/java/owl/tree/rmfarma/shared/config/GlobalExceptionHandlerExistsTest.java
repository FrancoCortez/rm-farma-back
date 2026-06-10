package owl.tree.rmfarma.shared.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import owl.tree.rmfarma.shared.exception.domain.ExistsException;

class GlobalExceptionHandlerExistsTest {

    private MockMvc mockMvc;

    @RestController
    static class ThrowingController {
        @GetMapping("/throw/exists")
        public String throwExists() {
            throw new ExistsException("code", "Service", "SRV-001");
        }
    }

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setMessageConverters(converter)
                .build();
    }

    @Test
    void existsExceptionMapsTo409Conflict() throws Exception {
        mockMvc.perform(get("/throw/exists"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Resource already exists"))
                .andExpect(jsonPath("$.path").value(org.hamcrest.Matchers.containsString("/throw/exists")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("code")))
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.containsString("SRV-001")));
    }
}
