package owl.tree.rmfarma.shared.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Configuration
public class AuditConfig implements AuditorAware<String> {

    @Override
    public java.util.Optional<String> getCurrentAuditor() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String auditor = request.getHeader("X-Auditor");
            if (auditor != null && !auditor.isEmpty()) {
                return Optional.of(auditor);
            }
        }
        return Optional.of("system"); // Valor por defecto
    }
}
