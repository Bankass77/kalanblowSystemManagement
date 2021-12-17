package ml.kalanblowSystemManagement.security.event;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.security.bruteForce.BruteForceProtectionService;

@Component
@Slf4j
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent>  {

	 @Resource(name="bruteForceProtectionService")
	    private BruteForceProtectionService bruteForceProtectionService;

	    @Override
	    public void onApplicationEvent(AuthenticationSuccessEvent event) {
	        String username= event.getAuthentication().getName();
	        log.info("********* login successful for user {} ", username);
	        bruteForceProtectionService.resetBruteForceCounter(username);
	    }
}
