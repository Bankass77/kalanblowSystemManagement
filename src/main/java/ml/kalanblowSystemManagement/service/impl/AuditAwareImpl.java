
package ml.kalanblowSystemManagement.service.impl;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import ml.kalanblowSystemManagement.model.User;

@Component
public class AuditAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        User principal =
                (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(principal.getId());
    }
}
