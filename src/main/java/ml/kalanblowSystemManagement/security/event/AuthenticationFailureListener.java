package ml.kalanblowSystemManagement.security.event;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import ml.kalanblowSystemManagement.security.bruteForce.BruteForceProtectionService;

@Component
@Slf4j
public abstract class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

	
	 @Resource(name="bruteForceProtectionService")
	    private BruteForceProtectionService bruteForceProtectionService;
	@Override
	public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
		
String username = event.getAuthentication().getName();
bruteForceProtectionService.registerLoginFailure(username);

log.debug(username);
		
	}

}
