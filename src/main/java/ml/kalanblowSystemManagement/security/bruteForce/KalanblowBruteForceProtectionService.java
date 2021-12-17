package ml.kalanblowSystemManagement.security.bruteForce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import ml.kalanblowSystemManagement.dto.model.UserDto;
import ml.kalanblowSystemManagement.service.UserService;

@Service("bruteForceProtectionService")
public class KalanblowBruteForceProtectionService implements BruteForceProtectionService {
	
	@Autowired
	private UserService userService;

	 @Value("${jdj.security.failedlogin.count}")
	private int maxFailedLogins;
	
	@Value("${jdj.brute.force.cache.max}")
	private int cacheMaxLimit;
	
	
	private final ConcurrentHashMap<String, FailedLogin> cache;
	public KalanblowBruteForceProtectionService() {
		this.cache=new ConcurrentHashMap<>(cacheMaxLimit);
	}
	


	@Override
	public void registerLoginFailure(String username) {
		
		Optional<UserDto> userDto= getUser(username);
		
		if (userDto.isPresent() && !userDto.get().isLoginDisabled()) {
			int failedCoounter=userDto.get().getFailedLoginAttempts();
			if (maxFailedLogins<failedCoounter +1) {
				userDto.get().setLoginDisabled(true); //disabling the account
			}else {
				
				//let's update counter
				userDto.get().setFailedLoginAttempts(failedCoounter+1);
			}
			
			userService.signup(userDto.get());
		}
		
		
	}

	
	protected FailedLogin getFailedLogin(final String username) {
		
		FailedLogin failedLogin=cache.get(username.toLowerCase());
		if (failedLogin ==null) {
			//setup the initial data
			failedLogin= new FailedLogin(0, LocalDateTime.now());
			cache.put(username.toLowerCase(), failedLogin);
			
			if (cache.size() > cacheMaxLimit) {
				//add the logic to remove the key by timestamp
			}
		}
		return failedLogin;
	}
	
	
	private Optional<UserDto> getUser(final String username) {
		
		return userService.findUserByEmail(username).ofNullable(null);
	}



	@Override
	public void resetBruteForceCounter(String username) {
		
		
		Optional<UserDto> userDto = getUser(username);
		if (userDto.isPresent() ) {
			
			userDto.get().setFailedLoginAttempts(0);
			userDto.get().setLoginDisabled(false);
			userService.signup(userDto.get());
		}

	}

	@Override
	public boolean isBruteForceAttack(String username) {
		Optional<UserDto> userDto = getUser(username);
		
		if (userDto.isPresent()) {
			
			return userDto.get().getFailedLoginAttempts()>=maxFailedLogins?true:false;
		}
		return false;
	}

	
	public class FailedLogin{

        private int count;
        private LocalDateTime date;

        public FailedLogin() {
            this.count = 0;
            this.date = LocalDateTime.now();
        }

        public FailedLogin(int count, LocalDateTime date) {
            this.count = count;
            this.date = date;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public LocalDateTime getDate() {
            return date;
        }

        public void setDate(LocalDateTime date) {
            this.date = date;
        }
    }
}
